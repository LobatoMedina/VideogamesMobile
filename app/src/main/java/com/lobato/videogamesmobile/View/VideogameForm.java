package com.lobato.videogamesmobile.View;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.lobato.videogamesmobile.Controller.MainController;
import com.lobato.videogamesmobile.DTOs.DTOVideogame;
import com.lobato.videogamesmobile.DTOs.EsrbDTO;
import com.lobato.videogamesmobile.DTOs.GenreDTO;
import com.lobato.videogamesmobile.DTOs.PlatformDTO;
import com.lobato.videogamesmobile.DTOs.VideoGameInDTO;
import com.lobato.videogamesmobile.R;
import com.lobato.videogamesmobile.Services.ApiService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VideogameForm extends AppCompatActivity {

    public static DTOVideogame videogame = null;

    EsrbDTO esrbs;
    List<GenreDTO> genres;
    List<PlatformDTO> platforms;
    ApiService apiService;
    Uri selectedImageUri;
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
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);

                        this.selectedImageUri = uri;
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }});
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

        btnImage.setOnClickListener(v -> {

            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
        if(videogame != null){
          btnAddGame.setText("Actualizar videojuego");
          btnAddGame.setOnClickListener(v -> {
              DTOVideogame gameDto = new DTOVideogame();
              RequestBody gameData = RequestBody.create(
                      MediaType.parse("application/json"),
                      new Gson().toJson(gameDto)
              );
              PostGame(gameData);
          });
          videogame.setGenres( new ArrayList<>());
          videogame.setPlatforms(new ArrayList<>());
        }else {
            btnAddGame.setOnClickListener(v -> {
                VideoGameInDTO gameDto = new VideoGameInDTO();
                RequestBody gameData = RequestBody.create(
                        MediaType.parse("application/json"),
                        new Gson().toJson(gameDto)
                );
                PostGame(gameData);
            });
        }

        try{
            fecthEsrb();
            fetchGenres();
            fetchPlatforms();
            buttonGenre.setOnClickListener(v -> {
                genres.add(new GenreDTO(spGenres.getSelectedItemPosition(), spGenres.getSelectedItem().toString()));
            });
            buttonPlatform.setOnClickListener(v -> {
                platforms.add(new PlatformDTO(spPlatforms.getSelectedItemPosition() ,spPlatforms.getSelectedItem().toString()));
            });

        }catch(Exception e){
            Log.e("Fallo", e.toString());
        }
    }
    public void PostGame(RequestBody gameData){
        if (selectedImageUri == null) {
            return;
        }

        File imageFile = getFileFromUri(selectedImageUri);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);
        Call<ResponseBody> call = apiService.addGame(gameData, filePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
    private void fecthEsrb(){
        Call<List<EsrbDTO>> call = apiService.getAllEsrbDTO();
        call.enqueue(new Callback<List<EsrbDTO>>() {
            @Override
            public void onResponse(Call<List<EsrbDTO>> call, Response<List<EsrbDTO>> response) {
                Log.println(Log.VERBOSE, "Responser",response.message() );
                if(response.isSuccessful() && response.body() != null){
                    ArrayList<String> list = new ArrayList<>();
                    for (var esrb :
                            response.body()) {
                        list.add(esrb.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(VideogameForm.this, android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spEsrb.setAdapter(adapter);
                    spEsrb.setSelection(1);
                    spEsrb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            esrbs = response.body().get(position);
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
    private void fetchGenres(){
        Call<List<GenreDTO>> call = apiService.getAllGenres();
        call.enqueue(new Callback<List<GenreDTO>>() {
            @Override
            public void onResponse(Call<List<GenreDTO>> call, Response<List<GenreDTO>> response) {
                if(response.isSuccessful() && response.body() != null) {

                    ArrayList<String> list = new ArrayList<>();
                    for (var genres :
                            response.body()) {
                        list.add(genres.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(VideogameForm.this, android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spGenres.setAdapter(adapter);
                    spGenres.setSelection(1);
                    spGenres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else{

                }
            }

            @Override
            public void onFailure(Call<List<GenreDTO>> call, Throwable t) {

            }
        });

    }
    private void fetchPlatforms(){
        Call<List<PlatformDTO>> call = apiService.getAllPlatforms();
        call.enqueue(new Callback<List<PlatformDTO>>() {
            @Override
            public void onResponse(Call<List<PlatformDTO>> call, Response<List<PlatformDTO>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    ArrayList<String> list = new ArrayList<>();
                    for (var patform :
                            response.body()) {
                        list.add(patform.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(VideogameForm.this, android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spPlatforms.setAdapter(adapter);
                    spPlatforms.setSelection(1);
                    spPlatforms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else{

                }
            }

            @Override
            public void onFailure(Call<List<PlatformDTO>> call, Throwable t) {

            }
        });
    }
    private File getFileFromUri(Uri uri) {
        try {
            File tempFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp_image.jpg");
            InputStream inputStream = getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}