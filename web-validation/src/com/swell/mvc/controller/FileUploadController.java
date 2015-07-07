package com.swell.mvc.controller;

import java.io.File;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {
	private String dir = System.getProperty("java.io.tmpdir");

	@RequestMapping(value = "/fileuploadView")
	public String view() {
		return "fileupload";
	}

	@RequestMapping(value = "/fileupload", method = RequestMethod.POST)
	public String handleFormUpload(@RequestParam("name") String name,
			@RequestParam("file") MultipartFile file) throws Exception {
		if (!file.isEmpty()) {

			String originalFilename = file.getOriginalFilename();
			file.transferTo(new File(dir + originalFilename));

			// byte[] bytes = file.getBytes();
			// store the bytes somewhere
			return "uploadSuccess";
		} else {
			return "uploadFailure";
		}
	}
}