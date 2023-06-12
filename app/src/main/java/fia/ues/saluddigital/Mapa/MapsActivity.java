package fia.ues.saluddigital.Mapa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import fia.ues.saluddigital.R;
import fia.ues.saluddigital.databinding.ActivityMapsBinding;
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    ImageView imgmarker;
    private BottomSheetBehavior mBottomSheetBehavior1;
    LinearLayout tapactionlayout;
    View bottomSheet;
    TextView txtnombre_local, txtDireccion, txtHorario;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final float camera_zoom = 14;

    private ArrayList<Puntos> listaPuntos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        View headerLayout1 = findViewById(R.id.bottomJsoft);
        imgmarker = (ImageView) findViewById(R.id.ImgMarker);
        txtnombre_local = (TextView) findViewById(R.id.txtNombreLocal);
        txtDireccion = (TextView) findViewById(R.id.txtDireccion);
        txtHorario = (TextView) findViewById(R.id.txtHorario);
        tapactionlayout = (LinearLayout) findViewById(R.id.tap_action_layout);
        bottomSheet = findViewById(R.id.bottomJsoft);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior1.setPeekHeight(120);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);

        mBottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    tapactionlayout.setVisibility(View.VISIBLE);
                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    tapactionlayout.setVisibility(View.GONE);
                }
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    tapactionlayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        tapactionlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomSheetBehavior1.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Agregamos el marcador y centramos la camara
        LatLng UES = new LatLng(13.71653408409292, -89.20334100846175);
        mMap.addMarker(new MarkerOptions().position(UES).title("Marcador en UES"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(UES));

        VolverPosicion(UES);
        // Llamar al web service para obtener los datos
        marcadores();

    }

    private void VolverPosicion(LatLng miLatLng) {
        //
        CameraPosition camPos = new CameraPosition.Builder()
                .target(miLatLng)
                .zoom(camera_zoom).build();        //Establecemos el zoom en 14
        CameraUpdate miUbicacion = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(miUbicacion);
    }

    public void marcadores() {
        class GetDataTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... params) {
                String urlString = "https://gg20013pdm115.000webhostapp.com/clinicasmark.php";
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();
                    inputStream.close();

                    return stringBuilder.toString();

                } catch (IOException e) {
                    Log.e("GetDataTask", "Error al obtener los datos: " + e.getMessage());
                }

                return null;
            }

            @Override
            protected void onPostExecute(@Nullable String result) {
                super.onPostExecute(result);
                if (result != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String id = jsonObject.getString("id");
                            double latitud = jsonObject.getDouble("latitud");
                            double longitud = jsonObject.getDouble("longitud");
                            String nombreLugar = jsonObject.getString("nombre_lugar");
                            String descripcionLugar = jsonObject.getString("descripcion_lugar");
                            String urlImagenLugar = jsonObject.getString("url_imagen");
                            String direccion = jsonObject.getString("direccion");

                            Toast.makeText(getApplicationContext(), latitud + " , " + longitud, Toast.LENGTH_SHORT).show();
                            LatLng location = new LatLng(latitud, longitud);
                            Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(nombreLugar).snippet(descripcionLugar));
                            marker.setTag(id);

                            Puntos puntoH = new Puntos();
                            puntoH.setId(id);
                            puntoH.setNombreLugar(nombreLugar);
                            puntoH.setDescripcionLugar(descripcionLugar);
                            puntoH.setImagenLugar(urlImagenLugar);
                            puntoH.setDireccion(direccion);

                            listaPuntos.add(puntoH);


                        }

                        //---Evento de Click en marcador
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                for (int i =0; i < listaPuntos.size(); i++){
                                    if(listaPuntos.get(i).getId()== marker.getTag()){
                                        txtnombre_local.setText(listaPuntos.get(i).getNombreLugar());
                                        txtHorario.setText(listaPuntos.get(i).getDescripcionLugar());
                                        txtDireccion.setText(listaPuntos.get(i).getDireccion());
                                        loadImageFromUrl(listaPuntos.get(i).getImagenLugar(),imgmarker);
                                    }
                                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                                return false;}
                        });
                        //---------

                    } catch (JSONException e) {
                        Log.e("GetDataTask", "Error al analizar JSON: " + e.getMessage());
                    }
                }
            }
        }
        new GetDataTask().execute();
    }

    private void loadImageFromUrl(String url, ImageView imageView) {
        Picasso.get().load(url).into(imageView);
    }
}