<%@ tag pageEncoding="UTF-8" %>
<%@ taglib prefix="t" uri="http://www.touchtec.com.br/treinamento-tags" %>
<%@ attribute name="label" required="true" %>
<div class="treinamento-field">
	<t:label>${label}</t:label>
	<jsp:doBody />
</div>