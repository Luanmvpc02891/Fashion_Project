package com.poly.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Users {
	@Id
	@NotBlank(message = "{NotBlank.user.username}")
	private String username;
	@NotBlank(message = "{NotBlank.user.email}")
	@Email(message = "{Email.user.email}")
	private String email;
	@NotBlank(message = "{NotBlank.user.fullname}")
	private String fullname;
	@NotBlank(message = "{NotBlank.user.password}")
	private String password;

}