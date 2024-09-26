package com.example.documentapi.controller;


import com.example.documentapi.dao.IDocumentDao;
import com.example.documentapi.entity.Document;
import com.example.documentapi.service.IDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = { "http://localhost:3000" })
public class DocumentController {

    @Autowired
    private IDocumentService documentService;

    @Autowired
    private IDocumentDao documentDao;

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
    public ResponseEntity<byte[]> download(@PathVariable String documentName) throws IOException {
        byte[] content = documentService.download(documentName);

        // 1. Buscar el documento en la base de datos
        Document document = documentDao.findByName(documentName);
        if (document == null) {
            throw new RuntimeException("Documento no encontrado");
        }


        // Leer el contenido del archivo encriptado
        Path filePath = Paths.get(document.getFilePath());



        Path rutaAnterior = Paths.get("uploads").resolve(document.getFilePath()).toAbsolutePath();


        File archivoAnterior = filePath.toFile();

        String filePathName = archivoAnterior.getPath();
        filePathName =
                filePathName.replace(filePathName.split("enc")[1], document.getFileExtension());


        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePathName + "\"");



        return ResponseEntity.ok().headers(headers).body(content);
    }

    @DeleteMapping("/delete/{documentId}")
    public ResponseEntity<String> delete(@PathVariable String name) throws IOException {
        boolean d = documentService.delete(name);
        return ResponseEntity.ok("Document " + d + "deleted succesfully.");
    }

    public ResponseEntity<Page<Document>> getDocuments(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String action,
            Pageable pageable) {

        validateDates(startDate, endDate);

        Page<Document> documents = documentService.getDocuments(startDate, endDate, userId, action, pageable);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<Document>> getDocumentsByBusiness(@PathVariable Long businessId) {
        List<Document> documents = documentService.getDocumentsByBusinessId(businessId);
        return ResponseEntity.ok(documents);
    }

    private void validateDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser después de la fecha de fin.");
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }



}
