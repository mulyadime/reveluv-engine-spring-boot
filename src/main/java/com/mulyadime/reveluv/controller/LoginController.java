package com.mulyadime.reveluv.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mulyadime.reveluv.configuration.CoreConstant;
import com.mulyadime.reveluv.model.User;
import com.mulyadime.reveluv.service.UserService;
import com.mulyadime.reveluv.utilities.AppUtil;
import com.mulyadime.reveluv.utilities.ErrorCode;

@Controller
public class LoginController {
	
	@Autowired
    private UserService userService;
	
	protected ModelAndView modelAndView = null;
	
	@RequestMapping(
			method = RequestMethod.GET
			, value = {CoreConstant.URI.HOME, CoreConstant.URI.LOGIN}
			)
	public ModelAndView login() {
		modelAndView = new ModelAndView();
		modelAndView.setViewName(CoreConstant.View.LOGIN);
		
		return modelAndView;
	}
	
	@RequestMapping(
			method = RequestMethod.GET
			, value = CoreConstant.URI.REGISTER
			)
	public ModelAndView register() {
		modelAndView = new ModelAndView();
		modelAndView.addObject("user", new User());
		modelAndView.setViewName(CoreConstant.View.REGISTER);
		
		return modelAndView;
	}
	
	@RequestMapping(
			method = RequestMethod.POST
			, value = CoreConstant.URI.REGISTER
			)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindResult) {
		modelAndView = new ModelAndView();
		int userExists = userService.findUserExists(user.getUserid());
        if (userExists > 0) {
        	bindResult.rejectValue(
            		"userid"
            		, "error.user"
            		, AppUtil.formatToString(ErrorCode.FIELD_ALREADY_EXISTS, "userid", user.getUserid())
    		);
        }
        
		if (!bindResult.hasErrors()) {
			userService.save(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
			
		}
		
		modelAndView.setViewName(CoreConstant.View.REGISTER);
		
		return modelAndView;
	}
	
	@RequestMapping(
			method = RequestMethod.GET
			, value = CoreConstant.URI.DASHBOARD + "/home"
			)
	public ModelAndView dashboard() {
		modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByUserid(auth.getName());
		modelAndView.addObject("userName", "Welcome " + user.getName() + " (" + user.getUserid() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
		modelAndView.setViewName(CoreConstant.View.DASHBOARD_HOME);
		
		return modelAndView;
	}
	
}
