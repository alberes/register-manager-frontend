package io.github.alberes.register.manager.frontend.filter;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.github.alberes.register.manager.frontend.constants.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;

@Component
public class FeignHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        template.header(Constants.APP_NAME, Constants.REGISTER_MAMANER_FRONTEND);
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String value = request.getHeader(headerName);
                if(!Constants.HEADERS_TO_REMOVE.contains(headerName)) {
                    template.header(headerName, value);
                }
            }
        }
    }
}
