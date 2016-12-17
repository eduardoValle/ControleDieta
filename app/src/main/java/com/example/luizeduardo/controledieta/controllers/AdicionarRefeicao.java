package com.example.luizeduardo.controledieta.controllers;

/**
 * Created by Luiz Eduardo on 13/12/2016.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

public class AdicionarRefeicao extends Fragment {

    private BancoDadosController db;
    private Cursor cursor;
    private ListView listView;
    private Button incluirReceita, incluirDespesa;
    private ArrayList<ItemListView> itens = new ArrayList<ItemListView>();
    String editar = "";
    int posicao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adicionar_refeicao, null);

        //Pega a referencia do ListView
        listView = (ListView) view.findViewById(R.id.listView);

        // Botão de incluir receita.
        incluirReceita = (Button) view.findViewById(R.id.incluirRefeicao);
        incluirReceita.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                cricarCaixaDialogo("receita");
            }
        });

        // Botão de incluir receita.
        incluirDespesa = (Button) view.findViewById(R.id.incluirRefeicao);
        incluirDespesa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                cricarCaixaDialogo("despesa");
            }
        });

        db = new BancoDadosController(getContext());
        cursor = db.carregaUsuario();
        cursor.moveToFirst();

        // Criando a lista de clientes.
        while (!cursor.isAfterLast()) {
            itens.add(new ItemListView(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.NOME_USUARIO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.EMAIL)),
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
                String codigo = itens.get(posicao).getNome();
                cursor.moveToPosition(position);
                cricarCaixaDialogo("Editar");
            }
        });
        return(view);
    }


    public void cricarCaixaDialogo (final String tipo){

        final EditText incluirNome, incluirDescricao, incluirValor, incluirData;
        Button incluirSalvar, incluirCancelar;
        final Dialog dialog;

        LayoutInflater inf = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view2 = inf.inflate(R.layout.incluir_caixa_dialogo, null);

        incluirNome = (EditText) view2.findViewById(R.id.incluirNome);
        incluirDescricao = (EditText) view2.findViewById(R.id.incluirPeso);
        incluirValor = (EditText) view2.findViewById(R.id.incluirPeso);
        incluirData = (EditText) view2.findViewById(R.id.incluirData);
        incluirSalvar = (Button) view2.findViewById(R.id.incluirSalvar);
        incluirCancelar = (Button) view2.findViewById(R.id.incluirCancelar);

        if(tipo.equals("Editar")){

            incluirNome.setText(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.NOME_USUARIO)));
            incluirDescricao.setText(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.EMAIL)));
            incluirValor.setText(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.EMAIL)));
            incluirData.setText(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.EMAIL)));
            editar = cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.EMAIL));
        }

        dialog = new Dialog(getContext());
        dialog.setContentView(view2);
        dialog.setTitle((tipo.equals("Editar")? "Editar " + editar : "Incluir " + tipo));
        dialog.show();

        incluirSalvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Map<String,String> dados = new HashMap<String, String>();
                String nome, descricao, valor, data;

                nome = incluirNome.getText().toString();
                descricao = incluirDescricao.getText().toString();
                valor = incluirValor.getText().toString();
                data = incluirData.getText().toString();

                // Checando se os campos NOME, VALOR e DATA estão em branco.
                if (checaCampos(nome, valor, data)) {
                    Toast.makeText(getContext(), "Os campos de NOME, VALOR e DATA não podem estar em branco.", Toast.LENGTH_SHORT).show();
                    return;
                }

                dados.put("nome", nome);
                dados.put("descricao", descricao);
                dados.put("tipo", (tipo.equals("Editar") ? editar : tipo));
                dados.put("valor", valor);
                dados.put("data", data);

                if(tipo.equals("Editar")){
                    dados.put("_id", cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.ID_USUARIO)));
                }

                String resultado = db.inserirUsuario(dados);
                Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), ControleDieta.class);
                startActivity(intent);
                dialog.cancel();
            }

            public boolean checaCampos(String nome, String valor, String data){

                if(nome.equals("") || valor.equals("") || data.equals("")){
                    return true;
                }else{
                    return false;
                }
            }
        });

        incluirCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog.cancel();
            }
        });
    }
}