package io.github.alberes.register.manager.frontend.filter;

import io.github.alberes.register.manager.frontend.constants.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SecuritySessionFilter extends OncePerRequestFilter {

    private static Map<String, String> IGNORED_PATHS;

    public SecuritySessionFilter(){
        IGNORED_PATHS = new HashMap<String, String>();
        IGNORED_PATHS.put("/login", "/login");
        IGNORED_PATHS.put("/login-user", "/login-user");
        IGNORED_PATHS.put("/", "/");
        IGNORED_PATHS.put("/styles/login.css", "/styles/login.css");
        IGNORED_PATHS.put("/styles/listregister.css", "/styles/listregister.css");
        IGNORED_PATHS.put("/styles/register.css", "/styles/register.css");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if(!IGNORED_PATHS.containsKey(requestURI)){
            if(request.getSession(false) == null || request.getSession().getAttribute(Constants.USER_SESSION) == null) {
                response.sendRedirect(Constants.LOGIN);
            }
        }
        filterChain.doFilter(request, response);
    }
}