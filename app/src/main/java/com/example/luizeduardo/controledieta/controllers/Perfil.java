package com.example.luizeduardo.controledieta.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class Perfil extends Fragment {

    private EditText perfNome, perfEmail, perfSenha, perfPeso, perfAltura;
    private Spinner perfObjetivo, perfSexo, perfAtividade;
    private Button salvar;
    private BancoDadosController db;
    private Cursor cursor;
    private String codigo;
    private Map<String,String> dadosCliente = new HashMap<String, String>();
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.perfil, null);

        preferences = getActivity().getSharedPreferences("CODIGO", Context.MODE_PRIVATE);
        codigo = preferences.getString("codigo", "");
        //codigo = getActivity().getIntent().getStringExtra("codigo");

        // INICIANDO OS CAMPOS CRIADOS NO ARQUIVO activity_main.xml
        perfNome = (EditText) view.findViewById(R.id.perfNome);
        perfEmail = (EditText) view.findViewById(R.id.perfEmail);
        perfSenha = (EditText) view.findViewById(R.id.perfSenha);
        perfPeso = (EditText) view.findViewById(R.id.perfPeso);
        perfAltura = (EditText) view.findViewById(R.id.perfAltura);

        // Lista de sexo
        perfSexo= (Spinner) view.findViewById(R.id.perfSexo);
        ArrayAdapter listaSexo = ArrayAdapter.createFromResource(getContext(), R.array.sexo, android.R.layout.simple_spinner_item);
        perfSexo.setAdapter(listaSexo);

        // Lista de objetivo
        perfObjetivo = (Spinner) view.findViewById(R.id.perfObjetivo);
        ArrayAdapter listaObjetivo = ArrayAdapter.createFromResource(getContext(), R.array.objetivo, android.R.layout.simple_spinner_item);
        perfObjetivo.setAdapter(listaObjetivo);

        // Lista de atividade
        perfAtividade = (Spinner) view.findViewById(R.id.perfAtividade);
        ArrayAdapter listaAtividade = ArrayAdapter.createFromResource(getContext(), R.array.atividade, android.R.layout.simple_spinner_item);
        perfAtividade.setAdapter(listaAtividade);

        db = new BancoDadosController(getContext());
        cursor = db.carregaUsuarioById(Integer.parseInt(codigo));
        perfNome.setText(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.NOME_USUARIO)));
        perfEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.EMAIL)));
        perfSenha.setText(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.SENHA)));
        perfPeso.setText(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.PESO_FINAL)));
        perfAltura.setText(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.ALTURA)));
        perfSexo.setSelection(listaSexo.getPosition(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.SEXO))));
        perfObjetivo.setSelection(listaObjetivo.getPosition(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.OBJETIVO))));
        perfAtividade.setSelection(listaAtividade.getPosition(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.ATIVIDADE))));

        salvar = (Button)view.findViewById(R.id.perfAtualizarDados);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome, email, senha, peso, altura, sexo, objetivo, atividade, nascimento;
                nome = perfNome.getText().toString();
                email = perfEmail.getText().toString();
                senha = perfSenha.getText().toString();
                peso = perfPeso.getText().toString();
                altura = perfAltura.getText().toString();
                sexo = perfSexo.getSelectedItem().toString();
                objetivo = perfObjetivo.getSelectedItem().toString();
                atividade = perfAtividade.getSelectedItem().toString();
                nascimento = cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.DATA_NASCIMENTO));

                dadosCliente.put(BancoDados.ID_USUARIO, codigo);
                dadosCliente.put(BancoDados.NOME_USUARIO, nome);
                dadosCliente.put(BancoDados.EMAIL, email);
                dadosCliente.put(BancoDados.SENHA, senha);
                dadosCliente.put(BancoDados.PESO_FINAL, peso);
                dadosCliente.put(BancoDados.ALTURA, altura);
                dadosCliente.put(BancoDados.SEXO, sexo);
                dadosCliente.put(BancoDados.OBJETIVO, objetivo);
                dadosCliente.put(BancoDados.ATIVIDADE, atividade);
                dadosCliente.put(BancoDados.DATA_NASCIMENTO, nascimento);

                if(!checarCampos(nome, email, senha, peso, altura, objetivo, atividade, sexo)) {
                    String resultado = db.alteraDadosUsuario(dadosCliente, false);
                    Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), ControleDieta.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(), "Os campos não podem estar em branco!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return(view);
    }

    /**
     * Checa se as Strings passadas estão em branco estão em branco.
     * @return true se estão em branco, false se não estão
     */
    public boolean checarCampos(String nome, String email, String senha, String peso, String altura, String objetivo, String atividade, String sexo){

        return nome.equals("") || email.equals("") || senha.equals("")|| peso.equals("")|| altura.equals("")|| objetivo.equals("")|| atividade.equals("")|| sexo.equals("");
    }
}