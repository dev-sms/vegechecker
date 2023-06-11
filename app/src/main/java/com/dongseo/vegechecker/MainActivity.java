package com.dongseo.vegechecker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {
    String scannedBarcode;
    String scannedPrdlst_nm;
    String scannedProduct;
    String scannedRawMaterial;
    Button scanButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanButton = (Button) findViewById(R.id.scanBtn);

        scanButton.setOnClickListener(v -> {
            mStartForResult.launch(new Intent(getApplicationContext(), ScanBarcodeActivity.class));
        });
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        scannedBarcode = intent.getStringExtra("barcode");
                        Log.d("MainActivity", "barcode scanned clear" + scannedBarcode);
                        mSearchProduct.launch(new Intent(getApplicationContext(), SearchProduct.class).putExtra("barcode", scannedBarcode));
                    }
                }
            });


    ActivityResultLauncher<Intent> mSearchProduct = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        scannedProduct = intent.getStringExtra("product");
                        scannedPrdlst_nm = intent.getStringExtra("prdlst_nm");
                        Log.d("MainActivity", scannedProduct);
                        mGetRawMaterial.launch(new Intent(getApplicationContext(), GetRawMaterialActivity.class).putExtra("prdlst_nm", scannedPrdlst_nm));
                    }
                }
            });

    ActivityResultLauncher<Intent> mGetRawMaterial = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        scannedRawMaterial = intent.getStringExtra("material");
                        Log.d("GetRawMaterialActivity", scannedRawMaterial);
                        MaterialItem items = new MaterialItem(scannedRawMaterial, scannedProduct, false);
                        mFilteringMaterialActivity.launch(new Intent(getApplicationContext(), FilteringMaterialActivity.class).putExtra("items", items));
                    }
                }
            });

    ActivityResultLauncher<Intent> mFilteringMaterialActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        Log.d("mFilteringMaterialActivity", intent.getStringExtra("result"));
                    }
                }
            });
}