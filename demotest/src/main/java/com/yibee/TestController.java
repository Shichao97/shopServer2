package com.yibee;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/audio")
	public void  getMp3(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		

		request.getRequestDispatcher("/audio/1.mp3").forward(request, response);
		

	}
}
