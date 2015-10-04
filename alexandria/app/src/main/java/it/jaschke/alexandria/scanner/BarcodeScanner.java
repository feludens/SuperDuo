package it.jaschke.alexandria.scanner;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import javax.xml.transform.Result;

import it.jaschke.alexandria.AddBook;
import it.jaschke.alexandria.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private final String TAG = "Scanner Activiy";

    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(com.google.zxing.Result result) {
        Intent intent = new Intent();
        intent.putExtra(AddBook.BARCODE_RESULT_CODE,result.getText());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
