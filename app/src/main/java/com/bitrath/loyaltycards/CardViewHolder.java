package com.bitrath.loyaltycards;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CardViewHolder extends RecyclerView.ViewHolder {
    // Card
    protected CardView cardView;
    // Image Views
    protected ImageView logoIV;

    public CardViewHolder(@NonNull View itemView) {
        super(itemView);

        // Link Card
        cardView = (CardView) itemView.findViewById(R.id.cardHolder);

        // Link Images
        logoIV = (ImageView) itemView.findViewById(R.id.logoImageView);
    }
}
