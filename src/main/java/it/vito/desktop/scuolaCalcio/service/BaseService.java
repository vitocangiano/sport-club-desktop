package it.vito.desktop.scuolaCalcio.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
 




public class BaseService {
	int MAX_RESULTS = 5;
	Duration TIMEOUT = Duration.ofSeconds(20L);
	ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
	 
    Validator validator = validatorFactory.getValidator();


	protected Response response = new Response();

	protected void setError(Exception e) {
		setError(e.getMessage());
	}

	protected void setError(InvocationTargetException e) {
		setError(e.getCause().toString());
	}

	protected void setError(String error) {
		contentReset();
		response.setStatus(false);
		response.setError(response.getError() + "\r\n" + error);
	}

	protected void contentReset() {
		response.setContent(new HashMap<String, Object>());
	}

	protected void responseReset() {
		response = new Response();
	}

	protected void checkOrThrow(String message) throws Exception {
		if (!response.getStatus()) {
			throw new Exception(message);
		}
	}
	
	protected StringBuilder valid(Object entity) {
		StringBuilder eLst = new StringBuilder();
		Set<ConstraintViolation<Object>> valid = validator.validate(entity);
		for (ConstraintViolation<Object> e : valid) {
			eLst.append(e.getPropertyPath() + ": " + e.getMessage() + "\r\n");
		}
		return eLst;
	}


}
