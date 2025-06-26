/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pahana.utils;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class JwtFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {
    HttpServletRequest r = (HttpServletRequest) req;
    HttpServletResponse s = (HttpServletResponse) res;

    String path = r.getRequestURI();

    // Allow login servlet and static files without auth
    if (path.contains("/AuthServlet") ||
        path.endsWith("/login.html") ||
        path.endsWith(".css") ||
        path.endsWith(".js") ||
        path.endsWith(".png") ||
        path.endsWith(".jpg") ||
        path.endsWith(".jpeg") ||
        path.endsWith(".woff") ||
        path.endsWith(".ttf")) {
        chain.doFilter(req, res);
        return;
    }

    String auth = r.getHeader("Authorization");
    if (auth != null && auth.startsWith("Bearer ")) {
        String username = JwtUtil.validate(auth.substring(7));
        if (username != null) {
            chain.doFilter(req, res);
            return;
        }
    }

    s.sendError(401, "Unauthorized");
}

    public void init(FilterConfig c) {}
    public void destroy() {}
}

