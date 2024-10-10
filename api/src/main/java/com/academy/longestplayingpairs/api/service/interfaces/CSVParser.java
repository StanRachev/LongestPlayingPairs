package com.academy.longestplayingpairs.api.service.interfaces;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CSVParser {

    default void setDateFormatOption(String dateFormat) {
        // No-op by default
    }

    @Transactional
    List<String> csvParse();
}
