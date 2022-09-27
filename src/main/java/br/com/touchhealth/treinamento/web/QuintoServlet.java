package br.com.touchhealth.treinamento.web;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.touchhealth.treinamento.model.Pessoa;
import br.com.touchhealth.treinamento.model.Sexo;


@WebServlet(urlPatterns = "/servlet/quinto")
public class QuintoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher cabecalhoDispatcher = req.getRequestDispatcher("/includes/cabecalho.jsp");
        RequestDispatcher rodapeDispatcher = req.getRequestDispatcher("/includes/rodape.jsp");
        cabecalhoDispatcher.include(req, resp);
        PrintWriter out = resp.getWriter();
        out.println("<div>Aqui vai só o miolo da página! Mas espere não é só apenas isso!!!</div>");
        out.flush();
        rodapeDispatcher.include(req, resp);
    }

}
