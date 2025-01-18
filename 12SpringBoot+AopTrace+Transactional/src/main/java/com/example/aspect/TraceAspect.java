package com.example.aspect;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Optional;

@Component
@Aspect
public class TraceAspect {

    private final CustomizableTraceInterceptor traceInterceptor;

    public TraceAspect(
            @Value("${trace.useDynamicLogger}") boolean useDynamicLogger,
            @Value("${trace.hideProxyClassNames}") boolean hideProxyClassNames,
            @Value("${trace.logExceptionStackTrace}") boolean logExceptionStackTrace,
            @Value("${trace.enterMessage}") String enterMessage,
            @Value("${trace.exitMessage}") String exitMessage,
            @Value("${trace.exceptionMessage}") String exceptionMessage
    ) {
        traceInterceptor = new CustomizableTraceInterceptor();
        traceInterceptor.setUseDynamicLogger(useDynamicLogger);
        traceInterceptor.setHideProxyClassNames(hideProxyClassNames);
        traceInterceptor.setLogExceptionStackTrace(logExceptionStackTrace);
        traceInterceptor.setEnterMessage(enterMessage);
        traceInterceptor.setExitMessage(exitMessage);
        traceInterceptor.setExceptionMessage(exceptionMessage);
    }

    @Around("""
            execution(* com.example.app..*(..))
            """)
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
        return traceInterceptor.invoke(
                new ProceedingJoinPointMethodInvocation(joinPoint)
        );
    }

    static class ProceedingJoinPointMethodInvocation implements MethodInvocation {
        private final ProceedingJoinPoint joinPoint;

        ProceedingJoinPointMethodInvocation(ProceedingJoinPoint joinPoint) {
            this.joinPoint = joinPoint;
        }

        @Override
        public Object proceed() throws Throwable {
            return joinPoint.proceed();
        }

        @Override
        public Object[] getArguments() {
            return joinPoint.getArgs();
        }

        @Override
        public Object getThis() {
            return joinPoint.getThis();
        }

        @Override
        public Method getMethod() {
            return Optional.of(joinPoint).map(ProceedingJoinPoint::getSignature)
                    .filter(MethodSignature.class::isInstance).map(MethodSignature.class::cast)
                    .map(MethodSignature::getMethod)
                    .get();
        }

        @Override
        public AccessibleObject getStaticPart() {
            return getMethod();
        }
    }

}
