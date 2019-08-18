package com.mulyadime.reveluv.service;

import java.util.HashMap;

import com.mulyadime.reveluv.service.UserService;

public class ServiceLocator {
	
	public static void resetParameterValue(HashMap<String, String> params) {
		params.clear();
		
	}
	
	public static UserService getUserService() {
		return new UserService();
	}

}
