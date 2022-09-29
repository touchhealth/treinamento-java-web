package br.com.touchhealth.treinamento.web;


import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.security.DeclareRoles;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/admin/primeiro")
@DeclareRoles("admin")
@ServletSecurity(@HttpConstraint(rolesAllowed = "admin"))
public class PrimeroServletSeguro extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("<html><body>Meu Primeiro Servlet Seguro : Ola " + req.getRemoteUser() + "</body></html>");
    }
}
