package com.example.luizeduardo.controledieta.controllers;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luizeduardo.controledieta.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luiz Eduardo on 14/12/2016.
 */

public class NovoUsuario extends FragmentActivity {

    private Map<String,String> dados = new HashMap<String, String>();
    private EditText novoNome, novoEmail, novoSenha;
    private Button novoBtnSalvar;
    private BancoDadosController db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novo_usuario);

        novoNome = (EditText)findViewById(R.id.novoNome);
        novoEmail = (EditText)findViewById(R.id.novoEmail);
        novoSenha = (EditText)findViewById(R.id.novoSenha);

        // Botão de salvar dados.
        novoBtnSalvar = (Button) findViewById(R.id.novoBtnSalvar);
        novoBtnSalvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String nome, email, senha;
                nome = novoNome.getText().toString();
                email = novoEmail.getText().toString().toLowerCase();
                senha = novoSenha.getText().toString();

                // Checando se os campos NOME, E-MAIL e SENHA estão em branco.
                if (checarCampos(nome, email, senha)) {
                    Toast.makeText(getApplicationContext(), "Os campos de NOME, EMAIL e SENHA não podem estar em branco.", Toast.LENGTH_SHORT).show();
                    return;
                }

                db = new BancoDadosController(getBaseContext());
                cursor = db.carregaUsuarioByEmail(email);

                // Checando se o email já existe no banco de dados.
                if(cursor.getCount() > 0){
                    Toast.makeText(getApplicationContext(), "E-Mail já cadastrado no banco!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Checando se o email é válido.
                if(!validarEmail(email)){
                    Toast.makeText(getApplicationContext(), "E-Mail passado não é válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                dados.put("nome", nome);
                dados.put("email", email);
                dados.put("senha", senha);

                String resultado = db.inserirUsuario(dados);
                Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), DadosUsuario.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }

    /**
     * Checa se os campos de EMAIL e telefone estão em branco.
     * @return true se estão em branco, false se não estão
     */
    public boolean checarCampos(String nome, String email, String senha){

        if(nome.equals("") || email.equals("") || senha.equals("")){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Checa se o EMAIL passado é válido.
     * @return true se o EMAIL passado é válido, false se não é.
     */
    public boolean validarEmail(String email){

        if(email.contains("@")){
            return true;
        }else{
            return false;
        }
    }
}