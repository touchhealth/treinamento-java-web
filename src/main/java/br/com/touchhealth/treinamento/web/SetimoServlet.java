package br.com.touchhealth.treinamento.web;


import java.io.IOException;

import javax.el.ELProcessor;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = "/servlet/setimo")
public class SetimoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/setimo.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String equacao = req.getParameter("equacao");
        String a = req.getParameter("a");
        String b = req.getParameter("b");
        String c = req.getParameter("c");
        ELProcessor processor = new ELProcessor();
        if (a != null && !a.isEmpty()) {
            req.setAttribute("a", a);
            processor.setValue("a", Double.valueOf(a));
        }
        if (b != null && !b.isEmpty()) {
            req.setAttribute("b", b);
            processor.setValue("b", Double.valueOf(b));
        }
        if (c != null && !c.isEmpty()) {
            req.setAttribute("c", c);
            processor.setValue("c", Double.valueOf(c));
        }
        if (equacao != null && !equacao.isEmpty()) {
            req.setAttribute("equacao", equacao);
            req.setAttribute("resultado", processor.eval(equacao));
        }
        req.getRequestDispatcher("/setimo.jsp").forward(req, resp);
    }
}
