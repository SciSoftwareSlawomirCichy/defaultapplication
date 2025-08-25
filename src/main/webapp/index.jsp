<%@page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<body>
	<h1>Hello World!</h1>
	<h2>Welcome on Default Web Application!</h2>
	<%
	if (request.getRemoteUser() != null) {%>Logged as <%=request.getRemoteUser()%>.<%}
	%>
	<br />
	<a href="<%=request.getContextPath()%>/pages/index.jsp">Protected Page</a>
</body>
</html>
