<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>

    <title>Forgot Password</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
          rel="stylesheet">

</head>

<body class="bg-light">

<div class="container mt-5">

    <div class="row justify-content-center">

        <div class="col-md-5">

            <div class="card shadow">

                <div class="card-body">

                    <h2 class="text-center mb-4">
                        Forgot Password
                    </h2>

                    <div class="text-danger text-center mb-3">
                        ${forgotPasswordDto.commonError}
                    </div>

                    <form action="${pageContext.request.contextPath}/forgot-password"
                          method="post">

                        <div class="mb-3">

                            <label class="form-label">
                                Email *
                            </label>

                            <input type="email"
                                   class="form-control"
                                   name="email"
                                   value="${forgotPasswordDto.email}">

                            <div class="invalid-feedback d-block">
                                ${forgotPasswordDto.emailError}
                            </div>

                        </div>

                        <button type="submit"
                                class="btn btn-primary w-100">

                            Send Reset Link

                        </button>

                    </form>

                    <div class="text-center mt-3">

                        <a href="${pageContext.request.contextPath}/login">
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