<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<% if (request.getRemoteUser() != null) {%><%=request.getRemoteUser()%><%}else {%>UNAUTENTICATED<%} %>