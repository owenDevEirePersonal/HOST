package com.deveire.dev.host.speechIntents;

import com.deveire.dev.host.SpeechIntent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by owenryan on 03/05/2018.
 */

public class PingingFor_JanitorTroubleTicket2 extends SpeechIntent
{

    public static final String Response_Leak = "Leak";
    public static final String Response_Tile = "Tile";

    public PingingFor_JanitorTroubleTicket2()
    {
        super("PingingFor_JanitorTroubleTicket2");
        setFillInIntent(false);
        setSpeechPrompt("Raising Trouble ticket. Whats seems to be the problem?");
        HashMap<String, ArrayList<String>> responses = new HashMap<>();
        responses.put(Response_Leak, SpeechIntent.compileSynonyms(new String[]{"leak", "water on the floor", "pool of water", "leaking", "water coming", "liquid"}));
        responses.put(Response_Tile, SpeechIntent.compileSynonyms(new String[]{"cracked tile", "broken tile", "smashed tile", "damaged tile", "broken tile", "crack in the tiles", "tile is cracked"}));
        setResponses(responses);
    }


}
