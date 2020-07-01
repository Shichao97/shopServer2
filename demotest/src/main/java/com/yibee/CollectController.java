package com.yibee;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.Resource;
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

import com.yibee.entity.Collect;
import com.yibee.entity.Goods;

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
}