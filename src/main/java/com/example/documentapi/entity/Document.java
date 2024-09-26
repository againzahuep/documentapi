package com.example.documentapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
public class Document implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Setter
    @Getter
    @Column(unique = true)
    private String name;

    @Setter
    @Getter
    private String action; // uploaded, deleted, downloaded

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Setter
    @Getter
    private User user;

    @Setter
    @Getter
    private LocalDateTime createdAt;

    @Setter
    @Getter
    private String filePath;

    @Setter
    @Getter
    private String fileExtension;



    private static final long serialVersionUID = 1L;


}
