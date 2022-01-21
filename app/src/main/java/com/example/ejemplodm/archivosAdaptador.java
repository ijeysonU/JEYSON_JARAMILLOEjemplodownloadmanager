package com.example.ejemplodm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class archivosAdaptador extends RecyclerView.Adapter<archivosViewHolder>
implements View.OnClickListener{
    private Context ctx;
    private List<c_Archivos> lstArchivos;
    private View.OnClickListener listener;
public archivosAdaptador(Context mCtx, List<c_Archivos> archivos){
    this.lstArchivos = archivos;
    ctx = mCtx;
    }

    @Override
    public archivosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.archivos_ly, null);
        view.setOnClickListener(this);
        return new archivosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull archivosViewHolder holder, int position) {
        c_Archivos archivos = lstArchivos.get(position);
        holder.textViewArchivoName.setText(archivos.getNombreA());
        holder.textViewArchivoFormato.setText(archivos.getFormatoA());
        holder.textViewArchivoTamanio.setText(archivos.getTamanioA());
        try {
            Glide.with(ctx)
                    .load(archivos.getUrl())
                    .error(R.drawable.unknown)
                    .into(holder.prevImg)

            ;//(Drawable("https://evaladmin.uteq.edu.ec/adminimg/unknown.png"));
        }catch(Exception ex){

        }
    }

    @Override
    public int getItemCount() {
        final int size = lstArchivos.size();
        return size;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }
}
