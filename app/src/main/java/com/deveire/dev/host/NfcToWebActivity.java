package com.deveire.dev.host;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.Ndef;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

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
        ArrayList<String> stringRecords = new ArrayList<String>();
        String locationId = "";
        String locationType = "";
        String staffID = Settings.Secure.ANDROID_ID;

        String tagRead = NfcScanner.getTagIDFromIntent(intent);
        if(tagRead != null)
        {
            Log.i("NFC", "ID Scanned: " + tagRead);
        }


        Ndef ndef = Ndef.get(NfcScanner.getTagObjectFromIntent(intent));
        if (ndef == null) {
            Log.e("NFC", "NDEF is not supported by this Tag.");
        }
        NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        NdefRecord[] records = ndefMessage.getRecords();
        for (NdefRecord ndefRecord : records) {
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {
                    stringRecords.add(NfcScanner.readText(ndefRecord));
                } catch (UnsupportedEncodingException e) {
                    Log.e("NFC", "Unsupported Encoding", e);
                }
            }
        }


        Log.i("NFC", "stringRecords: " + stringRecords.toString());
        for (String aString: stringRecords)
        {
            if(aString.startsWith("ID:"))
            {

                locationId = aString.substring(aString.indexOf(":") + 1);
            }
            else if(aString.startsWith("TYPE:"))
            {

                locationType = aString.substring(aString.indexOf(":") + 1);
            }
        }

        String params = "/search?q=params=" + locationId + "," + locationType;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.ie" + params));
        startActivity(browserIntent);
    }
    //[END OF NFC CODE]
}
