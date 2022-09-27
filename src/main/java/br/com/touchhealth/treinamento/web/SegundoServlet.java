package br.com.touchhealth.treinamento.web;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = "/servlet/segundo")
public class SegundoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String who = "Segundo";
        if (req.getAttribute("who") != null) {
            who = (String) req.getAttribute("who");
        }
        out.println("<html><body>Meu Segundo Servlet : Ola " + who + "</body></html>");
    }
}
