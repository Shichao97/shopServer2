package com.yibee;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yibee.entity.Goods;
import com.yibee.entity.GoodsWithMember;
import com.yibee.entity.GoodsWithOrder;
import com.yibee.entity.Member;
import com.yibee.entity.Order;

@RestController
@RequestMapping("/goods")
public class GoodsController {
	@Resource
	private GoodsRepository repo;
	@Resource
	private OrderRepository orderRepo;
	
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
	
	
	/**
	 * get goods info by goods id
	 * 
	 * @param Id
	 * @return Optional<Goods>
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/getgoodsinfo")
	public Optional<Goods> getGoodsById(@RequestParam("Id") Long Id){ //selling now
		//Goods g = repo.findGoodsById(Id);
		Optional<Goods> og = repo.findById(Id);
		return og;
//		if(og.isPresent()) {
//			Goods g = og.get();
//			return g;
//		}else {
//			return null;
//		}
	}
	
	/**
	 * get big img for certain goods to display
	 * 
	 * @param response
	 * @param Id
	 * @param fname
	 * @return goods big img source
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/getGoodsBigImg")
	public ResponseEntity<FileSystemResource> getGoodsBigImg(HttpServletResponse response,@RequestParam("Id") Long Id,@RequestParam("fname") String fname) {
		Properties pp;
		try {
			pp = MyUtil.getConfProperties();
			String savePath = pp.getProperty("goods_main_img.dir");
			int folderName = (int)Math.floor(Id/1000);
			String save2Path = savePath + ("/"+folderName)+ "/" + Id;
			Goods g = repo.findGoodsById(Id);
//			String totalFile = g.getFilename();
//			//String total = "3";
//			String[] array = totalFile.split(";");
//			String firstNo = array[0];
			String absolutePath = save2Path+"/"+fname+".jpg";
			File file = new File(absolutePath);
			//默认商品图？
			/*
			if(!file.exists()) {
				file = new File(savePath+"/default_0.jpg");
			}
			*/
			HttpHeaders headers = new HttpHeaders();
		    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		   //浏览器自动下载
		   //headers.add("Content-Disposition", "attachment; filename=ttt.jpg");
		   //缓存
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
	@GetMapping(value="/getgoodsimg")
	public ResponseEntity<FileSystemResource> getGoodsImg(HttpServletResponse response,@RequestParam("Id") Long Id,@RequestParam("fname") String fname) {
		//String savePath = "/Users/liushichao/desktop/member_icon";
		Properties pp;
		try {
			pp = MyUtil.getConfProperties();
			String savePath = pp.getProperty("goods_main_img.dir");
			int folderName = (int)Math.floor(Id/1000);
			String save2Path = savePath + ("/"+folderName)+ "/" + Id;
			Goods g = repo.findGoodsById(Id);
//			String totalFile = g.getFilename();
//			//String total = "3";
//			String[] array = totalFile.split(";");
//			String firstNo = array[0];
			String absolutePath = save2Path+"/"+fname+"_l.jpg";
			File file = new File(absolutePath);
			//默认商品图？
			/*
			if(!file.exists()) {
				file = new File(savePath+"/default_0.jpg");
			}
			*/
			HttpHeaders headers = new HttpHeaders();
		    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		   //浏览器自动下载
		   //headers.add("Content-Disposition", "attachment; filename=ttt.jpg");
		   //缓存
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
	@GetMapping(value="/getgoodsmainimg")
	public ResponseEntity<FileSystemResource> getGoodsMainImg(HttpServletResponse response,@RequestParam("Id") Long Id) {
		//String savePath = "/Users/liushichao/desktop/member_icon";
		Properties pp;
		try {
			pp = MyUtil.getConfProperties();
			String savePath = pp.getProperty("goods_main_img.dir");
			int folderName = (int)Math.floor(Id/1000);
			String save2Path = savePath + ("/"+folderName)+ "/" + Id;
			Goods g = repo.findGoodsById(Id);
			String totalFile = g.getImgNames();
			//String total = "3";
			String[] array = totalFile.split(";");
			String firstNo = array[0];
			String absolutePath = save2Path+"/"+firstNo+"_l.jpg";
			File file = new File(absolutePath);
			//默认商品图？
			/*
			if(!file.exists()) {
				file = new File(savePath+"/default_0.jpg");
			}
			*/
			HttpHeaders headers = new HttpHeaders();
		    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		   //浏览器自动下载
		   //headers.add("Content-Disposition", "attachment; filename=ttt.jpg");
		   //缓存
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
	
	/**
	 * put goods on the shelf
	 * 
	 * @param request
	 * @param gid
	 * @return
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/sell/putonshelf")
	public Properties putOnShelf(HttpServletRequest request,@RequestParam("gid") Long gid) {
		Properties p = new Properties();
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		
		Optional<Goods> og = repo.findById(gid);
		if(!og.isPresent()) {
			p.put("success",0);
			p.put("msg", "Wrong goods_id for matching!");
			return p;
		}
        
		Goods g = og.get();
		if(!m.getId().equals(g.getSellerId())) {
			p.put("success",0);
			p.put("msg", "You do not have previllege to do so!");
			return p;
		}
		
		g.setStatus(1);
		
		repo.save(g);
		p.put("success",1);
		p.put("msg","Put on shelf success!");
		return p;
		
	}
	

	/**
	 * remove goods from shelf
	 * 
	 * @param request
	 * @param gid
	 * @return
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/sell/removefromshelf")
	public Properties removeFromShelf(HttpServletRequest request,@RequestParam("gid") Long gid) {
		Properties p = new Properties();
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		
		Optional<Goods> og = repo.findById(gid);
		if(!og.isPresent()) {
			p.put("success",0);
			p.put("msg", "Wrong goods_id for matching!");
			return p;
		}
        
		Goods g = og.get();
		if(!m.getId().equals(g.getSellerId())) {
			p.put("success",0);
			p.put("msg", "You do not have previllege to do so!");
			return p;
		}
		
		g.setStatus(0);
		
		repo.save(g);
		p.put("success",1);
		p.put("msg","Remove success!");
		return p;
		
	}
	/*
	 * buyer search
	 */
//	@GetMapping(value = "search")
//	@CrossOrigin(origins = "*", maxAge = 3600)
//	public Page<Goods> goodsSearch(@RequestParam("searchType") String searchType,
//			@RequestParam(value="searchValue",defaultValue="") String searchValue,
//			@RequestParam(value="pageNo",defaultValue="0") Integer pageNo,
//			@RequestParam(value="pageSize",defaultValue="8") Integer pageSize,
//			@RequestParam(value="sortBy",defaultValue="") String sortBy){
//		
//		Page<Goods> page = null;
//		
//		Pageable pageable = null;
//		if(sortBy.length() == 0) {
//			pageable = PageRequest.of(pageNo, pageSize);
//		}else {
//			pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
//		}
//		
//		if(searchType.contentEquals("name")){
//			page = repo.findByStatusAndName("%"+searchValue+"%",pageable); //未发布
//		}else{
//			page = repo.findByStatusAndDesc("%"+searchValue+"%",pageable); 
//		}
//						
//		return page;
//		
//	}
	
	
	/**
	 * Search function for buyer
	 * 
	 * @param searchValue
	 * @param pageNo
	 * @param schoolCode
	 * @param sellerId
	 * @param pageSize
	 * @param sortBy
	 * @return goods with info of seller
	 */
	@GetMapping(value = "search2")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public Page<GoodsWithMember> goodsSearch2(@RequestParam(value="searchValue",defaultValue="") String searchValue,
			@RequestParam(value="pageNo",defaultValue="1") Integer pageNo,
			@RequestParam(value="schoolCode",defaultValue="") String schoolCode,
			@RequestParam(value="sellerId",defaultValue="0") Long sellerId,
			@RequestParam(value="pageSize",defaultValue="8") Integer pageSize,@RequestParam(value="sortBy",defaultValue="") String sortBy){
		
		Page<GoodsWithMember> page = null;
		int pnum = pageNo-1;
		Pageable pageable = null;
		if(sortBy.length() == 0) {
			pageable = PageRequest.of(pnum, pageSize);
		}else {
			pageable = PageRequest.of(pnum, pageSize, Sort.by(sortBy));
		}
		
		if(schoolCode.length()==0 && sellerId.longValue()==0L) {
			page = repo.findSellingGMByName("%"+searchValue+"%",pageable); //未发布
		}
		else if(sellerId.longValue() == 0L) {
			page = repo.findSellingGMByNameAndSchool("%"+searchValue+"%", schoolCode,pageable); 
		}
		else {
			page = repo.findSellingGMBySellerAndName(sellerId, "%"+searchValue, pageable);
		}
						
		return page;
		
	}	
	
	
	@GetMapping(value = "sell/getSellCount")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public Properties getSellCount(HttpServletRequest request)
	{
		Properties p = new Properties();
		HttpSession session = request.getSession();
		Member m =(Member)session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Long sellerId = m.getId();
		int n1 = repo.getSellingCount(sellerId);
		int n2 = repo.getOnTheWayCount(sellerId);
		p.put("sellingCount",n1);
		p.put("onTheWayCount",n2);
		return p;
	}
	
	/**
	 * seller search for goods
	 * 
	 * @param request
	 * @param searchType
	 * @param pageNo
	 * @param pageSize
	 * @param sortBy
	 * @return
	 */
	@GetMapping(value = "sell/search")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public Page<GoodsWithOrder> goodsSelect(HttpServletRequest request,
			@RequestParam("searchType") String searchType,
			@RequestParam(value="pageNo",defaultValue="1") Integer pageNo,
			@RequestParam(value="pageSize",defaultValue="10") Integer pageSize,
			@RequestParam(value="sortBy",defaultValue="") String sortBy)
	{
			
		Page<GoodsWithOrder> page = null;
		HttpSession session = request.getSession();
		Member m =(Member)session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Long sellerId = m.getId();
		int pnum = pageNo-1;
		Pageable pageable = null;
		if(sortBy.length() == 0) {
			pageable = PageRequest.of(pnum, pageSize);
		}else {
			pageable = PageRequest.of(pnum, pageSize, Sort.by(sortBy));
		}
		
		if(searchType.contentEquals("1")){
			page = repo.findSellingNow(sellerId,pageable); 
		}else if(searchType.contentEquals("2")){
			page = repo.findOnTheWay(sellerId,pageable); 
		}else if(searchType.contentEquals("3")) {
			page = repo.findSold(sellerId,pageable); 
		}else if(searchType.contentEquals("4")) {
			page = repo.findRemoveOff(sellerId,pageable); 
		}
						
		return page;
		
	}	
	
	
	public int getMaxId(String str) {
		if(str == null || str.length() == 0) {
			return -1;
		}
		String array1[]= str.split(";");
		int maxId = Integer.parseInt(array1[0]);
		if(array1.length == 1) return maxId;
		for (int i=1; i<array1.length; i++) {
			maxId = Math.max(maxId, Integer.parseInt(array1[i]));
		}
		return maxId;
	}
	
	/**
	 * seller edit goods info
	 * 
	 * @param request
	 * @param gid
	 * @param typeCode
	 * @param location
	 * @param description
	 * @param name
	 * @param price
	 * @param method1
	 * @param method2
	 * @param method3
	 * @param oldimgnames
	 * @return
	 */
	
	@PostMapping(value = "/sell/edit")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public Properties editGoods(HttpServletRequest request,
			@RequestParam("gid") Long gid,
			@RequestParam(value = "typeCode",defaultValue = "A0001") String typeCode, 
			@RequestParam(value = "location",defaultValue = "") String location,
			@RequestParam("description") String description,
			@RequestParam("name") String name,
			@RequestParam("price") float price, 
			@RequestParam(value = "method1",defaultValue = "0") byte method1,
			@RequestParam(value = "method2",defaultValue = "0") byte method2,
			@RequestParam(value = "method3",defaultValue = "0") byte method3,
			@RequestParam(value = "oldimgnames") String oldimgnames
			){
		Properties p = new Properties();
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		
		Optional<Goods> og = repo.findById(gid);
		if(!og.isPresent()) {
			p.put("success",0);
			p.put("msg", "Wrong goods_id for matching!");
			return p;
		}
        
		Goods g = og.get();
		if(!m.getId().equals(g.getSellerId())) {
			p.put("success",0);
			p.put("msg", "You do not have previllege to do so!");
			return p;
		}
		//g.setSellerId(m.getId());
		g.setTypeCode(typeCode);
		g.setLocation(location);
		g.setDescription(description);
		g.setName(name);
		g.setPrice(price);
		byte sellingMethod =(byte) (method1 | method2 | method3) ;
		g.setSellingMethod(sellingMethod);
		
		//old images
		//String oldDatabaseNames = g.getImgNames();
		int maxImgId;
		maxImgId = getMaxId(oldimgnames);
		//Part part;
		Properties pp;
		try {
			Collection<Part> parts = request.getParts();
			String fileName = oldimgnames;
			
			int imgId,newId;
			for(Part part : parts) {
				if( part.getContentType() != null && part.getContentType().startsWith("image/")){
					String fieldName = part.getName();
					imgId = Integer.parseInt(fieldName.substring(3,fieldName.length()));
					newId = imgId + maxImgId + 1;
					if(fileName == null || fileName.length() == 0) {
						fileName += newId;
					}else {
						fileName += (";"+newId);
					}
					
					part = request.getPart(fieldName);
					pp = MyUtil.getConfProperties();
					String savePath = pp.getProperty("goods_main_img.dir");
					int folderName = (int)Math.floor(gid/1000);
					String save2Path = savePath + ("/"+folderName) + "/" + gid;
					File fileSaveDir = new File(save2Path);
			        if (!fileSaveDir.exists()) {
			            fileSaveDir.mkdir();
			        }
			        part.write(save2Path + File.separator + newId +".jpg");
			        MyUtil.resizeImage(240,save2Path + File.separator + newId+".jpg",save2Path + File.separator + newId+"_l.jpg");
			       
				}
			}
			
			//fileName = fileName.substring(0, fileName.length()-1);
			//String imgnames = fileName.length() == 0? oldimgnames : oldimgnames+";"+fileName;
			
			g.setImgNames(fileName);
			repo.save(g);
			p.put("success",1);
			p.put("msg","Edit success!");
		}catch(Exception e1) {
			e1.printStackTrace();
		}
		return p;
	}
	

	/**
	 * add goods
	 * 
	 * @param request
	 * @param typeCode
	 * @param location
	 * @param description
	 * @param name
	 * @param price
	 * @param method1
	 * @param method2
	 * @param method3
	 * @param status
	 * @return
	 */
	@PostMapping(value = "/sell/add")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public Optional<Goods> addGoods(HttpServletRequest request,
			@RequestParam(value = "typeCode",defaultValue = "A0001") String typeCode, 
			@RequestParam(value = "location",defaultValue = "") String location,
			@RequestParam("description") String description,
			@RequestParam("name") String name,
			@RequestParam("price") float price, 
			@RequestParam(value = "method1",defaultValue = "0") byte method1,
			@RequestParam(value = "method2",defaultValue = "0") byte method2,
			@RequestParam(value = "method3",defaultValue = "0") byte method3,
			@RequestParam(value = "status",defaultValue = "1") int status
			) {
		
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		
		/*
		Enumeration<String> e = request.getParameterNames();
		while(e.hasMoreElements()){
            String value = (String)e.nextElement();//调用nextElement方法获得元素
            System.out.print(value);
        }
        */
		
		
		Long gid = repo.getMaxId();
		
		Long id = gid==null?1L:gid + 1;
		
		Goods g = new Goods();
		Date addTime = new Date();
		g.setSellerId(m.getId());
		g.setTypeCode(typeCode);
		g.setLocation(location);
		g.setName(name);
		g.setDescription(description);
		g.setPrice(price);
		byte sellingMethod =(byte) (method1 | method2 | method3) ;
		g.setSellingMethod(sellingMethod);
		g.setStatus(status);
		g.setAddTime(addTime);
		
		
		//Part part;
		Properties pp;
		try {
			Collection<Part> parts = request.getParts();
			String fileName = "";
			for(Part part : parts) {
				if( part.getName().startsWith("img")){
					String fieldName = part.getName();
					String imgId = fieldName.substring(3,fieldName.length());
					fileName += (imgId+";");
					part = request.getPart(fieldName);
					pp = MyUtil.getConfProperties();
					String savePath = pp.getProperty("goods_main_img.dir");
					int folderName = (int)Math.floor(id/1000);
					String save2Path = savePath + ("/"+folderName);
					File fileSaveDir = new File(save2Path);
			        if (!fileSaveDir.exists()) {
			            fileSaveDir.mkdir();
			        }
					save2Path += "/" + id;
					fileSaveDir = new File(save2Path);
			        if (!fileSaveDir.exists()) {
			            fileSaveDir.mkdir();
			        }
			        part.write(save2Path + File.separator + imgId +".jpg");
			        MyUtil.resizeImage(240,save2Path + File.separator + imgId+".jpg",save2Path + File.separator + imgId+"_l.jpg");
				}
			}
			
			fileName = fileName.substring(0, fileName.length()-1);
			g.setImgNames(fileName);
			
			Goods g2 = repo.save(g);
			return Optional.of(g2);
			
		}catch(Exception e1) {
			e1.printStackTrace();
			return Optional.empty();
		}
			
		
	}
	
	@GetMapping(value = "/getGMById")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public Optional findGMById(HttpServletRequest request,@RequestParam("id") long id)
	{
		Optional<GoodsWithMember> op = repo.findGMById(id);
		if(op.isPresent()) {
			HttpSession session = request.getSession();
			GoodsWithMember gm = op.get();
			if(gm.getG().getStatus() == Goods.STATUS_SELLING_NOW) {
				return op;
			}
			Member m = (Member)session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
			if(m == null) {
				return Optional.empty();
			}
			else if(m.getId().longValue() != gm.getG().getSellerId().longValue()) {
				int n = orderRepo.getCountByGoodsIdAndBuyerId(id,m.getId());
				if(n==0) return Optional.empty();
			}
		}
		return op;
	}
}
