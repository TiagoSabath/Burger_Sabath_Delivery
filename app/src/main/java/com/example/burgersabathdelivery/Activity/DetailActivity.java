package com.example.burgersabathdelivery.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.burgersabathdelivery.Domain.FoodDomain;
import com.example.burgersabathdelivery.R;
import com.example.burgersabathdelivery.model.ManagementCart;

public class DetailActivity extends AppCompatActivity {

    private Button btnAddtoCart;
    private TextView plusCartBtn, MinusCartBtn, titleTxt, DescriptionTxt, numberOrderTxt, priceTxt;
    private ImageView foodPic, imgBack;
    private FoodDomain objetc;
    private int numberOrder = 1;
    private ManagementCart managementcart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        managementcart= new ManagementCart(DetailActivity.this);

        initView();
        getBundle();
    }

    private void getBundle() {
        objetc = (FoodDomain) getIntent().getSerializableExtra("object");

        int drawableResourceID = this.getResources().getIdentifier(objetc.getPicUrl(),"drawable",this.getPackageName());
        Glide.with(this)
                .load(drawableResourceID)
                .into(foodPic);

        titleTxt.setText(objetc.getTitle());
        priceTxt.setText("R$" + objetc.getPrice());
        DescriptionTxt.setText(objetc.getDescription());
        numberOrderTxt.setText("" + numberOrder);
        btnAddtoCart.setText("Adicionar ao Carrinho - R$" + Math.round(numberOrder * objetc.getPrice()));

        plusCartBtn.setOnClickListener(v -> {
            numberOrder = numberOrder + 1;
            numberOrderTxt.setText("" + numberOrder);
            btnAddtoCart.setText("Adicionar ao Carrinho - R$" + Math.round(numberOrder * objetc.getPrice()));
        });

        MinusCartBtn.setOnClickListener(v -> {
            numberOrder = numberOrder - 1  ;
            numberOrderTxt.setText("" + numberOrder);
            btnAddtoCart.setText("Adicionar ao Carrinho - R$" + Math.round(numberOrder * objetc.getPrice()));
        });

        btnAddtoCart.setOnClickListener(v -> {
            objetc.setNumCart(numberOrder);
            managementcart.insertFood(objetc);
        });

        imgBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        btnAddtoCart= findViewById(R.id.btnAddtoCart);
        plusCartBtn = findViewById(R.id.plusCartBtn);
        MinusCartBtn = findViewById(R.id.MinusCartBtn);
        titleTxt = findViewById(R.id.titleTxt);
        DescriptionTxt = findViewById(R.id.DescriptionTxt);
        numberOrderTxt = findViewById(R.id.nunberItemTxt);
        foodPic = findViewById(R.id.foodPic);
        priceTxt = findViewById(R.id.priceTxt);
        imgBack = findViewById(R.id.imgBack);
    }
}