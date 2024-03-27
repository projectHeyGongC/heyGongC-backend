package com.heygongc.global.common.request;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Validator {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // Controller 메소드 실행 전 validate 실행
    @Before("execution(* com.heygongc..presentation.*Controller.*(..))")
    public void validateParameters(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof RequestValidator) {
                ((RequestValidator) arg).validate();
            }
        }
    }
}
