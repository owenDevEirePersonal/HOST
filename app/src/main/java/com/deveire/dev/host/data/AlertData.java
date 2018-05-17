package com.deveire.dev.host.data;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by owenryan on 08/09/2017.
 */

public class AlertData
{
    private String stationID;
    private String alertText;
    private boolean isActive;
    private String type;

    private Boolean isPriority;
    private Date earliestValidDate;
    private Date latestValidDate;
    private String recipientName;

    /*e.g. of alerts
        1 42  today tommorow
        2 42 Gordon Freeman today tommorrow
        3 42 man today tommorrow
        4 42 tommorow day after
        5 42 Gordon Freeman tommorrow day after
        6 42 man tommorrow day after
    */


    public AlertData(String stationID, String alertText, boolean isActive, String type, boolean inPriority, String inRecipientName, String inEarlyDate, String inLatestDate)
    {
        this.stationID = stationID;
        this.alertText = alertText;
        this.isActive = isActive;
        this.type = type;
        this.isPriority = inPriority;
        recipientName = inRecipientName;

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try
        {
            this.earliestValidDate = format.parse(inEarlyDate);
            this.latestValidDate = format.parse(inLatestDate);
        }
        catch (Exception e)
        {
            Log.e("AlertData", "Parse Error creating AlertData: " + e.toString());
        }
    }

    public AlertData(String serializedAlert)
    {
        Log.i("Alerts", "DeSerializing Alert: " + serializedAlert);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String trimmedSerializedTag = serializedAlert.substring(1, serializedAlert.length() -1);
        for (String aFieldPair: trimmedSerializedTag.split(",,,"))
        {
            String[] aPair = aFieldPair.split(":::");
            switch (aPair[0])
            {
                case "stationID": if(aPair.length > 1){this.stationID = aPair[1];} else {this.stationID = "Error: StationID Not Found";} break;
                case "alertBody": if(aPair.length > 1){this.alertText = aPair[1];} else {this.alertText = "Error: Alert Body Not Found";} break;
                case "type": this.type = aPair[1]; break;
                case "isActive": if(aPair[1].matches("true")){this.isActive = true;} else {this.isActive = false;} break;
                case "isPriority": if(aPair[1].matches("true")){this.isPriority = true;} else {this.isPriority = false;} break;
                case "recipientName": if(aPair.length > 1){this.recipientName = aPair[1];} else {this.recipientName = "";} break;
                case "earliestValidDate": try {this.earliestValidDate = format.parse(aPair[1]);} catch(Exception e){Log.e("AlertData", "Parse Error creating AlertData: " + e.toString());} break;
                case "latestValidDate": try {this.latestValidDate = format.parse(aPair[1]);} catch(Exception e){Log.e("AlertData", "Parse Error creating AlertData: " + e.toString());} break;
            }
        }
        Log.i("Alerts", "DeSerialized AlertData:" + this.getStationID() + " " + this.getAlertText() + " " + this.getType() + " " + this.isActive() + " " + this.isPriority() + " " + this.getRecipientName() + " " + this.getEarliestValidDateString() + " " + this.getLatestValidDateString());
    }

    public String getStationID()
    {
        return stationID;
    }

    public void setStationID(String stationID)
    {
        this.stationID = stationID;
    }

    public String getAlertText()
    {
        return alertText;
    }

    public void setAlertText(String alertText)
    {
        this.alertText = alertText;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean active)
    {
        isActive = active;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public boolean isPriority()
    {
        return isPriority;
    }

    public String getRecipientName()
    {
        return recipientName;
    }

    public void setRecipientName(String recipientName)
    {
        this.recipientName = recipientName;
    }

    public Date getEarliestValidDate()
    {
        return earliestValidDate;
    }

    public Date getLatestValidDate()
    {
        return latestValidDate;
    }

    public String getEarliestValidDateString()
    {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(earliestValidDate);
    }

    public String getLatestValidDateString()
    {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(latestValidDate);
    }

    public void setIsPriority(Boolean priority)
    {
        isPriority = priority;
    }

    public void setEarliestValidDate(Date earliestValidDate)
    {
        this.earliestValidDate = earliestValidDate;
    }

    public void setLatestValidDate(Date latestValidDate)
    {
        this.latestValidDate = latestValidDate;
    }

    public String serialize()
    {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String serialized = "[";
        serialized += "stationID:::" + this.getStationID();
        serialized += ",,,";
        serialized += "alertBody:::" + this.getAlertText();
        serialized += ",,,";
        serialized += "type:::" + this.getType();
        serialized += ",,,";
        if(isActive())
        {
            serialized += "isActive:::" + "true";
        }
        else
        {
            serialized += "isActive:::" + "false";
        }
        serialized += ",,,";
        if(isPriority())
        {
            serialized += "isPriority:::" + "true";
        }
        else
        {
            serialized += "isPriority:::" + "false";
        }
        serialized += ",,,";
        serialized += "recipientName:::" + this.getRecipientName();
        serialized += ",,,";
        serialized += "earliestValidDate:::" + this.getEarliestValidDateString();
        serialized += ",,,";
        serialized += "latestValidDate:::" + this.getLatestValidDateString();
        serialized += "]";
        Log.i("Alerts", "Serialized as: " + serialized);
        return serialized;
    }

}
