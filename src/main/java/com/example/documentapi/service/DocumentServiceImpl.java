package com.example.documentapi.service;

import com.example.documentapi.dao.IDocumentDao;
import com.example.documentapi.dao.IUserDao;
import com.example.documentapi.entity.Action;
import com.example.documentapi.entity.Document;
import com.example.documentapi.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentServiceImpl implements IDocumentService {
    private final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    IUserDao userDao;

    private final static String DIRECTORY_UPLOAD = "uploads";
    @Autowired
    private IDocumentDao documentDao;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final String[] ALLOWED_EXTENSIONS = {".doc", ".docx", ".pdf", ".odt"};

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional(readOnly = true)
    public Page<Document> findAll(Pageable pageable) {
        return documentDao.findAll(pageable);
    }

    public String upload(MultipartFile file, String password) throws IOException {
        // Validaciones
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty.");
        }

        // Validar la extensión del archivo
        String fileName = file.getOriginalFilename();
        if (!isValidExtension(fileName)) {
            throw new IllegalArgumentException("Type file not allowed.");
        }

        // Save the file
        File directory = new File(DIRECTORY_UPLOAD);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Encrypt the file
        byte[] fileBytes = file.getBytes();
        String encryptedContent = bCryptPasswordEncoder.encode(new String(fileBytes));

        // Save the encrypted file
        String extension = getExtension(fileName);


        Path filePath = Paths.get(DIRECTORY_UPLOAD + fileName + ".enc"); // Añadir .enc para indicar que está encriptado
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(encryptedContent.getBytes());
        }


        // Save the info of document in database
        Document document = new Document();
        document.setName(fileName);
        document.setFileExtension(extension);
        document.setCreatedAt(LocalDateTime.now());
        documentDao.save(document);

        // Registrar la acción del usuario (esto puede ser modificado según tu lógica)


        User userDB = userDao.findByPassword(document.getUser().getPassword()).orElseThrow();
        if(passwordEncoder.matches(password, userDB.getPassword())){
            userDB.setAction(Action.UPLOADED);
            userDB.setActionDate(LocalDateTime.now());
            userDao.save(userDB);
        }


        return fileName;

    }

    @Override
    public boolean delete(String name) {
        return false;
    }


    @Override
    public List<Document> findByUserIdAndTimestampBetween(Long userId, LocalDateTime start, LocalDateTime end) {
        return null;
    }

    @Override
    public byte[] download(String name) throws IOException {

        // 1. Buscar el documento en la base de datos
        Document document = documentDao.findByName(name).orElseThrow();
        if (document == null) {
            throw new RuntimeException("Documento no encontrado");
        }

        // Leer el contenido del archivo encriptado
        Path filePath = Paths.get(document.getFilePath());
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean isValidExtension(String fileName) {
        for (String extension : ALLOWED_EXTENSIONS) {
            if (fileName != null && fileName.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    private String getExtension(String fileName) {
        for (String extension : ALLOWED_EXTENSIONS) {
            if (fileName.toLowerCase().substring(fileName.length() - 3, fileName.length()).equals("doc")) {
                return ".doc";
            } else if (fileName.toLowerCase().substring(fileName.length() - 4, fileName.length()).equals("docx")) {
                return ".docx";
            } else if (fileName.toLowerCase().substring(fileName.length() - 3, fileName.length()).equals("pdf")) {
                return ".pdf";
            } else if (fileName.toLowerCase().substring(fileName.length() - 3, fileName.length()).equals("odt")) {
                return ".odt";
            }
        }
        return "";
    }

    public boolean deleteDocument(String documentName) {

        if (documentName != null && documentName.length() > 0) {
            Path previousPath = Paths.get("uploads").resolve(documentName).toAbsolutePath();
            File previousFile = previousPath.toFile();
            if (previousFile.exists() && previousFile.canRead()) {
                previousFile.delete();
                return true;
            }
        }

        return false;
    }

    public Page<Document> getDocuments(LocalDateTime startDate, LocalDateTime endDate, Long userId, String action, Pageable pageable) {
        return documentDao.findByFilters(startDate, endDate, userId, action, pageable);
    }

    public List<Document> getDocumentsByBusinessId(Long businessId) {
        return documentDao.findByBusinessId(businessId);
    }

    @Override
    public List<Document> findAll() {
        return documentDao.findAll();
    }

    @Override
    public Path getPath(String name) {
        return Paths.get(DIRECTORY_UPLOAD).resolve(name).toAbsolutePath();
    }

    @Override
    public Document findByName(String documentName) {
        return documentDao.findByName(documentName).orElseThrow();
    }


}

