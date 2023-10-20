package com.example.burgersabathdelivery;

import android.widget.Toast;

import com.example.burgersabathdelivery.Activity.CartActivity;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentIntent;


public class PaymentResultCallback implements PaymentIntentResultCallback {
    private final CartActivity Activity;
    private final Stripe Stripe;

    public PaymentResultCallback(CartActivity activity, Stripe stripe) {
        Activity = activity;
        Stripe = stripe;
    }

    @Override
    public void onPaymentResult(PaymentIntentResult paymentIntentResult) {
        PaymentIntent paymentIntent = paymentIntentResult.getIntent();

        PaymentIntent.Status status = paymentIntent.getStatus();

        if (status.equals(PaymentIntent.Status.Succeeded)) {

            Toast.makeText(Activity, "Pagamento Aprovado", Toast.LENGTH_SHORT).show();
        } else if (status.equals(PaymentIntent.Status.RequiresPaymentMethod)) {

            Toast.makeText(Activity, "Pagamento inválido", Toast.LENGTH_SHORT).show();
        } else if (status.equals(PaymentIntent.Status.Canceled)) {

            Toast.makeText(Activity, "Pagamento cancelado ", Toast.LENGTH_SHORT).show();
        }


    }

}
