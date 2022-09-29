<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty propriedade}">
    <div>propriedade antes: ${propriedade}</div>
</c:if>

<c:if test="${not empty propriedadeCliente}">
    <div>propriedade cliente: ${propriedadeCliente}</div>
</c:if>

<div>propriedade nova: ${propriedadeNova}</div>

<div>Cookie Value: <span id="cookieValue"></span></div>

<hr>

<form action="sexto" method="get">
    <input type="text" name="propriedade" id="propriedade">
    <input type="submit" value="Salvar">
</form>

<form action="sexto" method="post">
    <input type="submit" value="Remover Sessão">
</form>

<form action="sexto" method="get">
    <input type="submit" value="Recarregar">
</form>

<script type="text/javascript">
    cookieStore.get("propriedadeCliente").then(c => {
        console.log(c);
        if (c != null) {
            document.getElementById("cookieValue").innerText = c.value;
        } else {
            document.getElementById("cookieValue").innerText = "";
        }
    });
    document.getElementById("propriedade").focus();
</script>

