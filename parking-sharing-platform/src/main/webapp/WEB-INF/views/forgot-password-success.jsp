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

                    <h2 class="mb-4">
                        Password Reset
                    </h2>

                    <div class="alert alert-success">

                        <strong>
                            ✔ Reset link generated successfully!
                        </strong>

                        <br><br>

                        This project does not send emails.

                        <br>

                        Click the button below to continue.

                    </div>

                    <a href="${pageContext.request.contextPath}${resetLink}"
                       class="btn btn-primary w-100 mb-3">

                        Reset Password

                    </a>

                    <a href="${pageContext.request.contextPath}/login"
                       class="btn btn-outline-secondary w-100">

                        Back to Login

                    </a>

                </div>

            </div>

        </div>

    </div>

</div>

</body>
</html>