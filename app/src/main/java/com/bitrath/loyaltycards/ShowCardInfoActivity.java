package com.bitrath.loyaltycards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitrath.loyaltycards.model.LoyaltyCard;
import com.bitrath.loyaltycards.model.LoyaltyCardsDAO;
import com.bitrath.loyaltycards.model.LoyaltyCardsDB;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ShowCardInfoActivity extends AppCompatActivity {
    // DB Controller
    private LoyaltyCardsDAO lcDAO;
    private LoyaltyCard card;

    // Utils
    int card_id;
    private boolean starChoice;
    private int codeType;

    // View Elements
    CardView card_view;
    ImageView logoCard, codeCard;
    private ImageButton favoriteStarButton;
    Button qrChosenButton, barcodeChosenButton, deleteLCButton;
    TextView companyCard, addressCard, loyaltyCodeCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_card_info);

        // Retrieve Bundle
        Bundle getIDBundle = this.getIntent().getExtras();
        card_id = getIDBundle.getInt("card_id");
        Log.v("Card Intent ID: ", Integer.toString(card_id));

        // Retrieve Card form DB
        lcDAO = LoyaltyCardsDB.getLoyaltyCardDB(this).loyaltyCardsDAO();
        card = lcDAO.getLC(card_id);
        // favorite onCreate()
        starChoice = card.isFavorite();

        // View Links
        // CARD
        card_view = (CardView)findViewById(R.id.cardInfoView);
        card_view.setCardBackgroundColor(card.getThemeColor());
        logoCard = (ImageView) findViewById(R.id.logoImageView);
            logoCard.setImageBitmap(Utils.byteArrayToImage(card.getLogo()));
        codeCard = (ImageView) findViewById(R.id.codeImageView);
            codeCard.setImageBitmap(Utils.byteArrayToImage(card.getBarCode()));
            codeType = 0; // Barcode = 0, QR = 1
            codeCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Intent to activity that only shows CODE
                    Bundle codeBundle = new Bundle();
                    codeBundle.putInt("card_code_id", card.getCardID());
                    codeBundle.putInt("card_code_type", codeType);
                    Intent intentCode = new Intent(view.getContext(), ShowLoyaltyCodeActivity.class);
                    intentCode.putExtras(codeBundle);
                    view.getContext().startActivity(intentCode);
                }
            });
        favoriteStarButton = (ImageButton) findViewById(R.id.favoriteButton);
            setStarFromCard();
            favoriteStarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    starChoice = !starChoice;
                    lcDAO.updateFavorite(card.getCardID(), starChoice);
                    Log.v("Card Favorite Click: ", Boolean.toString(starChoice));
                    if(starChoice){
                        favoriteStarButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                    } else {
                    favoriteStarButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
                    }
                }
            });
        // CODES BUTTONS
        qrChosenButton = (Button) findViewById(R.id.viewQRButton);
        qrChosenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeType = 1;
                codeCard.setImageBitmap(Utils.byteArrayToImage(card.getQRCode()));
            }
        });
        barcodeChosenButton = (Button) findViewById(R.id.viewBARCODEButton);
        barcodeChosenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeType = 0;
                codeCard.setImageBitmap(Utils.byteArrayToImage(card.getBarCode()));
            }
        });
        deleteLCButton = (Button) findViewById(R.id.deleteCardButton);
        deleteLCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAction(view);
            }
        });
        // INFO
        companyCard = (TextView) findViewById(R.id.companyCardLabelTextView);
            companyCard.setText(card.getCompany());
        addressCard = (TextView) findViewById(R.id.addressCardTextView);
            addressCard.setText(card.getAddress());
        loyaltyCodeCard = (TextView) findViewById(R.id.clientCodeCardTextView);
            loyaltyCodeCard.setText(card.getLoyaltyCode());
    }

    @Override
    public void onStop() {
        super.onStop();
        // Set new favorite value
        lcDAO.updateFavorite(card.getCardID(), starChoice);
        Log.v("Card Favorite on Exit: ", Boolean.toString(card.isFavorite()));
    }

    private void setStarFromCard(){
        if(starChoice){
            favoriteStarButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
        } else {
            favoriteStarButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star_big_off));
        }
    }

    private void deleteAction(View view){
        try {
            lcDAO.deleteLoyaltyCard(card);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Card Deleted", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}