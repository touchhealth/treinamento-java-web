<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
    pageContext.setAttribute("contexto", contextPath);
%>
<html>
<head>
    <title></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-u1OknCvxWvY5kfmNBILK2hRnQC3Pr17a+RTT6rIHI7NnikvbZlHgTPOOmMi466C8" crossorigin="anonymous"></script>
    <style>
        .label, .treinamento-label {
            display: inline-block;
            width: 100px;
            margin-right: 8px;
        }
        .treinamento-field {
            display: block;
            margin: 8px 0 8px 0;
        }
        .fraco {
            opacity: 0.4;
            font-size: 12px;
        }
    </style>
</head>
<body class="container">
<nav class="navbar navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="<%= contextPath %>">Treinamento <span class="fraco">contextPath: '${contextPath}' - contexto: '${contexto}'</span></a>
    </div>
</nav>
<div style="padding-top: 16px; padding-bottom: 16px">
