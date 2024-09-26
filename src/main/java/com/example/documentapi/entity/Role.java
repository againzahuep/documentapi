package com.example.documentapi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name="roles")
public class Role implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter
	private Long id;
	
	@Column(unique=true, length=20)
	@Setter
	@Getter
	private String name;

	private static final long serialVersionUID = 1L;
}
