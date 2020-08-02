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

import org.springframework.beans.factory.annotation.Autowired;
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
import com.yibee.entity.GoodsWithMember;
import com.yibee.entity.Member;
import com.yibee.websocket.MyWebSocketHandler;

@RestController
@RequestMapping("/collect")
public class CollectController {
	 @Autowired
	 private MyWebSocketHandler webSocketHander;
	 @Resource
	 private CollectRepository repo;
	 @Resource
	 private GoodsRepository goodsRepo;
	 
	 	 
	 /**
	  * When user click the collect button goods,this function is invoked
	  * 
	  * @param request
	  * @param goodsId
	  * @param memberId
	  * @return if the click operation is success and some info message
	  */
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
		         Optional<Goods> og = goodsRepo.findById(goodsId);
		         if(og.isPresent() && og.get().getStatus()==Goods.STATUS_SELLING_NOW)
		        	 webSocketHander.sendSysMessage(og.get().getSellerId(), "Your goods '"+og.get().getName()+"' is disliked by "+m.getUserName());
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
		         Optional<Goods> og = goodsRepo.findById(goodsId);
		         if(og.isPresent() && og.get().getStatus()==Goods.STATUS_SELLING_NOW)
		        	 webSocketHander.sendSysMessage(og.get().getSellerId(), "Your goods '"+og.get().getName()+"' is liked by "+m.getUserName());

				 return p;
			 }catch(Exception e){
				 e.printStackTrace();
				 p.put("success", 0);
				 p.put("msg","Add collection failed!");
				 return p;
			 }
		 }
		 
		 
	 }
	 
	 /**
	  * get the collection relationship for a certain user and goods
	  * 
	  * @param request
	  * @param goodsId
	  * @param memberId
	  * @return if the get operation is success and some info msg
	  */
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
	 
	 /**
	  * search for all the collection of a certain user
	  * 
	  * @param request
	  * @param pageNo
	  * @param pageSize
	  * @param searchValue
	  * @param sortBy
	  * @return page with goods and member info for display
	  */
	 @CrossOrigin(origins = "*", maxAge = 3600)
	 @GetMapping(value="/edit/searchCollect")
	 public Page<GoodsWithMember> searchCollect(HttpServletRequest request,
			@RequestParam(value="pageNo",defaultValue="0") Integer pageNo,
			@RequestParam(value="pageSize",defaultValue="8") Integer pageSize,
			@RequestParam(value="searchValue",defaultValue="") String searchValue,
			@RequestParam(value="sortBy",defaultValue="") String sortBy){
		 	
		 	HttpSession session = request.getSession();
		 	
		 	Member m = (Member)session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
			Long uid = m.getId();
		 	Page<GoodsWithMember> page = null;
			Pageable pageable = null;
			
			if(sortBy.length() == 0) {
				pageable = PageRequest.of(pageNo, pageSize);
			}else {
				pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
			}
			if(searchValue.length()==0) page = repo.findGMByUID(m.getId(), pageable);
			else page = repo.findGMByUIDAndGoodsName(m.getId(),"%"+searchValue+"%", pageable);
							
			return page;
	 }
	 
//	 @CrossOrigin(origins = "*", maxAge = 3600)
//	 @GetMapping(value="/getcollecticon")
//	 public ResponseEntity<FileSystemResource> getCollectIcon(HttpServletResponse response,@RequestParam("goodsId") Long goodsId,@RequestParam("memberId") Long memberId) {
//	  Properties pp;
//	  try {
//	   File file;
//	   pp = MyUtil.getConfProperties();
//	   String savePath = pp.getProperty("collect_icon.dir");
//	   
//	   Optional<Collect> oc = repo.findByGoodsidAndMemberid(goodsId, memberId);
//	   if(!oc.isPresent()) {
//	    
//	    String absolutePath = savePath+"/purple_heart.png";
//	    file = new File(absolutePath);
//	   }else {
//	    String absolutePath = savePath+"/red_heart.png";
//	    file = new File(absolutePath);
//	   }
//	   
//	   HttpHeaders headers = new HttpHeaders();
//	      headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//	      headers.add("Pragma", "no-cache");
//	      headers.add("Expires", "0");
//	      headers.add("Last-Modified", new Date().toString()); 
//	      headers.add("ETag", String.valueOf(System.currentTimeMillis()));
//	      return ResponseEntity
//	        .ok()
//	        .headers(headers)
//	        .contentLength(file.length())
//	        .contentType(MediaType.IMAGE_JPEG)
//	        .body(new FileSystemResource(file));
//	
//	  } catch (IOException e) {
//	   // TODO Auto-generated catch block
//	   e.printStackTrace();
//	  }
//	  return ResponseEntity.status(602).build();
//	 }

 
}