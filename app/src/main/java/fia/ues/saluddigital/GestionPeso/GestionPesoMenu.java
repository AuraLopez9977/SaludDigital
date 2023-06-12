package fia.ues.saluddigital.GestionPeso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import fia.ues.saluddigital.R;

public class GestionPesoMenu extends AppCompatActivity implements listAdapter.OnItemClickListener {

    List<listView> elements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_peso_menu);

        init();
    }

    @Override
    public void onItemClick(int position) {
        // Obtén el elemento de la lista según la posición
        listView item = elements.get(position);

        // Determina la actividad correspondiente según el elemento seleccionado
        Class<?> activityClass = null;
        if (item.getName().equals("Registrar pesajes")) {
            activityClass = RegistrarPeso.class;
        } else if (item.getName().equals("Gráficos")) {
            activityClass = GraficosPeso.class;
        }

        // Inicia la actividad correspondiente si se encontró una clase válida
        if (activityClass != null) {
            Intent intent = new Intent(this, activityClass);
            startActivity(intent);
        }
    }

    public void init() {
        elements = new ArrayList<>();
        elements.add(new listView("#1A998E", R.drawable.form_icon, "Registrar pesajes", "Registra la información completa de tus pesajes", "Disponible"));
        elements.add(new listView("#1A998E", R.drawable.graph, "Gráficos", "Mira los gráficos de la información de tu peso", "Disponible"));
        listAdapter listAdapter = new listAdapter(elements, this);
        listAdapter.setOnItemClickListener(this); // Establecer el OnItemClickListener
        RecyclerView recyclerView = findViewById(R.id.listRecicleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }
}