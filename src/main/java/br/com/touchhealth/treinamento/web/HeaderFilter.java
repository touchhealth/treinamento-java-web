package br.com.touchhealth.treinamento.web;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//@WebFilter(urlPatterns = { "/servlet/*", "/admin/*" })
public class HeaderFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("HeaderFilter Antes " + req.getRequestURL());
        chain.doFilter(req, res);
        res.addHeader("X-HEALS-VERSION", "1.0");
        System.out.println("HeaderFilter depois " + req.getRequestURL());
    }
}
