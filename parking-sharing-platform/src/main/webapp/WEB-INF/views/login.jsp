<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Login</title>
</head>

<body>

<h1>Login</h1>

<form action="${pageContext.request.contextPath}/login"
      method="post">

    <div>
        <label>Email</label>
        <input type="email"
               name="email">
    </div>

    <br>

    <div>
        <label>Password</label>
        <input type="password"
               name="password">
    </div>

    <br>

    <button type="submit">
        Login
    </button>

</form>

<br>

<a href="${pageContext.request.contextPath}/register">
    Register
</a>

</body>
</html>