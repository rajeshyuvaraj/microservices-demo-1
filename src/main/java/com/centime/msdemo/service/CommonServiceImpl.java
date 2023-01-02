package com.centime.msdemo.service;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.centime.msdemo.config.LogIt;
import com.centime.msdemo.model.Person;

@Service
public class CommonServiceImpl implements CommonService {

	@Autowired
	RestTemplate restTemplate;

	@Value("${app.server.service.2.url}")
	private String secondService;

	@Value("${app.server.service.3.url}")
	private String thirdService;

	private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

	@Override
	@LogIt
	public ResponseEntity<String> getConcatinatedResponse(Person person) {
		String greeting = "";

		ResponseEntity<String> secondServiceEntity = getSecondServiceResponse();

		if (secondServiceEntity.getStatusCode() == HttpStatus.OK) {
			greeting = secondServiceEntity.getBody();
		} else {
			logger.error("error in callling getSecondServiceResponse");
			return new ResponseEntity<>(secondServiceEntity.getBody(), secondServiceEntity.getStatusCode());
		}

		ResponseEntity<String> thirdServiceEntity = getThirdServiceResponse(person);
		if (thirdServiceEntity.getStatusCode() == HttpStatus.OK) {
			greeting = greeting + thirdServiceEntity.getBody();
		} else {
			logger.error("error in callling getThirdServiceResponse");
			return new ResponseEntity<>(thirdServiceEntity.getBody(), thirdServiceEntity.getStatusCode());
		}
		return new ResponseEntity<>(greeting, HttpStatus.OK);
	}

	@Override
	@LogIt
	public ResponseEntity<String> getSecondServiceResponse() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		try {
			return new ResponseEntity<>(restTemplate
					.exchange(secondService + "api/second-service/", HttpMethod.GET, entity, String.class).getBody(),
					HttpStatus.OK);
		} catch (HttpStatusCodeException e) {
			logger.error("error in callling getSecondServiceResponse--->{}", e.getMessage());
			return ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
					.body(e.getResponseBodyAsString());
		}
	}

	@Override
	@LogIt
	public ResponseEntity<String> getThirdServiceResponse(Person person) {
		HttpHeaders headersPost = new HttpHeaders();
		headersPost.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<Person> entityPost = new HttpEntity<Person>(person, headersPost);
		try {
			return new ResponseEntity<>(restTemplate
					.exchange(thirdService + "api/third-service/", HttpMethod.POST, entityPost, String.class).getBody(),
					HttpStatus.OK);
		} catch (HttpStatusCodeException e) {
			logger.error("error in callling getThirdServiceResponse--->{}", e.getMessage());
			return ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
					.body(e.getResponseBodyAsString());
		}
	}
}
