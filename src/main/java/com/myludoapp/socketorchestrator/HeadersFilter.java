package com.myludoapp.socketorchestrator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class HeadersFilter implements Filter {
    @Override
    public void init(FilterConfig fc) throws ServletException {
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain fc) throws IOException, ServletException {
        log.info("Inside doFilter...");
        if(servletResponse instanceof HttpServletResponse){
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            // here add the headers
            String whitelist = "http://localhost:3000, http://129.168.0.105:3000, http://172.18.5.3:3000, http://192.168.1.4:3000";
            response.setHeader("Access-Control-Allow-Origin", whitelist);
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD");
        }
        fc.doFilter(servletRequest, servletResponse);
    }
    @Override
    public void destroy() {
    }
}