package com.Denzo.firl.Model;

import androidx.room.Ignore;
import java.util.Map;

public class DiscoveryPreferences {
    private int minAge = 18;
    private int maxAge = 50;
    private int distance = 50;
    
    @Ignore
    private Map<String, Object> additionalPrefs;

    public DiscoveryPreferences() {}

    public int getMinAge() { return minAge; }
    public void setMinAge(int minAge) { this.minAge = minAge; }

    public int getMaxAge() { return maxAge; }
    public void setMaxAge(int maxAge) { this.maxAge = maxAge; }

    public int getDistance() { return distance; }
    public void setDistance(int distance) { this.distance = distance; }

    public Map<String, Object> getAdditionalPrefs() { return additionalPrefs; }
    public void setAdditionalPrefs(Map<String, Object> additionalPrefs) { this.additionalPrefs = additionalPrefs; }
}
