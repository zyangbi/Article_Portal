package com.imooc.api.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Around("execution(* com.imooc.*.service..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("========== Start {}.{} ============",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        if (duration > 3000) {
            logger.error("=============== Execution takes {}ms ===============", duration);
        } else if (duration > 2000) {
            logger.debug("=============== Execution takes {}ms ===============", duration);
        } else {
            logger.info("=============== Execution takes {}ms ===============", duration);
        }
        return result;

    }

}
