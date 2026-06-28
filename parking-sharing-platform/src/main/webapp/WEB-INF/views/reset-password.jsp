<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>

    <title>Reset Password</title>

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
                        Reset Password
                    </h2>

                    <div class="text-danger text-center mb-3">
                        ${resetPasswordDto.commonError}
                    </div>

                    <form action="${pageContext.request.contextPath}/reset-password"
                          method="post">

                        <input type="hidden"
                               name="token"
                               value="${resetPasswordDto.token}">

                        <div class="mb-3">

                            <label class="form-label">
                                New Password *
                            </label>

                            <input type="password"
                                   class="form-control"
                                   name="newPassword">

                            <div class="invalid-feedback d-block">
                                ${resetPasswordDto.newPasswordError}
                            </div>

                        </div>

                        <div class="mb-3">

                            <label class="form-label">
                                Confirm Password *
                            </label>

                            <input type="password"
                                   class="form-control"
                                   name="confirmPassword">

                            <div class="invalid-feedback d-block">
                                ${resetPasswordDto.confirmPasswordError}
                            </div>

                        </div>

                        <button type="submit"
                                class="btn btn-success w-100">

                            Reset Password

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