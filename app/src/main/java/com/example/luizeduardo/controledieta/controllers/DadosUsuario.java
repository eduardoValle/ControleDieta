package com.example.luizeduardo.controledieta.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.luizeduardo.controledieta.R;
import com.example.luizeduardo.controledieta.models.BancoDados;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luiz Eduardo on 15/12/2016.
 */

public class DadosUsuario extends FragmentActivity {

    private EditText dadosPeso, dadosAltura, dadosDataNasciemnto;
    private Spinner dadosSexo, dadosObjetivo, dadosAtividade;
    private BancoDadosController db;
    private Cursor cursor;
    private SharedPreferences preferences;
    private Map<String,String> dadosCliente = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dados_usuario);

        preferences = getSharedPreferences("CODIGO", MODE_PRIVATE);
        String intentEmail = getIntent().getStringExtra("email");

        // INICIANDO OS CAMPOS CRIADOS NO ARQUIVO DE LAYOUT.
        dadosPeso = (EditText) findViewById(R.id.dadosPeso);
        dadosAltura = (EditText) findViewById(R.id.dadosAltura);
        dadosDataNasciemnto = (EditText) findViewById(R.id.dadosDataNasciemnto);

        // Lista de categorias
        dadosSexo = (Spinner) findViewById(R.id.dadosSexo);
        ArrayAdapter listaSexo = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sexo, R.layout.spinner_item);
        dadosSexo.setAdapter(listaSexo);

        // Lista de objetivos
        dadosObjetivo = (Spinner) findViewById(R.id.dadosObjetivo);
        ArrayAdapter listaObjetivo = ArrayAdapter.createFromResource(getApplicationContext(), R.array.objetivo, R.layout.spinner_item);
        dadosObjetivo.setAdapter(listaObjetivo);

        // Lista de atividades
        dadosAtividade = (Spinner) findViewById(R.id.dadosAtividade);
        ArrayAdapter listaAtividade = ArrayAdapter.createFromResource(getApplicationContext(), R.array.atividade, R.layout.spinner_item);
        dadosAtividade.setAdapter(listaAtividade);

        db = new BancoDadosController(getApplicationContext());
        cursor = db.carregaUsuarioByEmail(intentEmail);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("codigo", cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.ID_USUARIO)));
        editor.apply();

        Button salvar = (Button) findViewById(R.id.dadosSalvar);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id, nome, email, senha, peso, altuta, sexo, dataNasciemnto, objetivo, atividade;
                id = cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.ID_USUARIO));
                nome = cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.NOME_USUARIO));
                email = cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.EMAIL));
                senha = cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.SENHA));
                peso = dadosPeso.getText().toString();
                altuta = dadosAltura.getText().toString();
                sexo = dadosSexo.getSelectedItem().toString();
                dataNasciemnto = dadosDataNasciemnto.getText().toString();
                objetivo = dadosObjetivo.getSelectedItem().toString();
                atividade = dadosAtividade.getSelectedItem().toString();

                if(!checarCampos(peso, altuta, dataNasciemnto, objetivo)) {

                    dadosCliente.put(BancoDados.ID_USUARIO, id);
                    dadosCliente.put(BancoDados.NOME_USUARIO, nome);
                    dadosCliente.put(BancoDados.EMAIL, email);
                    dadosCliente.put(BancoDados.SENHA, senha);
                    dadosCliente.put(BancoDados.PESO_FINAL, peso);
                    dadosCliente.put(BancoDados.ALTURA, altuta);
                    dadosCliente.put(BancoDados.SEXO, sexo);
                    dadosCliente.put(BancoDados.DATA_NASCIMENTO, dataNasciemnto);
                    dadosCliente.put(BancoDados.OBJETIVO, objetivo);
                    dadosCliente.put(BancoDados.ATIVIDADE, atividade);

                    String resultado = db.alteraDadosUsuario(dadosCliente, true);

                    Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), ControleDieta.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Os campos de PESO, ALTURA e OBJETIVO não podem estar em branco!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Checa se os campos de PESO, ALTURA e OBJETIVO estão em branco.
     * @return true se estão em branco, false se não estão
     */
    public boolean checarCampos(String peso, String altuta, String dataNasciemnto, String objetivo){

        return peso.equals("") || altuta.equals("") || dataNasciemnto.equals("") || objetivo.equals("");
    }
}