<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="navbar navbar-expand-lg tm-navbar sticky-top bg-white border-bottom shadow-sm py-2">
    <div class="container">
        <!-- Brand Logo -->
        <a class="navbar-brand tm-brand d-flex align-items-center gap-2 fw-extrabold text-dark fs-4" href="${pageContext.request.contextPath}/">
            <span class="rounded-circle bg-primary bg-opacity-10 text-primary d-inline-flex align-items-center justify-content-center" style="width: 36px; height: 36px;">
                <i class="bi bi-compass-fill fs-5"></i>
            </span>
            <span style="letter-spacing: -0.5px;">TripMate</span>
        </a>

        <!-- Mobile Toggle Button -->
        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#tmNavbarCollapse">
            <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Navbar Links -->
        <div class="collapse navbar-collapse" id="tmNavbarCollapse">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0 ms-lg-4 gap-lg-2">
                <li class="nav-item">
                    <a class="nav-link fw-semibold px-3 d-flex align-items-center gap-1 ${activeNav == 'home' ? 'text-primary active' : 'text-secondary'}" 
                       href="${pageContext.request.contextPath}/">
                        <i class="bi bi-house-door"></i> Home
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link fw-semibold px-3 d-flex align-items-center gap-1 ${activeNav == 'destinations' ? 'text-primary active' : 'text-secondary'}" 
                       href="${pageContext.request.contextPath}/destinations">
                        <i class="bi bi-geo-alt"></i> Destinations
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link fw-semibold px-3 d-flex align-items-center gap-1 text-secondary" 
                       href="${pageContext.request.contextPath}/#themes">
                        <i class="bi bi-info-circle"></i> About
                    </a>
                </li>
                <c:if test="${not empty sessionScope.currentUser}">
                    <li class="nav-item">
                        <a class="nav-link fw-semibold px-3 d-flex align-items-center gap-1 ${activeNav == 'trips' ? 'text-primary active' : 'text-secondary'}" 
                           href="${pageContext.request.contextPath}/trips">
                            <i class="bi bi-briefcase"></i> My Trips
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link fw-semibold px-3 d-flex align-items-center gap-1 ${activeNav == 'favourites' ? 'text-primary active' : 'text-secondary'}" 
                           href="${pageContext.request.contextPath}/favourites">
                            <i class="bi bi-heart"></i> Saved Spots
                        </a>
                    </li>
                </c:if>
            </ul>

            <!-- Right-side Auth Controls -->
            <div class="d-flex align-items-center gap-3">
                <c:choose>
                    <c:when test="${not empty sessionScope.currentUser}">
                        <div class="dropdown">
                            <button class="btn btn-light d-flex align-items-center gap-2 rounded-pill px-3 py-1 border shadow-sm dropdown-toggle" 
                                    type="button" data-bs-toggle="dropdown">
                                <i class="bi bi-person-circle fs-5 text-primary"></i>
                                <span class="fw-semibold small">${sessionScope.currentUser.fullName}</span>
                                <span class="badge ${sessionScope.currentUser.admin ? 'bg-warning text-dark' : 'bg-primary'} rounded-pill" style="font-size: 0.65rem;">
                                    ${sessionScope.currentUser.role}
                                </span>
                            </button>
                            <ul class="dropdown-menu dropdown-menu-end border shadow-sm rounded-3 mt-2">
                                <li>
                                    <div class="px-3 py-2 border-bottom">
                                        <div class="fw-bold small text-dark">${sessionScope.currentUser.fullName}</div>
                                        <div class="text-muted" style="font-size: 0.75rem;">${sessionScope.currentUser.email}</div>
                                    </div>
                                </li>
                                <li>
                                    <a class="dropdown-item py-2 small" href="${pageContext.request.contextPath}/dashboard">
                                        <i class="bi bi-speedometer2 me-2 text-primary"></i> My Dashboard
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item py-2 small" href="${pageContext.request.contextPath}/trips">
                                        <i class="bi bi-map me-2 text-primary"></i> Planned Trips
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item py-2 small" href="${pageContext.request.contextPath}/favourites">
                                        <i class="bi bi-bookmark-heart me-2 text-primary"></i> Saved Spots
                                    </a>
                                </li>
                                <c:if test="${sessionScope.currentUser.admin}">
                                    <li><hr class="dropdown-divider"></li>
                                    <li>
                                        <a class="dropdown-item py-2 small text-warning-emphasis" href="${pageContext.request.contextPath}/admin/dashboard">
                                            <i class="bi bi-shield-lock-fill me-2 text-warning"></i> Admin Panel
                                        </a>
                                    </li>
                                </c:if>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item py-2 small text-danger" href="${pageContext.request.contextPath}/logout">
                                        <i class="bi bi-box-arrow-right me-2"></i> Log Out
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login" class="text-decoration-none fw-semibold text-dark d-flex align-items-center gap-1 small">
                            <i class="bi bi-box-arrow-in-right fs-5 text-secondary"></i> Log In
                        </a>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-tm-primary px-3 py-2 rounded-3 small fw-semibold shadow-sm">
                            Register Free
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>
