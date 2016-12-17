package com.example.luizeduardo.controledieta.controllers;

/**
 * Created by Luiz Eduardo on 02/12/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.luizeduardo.controledieta.helpers.Helpers;
import com.example.luizeduardo.controledieta.models.BancoDados;

import java.util.Map;

public class BancoDadosController {

    private SQLiteDatabase db;
    private BancoDados banco;
    private Log log;

    public BancoDadosController(Context context){
        banco = new BancoDados(context);
    }

    /******************
     * TABELA USUÁRIO
     *****************/

    /**
     * Insere um usuário no banco de dados.
     * @param dados Hash Map contento os dados do usuário.
     * @return Mensagem de resposta da oparação.
     */
    public String inserirUsuario(Map<String,String> dados){

        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(BancoDados.NOME_USUARIO, dados.get(BancoDados.NOME_USUARIO));
        valores.put(BancoDados.EMAIL, dados.get(BancoDados.EMAIL));
        valores.put(BancoDados.SENHA, dados.get(BancoDados.SENHA));
        valores.put(BancoDados.DATA_CADASTRO, dados.get(BancoDados.DATA_CADASTRO));
        resultado = db.insert(BancoDados.TABELA_USUARIO, null, valores);
        db.close();

        if (resultado ==-1)
            return "Erro ao inserir registro!!";
        else
            return "Registro inserido com sucesso!!";
    }

    /**
     * Pega no banco todos os usuários cadastrados.
     * @return todos os usuários cadastrados.
     */
    public Cursor carregaUsuario(){
        Cursor cursor;
        db = banco.getReadableDatabase();
        cursor = db.query(BancoDados.TABELA_USUARIO, null, null, null, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    /**
     * Procura no banco o usuário do id passado como parâmetro.
     * @param id do usuário a procurar.
     * @return dados do usuário procurado, se exixtir.
     */
    public Cursor carregaUsuarioById(int id){
        Cursor cursor;
        String where = BancoDados.ID_USUARIO + "=" + id;
        db = banco.getReadableDatabase();
        cursor = db.query(BancoDados.TABELA_USUARIO, null, where, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    /**
     * Procura no banco o usuário do email passado como parâmetro.
     * @param email do usuário a procurar.
     * @return dados do usuário procurado, se exixtir.
     */
    public Cursor carregaUsuarioByEmail(String email){
        Cursor cursor;
        String where = BancoDados.EMAIL + "= '" + email + "'";
        db = banco.getReadableDatabase();
        cursor = db.query(BancoDados.TABELA_USUARIO, null, where, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    /**
     * Altera o registro do usuário do id passado como parâmetro.
     * @param dados Hash Map contento os dados do usuário.
     * @return Mensagem de resposta da oparação.
     */
    public String alteraDadosUsuario(Map<String,String> dados, boolean primeiroInsert){

        ContentValues valores;
        long resultado;
        String nome, email, senha, peso_final, calorias_max, altura, sexo, data_nascimento, objetivo, atividade;
        String where = BancoDados.ID_USUARIO + "=" + dados.get(BancoDados.ID_USUARIO);
        db = banco.getWritableDatabase();

        nome = dados.get(BancoDados.NOME_USUARIO);
        email = dados.get(BancoDados.EMAIL);
        senha = dados.get(BancoDados.SENHA);
        peso_final = dados.get(BancoDados.PESO_FINAL);
        altura = dados.get(BancoDados.ALTURA);
        sexo = dados.get(BancoDados.SEXO);
        data_nascimento = dados.get(BancoDados.DATA_NASCIMENTO);
        objetivo = dados.get(BancoDados.OBJETIVO);
        atividade = dados.get(BancoDados.ATIVIDADE);

        calorias_max = Helpers.calcularCalorias(peso_final, altura, data_nascimento, sexo, objetivo, atividade);

        valores = new ContentValues();
        valores.put(BancoDados.NOME_USUARIO, nome);
        valores.put(BancoDados.EMAIL, email);
        valores.put(BancoDados.SENHA, senha);
        valores.put(BancoDados.PESO_FINAL, peso_final);
        valores.put(BancoDados.ALTURA, altura);
        valores.put(BancoDados.SEXO, sexo);
        valores.put(BancoDados.DATA_NASCIMENTO, data_nascimento);
        valores.put(BancoDados.OBJETIVO, objetivo);
        valores.put(BancoDados.ATIVIDADE, atividade);
        valores.put(BancoDados.CALORIAS_MAX, calorias_max);

        if(primeiroInsert){
            valores.put(BancoDados.PESO_INICIAL, peso_final);
        }

        resultado = db.update(BancoDados.TABELA_USUARIO, valores, where,null);
        db.close();

        if (resultado ==-1)
            return "Erro ao alterar registro!!";
        else
            return "Registro alterado com sucesso!!";
    }


    /**
     * Exclui o usuário do id passado como parâmetro.
     * @param id id do usuário a excluir.
     * @return Mensagem de resposta da oparação.
     */
    public String deletaUsuario(int id){

        long resultado;
        String where = BancoDados.ID_USUARIO + "=" + id;
        db = banco.getReadableDatabase();
        resultado = db.delete(BancoDados.TABELA_USUARIO, where, null);
        db.close();

        if (resultado ==-1)
            return "Erro ao apagar registro!!";
        else
            return "Registro apagado com sucesso!!";
    }


    /******************
     * TABELA AGUA
     *****************/

    public String inserirAgua(Map<String,String> dados){

        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(BancoDados.ID_USUARIO_AGUA, dados.get(BancoDados.ID_USUARIO_AGUA));
        valores.put(BancoDados.QUANTIDADE_AGUA, dados.get(BancoDados.QUANTIDADE_AGUA));
        valores.put(BancoDados.DATA_AGUA, dados.get(BancoDados.DATA_AGUA));
        resultado = db.insert(BancoDados.TABELA_AGUA, null, valores);
        db.close();

        if (resultado ==-1)
            return "Erro ao inserir registro!!";
        else
            return "Registro inserido com sucesso!!";
    }

    /**
     * Pega no banco todos os usuários cadastrados.
     * @return todos os usuários cadastrados.
     */
    public Cursor carregaAgua(int id){
        Cursor cursor;
        String where = BancoDados.ID_USUARIO_AGUA + "=" + id;
        String[] campos = {BancoDados.ID_AGUA, BancoDados.ID_USUARIO_AGUA, BancoDados.QUANTIDADE_AGUA, BancoDados.DATA_AGUA};
        db = banco.getReadableDatabase();
        cursor = db.query(BancoDados.TABELA_AGUA, campos, where, null, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    /********************
     * TABELA REFEICOES *
     *******************/

    public String inserirRefeicao(Map<String,String> dados){

        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(BancoDados.ID_USUARIO_REFEICAO, dados.get(BancoDados.ID_USUARIO_REFEICAO));
        valores.put(BancoDados.NOME_REFEICAO, dados.get(BancoDados.NOME_REFEICAO));
        valores.put(BancoDados.CALORIAS, dados.get(BancoDados.CALORIAS));
        valores.put(BancoDados.PESO_REFEICAO, dados.get(BancoDados.PESO_REFEICAO));
        valores.put(BancoDados.DATA_REFEICAO, dados.get(BancoDados.DATA_REFEICAO));
        resultado = db.insert(BancoDados.TABELA_REFEICOES, null, valores);
        db.close();

        if (resultado ==-1)
            return "Erro ao inserir registro!!";
        else
            return "Registro inserido com sucesso!!";
    }

    /**
     * Pega no banco todos os usuários cadastrados.
     * @return todos os usuários cadastrados.
     */
    public Cursor carregaRefeicao(int id){
        Cursor cursor;
        String where = BancoDados.ID_USUARIO_REFEICAO + "=" + id;
        String[] campos = {BancoDados.ID_REFEICAO, BancoDados.ID_USUARIO_REFEICAO, BancoDados.NOME_REFEICAO,
                BancoDados.CALORIAS, BancoDados.PESO_REFEICAO, BancoDados.DATA_REFEICAO,};
        db = banco.getReadableDatabase();
        cursor = db.query(BancoDados.TABELA_REFEICOES, campos, where, null, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public String alteraRefeicao(Map<String,String> dados){

        ContentValues valores;
        long resultado;
        db = banco.getWritableDatabase();
        String where = BancoDados.ID_REFEICAO + "=" + dados.get(BancoDados.ID_REFEICAO);
        valores = new ContentValues();
        valores.put(BancoDados.NOME_REFEICAO, dados.get(BancoDados.NOME_REFEICAO));
        valores.put(BancoDados.CALORIAS, dados.get(BancoDados.CALORIAS));
        valores.put(BancoDados.PESO_REFEICAO, dados.get(BancoDados.PESO_REFEICAO));
        valores.put(BancoDados.DATA_REFEICAO, dados.get(BancoDados.DATA_REFEICAO));

        resultado = db.update(BancoDados.TABELA_REFEICOES, valores, where, null);
        db.close();

        if (resultado ==-1)
            return "Erro ao alterar registro!!";
        else
            return "Registro alterado com sucesso!!";
    }

    /**
     * Procura no banco o usuário do id passado como parâmetro.
     * @param id do usuário a procurar.
     * @return dados do usuário procurado, se exixtir.
     */
    public Cursor carregaRefeicaoById(int id){
        Cursor cursor;
        String[] campos = {BancoDados.ID_USUARIO, BancoDados.NOME_USUARIO, BancoDados.EMAIL, BancoDados.SENHA, BancoDados.PESO_INICIAL,
                BancoDados.PESO_FINAL, BancoDados.ALTURA, BancoDados.OBJETIVO, BancoDados.DATA_NASCIMENTO, BancoDados.DATA_CADASTRO};
        String where = BancoDados.ID_USUARIO + "=" + id;
        db = banco.getReadableDatabase();
        cursor = db.query(BancoDados.TABELA_USUARIO, campos, where, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }
}
