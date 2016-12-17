package com.example.luizeduardo.controledieta.controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luizeduardo.controledieta.R;
import com.example.luizeduardo.controledieta.models.BancoDados;

public class Login extends FragmentActivity {

    private EditText logEmail, logSenha;
    private Button novoUsuario, logar;
    private BancoDadosController db;
    private Cursor cursor;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("CODIGO", MODE_PRIVATE);

        logEmail = (EditText)findViewById(R.id.logEmail);
        logSenha = (EditText)findViewById(R.id.logSenha);

        novoUsuario = (Button) findViewById(R.id.logNovoUsuario);
        logar = (Button) findViewById(R.id.logBtnLogar);

        novoUsuario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), NovoUsuario.class);
                startActivity(intent);
            }
        });

        logar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String email, senha;
                email = logEmail.getText().toString().toLowerCase();
                senha = logSenha.getText().toString();

                // Validando campos em branco.
                if(checarCamposLogin(email, senha)) {
                    Toast.makeText(getApplicationContext(), "Os campos de EMAIL e SENHA não podem estar em branco!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                db = new BancoDadosController(getBaseContext());
                cursor = db.carregaUsuarioByEmail(email);

                // Validando se o usuário existe no banco.
                if(cursor.getCount() > 0) {

                    String idUsuario, emailBanco, senhaBanco;
                    idUsuario = cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.ID_USUARIO));
                    emailBanco = cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.EMAIL));
                    senhaBanco = cursor.getString(cursor.getColumnIndexOrThrow(BancoDados.SENHA));

                    // Veirificando se o E-Mail e a SENHA são as mesmas cadastradas no banco
                    if(email.equals(emailBanco) && senha.equals(senhaBanco)){

                        Toast.makeText(getApplicationContext(), "Login efetuado com sucesso!!", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("codigo", idUsuario);
                        editor.apply();

                        Intent intent = new Intent(getBaseContext(), ControleDieta.class);
                        intent.putExtra("codigo", idUsuario);
                        startActivity(intent);
                    }else{

                        Toast.makeText(getApplicationContext(), "E-MAIL ou SENHA incorretos!!", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(getApplicationContext(), "E-MAIL não cadastrado!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Checa se os campos de EMAIL e telefone estão em branco.
     * @return true se estão em branco, false se não estão
     */
    public boolean checarCamposLogin(String email, String senha){

        if(email.equals("") || senha.equals("")){
            return true;
        }else{
            return false;
        }
    }
}