<%@page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<body>
	<h2>Hello World!</h2>
	<%
	if (request.getRemoteUser() != null) {%>Logged as <%=request.getRemoteUser()%>.<%}
	%>
	<br />
	<a href="<%=request.getContextPath()%>/pages/index.jsp">Protected Page</a>
</body>
</html>
