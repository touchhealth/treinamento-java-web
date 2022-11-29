<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://www.touchtec.com.br/treinamento-tags" %>
<%@ taglib prefix="tt" tagdir="/WEB-INF/tags/treinamento" %>
<jsp:include page="includes/cabecalho.jsp" />
<form action="setimo" method="post">
    <div class="treinamento-field">
        <t:label>a:</t:label>
        <input type="text" name="a" value="${a}">
    </div>
    <t:field label="b:">
        <input type="text" name="b" value="${b}">
    </t:field>
    <t:field label="c:">
        <input type="text" name="c" value="${c}">
    </t:field>
    <t:field label="equacao:">
        <input type="text" name="equacao" value="${equacao}">
    </t:field>
    <t:field label="Resultado:" labelSize="200">
        ${resultado}
    </t:field>
    <tt:field label="Teste de Tag">
    	<input type="text" name="qualquer" value="">
    </tt:field>
    <input type="submit" value="Rodar">
</form>
<hr>

<tt:layout>
	<jsp:attribute name="head">
		<tt:simple></tt:simple>
	</jsp:attribute>
	<jsp:attribute name="left">
		<tt:olamundo greeting="Fernando" />
	</jsp:attribute>
	<jsp:body>
		<ul>	
			<tt:iterate iterations="5" var="k">
				<li>Iteração ${k}</li>
			</tt:iterate>
		</ul>
	</jsp:body>
</tt:layout>

<jsp:include page="includes/rodape.jsp" />
