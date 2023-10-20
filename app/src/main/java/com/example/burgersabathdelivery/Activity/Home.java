package com.example.burgersabathdelivery.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.burgersabathdelivery.Domain.FoodDomain;
import com.example.burgersabathdelivery.R;
import com.example.burgersabathdelivery.adapter.FoodListAdapter;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private RecyclerView.Adapter adapterFoodList;
    private RecyclerView recyclerViewFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initRecyclerview();
        buttonNavigation();
    }

    private void buttonNavigation() {
        LinearLayout homebtn= findViewById(R.id.homeBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        LinearLayout perfilBtn = findViewById(R.id.perfilBtn);


        homebtn.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, Home.class));
        });

        cartBtn.setOnClickListener(v -> startActivity(new Intent(Home.this, CartActivity.class)));

        perfilBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Home.this, PerfilActivity.class);
            startActivity(intent);
        });
    }

    private void initRecyclerview(){
        ArrayList<FoodDomain> itens = new ArrayList<>();
        itens.add(new FoodDomain("X-Hamburger","Pão com gergelim, alface, tomate, queijo \n" +
                "e 100 g de hambúrger.","food1", (double) 15));
        itens.add(new FoodDomain("X-Picanha","Pão com gergelim, alface, tomate, queijo, \n" +
                "picles e 150 g de hambúrger.","food2", (double) 17));
        itens.add(new FoodDomain("X-Duplo","Pão, alface, bacon, queijo \n" +
                "e duas carnes de humbúger de 80 g.","food3", (double) 20));
        itens.add(new FoodDomain("X-Frango","Pão com gergelim, alface, tomate, queijo \n" +
                "e um file de grango.","food4", (double) 22));
        itens.add(new FoodDomain("X-Bacon","Pão com gergelim, alface, tomate, queijo, \n" +
                "bacon, e 150 g de hambúrger.","food5", (double) 27));

        recyclerViewFood = findViewById(R.id.view1);
        recyclerViewFood.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        adapterFoodList = new FoodListAdapter(itens);
        recyclerViewFood.setAdapter(adapterFoodList);
    }
}