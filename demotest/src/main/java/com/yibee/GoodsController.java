package com.yibee;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yibee.entity.Goods;
import com.yibee.entity.Member;

@RestController
@RequestMapping("/goods")
public class GoodsController {
	@Resource
	private GoodsRepository repo;
	
	@PostMapping(value = "/add2")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public Goods addGoods(HttpServletRequest request) {
		Map<String,String[]> map = request.getParameterMap();
		Set<String> set = map.keySet();
		for(Object o : set) {
			System.out.println(o.toString());
		}
		return null;
	}

	
	@PostMapping(value = "/add")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public Goods addGoods(@RequestParam("typeCode") String typeCode, @RequestParam("location") String location,@RequestParam("name") String name,@RequestParam("price") float price, @RequestParam("sellingMethod") byte sellingMethod,@RequestParam("status") int status) {
		Goods g = new Goods();
		g.setSellerId(1005L);
		g.setTypeCode(typeCode);
		g.setLocation(location);
		g.setName(name);
		g.setPrice(price);
		g.setSellingMethod(sellingMethod);
		g.setStatus(status);
		
		Goods g2 = repo.save(g);
		return g2;
		
	}
}
