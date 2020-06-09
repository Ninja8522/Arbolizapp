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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

/**
 * Este es el código del fragment de perfil de usuario, conserva el nombre de galllery por que no supe como cambiarle el nombre al archivo,
 * pero en este apartado es donde trabaje el código completo del perfil de usuario.
 */

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    ImageView cambiarTelefono, cambiarCorreo, cambiarUsuario, cambiarFoto, fotoPerfil;
    View root;
    /**
     * Los enlaces abajo corresponden a en orden como estan, editar nombre de usuario, editar correo, editar telefono,
     * subir la imagen a la carpeta donde se almacenan las fotos y por ultimo el enlace que carga la foto correspondiente al usuario que
     * esta accediendo a la app.
     */
    private static String URL_UPDATEUSER = "https://arbolizappgreen.000webhostapp.com/editUser.php";
    private static String URL_UPDATEEMAIL = "https://arbolizappgreen.000webhostapp.com/editEmail.php";
    private static String URL_UPDATEPHONE ="https://arbolizappgreen.000webhostapp.com/editPhone.php";
    private static String UPLOAD_URL = "https://arbolizappgreen.000webhostapp.com/upload.php";
    private static String URL_PERFIL = "https://arbolizappgreen.000webhostapp.com/uploads/";
    Bitmap bitmap;
    /**
     * Estas variables estan relacionadas con la foto de perfil.
     */
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
        /**
         * Al momento de que se crea la vista de este fragment se asigna a las variables de abajo los campos de la imagen de lapiz del diseño de fragment,
         * este es correspondiente a cada uno de los que estan a un lado de aquellas opciones que se pueden cambiar por el usuario.
         * La ultima es para asignar el imageview que se tiene en el diseño a la variable fotoPerfil.
         */

        cambiarUsuario = root.findViewById(R.id.cambiarUsuario);
        cambiarCorreo = root.findViewById(R.id.cambiarCorreo);
        cambiarTelefono = root.findViewById(R.id.cambiarTelefono);
        cambiarFoto = root.findViewById(R.id.cambiarImagen);
        fotoPerfil = root.findViewById(R.id.profilePicture);

        /**
         * En esta parte se relacionan los diferentes TextView con los que estan en el diseño, estos son los encargados de mostrar la informacion
         * que presenta cada uno de los campos correspondientes al usuario que inicio sesion en la app.
         */

        final TextView username = root.findViewById(R.id.datoUsuario2);
        final TextView fullname = root.findViewById(R.id.datofullname2);
        final TextView email = root.findViewById(R.id.datoemail3);
        final TextView phone = root.findViewById(R.id.datophone2);

        /**
         * Aqui se cacha la informacion que se manda desde el activity de login, se asigna a cada variable el valor que se optiene de ese campo.
         */

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
        /**
         * En esta parte se fija la imagen que tiene el usuario en ese momento.
         */
        Picasso.get().load(URL_PERFIL+foto+".png").into(fotoPerfil);
        //imageprof = Integer.parseInt(foto);

        cambiarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Aqui creo un alertdialog para que vaya acorde a los diseños que se hicieron de la app, en este alertdialog solo hay un edittext y
                 * un boton de guardado, cree un diseño de un fragment para este.
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                LayoutInflater inflater1 = getLayoutInflater();

                View view = inflater1.inflate(R.layout.cambiar_usuario, null);

                builder.setView(view);

                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                dialog.show();
                final EditText nuevoNombreUsuario = view.findViewById(R.id.editUsuario);

                Button guardarNuevoUsername = view.findViewById(R.id.guardarCambioUsuario);
                guardarNuevoUsername.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         * En esta parte meto la funcionalidad del boton de guardar, primero compruebo que no el campo de nombre de usuario no este vacio,
                         * en caso de estarlo se manda un mensaje de error para que se ingrese un usuario.
                         */
                        if (!nuevoNombreUsuario.getText().toString().isEmpty()){
                            /**
                             * En caso de que el campo no este vacio, creo una variable donde almaceno el texto del nuevo nombre y realizo un request.
                             */
                            final String nombreUsuario = nuevoNombreUsuario.getText().toString();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATEUSER,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                /**
                                                 * En caso de que la request tenga respuesta verifico que fue lo que llego, en el codigo de php de updateUsername
                                                 * compruebo si en la base de datos hay o no usuario con ese nombre de usuario, dependiendop de que se optenga
                                                 * es el mensaje que se despliega
                                                 */
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
                                    /**
                                     * Aqui le paso los datos correspondientes al codigo php para que lleve los datos a alterar en la base de datos.
                                     */
                                    Map<String, String> params = new HashMap<>();
                                    params.put("username", nombreUsuario);
                                    params.put("id", id_usuario);
                                    return params;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
                            requestQueue.add(stringRequest);
                            /**
                             * Aqui actualizo lo que se tiene en el SharedPreferences, para que al momento de salir de el fragment de perfil no se carge de nuevo
                             * la anterior del SharedPreferences.
                             */
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
                /**
                 * Esta parte no es diferente de lo anterior, solo cambia el hecho de que aqui hay dos campos a llenar, uno de
                 * correo nuevo y otro de confirmar.
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                LayoutInflater in = getLayoutInflater();

                View view = in.inflate(R.layout.cambiar_correo, null);

                builder.setView(view);

                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                dialog.show();
                final EditText nuevoCorreo = view.findViewById(R.id.editCorreo);
                final EditText confirmarCorreo = view.findViewById(R.id.confirmarCorreo);

                final Button guardarNuevoCorro = view.findViewById(R.id.guardarCambioCorreo);
                guardarNuevoCorro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         * Aqui tambien asigno a una variable el texto que tiene el campo de nuevo correo solamente, ya que no es necesario guardar el de
                         * confirmacion, el regex es para verificar si lo ingresado en el campo de email se ingreso en formato de email (@mail.com).
                         */

                        final String comprobarEmail = nuevoCorreo.getEditableText().toString().trim();
                        final String regex = "(?:[^<>()\\[\\].,;:\\s@\"]+(?:\\.[^<>()\\[\\].,;:\\s@\"]+)*|\"[^\\n\"]+\")@(?:[^<>()\\[\\].,;:\\s@\"]+\\.)+[^<>()\\[\\]\\.,;:\\s@\"]{2,63}";

                        /**
                         * En este if se comprueba que los campos sean iguales, ninguno este vacio y que el formato ingresado corresponda a un email.
                         */
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
                /**
                 * Aqui es exactamente igual a cambiar nombre de usuario
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                LayoutInflater in = getLayoutInflater();

                View view = in.inflate(R.layout.cambiar_telefono, null);

                builder.setView(view);

                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
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
                /**
                 * Aqui el AlertDialog tiene dos botones, el primero para seleccionar una imagen almacenada y el segundo para guardar
                 * la imagen.
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                LayoutInflater in = getLayoutInflater();
                View view = in.inflate(R.layout.cambiar_foto, null);
                builder.setView(view);

                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
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

        System.out.println(encodeImage);

        return encodeImage;
    }

    public void uploadImage(){
        final ProgressDialog cargando = ProgressDialog.show(getContext(), "Subiendo...", "Espera por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /**
                 * En esta parte al momento de guardar la imagen el imageview se ve actualizado por la imagen que se selecciono y cargo al servidor.
                 */
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
        /**
         * Este es el encargado de abrir el selector de imagenes.
         */
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
