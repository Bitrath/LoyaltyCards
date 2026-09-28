package com.bitrath.loyaltycards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bitrath.loyaltycards.model.LoyaltyCardsDAO;
import com.bitrath.loyaltycards.model.LoyaltyCardsDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListLoyaltyCardsActivity extends AppCompatActivity {
    // RecyclerView is like a fragment, but it needs a LAYOUT MANAGER and an ADAPTER
    RecyclerView recyclerViewLC;
    CardsRecyclerAdapter adapter;

    // Database Methods
    LoyaltyCardsDAO lcDAO;

    // Floating Button
    FloatingActionButton addLoyaltyCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_loyalty_cards);

        // Load DAO
        lcDAO = LoyaltyCardsDB.getLoyaltyCardDB(this).loyaltyCardsDAO();

        // Load Recycler View
        recyclerViewLC = (RecyclerView)findViewById(R.id.loyaltyCardsRecycler);

        // Load Floating Button
        addLoyaltyCard = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        addLoyaltyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), InsertCardActivity.class);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // ... Set Layout and Adapter ...
        recyclerViewLC.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new CardsRecyclerAdapter(lcDAO.getAllLoyaltyCards());
        adapter.notifyDataSetChanged();
        recyclerViewLC.setAdapter(adapter);
    }
}