package com.yibee;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Collect")
public class CollectController {
	CollectRepository repo;
}
