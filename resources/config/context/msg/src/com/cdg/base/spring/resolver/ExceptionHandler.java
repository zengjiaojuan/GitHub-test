package com.cddgg.base.spring.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.cddgg.base.spring.exception.ResponseException;

/**
 * 异常处理者
 * @author ldd
 *
 */
@Component
public class ExceptionHandler extends SimpleMappingExceptionResolver {

    
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception exception) {
        
        Throwable ex = exception.getCause();
        
        //这是一个已知的响应式异常
        if(ex instanceof ResponseException){
            
            return ((ResponseException)ex).execute(request, response, handler, exception);
            
        }
        
        return super.doResolveException(request, response, handler, exception);
    }
    
}
