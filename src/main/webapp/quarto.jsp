<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Quarto - JSP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-u1OknCvxWvY5kfmNBILK2hRnQC3Pr17a+RTT6rIHI7NnikvbZlHgTPOOmMi466C8" crossorigin="anonymous"></script>
    <style>
        .label {
            display: inline-block;
            width: 100px;
        }
    </style>
</head>
<body class="container">
<%-- <jsp:include page="terceiro.jsp"/> --%>
<%@include file="terceiro.jsp"%>

<% for (int i = 0; i < 10; i++) { %>
<div>indice <%= i %></div>
<% } %>

<div class="card" style="margin-top: 16px">
    <div class="card-body">
        <h5 class="card-title">Pessoa</h5>
        <div><span class="label">ID:</span> ${pessoa.id}</div>
        <div><span class="label">NOME:</span> ${pessoa.nome}</div>
        <div><span class="label">DT NASC:</span> ${pessoa.dataNascimento}</div>
        <div><span class="label">SEXO:</span> ${pessoa.sexo}</div>
        <div><span class="label">CPF:</span> ${pessoa.cpf}</div>
    </div>
</div>
</body>
</html>
