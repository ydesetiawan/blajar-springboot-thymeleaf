package com.ydesetiawan.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public ModelAndView defaultPage() {

		ModelAndView model = new ModelAndView();
		model.setViewName("home");
		return model;

	}

	@RequestMapping(value = { "/hello" }, method = RequestMethod.GET)
	public ModelAndView helloPage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Boot + Thymeleaf Example");
		model.addObject("message", "This is default page!");
		model.setViewName("hello");
		return model;

	}

}
