package com.lobato.videogamesmobile.View;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.lobato.videogamesmobile.DTOs.DTOVideogame;
import com.lobato.videogamesmobile.R;

public class VideogameForm extends AppCompatActivity {

    public static DTOVideogame videogame = null;


    Button buttonGenre, buttonPlatform,btnImage, btnAddGame;
    Spinner spGenres, spPlatforms, spEsrb;
    EditText name, author, price, specs, url, stock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_videogame_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnAddGame = findViewById(R.id.btn_addGame);
        spGenres = findViewById(R.id.sp_genres);
        spPlatforms = findViewById(R.id.sp_platform);
        spEsrb = findViewById(R.id.sp_esrb);
        buttonGenre = findViewById(R.id.btn_genre);
        buttonPlatform = findViewById(R.id.btn_platform);
        btnImage = findViewById(R.id.btn_img);
        name = findViewById(R.id.etxt_name);
        author = findViewById(R.id.etxt_author);
        price = findViewById(R.id.etxt_price);
        specs = findViewById(R.id.etxt_specs);
        url = findViewById(R.id.etxt_url);
        stock = findViewById(R.id.etext_stock);
        if(videogame != null){
          btnAddGame.setText("Actualizar videojuego");
        }
    }


}