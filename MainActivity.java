package com.example.apiclimatempo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private EditText nmCidade;
    private TextView nmClima;
    private TextView nmTemperatura;
// 1 Pegar key e botar em outra api
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nmCidade = findViewById(R.id.txtCidade);
        nmClima = findViewById(R.id.tvClima);
        nmTemperatura = findViewById(R.id.tvTemperatura);
        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }
    public void openActivity2(){
        Intent intent= new Intent(this, activityResultado.class);
        startActivity(intent);
    }

       public void buscaTemperatura(View view) {
        // Recupera a string de busca.
           Log.d("asd","asdsd");
        String queryString = nmCidade.getText().toString();
        // esconde o teclado qdo o botão é clicado
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // Verifica o status da conexão de rede
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        /* Se a rede estiver disponivel e o campo de busca não estiver vazio
         iniciar o Loader */
           openActivity2();
        if (networkInfo != null && networkInfo.isConnected()
                && queryString.length() != 0) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
            nmClima.setText(R.string.str_empty);
            nmTemperatura.setText(R.string.str_empty);

        }
        // atualiza a textview para informar que não há conexão ou termo de busca
        else {
            if (queryString.length() == 0) {
                nmClima.setText(R.string.str_empty);
                nmTemperatura.setText(R.string.str_empty);
            } else {
                nmClima.setText(" ");
                nmTemperatura.setText(" ");
            }
        }
    }
    @NonNull
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";
        if (args != null) {
            queryString = args.getString("queryString");
        }
        return new CarregaCodigo(this, queryString);
    }
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            // Converte a resposta em Json
            JSONObject jsonObject = new JSONObject(data);
            // Obtem o JSONArray
            JSONArray itemsArray = jsonObject.getJSONArray("");
            // inicializa o contador
            int i = 0;
            String clima = null;
            String temperatura = null;
            // Procura pro resultados nos itens do array
            while (clima == null && temperatura== null) {
                // Obtem a informação
                JSONObject obj = itemsArray.getJSONObject(i);
                JSONObject temp = obj.getJSONObject("Temperature");
                JSONObject met = temp.getJSONObject("Metric");
                //  Obter autor e titulo para o item,
                // erro se o campo estiver vazio
                try {
                    temperatura = met.getString("Value");
                    clima = obj.getString("WeatherIcon");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // move para a proxima linha
                i++;
            }
            //mostra o resultado qdo possivel.
            if (clima != null && temperatura!= null) {
                nmClima.setText(clima);
                nmTemperatura.setText(temperatura);
                //nmLivro.setText(R.string.str_empty);
            } else {
                // If none are found, update the UI to show failed results.
                nmClima.setText(R.string.str_empty);
                nmTemperatura.setText(R.string.str_empty);
            }
        } catch (Exception e) {
            // Se não receber um JSOn valido, informa ao usuário
            nmClima.setText(R.string.str_empty);
            nmTemperatura.setText(R.string.str_empty);
            e.printStackTrace();
        }
    }
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // obrigatório implementar, nenhuma ação executada
    }
}