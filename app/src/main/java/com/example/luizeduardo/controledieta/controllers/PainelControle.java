package com.example.luizeduardo.controledieta.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.luizeduardo.controledieta.R;
import com.example.luizeduardo.controledieta.helpers.AdapterListView;
import com.example.luizeduardo.controledieta.helpers.ItemListView;
import com.example.luizeduardo.controledieta.models.BancoDados;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Luiz Eduardo on 13/12/2016.
 */

public class PainelControle  extends Fragment {

    private Cursor cursor;
    private BancoDadosController db;
    private SharedPreferences preferences;
    private ListView listView;
    private ArrayList<ItemListView> itens;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.painel_controle, null);

        String codigo;
        double caloriasMax, caloriasCafeManha, caloriasLancheManha, caloriasAlmoco, caloriasCafeTarde, caloriasjantar, caloriasLancheJantar;
        preferences = getActivity().getSharedPreferences("CODIGO", Context.MODE_PRIVATE);
        codigo = preferences.getString("codigo", "");

        db = new BancoDadosController(getContext());
        cursor = db.carregaUsuarioById(Integer.parseInt(codigo));

        caloriasMax = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.CALORIAS_MAX)));

        DecimalFormat formato = new DecimalFormat("#.#");

        caloriasCafeManha = Double.valueOf(formato.format((21.20 * caloriasMax)/100));
        caloriasLancheManha = Double.valueOf(formato.format((3.47 * caloriasMax)/100));
        caloriasAlmoco = Double.valueOf(formato.format((31.06 * caloriasMax)/100));
        caloriasCafeTarde = Double.valueOf(formato.format((10.65 * caloriasMax)/100));
        caloriasjantar = Double.valueOf(formato.format((26.92 * caloriasMax)/100));
        caloriasLancheJantar = Double.valueOf(formato.format((6.70 * caloriasMax)/100));

        //Pega a referencia do ListView
        listView = (ListView) view.findViewById(R.id.listView);

        //Criamos nossa lista que preenchera o ListView
        itens = new ArrayList<ItemListView>();

        itens.add(new ItemListView("Água",
                "Horario: a cada 1H",
                "150ml",
                R.drawable.copo_agua));

        itens.add(new ItemListView("Café da manhã",
                "Horario: 07:00",
                caloriasCafeManha + "Kcal",
                R.drawable.cafe));

        itens.add(new ItemListView("frutas",
                "Horario: 10:00",
                caloriasLancheManha + "Kcal",
                R.drawable.frutas));

        itens.add(new ItemListView("Almoço",
                "Horario: 13:00",
                caloriasAlmoco + "Kcal",
                R.drawable.jantar));

        itens.add(new ItemListView("Café da tarde",
                "Horario: 16:00",
                caloriasCafeTarde + "Kcal",
                R.drawable.cafe));

        itens.add(new ItemListView("Jantar",
                "Horario: 19:00",
                caloriasjantar + "Kcal",
                R.drawable.jantar));

        itens.add(new ItemListView("Lanche noturno",
                "Horario: 22:00",
                caloriasLancheJantar + "Kcal",
                R.drawable.frutas));

        //Define o Adapter
        listView.setAdapter(new AdapterListView(getContext(), itens));
        //Cor quando a lista é selecionada para ralagem.
        listView.setCacheColorHint(Color.TRANSPARENT);

        // Adicionando o evento em cada linha do listView.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String codigo = itens.get(position).getNome();

                //Intent intent = new Intent(PainelControle.this, EditarCliente.class);
                //intent.putExtra("codigo", codigo);
                //startActivity(intent);
                //finish();
            }
        });

        return (view);
    }
}