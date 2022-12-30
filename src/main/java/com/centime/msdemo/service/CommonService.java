package com.centime.msdemo.service;

import org.springframework.http.ResponseEntity;

import com.centime.msdemo.model.Person;

public interface CommonService {
	public ResponseEntity<String> getConcatinatedResponse(Person person);

	public ResponseEntity<String> getSecondServiceResponse();

	public ResponseEntity<String> getThirdServiceResponse(Person person);

}
