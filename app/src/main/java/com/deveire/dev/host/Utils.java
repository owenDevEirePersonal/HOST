package com.deveire.dev.host;

import android.content.SharedPreferences;
import android.util.Log;

import com.deveire.dev.host.data.AlertData;
import com.deveire.dev.host.data.RoomTag;
import com.deveire.dev.host.data.SignInRecord;

import java.util.ArrayList;

/**
 * Created by owenryan on 10/05/2018.
 */

public class Utils
{

    //[Offline loading]
    protected static ArrayList<AlertData> retrieveAlerts(SharedPreferences savedData)
    {

        int alertsCount = savedData.getInt("alertsCount", 0);
        Log.i("Offline", "Total number of alerts: " + alertsCount);
        ArrayList<AlertData> allAlerts = new ArrayList<AlertData>();

        for (int i = 0; i < alertsCount; i++)
        {
            allAlerts.add(new AlertData(savedData.getString("alerts" + i, "ERROR")));
        }
        return allAlerts;
    }

    protected static ArrayList<RoomTag> retrieveTags(SharedPreferences savedData)
    {
        int tagsCount = savedData.getInt("tagsCount", 0);
        Log.i("Offline", "Total number of tags: " + tagsCount);
        ArrayList<RoomTag> allTags = new ArrayList<RoomTag>();

        for (int i = 0; i < tagsCount; i++)
        {
            allTags.add(new RoomTag(savedData.getString("tags" + i, "ERROR")));
        }

        return allTags;
    }

    protected static ArrayList<SignInRecord> retrieveSignIns(SharedPreferences savedData)
    {
        int signInsCount = savedData.getInt("signInsCount", 0);
        Log.i("Offline", "Total number of signins: " + signInsCount);
        ArrayList<SignInRecord> allSignIns = new ArrayList<SignInRecord>();

        for (int i = 0; i < signInsCount; i++)
        {
            allSignIns.add(new SignInRecord(savedData.getString("signIns" + i, "ERROR")));
        }

        return allSignIns;
    }

    protected static void saveAllData(SharedPreferences savedData, ArrayList<RoomTag> allTags, ArrayList<AlertData> allAlerts, ArrayList<SignInRecord> allSignIns)
    {
        Log.i("Saving", "Saving Data in saveData()");

        SharedPreferences.Editor edit = savedData.edit();
        int alertsCount = allAlerts.size();
        int tagsCount = allTags.size();
        int signInsCount = allSignIns.size();
        edit.putInt("alertsCount", alertsCount);
        edit.putInt("tagsCount", tagsCount);
        edit.putInt("signInsCount", signInsCount);


        for (int i = 0; i < allAlerts.size(); i++)
        {
            edit.putString("alerts" + i, allAlerts.get(i).serialize());
        }

        for (int i = 0; i < allTags.size(); i++)
        {
            edit.putString("tags" + i, allTags.get(i).serializeTag());
        }

        for (int i = 0; i < allSignIns.size(); i++)
        {
            edit.putString("signIns" + i, allSignIns.get(i).serializeRecord());
        }

        edit.commit();
        Log.i("Offline Update", "Saved Data: alertCount: " + alertsCount + ", tagscount: " + tagsCount + ", signinscount: " + signInsCount);
        Log.i("Offline Update", "Saved Data: allalerts: " + allAlerts.size() + ", alltags: " + allTags.size() + ", allsignins: " + allSignIns.size());
    }

    protected static void saveAlertData(SharedPreferences savedData, ArrayList<AlertData> allAlerts)
    {
        Log.i("Saving", "Saving Data in saveData()");

        SharedPreferences.Editor edit = savedData.edit();
        int alertsCount = allAlerts.size();
        edit.putInt("alertsCount", alertsCount);



        for (int i = 0; i < allAlerts.size(); i++)
        {
            edit.putString("alerts" + i, allAlerts.get(i).serialize());
        }

        edit.commit();
        Log.i("Offline Update", "Saved Data: alertCount: " + alertsCount );
        Log.i("Offline Update", "Saved Data: allalerts: " + allAlerts.size());
    }

    protected static void saveTagData(SharedPreferences savedData, ArrayList<RoomTag> allTags)
    {
        Log.i("Saving", "Saving Data in saveData()");

        SharedPreferences.Editor edit = savedData.edit();
        int tagsCount = allTags.size();
        edit.putInt("tagsCount", tagsCount);

        for (int i = 0; i < allTags.size(); i++)
        {
            edit.putString("tags" + i, allTags.get(i).serializeTag());
        }

        edit.commit();
        Log.i("Offline Update", "Saved Data:  tagscount: " + tagsCount);
        Log.i("Offline Update", "Saved Data:  alltags: " + allTags.size());
    }

    protected static void saveSignInData(SharedPreferences savedData, ArrayList<SignInRecord> allSignIns)
    {
        Log.i("Saving", "Saving Data in saveData()");

        SharedPreferences.Editor edit = savedData.edit();
        int signInsCount = allSignIns.size();
        edit.putInt("signInsCount", signInsCount);

        for (int i = 0; i < allSignIns.size(); i++)
        {
            edit.putString("signIns" + i, allSignIns.get(i).serializeRecord());
        }

        edit.commit();
        Log.i("Offline Update", "Saved Data: signinscount: " + signInsCount);
        Log.i("Offline Update", "Saved Data: allsignins: " + allSignIns.size());
    }

    protected static RoomTag findTagFromID(String tagIDin, ArrayList<RoomTag> allTags)
    {
        Log.i("Offline Update", "finding row from ID:" + tagIDin + ", searching " + allTags.size() + " rows.");
        for (RoomTag arow: allTags)
        {
            if(arow.getTagID().matches(tagIDin))
            {
                return arow;
            }
        }
        return null;
    }

    //[End of Offline loading]
}
