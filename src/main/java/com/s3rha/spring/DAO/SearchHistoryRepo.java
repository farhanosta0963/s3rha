package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchHistoryRepo extends JpaRepository<SearchHistory,Long> {
}
