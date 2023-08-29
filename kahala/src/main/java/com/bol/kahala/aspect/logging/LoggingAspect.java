package com.bol.kahala.aspect.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

import static com.bol.kahala.util.LoggingUtil.logInfo;

@Configuration
@Aspect
public class LoggingAspect {

    @Before("com.bol.kahala.aspect.CommonPointcutConfig.allServices()")
    public void logMethodCallBeforeExecution(JoinPoint joinPoint) {
        String requestId = UUID.randomUUID().toString();
        // Add the trace id to MDC
        MDC.put("requestId", requestId);
        logInfo("requestId created");
        logInfo("Before Aspect - {} is called with arguments: {}"
                , joinPoint, joinPoint.getArgs());
    }

    @After("com.bol.kahala.aspect.CommonPointcutConfig.allServices()")
    public void logMethodCallAfter(JoinPoint joinPoint) {
        logInfo("After Aspect - {} has thrown an exception {}"
                , joinPoint);
        MDC.remove("requestId");
        logInfo("requestId removed");
    }

    @AfterThrowing(
            pointcut = "com.bol.kahala.aspect.CommonPointcutConfig.allServices()",
            throwing = "exception"
    )
    public void logMethodCallAfterException(JoinPoint joinPoint, Exception exception) {
        logInfo("AfterThrowing Aspect - {} has thrown an exception {}"
                , joinPoint, exception);
    }

    @AfterReturning(
            pointcut = "com.bol.kahala.aspect.CommonPointcutConfig.allServices()",
            returning = "resultValue"
    )
    public void logMethodCallAfterSuccessfulExecution(JoinPoint joinPoint,
                                                      Object resultValue) {
        logInfo("AfterReturning Aspect - {} has returned {}"
                , joinPoint, resultValue);
    }
}
