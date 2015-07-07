package com.swell.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorTestController {

	public enum Code{
		a,b,c,d
		}
	
	@RequestMapping(value="/errorTest")
	public String errorTest(String errorCode) throws Exception{
		switch (Code.valueOf(errorCode)) {
		case a:
		case b:
		case d:
			throw new Exception(errorCode);
		default:
			break;
		}
		return null;
	}
}
