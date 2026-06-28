
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>

    <title>Edit Profile</title>

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
                        Edit Profile
                    </h2>

                    <div class="text-danger text-center mb-3">
                        ${profileDto.commonError}
                    </div>

                    <form action="${pageContext.request.contextPath}/profile/edit"
                          method="post">

                        <div class="mb-3">

                            <label class="form-label">
                                Name *
                            </label>

                            <input type="text"
                                   class="form-control"
                                   name="name"
                                   value="${profileDto.name}">

                            <div class="invalid-feedback d-block">
                                ${profileDto.nameError}
                            </div>

                        </div>

                        <div class="mb-3">

                            <label class="form-label">
                                Email *
                            </label>

                            <input type="email"
                                   class="form-control"
                                   name="email"
                                   value="${profileDto.email}">

                            <div class="invalid-feedback d-block">
                                ${profileDto.emailError}
                            </div>

                        </div>

                        <div class="mb-3">

                            <label class="form-label">
                                Phone *
                            </label>

                            <input type="text"
                                   class="form-control"
                                   name="phone"
                                   value="${profileDto.phone}">

                            <div class="invalid-feedback d-block">
                                ${profileDto.phoneError}
                            </div>

                        </div>

                        <div class="text-muted mb-3">
                            * Required fields
                        </div>

                        <button type="submit"
                                class="btn btn-success w-100">
                            Save Changes
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

