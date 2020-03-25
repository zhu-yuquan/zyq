package com.zyq.frechwind.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class ControllerRestPagePathAspect {

    private static final Logger log = LoggerFactory.getLogger(ControllerRestPagePathAspect.class);

    /**
     * 定义拦截规则：拦截com.xjj.web.controller包下面的所有类中，有@RequestMapping注解的方法。
     */
    @Pointcut("execution(* com.zyq..*Controller.*(..)) || execution(* com.zyq..*Rest.*(..))")
    public void controllerMethodPointcut() {
    }

    /**
     * 拦截器具体实现
     *
     * @param pjp
     * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。）
     */
    @Around("controllerMethodPointcut()") //指定拦截器规则
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod(); //获取被拦截的方法

        Object result = null;
            String className = method.getDeclaringClass().getName();
            if(className.indexOf("BaseController") == -1 && className.indexOf("BaseRest") == -1) {
                log.info(">--------> method: " + className + "(*)." + method.getName());
            }
            result = pjp.proceed();
            if(className.indexOf("BaseController") == -1) {
                log.info(">--------> method: " + className + "(*)." + method.getName());
            }
            if (result != null) {
                log.info(">--------> page: " + result);
            }
        return result;
    }
}