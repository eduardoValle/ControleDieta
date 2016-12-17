package com.example.luizeduardo.controledieta.helpers;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by Luiz Eduardo on 17/12/2016.
 */

public class Helpers {

    /**
     * Calcula a quantidade da calorias que a pessoa pode ingerir.
     * @param peso peso em KG.
     * @param altura altura em centímetros.
     * @param nascimento nascimento no usuário.
     * @return
     */
    public static String calcularCalorias(String peso, String altura, String nascimento, String sexo, String objetivo, String atividade){

        double caloriasMax, pessoMax, alturaMax, idadeMax;
        int idade;

        String [] data = nascimento.split("-");

        idade = Integer.parseInt(String.valueOf(Calendar.getInstance().get(Calendar.YEAR))) - Integer.parseInt(data[2]);

        if(sexo.equals("Masxulino")){

            pessoMax = Double.parseDouble(peso) * 13.7;
            alturaMax = Double.parseDouble(altura) * 5;
            idadeMax = idade * 6.8;
            caloriasMax = 66 + pessoMax + alturaMax - idadeMax;
        }else{

            pessoMax = Double.parseDouble(peso) * 9.6;
            alturaMax = Double.parseDouble(altura) * 1.8;
            idadeMax = idade * 4.7;
            caloriasMax = 655 + pessoMax + alturaMax - idadeMax;
        }

        switch (atividade){

            case "Sedentário":

                caloriasMax = caloriasMax * 1.2;
                break;
            case "Levemente ativo":

                caloriasMax = caloriasMax * 1.375;
                break;
            case "Moderadamente ativo":

                caloriasMax = caloriasMax * 1.55;
                break;
            case "Altamente ativo":

                caloriasMax = caloriasMax * 1.725;
                break;
            case "Extremamente ativo":

                caloriasMax = caloriasMax * 1.9;
                break;
        }

        switch (objetivo){

            case "Perder peso":

                caloriasMax = caloriasMax - 500;
                break;
            case "Manter peso":

                caloriasMax = caloriasMax + 0;
                break;
            case "Ganhar massa muscular":

                caloriasMax = caloriasMax + 500;
                break;
        }

        DecimalFormat formato = new DecimalFormat("#.###");
        caloriasMax = Double.valueOf(formato.format(caloriasMax));
        return  String.valueOf(caloriasMax);
    }
}
