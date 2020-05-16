package com.example.arbolizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends AppCompatActivity {
    Button cerrar;
    TextView datoUsuario, datoNombre, datoCorreo, datoTelefono;

    Bundle datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        cerrar = findViewById(R.id.cerrar);
        datoUsuario = findViewById(R.id.datoUsuario);
        datoNombre = findViewById(R.id.datofullname);
        datoCorreo = findViewById(R.id.datoemail);
        datoTelefono = findViewById(R.id.datophone);


        datos = getIntent().getExtras();
        String datoUser = datos.getString("username");
        String datoName = datos.getString("fullname");
        String datoEmail = datos.getString("email");
        String datoPhone = datos.getString("phone");

        datoUsuario.setText(datoUser);
        datoNombre.setText(datoName);
        datoCorreo.setText(datoEmail);
        datoTelefono.setText(datoPhone);

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.changeEstado(Profile.this,false);
                Intent i = new Intent(Profile.this, MainActivity.class);
                startActivity(i);
                finish();
                Toast.makeText(Profile.this, "Vuelva pronto", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
