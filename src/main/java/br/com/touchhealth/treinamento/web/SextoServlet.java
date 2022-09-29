package br.com.touchhealth.treinamento.web;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(urlPatterns = "/servlet/sexto")
public class SextoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher cabecalhoDispatcher = req.getRequestDispatcher("/includes/cabecalho.jsp");
        RequestDispatcher corpoDispatcher = req.getRequestDispatcher("/sexto.jsp");
        RequestDispatcher rodapeDispatcher = req.getRequestDispatcher("/includes/rodape.jsp");
        cabecalhoDispatcher.include(req, resp);

        String propriedadeNova = req.getParameter("propriedade");
        req.setAttribute("propriedadeNova", propriedadeNova);

        Cookie cookieRequest = Arrays.stream(req.getCookies())
                .filter((Cookie c) -> c.getName().equals("propriedadeCliente"))
                .findFirst()
                .orElse(null);
        if (cookieRequest != null) {
            req.setAttribute("propriedadeCliente", Integer.valueOf(cookieRequest.getValue()));
        }
        corpoDispatcher.include(req, resp);

        rodapeDispatcher.include(req, resp);

        if (propriedadeNova != null) {
            HttpSession session = req.getSession();
            session.setAttribute("propriedade", propriedadeNova);
            if (propriedadeNova.equalsIgnoreCase("X") && cookieRequest != null) {
                cookieRequest.setMaxAge(0);
                resp.addCookie(cookieRequest);
            } else {
                try {
                    Integer.valueOf(propriedadeNova);
                    Cookie cookie = new Cookie("propriedadeCliente", propriedadeNova);
                    //cookie.setHttpOnly(true);
                    resp.addCookie(cookie);
                } catch (Exception e) {
                    System.out.println("Propriedade Nova informada não é numerica");
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        doGet(req, resp);
    }
}
