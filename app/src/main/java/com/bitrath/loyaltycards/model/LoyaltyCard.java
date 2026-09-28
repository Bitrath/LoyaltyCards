package com.bitrath.loyaltycards.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "LoyaltyCard")
public class LoyaltyCard {
    @PrimaryKey(autoGenerate = true)
    int cardID;

    @ColumnInfo(name = "Company")
    public String company;

    @ColumnInfo(name = "LoyaltyCode")
    public String loyaltyCode;

    @ColumnInfo(name = "QRCode")
    byte [] QRCode;

    @ColumnInfo(name = "Barcode")
    byte [] barCode;

    @ColumnInfo(name = "ThemeColor")
    public int themeColor;

    @ColumnInfo(name = "Logo")
    byte [] logo;

    @ColumnInfo(name = "Address")
    public String address;

    @ColumnInfo(name = "Favorite")
    public boolean favorite;

    // Getters
    public int getCardID() {
        return cardID;
    }
    public String getCompany() {
        return company;
    }
    public String getLoyaltyCode() {
        return loyaltyCode;
    }
    public byte[] getQRCode() {
        return QRCode;
    }
    public byte[] getBarCode() {
        return barCode;
    }
    public int getThemeColor() {
        return themeColor;
    }
    public byte[] getLogo() {
        return logo;
    }
    public String getAddress() {
        return address;
    }
    public boolean isFavorite() {
        return favorite;
    }

    // Setters
    public void setCardID(int cardID) {
        this.cardID = cardID;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public void setLoyaltyCode(String loyaltyCode) {
        this.loyaltyCode = loyaltyCode;
    }
    public void setQRCode(byte[] QRCode) {
        this.QRCode = QRCode;
    }
    public void setBarCode(byte[] barCode) {
        this.barCode = barCode;
    }
    public void setThemeColor(int themeColor) {
        this.themeColor = themeColor;
    }
    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
