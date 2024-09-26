package com.example.documentapi.service;

import com.example.documentapi.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public interface IDocumentService {

	public List<Document> getAllDocuments();

	public byte[] download(String name) throws MalformedURLException, IOException;
	public String upload(MultipartFile name, String key) throws IOException;
	public boolean delete(String name);
	public Path getPath(String name);

	Document findByName(String documentName);

	Page<Document> getDocuments(LocalDateTime startDate, LocalDateTime endDate, Long userId, String action, Pageable pageable);

	List<Document> getDocumentsByBusinessId(Long businessId);
}
