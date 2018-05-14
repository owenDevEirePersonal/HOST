package com.deveire.dev.host.data;

import android.util.Log;

/**
 * Created by owenryan on 08/09/2017.
 */

public class RoomTag
{
    public static final String tagtype_UNDEFINED_TAG = "Undefined";
    public static final String tagtype_ROOM = "Room";
    public static final String tagtype_FLOORWALK = "FloorWalk";

    private String name;
    private String tagID;
    private String type;
    private boolean isOccupied;

    public RoomTag(String name, String tagID, String type)
    {
        this.name = name;
        this.tagID = tagID;
        this.type = type;
        this.isOccupied = false;
    }

    public RoomTag(String serializedTag)
    {
        String trimmedSerializedTag = serializedTag.substring(1, serializedTag.length() -1);
        for (String aFieldPair: trimmedSerializedTag.split(",,,"))
        {
            String[] aPair = aFieldPair.split(":::");
            switch (aPair[0])
            {
                case "name": this.name = aPair[1]; break;
                case "tagID": this.tagID = aPair[1]; break;
                case "type": this.type = aPair[1]; break;
                case "isBeingCleaned": if(aPair[1].matches("true")){this.isOccupied = true;} else {this.isOccupied = false;} break;
            }
        }
        Log.i("Tags", "DeSerialized TagsRow:" + this.name + " " + this.tagID + " " + this.type + " " + this.isOccupied);
    }

    public RoomTag()
    {
        this.name = "Undefined Name";
        this.type = tagtype_UNDEFINED_TAG;
        this.tagID = "Undefined tag ID";
        this.isOccupied = false;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTagID()
    {
        return tagID;
    }

    public void setTagID(String tagID)
    {
        this.tagID = tagID;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public boolean isBeingCleaned()
    {
        return this.isOccupied;
    }

    public void setIsBeingCleaned(boolean onJob)
    {
        this.isOccupied = onJob;
        Log.i("OnTheJob", "TagsRow:" + tagID + " " + name + " " + type + " OnJob is now: " + isOccupied);
    }

    public String serializeTag()
    {
        String serialized = "[";
        serialized += "name:::" + this.getName();
        serialized += ",,,";
        serialized += "tagID:::" + this.getTagID();
        serialized += ",,,";
        serialized += "type:::" + this.getType();
        serialized += ",,,";
        if(isBeingCleaned())
        {
            serialized += "isBeingCleaned:::" + "true";
        }
        else
        {
            serialized += "isBeingCleaned:::" + "false";
        }
        serialized += "]";
        Log.i("Tags", "Serialized as: " + serialized);
        return serialized;
    }

}
