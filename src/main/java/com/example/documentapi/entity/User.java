package com.example.documentapi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-08-29T12:04:37.072+02:00")
public class User implements Serializable, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    private Long id;

    @Column(length = 40)
    @Setter
    @Getter
    private String username;

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


    @ManyToOne
    @JoinColumn(name = "business_id")
    @Setter
    @Getter
    private Business business;

    @Setter
    @Getter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Document> documents;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="user_roles", joinColumns= @JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"),
            uniqueConstraints= {@UniqueConstraint(columnNames= {"user_id", "role_id"})})
    @Setter
    @Getter
    private List<Role> roles;


    @Override
    public String getName() {
        return username;
    }
}
