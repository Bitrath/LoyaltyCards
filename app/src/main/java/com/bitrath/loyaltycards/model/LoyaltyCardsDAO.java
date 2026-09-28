package com.bitrath.loyaltycards.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// It's a Database Access Object
// IT contains all the methods used to fro accessing the DB

@Dao
public interface LoyaltyCardsDAO {
    @Insert
    void insertNewLoyaltyCard(LoyaltyCard lc);

    @Delete
    void deleteLoyaltyCard(LoyaltyCard lc);

    @Query("SELECT * FROM LoyaltyCard")
    List<LoyaltyCard> getAllLoyaltyCards();

    @Query("SELECT * FROM LoyaltyCard WHERE favorite = 1")
    List<LoyaltyCard> getAllFavoritesCards();

    @Query("SELECT * FROM LoyaltyCard WHERE cardID = :id")
    LoyaltyCard getLC(int id);

    @Query("UPDATE LoyaltyCard SET favorite = :choice WHERE cardID = :id")
    void updateFavorite(int id, boolean choice);
}
