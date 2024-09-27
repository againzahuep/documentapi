package com.example.documentapi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Serializable {

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Setter
    @Getter
    private Set<Document> documents;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="user_roles", joinColumns= @JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"),
            uniqueConstraints= {@UniqueConstraint(columnNames= {"user_id", "role_id"})})
    @Setter
    @Getter
    private List<Role> roles;


}
