<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="head" required="true" fragment="true" %>
<%@ attribute name="left" fragment="true" %>
<div style="position: relative;height: 480px; overflow: auto;">
	<div style="position: absolute;top: 0;left: 0;right: 0;height: 80px">
		<jsp:invoke fragment="head"/>
	</div>
	<div style="position: absolute;top: 80px;left: 0;bottom: 0;width: 340px">
		<jsp:invoke fragment="left"/>
	</div>
	<div style="position: absolute;top: 80px;left: 340px;bottom: 0;right: 0">
		<jsp:doBody />
	</div>
</div>
