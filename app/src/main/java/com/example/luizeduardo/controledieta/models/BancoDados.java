package com.example.luizeduardo.controledieta.models;

/**
 * Created by Luiz Eduardo on 02/12/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoDados extends SQLiteOpenHelper {

    public BancoDados(Context context){
        super(context, NOME_BANCO,null,VERSAO);
    }

    public static final String NOME_BANCO = "banco.db";

    public static final String TABELA_USUARIO = "usuario";
    public static final String ID_USUARIO = "_id";
    public static final String NOME_USUARIO = "nome";
    public static final String EMAIL = "email";
    public static final String SENHA = "senha";
    public static final String PESO_INICIAL = "peso_inicial";
    public static final String PESO_FINAL = "peso_final";
    public static final String ALTURA = "altura";
    public static final String SEXO = "sexo";
    public static final String AGUA_MAX = "agua_max";
    public static final String CALORIAS_MAX = "caloria_max";
    public static final String OBJETIVO = "objetivo";
    public static final String ATIVIDADE = "atividade";
    public static final String DATA_NASCIMENTO = "data_nascimento";
    public static final String DATA_CADASTRO = "data_cadastro";

    public static final String TABELA_REFEICOES = "refeicoes";
    public static final String ID_REFEICAO= "_id";
    public static final String ID_USUARIO_REFEICAO= "id_usuario_refeicao";
    public static final String NOME_REFEICAO = "nome_refeicao";
    public static final String CALORIAS = "calorias";
    public static final String PESO_REFEICAO = "peso";
    public static final String DATA_REFEICAO = "data_refeicao";

    public static final String TABELA_AGUA = "quantidade_agua";
    public static final String ID_AGUA = "_id";
    public static final String ID_USUARIO_AGUA = "id_usuario_agua";
    public static final String QUANTIDADE_AGUA = "quantidade_agua";
    public static final String DATA_AGUA = "data_agua";

    public static final int VERSAO = 4;

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE "+ TABELA_USUARIO +" ("
                + ID_USUARIO + " integer primary key autoincrement,"
                + NOME_USUARIO + " text,"
                + EMAIL + " text,"
                + SENHA + " text,"
                + PESO_INICIAL + " text,"
                + PESO_FINAL + " text,"
                + ALTURA + " text,"
                + SEXO + " text,"
                + OBJETIVO + " text,"
                + ATIVIDADE + " text,"
                + AGUA_MAX + " text,"
                + CALORIAS_MAX + " text,"
                + DATA_NASCIMENTO + " text,"
                + DATA_CADASTRO + " text"
                +")";
        db.execSQL(sql);

        sql = "CREATE TABLE "+ TABELA_REFEICOES +" ("
                + ID_REFEICAO + " integer primary key autoincrement,"
                + ID_USUARIO_REFEICAO + " text,"
                + NOME_REFEICAO + " text,"
                + CALORIAS + " text,"
                + PESO_REFEICAO + " text,"
                + DATA_REFEICAO + " text"
                +")";
        db.execSQL(sql);

        sql = "CREATE TABLE "+ TABELA_AGUA +" ("
                + ID_AGUA+ " integer primary key autoincrement,"
                + ID_USUARIO_AGUA + " text,"
                + QUANTIDADE_AGUA + " text,"
                + DATA_AGUA + " text"
                +")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_REFEICOES);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_AGUA);
        onCreate(db);
    }
}
