package com.example.documentapi.dao;

import com.example.documentapi.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IDocumentDao extends JpaRepository<Document, Long> {
    @Query("from documents")
    public List<Document> findAll();

    Document findByName(String documentName);

    @Query("SELECT d FROM Document d WHERE (:startDate IS NULL OR d.timestamp >= :startDate) " +
            "OR (:endDate IS NULL OR d.timestamp <= :endDate) " +
            "OR (:userId IS NULL OR d.user.id = :userId) " +
            "OR (:action IS NULL OR d.action = :action)")
    Page<Document> findByFilters(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("userId") Long userId,
            @Param("action") String action,
            Pageable pageable);

    @Query("SELECT d FROM Document d WHERE d.user.business.id = :businessId")
    List<Document> findByBusinessId(@Param("businessId") Long businessId);
}