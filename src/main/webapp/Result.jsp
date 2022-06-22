<%@page import="utils.CookieUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css">
<meta charset="UTF-8">
<link rel="icon" type="image/x-icon" href="https://img.icons8.com/ios/344/cookies.png">
<title>Your cookies</title>
</head>
<body>

<%

String cookievalue = "";

Cookie nameString = CookieUtils.getCookie(request, "usercookie");

if(nameString != null){
	cookievalue = nameString.getValue();
} 

%>

<c:set var="valueinfo" scope="session" value="<%= cookievalue %>"/>

<div class="d-flex justify-content-center flex-column">
<c:if test="${ valueinfo.length() > 1 }">
<div class="p-5 d-flex flex-column justify-content-center align-items-center">
<h1 class="lead h-5 p-5 flex-column justify-content-center align-items-center"> Your cookie is: <b class="h-3"><%= cookievalue %></b> </h1> 
<a href="<%= request.getContextPath()%>">Home</a>
</div>
</c:if>

 

<c:if test="${ valueinfo.length() == 0 }">
<div class="p-5 d-flex flex-column justify-content-center align-items-center">
<h1 class="lead h-5 p-5"> You have not added cookies</h1> 
<a href="<%= request.getContextPath()%>">Home</a>
</div>
</c:if>



</div>



</body>
</html>