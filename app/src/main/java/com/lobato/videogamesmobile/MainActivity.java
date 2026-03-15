package com.lobato.videogamesmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.lobato.videogamesmobile.DTOs.DTOVideogame;
import com.lobato.videogamesmobile.Services.ApiService;
import com.lobato.videogamesmobile.View.VideogameForm;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    public ApiService apiService;
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
    }
    @Override
    protected void onResume(){
        super.onResume();

    }
    public void addGame(View v){
        startActivity(new Intent(MainActivity.this, VideogameForm.class));
    }
    private void fetchAllGames(){
        Call<List<DTOVideogame>> call= apiService.getAllVideogames();
        call.enqueue(new Callback<List<DTOVideogame>>() {
            @Override
            public void onResponse(Call<List<DTOVideogame>> call, Response<List<DTOVideogame>> response) {

            }

            @Override
            public void onFailure(Call<List<DTOVideogame>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Hubo un error con el servidor",Toast.LENGTH_LONG);
            }
        });
    }
}