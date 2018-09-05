package com.samule.example.elastic.controller;

import com.samule.example.elastic.pojo.MyDocument;
import com.samule.example.elastic.service.MyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MyController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MyService myService;

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public void search(@RequestBody MyDocument document) {
		int count = myService.search(document);
		logger.debug("{}", count);
	}

	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public void load() {
		try {
			long start = System.currentTimeMillis();
			myService.load();
			long end = System.currentTimeMillis();
			logger.info(">>> {}", end - start);
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
		}
	}
}
