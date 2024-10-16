package org.xiaoshuyui.simplekb.decoration;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TimingAspect {

    @Around("@annotation(TimeIt)")
    public Object timeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed(); // 调用实际的方法
        } finally {
            long elapsedTime = System.currentTimeMillis() - start;
            log.info("{} executed in {} ms", joinPoint.getSignature(), elapsedTime);
        }
    }
}
