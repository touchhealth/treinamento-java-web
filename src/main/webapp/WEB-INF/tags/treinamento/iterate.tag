<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="iterations" required="true" type="java.lang.Integer" %>
<%@ attribute name="var" required="true" rtexprvalue="false" %>
<%@ variable alias="it" name-from-attribute="var" %>
<%
for (int i = 0; i < iterations; i++) {
	jspContext.setAttribute("it", i);
%>
	<jsp:doBody />
<%	
}
%>
