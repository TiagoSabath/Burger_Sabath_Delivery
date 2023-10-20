package com.example.burgersabathdelivery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.burgersabathdelivery.PaymentIntentResultCallback;
import com.example.burgersabathdelivery.PaymentResultCallback;
import com.example.burgersabathdelivery.R;
import com.example.burgersabathdelivery.adapter.CartListAdapter;
import com.example.burgersabathdelivery.model.ChangerNumberItensListener;
import com.example.burgersabathdelivery.model.ManagementCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.payments.paymentlauncher.PaymentLauncher;
import com.stripe.android.view.CardInputWidget;

import org.checkerframework.checker.nullness.qual.NonNull;


public class CartActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewlist;
    private ManagementCart managementcart;
    private TextView totalFreeTxt, taxTxt, deliverytxt, totalTxt, emptyTxt, txtLocal;
    private double tax;
    private ScrollView scrollView;
    private ImageView backBtn, imgCartEmpty;
    private Button btn_finish;
    Spinner spinner;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String UsuarioID;
    private Stripe stripe;
    private CardInputWidget cardInputWidget;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        PaymentConfiguration.init(getApplicationContext(), "pk_test_51O2v0zFQ3nvTKzgMBHbdK4ugXslaO2jGsOUMiUBEoXiQJRURkOP53eQmUWsqbE6LMcQM9oDwstQgbp8U61Ey3srC00zKaeMm5i");;
        stripe = new Stripe(this, PaymentConfiguration.getInstance(this).getPublishableKey());

        managementcart = new ManagementCart(this);

        initView();
        initList();
        calcCart();
        setVariable();

        btn_finish.setOnClickListener(v -> {
            managementcart.clearCart();
            startActivity(new Intent(CartActivity.this, TelaAvaliacao.class));


        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selecaoPagamento = spinner.getSelectedItem().toString();

                if ("Cartão de Crédito".equals(selecaoPagamento)){
                    iniciarTelaPagamentoPayPal();
                    cardInputWidget.setVisibility(View.VISIBLE);
                } else {
                    cardInputWidget.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void iniciarTelaPagamentoPayPal() {
        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();

        if (params != null) {
            stripe = new Stripe(this, "sk_test_51O2v0zFQ3nvTKzgM4WrMHY186jU2jTyGSTU9sbvy5PqY0k6nBD5T91mYIdvg5Og6PdCLgfNNNcQUD4Cnuht84TTF00fpiFF2II");

            ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(params, "sk_test_51O2v0zFQ3nvTKzgM4WrMHY186jU2jTyGSTU9sbvy5PqY0k6nBD5T91mYIdvg5Og6PdCLgfNNNcQUD4Cnuht84TTF00fpiFF2II");

            stripe.confirmPayment(this, confirmParams);
        }
    }

    private void confirmPayment(ConfirmPaymentIntentParams confirmParams) {
        try {
            stripe.confirmPayment(this, confirmParams);
        } catch (Exception e) {

            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        PaymentIntentResultCallback callback = new PaymentResultCallback(this,stripe);
        stripe.onPaymentResult(requestCode, data, new ApiResultCallback<PaymentIntentResult>() {
            @Override
            public void onSuccess(@NonNull PaymentIntentResult result) {
                callback.onPaymentResult(result);
            }

            @Override
            public void onError(@NonNull Exception e) {

            }
        });
    }


    private void setVariable() {
        backBtn.setOnClickListener(v -> finish());
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewlist.setLayoutManager(linearLayoutManager);
        adapter = new CartListAdapter(managementcart.getListCart(), this, new ChangerNumberItensListener() {
            @Override
            public void changed() {
                calcCart();
            }
        });
        recyclerViewlist.setAdapter(adapter);

        if (managementcart.getListCart().isEmpty()){
            emptyTxt.setVisibility(View.VISIBLE);
            imgCartEmpty.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else {
            emptyTxt.setVisibility(View.GONE);
            imgCartEmpty.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void calcCart(){
        double parcentTax = 0.0;
        double delivery = 0.0;
        tax = Math.round((managementcart.getTotalFee() * parcentTax * 100.0)) / 100.0;

        double total = Math.round((managementcart.getTotalFee() + tax + delivery) * 100.0) / 100;
        double itemTotal = Math.round(managementcart.getTotalFee() * 100.0) / 100.0;

        totalFreeTxt.setText("R$" + itemTotal);
        taxTxt.setText("R$" + tax);
        deliverytxt.setText("R$" + delivery);
        totalTxt.setText("R$" + total);
    }

    @Override
    protected void onStart() {
        super.onStart();
        UsuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference= db.collection("Usuario").document(UsuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null){
                    txtLocal.setText(documentSnapshot.getString("endereco"));
                }
            }
        });
    }

    private void initView() {
    totalFreeTxt = findViewById(R.id.totalFeeTxt);
    taxTxt = findViewById(R.id.taxTxt);
    deliverytxt = findViewById(R.id.deliveryTxt);
    totalTxt = findViewById(R.id.totalTxt);
    recyclerViewlist = findViewById(R.id.recyclerViewlist);
    scrollView = findViewById(R.id.scrollView3);
    backBtn=findViewById(R.id.backBtn);
    emptyTxt=findViewById(R.id.txtEmpty);
    btn_finish = findViewById(R.id.btn_finish);
    spinner = findViewById(R.id.spinner);
    txtLocal = findViewById(R.id.txtLocal);
    imgCartEmpty = findViewById(R.id.imgCartEmpty);
    cardInputWidget = findViewById(R.id.cardInputWidget);

    }
}