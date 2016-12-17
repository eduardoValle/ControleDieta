package com.example.luizeduardo.controledieta.controllers;

/**
 * Created by Luiz Eduardo on 02/12/2016.
 */

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;

import com.example.luizeduardo.controledieta.R;

public class ControleDieta extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controle_dieta);

        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // TABS
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        Tab home = ab.newTab();
        home.setText("Dieta");
        home.setTabListener(new NavegacaoTabs(new Dieta()));
        ab.addTab(home, false);

        Tab incluir = ab.newTab();
        incluir.setText("Refeições");
        incluir.setTabListener(new NavegacaoTabs(new Refeicoes()));
        ab.addTab(incluir, false);

        Tab detalhes = ab.newTab();
        detalhes.setText("Controle");
        detalhes.setTabListener(new NavegacaoTabs(new PainelControle()));
        ab.addTab(detalhes, false);

        Tab perfil = ab.newTab();
        perfil.setText("Perfil");
        perfil.setTabListener(new NavegacaoTabs(new Perfil()));
        ab.addTab(perfil, false);

        if(savedInstanceState != null){
            int indiceTab = savedInstanceState.getInt("indiceTab");
            getActionBar().setSelectedNavigationItem(indiceTab);
        } else{
            getActionBar().setSelectedNavigationItem(0);
        }
    }


    private class NavegacaoTabs implements ActionBar.TabListener {
        private Fragment frag;

        public NavegacaoTabs(Fragment frag){
            this.frag = frag;
        }

        @Override
        public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
            Log.i("Script", "onTabReselected()");
        }

        @Override
        public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
            FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
            fts.replace(R.id.fragmentContainer, frag);
            fts.commit();
        }

        @Override
        public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
            FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
            fts.remove(frag);
            fts.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("indiceTab", getActionBar().getSelectedNavigationIndex());
    }
}
