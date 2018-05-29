package com.example.otavio.newshowup.evento;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.LoadImg;

import java.util.ArrayList;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.ViewHolder> {
    private ArrayList<Firebase.Evento>eventos;
    private Context context;
    public EventoAdapter(ArrayList<Firebase.Evento>eventos,Context context){
        this.eventos=eventos;
        this.context=context;
    }
    @NonNull
    @Override
    public EventoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_evento,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventoAdapter.ViewHolder holder, int position) {
        String url=eventos.get(position).fotos.get(0);
        String title=eventos.get(position).nome;
        String preco=eventos.get(position).faixa_preco;
        String cidade=eventos.get(position).cidade;
        String data=eventos.get(position).data;
        LoadImg.loadImage(url,holder.imageView,context);
        holder.title.setText(title);
        holder.faixa_preco.setText(preco);
        holder.cidade.setText(cidade);
        holder.data.setText(data);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title,cidade,faixa_preco,data;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item_evento_image);
            title = itemView.findViewById(R.id.item_evento_title);
            cidade=itemView.findViewById(R.id.item_evento_cidade);
            faixa_preco=itemView.findViewById(R.id.item_evento_preco);
            data=itemView.findViewById(R.id.item_evento_data);
        }
    }
}
