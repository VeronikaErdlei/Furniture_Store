package org.example.Aspect;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;



/**
 * LoggerAspect is an excellent tool for increasing the transparency and manageability of your project.
 * You can use it as-is or extend it depending on your needs
 * (e.g., adding more detailed logs for specific layers).
 */


@Aspect
@Component
public class LoggerAspect {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Pointcut("within(org.example.controller..*) || within(org.example.service..*)")
    public void applicationPackagePointcut() {
    }

    @Before("applicationPackagePointcut()")
    public void beforeAdvice(JoinPoint joinPoint) {
        logger.debug("Method {} from Class {} started with args: {}",
                () -> joinPoint.getSignature().getName(),
                () -> joinPoint.getSourceLocation().getWithinType().getName(),
                () -> Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "applicationPackagePointcut()", returning = "returnValue")
    public void afterReturningAdvice(JoinPoint joinPoint, Object returnValue) {
        logger.debug("Method {} from Class {} completed with return value: {}",
                () -> joinPoint.getSignature().getName(),
                () -> joinPoint.getSourceLocation().getWithinType().getName(),
                () -> returnValue);
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "exception")
    public void afterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {
        logger.error("Method {} from Class {} threw an exception: {}",
                () -> joinPoint.getSignature().getName(),
                () -> joinPoint.getSourceLocation().getWithinType().getName(),
                () -> exception.getMessage());
    }
}
