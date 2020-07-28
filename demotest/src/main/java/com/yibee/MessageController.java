package com.yibee;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/msg")
//@Lazy
public class MessageController {
	@Resource
	private MessageRepository repo;
	
	@GetMapping("/getNewMsgCount")
	@CrossOrigin(origins = "*", maxAge = 3600)
	 public int getMemberById(@RequestParam("toId") Long toId) {
		  return repo.getNewMsgByToId(toId);
	}	

}
