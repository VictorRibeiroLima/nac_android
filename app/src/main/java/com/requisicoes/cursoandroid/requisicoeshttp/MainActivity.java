package com.requisicoes.cursoandroid.requisicoeshttp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button botaoRecuperar;
    private TextView resultadoCep;
    private TextView resultadoLogradouro;
    private TextView resultadoComplemento;
    private TextView resultadoBairro;
    private TextView resultadoLocalidade;
    private TextView resultadoUf;
    private EditText cep;
    private String cepPesquisa;
    private  TextView resultadoGeral;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setContentView(R.layout.activity_main);
        cep = findViewById(R.id.cep);
        cep.addTextChangedListener(new EditorCep("#####-###"));
        botaoRecuperar = findViewById(R.id.buttonRecuperar);
        resultadoGeral = findViewById(R.id.resultadoGeral);
        resultadoCep = findViewById(R.id.resultadoCep);
        resultadoLogradouro = findViewById(R.id.resultadoLogradouro);
        resultadoComplemento = findViewById(R.id.resultadoComplemento);
        resultadoBairro = findViewById(R.id.resultadoBairro);
        resultadoLocalidade = findViewById(R.id.resultadoLocalidade);
        resultadoUf = findViewById(R.id.resultadoUf);
        botaoRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTask task = new MyTask();
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                cepPesquisa = cep.getText().toString().replace("-","");
                String urlCep = "https://viacep.com.br/ws/" + cepPesquisa + "/json/";
                task.execute(urlCep);
            }
        });

    }

    class MyTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;

            try {

                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                if(conexao.getResponseCode() == 200) {
                    // Recupera os dados em Bytes
                    inputStream = conexao.getInputStream();

                    //inputStreamReader lê os dados em Bytes e decodifica para caracteres
                    inputStreamReader = new InputStreamReader(inputStream);

                    //Objeto utilizado para leitura dos caracteres do InpuStreamReader
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    buffer = new StringBuffer();
                    String linha = "";

                    while ((linha = reader.readLine()) != null) {
                        buffer.append(linha);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer != null? buffer.toString():null;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            cep.setText("");
            String logradouro = null;
            String cep = null;
            String complemento = null;
            String bairro = null;
            String localidade = null;
            String uf = null;
            if(resultado != null) {
                try {
                    JSONObject jsonObject = new JSONObject(resultado);
                    logradouro = jsonObject.getString("logradouro");
                    cep = jsonObject.getString("cep");
                    complemento = jsonObject.getString("complemento");
                    bairro = jsonObject.getString("bairro");
                    localidade = jsonObject.getString("localidade");
                    uf = jsonObject.getString("uf");
                    resultadoGeral.setText("CEP pesquisado com sucesso");
                    resultadoGeral.setVisibility(View.VISIBLE);
                    resultadoCep.setText("CEP: " + cep);
                    resultadoCep.setVisibility(View.VISIBLE);
                    resultadoLogradouro.setText("Logradouro: " + logradouro);
                    resultadoLogradouro.setVisibility(View.VISIBLE);
                    resultadoComplemento.setText("Complemento: " + complemento);
                    resultadoComplemento.setVisibility(View.VISIBLE);
                    resultadoBairro.setText("Bairro: " + bairro);
                    resultadoBairro.setVisibility(View.VISIBLE);
                    resultadoLocalidade.setText("Localidade: " + localidade);
                    resultadoLocalidade.setVisibility(View.VISIBLE);
                    resultadoUf.setText("UF: " + uf);
                    resultadoUf.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    resultadoGeral.setText("CEP não existe");
                    resultadoGeral.setVisibility(View.VISIBLE);
                    resultadoCep.setVisibility(View.INVISIBLE);
                    resultadoLogradouro.setVisibility(View.INVISIBLE);
                    resultadoComplemento.setVisibility(View.INVISIBLE);
                    resultadoBairro.setVisibility(View.INVISIBLE);
                    resultadoLocalidade.setVisibility(View.INVISIBLE);
                    resultadoUf.setVisibility(View.INVISIBLE);
                }
            }else{
                resultadoGeral.setText("CEP mal formado");
                resultadoGeral.setVisibility(View.VISIBLE);
                resultadoCep.setVisibility(View.INVISIBLE);
                resultadoLogradouro.setVisibility(View.INVISIBLE);
                resultadoComplemento.setVisibility(View.INVISIBLE);
                resultadoBairro.setVisibility(View.INVISIBLE);
                resultadoLocalidade.setVisibility(View.INVISIBLE);
                resultadoUf.setVisibility(View.INVISIBLE);
            }
        }
    }

}
