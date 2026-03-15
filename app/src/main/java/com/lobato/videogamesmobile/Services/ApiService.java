package com.lobato.videogamesmobile.Services;

import com.lobato.videogamesmobile.DTOs.DTOVideogame;
import com.lobato.videogamesmobile.DTOs.EsrbDTO;
import com.lobato.videogamesmobile.DTOs.GenreDTO;
import com.lobato.videogamesmobile.DTOs.PlatformDTO;
import com.lobato.videogamesmobile.DTOs.VideoGameInDTO;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @POST("api/game/add")
    @Multipart
    Call<ResponseBody> addGame(
            @Part("videoGameInDTO") RequestBody dto,
            @Part("file") MultipartBody.Part image
            );
    @POST("api/game/update")
    @Multipart
    Call<ResponseBody> updateGame(
            @Part("videogame") RequestBody dto,
            @Part("file") MultipartBody.Part image
    );
    @FormUrlEncoded
    @POST("api/game/delete")
    Call<ResponseBody> deleteGame(
            @Field("id") Integer id
    );

    @GET("/api/game/getGames")
    Call<List<DTOVideogame>> getAllVideogames();
    @GET("api/game/esrb")
    Call<List<EsrbDTO>> getAllEsrbDTO();
    @GET("api/game/platforms")
    Call<List<PlatformDTO>> getAllPlatforms();
    @GET("api/game/genre")
    Call<List<GenreDTO>> getAllGenres();
    

}
