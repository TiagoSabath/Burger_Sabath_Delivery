package com.example.burgersabathdelivery.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.burgersabathdelivery.R;

public class TelaAvaliacao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_avaliacao);

        final TextView textPedidoPreparado = findViewById(R.id.textPedidoPreparado);
        final ProgressBar progressPedidoPreparado = findViewById(R.id.progressPedidoPreparado);
        final TextView textSaindoParaEntregar = findViewById(R.id.textSaindoParaEntregar);
        final ProgressBar progressSaindoParaEntregar = findViewById(R.id.progressSaindoParaEntregar);
        final TextView textPedidoEntregue = findViewById(R.id.textPedidoEntregue);
        final TextView textAvaliePedido = findViewById(R.id.textAvaliePedido);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        final Button btnConcluir = findViewById(R.id.btnConcluir);
        final ImageView imgEntrega = findViewById(R.id.imgEntrega);
        final ImageView imgPreparando = findViewById(R.id.imgPreparando);
        final TextView textAprovado = findViewById(R.id.textAprovado);
        final ImageView imgPayArpovado = findViewById(R.id.imgPayArpovado);
        final ProgressBar progressPayAprovado = findViewById(R.id.progressPayAprovado);


        final Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textAprovado.setVisibility(View.GONE);
                imgPayArpovado.setVisibility(View.GONE);
                progressPayAprovado.setVisibility(View.GONE);
                textPedidoPreparado.setVisibility(View.VISIBLE);
                progressPedidoPreparado.setVisibility(View.VISIBLE);
                imgPreparando.setVisibility(View.VISIBLE);
            }
        }, 3000); // Mostra "Pedido sendo preparado" após 3 segundos

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textPedidoPreparado.setVisibility(View.GONE);
                progressPedidoPreparado.setVisibility(View.GONE);
                imgPreparando.setVisibility(View.GONE);
                textSaindoParaEntregar.setVisibility(View.VISIBLE);
                progressSaindoParaEntregar.setVisibility(View.VISIBLE);
                imgEntrega.setVisibility(View.VISIBLE);
            }
        }, 6000); // Mostra "Saindo para entregar" após mais 3 segundos (total de 6 segundos)

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textSaindoParaEntregar.setVisibility(View.GONE);
                progressSaindoParaEntregar.setVisibility(View.GONE);
                imgEntrega.setVisibility(View.GONE);
                textPedidoEntregue.setVisibility(View.VISIBLE);
                textAvaliePedido.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
                btnConcluir.setVisibility(View.VISIBLE);
            }
        }, 9000);


        btnConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaAvaliacao.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    Toast.makeText(TelaAvaliacao.this, "Obrigado por avaliar", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}