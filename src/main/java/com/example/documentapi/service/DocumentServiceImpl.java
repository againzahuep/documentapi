package com.example.documentapi.service;

import com.example.documentapi.dao.IDocumentRepository;
import com.example.documentapi.dao.IUserRepository;
import com.example.documentapi.entity.Document;
import com.example.documentapi.entity.User;
import com.example.documentapi.utils.Utils;
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

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentServiceImpl implements IDocumentService {
    private final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    IUserRepository userRepository;

    private final static String UPLOADS_FOLDER = "uploads";
    @Autowired
    private IDocumentRepository documentRepository;

    private static final String[] ALLOWED_EXTENSIONS = {".doc", ".docx", ".pdf", ".odt"};


    @Override
    public void init() {
        // TODO Auto-generated method stub
        try {
            Files.createDirectory(Paths.get(UPLOADS_FOLDER));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Document> findAll(Pageable pageable) {
        return documentRepository.findAll(pageable);
    }

    public String upload(MultipartFile file, String password) throws IOException {
        // Validaciones
        String fileName = file.getOriginalFilename();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        User user = userRepository.findByUsername(username);

        if (user != null) {
            // Procesar el archivo

            if (file.isEmpty()) {
                throw new IllegalArgumentException("File is empty.");
            }

            // Validar la extensión del archivo

            if (!isValidExtension(fileName)) {
                throw new IllegalArgumentException("Type file not allowed.");
            }

            // Save the file
            File directory = new File(UPLOADS_FOLDER);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            byte[] encryptedContent = new byte[0];
            try {
                encryptedContent = Utils.encryptFile(file.getBytes(), password);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Save file to disk
            String filePath = UPLOADS_FOLDER + "/" + file.getOriginalFilename();
            Files.write(Paths.get(filePath), encryptedContent);

            // Hash password
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Save document to database
            Document document = new Document();
            document.setName(fileName);
            document.setFilePath(filePath);
            document.setPasswordHash(hashedPassword);
            document.setAction("Upload");
            document.setCreatedAt(LocalDateTime.now());

            document.setUser(user);
            documentRepository.save(document);
        }

        // Registrar la acción del usuario (esto puede ser modificado según tu lógica)

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
    public byte[] download(String documentId, String password) {
        // Obtener el usuario autenticado
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        User currentUser = userRepository.findByUsername(username);

        if (currentUser != null) {
            // Buscar el documento por ID
            Document document = documentRepository.findByName(documentId);

            if (document != null && document.getUser().getUsername().equals(currentUser.getUsername())) {
                // Verificar si la contraseña proporcionada coincide con el hash almacenado
                if (BCrypt.checkpw(password, document.getPasswordHash())) {
                    // Leer y desencriptar el archivo del disco
                    byte[] encryptedContent = new byte[0];
                    try {
                        encryptedContent = Files.readAllBytes(Paths.get(document.getFilePath()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        return Utils.decryptFile(encryptedContent, password);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        throw new Exception("Invalid password.");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                try {
                    throw new Exception("Unauthorized or document not found.");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            try {
                throw new Exception("Unauthorized user.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
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

    public Page<Document> filterBy(LocalDateTime startDate, LocalDateTime endDate, Long userId, String action, Pageable pageable) {
        return documentRepository.findByFilters(startDate, endDate, userId, action, pageable);
    }

    public List<Document> getDocumentsByBusinessId(Long businessId) {
        return documentRepository.findByBusinessId(businessId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

}

