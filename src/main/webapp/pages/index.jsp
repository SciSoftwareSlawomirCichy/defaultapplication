<%@page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>

<html lang="en">
<head>
  <title>HTTP Server Test Page</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <link rel="shortcut icon" href="<%=request.getContextPath()%>/static/favicon.ico" type="image/x-icon" />
</head>
<body>
  <h1>Hello World!</h1>
  <h2>Welcome on Default Web Application!</h2>
  <%
  if (request.getRemoteUser() != null) {%>Logged as <%=request.getRemoteUser()%>.<%}
  %>
  <br/>
  <a href="<%=request.getContextPath()%>/pages/protected.jsp">Protected Test Page</a>
</body>
</html>
