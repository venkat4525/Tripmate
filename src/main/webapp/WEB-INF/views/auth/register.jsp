<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account - TripMate Smart Travel</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <!-- Custom Theme CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="bg-light">

    <!-- Top Navigation -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom py-3">
        <div class="container">
            <a class="navbar-brand d-flex align-items-center gap-2 fw-bold text-dark" href="${pageContext.request.contextPath}/">
                <i class="bi bi-compass text-primary fs-3"></i>
                <span style="letter-spacing: -0.5px;">TripMate</span>
            </a>
            <div class="ms-auto">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-sm btn-outline-primary rounded-pill px-3">
                    Already have an account? Sign In
                </a>
            </div>
        </div>
    </nav>

    <!-- Auth Container -->
    <div class="auth-wrapper">
        <div class="auth-card" style="max-width: 520px;">
            <div class="text-center mb-4">
                <div class="d-inline-flex align-items-center justify-content-center bg-primary bg-opacity-10 text-primary rounded-circle mb-3" style="width: 60px; height: 60px;">
                    <i class="bi bi-person-plus-fill fs-2"></i>
                </div>
                <h3 class="fw-bold mb-1">Create Your Account</h3>
                <p class="text-muted small">Join TripMate to start planning trips, sharing itineraries &amp; tracking budgets</p>
            </div>

            <!-- Error Alerts -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show d-flex align-items-center py-2 px-3 small" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2 fs-6"></i>
                    <div>${errorMessage}</div>
                    <button type="button" class="btn-close ms-auto p-2" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Registration Form -->
            <form action="${pageContext.request.contextPath}/register" method="post" id="regForm">
                <div class="mb-3">
                    <label for="fullName" class="form-label small fw-semibold text-secondary">Full Name <span class="text-danger">*</span></label>
                    <div class="input-group">
                        <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-person"></i></span>
                        <input type="text" class="form-control border-start-0" id="fullName" name="fullName" 
                               value="${fullName != null ? fullName : ''}" placeholder="e.g. Alex Morgan" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="email" class="form-label small fw-semibold text-secondary">Email Address <span class="text-danger">*</span></label>
                    <div class="input-group">
                        <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-envelope"></i></span>
                        <input type="email" class="form-control border-start-0" id="email" name="email" 
                               value="${email != null ? email : ''}" placeholder="name@example.com" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="phone" class="form-label small fw-semibold text-secondary">Phone Number</label>
                    <div class="input-group">
                        <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-telephone"></i></span>
                        <input type="text" class="form-control border-start-0" id="phone" name="phone" 
                               value="${phone != null ? phone : ''}" placeholder="+1 555-0123">
                    </div>
                </div>

                <div class="row g-2 mb-3">
                    <div class="col-sm-6">
                        <label for="password" class="form-label small fw-semibold text-secondary">Password <span class="text-danger">*</span></label>
                        <div class="input-group">
                            <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-key"></i></span>
                            <input type="password" class="form-control border-start-0" id="password" name="password" 
                                   placeholder="Min 6 chars" required minlength="6">
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <label for="confirmPassword" class="form-label small fw-semibold text-secondary">Confirm Password <span class="text-danger">*</span></label>
                        <div class="input-group">
                            <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-key-fill"></i></span>
                            <input type="password" class="form-control border-start-0" id="confirmPassword" name="confirmPassword" 
                                   placeholder="Re-type password" required>
                        </div>
                    </div>
                </div>

                <div class="form-check mb-4 small">
                    <input class="form-check-input" type="checkbox" id="agreeTerms" required checked>
                    <label class="form-check-label text-muted" for="agreeTerms">
                        I agree to the TripMate <a href="#" class="text-decoration-none">Terms of Service</a> and <a href="#" class="text-decoration-none">Privacy Policy</a>
                    </label>
                </div>

                <button type="submit" class="btn btn-tm-primary w-100 py-2 mb-3">
                    <i class="bi bi-check2-circle me-1"></i> Register Account
                </button>
            </form>

            <div class="text-center mt-3 text-muted small">
                Already registered? 
                <a href="${pageContext.request.contextPath}/login" class="text-primary fw-semibold text-decoration-none">Sign in here</a>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.getElementById('regForm').addEventListener('submit', function (e) {
            const p1 = document.getElementById('password').value;
            const p2 = document.getElementById('confirmPassword').value;
            if (p1 !== p2) {
                e.preventDefault();
                alert('Passwords do not match. Please re-enter.');
            }
        });
    </script>
</body>
</html>
