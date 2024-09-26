package com.example.documentapi.dto;

public class DocumentDTO {

    String name;

    public DocumentDTO() {
    }

    public DocumentDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
