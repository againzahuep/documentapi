
import com.example.documentapi.dao.IDocumentDao;
import com.example.documentapi.entity.Action;
import com.example.documentapi.entity.Document;
import com.example.documentapi.entity.User;
import com.example.documentapi.service.IDocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {

    @Mock
    @Autowired
    private IDocumentDao documentDao;

    @InjectMocks
    @Autowired
    private IDocumentService documentService;

    @BeforeEach
    public void setUp() {
        // Configura cualquier inicialización necesaria antes de cada prueba
    }

    @Test
    public void testFindByUserIdAndTimestampBetween() {
        // Dados
        Long userId = 1L;
        LocalDateTime start = LocalDateTime.now().minusDays(10);
        LocalDateTime end = LocalDateTime.now();

        List<Document> documents = new ArrayList<>();
        Document doc1 = new Document();
        doc1.setId(1L);
        doc1.setName("doc1.pdf");
        doc1.setCreatedAt(LocalDateTime.now().minusDays(5));

        User user = new User();
        user.setId(1L);
        user.setName("Joe");
        doc1.setUser(user);



        documents.add(doc1);


        // Simulación del comportamiento del repositorio
        when(documentDao.findByFilters(start, end, user.getId(), String.valueOf(Action.UPLOADED), Pageable.ofSize(1))).thenReturn((Page<Document>) documents);

        // Cuando se llama al método
        Page<Document> result = documentService.getDocuments(start, end, user.getId(), String.valueOf(Action.UPLOADED), Pageable.ofSize(1));

        // Verificaciones
        assertThat(result).isNotNull();
        assertThat(result.getContent().size()).isEqualTo(1); // Deben haber dos documentos
        assertThat(result).contains(doc1); // Deben ser los documentos esperados
    }
}
