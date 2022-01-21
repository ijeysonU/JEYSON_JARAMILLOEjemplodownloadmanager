package com.example.ejemplodm;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class archivosViewHolder extends RecyclerView.ViewHolder{
    TextView textViewArchivoName, textViewArchivoFormato, textViewArchivoTamanio;
    ImageView prevImg;
    public archivosViewHolder(View itemView) {
        super(itemView);

        textViewArchivoName= itemView.findViewById(R.id.txtArchivo);
        textViewArchivoFormato = itemView.findViewById(R.id.txtformato);
        textViewArchivoTamanio = itemView.findViewById(R.id.txttamanio);
        prevImg = itemView.findViewById(R.id.imgAvatar);
    }
}
