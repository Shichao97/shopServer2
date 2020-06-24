package com.yibee;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
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
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/sell/getgoodsimg")
	public ResponseEntity<FileSystemResource> getGoodsImg(HttpServletResponse response,@RequestParam("Id") int Id) {
		//String savePath = "/Users/liushichao/desktop/member_icon";
		Properties pp;
		try {
			pp = MyUtil.getConfProperties();
			String savePath = pp.getProperty("goods_main_img.dir");
			int folderName = (int)Math.floor(Id/1000);
			String save2Path = savePath + ("/"+folderName); 
			String absolutePath = save2Path+"/"+Id+"_0.jpg";
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
	
	@GetMapping(value = "sell/search")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public Page<Goods> goodsSelect(@RequestParam("sellerId") Long sellerId,@RequestParam("searchType1") int searchType1,@RequestParam("searchType2") String searchType2,@RequestParam(value="searchValue",defaultValue="") String searchValue,@RequestParam(value="pageNo",defaultValue="0") Integer pageNo,@RequestParam(value="pageSize",defaultValue="20") Integer pageSize,@RequestParam(value="sortBy",defaultValue="") String sortBy){
		//List<Teacher> list = null;
		
		
		Page<Goods> page = null;
		
		Pageable pageable = null;
		if(sortBy.length() == 0) {
			pageable = PageRequest.of(pageNo, pageSize);
		}else {
			pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		}
		
		if(searchType2.contentEquals("name")){
			page = repo.findBySelleridAndStatusAndName(sellerId,searchType1,"%"+searchValue+"%",pageable); //未发布
		}else{
			page = repo.findBySelleridAndStatusAndDesc(sellerId,searchType1,"%"+searchValue+"%",pageable); 
		}
						
		return page;
		
	}
	
	@PostMapping(value = "/sell/add")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public Goods addGoods(HttpServletRequest request,
			@RequestParam(value = "typeCode",defaultValue = "A0001") String typeCode, 
			@RequestParam("location") String location,
			@RequestParam("name") String name,
			@RequestParam("price") float price, 
			@RequestParam(value = "method1",defaultValue = "0") byte method1,
			@RequestParam(value = "method2",defaultValue = "0") byte method2,
			@RequestParam(value = "method3",defaultValue = "0") byte method3,
			@RequestParam(value = "status",defaultValue = "0") int status
			) {
		
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		
		Enumeration<String> e = request.getParameterNames();
		while(e.hasMoreElements()){
            String value = (String)e.nextElement();//调用nextElement方法获得元素
            System.out.print(value);
        }
        
		
		
		Long gid = repo.getMaxId();
		Long id = gid + 1;
		
		Goods g = new Goods();
		g.setSellerId(m.getId());
		g.setTypeCode(typeCode);
		g.setLocation(location);
		g.setName(name);
		g.setPrice(price);
		byte sellingMethod =(byte) (method1 | method2 | method3) ;
		g.setSellingMethod(sellingMethod);
		g.setStatus(status);
		
		
		Part part;
		Properties pp;
		try {
			Collection<Part> parts = request.getParts();
			for(Part p : parts) {
				Part ll = p;
			}
			part = request.getPart("mainImg");
			pp = MyUtil.getConfProperties();
			String savePath = pp.getProperty("goods_main_img.dir");
			int folderName = (int)Math.floor(id/1000);
			String save2Path = savePath + ("/"+folderName);
			File fileSaveDir = new File(save2Path);
	        if (!fileSaveDir.exists()) {
	            fileSaveDir.mkdir();
	        }
	        part.write(save2Path + File.separator + id+".jpg");
	        MyUtil.manageImage(240,save2Path + File.separator + id+".jpg",save2Path + File.separator + id+"_0.jpg");
		}catch(Exception e1) {
			e1.printStackTrace();
		}
			
		
		
		Goods g2 = repo.save(g);
		return g2;
		
		
	}
}
