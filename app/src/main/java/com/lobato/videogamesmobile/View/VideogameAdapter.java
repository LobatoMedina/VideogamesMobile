package com.lobato.videogamesmobile.View;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lobato.videogamesmobile.Controller.MainController;
import com.lobato.videogamesmobile.DTOs.DTOVideogame;
import com.lobato.videogamesmobile.R;
import com.lobato.videogamesmobile.Services.ApiService;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideogameAdapter extends RecyclerView.Adapter<VideogameAdapter.ViewHolder> {
    private List<DTOVideogame> videogameList;
    private ApiService apiService;
    private Context context;

    public VideogameAdapter(List<DTOVideogame> list, Context context) {
        this.videogameList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_games, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DTOVideogame game = videogameList.get(position);

        holder.title.setText(game.getName());
        holder.author.setText(game.getAuthor());
        holder.price.setText("$" + game.getPrice());

        String finalUrl = game.getImgUrl();
        Glide.with(context).load(finalUrl).into(holder.image);

        holder.btnPreview.setOnClickListener(v -> {
            if (game.getDemo() != null && !game.getDemo().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(game.getDemo()));
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "No hay demo disponible", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            deleteGameFromApi(game.getId(), position);
        });

        holder.btnUpdate.setOnClickListener(v -> {
            // TODO
            Toast.makeText(context, "Módulo de actualización próximamente", Toast.LENGTH_SHORT).show();
        });
    }

    private void deleteGameFromApi(int id, int position) {
        apiService =MainController.getclient().create(ApiService.class);
        Call<ResponseBody> call = apiService.deleteGame(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    videogameList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, videogameList.size());
                    Toast.makeText(context, "Eliminado con éxito", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() { return videogameList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, price;
        ImageView image;
        Button btnUpdate, btnDelete, btnPreview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_title);
            author = itemView.findViewById(R.id.txt_author);
            price = itemView.findViewById(R.id.txt_price);
            image = itemView.findViewById(R.id.imageView);
            btnUpdate = itemView.findViewById(R.id.btn_update);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnPreview = itemView.findViewById(R.id.btn_preview);
        }
    }
}