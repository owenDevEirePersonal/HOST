package com.deveire.dev.host.data;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by owenryan on 08/09/2017.
 */

public class SignInRecord
{
    String stationID;
    String serializedTag;
    Date timestamp;

    public SignInRecord(String stationID, String serializedTag, String timestamp)
    {
        this.stationID = stationID;
        this.serializedTag = serializedTag;
        setTimestamp(timestamp);
    }

    public SignInRecord(String stationID, String serializedTag, Date timestamp)
    {
        this.stationID = stationID;
        this.serializedTag = serializedTag;
        setTimestamp(timestamp);
    }

    public SignInRecord(String serializedRecord)
    {
        Log.i("Tags", "DeSerializing Signin: " + serializedRecord);
        String trimmedSerializedTag = serializedRecord.substring(1, serializedRecord.length() -1);
        for (String aFieldPair: trimmedSerializedTag.split(",,,,,"))
        {
            String[] aPair = aFieldPair.split(":::::");
            Log.i("Tags", "DeSerialized Signin Pair Size: " + aPair.length);
            switch (aPair[0])
            {
                case "stationID": this.stationID = aPair[1]; break;
                case "tag": this.serializedTag = aPair[1]; break;
                case "timestamp": setTimestamp(aPair[1]); break;
            }
        }
        Log.i("Tags", "DeSerialized SigninRow:" + this.stationID + " " + this.serializedTag + " " + getTimestamp().toString());
    }


    public String getStationID()
    {
        return stationID;
    }

    public void setStationID(String stationID)
    {
        this.stationID = stationID;
    }

    public String getSerializedTag() { return serializedTag;}

    public String getTagID()
    {
        return (new RoomTag(serializedTag).getTagID());
    }

    public String getTagName()
    {
        return (new RoomTag(serializedTag).getName());
    }

    public String getTagType()
    {
        return (new RoomTag(serializedTag).getType());
    }

    public boolean getTagIsOnJob()
    {
        return (new RoomTag(serializedTag).isBeingCleaned());
    }

    public void setSerializedTag(String aSerializedTag)
    {
        this.serializedTag = aSerializedTag;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public String getTimestampString()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(timestamp);
    }

    public void setTimestamp(String timestamp)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            this.timestamp = dateFormat.parse(timestamp);
        }
        catch (ParseException e)
        {
            Log.e("offline Load", "ParseException in SignInsRow: " + e.toString() + "\n Using timestamp:" + timestamp);
        }
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public String serializeRecord()
    {
        String serialized = "[";
        serialized += "stationID:::::" + this.getStationID();
        serialized += ",,,,,";
        serialized += "tag:::::" + this.getSerializedTag();
        serialized += ",,,,,";
        serialized += "timestamp:::::" + this.getTimestampString();
        serialized += "]";
        Log.i("TagsRow", "Serialized as: " + serialized);
        return serialized;
    }
}
