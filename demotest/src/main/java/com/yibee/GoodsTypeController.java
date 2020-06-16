package com.yibee;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yibee.entity.GoodsType;

@RestController
@RequestMapping("/goodstype")
public class GoodsTypeController {
	
	@Resource
	private GoodsTypeRepository repo;
	 
	@RequestMapping(value = "/showAll")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public List showAll(){
	List list = repo.findAllOrderByCategory();
	  return list;
	}

}
