package com.yibee;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goodstype")
public class GoodstypeController {
	
	@Resource
	private GoodstypeRepository repo;
	
	/*
	@RequestMapping(value = "/showAll")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public List showAll(){
		List list = repo.findAllOrderByCategory();
		return list;
	}
	*/
}
