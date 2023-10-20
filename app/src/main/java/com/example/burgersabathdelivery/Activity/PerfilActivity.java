package com.example.burgersabathdelivery.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.burgersabathdelivery.Controller.Login;
import com.example.burgersabathdelivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class PerfilActivity extends AppCompatActivity {

    private TextView txtNameUser, txt_emailUser, txtLocation, textphone;
    private Button btnSair;
    private ImageView btnVoltar, editNomeBtn, editEnderecoBtn, editTeleBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String UsuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        IniciarComponente();

        btnSair.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(PerfilActivity.this, Login.class);
            startActivity(intent);
            finish();
        });

        btnVoltar.setOnClickListener(view -> {
            Intent intent = new Intent(PerfilActivity.this, Home.class);
            startActivity(intent);
            finish();
        });

        editNomeBtn.setOnClickListener(view -> {
            showEditNome();
        });

        editTeleBtn.setOnClickListener(view -> {
            showEditTele();
        });

        editEnderecoBtn.setOnClickListener(view -> {
            showEditEnderco();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        UsuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference= db.collection("Usuario").document(UsuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null){
                    txtNameUser.setText(documentSnapshot.getString("nome"));
                    txt_emailUser.setText(email);
                    txtLocation.setText(documentSnapshot.getString("endereco"));
                    textphone.setText(documentSnapshot.getString("telefone"));
                }
            }
        });
    }

    private void showEditNome() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Nome");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            String novoNome = input.getText().toString();
            EditNomeFire(novoNome);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void EditNomeFire(String novoNome){
        UsuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuario").document(UsuarioID);
        documentReference.update("nome", novoNome).addOnSuccessListener(aVoid -> {
            txtNameUser.setText(novoNome);
            Toast.makeText(PerfilActivity.this, "Nome editado com Sucesso", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(PerfilActivity.this, "Erro ao atualizar o nome: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void showEditTele() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Telefone");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        input.setHint("Digite o novo telefone");
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(11); // Limita o número de caracteres a 11
        input.setFilters(filters);
        builder.setView(input);

        builder.setPositiveButton("Salvar", (dialogInterface, i) -> {
            String novoTelefone = input.getText().toString();
            if (novoTelefone.length() == 11) {
                String numeroFormatado = String.format("%s-%s%s%s%s%s-%s%s%s%s",
                        novoTelefone.substring(0, 2),
                        novoTelefone.substring(2, 3),
                        novoTelefone.substring(3, 4),
                        novoTelefone.substring(4, 5),
                        novoTelefone.substring(5, 6),
                        novoTelefone.substring(6, 7),
                        novoTelefone.substring(7, 8),
                        novoTelefone.substring(8, 9),
                        novoTelefone.substring(9, 10),
                        novoTelefone.substring(10, 11));


               EditTeleFire(numeroFormatado);
            } else {
                // Lida com o caso de entrada inválida (não tem 11 dígitos)
                Toast.makeText(getApplicationContext(), "Número de telefone inválido", Toast.LENGTH_SHORT).show();
            }
    });

        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.cancel());
        builder.show();

    }



    private void EditTeleFire(String novotelefone){
        UsuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuario").document(UsuarioID);
        documentReference.update("telefone", novotelefone).addOnSuccessListener(aVoid -> {
            textphone.setText(novotelefone);
            Toast.makeText(PerfilActivity.this, "Telefone editado com Sucesso", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(PerfilActivity.this, "Erro ao atualizar o Telefone: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void showEditEnderco() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Endereço");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String novoEndereco = input.getText().toString();
                EditEnderecoFire(novoEndereco);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void EditEnderecoFire(String novoEndereco){
        UsuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuario").document(UsuarioID);
        documentReference.update("endereco", novoEndereco).addOnSuccessListener(aVoid -> {
            txtLocation.setText(novoEndereco);
            Toast.makeText(PerfilActivity.this, "Endereço editado com Sucesso", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(PerfilActivity.this, "Erro ao atualizar o Endereço: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void IniciarComponente(){
        txtNameUser = findViewById(R.id.txtNameUser);
        txt_emailUser = findViewById(R.id.txt_emailUser);
        txtLocation = findViewById(R.id.txtLocation);
        textphone = findViewById(R.id.textphone);
        btnSair = findViewById(R.id.btnSair);
        btnVoltar = findViewById(R.id.btnVoltar);
        editNomeBtn = findViewById(R.id.editNomeBtn);
        editTeleBtn = findViewById(R.id.editTeleBtn);
        editEnderecoBtn = findViewById(R.id.editEnderecoBtn);
    }

}