package com.example.burgersabathdelivery.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.burgersabathdelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Cadastro extends AppCompatActivity {
    private EditText edit_name;
    private EditText edit_email_cadastrar;
    private EditText edit_senha_cadastrar,edit_telefone,edit_endereco;
    private Button btnCadastrar;
    String[] mensagens = {"Preencha todos os campos", "Cadasstro realizado com sucesso"};
    String usuarioID;
    ImageView eyeIcon;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        IniciarComponentes();

        btnCadastrar.setOnClickListener(view -> {
            String nome = edit_name.getText().toString();
            String email = edit_email_cadastrar.getText().toString();
            String senha = edit_senha_cadastrar.getText().toString();
            String tele = edit_telefone.getText().toString();
            String ende = edit_endereco.getText().toString();
            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() ||tele.isEmpty() || ende.isEmpty()){
                Snackbar snackbar = Snackbar.make(view,mensagens[0],Snackbar.LENGTH_LONG );
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }else {
                CadastrarUsuario(view);
            }
        });

        eyeIcon.setOnClickListener(view -> {
            EditText editSenha = findViewById(R.id.edit_senha_cadastrar);
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

        edit_telefone.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;
            private boolean deletingHyphen;
            private int hyphenStart;
            private String lastFormatted = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                deletingHyphen = count > 0 && s.charAt(start) == '-';
                hyphenStart = -1;
                lastFormatted = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isFormatting) {
                    return;
                }

                int digitCount = 0;
                StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    if (Character.isDigit(c)) {
                        digitCount++;
                        if (digitCount <= 11) {
                            formatted.append(c);
                            if (digitCount == 2 || digitCount == 7) {
                                formatted.append('-');
                            }
                        }
                    }
                }

                    isFormatting = true;
                    edit_telefone.setText(formatted);
                    edit_telefone.setSelection(formatted.length());
                    isFormatting = false;

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void CadastrarUsuario(View view){
        String email = edit_email_cadastrar.getText().toString();
        String senha = edit_senha_cadastrar.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    SalvarDadosUsuario();

                    Snackbar snackbar = Snackbar.make(view,mensagens[1],Snackbar.LENGTH_LONG );
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();


                    Intent intent = new Intent(Cadastro.this, Login.class);
                    startActivity(intent);

                }else {
                    String erro;
                    try {
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e){
                        erro = "Digite uma senha no mínimo 6 numeros.";

                    }catch (FirebaseAuthUserCollisionException e){
                        erro = "Essa conta já foi cadastrada";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "E-mail inválido";
                    }catch (Exception e){
                        erro = "Erro ao cadastrar o usuário";
                    }
                    Snackbar snackbar = Snackbar.make(view,erro,Snackbar.LENGTH_LONG );
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    private void SalvarDadosUsuario(){
        String nome = edit_name.getText().toString();
        String email = edit_email_cadastrar.getText().toString();
        String tele = edit_telefone.getText().toString();
        String ende = edit_endereco.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("nome",nome);
        usuarios.put("email",email);
        usuarios.put("telefone", tele);
        usuarios.put("endereco",ende);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("db", "Sucesso ao salva os dados");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db","Erro ao salvar os dados" + e.toString());
                    }
                });
    }


    private void IniciarComponentes(){
        edit_name = findViewById(R.id.edit_name);
        edit_email_cadastrar = findViewById(R.id.edit_email_cadastrar);
        edit_senha_cadastrar = findViewById(R.id.edit_senha_cadastrar);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        edit_endereco = findViewById(R.id.edit_endereco);
        edit_telefone = findViewById(R.id.edit_telefone);
        eyeIcon = findViewById(R.id.eye_icon);
    }
}