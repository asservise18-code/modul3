package com.example.modul3.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceMethods() {
    }

    @Around("serviceMethods()")
    public Object logServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature()
                .getDeclaringType()
                .getSimpleName();

        String methodName = joinPoint.getSignature().getName();

        log.info("START {}.{}()", className, methodName);

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - startTime;

            log.info("END {}.{}() in {} ms", className, methodName, duration);

            return result;
        } catch (Exception exception) {
            log.error("ERROR in {}.{}(): {}: {}",
                    className,
                    methodName,
                    exception.getClass().getSimpleName(),
                    exception.getMessage()
            );

            throw exception;
        }
    }
}