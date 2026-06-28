package com.epam.capstone.filter;

import com.epam.capstone.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();

        HttpSession session = req.getSession(false);

        User user = null;

        if (session != null) {
            user = (User) session.getAttribute("user");
        }

        boolean publicPage =
                uri.equals(contextPath + "/")
                        || uri.startsWith(contextPath + "/login")
                        || uri.startsWith(contextPath + "/register")
                        || uri.startsWith(contextPath + "/forgot-password")
                        || uri.startsWith(contextPath + "/reset-password")
                        || uri.startsWith(contextPath + "/css")
                        || uri.startsWith(contextPath + "/js")
                        || uri.startsWith(contextPath + "/images")
                        || uri.contains("favicon.ico");

        if (user != null &&
                (uri.startsWith(contextPath + "/login")
                        || uri.startsWith(contextPath + "/register")
                        || uri.startsWith(contextPath + "/forgot-password")
                        || uri.startsWith(contextPath + "/reset-password"))) {

            resp.sendRedirect(contextPath + "/");
            return;
        }


        if (publicPage) {
            chain.doFilter(request, response);
            return;
        }


        if (user == null) {
            resp.sendRedirect(contextPath + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}