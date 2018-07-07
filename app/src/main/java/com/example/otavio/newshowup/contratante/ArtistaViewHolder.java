package com.example.otavio.newshowup.contratante;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otavio.newshowup.R;

public class ArtistaViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView title,cidade,faixa_preco;
    View linear;
    int id;
    public ArtistaViewHolder(View itemView) {
        super(itemView);
        linear=itemView.findViewById(R.id.item_artista);
        imageView=itemView.findViewById(R.id.item_artista_image);
        title = itemView.findViewById(R.id.item_artista_nome);
        cidade=itemView.findViewById(R.id.item_artista_cidade);
        faixa_preco=itemView.findViewById(R.id.item_artista_faixa_preco);
        id=itemView.getId();
    }
}
