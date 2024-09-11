package com.academy.longestplayingpairs.api.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// A class that validates a newly created team object

public class TeamValidator {

    public static boolean isValid(Team team) {
        String toCheck = team.getName() + ", " + team.getManagerFullName();

        Pattern pattern = Pattern.compile("\\s*(?<name>([A-Z][a-z]+\\s+){0,2}[A-Z][a-z]+)\\s*[,;-]\\s*(?<manager>(([\\p{L}]+\\s+){0,5}[\\p{L}]+|[a-z]+)(?: \\([a-z]+\\))?)\\s*");
        Matcher matcher = pattern.matcher(toCheck);

        return matcher.matches();
    }
}
