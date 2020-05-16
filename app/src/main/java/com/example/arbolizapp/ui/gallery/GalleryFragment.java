package com.example.arbolizapp.ui.gallery;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.arbolizapp.MainActivity;
import com.example.arbolizapp.MenuActivity;
import com.example.arbolizapp.Profile;
import com.example.arbolizapp.R;
import com.example.arbolizapp.Register;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    ImageView cambiarTelefono, cambiarCorreo, cambiarUsuario, cambiarFoto, fotoPerfil;
    View root;
    private static String URL_UPDATEUSER = "http://192.168.56.1/android_register_login/editUser.php";
    private static String URL_UPDATEEMAIL = "http://192.168.56.1/android_register_login/editEmail.php";
    private static String URL_UPDATEPHONE ="http://192.168.56.1/android_register_login/editPhone.php";
    private static String UPLOAD_URL = "http://192.168.56.1/android_register_login/upload.php";
    private static String URL_PERFIL = "http://192.168.56.1/android_register_login/uploads/";
    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;
    String KEY_IMAGE = "photo";
    String KEY_NOMBRE = "nombre";
    int imageprof;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        root = inflater.inflate(R.layout.fragment_gallery, container, false);

        //final String texto = getArguments().getString("username");

        cambiarUsuario = root.findViewById(R.id.cambiarUsuario);
        cambiarCorreo = root.findViewById(R.id.cambiarCorreo);
        cambiarTelefono = root.findViewById(R.id.cambiarTelefono);
        cambiarFoto = root.findViewById(R.id.cambiarImagen);
        fotoPerfil = root.findViewById(R.id.profilePicture);

        final TextView username = root.findViewById(R.id.datoUsuario2);
        final TextView fullname = root.findViewById(R.id.datofullname2);
        final TextView email = root.findViewById(R.id.datoemail3);
        final TextView phone = root.findViewById(R.id.datophone2);


        SharedPreferences prefs = getActivity().getSharedPreferences("Preferences", 0);
        String usuario = prefs.getString("username", "");
        String nombreCompleto = prefs.getString("fullname", "");
        String correo = prefs.getString("email", "");
        String telefono = prefs.getString("phone", "");
        String foto = prefs.getString("image","");
        final String id_usuario = prefs.getString("id","");

        username.setText(usuario);
        fullname.setText(nombreCompleto);
        email.setText(correo);
        phone.setText(telefono);
        Picasso.get().load(URL_PERFIL+foto+".png").into(fotoPerfil);
        imageprof = Integer.parseInt(foto);

        cambiarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                LayoutInflater inflater1 = getLayoutInflater();

                View view = inflater1.inflate(R.layout.cambiar_usuario, null);

                builder.setView(view);

                final AlertDialog dialog = builder.create();
                dialog.show();
                final EditText nuevoNombreUsuario = view.findViewById(R.id.editUsuario);

                Button guardarNuevoUsername = view.findViewById(R.id.guardarCambioUsuario);
                guardarNuevoUsername.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!nuevoNombreUsuario.getText().toString().isEmpty()){
                            final String nombreUsuario = nuevoNombreUsuario.getText().toString();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATEUSER,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                String success = jsonObject.getString("success");

                                                if (success.equals("1")){
                                                    Toast.makeText(getContext(), "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
                                                    username.setText(nuevoNombreUsuario.getText());
                                                    dialog.dismiss();
                                                } else if (success.equals("2")){
                                                    Toast.makeText(getContext(), "Nombre de usuario existente", Toast.LENGTH_SHORT).show();
                                                }

                                            }catch (JSONException e){
                                                e.printStackTrace();
                                                Toast.makeText(getContext(), "Error al actualizar " +e.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), "Error al actualizar  "+error.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    })
                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("username", nombreUsuario);
                                    params.put("id", id_usuario);
                                    return params;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
                            requestQueue.add(stringRequest);

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Preferences", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", nombreUsuario);
                            editor.commit();
                        } else {
                            nuevoNombreUsuario.setError("Usuario requerido");
                        }
                    }
                });
            }
        });

        cambiarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                LayoutInflater in = getLayoutInflater();

                View view = in.inflate(R.layout.cambiar_correo, null);

                builder.setView(view);

                final AlertDialog dialog = builder.create();
                dialog.show();
                final EditText nuevoCorreo = view.findViewById(R.id.editCorreo);
                final EditText confirmarCorreo = view.findViewById(R.id.confirmarCorreo);

                final Button guardarNuevoCorro = view.findViewById(R.id.guardarCambioCorreo);
                guardarNuevoCorro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String comprobarEmail = nuevoCorreo.getEditableText().toString().trim();
                        final String regex = "(?:[^<>()\\[\\].,;:\\s@\"]+(?:\\.[^<>()\\[\\].,;:\\s@\"]+)*|\"[^\\n\"]+\")@(?:[^<>()\\[\\].,;:\\s@\"]+\\.)+[^<>()\\[\\]\\.,;:\\s@\"]{2,63}";

                        if (nuevoCorreo.getText().toString().equals(confirmarCorreo.getText().toString())
                                && !nuevoCorreo.getText().toString().isEmpty()
                                && !confirmarCorreo.getText().toString().isEmpty()
                                && comprobarEmail.matches(regex)) {
                            final String correoNuevo = nuevoCorreo.getText().toString();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATEEMAIL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                String success = jsonObject.getString("success");

                                                if (success.equals("1")) {
                                                    Toast.makeText(getContext(), "Correo actualizado correctamente", Toast.LENGTH_SHORT).show();
                                                    email.setText(nuevoCorreo.getText());
                                                    dialog.dismiss();
                                                } else if (success.equals("2")) {
                                                    Toast.makeText(getContext(), "Correo actualmente en uso", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Toast.makeText(getContext(), "Error al actualizar" + e.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), "Error al actualizar" + error.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("email", correoNuevo);
                                    params.put("id", id_usuario);
                                    return params;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
                            requestQueue.add(stringRequest);

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Preferences", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", correoNuevo);
                            editor.commit();
                        } else if (nuevoCorreo.getText().toString().isEmpty()) {
                            nuevoCorreo.setError("Correo requerido");
                        } else if (!comprobarEmail.matches(regex)) {
                            nuevoCorreo.setError("Correo no valido");
                        } else if (!nuevoCorreo.getText().toString().equals(confirmarCorreo.getText().toString()) || confirmarCorreo.getText().toString().isEmpty()){
                            nuevoCorreo.setError("Los correos no coinciden");
                        }
                    }
                });
            }
        });

        cambiarTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                LayoutInflater in = getLayoutInflater();

                View view = in.inflate(R.layout.cambiar_telefono, null);

                builder.setView(view);

                final AlertDialog dialog = builder.create();
                dialog.show();
                final EditText nuevoTelefono = view.findViewById(R.id.editTelefono);

                Button guardarNuevoTelefono = view.findViewById(R.id.guardarCambioTelefono);

                guardarNuevoTelefono.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!nuevoTelefono.getText().toString().isEmpty()){
                            final String nuevoNumero = nuevoTelefono.getText().toString();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATEPHONE,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                String success = jsonObject.getString("success");

                                                if (success.equals("1")) {
                                                    Toast.makeText(getContext(), "Telefono actualizado correctamente", Toast.LENGTH_SHORT).show();
                                                    phone.setText(nuevoTelefono.getText());
                                                    dialog.dismiss();
                                                } else if (success.equals("2")) {
                                                    Toast.makeText(getContext(), "Telefono en uso", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Toast.makeText(getContext(), "Error al actualizar" + e.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), "Error al actualizar  "+error.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    })
                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("phone", nuevoNumero);
                                    params.put("id", id_usuario);
                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
                            requestQueue.add(stringRequest);

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Preferences", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("phone", nuevoNumero);
                            editor.commit();
                        } else {
                            nuevoTelefono.setError("Usuario requerido");
                        }
                    }
                });


            }
        });

        cambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                LayoutInflater in = getLayoutInflater();
                View view = in.inflate(R.layout.cambiar_foto, null);
                builder.setView(view);

                final AlertDialog dialog = builder.create();
                dialog.show();

                Button seleccionar = view.findViewById(R.id.seleccionarFoto);
                Button guardarFoto = view.findViewById(R.id.guardarFoto);

                seleccionar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFileChooser();
                    }
                });

                guardarFoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadImage();
                        dialog.dismiss();
                    }
                });
            }
        });

        return root;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodeImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodeImage;
    }

    public void uploadImage(){
        final ProgressDialog cargando = ProgressDialog.show(getContext(), "Subiendo...", "Espera por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cargando.dismiss();
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                fotoPerfil.setImageBitmap(bitmap);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cargando.dismiss();
                Toast.makeText(getContext(), "Favor de seleccionar una imagen", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                SharedPreferences prefs = getActivity().getSharedPreferences("Preferences", 0);
                final String id_usuario = prefs.getString("id","");

                String image = getStringImage(bitmap);
                String nombre = id_usuario;

                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_IMAGE, image);
                params.put(KEY_NOMBRE, nombre);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleccionar una imagen"), PICK_IMAGE_REQUEST);
        SharedPreferences prefs = getActivity().getSharedPreferences("Preferences",0);
        String image = prefs.getString("image", "");

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("image",image);
        editor.commit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=null){
            Uri filePath = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),filePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
