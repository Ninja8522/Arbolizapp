package com.example.arbolizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Código encargado del activity de login, la URL en el es para usar el codigo de php que permite la conexion a la app,
 * en este se optiene la informacion que se va a mandar al fragment de perfil, tambien es el encargado de permitir ir al registro
 * de usuario en caso de no tener uno.
 */

public class MainActivity extends AppCompatActivity {
    private TextView tv_register, tvOlvidecontraseña;
    private EditText user_email, pass;
    private Button btnLogin;
    private static String URL_LOGIN="https://arbolizappgreen.000webhostapp.com/login.php";
    String usuario, password , email;
    private CheckBox recordar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    private boolean Activated;
    private static final String STRING_PREFERENCES ="com.example.arbolizapp";
    private static final String PREFERENCES_ESTADO_CHECKBOX = "estado.checkbox.sesion";
    public static String nombreUsuario, fullname, correo, telefono, tipo, id_usuario, foto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (obtenerEstadoCheckBox()){
            /**
             * Por medio de un sharedpreferece obtengo el tipo del usuario que mantuvo iniciada su sesion, esto para que el diseño que se cargue
             * sea el correspondiente a ese usuario.
             */

            SharedPreferences obtenerTipo = getSharedPreferences("TipoDeUsuario", 0);
            String userType = obtenerTipo.getString("user_type","");
            
            if (userType.equals("general")){
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            } else if (userType.equals("vivero")){
                Intent intent = new Intent(getApplicationContext(), ViverpActivity.class);
                startActivity(intent);
            }
        }

        user_email = findViewById(R.id.username_email);
        pass = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);
        recordar = findViewById(R.id.recordar);

        tv_register = findViewById(R.id.tv_registrar);
        tvOlvidecontraseña = findViewById(R.id.tvOlvidecontraseña);

        Activated = recordar.isChecked();

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReg = new Intent(MainActivity.this, Register.class);
                MainActivity.this.startActivity(intentReg);
                MainActivity.this.finish();
            }
        });

        tvOlvidecontraseña.setOnClickListener(new View.OnClickListener() {
           /* @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }*/
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.cambiarcontra, null);

                builder.setView(view);

                final AlertDialog dialog = builder.create();
                dialog.show();
                final EditText telefono = view.findViewById(R.id.editPhone);
                final EditText nuevaContra = view.findViewById(R.id.editNuevaContra);
                final EditText confirmarContra = view.findViewById(R.id.editConfirmarNuevaContra);


                Button guardarNuevaContra = view.findViewById(R.id.guardarCambioContra);

                guardarNuevaContra.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!nuevaContra.getText().toString().isEmpty()
                                && !confirmarContra.getText().toString().isEmpty()
                                && nuevaContra.getText().toString().equals(confirmarContra.getText().toString())){
                            Toast.makeText(MainActivity.this, "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            nuevaContra.setError("Las contraseñas no coinciden");
                            nuevaContra.requestFocus();
                        }
                    }
                });


            }
        });

       btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = user_email.getText().toString().trim();
                String mPass = pass.getText().toString().trim();

                if (!mEmail.isEmpty() && !mPass.isEmpty()){
                    Login(mEmail, mPass, mEmail);
                    if (recordar.isChecked()){
                        usuario = mEmail;
                        password = mPass;
                        email = mEmail;
                    }
                }else if (mEmail.isEmpty()){
                    user_email.setError("Usuario o correo invalidos");
                    user_email.requestFocus();
                }else if (mPass.isEmpty()){
                    pass.setError("Contraseña incorrecta");
                    pass.requestFocus();
                }
            }
        });

       recordar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (Activated){
                   recordar.setChecked(false);
               }
               Activated = recordar.isChecked();
           }
       });

    }

    public static void changeEstado(Context c, boolean b){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCES_ESTADO_CHECKBOX,b).apply();
    }

    private void guardarEstado(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCES_ESTADO_CHECKBOX,recordar.isChecked()).apply();

    }

    public boolean obtenerEstadoCheckBox(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCES_ESTADO_CHECKBOX,false);
    }

   private void Login(final String email, final String password, final String username) {
        btnLogin.setVisibility(View.GONE);

       if (recordar.isChecked()){
            sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
            edit = sharedPreferences.edit();
            edit.putString(username,password);
            edit.commit();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        guardarEstado();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");
                            JSONArray jsonArrayA = jsonObject.getJSONArray("arboles");

                            if (success.equals("1")){

                                for (int i = 0; i < jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id");
                                    String name = object.getString("name").trim();
                                    String email = object.getString("email").trim();
                                    String user_type = object.getString("user_type").trim();
                                    String username = object.getString("username").trim();
                                    String phone = object.getString("phone").trim();
                                    String image = object.getString("image").trim();

                                    if (user_type.equals("general")){

                                        Toast.makeText(MainActivity.this, "Bienvenido \n "+ name, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                        id_usuario = id;
                                        nombreUsuario = username;
                                        fullname = name;
                                        correo = email;
                                        telefono = phone;
                                        tipo = user_type;
                                        foto = image;
                                        SharedPreferences tipoUsuario = getSharedPreferences("TipoDeUsuario", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editorTipo = tipoUsuario.edit();
                                        editorTipo.putString("user_type", tipo);
                                        editorTipo.commit();

                                        SharedPreferences prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putString("username", nombreUsuario);
                                        editor.putString("fullname", fullname);
                                        editor.putString("email", correo);
                                        editor.putString("phone", telefono);
                                        editor.putString("id", id_usuario);
                                        editor.putString("image", foto);
                                        editor.commit();

                                        startActivity(intent);
                                        MainActivity.this.finish();
                                    } else if (user_type.equals("vivero")){

                                        Toast.makeText(MainActivity.this, "Bienvenido \n "+ name+"\n"+ email, Toast.LENGTH_SHORT).show();
                                        Intent intentVivero = new Intent(MainActivity.this, ViverpActivity.class);

                                        nombreUsuario = username;
                                        correo = email;
                                        telefono = phone;
                                        tipo = user_type;
                                        foto = image;

                                        SharedPreferences tipoUsuario = getSharedPreferences("TipoDeUsuario", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editorTipo = tipoUsuario.edit();
                                        editorTipo.putString("user_type", tipo);
                                        editorTipo.commit();
                                        
                                        SharedPreferences prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putString("username", nombreUsuario);
                                        editor.putString("email", correo);
                                        editor.putString("phone", telefono);
                                        editor.putString("image", foto);
                                        editor.commit();

                                        startActivity(intentVivero);
                                        MainActivity.this.finish();
                                    }

                                }

                            } else {
                                btnLogin.setVisibility(View.VISIBLE);
                                String message = jsonObject.getString("message");
                                if (message.equals("error")){
                                    pass.setError("Contraseña incorrecta");
                                    pass.requestFocus();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            btnLogin.setVisibility(View.VISIBLE);
                            user_email.setError("Usuario o correo invalidos");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        btnLogin.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "Error al iniciar sesion"+error.toString(), Toast.LENGTH_SHORT).show();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError){
                            Toast.makeText(MainActivity.this, "Error al iniciar sesion", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError){
                            user_email.setError("Usuario incorrecto");
                            pass.setError("Contraseña incorrecta");
                        } else if (error instanceof ServerError){
                            pass.setError("Contraseña incorrecta");
                        } else if (error instanceof NetworkError){
                            pass.setError("Contraseña incorrecta");
                        } else if (error instanceof ParseError){
                            pass.setError("Contraseña incorrecta");
                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("password",password);
                params.put("username", username);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

   }

}

