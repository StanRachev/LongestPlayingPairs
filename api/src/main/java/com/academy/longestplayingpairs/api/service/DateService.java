package com.academy.longestplayingpairs.api.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DateService {

    Map<String, Pattern> datePatterns;

    public DateService() {
        this.datePatterns = addPatterns();
    }

    LocalDate convertToLocalDate(String dateFormatOption, String dateStr) {
        Pattern datePattern = getPattern(datePatterns, dateFormatOption);

        return toLocalDate(datePattern, dateStr);
    }

    private Map<String, Pattern> addPatterns() {
        Map<String, Pattern> datePatterns = new HashMap<>();
        datePatterns.put("dd.mm.yyyy", Pattern.compile("^(?<dayjun>1[4-9]|2[0-9]|30)([/\\-.])(?<jun>0?6|[Jj]un)\\2(?:2024|24)$|^(?<dayjul>0?[1-9]|1[0-4])([/\\-.])(?<jul>0?7|[Jj]ul)\\5(?:2024|24)$"));
        datePatterns.put("mm.dd.yyyy", Pattern.compile("^\\s*(?<jun>0?6|[Jj]un)([/\\-.])(?<dayjun>1[4-9]|2[0-9]|30)\\2(?:2024|24)\\s*$|^\\s*(?<jul>0?7|[Jj]ul)([/\\-.])(?<dayjul>0?[1-9]|1[0-4])\\5(?:2024|24)\\s*$"));
        datePatterns.put("yyyy.mm.dd", Pattern.compile("^(?:2024|24)([/\\-.])(?<jun>0?6|[Jj]un)\\1(?<dayjun>1[4-9]|2[0-9]|30)$|^(?:2024|24)([/\\-.])(?<jul>0?7|[Jj]ul)\\4(?<dayjul>0?[1-9]|1[0-4])$"));
        datePatterns.put("yyyy.dd.mm", Pattern.compile("^(?:2024|24)([/\\-.])(?<dayjun>1[4-9]|2[0-9]|30)\\1(?<jun>0?6|[Jj]un)$|^(?:2024|24)([/\\-.])(?<dayjul>0?[1-9]|1[0-4])\\4(?<jul>0?7|[Jj]ul)$"));

        return datePatterns;
    }

    private Pattern getPattern(Map<String, Pattern> map, String dateFormat) {
        return map.get(dateFormat);
    }

    boolean isDateCorrect(String patternChosen, String date) {
        Pattern pattern = getPattern(datePatterns, patternChosen);
        Matcher matcher = pattern.matcher(date);

        return matcher.matches();
    }

    LocalDate toLocalDate(Pattern pattern, String date) {

        Matcher matcher = pattern.matcher(date);

        int day = 0;
        int month = 0;
        int year = 2024;

        if (matcher.matches()) {
            if (matcher.group("dayjun") != null) {
                day = Integer.parseInt(matcher.group("dayjun"));
                month = 6;
            } else {
                day = Integer.parseInt(matcher.group("dayjul"));
                month = 7;
            }
        }
        return LocalDate.of(year, month, day);
    }
}
