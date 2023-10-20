package com.example.burgersabathdelivery.model;

import android.content.Context;
import android.widget.Toast;

import com.example.burgersabathdelivery.Domain.FoodDomain;

import java.util.ArrayList;

public class ManagementCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagementCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertFood(FoodDomain item) {
        ArrayList<FoodDomain> listfood = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int y = 0; y < listfood.size(); y++) {
            if (listfood.get(y).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = y;
                break;
            }
        }
        if (existAlready) {
            listfood.get(n).setNumCart(item.getNumCart());
        } else {
            listfood.add(item);
        }
        tinyDB.putListObject("CartList", listfood);
        Toast.makeText(context, "Adicionado ao carrinho", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<FoodDomain> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    public void minusNumberFood(ArrayList<FoodDomain> listfood, int position, ChangerNumberItensListener changerNumberItensListener) {
        if (listfood.get(position).getNumCart() == 1) {
            listfood.remove(position);
        } else {
            listfood.get(position).setNumCart(listfood.get(position).getNumCart() - 1);
        }
        tinyDB.putListObject("CartList", listfood);
        changerNumberItensListener.changed();
    }

    public void plusNumberFood(ArrayList<FoodDomain> listfood, int position, ChangerNumberItensListener changerNumberItensListener) {
        listfood.get(position).setNumCart(listfood.get(position).getNumCart() + 1);
        tinyDB.putListObject("CartList", listfood);
        changerNumberItensListener.changed();
    }

    public Double getTotalFee() {
        ArrayList<FoodDomain> listFood2 = getListCart();
        double fee = 0;
        for (int i = 0; i < listFood2.size(); i++) {
            fee = fee + (listFood2.get(i).getPrice() * listFood2.get(i).getNumCart());
        }
        return fee;
    }

    public void clearCart() {
        ArrayList<FoodDomain> listFood2 = getListCart();
        listFood2.clear();
        tinyDB.putListObject("CartList", listFood2);
    }
}