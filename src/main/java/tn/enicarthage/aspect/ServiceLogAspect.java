package tn.enicarthage.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ServiceLogAspect {

 
    @Pointcut("within(tn.enicarthage.services..*)")
    public void servicePointcut() {
    }

    @AfterThrowing(pointcut = "servicePointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
        logger.error("Exception in service {}.{}() with cause = {}", 
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), 
                e.getCause() != null ? e.getCause() : "NULL");
        logger.error("Service exception details: ", e);
    }

  
    @Around("servicePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
        
        long start = System.currentTimeMillis();
        logger.debug("Enter: {}.{}() with arguments = {}", 
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), 
                Arrays.toString(joinPoint.getArgs()));
        
        try {
            Object result = joinPoint.proceed();
            
            long executionTime = System.currentTimeMillis() - start;
            logger.debug("Exit: {}.{}() with result = {} (executed in {} ms)", 
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), 
                    result,
                    executionTime);
            
            if (executionTime > 1000) {
                logger.warn("Long execution time detected: {}.{}() took {} ms", 
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), 
                    executionTime);
            }
            
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument in service: {} in {}.{}()", 
                    Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), 
                    joinPoint.getSignature().getName());
            throw e;
        }
    }
}