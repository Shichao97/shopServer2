package com.yibee;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yibee.entity.Collect;
import com.yibee.entity.CollectWithGoodsAndMember;
import com.yibee.entity.Goods;
import com.yibee.entity.Member;

@RestController
@RequestMapping("/collect")
public class CollectController {
	
	 @Resource
	 private CollectRepository repo;
 
	 @CrossOrigin(origins = "*", maxAge = 3600)
	 @GetMapping(value="/getcollecticon")
	 public ResponseEntity<FileSystemResource> getCollectIcon(HttpServletResponse response,@RequestParam("goodsId") Long goodsId,@RequestParam("memberId") Long memberId) {
	  Properties pp;
	  try {
	   File file;
	   pp = MyUtil.getConfProperties();
	   String savePath = pp.getProperty("collect_icon.dir");
	   
	   Optional<Collect> oc = repo.findByGoodsidAndMemberid(goodsId, memberId);
	   if(!oc.isPresent()) {
	    
	    String absolutePath = savePath+"/purple_heart.png";
	    file = new File(absolutePath);
	   }else {
	    String absolutePath = savePath+"/red_heart.png";
	    file = new File(absolutePath);
	   }
	   
	   HttpHeaders headers = new HttpHeaders();
	      headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	      headers.add("Pragma", "no-cache");
	      headers.add("Expires", "0");
	      headers.add("Last-Modified", new Date().toString()); 
	      headers.add("ETag", String.valueOf(System.currentTimeMillis()));
	      return ResponseEntity
	        .ok()
	        .headers(headers)
	        .contentLength(file.length())
	        .contentType(MediaType.IMAGE_JPEG)
	        .body(new FileSystemResource(file));
	
	  } catch (IOException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	  return ResponseEntity.status(602).build();
	 }
	 
	 @CrossOrigin(origins = "*", maxAge = 3600)
	 @GetMapping(value="/edit/clickcollect")
	 public Properties clickCollect(HttpServletRequest request,@RequestParam("goodsId") Long goodsId,
			 @RequestParam("memberId") Long memberId) {
		 Properties p = new Properties();
		 HttpSession session = request.getSession();
		 Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		 Member m =(Member)o;
		 System.out.println(m.getId());
		 System.out.println(memberId);
		 if(m.getId()==null || !m.getId().equals(memberId)) {
			 p.put("success", 0);
			 p.put("msg", "You do not have previllege to do so!");
			 return p;
		 }
		 Optional<Collect> oc = repo.findByGoodsidAndMemberid(goodsId, memberId);
		 if(oc.isPresent()) {
			Collect c = oc.get();
			try {
				repo.deleteById(c.getId());
				p.put("success", 1);
				p.put("like",false);
				return p;
			}catch(Exception e){
				e.printStackTrace();
				p.put("success", 0);
				p.put("msg",e.getMessage());
				return p;
			}
		    
		    
		 }else {
			 Collect c = new Collect();
			 c.setId(0L);
			 c.setGoodsId(goodsId);
			 c.setMemberId(memberId);
			 try{
				 repo.save(c);
				 p.put("success", 1);
				 p.put("like",true);
				 return p;
			 }catch(Exception e){
				 e.printStackTrace();
				 p.put("success", 0);
				 p.put("msg","Add collection failed!");
				 return p;
			 }
		 }
		 
		 
	 }
	 
	 @CrossOrigin(origins = "*", maxAge = 3600)
	 @GetMapping(value="/edit/getIsLike")
	 public Properties getIsLike(HttpServletRequest request,@RequestParam("goodsId") Long goodsId,
			 @RequestParam("memberId") Long memberId) {
		 Properties p = new Properties();
		 HttpSession session = request.getSession();
		 Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		 Member m =(Member)o;
		 System.out.println(m.getId());
		 System.out.println(memberId);
		 if(m.getId()==null || !m.getId().equals(memberId)) {
			 p.put("success", 0);
			 p.put("msg", "You do not have previllege to do so!");
			 return p;
		 }
		 Optional<Collect> oc = repo.findByGoodsidAndMemberid(goodsId, memberId);
		 if(oc.isPresent()) {
			Collect c = oc.get();
			try {
				//repo.deleteById(c.getId());
				p.put("success", 1);
				p.put("like",true);
				return p;
			}catch(Exception e){
				e.printStackTrace();
				p.put("success", 0);
				p.put("msg",e.getMessage());
				return p;
			}
		 }
		 else {
			 p.put("success", 1);
				p.put("like",false);
				return p;
		 }
		 
	 }
	 
	 @CrossOrigin(origins = "*", maxAge = 3600)
	 @GetMapping(value="/edit/searchCollect")
	 public Page<CollectWithGoodsAndMember> searchCollect(
			@RequestParam("uid") Long uid,
			@RequestParam(value="pageNo",defaultValue="0") Integer pageNo,
			@RequestParam(value="pageSize",defaultValue="8") Integer pageSize,
			@RequestParam(value="sortBy",defaultValue="") String sortBy){
		 	Page<CollectWithGoodsAndMember> page = null;
			
			Pageable pageable = null;
			if(sortBy.length() == 0) {
				pageable = PageRequest.of(pageNo, pageSize);
			}else {
				pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
			}
			page = repo.findCGMByUID(uid, pageable);
							
			return page;
	 }
 
}