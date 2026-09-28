package com.bitrath.loyaltycards;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitrath.loyaltycards.model.LoyaltyCard;
import com.bitrath.loyaltycards.model.LoyaltyCardsDAO;
import com.bitrath.loyaltycards.model.LoyaltyCardsDB;

import java.util.List;

public class CardsRecyclerAdapter extends RecyclerView.Adapter<CardViewHolder> {

    // List of Loyalty Cards
    private List<LoyaltyCard> cards;
    // DAO Object to perform action onto context
    // LoyaltyCardsDAO lcDAO;

    // Constructor (depends on dataset)
    public CardsRecyclerAdapter(List<LoyaltyCard> data){
        cards = data;
    }

    //  Create new views (invoked by layout manager)
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder, parent, false);
        //lcDAO = LoyaltyCardsDB.getLoyaltyCardDB(parent.getContext()).loyaltyCardsDAO();
        return new CardViewHolder(view);
    }

    // Replace the contents of a view (invoked by layout manager)
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position){
        LoyaltyCard lc = cards.get(position);

        holder.cardView.setCardBackgroundColor(lc.getThemeColor());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle cardBundle = new Bundle();
                cardBundle.putInt("card_id", lc.getCardID());
                Intent intentCardInfo = new Intent(view.getContext(), ShowCardInfoActivity.class);
                intentCardInfo.putExtras(cardBundle);
                view.getContext().startActivity(intentCardInfo);
            }
        });
        holder.logoIV.setImageBitmap(Utils.byteArrayToImage(lc.getLogo()));
    }

    // Return size of dataset used (invoked by layout manager)
    @Override
    public int getItemCount(){
        return cards.size();
    }
}
