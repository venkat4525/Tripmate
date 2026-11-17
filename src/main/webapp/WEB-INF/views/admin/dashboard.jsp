<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-4">
    <div class="row g-4">
        <!-- Sidebar -->
        <div class="col-lg-3">
            <jsp:include page="../common/sidebar.jsp" />
        </div>

        <!-- Main Content -->
        <div class="col-lg-9">
            <div class="p-4 rounded-4 text-white mb-4 shadow-sm" style="background: linear-gradient(135deg, #1e1b4b 0%, #312e81 100%);">
                <span class="badge bg-warning text-dark rounded-pill px-3 py-1 fw-bold small mb-2">ADMINISTRATOR CONTROL PANEL</span>
                <h3 class="fw-bold mb-1">System Overview &amp; Analytics</h3>
                <p class="text-light text-opacity-75 mb-0 small">Monitor registered travelers, active trips, destinations, and system metrics.</p>
            </div>

            <!-- 4 Admin Stat Counters -->
            <div class="row g-3 mb-4">
                <div class="col-md-3 col-6">
                    <div class="stat-widget">
                        <div class="stat-icon bg-primary bg-opacity-10 text-primary">
                            <i class="bi bi-people-fill"></i>
                        </div>
                        <div>
                            <div class="text-muted small">Total Users</div>
                            <h4 class="fw-bold mb-0">${totalUsers}</h4>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="stat-widget">
                        <div class="stat-icon bg-success bg-opacity-10 text-success">
                            <i class="bi bi-pin-map-fill"></i>
                        </div>
                        <div>
                            <div class="text-muted small">Destinations</div>
                            <h4 class="fw-bold mb-0">${totalDestinations}</h4>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="stat-widget">
                        <div class="stat-icon bg-info bg-opacity-10 text-info">
                            <i class="bi bi-briefcase-fill"></i>
                        </div>
                        <div>
                            <div class="text-muted small">Total Trips</div>
                            <h4 class="fw-bold mb-0">${totalTrips}</h4>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="stat-widget">
                        <div class="stat-icon bg-warning bg-opacity-10 text-warning">
                            <i class="bi bi-tags-fill"></i>
                        </div>
                        <div>
                            <div class="text-muted small">Categories</div>
                            <h4 class="fw-bold mb-0">${totalCategories}</h4>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Recent Trips Table -->
            <div class="card tm-card border-0 shadow-sm p-4 mb-4">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="fw-bold mb-0">System Trips Ledger</h5>
                    <a href="${pageContext.request.contextPath}/destinations" class="btn btn-sm btn-outline-primary">Explore Destinations</a>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover align-middle small mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>Trip Title</th>
                                <th>Destination</th>
                                <th>Dates</th>
                                <th>Budget</th>
                                <th>Status</th>
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="t" items="${recentTrips}">
                                <tr>
                                    <td class="fw-bold text-dark">${t.title}</td>
                                    <td>${t.destinationName}</td>
                                    <td>${t.formattedStartDate} - ${t.formattedEndDate}</td>
                                    <td>$<fmt:formatNumber value="${t.estimatedBudget}" pattern="#,##0.00"/></td>
                                    <td><span class="badge bg-secondary">${t.status}</span></td>
                                    <td class="text-end">
                                        <a href="${pageContext.request.contextPath}/trip-details?id=${t.id}" class="btn btn-sm btn-light border">
                                            View
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Registered Users Table -->
            <div class="card tm-card border-0 shadow-sm p-4">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="fw-bold mb-0">Registered User Accounts</h5>
                    <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-sm btn-outline-secondary">Manage All Users</a>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover align-middle small mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>User</th>
                                <th>Email</th>
                                <th>Role</th>
                                <th>Status</th>
                                <th class="text-end">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${recentUsers}">
                                <tr>
                                    <td class="fw-semibold text-dark">${u.fullName}</td>
                                    <td>${u.email}</td>
                                    <td>
                                        <span class="badge ${u.admin ? 'bg-warning text-dark' : 'bg-primary'}">${u.role}</span>
                                    </td>
                                    <td>
                                        <span class="badge ${u.active ? 'bg-success' : 'bg-danger'}">
                                            ${u.active ? 'Active' : 'Inactive'}
                                        </span>
                                    </td>
                                    <td class="text-end">
                                        <a href="${pageContext.request.contextPath}/admin/user-toggle?id=${u.id}" class="btn btn-sm btn-outline-secondary py-0 px-2">
                                            ${u.active ? 'Deactivate' : 'Activate'}
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
