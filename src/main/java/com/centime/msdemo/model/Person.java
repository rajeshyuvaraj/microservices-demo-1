package com.centime.msdemo.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class Person {

	@NotBlank(message = "The name is required.")
	@Pattern(regexp = "[a-zA-Z]+", message = "name should contain only letters")
	String name;

	@Pattern(regexp = "[a-zA-Z]+", message = "surname should contain only letters")
	@NotBlank(message = "The surname is required.")
	String surname;

}
