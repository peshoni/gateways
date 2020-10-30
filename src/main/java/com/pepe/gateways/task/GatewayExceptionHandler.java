/**
 * 
 */
package com.pepe.gateways.task;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * The global exception handler
 * 
 * @author petar ivanov
 *
 */
@ControllerAdvice
public class GatewayExceptionHandler {

	@ExceptionHandler(value = { Exception.class, RuntimeException.class })
	public void defaultErrorHandler(HttpServletRequest request, Exception e) {
		if (e instanceof IOException) {
			System.out.println("I/O Exception");
		} else {
			System.out.println("Exception");
		}
		System.out.println(e.getMessage());
		e.printStackTrace();
	}

}