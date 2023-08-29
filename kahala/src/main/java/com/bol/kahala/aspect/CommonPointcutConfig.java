package com.bol.kahala.aspect;


import org.aspectj.lang.annotation.Pointcut;

public class CommonPointcutConfig {

	@Pointcut("execution(* com.bol.kahala.service.impl.*.*(..))")
	public void allServices() {}

	@Pointcut("@annotation(com.bol.kahala.aspect.performance.annotations.TrackTime)")
	public void trackTimeAnnotation() {}
}
