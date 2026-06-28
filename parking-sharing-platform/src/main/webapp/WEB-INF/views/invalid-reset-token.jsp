<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>

    <title>Invalid Reset Link</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
          rel="stylesheet">

</head>

<body class="bg-light">

<div class="container mt-5">

    <div class="row justify-content-center">

        <div class="col-md-6">

            <div class="card shadow border-danger">

                <div class="card-body text-center">

                    <h2 class="text-danger mb-3">
                        Invalid Reset Link
                    </h2>

                    <p class="mb-4">

                        ${message}

                    </p>

                    <div class="d-grid gap-2">

                        <a href="${pageContext.request.contextPath}/forgot-password"
                           class="btn btn-primary">

                            Request New Reset Link

                        </a>

                        <a href="${pageContext.request.contextPath}/login"
                           class="btn btn-outline-secondary">

                            Back to Login

                        </a>

                    </div>

                </div>

            </div>

        </div>

    </div>

</div>

</body>
</html>