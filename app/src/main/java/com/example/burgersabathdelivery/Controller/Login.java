package com.example.burgersabathdelivery.Controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.burgersabathdelivery.Activity.Home;
import com.example.burgersabathdelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private TextView text_cadastrar;
    private EditText edit_Username;
    private EditText edit_Password;
    private Button btnLogin;
    String[] mensagens = {"Preencha todos os campos", "Login realizado com sucesso", "E-mail ou senha incorreta"};
    ImageView eye_icon2;
    private boolean isPasswordVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        IniciarComponntes();

        text_cadastrar.setOnClickListener(view -> {

            Intent intent = new Intent(Login.this, Cadastro.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edit_Username.getText().toString();
                String senha = edit_Password.getText().toString();

                if(email.isEmpty() || senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(view,mensagens[0],Snackbar.LENGTH_LONG );
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else {
                    AutenticarUsuario(view);
                }
            }
        });

        eye_icon2.setOnClickListener(view -> {
            EditText editSenha = findViewById(R.id.edit_Password);
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                editSenha.setInputType(InputType.TYPE_CLASS_NUMBER);
                editSenha.setTransformationMethod(null);
            } else {
                editSenha.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                editSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }

            editSenha.setSelection(editSenha.length());
        });


    }
    private void AutenticarUsuario(View view){
        String email = edit_Username.getText().toString();
        String senha = edit_Password.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    TelaPrincipal();
                } else {
                    Snackbar snackbar = Snackbar.make(view,mensagens[2],Snackbar.LENGTH_LONG );
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioAtual != null){
            TelaPrincipal();
        }
    }

    private  void TelaPrincipal(){
        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
        finish();

    }

    private void IniciarComponntes(){

        text_cadastrar=findViewById(R.id.text_cadastrar);
        edit_Username=findViewById(R.id.edit_Username);
        edit_Password=findViewById(R.id.edit_Password);
        btnLogin=findViewById(R.id.btnLogin);
        eye_icon2=findViewById(R.id.eye_icon2);
    }
}