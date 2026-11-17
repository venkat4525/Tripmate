<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-4">
    <!-- Top Welcome Banner -->
    <div class="p-4 p-md-5 mb-4 rounded-4 text-white position-relative shadow-sm" style="background: linear-gradient(135deg, #0f172a 0%, #312e81 100%);">
        <div class="row align-items-center">
            <div class="col-lg-8">
                <span class="badge bg-white bg-opacity-20 text-white rounded-pill px-3 py-1 small mb-2">TRAVELER DASHBOARD</span>
                <h2 class="fw-bold mb-2">Welcome back, ${sessionScope.currentUser.fullName}! 👋</h2>
                <p class="text-light text-opacity-75 mb-0">
                    Track your adventures, manage day-wise itineraries, split group expenses, and pack with ease.
                </p>
            </div>
            <div class="col-lg-4 text-lg-end mt-3 mt-lg-0">
                <a href="${pageContext.request.contextPath}/trip-create" class="btn btn-light fw-bold px-4 py-2 rounded-pill shadow-sm">
                    <i class="bi bi-plus-circle text-primary me-1"></i> Plan New Trip
                </a>
            </div>
        </div>
    </div>

    <!-- Alert Messages -->
    <c:if test="${not empty param.success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i> ${param.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i> ${param.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="row g-4">
        <!-- Sidebar Navigation -->
        <div class="col-lg-3">
            <jsp:include page="../common/sidebar.jsp" />

            <!-- Notification Drawer -->
            <div class="card tm-card border-0 shadow-sm p-3">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h6 class="fw-bold text-dark mb-0"><i class="bi bi-bell-fill text-warning me-1"></i> Notifications</h6>
                    <c:if test="${unreadCount > 0}">
                        <span class="badge bg-danger rounded-pill">${unreadCount} new</span>
                    </c:if>
                </div>
                <c:choose>
                    <c:when test="${empty notifications}">
                        <p class="text-muted small mb-0">No new notifications.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="d-flex flex-column gap-2">
                            <c:forEach var="notif" items="${notifications}">
                                <div class="p-2 rounded-2 ${notif.read ? 'bg-light' : 'bg-primary bg-opacity-10 border border-primary border-opacity-25'} small">
                                    <div class="fw-semibold text-dark">${notif.title}</div>
                                    <div class="text-muted" style="font-size: 0.75rem;">${notif.message}</div>
                                    <div class="text-secondary mt-1" style="font-size: 0.7rem;">${notif.formattedCreatedAt}</div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Main Dashboard Content -->
        <div class="col-lg-9">
            <!-- 4 Stat Counters -->
            <div class="row g-3 mb-4">
                <div class="col-md-3 col-6">
                    <div class="stat-widget">
                        <div class="stat-icon bg-primary bg-opacity-10 text-primary">
                            <i class="bi bi-briefcase-fill"></i>
                        </div>
                        <div>
                            <div class="text-muted small">Total Trips</div>
                            <h4 class="fw-bold mb-0 text-dark">${totalTripsCount}</h4>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="stat-widget">
                        <div class="stat-icon bg-success bg-opacity-10 text-success">
                            <i class="bi bi-hourglass-split"></i>
                        </div>
                        <div>
                            <div class="text-muted small">Active Plans</div>
                            <h4 class="fw-bold mb-0 text-dark">${activeTripsCount}</h4>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="stat-widget">
                        <div class="stat-icon bg-info bg-opacity-10 text-info">
                            <i class="bi bi-wallet2"></i>
                        </div>
                        <div>
                            <div class="text-muted small">Expenses Logged</div>
                            <h4 class="fw-bold mb-0 text-dark">$<fmt:formatNumber value="${totalExpenses}" pattern="#,##0"/></h4>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="stat-widget">
                        <div class="stat-icon bg-danger bg-opacity-10 text-danger">
                            <i class="bi bi-heart-fill"></i>
                        </div>
                        <div>
                            <div class="text-muted small">Saved Spots</div>
                            <h4 class="fw-bold mb-0 text-dark">${favouritesCount}</h4>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Planned Trips -->
            <div class="card tm-card border-0 shadow-sm p-4 mb-4">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <div>
                        <h5 class="fw-bold text-dark mb-1">My Managed Trips</h5>
                        <p class="text-muted small mb-0">Trips created and owned by you</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/trip-create" class="btn btn-sm btn-tm-primary px-3">
                        <i class="bi bi-plus-lg me-1"></i> Create Trip
                    </a>
                </div>

                <c:choose>
                    <c:when test="${empty ownedTrips}">
                        <div class="text-center py-4 bg-light rounded-3 border">
                            <i class="bi bi-map text-muted display-5 mb-2"></i>
                            <h6 class="fw-bold text-secondary">You haven't created any trips yet</h6>
                            <p class="text-muted small mb-3">Start your journey by creating a personalized travel plan.</p>
                            <a href="${pageContext.request.contextPath}/trip-create" class="btn btn-sm btn-primary">
                                <i class="bi bi-plus-circle me-1"></i> Plan First Trip
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="row g-3">
                            <c:forEach var="trip" items="${ownedTrips}">
                                <div class="col-md-6">
                                    <div class="card tm-card border p-3 h-100">
                                        <div class="d-flex justify-content-between align-items-start mb-2">
                                            <h6 class="fw-bold text-dark mb-0">${trip.title}</h6>
                                            <span class="badge ${trip.status == 'PLANNING' ? 'bg-primary' : (trip.status == 'ONGOING' ? 'bg-success' : 'bg-secondary')} rounded-pill" style="font-size: 0.65rem;">
                                                ${trip.status}
                                            </span>
                                        </div>

                                        <div class="text-muted small mb-3">
                                            <i class="bi bi-geo-alt-fill text-danger me-1"></i> ${trip.destinationName}
                                            <span class="mx-1">•</span>
                                            <i class="bi bi-calendar3 me-1"></i> ${trip.formattedStartDate} - ${trip.formattedEndDate} (${trip.durationDays} days)
                                        </div>

                                        <!-- Budget Bar -->
                                        <div class="mb-3">
                                            <div class="d-flex justify-content-between small text-muted mb-1" style="font-size: 0.75rem;">
                                                <span>Spent: $<fmt:formatNumber value="${trip.actualExpenses}" pattern="#,##0.00"/></span>
                                                <span>Budget: $<fmt:formatNumber value="${trip.estimatedBudget}" pattern="#,##0.00"/></span>
                                            </div>
                                            <div class="progress" style="height: 6px;">
                                                <div class="progress-bar ${trip.budgetUsagePercentage > 90 ? 'bg-danger' : 'bg-primary'}" 
                                                     role="progressbar" style="width: ${trip.budgetUsagePercentage}%;"></div>
                                            </div>
                                        </div>

                                        <!-- Action Buttons -->
                                        <div class="d-flex gap-2 pt-2 border-top mt-auto">
                                            <a href="${pageContext.request.contextPath}/trip-details?id=${trip.id}" class="btn btn-sm btn-outline-primary w-50">
                                                <i class="bi bi-sliders me-1"></i> Manage
                                            </a>
                                            <a href="${pageContext.request.contextPath}/trip-summary?id=${trip.id}" class="btn btn-sm btn-light border w-50">
                                                <i class="bi bi-printer me-1"></i> Summary
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Shared / Collaborator Trips -->
            <c:if test="${not empty sharedTrips}">
                <div class="card tm-card border-0 shadow-sm p-4">
                    <div class="mb-3">
                        <h5 class="fw-bold text-dark mb-1"><i class="bi bi-people-fill text-primary me-2"></i> Shared Trips (Collaborations)</h5>
                        <p class="text-muted small mb-0">Trips friends have invited you to collaborate on</p>
                    </div>

                    <div class="row g-3">
                        <c:forEach var="strip" items="${sharedTrips}">
                            <div class="col-md-6">
                                <div class="card tm-card border p-3">
                                    <div class="d-flex justify-content-between align-items-start mb-2">
                                        <h6 class="fw-bold text-dark mb-0">${strip.title}</h6>
                                        <span class="badge bg-info text-dark rounded-pill" style="font-size: 0.65rem;">COLLABORATOR</span>
                                    </div>
                                    <div class="text-muted small mb-3">
                                        <i class="bi bi-geo-alt me-1 text-danger"></i> ${strip.destinationName}
                                    </div>
                                    <a href="${pageContext.request.contextPath}/trip-details?id=${strip.id}" class="btn btn-sm btn-outline-primary">
                                        <i class="bi bi-eye me-1"></i> View Itinerary &amp; Expenses
                                    </a>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
