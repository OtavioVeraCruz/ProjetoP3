package com.example.otavio.newshowup.contratante;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.LoadImg;

import java.util.ArrayList;

public class ArtistaAdapter extends RecyclerView.Adapter<ArtistaAdapter.ViewHolder> {

    private ArrayList<Firebase.Artista> artistas;
    private Context context;
    ArtistaAdapter(ArrayList<Firebase.Artista> artistas, Context context){
        this.artistas=artistas;
        this.context=context;
    }
    @NonNull
    @Override
    public ArtistaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArtistaAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_artista,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ArtistaAdapter.ViewHolder holder, int position) {
        String url=artistas.get(position).foto;
        String title=artistas.get(position).nome;
        String preco=artistas.get(position).dadosArtista.faixa_preco;
        String cidade=artistas.get(position).dadosArtista.cidade;
        LoadImg.loadImage(url,holder.imageView,context);
        holder.title.setText(title);
        holder.faixa_preco.setText(preco);
        holder.cidade.setText(cidade);
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,DetalhesArtistaActivity.class).
                        putExtra("artista", artistas.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return artistas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title,cidade,faixa_preco;
        LinearLayout linear;
        ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item_artista_image);
            title = itemView.findViewById(R.id.item_artista_nome);
            cidade=itemView.findViewById(R.id.item_artista_cidade);
            faixa_preco=itemView.findViewById(R.id.item_artista_faixa_preco);
            linear=itemView.findViewById(R.id.item_artista);
        }
    }

}
