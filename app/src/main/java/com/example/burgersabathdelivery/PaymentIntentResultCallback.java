package com.example.burgersabathdelivery;

import androidx.annotation.NonNull;

import com.example.burgersabathdelivery.Activity.CartActivity;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.model.StripeIntent;

public interface PaymentIntentResultCallback {
    void onPaymentResult(PaymentIntentResult paymentIntentResult);
}

