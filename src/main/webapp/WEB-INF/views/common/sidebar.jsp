<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="card tm-card border-0 shadow-sm p-3 mb-4">
    <!-- User Quick Badge -->
    <div class="d-flex align-items-center gap-3 p-2 mb-3 bg-light rounded-3 border">
        <div class="rounded-circle bg-primary bg-opacity-10 text-primary d-flex align-items-center justify-content-center fw-bold" style="width: 48px; height: 48px; font-size: 1.2rem;">
            ${sessionScope.currentUser.fullName.substring(0, 1).toUpperCase()}
        </div>
        <div class="overflow-hidden">
            <h6 class="fw-bold mb-0 text-truncate">${sessionScope.currentUser.fullName}</h6>
            <span class="badge ${sessionScope.currentUser.admin ? 'bg-warning text-dark' : 'bg-primary'} rounded-pill" style="font-size: 0.65rem;">
                ${sessionScope.currentUser.role}
            </span>
        </div>
    </div>

    <!-- Navigation List -->
    <div class="nav flex-column nav-pills gap-1">
        <a class="nav-link py-2 px-3 fw-semibold ${activeSidebar == 'dashboard' ? 'active bg-primary' : 'text-secondary'}" 
           href="${pageContext.request.contextPath}/dashboard">
            <i class="bi bi-speedometer2 me-2"></i> Dashboard Overview
        </a>
        <a class="nav-link py-2 px-3 fw-semibold ${activeSidebar == 'trips' ? 'active bg-primary' : 'text-secondary'}" 
           href="${pageContext.request.contextPath}/trips">
            <i class="bi bi-briefcase me-2"></i> My Trips
        </a>
        <a class="nav-link py-2 px-3 fw-semibold ${activeSidebar == 'trip-create' ? 'active bg-primary' : 'text-secondary'}" 
           href="${pageContext.request.contextPath}/trip-create">
            <i class="bi bi-plus-circle me-2"></i> Plan New Trip
        </a>
        <a class="nav-link py-2 px-3 fw-semibold ${activeSidebar == 'favourites' ? 'active bg-primary' : 'text-secondary'}" 
           href="${pageContext.request.contextPath}/favourites">
            <i class="bi bi-heart me-2"></i> Saved Spots
        </a>
        <a class="nav-link py-2 px-3 fw-semibold ${activeSidebar == 'destinations' ? 'active bg-primary' : 'text-secondary'}" 
           href="${pageContext.request.contextPath}/destinations">
            <i class="bi bi-compass me-2"></i> Explore Destinations
        </a>

        <c:if test="${sessionScope.currentUser.admin}">
            <div class="text-uppercase text-muted fw-bold mt-3 mb-1 px-3" style="font-size: 0.7rem; letter-spacing: 0.5px;">
                Admin Controls
            </div>
            <a class="nav-link py-2 px-3 fw-semibold ${activeSidebar == 'admin-dashboard' ? 'active bg-warning text-dark' : 'text-warning-emphasis'}" 
               href="${pageContext.request.contextPath}/admin/dashboard">
                <i class="bi bi-speedometer me-2"></i> Admin Analytics
            </a>
            <a class="nav-link py-2 px-3 fw-semibold ${activeSidebar == 'admin-destinations' ? 'active bg-warning text-dark' : 'text-warning-emphasis'}" 
               href="${pageContext.request.contextPath}/admin/destinations">
                <i class="bi bi-pin-map me-2"></i> Manage Destinations
            </a>
            <a class="nav-link py-2 px-3 fw-semibold ${activeSidebar == 'admin-categories' ? 'active bg-warning text-dark' : 'text-warning-emphasis'}" 
               href="${pageContext.request.contextPath}/admin/categories">
                <i class="bi bi-tags me-2"></i> Manage Categories
            </a>
            <a class="nav-link py-2 px-3 fw-semibold ${activeSidebar == 'admin-users' ? 'active bg-warning text-dark' : 'text-warning-emphasis'}" 
               href="${pageContext.request.contextPath}/admin/users">
                <i class="bi bi-people me-2"></i> Manage Users
            </a>
        </c:if>

        <hr class="my-2">
        <a class="nav-link py-2 px-3 fw-semibold text-danger" href="${pageContext.request.contextPath}/logout">
            <i class="bi bi-box-arrow-right me-2"></i> Log Out
        </a>
    </div>
</div>
