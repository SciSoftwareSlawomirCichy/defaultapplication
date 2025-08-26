<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>

<html lang="en">
<head>
  <title>Protected Test Page</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <link rel="shortcut icon" href="<%=request.getContextPath()%>/static/favicon.ico" type="image/x-icon" />
</head>
<body>
  <h2>Welcome <% if (request.getRemoteUser() != null) {%><%=request.getRemoteUser()%><%}else {%>UNAUTENTICATED<%} %></h2>
</body>
</html>