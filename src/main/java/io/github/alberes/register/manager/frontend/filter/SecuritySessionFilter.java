package io.github.alberes.register.manager.frontend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class SecuritySessionFilter extends OncePerRequestFilter {

    //private static final String IGNORED_PATH = "/login-user";

    private static Map<String, String> IGNORED_PATHS;

    public SecuritySessionFilter(){
        IGNORED_PATHS = new HashMap<String, String>();
        IGNORED_PATHS.put("/login", "/login");
        IGNORED_PATHS.put("/login-user", "/login-user");
        IGNORED_PATHS.put("/", "/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if(!IGNORED_PATHS.containsKey(requestURI)){
            Object userSession = request.getSession().getAttribute("userSession");
            if(userSession == null) {
                response.sendRedirect("/login");
            }
        }
        filterChain.doFilter(request, response);
    }
}