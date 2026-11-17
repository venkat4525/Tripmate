<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - TripMate Smart Travel</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <!-- Custom Theme CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="bg-light">

    <!-- Simple Top Nav -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom py-3">
        <div class="container">
            <a class="navbar-brand d-flex align-items-center gap-2 fw-bold text-dark" href="${pageContext.request.contextPath}/">
                <i class="bi bi-compass text-primary fs-3"></i>
                <span style="letter-spacing: -0.5px;">TripMate</span>
            </a>
            <div class="ms-auto">
                <a href="${pageContext.request.contextPath}/" class="text-decoration-none text-secondary me-3">
                    <i class="bi bi-house me-1"></i> Home
                </a>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-sm btn-outline-primary rounded-pill px-3">
                    Create Account
                </a>
            </div>
        </div>
    </nav>

    <!-- Auth Container -->
    <div class="auth-wrapper">
        <div class="auth-card">
            <div class="text-center mb-4">
                <div class="d-inline-flex align-items-center justify-content-center bg-primary bg-opacity-10 text-primary rounded-circle mb-3" style="width: 60px; height: 60px;">
                    <i class="bi bi-shield-lock-fill fs-2"></i>
                </div>
                <h3 class="fw-bold mb-1">Welcome Back</h3>
                <p class="text-muted small">Sign in to manage your trips, itineraries &amp; budgets</p>
            </div>

            <!-- Error Alerts -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show d-flex align-items-center py-2 px-3 small" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2 fs-6"></i>
                    <div>${errorMessage}</div>
                    <button type="button" class="btn-close ms-auto p-2" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            <c:if test="${not empty param.error}">
                <div class="alert alert-danger alert-dismissible fade show d-flex align-items-center py-2 px-3 small" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2 fs-6"></i>
                    <div>${param.error}</div>
                    <button type="button" class="btn-close ms-auto p-2" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            <c:if test="${not empty param.success}">
                <div class="alert alert-success alert-dismissible fade show d-flex align-items-center py-2 px-3 small" role="alert">
                    <i class="bi bi-check-circle-fill me-2 fs-6"></i>
                    <div>${param.success}</div>
                    <button type="button" class="btn-close ms-auto p-2" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Login Form -->
            <form action="${pageContext.request.contextPath}/login" method="post" class="needs-validation" novalidate>
                <div class="mb-3">
                    <label for="email" class="form-label small fw-semibold text-secondary">Email Address</label>
                    <div class="input-group">
                        <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-envelope"></i></span>
                        <input type="email" class="form-control border-start-0" id="email" name="email" 
                               value="${email != null ? email : ''}" placeholder="name@example.com" required>
                    </div>
                </div>

                <div class="mb-4">
                    <div class="d-flex justify-content-between">
                        <label for="password" class="form-label small fw-semibold text-secondary">Password</label>
                    </div>
                    <div class="input-group">
                        <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-key"></i></span>
                        <input type="password" class="form-control border-start-0" id="password" name="password" 
                               placeholder="••••••••" required>
                        <button class="btn btn-outline-secondary" type="button" id="togglePassword">
                            <i class="bi bi-eye"></i>
                        </button>
                    </div>
                </div>

                <button type="submit" class="btn btn-tm-primary w-100 py-2 mb-3">
                    <i class="bi bi-box-arrow-in-right me-1"></i> Sign In
                </button>
            </form>

            <!-- Quick Demo Credentials Fill -->
            <div class="p-3 bg-light rounded-3 border mt-4">
                <div class="small fw-bold text-dark mb-2"><i class="bi bi-lightning-charge-fill text-warning me-1"></i> Quick Demo Accounts:</div>
                <div class="d-flex gap-2">
                    <button type="button" class="btn btn-sm btn-outline-dark w-50 py-1" onclick="fillCredentials('admin@tripmate.com', 'Admin@123')">
                        <i class="bi bi-shield-check me-1"></i> Admin
                    </button>
                    <button type="button" class="btn btn-sm btn-outline-secondary w-50 py-1" onclick="fillCredentials('john.doe@example.com', 'User@123')">
                        <i class="bi bi-person me-1"></i> User
                    </button>
                </div>
            </div>

            <div class="text-center mt-4 text-muted small">
                Don't have an account? 
                <a href="${pageContext.request.contextPath}/register" class="text-primary fw-semibold text-decoration-none">Register here</a>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function fillCredentials(email, pwd) {
            document.getElementById('email').value = email;
            document.getElementById('password').value = pwd;
        }

        const toggleBtn = document.getElementById('togglePassword');
        const pwdInput = document.getElementById('password');
        toggleBtn.addEventListener('click', () => {
            const isPassword = pwdInput.type === 'password';
            pwdInput.type = isPassword ? 'text' : 'password';
            toggleBtn.innerHTML = isPassword ? '<i class="bi bi-eye-slash"></i>' : '<i class="bi bi-eye"></i>';
        });
    </script>
</body>
</html>
