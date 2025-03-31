package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepo extends JpaRepository <Report,Long>{
}
