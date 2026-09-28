package com.bitrath.loyaltycards;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitrath.loyaltycards.model.LoyaltyCard;
import com.bitrath.loyaltycards.model.LoyaltyCardsDAO;
import com.bitrath.loyaltycards.model.LoyaltyCardsDB;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import yuku.ambilwarna.AmbilWarnaDialog;

import java.io.IOException;

public class InsertCardActivity extends AppCompatActivity {
    // Field obj to access database methods
    private LoyaltyCardsDAO lcDAO;

    // Fields view elements
    ImageView logoImageView, qrCodeImageView, barcodeImageView;
    Button changeLogoButton, generateCodesButton, pickColorButton, saveNewCard;
    EditText companyNameEditText, addressEditText, clientCodeEditText;

    // Fields To Manage BitMap Data
    Bitmap bitmapLogo, bitmapQRCode, bitmapBarcode;

    // Other Fields
    private int colorChosen;
    protected final int SELECT_LOGO_IMAGE = 51;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_card);

        // Load DB at creation
        lcDAO = LoyaltyCardsDB.getLoyaltyCardDB(this).loyaltyCardsDAO();

        // Link Image Views
        logoImageView = (ImageView) findViewById(R.id.logoImageView);
        qrCodeImageView = (ImageView) findViewById(R.id.imageViewQRCode);
        barcodeImageView = (ImageView) findViewById(R.id.imageViewBarcode);

        // Link Buttons
        changeLogoButton = (Button) findViewById(R.id.changeLogoButton);
        generateCodesButton = (Button) findViewById(R.id.buttonCreateCodes);
        pickColorButton = (Button) findViewById(R.id.buttonChangeColorTheme);
        saveNewCard = (Button) findViewById(R.id.buttonSave);

        // Link EditText
        companyNameEditText = (EditText) findViewById(R.id.editTextTextCompanyName);
        addressEditText = (EditText) findViewById(R.id.editTextTextAddress);
        clientCodeEditText = (EditText) findViewById(R.id.editTextTextClientCode);


        // Bitmap Init
        bitmapLogo = null;
        bitmapQRCode = null;
        bitmapBarcode = null;

        // Color: init with Default value
        colorChosen = ContextCompat.getColor(InsertCardActivity.this, com.google.android.material.R.color.design_default_color_primary);

        //ONCLICK Methods
        changeLogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Something
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_LOGO_IMAGE);
            }
        });

        generateCodesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateQRCodeAction(view);
                generateBarCodeAction(view);
            }
        });

        pickColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeColorThemeAction(view);
            }
        });

        saveNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewCardAction(view);
            }
        });
    }

    private void generateQRCodeAction(View view){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        String editTextContent = clientCodeEditText.getText().toString();
        if(!editTextContent.matches("")){
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(clientCodeEditText.getText().toString(), BarcodeFormat.QR_CODE, 150, 150);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                bitmapQRCode = barcodeEncoder.createBitmap(bitMatrix);
                qrCodeImageView.setImageBitmap(bitmapQRCode);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Client Code Empty!", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateBarCodeAction(View view){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        String editTextContent = clientCodeEditText.getText().toString();
        if(!editTextContent.matches("")){
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(clientCodeEditText.getText().toString(), BarcodeFormat.CODE_128, 150, 75);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                bitmapBarcode = barcodeEncoder.createBitmap(bitMatrix);
                barcodeImageView.setImageBitmap(bitmapBarcode);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Client Code Empty!", Toast.LENGTH_SHORT).show();
        }
    }

    private void changeColorThemeAction(View view){
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, colorChosen, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color){
                colorChosen = color;
                pickColorButton.setBackgroundColor(colorChosen);
            }
        });
        colorPicker.show();
    }

    private void saveNewCardAction(View view){
        if(companyNameEditText.getText().toString().isEmpty() ||
        addressEditText.getText().toString().isEmpty() ||
        clientCodeEditText.getText().toString().isEmpty() ||
        bitmapBarcode == null ||
        bitmapQRCode == null ||
        bitmapLogo == null){
            Toast.makeText(this, "Not Enough Data. Try Again.", Toast.LENGTH_SHORT).show();
        } else {
            // Ok, we have data. Save it into a DB Card Entity Instance.
            LoyaltyCard newLC = new LoyaltyCard();
            newLC.setCompany(companyNameEditText.getText().toString());
            newLC.setAddress(addressEditText.getText().toString());
            newLC.setLoyaltyCode(clientCodeEditText.getText().toString());
            newLC.setThemeColor(colorChosen);
            newLC.setLogo(Utils.imageToByteArray(bitmapLogo));
            newLC.setQRCode(Utils.imageToByteArray(bitmapQRCode));
            newLC.setBarCode(Utils.imageToByteArray(bitmapBarcode));

            // Save it into Context
            lcDAO.insertNewLoyaltyCard(newLC);

            // Notify User
            Toast.makeText(this, "Loyalty Card Creation Successful", Toast.LENGTH_SHORT).show();

            // Clean Activity Elements
            companyNameEditText.setText("");
            addressEditText.setText("");
            clientCodeEditText.setText("");
            logoImageView.setImageResource(R.drawable.ic_standard_image);
            qrCodeImageView.setImageResource(R.drawable.ic_qr_code);
            barcodeImageView.setImageResource(R.drawable.ic_barcode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_LOGO_IMAGE) {
            try {
                bitmapLogo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                if (bitmapLogo != null) {
                    logoImageView.setImageBitmap(bitmapLogo);
                } else {
                    Toast.makeText(this, "Error: Could Not Load Image File", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}