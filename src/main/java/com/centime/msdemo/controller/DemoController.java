package com.centime.msdemo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.centime.msdemo.model.Person;
import com.centime.msdemo.model.ServerInfo;
import com.centime.msdemo.service.CommonService;

@RestController
@RequestMapping("/api")
public class DemoController {

	private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

	@Autowired
	CommonService commonService;

	@Autowired
	private Tracer tracer;

	@GetMapping("/state")
	public ResponseEntity<ServerInfo> serverStatus() {
		Span span = tracer.currentSpan();
		if (span != null)
			logger.info(" state Transaction ID {}", span.context().traceId());
		return new ResponseEntity<>(new ServerInfo("Up"), HttpStatus.OK);
	}

	@PostMapping("/first-service")
	public ResponseEntity<String> sayHello(@Valid @RequestBody Person person) {
		Span span = tracer.currentSpan();
		if (span != null)
			logger.info(" first-service Transaction ID {}", span.context().traceId());
		return commonService.getConcatinatedResponse(person);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
}
