package com.example.luizeduardo.controledieta.controllers;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.luizeduardo.controledieta.R;
import com.example.luizeduardo.controledieta.helpers.AdapterListView;
import com.example.luizeduardo.controledieta.helpers.ItemListView;
import com.example.luizeduardo.controledieta.models.BancoDados;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Refeicoes extends Fragment {

    private SharedPreferences preferences;
    EditText nome = null, quantidadeCalorias = null, peso = null, data = null;
    private BancoDadosController db;
    private Cursor cursor;
    private ListView listView;
    private Button incluirRefeicao;
    private ArrayList<ItemListView> itens = new ArrayList<ItemListView>();
    String codigo;
    int posicao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adicionar_refeicao, null);

        preferences = getActivity().getSharedPreferences("CODIGO", Context.MODE_PRIVATE);
        codigo = preferences.getString("codigo", "");

        //Pega a referencia do ListView
        listView = (ListView) view.findViewById(R.id.listView);

        // Botão de incluir receita.
        incluirRefeicao = (Button) view.findViewById(R.id.incluirRefeicao);
        incluirRefeicao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                cricarCaixaDialogo("refeição");
            }
        });

        db = new BancoDadosController(getContext());
        cursor = db.carregaRefeicao(Integer.parseInt(codigo));
        cursor.moveToFirst();

        // Criando a lista de clientes.
        while (!cursor.isAfterLast()) {
            itens.add(new ItemListView(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.NOME_REFEICAO)),
                    "Data: " + cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.DATA_REFEICAO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.CALORIAS)) + "Kcal",
                    R.drawable.dieta));
            cursor.moveToNext();
        }

        //Define o Adapter
        listView.setAdapter(new AdapterListView(getContext(), itens));
        //Cor quando a lista é selecionada para ralagem.
        listView.setCacheColorHint(Color.TRANSPARENT);

        // Adicionando o evento em cada linha do listView.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                posicao = position;
                cursor.moveToPosition(position);
                cricarCaixaDialogo("Editar");
            }
        });
        return(view);
    }


    public void cricarCaixaDialogo (final String tipo){

        Button incluirSalvar, incluirCancelar;
        final Dialog dialog;

        LayoutInflater inf = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view2;

        view2 = inf.inflate( R.layout.incluir_caixa_dialogo, null);
        nome = (EditText) view2.findViewById(R.id.incluirNome);
        quantidadeCalorias = (EditText) view2.findViewById(R.id.incluirCalorias);
        peso = (EditText) view2.findViewById(R.id.incluirPeso);
        data = (EditText) view2.findViewById(R.id.incluirData);


        if(tipo.equals("Editar")){

            nome.setText(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.NOME_REFEICAO)));
            quantidadeCalorias.setText(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.CALORIAS)));
            peso.setText(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.PESO_REFEICAO)));
            data.setText(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.DATA_REFEICAO)));
        }

        incluirSalvar = (Button) view2.findViewById(R.id.incluirSalvar);
        incluirCancelar = (Button) view2.findViewById(R.id.incluirCancelar);

        dialog = new Dialog(getContext());
        dialog.setContentView(view2);
        dialog.setTitle((tipo.equals("Editar") ? "Editar refeição" : "Incluir " + tipo));
        dialog.show();

        incluirSalvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Map<String,String> dados = new HashMap<String, String>();
                String resultado, txtNome, txtPeso, strCalorias, txtData;

                txtNome = nome.getText().toString();
                strCalorias = quantidadeCalorias.getText().toString();
                txtPeso = peso.getText().toString();
                txtData = data.getText().toString();

                // Checando se os campos NOME, VALOR e DATA estão em branco.
                if (checaCamposRefeicao(txtNome, strCalorias, txtPeso, txtData)) {
                    Toast.makeText(getContext(), "Os campos não podem estar em branco!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                dados.put(BancoDados.ID_REFEICAO, String.valueOf(posicao));
                dados.put(BancoDados.ID_USUARIO_REFEICAO, codigo);
                dados.put(BancoDados.NOME_REFEICAO, txtNome);
                dados.put(BancoDados.CALORIAS, strCalorias);
                dados.put(BancoDados.PESO_REFEICAO, txtPeso);
                dados.put(BancoDados.DATA_REFEICAO, txtData);
                resultado = (tipo.equals("Editar") ? db.alteraRefeicao(dados) : db.inserirRefeicao(dados));

                Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ControleDieta.class);
                startActivity(intent);
                dialog.cancel();
            }

            boolean checaCamposRefeicao(String nome, String calorias, String peso, String data){

                return nome.equals("") || calorias.equals("") || peso.equals("") || data.equals("");
            }
        });

        incluirCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog.cancel();
            }
        });
    }
}
