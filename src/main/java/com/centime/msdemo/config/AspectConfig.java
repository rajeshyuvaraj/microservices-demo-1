package com.centime.msdemo.config;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class AspectConfig {

	private static final Logger logger = LoggerFactory.getLogger(AspectConfig.class);

	@Around("execution(* *(..)) && @annotation(com.centime.msdemo.config.LogIt)")
	public Object logMethods(ProceedingJoinPoint jp) throws Throwable {

		String[] argNames = ((MethodSignature) jp.getSignature()).getParameterNames();
		Object[] values = jp.getArgs();
		Map<String, Object> params = new HashMap<>();
		if (argNames.length != 0) {
			for (int i = 0; i < argNames.length; i++) {
				params.put(argNames[i], values[i]);
			}
		}
		logger.info("Calling-->{}", jp.getSignature().getName());
		System.out.println();
		if (!params.isEmpty())
			logger.info("Parameters-->{}", params);
		return jp.proceed(jp.getArgs());
	}
}
