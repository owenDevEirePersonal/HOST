package com.deveire.dev.host;

import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.net.URI;

public class NfcToWebActivity extends AppCompatActivity
{
    NfcAdapter nfcAdapt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_to_web);

        nfcAdapt = NfcScanner.setupNfcScanner(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();


        nfcAdapt = NfcScanner.setupNfcScanner(this);
        if(nfcAdapt == null)
        {
            Toast.makeText(this, "Please turn on NFC scanner before continuing", Toast.LENGTH_LONG).show();
            finish();
        }
        else
        {
            NfcScanner.setupForegroundDispatch(this, nfcAdapt);
        }
    }

    @Override
    protected void onPause()
    {
        Log.e("TileScanner", "onStop");

        super.onPause();

        if(nfcAdapt == null)
        {
            Toast.makeText(this, "Please turn on NFC scanner before continuing", Toast.LENGTH_LONG).show();
        }
        else
        {
            NfcScanner.stopForegroundDispatch(this, nfcAdapt);
        }
        //finish();
    }

    //[NFC CODE]
    @Override
    public void onNewIntent(Intent intent)
    {
        String tagRead = NfcScanner.getTagIDFromIntent(intent);
        if(tagRead != null)
        {
            Log.i("NFC", "ID Scanned: " + tagRead);
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.ie/search?q=DevEire Host Room ID is equal to :" + tagRead));
        startActivity(browserIntent);
    }
    //[END OF NFC CODE]
}
