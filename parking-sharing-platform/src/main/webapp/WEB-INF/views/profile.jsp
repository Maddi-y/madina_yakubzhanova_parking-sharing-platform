<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>

    <title>My Profile</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
          rel="stylesheet">

</head>

<body class="bg-light">

<div class="container mt-5">

    <div class="row justify-content-center">

        <div class="col-md-8">

            <div class="card shadow">

                <div class="card-body">

                    <h2 class="text-center mb-4">
                        My Profile
                    </h2>

                    <table class="table">

                        <tr>
                            <th>ID</th>
                            <td>${user.userId}</td>
                        </tr>

                        <tr>
                            <th>Name</th>
                            <td>${user.name}</td>
                        </tr>

                        <tr>
                            <th>Email</th>
                            <td>${user.email}</td>
                        </tr>

                        <tr>
                            <th>Phone</th>
                            <td>${user.phone}</td>
                        </tr>

                        <tr>
                            <th>Role</th>
                            <td>${user.role}</td>
                        </tr>

                        <tr>
                            <th>Status</th>
                            <td>${user.status}</td>
                        </tr>

                        <tr>
                            <th>Created</th>
                            <td>${user.createdAt}</td>
                        </tr>

                    </table>

                    <div class="d-grid gap-2">

                       <a href="${pageContext.request.contextPath}/profile/edit"
                          class="btn btn-primary">
                           Edit Profile
                       </a>

                        <a href="${pageContext.request.contextPath}/profile/change-password"
                           class="btn btn-warning">
                            Change Password
                        </a>

                        <a href="${pageContext.request.contextPath}/"
                           class="btn btn-secondary">

                            Back Home

                        </a>

                    </div>

                </div>

            </div>

        </div>

    </div>

</div>

</body>
</html>