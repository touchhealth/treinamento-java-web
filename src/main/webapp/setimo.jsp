<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://www.touchtec.com.br/treinamento-tags" %>
<jsp:include page="includes/cabecalho.jsp" />
<form action="setimo" method="post">
    <t:label>a:</t:label><input type="text" name="a" value="${a}"><br>
    <t:label>b:</t:label><input type="text" name="b" value="${b}"><br>
    <span class="label">c:</span><input type="text" name="c" value="${c}"><br>
    <span class="label">equacao:</span><input type="text" name="equacao" value="${equacao}"><br>
    <span class="label">Resultado:</span>${resultado}<br>
    <input type="submit" value="Rodar">
</form>
<jsp:include page="includes/rodape.jsp" />
