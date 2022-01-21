package com.example.ejemplodm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class c_Archivos {

    private String nombreA;
    private String formatoA;
    private String tamanioA;
    private String url;

    public String getNombreA() {
        return nombreA;
    }

    public void setNombreA(String nombreA) {
        this.nombreA = nombreA;
    }

    public String getFormatoA() {
        return formatoA;
    }

    public void setFormatoA(String formatoA) {
        this.formatoA = formatoA;
    }

    public String getTamanioA() {
        return tamanioA;
    }

    public void setTamanioA(String tamanioA) {
        this.tamanioA = tamanioA;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public c_Archivos(JSONObject a){
        try {
            nombreA = a.getString("file").toString() ;
            formatoA = a.getString("format").toString() ;
            tamanioA = a.getString("img").toString() ;
            url = a.getString("img").toString() ;
        } catch (JSONException e) {
            System.out.println("Error: " + e.toString());
        }
    }

    public static ArrayList<c_Archivos> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<c_Archivos> archivos = new ArrayList<>();

        for (int i = 0; i < datos.length() ; i++) {
            archivos.add(new c_Archivos(datos.getJSONObject(i)));
        }
        return archivos;
    }
}
