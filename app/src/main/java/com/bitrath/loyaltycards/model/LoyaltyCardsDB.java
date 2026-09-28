package com.bitrath.loyaltycards.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {LoyaltyCard.class}, version = 1, exportSchema = false)
public abstract class LoyaltyCardsDB extends RoomDatabase {
    private static LoyaltyCardsDB lcDB = null;
    public abstract LoyaltyCardsDAO loyaltyCardsDAO();
    public static synchronized LoyaltyCardsDB getLoyaltyCardDB(Context context) {
        if(lcDB == null)
        {
            lcDB = Room.databaseBuilder(context.getApplicationContext(),
                    LoyaltyCardsDB.class, "LCDatabase")
                    .allowMainThreadQueries().build();
        }
        return lcDB;
    }
}
