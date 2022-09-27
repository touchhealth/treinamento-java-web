package br.com.touchhealth.treinamento.web;


import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.touchhealth.treinamento.model.Pessoa;
import br.com.touchhealth.treinamento.model.Sexo;


@WebServlet(urlPatterns = "/servlet/quarto")
public class QuartoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result = req.getParameter("result");
        if (result == null || result.isEmpty()) {
            result = "quarto";
        }
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/" + result + ".jsp");
        req.setAttribute("pessoa",
                new Pessoa()
                    .id(10L)
                    .nome("Fernando")
                    .dataNascimento(new Date(367383600000L))
                    .sexo(Sexo.MASCULINO)
                    .cpf("00011122233")
        );
        requestDispatcher.forward(req, resp);
    }

}
