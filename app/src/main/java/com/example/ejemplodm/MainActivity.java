package com.example.ejemplodm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    public static final String URL1 = "https://my-json-server.typicode.com/ijeysonU/repo_image/db";
    RecyclerView recyclerView;
    RequestQueue rq;
    ArrayList<c_Archivos> lstEval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> permisos = new ArrayList<>();
        permisos.add(Manifest.permission.CAMERA);
        permisos.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permisos.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permisos.add(Manifest.permission.WRITE_CALENDAR);
        rq = Volley.newRequestQueue(this);
        handleSSLHandshake();
        jsonObjectRequest();

        recyclerView = (RecyclerView) findViewById(R.id.reciclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getPermission(permisos);

    }
    public void getPermission(ArrayList<String> permisosSolicitados){
        ArrayList<String> listPermisosNoAprob = getPermisosNoAprobados(permisosSolicitados);
        if (listPermisosNoAprob.size()>0){
            if (Build.VERSION.SDK_INT>=23){
                requestPermissions(listPermisosNoAprob.toArray(new String[listPermisosNoAprob.size()]),1);
            }
        }
    }

    public ArrayList<String> getPermisosNoAprobados(ArrayList<String> listaPermisos){
        ArrayList<String> list = new ArrayList<>();
        for (String permiso: listaPermisos){
            if (Build.VERSION.SDK_INT>=23){
                if (checkSelfPermission(permiso)!= PackageManager.PERMISSION_GRANTED)
                    list.add(permiso);
            }
        }
        return list;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String s = "";
        if (requestCode==1){
            for (int i =0; i<permissions.length; i++){
                if (grantResults[i]== PackageManager.PERMISSION_GRANTED){
                    s =s+"OK "+permissions[i]+ "\n";
                }else{
                    s =s+"NO "+permissions[i]+ "\n";
                }
                Toast.makeText(this.getApplicationContext(), s,Toast.LENGTH_LONG).show();
            }
        }
    }

    public void MostrarDescargas(View v){
        Intent intent = new Intent();
        intent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(intent);
    }

    public void BajarDoc(View v, String uri){
        String url = uri;
        DownloadManager.Request request =  new DownloadManager.Request(Uri.parse(url));
        request.setDescription("PDF");
        request.setTitle("pdf");
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB){
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "filedownload.pdf");
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            manager.enqueue(request);
        }catch (Exception e)
        {
            Toast.makeText(this.getApplicationContext(), "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void jsonObjectRequest(){
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL1,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            int resId = R.anim.layout_animation_down_to_up;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(),
                                    resId);
                            recyclerView.setLayoutAnimation(animation);
                            lstEval = c_Archivos.JsonObjectsBuild(jsonArray);
                            archivosAdaptador archivosAdaptador = new archivosAdaptador(MainActivity.this, lstEval);
                            recyclerView.setAdapter(archivosAdaptador);
                            archivosAdaptador.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String ideEva = lstEval.get(recyclerView.getChildAdapterPosition(v)).getUrl();
                                    BajarDoc(v, ideEva);
                                }
                            });

                            //adaptador_evaluadores ae = new adaptador_evaluadores(MainActivity.this, lstEval);
                            System.out.println("Data: "+lstEval);
                            //lstOpcionesIn.setAdapter(ae);
                            //System.out.println(jsonArray);
                        }catch (JSONException ex){
                            System.out.println("Error: "+ex.toString());
                            //Toast.makeText(ex.getMessage(),Toast.LENGTH_LONG);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error: "+error.toString());
            }
        }
        );

        rq.add(jsonArrayRequest);
    }



    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
}