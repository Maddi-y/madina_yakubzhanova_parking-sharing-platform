<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>

    <title>Password Reset</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
          rel="stylesheet">

</head>

<body class="bg-light">

<div class="container mt-5">

    <div class="row justify-content-center">

        <div class="col-md-6">

            <div class="card shadow">

                <div class="card-body text-center">

                    <h2 class="text-success mb-4">
                        Password changed successfully
                    </h2>

                    <p class="lead">
                        Your password has been updated.
                    </p>

                    <p class="text-muted">
                        You can now log in using your new password.
                    </p>

                    <a href="${pageContext.request.contextPath}/login"
                       class="btn btn-primary mt-3">

                        Go to Login

                    </a>

                </div>

            </div>

        </div>

    </div>

</div>

</body>
</html>