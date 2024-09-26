package com.example.documentapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 40)
    private String name;

    @Setter
    @Getter
    private String password;

    @Setter
    @Getter
    @Column(unique = true, length = 40)
    private String email;

    @Setter
    @Getter
    @Column(unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Action action;

    @Setter
    @Getter
    private LocalDateTime actionDate;

    @ManyToOne
    @JoinColumn(name = "business_id")
    @Setter
    @Getter
    private Business business;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Setter
    @Getter
    private Set<Document> documents;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="user_roles", joinColumns= @JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"),
            uniqueConstraints= {@UniqueConstraint(columnNames= {"user_id", "role_id"})})
    private List<Role> roles;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public LocalDateTime getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDateTime actionDate) {
        this.actionDate = actionDate;
    }
}
