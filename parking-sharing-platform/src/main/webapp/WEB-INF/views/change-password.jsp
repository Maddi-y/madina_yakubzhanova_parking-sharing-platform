<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>

    <title>Change Password</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
          rel="stylesheet">

</head>

<body class="bg-light">

<div class="container mt-5">

    <div class="row justify-content-center">

        <div class="col-md-6">

            <div class="card shadow">

                <div class="card-body">

                    <h2 class="text-center mb-4">
                        Change Password
                    </h2>

                    <div class="text-danger text-center mb-3">
                        ${changePasswordDto.commonError}
                    </div>

                    <form action="${pageContext.request.contextPath}/profile/change-password"
                          method="post">

                        <div class="mb-3">

                            <label class="form-label">
                                Current Password *
                            </label>

                            <input type="password"
                                   class="form-control"
                                   name="currentPassword">

                            <div class="invalid-feedback d-block">
                                ${changePasswordDto.currentPasswordError}
                            </div>

                        </div>

                        <div class="mb-3">

                            <label class="form-label">
                                New Password *
                            </label>

                            <input type="password"
                                   class="form-control"
                                   name="newPassword">

                            <div class="invalid-feedback d-block">
                                ${changePasswordDto.newPasswordError}
                            </div>

                        </div>

                        <div class="mb-3">

                            <label class="form-label">
                                Confirm New Password *
                            </label>

                            <input type="password"
                                   class="form-control"
                                   name="confirmPassword">

                            <div class="invalid-feedback d-block">
                                ${changePasswordDto.confirmPasswordError}
                            </div>

                        </div>

                        <div class="text-muted mb-3">
                            * Required fields
                        </div>

                        <button
                                class="btn btn-warning w-100"
                                type="submit">

                            Change Password

                        </button>

                    </form>

                    <div class="text-center mt-3">

                        <a href="${pageContext.request.contextPath}/profile">

                            Back to Profile

                        </a>

                    </div>

                </div>

            </div>

        </div>

    </div>

</div>

</body>
</html>