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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luizeduardo.controledieta.R;
import com.example.luizeduardo.controledieta.models.BancoDados;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.HashMap;
import java.util.Map;

public class Dieta extends Fragment {

    SharedPreferences preferences;
    Cursor cursor;
    BancoDadosController db;
    EditText incluirData = null, quantidadeML = null, incluirCalorias = null, incluirPeso = null, incluirNome = null;
    TextView agua, calorias;
    String codigo;
    GraphView graph;
    int aguaToal = 0, caloriasTotal = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dieta, null);

        int pesoInicial = 0, pesoFinal = 0;
        String textAgua, textCalorias, usuCaloriasMax = "2400";

        preferences = getActivity().getSharedPreferences("CODIGO", Context.MODE_PRIVATE);
        codigo = preferences.getString("codigo", "");

        db = new BancoDadosController(getContext());
        cursor = db.carregaUsuarioById(Integer.parseInt(codigo));
        if(cursor.getCount() > 0){
            cursor = db.carregaUsuarioById(Integer.parseInt(codigo));
            pesoInicial = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.PESO_INICIAL)));
            pesoFinal = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.PESO_FINAL)));
            usuCaloriasMax = cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.CALORIAS_MAX));
        }

        /** AGUA **/

        cursor = db.carregaAgua(Integer.parseInt(codigo));
        aguaToal = 0;
        if(cursor.getCount() > 0){
            while (!cursor.isAfterLast()) {
                aguaToal += Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.QUANTIDADE_AGUA)));
                cursor.moveToNext();
            }
        }
        ((ImageView) view.findViewById(R.id.dietaImagemAgua)).setImageResource(R.drawable.copo_agua);
        agua = (TextView) view.findViewById(R.id.dietaAgua);
        textAgua = "Água consumida hoje:\n"+ aguaToal +"ml de 2000ml!!";
        agua.setText(textAgua);
        agua.setTextSize(20);
        agua.setTextColor(Color.BLUE);
        agua.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cricarCaixaDialogo("água");
            }
        });

        /** CALORIAS **/

        cursor = db.carregaRefeicao(Integer.parseInt(codigo));
        caloriasTotal = 0;
        if(cursor.getCount() > 0){
            while (!cursor.isAfterLast()) {
                caloriasTotal += Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.CALORIAS)));
                cursor.moveToNext();
            }
        }
        ((ImageView) view.findViewById(R.id.dietaImagemCaloria)).setImageResource(R.drawable.jantar);
        calorias = (TextView) view.findViewById(R.id.dietaCalorias);
        textCalorias = "Calorias consumidas hoje:\n"+ caloriasTotal + "kal de " + usuCaloriasMax + "kal!!";
        calorias.setText(textCalorias);
        calorias.setTextSize(20);
        calorias.setTextColor(Color.rgb(178, 34, 34));
        calorias.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cricarCaixaDialogo("refeição");
            }
        });


        /** GRAFICO **/

        graph = (GraphView) view.findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0),
                new DataPoint(1, pesoInicial),
                new DataPoint(2, pesoFinal),
                new DataPoint(4, 0)
        });
        graph.addSeries(series);
        graph.setHorizontalScrollBarEnabled(true);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"", "Peso inicial", "Peso atual", "", ""});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });
        series.setSpacing(50);
        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);

        return(view);
    }


    public void cricarCaixaDialogo (final String tipo){

        Button incluirSalvar, incluirCancelar;
        final Dialog dialog;

        LayoutInflater inf = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view2;

        if(tipo.equals("água")){

            view2 = inf.inflate(R.layout.incluir_agua_caixa_dialogo, null);
            quantidadeML = (EditText) view2.findViewById(R.id.quantidadeML);
            incluirData = (EditText) view2.findViewById(R.id.incluirData);

        }else{

            view2 = inf.inflate( R.layout.incluir_caixa_dialogo, null);
            incluirNome = (EditText) view2.findViewById(R.id.incluirNome);
            incluirCalorias = (EditText) view2.findViewById(R.id.incluirCalorias);
            incluirPeso = (EditText) view2.findViewById(R.id.incluirPeso);
            incluirData = (EditText) view2.findViewById(R.id.incluirData);
        }

        incluirSalvar = (Button) view2.findViewById(R.id.incluirSalvar);
        incluirCancelar = (Button) view2.findViewById(R.id.incluirCancelar);

        dialog = new Dialog(getContext());
        dialog.setContentView(view2);
        dialog.setTitle("Adicionar " + tipo);
        dialog.show();

        incluirSalvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Map<String,String> dados = new HashMap<String, String>();
                String strResultado, strNome, strQuantidadeML, strPeso, strCalorias, strData;

                if(tipo.equals("água")){

                    strQuantidadeML = quantidadeML.getText().toString();
                    strData = incluirData.getText().toString();

                    // Checando se os campos NOME, VALOR e DATA estão em branco.
                    if (checaCamposAgua(strQuantidadeML, strData)) {
                        Toast.makeText(getContext(), "Os campos não podem estar em branco!!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    dados.put(BancoDados.ID_USUARIO_AGUA, codigo);
                    dados.put(BancoDados.QUANTIDADE_AGUA, strQuantidadeML);
                    dados.put(BancoDados.DATA_AGUA, strData);
                    strResultado = db.inserirAgua(dados);
                }else{

                    strNome = incluirNome.getText().toString();
                    strCalorias = incluirCalorias.getText().toString();
                    strPeso = incluirPeso.getText().toString();
                    strData = incluirData.getText().toString();

                    // Checando se os campos NOME, VALOR e DATA estão em branco.
                    if (checaCamposRefeicao(strNome, strCalorias, strPeso, strData)) {
                        Toast.makeText(getContext(), "Os campos não podem estar em branco!!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    dados.put(BancoDados.ID_USUARIO_REFEICAO, codigo);
                    dados.put(BancoDados.NOME_REFEICAO, strNome);
                    dados.put(BancoDados.CALORIAS, strCalorias);
                    dados.put(BancoDados.PESO_REFEICAO, strPeso);
                    dados.put(BancoDados.DATA_REFEICAO, strData);
                    strResultado = db.inserirRefeicao(dados);
                }
                Toast.makeText(getContext(), strResultado, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ControleDieta.class);
                startActivity(intent);
                dialog.cancel();
            }

            boolean checaCamposAgua(String quantidade, String data){

                return quantidade.equals("") || data.equals("");
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