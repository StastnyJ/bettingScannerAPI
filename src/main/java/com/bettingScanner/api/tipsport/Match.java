package com.bettingScanner.api.tipsport;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Match {
    private final String description;
    private final int id;
    private final String matchUrl;

    public Match(String description, int id, String matchUrl) {
        this.description = description;
        this.id = id;
        this.matchUrl = matchUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getMatchUrl() {
        return this.matchUrl;
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
                JSONArray competitions = tab.getJSONArray("offerCompetitionAnnuals");
                for (int j = 0; j < competitions.length(); j++) {
                    JSONObject comp = competitions.getJSONObject(j);
                    JSONArray matches = comp.getJSONArray("offerMatches");
                    for (int k = 0; k < matches.length(); k++) {
                        JSONObject match = matches.getJSONObject(k).getJSONObject("match");
                        int id = match.getInt("id");
                        String desc = match.getString("nameFull");
                        String home = match.getString("homeParticipant").toLowerCase().replace(" ", "-");
                        String visit = match.getString("visitingParticipant").toLowerCase().replace(" ", "-");
                        String sport = match.getString("nameSuperSport").toLowerCase().replace(" ", "-");
                        String matchUrl = String.format("https://www.tipsport.cz/%s-%s-%s-%d", sport, home, visit, id);
                        res.add(new Match(desc, id, matchUrl));
                    }
                }
            }
        } catch (JSONException ex) {

        }
        return res;
    }

    public static Match parse(String raw) {
        String[] splited = raw.split("<split>");
        return new Match(splited[0], Integer.parseInt(splited[1]), splited[2]);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Match))
            return false;
        Match other = (Match) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return String.format("%s<split>%d<split>%s", this.description, this.id, this.matchUrl);
    }
}