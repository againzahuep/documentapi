package com.example.documentapi.controller;


import com.example.documentapi.entity.Document;
import com.example.documentapi.service.IDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = { "http://localhost:3000" })
public class DocumentController {

    @Autowired
    private IDocumentService documentService;


    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam MultipartFile file, String key) {
        try {
            String name = documentService.upload(file, key);
            return ResponseEntity.ok("Document" + name + "loaded succesfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error inesperado: " + e.getMessage());
        }
    }


    @GetMapping("/download/{documentId}")
    public ResponseEntity<Resource> download(@PathVariable String documentName, @PathVariable String password)  {
        byte[] content = new byte[0];
        try {
            content = documentService.download(documentName, password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (content != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new ByteArrayResource(content));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<Document>> getAllUsers() {
        List<Document> documents = documentService.findAll();
        return ResponseEntity.ok(documents);
    }

    @DeleteMapping("/delete/{documentId}")
    public ResponseEntity<String> delete(@PathVariable String name) throws IOException {
        boolean d = documentService.delete(name);
        return ResponseEntity.ok("Document " + d + "deleted succesfully.");
    }
    @GetMapping("/documents/{userId}/date-range")
    public ResponseEntity<Page<Document>> getDocuments(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String action,
            Pageable pageable) {

        validateDates(startDate, endDate);

        Page<Document> documents = documentService.filterBy(startDate, endDate, userId, action, pageable);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<Document>> getDocumentsByBusiness(@PathVariable Long businessId) {
        List<Document> documents = documentService.getDocumentsByBusinessId(businessId);
        return ResponseEntity.ok(documents);
    }

    private void validateDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null && startDate.isBefore(endDate)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser después de la fecha de fin.");
        }
    }


}
