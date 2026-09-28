package com.bitrath.loyaltycards;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitrath.loyaltycards.model.LoyaltyCardsDAO;
import com.bitrath.loyaltycards.model.LoyaltyCardsDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FavoriteCardsFragment extends Fragment {
    // RecyclerView is like a fragment, but it needs a LAYOUT MANAGER and an ADAPTER
    private RecyclerView recyclerViewLC;
    private CardsRecyclerAdapter adapter;

    // Database Methods
    private LoyaltyCardsDAO lcDAO;

    // Floating Button
    // private FloatingActionButton addLoyaltyCard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceBundle){
        View view = inflater.inflate(R.layout.fragment_all_cards, container, false);

        // Load DAO
        lcDAO = LoyaltyCardsDB.getLoyaltyCardDB(getActivity().getApplicationContext()).loyaltyCardsDAO();

        // Load Recycler View
        recyclerViewLC = (RecyclerView)view.findViewById(R.id.loyaltyCardsRecycler);

        // Load Floating Button
        /*
        addLoyaltyCard = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        addLoyaltyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), InsertCardActivity.class);
                view.getContext().startActivity(intent);
            }
        });
         */

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // ... Set Layout and Adapter ...
        recyclerViewLC.setLayoutManager(new GridLayoutManager(requireContext(), 1));
        adapter = new CardsRecyclerAdapter(lcDAO.getAllFavoritesCards());
        adapter.notifyDataSetChanged();
        recyclerViewLC.setAdapter(adapter);
    }
}
