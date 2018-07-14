package com.example.otavio.newshowup.artista;

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
import com.example.otavio.newshowup.evento.DetalhesEventoActivity;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.LoadImg;
import com.example.otavio.newshowup.utils.SnapshotEvento;

import java.util.ArrayList;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.ViewHolder> {

    private ArrayList<Firebase.Evento>eventos;
    private Context context;

    public EventoAdapter(Context context,ArrayList<Firebase.Evento>eventos){
        this.context=context;
        this.eventos=eventos;
    }

    public void setEventos(ArrayList<Firebase.Evento>eventos){
        this.eventos=eventos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventoAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_evento,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String url=eventos.get(position).fotos.get(0);
        String title=eventos.get(position).nome;
        String preco=eventos.get(position).faixa_preco;
        String cidade=eventos.get(position).cidade;
        String data=eventos.get(position).data;

        holder.title.setText(title);
        holder.cidade.setText(cidade);
        holder.data.setText(data);
        holder.faixa_preco.setText(preco);
        LoadImg.loadSimpleImage(url,holder.imageView,context);
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnapshotEvento.setEvento(eventos.get(position));
                context.startActivity(new Intent(context,DetalhesEventoActivity.class).
                        putExtra("evento",eventos.get(holder.getAdapterPosition())));
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title,cidade,faixa_preco,data;
        LinearLayout linear;

        ViewHolder(View itemView){
            super(itemView);
            imageView=itemView.findViewById(R.id.item_evento_image);
            title = itemView.findViewById(R.id.item_evento_title);
            cidade=itemView.findViewById(R.id.item_evento_cidade);
            faixa_preco=itemView.findViewById(R.id.item_evento_preco);
            data=itemView.findViewById(R.id.item_evento_data);
            linear=itemView.findViewById(R.id.item_evento);
        }
    }
}
