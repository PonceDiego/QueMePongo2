package com.s21.quemepongo2front.ui.home;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.s21.quemepongo2front.PronosticoRs;
import com.s21.quemepongo2front.R;
import com.s21.quemepongo2front.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Context context;

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.textViewUbicacion);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        /*
        AsyncTask.execute(new Runnable() {
            @Override

            public void run() {
                try {

                    String urlS = getString(R.string.ipApi)+ getString(R.string.json_pronostico);
                    JSONObject jsonObject= new JSONObject(readUrl(urlS));

                    String temperatura = jsonObject.get("temperatura").toString();
                    String nombre = jsonObject.get("ciudadNombre").toString();
                    String viento= jsonObject.get("viento").toString();
                    String humedad=jsonObject.get("humedad").toString();
                    TextView temp_actual = getView().findViewById(R.id.temperatura_actual);
                    temp_actual.setText(temperatura+"℃");

                    TextView ubicacion = getView().findViewById(R.id.textViewUbicacion);
                    ubicacion.setText(getText(R.string.ubicacion)+nombre);

                    TextView vientoView = getView().findViewById(R.id.textViento);
                    vientoView.setText("Viento: "+viento+" m/s");

                    TextView textHumedad= getView().findViewById(R.id.textHumedad);
                    textHumedad.setText("Humedad: "+humedad+"%");

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        */

        //Creamos la instancia de Retrofit, con el prefix de la URL( http://181.31.108.164:5599/ )
        //Le seteamos el convertor de JSON a java class3
        //TODO: Hacer este Retrofit Singleton
        Retrofit retro = new Retrofit.Builder()
                .baseUrl(getString(R.string.ipApi))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Creamos la clase RestClient ( que la cree yo )
        //En esta clase tiene los EndPoints de mi API ( sufix ). Como por ejemplo: "Pronostico?IdCiudad=3860259"
        RestClient restClient = retro.create(RestClient.class);

        //Aca creamos el objeto "llamada" el cual va a ser el endpoint a cual vamos a llamar
        Call<PronosticoRs> call = restClient.getData();

        //Ejecutamos la llamada en  un thread a parte, el cual si te deja ahcer modificaciones en la view
        call.enqueue(new Callback<PronosticoRs>() {

            //Este es el metodo en caso que la llamada a la API devuelva algo
            public void onResponse(Call<PronosticoRs> call, Response<PronosticoRs> response) {
                if(response.isSuccessful()){
                    //Obtenemos el body de la llamada, ya parseado a una clase java
                    PronosticoRs data = response.body();

                    TextView temp_actual = getView().findViewById(R.id.temperatura_actual);
                    temp_actual.setText(data.getTemperatura()+"℃");

                    TextView ubicacion = getView().findViewById(R.id.textViewUbicacion);
                    ubicacion.setText(getText(R.string.ubicacion)+data.getCiudadNombre());

                    TextView viento = getView().findViewById(R.id.textViento);
                    viento.setText("Viento: "+data.getViento()+" m/s");

                    TextView textHumedad= getView().findViewById(R.id.textHumedad);
                    textHumedad.setText("Humedad: "+data.getHumedad()+"%");
                }else{
                    Toast.makeText(context , "Hubo un error en la carga de los datos" , Toast.LENGTH_SHORT).show();
                }


            }

            //Este es el metodo en el caso de que algo falle, como que el celular no tiene internet
            public void onFailure(Call<PronosticoRs> call, Throwable t) {
            }
        });

        return root;
    }
}



