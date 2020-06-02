package com.bettingScanner.api.tipsport;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Match {
    private final String description;
    private final int id;

    public Match(String description, int id) {
        this.description = description;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public static List<Match> parseFromJson(String rawJson) {
        List<Match> res = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(rawJson);
            JSONArray tabs = json.getJSONArray("offerSuperSports").getJSONObject(0).getJSONArray("tabs");
            for (int i = 0; i < tabs.length(); i++) {
                JSONObject tab = tabs.getJSONObject(i);
                JSONArray competitions = tab.getJSONArray("offerCompetitions");
                for (int j = 0; j < competitions.length(); j++) {
                    JSONObject comp = competitions.getJSONObject(j);
                    JSONArray matches = comp.getJSONArray("offerMatches");
                    for (int k = 0; k < matches.length(); k++) {
                        JSONObject match = matches.getJSONObject(k).getJSONObject("match");
                        int id = match.getInt("id");
                        String desc = match.getString("nameFull");
                        res.add(new Match(desc, id));
                    }
                }
            }
        } catch (JSONException ex) {

        }
        return res;
    }
}