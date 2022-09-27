package br.com.touchhealth.treinamento.web;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = "/servlet/*")
public class AuditFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        long t1 = System.currentTimeMillis();
        System.out.println("AuditFilter Antes: " + req.getRequestURL());
        super.doFilter(req, res, chain);
        long t2 = System.currentTimeMillis();
        System.out.println("AuditFilter Depois: " + (t2 - t1) + " ms");
    }
}
