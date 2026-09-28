package com.bitrath.loyaltycards;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitrath.loyaltycards.model.LoyaltyCard;
import com.bitrath.loyaltycards.model.LoyaltyCardsDAO;
import com.bitrath.loyaltycards.model.LoyaltyCardsDB;

public class ShowLoyaltyCodeActivity extends AppCompatActivity {
    // Card
    LoyaltyCardsDAO lcDAO;
    LoyaltyCard card;
    int card_id;
    int whatCode;

    // View
    ImageView codeIV;
    TextView codeTV, companyTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_loyalty_code);

        // Retrieve Bundle
        Bundle getCodeBundle = this.getIntent().getExtras();
        card_id = getCodeBundle.getInt("card_code_id");
        whatCode = getCodeBundle.getInt("card_code_type");

        // Retrieve Card form DB
        lcDAO = LoyaltyCardsDB.getLoyaltyCardDB(this).loyaltyCardsDAO();
        card = lcDAO.getLC(card_id);

        // Views
        codeIV = (ImageView) findViewById(R.id.codeImageView);
        if(whatCode == 0){
            // BarCode
            codeIV.setImageBitmap(Utils.byteArrayToImage(card.getBarCode()));
        } else {
            // QR Code
            codeIV.setImageBitmap(Utils.byteArrayToImage(card.getQRCode()));
        }
        codeTV = (TextView) findViewById(R.id.codeCardTextView);
        codeTV.setText(card.getLoyaltyCode());
        companyTV = (TextView) findViewById(R.id.companyTextView);
        companyTV.setText(card.getCompany());
    }
}