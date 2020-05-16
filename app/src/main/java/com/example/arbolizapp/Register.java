package com.example.arbolizapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Código del activity encargado de los registros de usuario.
 */


public class Register extends AppCompatActivity {
    private String tipo = "general", email, name, user, pass, passconf, phone = "##########", image = "default";
    private EditText correo, nombre, usuario, contraseña, confcontraseña;
    private Button btnRegistrar;
    private static String URL_REGIST = "http://192.168.56.1/android_register_login/register.php";
    private TextView tvLogin;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    public static String nombreUsuario, fullname, mail, telefono, id_usuario, foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        correo = findViewById(R.id.email);
        nombre = findViewById(R.id.name);
        usuario = findViewById(R.id.user);
        contraseña = findViewById(R.id.password);
        confcontraseña = findViewById(R.id.confpassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        tvLogin = findViewById(R.id.tvCancelar);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,MainActivity.class);
                Register.this.startActivity(intent);
                Register.this.finish();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = correo.getText().toString();
                name = nombre.getText().toString();
                user = usuario.getText().toString();
                pass = contraseña.getText().toString();
                passconf = confcontraseña.getText().toString();

                final String comprobarEmail = correo.getEditableText().toString().trim();
                final String regex = "(?:[^<>()\\[\\].,;:\\s@\"]+(?:\\.[^<>()\\[\\].,;:\\s@\"]+)*|\"[^\\n\"]+\")@(?:[^<>()\\[\\].,;:\\s@\"]+\\.)+[^<>()\\[\\]\\.,;:\\s@\"]{2,63}";

                if (!email.isEmpty() &&
                        !name.isEmpty() &&
                        !user.isEmpty() &&
                        !pass.isEmpty() &&
                        !passconf.isEmpty() &&
                        comprobarEmail.matches(regex) &&
                        pass.equals(passconf) && pass.length()>=6) {
                    Regist();

                } else if (email.isEmpty()) {
                    correo.setError("Correo requerido");
                    correo.requestFocus();
                } else if (!comprobarEmail.matches(regex)) {
                    correo.setError("Correo no valido");
                    correo.requestFocus();
                } else if (name.isEmpty()) {
                    nombre.setError("Nombre requerido");
                    nombre.requestFocus();
                } else if (user.isEmpty()) {
                    usuario.setError("Usuario requerido");
                    usuario.requestFocus();
                } else if (pass.isEmpty()) {
                    contraseña.setError("Contraseña requerido");
                    contraseña.requestFocus();
                }else if (pass.length()<6){
                    contraseña.setError("La contraseña debe ser > 6 caracteres");
                }
                else if (passconf.isEmpty() || pass != passconf){
                    contraseña.setError("Las contraseñas no coinciden");
                    contraseña.requestFocus();
                }
            }
        });

    }

    private void Regist(){
        btnRegistrar.setVisibility(View.GONE);

        final String correo = this.correo.getText().toString().trim();
        final String nombre = this.nombre.getText().toString().trim();
        final String usuario = this.usuario.getText().toString().trim();
        final String contraseña = this.contraseña.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String id = jsonObject.getString("id");

                            if (success.equals("1")){

                                Toast.makeText(Register.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                nombreUsuario = usuario;
                                fullname = nombre;
                                mail = correo;
                                telefono = phone;
                                foto = image;
                                id_usuario = id;

                                SharedPreferences prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("username", nombreUsuario);
                                editor.putString("fullname", fullname);
                                editor.putString("email", mail);
                                editor.putString("phone", telefono);
                                editor.putString("image", foto);
                                editor.putString("id", id_usuario);
                                editor.commit();

                                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(intent);
                                Register.this.finish();
                            } else if (success.equals("2")){
                                btnRegistrar.setVisibility(View.VISIBLE);
                                Toast.makeText(Register.this, "Usuario existente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                Register.this.finish();
                            } else if (success.equals("3")){
                                btnRegistrar.setVisibility(View.VISIBLE);
                                Toast.makeText(Register.this, "Ya hay un usuario registrado con ese correo", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                Register.this.finish();
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(Register.this, "Error al registrar " +e.toString(), Toast.LENGTH_SHORT).show();
                            btnRegistrar.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Register.this, "Error al registrar  "+error.toString(), Toast.LENGTH_SHORT).show();
                        btnRegistrar.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_type", tipo);
                params.put("email", correo);
                params.put("name", nombre);
                params.put("username", usuario);
                params.put("password", contraseña);
                params.put("phone", phone);
                params.put("image", image);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
