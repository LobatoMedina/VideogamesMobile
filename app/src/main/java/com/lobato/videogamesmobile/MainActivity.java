package com.lobato.videogamesmobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lobato.videogamesmobile.Controller.MainController;
import com.lobato.videogamesmobile.DTOs.DTOVideogame;
import com.lobato.videogamesmobile.Services.ApiService;
import com.lobato.videogamesmobile.View.VideogameAdapter;
import com.lobato.videogamesmobile.View.VideogameForm;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    public ApiService apiService;
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rv = findViewById(R.id.recycler_videogames);
        rv.setLayoutManager(new LinearLayoutManager(this));
        fetchAllGames();


    }
    @Override
    protected void onResume(){
        super.onResume();

    }
    public void addGame(View v){
        startActivity(new Intent(MainActivity.this, VideogameForm.class));
    }
    private void fetchAllGames(){
        apiService = MainController.getclient().create(ApiService.class);
        Call<List<DTOVideogame>> call= apiService.getAllVideogames();
        call.enqueue(new Callback<List<DTOVideogame>>() {
            @Override
            public void onResponse(Call<List<DTOVideogame>> call, Response<List<DTOVideogame>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    VideogameAdapter adapter = new VideogameAdapter(response.body(), MainActivity.this);
                    rv.setAdapter(adapter);
                    Toast.makeText(MainActivity.this, "Datos cargados correctamente", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error en la respuesta del server", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<DTOVideogame>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Hubo un error con el servidor",Toast.LENGTH_LONG);
            }
        });
    }
}