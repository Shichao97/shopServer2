package com.yibee;

import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yibee.entity.Member;

@RestController
@RequestMapping("/order")
public class OrderController {
	@Resource
	private OrderRepository repo;
	
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/placeorder")
	public Properties placeOrder(HttpServletRequest request) {
		Properties p = new Properties();
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		
	}
}
