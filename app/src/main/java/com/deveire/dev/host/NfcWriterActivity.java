package com.deveire.dev.host;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class NfcWriterActivity extends Activity
{
    private NfcAdapter nfcAdapt;

    private Spinner typeSpinner;
    private EditText idEditText;
    private TextView idText;
    private TextView typeText;
    private Button writeButton;
    private Switch writeUrlSwitch;

    private final String siteAddress = "https://www.google.ie";
    private String siteParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_writer);

        idEditText = (EditText) findViewById(R.id.idEditText);

        idText = (TextView) findViewById(R.id.idText);
        typeText = (TextView) findViewById(R.id.typeText);

        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles_array, R.layout.spinner_item_layout);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_layout);
        // Apply the adapter to the spinner
        typeSpinner.setAdapter(adapter);

        writeUrlSwitch = (Switch) findViewById(R.id.writeUrlSwitch);

        nfcAdapt = NfcScanner.setupNfcScanner(this);

        writeButton = (Button) findViewById(R.id.writeButton);
        writeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!idEditText.getText().toString().matches(""))
                {
                    idText.setText(idEditText.getText().toString());
                    typeText.setText(typeSpinner.getSelectedItem().toString());
                    Toast.makeText(getApplicationContext(), "Ready to write to tag.", Toast.LENGTH_LONG).show();
                }
            }
        });

        siteParameters = "";
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

        if(writeUrlSwitch.isChecked())
        {
            if (!idText.getText().toString().matches(""))
            {
                siteParameters = "/search?q=locationId=" + idText.getText().toString() + "%20locationType=" + typeText.getText().toString();
                NdefRecord rec = NdefRecord.createUri(siteAddress + siteParameters);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{rec});
                NfcScanner.writeTag(NfcScanner.getTagObjectFromIntent(intent), msg);
                Toast.makeText(this, "Location ID: " + idText.getText().toString() + " Location Type: " + typeText.getText().toString() + " written to tag.", Toast.LENGTH_LONG).show();
                idText.setText("");
                typeText.setText("");
            }
            else
            {
                Toast.makeText(this, "Failed to write to tag, please enter an ID and tap prepare to write", Toast.LENGTH_LONG).show();
            }
        }
        else
        {

            if (!idText.getText().toString().matches(""))
            {
                NdefRecord rec1 = NdefRecord.createTextRecord(null, "ID:" + idText.getText().toString());
                NdefRecord rec2 = NdefRecord.createTextRecord(null, "TYPE:" + typeText.getText().toString());
                NdefMessage msg = new NdefMessage(new NdefRecord[]{rec1, rec2});
                NfcScanner.writeTag(NfcScanner.getTagObjectFromIntent(intent), msg);
                Toast.makeText(this, "Location ID: " + idText.getText().toString() + " Location Type: " + typeText.getText().toString() + " written to tag.", Toast.LENGTH_LONG).show();
                idText.setText("");
                typeText.setText("");
            }
            else
            {
                Toast.makeText(this, "Failed to write to tag, please enter an ID and tap prepare to write", Toast.LENGTH_LONG).show();
            }
        }
    }
    //[END OF NFC CODE]
}
