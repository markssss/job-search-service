/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sebicom.service;

import com.sebicom.util.Log;
import com.sebicom.util.States;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author mark
 */
public class LocationDeterminator {

    public boolean isZipcode(String location) {
        return StringUtils.isNumeric(location);
    }

    public boolean isStateCodeOnly(String location) {
        return (location.length() == 2 && StringUtils.isAlpha(location) && States.getAllStateCodes().contains(location.toUpperCase()));
    }

    public boolean isCityAndState(String location) {

        if (StringUtils.contains(location, ",")) {
            String[] parts = location.split(",");
            Log.d(LocationDeterminator.class, "isCityAndState() Parts:");
            int count = 1;
            for (String part : parts) {
                Log.d(LocationDeterminator.class, "Parts " + count++ + ": " + part);
            }

            if (!parts[0].isEmpty() && !parts[1].isEmpty()) {
                if (States.getLowerCaseStates().containsKey(parts[1].toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isCityAndState(String location, String delimiter) {

        if (StringUtils.contains(location, delimiter)) {
            String[] parts = location.split(delimiter);
            Log.d(LocationDeterminator.class, "isCityAndState() Parts:");
            int count = 1;
            for (String part : parts) {
                Log.d(LocationDeterminator.class, "Parts " + count++ + ": " + part);
            }

            if (parts.length == 2 && States.getLowerCaseStates().containsKey(parts[1].toLowerCase())) {
                return true;
            } else if (parts.length == 3 && States.getLowerCaseStates().containsKey(parts[1].toLowerCase() + " " + parts[2].toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public boolean isStateOnly(String location) {
        return States.getLowerCaseStates().containsKey(location.toLowerCase());
    }

    public boolean isCityAndStateCode(String location) {

        if (StringUtils.contains(location, ",")) {
            String[] parts = location.split(",");
            Log.d(LocationDeterminator.class, "isCityAndStateCode() Parts:");
            int count = 1;
            for (String part : parts) {
                Log.d(LocationDeterminator.class, "Parts " + count++ + ": " + part);
            }

            if (!parts[0].isEmpty() && !parts[1].isEmpty()) {
                if (States.getAllStateCodes().contains(parts[1].toUpperCase())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isCityAndStateCode(String location, String delimiter) {

        if (StringUtils.contains(location, delimiter)) {
            String[] parts = location.split(delimiter);
            Log.d(LocationDeterminator.class, "isCityAndStateCode() Parts:");
            int count = 1;
            for (String part : parts) {
                Log.d(LocationDeterminator.class, "Parts " + count++ + ": " + part);
            }

            if (parts.length > 1 && !parts[0].isEmpty() && !parts[1].isEmpty()) {
                if (States.getAllStateCodes().contains(parts[1].toUpperCase())) {
                    return true;
                }
            } else if (parts.length == 2) {
                if (States.getAllStateCodes().contains(parts[1].toUpperCase())) {
                    return true;
                }
            } else if (parts.length == 3) {
                if (States.getAllStateCodes().contains(parts[2].toUpperCase())) {
                    return true;
                }
            }
        }

        return false;
    }
}
