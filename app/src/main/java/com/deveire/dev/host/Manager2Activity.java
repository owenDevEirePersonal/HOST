package com.deveire.dev.host;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.deveire.dev.host.data.AlertData;
import com.deveire.dev.host.data.RoomTag;
import com.deveire.dev.host.data.SignInRecord;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.deveire.dev.host.Utils.retrieveAlerts;
import static com.deveire.dev.host.Utils.retrieveSignIns;
import static com.deveire.dev.host.Utils.saveAlertData;
import static com.deveire.dev.host.Utils.saveSignInData;

public class Manager2Activity extends FragmentActivity
{

    private EditText stationIDText;
    private EditText alertTextText;
    private EditText reciepientText;
    private EditText earliestDateText;
    private EditText latestDateText;
    private CheckBox isPrioritisedCheckBox;
    private Button addCleanupAlertButton;
    private Button addSecurityAlertButton;
    private Button registerButton;
    private Button clearButton;
    private Button clearSigninsButton;
    private TextView signinText;
    private EditText filterEditText;

    private TextWatcher filterTextWatcher;


    private ArrayList<String> allSignins;


    Timer periodicGetSigninsTimer;

    private PowerManager pm;
    private PowerManager.WakeLock wl;

    //[Offline Variables]
    private SharedPreferences savedData;

    private ArrayList<RoomTag> allTags;

    private ArrayList<AlertData> allAlerts;

    private ArrayList<SignInRecord> allSignInRecords;

    private int signInsCount;
    private int tagsCount;
    private int alertsCount;
    //[/Offline Variables]

    private String fullSignIn;
    ArrayList<SignInRecord> current3LatestSignins;


    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager2);




        signinText = (TextView) findViewById(R.id.signinsText);
        addCleanupAlertButton = (Button) findViewById(R.id.addJanitorAlertButton);
        addSecurityAlertButton = (Button) findViewById(R.id.addSecurityAlertButton);

        registerButton = (Button) findViewById(R.id.registerButton);
        clearButton = (Button) findViewById(R.id.clearButton);
        clearSigninsButton = (Button) findViewById(R.id.clearSigninsButton);
        stationIDText = (EditText) findViewById(R.id.stationIDEditText);
        alertTextText = (EditText) findViewById(R.id.alertTextEditText);
        reciepientText = (EditText) findViewById(R.id.userEditText);
        earliestDateText = (EditText) findViewById(R.id.earliestValidDateEditText);
        latestDateText = (EditText) findViewById(R.id.latestValidDateEditText);
        isPrioritisedCheckBox = (CheckBox) findViewById(R.id.isPriorityCheckBox);

        final DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        final Date earlyDefault = cal.getTime();
        earliestDateText.setText(format.format(earlyDefault));
        cal.add(Calendar.DATE, 1);
        final Date lateDefault = cal.getTime();
        latestDateText.setText(format.format(lateDefault));

        addSecurityAlertButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!stationIDText.getText().toString().matches("") && !alertTextText.getText().toString().matches(""))
                {
                    uploadAlert(RoomTag.tagtype_FLOORWALK);
                    stationIDText.setText("");
                    alertTextText.setText("");
                    reciepientText.setText("");
                    earliestDateText.setText(format.format(earlyDefault));
                    latestDateText.setText(format.format(lateDefault));
                    isPrioritisedCheckBox.setChecked(false);
                }
            }
        });

        addCleanupAlertButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!stationIDText.getText().toString().matches("") && !alertTextText.getText().toString().matches(""))
                {
                    uploadAlert(RoomTag.tagtype_ROOM);
                    stationIDText.setText("");
                    alertTextText.setText("");
                    reciepientText.setText("");
                    earliestDateText.setText(format.format(earlyDefault));
                    latestDateText.setText(format.format(lateDefault));
                    isPrioritisedCheckBox.setChecked(false);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), Register2Activity.class));
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                allAlerts = retrieveAlerts(savedData);
                for (AlertData a: allAlerts)
                {
                    a.setActive(false);
                }
                saveAlertData(savedData, allAlerts);
            }
        });

        clearSigninsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                allSignInRecords = new ArrayList<SignInRecord>();
                saveSignInData(savedData, allSignInRecords);
                retrieveAllSignins();
            }
        });

        allSignins = new ArrayList<String>();


        //[Offline Setup]
        savedData = this.getApplicationContext().getSharedPreferences("HOST SavedData", Context.MODE_PRIVATE);
        allAlerts = new ArrayList<AlertData>();
        allSignInRecords = new ArrayList<SignInRecord>();
        allTags = new ArrayList<RoomTag>();

        alertsCount = 0;
        signInsCount = 0;
        tagsCount = 0;
        //[/Offline Setup]

        //[filtering Signins]
        filterTextWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                String filter = s.toString();
                Log.i("filterWatcher", filter);
                filterSignins(filter);
            }
        };

        filterEditText = (EditText) findViewById(R.id.filterEditText);
        filterEditText.addTextChangedListener(filterTextWatcher);
        //[/filtering Signins]



        periodicGetSigninsTimer = new Timer();
        periodicGetSigninsTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                retrieveAllSignins();
            }
        }, 1000, 30000);

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "Manager_Activity_host tag");
        wl.acquire();


    }

    @Override
    protected void onPause()
    {

        periodicGetSigninsTimer.cancel();
        periodicGetSigninsTimer.purge();


        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(!wl.isHeld())
        {
            wl.acquire();
        }
    }

    @Override
    protected void onStop()
    {
        wl.release();
        super.onStop();

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }




    //[Offline loading]

    private void saveData()
    {
        Utils.saveAllData(savedData, allTags, allAlerts, allSignInRecords);
    }

    //[/Offline loading]\

    private void uploadAlert(String alertType)
    {
        allAlerts = retrieveAlerts(savedData);
        allAlerts.add(new AlertData(stationIDText.getText().toString(), alertTextText.getText().toString(), true, alertType, isPrioritisedCheckBox.isChecked(), reciepientText.getText().toString(), earliestDateText.getText().toString(), latestDateText.getText().toString()));
        saveAlertData(savedData, allAlerts);
    }

    private void retrieveAllSignins()
    {
        allSignInRecords = retrieveSignIns(savedData);
        Log.i("Signins", " starting retrieveAllSignins");

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                signinText.setText("");
            }
        });

        fullSignIn = "";
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ArrayList<SignInRecord> allRecordsOldestFirst = binarySortSignins(allSignInRecords);
        //reverse the order of allRecords so its newest first.
        ArrayList<SignInRecord> allRecordsNewestFirst = new ArrayList<>();
        for(int i = allRecordsOldestFirst.size() - 1; i >= 0; i--)
        {
            allRecordsNewestFirst.add(allRecordsOldestFirst.get(i));

            if(allRecordsOldestFirst.get(i).getTagType().matches(RoomTag.tagtype_ROOM))
            {
                fullSignIn += (allRecordsOldestFirst.get(i).getStationID() + " tagged Room " + allRecordsOldestFirst.get(i).getTagName() + " at " + format.format(allRecordsOldestFirst.get(i).getTimestamp()) + ". \n\n");
            }
            else if(allRecordsOldestFirst.get(i).getTagType().matches(RoomTag.tagtype_FLOORWALK))
            {
                fullSignIn += (allRecordsOldestFirst.get(i).getStationID() + " checked in at " + allRecordsOldestFirst.get(i).getTagName() + " during their floor walk at " + format.format(allRecordsOldestFirst.get(i).getTimestamp()) + ". \n\n");
            }
        }

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                signinText.setText(fullSignIn);
            }
        });
    }

    private void filterSignins(String inFilter)
    {
        Log.i("Signins", " starting filterSignins");
        /*TODO: redo signins text so that all entires are stored a strings in an arraylist, then redo this method to work off that list, rather than recompiling the
          entire list off of all the Arraylists of rows.*/
        allSignins = new ArrayList<String>();
        fullSignIn = "";
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        ArrayList<SignInRecord> allRecordsOldestFirst = binarySortSignins(allSignInRecords);
        //reverse the order of allRecords so its newest first.
        ArrayList<SignInRecord> allRecordsNewestFirst = new ArrayList<>();
        for(int i = allRecordsOldestFirst.size() - 1; i >= 0; i--)
        {
            if(allRecordsOldestFirst.get(i).getStationID().contains(inFilter) || allRecordsOldestFirst.get(i).getTagName().contains(inFilter))
            {
                allRecordsNewestFirst.add(allRecordsOldestFirst.get(i));

                fullSignIn += (allRecordsOldestFirst.get(i).getStationID() + " tagged Room " + allRecordsOldestFirst.get(i).getTagName() + " at " + format.format(allRecordsOldestFirst.get(i).getTimestamp()) + ". \n\n");
            }
        }

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                signinText.setText(fullSignIn);
            }
        });
    }

    private ArrayList<SignInRecord> binarySortSignins(ArrayList<SignInRecord> records)
    {
        Log.i("Binary", "Starting binary sort on records of size: " + records.size());
        ArrayList<SignInRecord> output = new ArrayList<SignInRecord>();
        ArrayList<SignInRecord> before = new ArrayList<SignInRecord>();
        ArrayList<SignInRecord> after = new ArrayList<SignInRecord>();

        if(records.size() > 0)
        {


            SignInRecord middle = records.get((int) (records.size() / 2));
            for (SignInRecord aRecord : records)
            {
                if (aRecord.getTimestamp().after(middle.getTimestamp()))
                {
                    after.add(aRecord);
                }
                else if (aRecord.getTimestamp().before(middle.getTimestamp()))
                {
                    before.add(aRecord);
                }
            }

            if (before.size() > 1)
            {
                binarySortSignins(before);
            }

            if (after.size() > 1)
            {
                binarySortSignins(after);
            }


            output = before;
            output.add(middle);
            output.addAll(after);
        }

        return output;
    }


}
