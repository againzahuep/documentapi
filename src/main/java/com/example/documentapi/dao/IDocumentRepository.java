package com.example.documentapi.dao;

import com.example.documentapi.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IDocumentRepository extends JpaRepository<Document, Long> {
    public List<Document> findAll();

    public Page<Document> findAll(Pageable pageable);

    @Query("SELECT d FROM Document d WHERE (:startDate IS NULL OR :startDate >= d.createdAt) " +
            "AND (:endDate IS NULL OR :endDate <= d.createdAt) " +
            "AND (:userId IS NULL OR d.user.id = :userId) " +
            "OR (:action IS NULL OR d.action = :action)")
    Page<Document> findByFilters(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("userId") Long userId,
            @Param("action") String action,
            Pageable pageable);

    @Query("SELECT d FROM Document d WHERE d.user.business.id = :businessId")
    List<Document> findByBusinessId(@Param("businessId") Long businessId);


    Document findByName(String documentName);
}