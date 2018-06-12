package com.example.otavio.newshowup.evento;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otavio.newshowup.R;

public class EventoViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView title,cidade,faixa_preco,data;
    EventoViewHolder(View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.item_evento_image);
        title = itemView.findViewById(R.id.item_evento_title);
        cidade=itemView.findViewById(R.id.item_evento_cidade);
        faixa_preco=itemView.findViewById(R.id.item_evento_preco);
        data=itemView.findViewById(R.id.item_evento_data);
    }
}
