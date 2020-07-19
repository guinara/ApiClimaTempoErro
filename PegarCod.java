package com.example.apiclimatempo;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class PegarCod {
    private static final String LOG_TAG = PegarCod.class.getSimpleName();
    // Constantes utilizadas pela API
    // URL para a API de metereologia.
    //                                      http://dataservice.accuweather.com/locations/v1/cities/search?apikey=uS4MACAkFEZHV7pqqM1HDPocakID0704&q=S%C3%A3o%20Paulo
    private static final String TEMPO_URL = "http://dataservice.accuweather.com/locations/v1/cities/search?"; //LIVROS_URL
    private static final String PREVTEMPO_URL = "http://dataservice.accuweather.com/currentconditions/v1/"; //LIVROS_URL
    // Parametros da api key, necessário para consultar
    private static final String API_KEY = "apikey"; //QUERY_PARAM
    // Parametro de pesquisa que será enviado pelo usuário
    private static final String API_KEY1 = "apikey"; //QUERY_PARAM
    private static final String QUERY_PARAM= "q"; //maxResults
    // Parametro do idioma
    private static final String LANGUAGE = "language";
    // Parametro se deve retornar detalhes completos
    private static final String DETAILS = "details";
    // Parametro da qtde de objetos para retornar
    private static final String ID = "";
    static String buscaCodigoCi(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String cod = null;
        // Construção da URI de Busca
        Uri builtURI1;
        String CodigoJSONString = null;
        Log.d("asdas0", "asd");
        String UrlTestes="http://dataservice.accuweather.com/locations/v1/cities/search?apikey=uS4MACAkFEZHV7pqqM1HDPocakID0704&q=S%C3%A3o%20Paulo";

        try {

            builtURI1 = Uri.parse(TEMPO_URL).buildUpon()
                    .appendQueryParameter(API_KEY, "uS4MACAkFEZHV7pqqM1HDPocakID0704")
                    .appendQueryParameter(QUERY_PARAM, queryString)
                   // .appendQueryParameter(LANGUAGE, "pt-br")
                    .appendQueryParameter(DETAILS, "false")
                    .build();
            Log.d("asdas1", "asd" + builtURI1);
            // Converte a URI para a URL.

            URL requestURL = new URL(UrlTestes);
            Log.d("asdas2", "asd" + requestURL);
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            Log.d("asdas3", "asd" + urlConnection);
            urlConnection.setRequestMethod("GET");
            //ERRO AQUI
            urlConnection.connect();
            // Busca o InputStream.
            InputStream inputStream = urlConnection.getInputStream();
            // Cria o buffer para o input stream
            reader = new BufferedReader(new InputStreamReader(inputStream));
            // Usa o StringBuilder para receber a resposta.
            StringBuilder builder = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Adiciona a linha a string.
                builder.append(linha);
                builder.append("\n");
            }
            if (builder.length() == 0) {
                // se o stream estiver vazio não faz nada
                return null;
            }
            CodigoJSONString = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // fecha a conexão e o buffer.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
        }
        // escreve o Json no log
        Log.d(LOG_TAG, CodigoJSONString);
        String Chave=null;
        try {

            JSONArray itemsArray = new JSONArray(CodigoJSONString);
                JSONObject obj = itemsArray.getJSONObject(0);
                Chave = obj.getString("Key");

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return Chave;
    }



    static String buscaCodigoCidade(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String CodigoJSONString = null;
        String Codigo = null;
        Codigo = PegarCod.buscaCodigoCi(queryString);
        Uri builtURI1;
        // Construção da URI de Busca
        try {

            builtURI1= Uri.parse(PREVTEMPO_URL).buildUpon()
                    .appendQueryParameter( ID, queryString)
                    .appendQueryParameter(API_KEY1, "uS4MACAkFEZHV7pqqM1HDPocakID0704")
                    .appendQueryParameter(LANGUAGE, "pt-br")
                    .appendQueryParameter(DETAILS, "false")

                    .build();
            Log.d("asdas", "asd" + builtURI1);
            // Converte a URI para a URL.
            URL requestURL = new URL(builtURI1.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Busca o InputStream.
            InputStream inputStream = urlConnection.getInputStream();
            // Cria o buffer para o input stream
            reader = new BufferedReader(new InputStreamReader(inputStream));
            // Usa o StringBuilder para receber a resposta.
            StringBuilder builder = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Adiciona a linha a string.
                builder.append(linha);
                builder.append("\n");
            }
            if (builder.length() == 0) {
                // se o stream estiver vazio não faz nada
                return null;
            }
            CodigoJSONString = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // fecha a conexão e o buffer.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
        }
        // escreve o Json no log
        Log.d(LOG_TAG, CodigoJSONString);

        return CodigoJSONString;
        // http://dataservice.accuweather.com/locations/v1/cities/search?apikey=uS4MACAkFEZHV7pqqM1HDPocakID0704&q=s%C3%A3o%20paulo&language=pt-br

    }
}

