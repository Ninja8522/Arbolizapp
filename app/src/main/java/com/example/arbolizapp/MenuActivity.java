package com.example.arbolizapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arbolizapp.interfaces.IComunicaFragment;
import com.example.arbolizapp.ui.gallery.GalleryFragment;
import com.example.arbolizapp.ui.generar.GenerarSolicitudFragment;
import com.example.arbolizapp.ui.generar.GenerarSolicitudMapaFragment;
import com.example.arbolizapp.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


/**
 * Código encargado del menu que esta en el diseño de los usuarios generales, a este código le e movido muy poco,
 * solo e ido agregando los fragment que cree yo para que el diseño de menu se muetre en estos tambien.
 */

public class MenuActivity extends AppCompatActivity implements IComunicaFragment {


    private AppBarConfiguration mAppBarConfiguration;
    TextView cerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        /*Bundle bundle = getIntent().getExtras();

        String datoUsuario = bundle.getString("username");
        String datoFullName = bundle.getString("fullname");
        String datoEmail = bundle.getString("email");
        String datoTelefono = bundle.getString("phone");

        Bundle args = new Bundle();

        args.putString("username",datoUsuario);

        GalleryFragment nuevoFragmento = new GalleryFragment();
        nuevoFragmento.setArguments(args);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, nuevoFragmento);
        fragmentTransaction.commit();*/

        cerrar = findViewById(R.id.cerrarSesion);

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.changeEstado(MenuActivity.this,false);
                Intent i = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                Toast.makeText(MenuActivity.this, "Vuelva pronto", Toast.LENGTH_SHORT).show();
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_catalago, R.id.nav_vale, R.id.nav_generar, R.id.nav_generar_mapa)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Aqui esta la parte que manda llamar los fragmet de generar solicitud y seleccionar ubicacion,
     * generar es llamado desde el fragment de home y seleccionar es llamado desde el fragment de generar.
     */
    @Override
    public void generarSolicitud() {
        Fragment generarSolicitudFragment = new GenerarSolicitudFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.nav_host_fragment, generarSolicitudFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void seleccionarUbicacion() {
        Fragment seleccionarUbicacion = new GenerarSolicitudMapaFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.nav_host_fragment, seleccionarUbicacion);
        fragmentTransaction.commit();
    }

}
