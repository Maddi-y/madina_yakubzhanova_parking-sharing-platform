<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
          rel="stylesheet">
          <title>Register</title>
</head>

<body class="bg-light">

<div class="container mt-5">

<div class="row justify-content-center">

    <div class="col-md-6">

        <div class="card shadow">

            <div class="card-body">

                <h2 class="text-center mb-4">
                    Registration
                </h2>

                <form action="${pageContext.request.contextPath}/register"
                      method="post">

                    <div class="mb-3">
                        <label class="form-label">
                            Name
                        </label>

                        <input type="text"
                               class="form-control"
                               name="name"
                               value="${registrationDto.name}">

                     <div class="invalid-feedback d-block">
                         ${registrationDto.nameError}
                     </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">
                            Email
                        </label>

                        <input type="email"
                               class="form-control"
                               name="email"
                               value="${registrationDto.email}">

                        <div class="invalid-feedback d-block">
                            ${registrationDto.emailError}
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">
                            Phone
                        </label>

                        <input type="text"
                               class="form-control"
                               name="phone"
                               value="${registrationDto.phone}">

                        <div class="invalid-feedback d-block">
                            ${registrationDto.phoneError}
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">
                            Password
                        </label>

                        <input type="password"
                               class="form-control"
                               name="password">

                        <div class="invalid-feedback d-block">
                            ${registrationDto.passwordError}
                        </div>
                    </div>

                    <button type="submit"
                            class="btn btn-primary w-100">
                        Register
                    </button>

                </form>

                <div class="text-center mt-3">
                    <a href="${pageContext.request.contextPath}/login">
                        Already have an account? Login
                    </a>
                </div>

            </div>

        </div>

    </div>

</div>

</div>

</body>

</html>