package com.s3rha.spring.DAO;

import com.s3rha.spring.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepo extends JpaRepository <Rating,Long>{
}
