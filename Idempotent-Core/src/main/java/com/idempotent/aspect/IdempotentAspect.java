package com.idempotent.aspect;

import com.idempotent.annotation.Idempotent;
import com.idempotent.exception.BusinessException;
import com.idempotent.operator.IdempotentOperator;
import com.idempotent.operator.IdempotentOperatorManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class IdempotentAspect {


    private String idempotentKey;

    private IdempotentOperatorManager idempotentOperatorManager;

    public IdempotentAspect(String idempotentKey, IdempotentOperatorManager idempotentOperatorManager) {

        this.idempotentKey = idempotentKey;

        this.idempotentOperatorManager = idempotentOperatorManager;

    }


    @Pointcut("@annotation(com.qpp.idempotent.annotation.Idempotent)")
    public void annotationIdempotent() {

    }


    @Around("annotationIdempotent()")
    public Object aroundIdempotent(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Optional<HttpServletRequest> requestOptional = this.getRequest();

        //is web request
        if (!requestOptional.isPresent()) {
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        }

        Method method = this.getMethod(proceedingJoinPoint);

        //find annotation
        Optional<Idempotent> idempotentOptional = this.findIdempotentAnnotation(method);

        //if not exists
        if (!idempotentOptional.isPresent()) {
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        }

        //check idempotentKey is required
        boolean required = idempotentOptional.get().required();

        Optional<String> idempotentKey = this.getIdempotentValue(requestOptional.get());

        if (!idempotentKey.isPresent()) {
            if (required) {
                throw new BusinessException(999900, "idempotentKey can not be null!");
            } else {
                return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
            }
        }

        //find the token status record
        Optional<IdempotentOperator> idempotentOperatorOptional = idempotentOperatorManager.findByKey(idempotentKey.get());

        if (!idempotentOperatorOptional.isPresent()) {
            throw new BusinessException("idempotentKey " + idempotentKey.get() + " is not valid!");
        }

        IdempotentOperator idempotentOperator = idempotentOperatorOptional.get();

        //if has executed
        if (idempotentOperator.isExecuted()) {
            return idempotentOperator.getResultObj();
        }

        //if is processing
        boolean canStartProcess = idempotentOperatorManager.processing(idempotentOperator.getId());
        if (!canStartProcess) {
            throw new BusinessException(999901, "idempotent request " + idempotentKey + " is processing,please try it later!");
        }

        Object result = null;

        try {
            result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        } catch (Exception e) {
            //invoke fail record
            idempotentOperatorManager.fail(idempotentOperator.getId());
            throw e;
        }

        //invoke success record
        idempotentOperatorManager.complete(idempotentOperator.getId(), result);

        return result;
    }


    private Method getMethod(ProceedingJoinPoint proceedingJoinPoint) {

        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        return method;
    }

    private Optional<HttpServletRequest> getRequest() {
        Optional<HttpServletRequest> request = Optional.empty();
        try {
            request = Optional.ofNullable(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        } catch (Exception e) {

        }

        return request;

    }


    private Optional<Idempotent> findIdempotentAnnotation(Method method) {


        Optional<Idempotent> result = Optional.empty();


        Idempotent idempotent = AnnotationUtils.findAnnotation(method, Idempotent.class);

        if (idempotent == null) {
            idempotent = AnnotationUtils.findAnnotation(method.getDeclaringClass(), Idempotent.class);
        }


        result = Optional.ofNullable(idempotent);

        return result;

    }


    private Optional<String> getIdempotentValue(HttpServletRequest request) {

        //先从查询参数获取
        String value = request.getParameter(this.idempotentKey);

        //如果查询参数处为空 再从header中获取
        if (StringUtils.isEmpty(value)) {
            value = request.getHeader(this.idempotentKey);
        }


        return Optional.ofNullable(value);
    }


}
