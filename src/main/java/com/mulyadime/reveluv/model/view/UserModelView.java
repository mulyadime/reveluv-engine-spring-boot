package com.mulyadime.reveluv.model.view;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UserModelView {
	
	private Long id;
	
	private String userid;
	
	@NotEmpty(message = "*Please provide your name")
	private String name;
	
	private String password;

}
