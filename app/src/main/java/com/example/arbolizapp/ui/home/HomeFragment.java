package com.example.arbolizapp.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.FrameMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.arbolizapp.R;
import com.example.arbolizapp.interfaces.IComunicaFragment;
import com.example.arbolizapp.ui.generar.GenerarSolicitudFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Esta parte corresponde al fragmet de home, este es el primero en cargar al momento de iniciar sesion como un usuario generarl,
 * de este se va al fragment de generar solicitud al precionar su fab button.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HomeViewModel homeViewModel;
    FloatingActionButton generarSolicitud;
    //LocationManager locationManager;

    IComunicaFragment interfaceComunicaFragments;
    Activity actividad;
    View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        generarSolicitud = root.findViewById(R.id.agregar);
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        generarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                interfaceComunicaFragments.generarSolicitud();
                /*Fragment nuevoFragment = new GenerarSolicitudFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.nav_host_fragment, nuevoFragment);
                transaction.addToBackStack(null);
                transaction.commit();*/

                generarSolicitud.setVisibility(View.GONE);
            }
        });


        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof Activity){
            actividad = (Activity) context;
            interfaceComunicaFragments= (IComunicaFragment) actividad;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(-34,151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LocationServices.getFusedLocationProviderClient(getContext()).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mMap.setMyLocationEnabled(true);
            }
        });
    }

    /*private boolean checkLocation() {
        if (!isLocationEnable()){
            showAlert();
        }
        return isLocationEnable();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Enable location")
                .setMessage("Su ubicación esta desactivada. \npor favor active su ubicación" +
                        "usa esta app")
                .setPositiveButton("Configuración de ubicación", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnable() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void toggleGPSUpdates(View view){

    }*/

}
