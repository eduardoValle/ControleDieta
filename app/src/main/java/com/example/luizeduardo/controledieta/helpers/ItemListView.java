package com.example.luizeduardo.controledieta.helpers;

/**
 * Created by Luiz Eduardo on 14/10/2016.
 */

public class ItemListView {
    private String nome, descricao, dado;
    private int iconeRid;

    public ItemListView() {}

    public ItemListView(String nome, String descricao, String dado, int iconeRid) {
        this.nome = nome;
        this.descricao = descricao;
        this.dado = dado;
        this.iconeRid = iconeRid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDado() {
        return dado;
    }

    public void setDado(String dado) {
        this.dado = dado;
    }

    public int getIconeRid() {
        return iconeRid;
    }

    public void setIconeRid(int iconeRid) {
        this.iconeRid = iconeRid;
    }
}