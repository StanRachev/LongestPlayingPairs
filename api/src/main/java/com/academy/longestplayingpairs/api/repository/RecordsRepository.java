package com.academy.longestplayingpairs.api.repository;

import com.academy.longestplayingpairs.api.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordsRepository extends JpaRepository<Record, Integer> {
}