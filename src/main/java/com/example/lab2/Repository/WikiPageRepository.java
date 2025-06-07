package com.example.lab2.Repository;

import com.example.lab2.Model.WikiPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface WikiPageRepository extends JpaRepository<WikiPage, Long> {
    @Query("SELECT w FROM WikiPage w WHERE w.category.id = :categoryId")
    List<WikiPage> findByCategoryId(@Param("categoryId") Long categoryId);
}