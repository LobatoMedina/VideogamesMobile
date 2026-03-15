package com.lobato.videogamesmobile.View;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.lobato.videogamesmobile.Controller.MainController;
import com.lobato.videogamesmobile.DTOs.DTOVideogame;
import com.lobato.videogamesmobile.DTOs.EsrbDTO;
import com.lobato.videogamesmobile.R;
import com.lobato.videogamesmobile.Services.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VideogameForm extends AppCompatActivity {

    public static DTOVideogame videogame;

    List<EsrbDTO> esrbs;
    ApiService apiService;

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
        apiService = MainController.getclient().create(ApiService.class);
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
        try{
            fecthEsrb();

        }catch(Exception e){
            Log.e("Fallo", e.toString());
        }
    }
    public void fecthEsrb(){

        Call<List<EsrbDTO>> call = apiService.getAllEsrbDTO();
        call.enqueue(new Callback<List<EsrbDTO>>() {
            @Override
            public void onResponse(Call<List<EsrbDTO>> call, Response<List<EsrbDTO>> response) {
                Log.println(Log.VERBOSE, "Responser",response.message() );
                if(response.isSuccessful() && response.body() != null){
                    esrbs = response.body();
                    ArrayList<String> list = new ArrayList<>();
                    for (var esrb :
                            esrbs) {
                        list.add(esrb.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(VideogameForm.this, android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spEsrb.setAdapter(adapter);
                    spEsrb.setSelection(1);
                    spEsrb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedItem = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else{
                    throw new RuntimeException("Error al consultar al servidor");
                }
            }

            @Override
            public void onFailure(Call<List<EsrbDTO>> call, Throwable t) {
                Toast.makeText(VideogameForm.this, "No es posible acceder al servidor", Toast.LENGTH_LONG);
                throw new RuntimeException("Error al acceder al servidor");
            }
        });
    }

}