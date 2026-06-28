<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    com.epam.capstone.model.User user =
            (com.epam.capstone.model.User)
                    request.getAttribute("user");
%>

<html>
<head>

    <title>Parking Sharing Platform</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
          rel="stylesheet">

</head>

<body class="bg-light">

<div class="container mt-5">

    <div class="row justify-content-center">

        <div class="col-md-8">

            <div class="card shadow">

                <div class="card-body text-center">

                    <h1 class="mb-3">
                        Parking Sharing Platform
                    </h1>

                    <p class="text-muted">
                        Find and share parking places easily
                    </p>

                    <hr>

                    <% if (user != null) { %>

                    <h4 class="mb-4">
                        Welcome,
                        <%= user.getName() %>
                    </h4>

                    <div class="d-grid gap-2 col-md-6 mx-auto">

                        <a href="${pageContext.request.contextPath}/profile"
                           class="btn btn-primary">
                            My Profile
                        </a>

                        <a href="#"
                           class="btn btn-outline-success">
                            Find Parking
                        </a>

                        <a href="#"
                           class="btn btn-outline-success">
                            Offer Parking
                        </a>

                        <a href="${pageContext.request.contextPath}/logout"
                           class="btn btn-danger">
                            Logout
                        </a>

                    </div>

                    <% } else { %>

                    <div class="d-grid gap-2 col-md-6 mx-auto">

                        <a href="${pageContext.request.contextPath}/login"
                           class="btn btn-primary">
                            Login
                        </a>

                        <a href="${pageContext.request.contextPath}/register"
                           class="btn btn-outline-primary">
                            Register
                        </a>

                    </div>

                    <% } %>

                </div>

            </div>

        </div>

    </div>

</div>

</body>
</html>