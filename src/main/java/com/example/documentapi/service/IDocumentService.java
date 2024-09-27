package com.example.documentapi.service;

import com.example.documentapi.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public interface IDocumentService {

	List<Document> findByUserIdAndTimestampBetween(Long userId, LocalDateTime start, LocalDateTime end);
	byte[] download(String name, String passwor) throws IOException;

	@Transactional(readOnly = true)
	Page<Document> findAll(Pageable pageable);

	String upload(MultipartFile name, String key) throws IOException;
	boolean delete(String name);

	Page<Document> filterBy(LocalDateTime startDate, LocalDateTime endDate, Long userId, String action, Pageable pageable);

	List<Document> getDocumentsByBusinessId(Long businessId);

	List<Document> findAll();

	void init() throws IOException;

}
