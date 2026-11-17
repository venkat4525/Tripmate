<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-4">
    <div class="row g-4">
        <div class="col-lg-3">
            <jsp:include page="../common/sidebar.jsp" />
        </div>

        <div class="col-lg-9">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold mb-1"><i class="bi bi-briefcase-fill text-primary me-2"></i> All My Trips</h3>
                    <p class="text-muted small mb-0">Browse and manage your travel itineraries, shared trips, and schedules.</p>
                </div>
                <a href="${pageContext.request.contextPath}/trip-create" class="btn btn-tm-primary px-3 py-2">
                    <i class="bi bi-plus-circle me-1"></i> Plan New Trip
                </a>
            </div>

            <!-- Alerts -->
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

            <!-- Filter Status Pills -->
            <ul class="nav nav-pills mb-4 gap-2 small" id="tripFilterPills" role="tablist">
                <li class="nav-item">
                    <button class="nav-link active rounded-pill px-3 py-1 fw-semibold" id="tab-all" data-bs-toggle="pill" data-bs-target="#pane-all">
                        All Trips (${ownedTrips.size() + sharedTrips.size()})
                    </button>
                </li>
                <li class="nav-item">
                    <button class="nav-link rounded-pill px-3 py-1 fw-semibold" id="tab-owned" data-bs-toggle="pill" data-bs-target="#pane-owned">
                        Created by Me (${ownedTrips.size()})
                    </button>
                </li>
                <li class="nav-item">
                    <button class="nav-link rounded-pill px-3 py-1 fw-semibold" id="tab-shared" data-bs-toggle="pill" data-bs-target="#pane-shared">
                        Shared Collaborations (${sharedTrips.size()})
                    </button>
                </li>
            </ul>

            <div class="tab-content">
                <!-- ALL TRIPS TAB -->
                <div class="tab-pane fade show active" id="pane-all">
                    <c:choose>
                        <c:when test="${empty ownedTrips and empty sharedTrips}">
                            <div class="card tm-card border-0 shadow-sm p-5 text-center">
                                <i class="bi bi-map text-muted display-4 mb-3"></i>
                                <h5 class="fw-bold">No trips planned yet</h5>
                                <p class="text-muted small mb-4">Start creating your first adventure with day-wise planning and expense tracking.</p>
                                <div>
                                    <a href="${pageContext.request.contextPath}/trip-create" class="btn btn-tm-primary px-4 py-2">
                                        <i class="bi bi-plus-circle me-1"></i> Plan a Trip Now
                                    </a>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="row g-4">
                                <c:forEach var="trip" items="${ownedTrips}">
                                    <div class="col-md-6">
                                        <div class="card tm-card h-100 border p-4 d-flex flex-column">
                                            <div class="d-flex justify-content-between align-items-start mb-2">
                                                <h5 class="fw-bold text-dark mb-0">${trip.title}</h5>
                                                <span class="badge ${trip.status == 'PLANNING' ? 'bg-primary' : (trip.status == 'ONGOING' ? 'bg-success' : 'bg-secondary')} rounded-pill" style="font-size: 0.7rem;">
                                                    ${trip.status}
                                                </span>
                                            </div>

                                            <div class="text-muted small mb-3">
                                                <i class="bi bi-geo-alt-fill text-danger me-1"></i> ${trip.destinationName}
                                                <span class="mx-1">•</span>
                                                <i class="bi bi-calendar3 me-1"></i> ${trip.formattedStartDate} - ${trip.formattedEndDate} (${trip.durationDays} days)
                                            </div>

                                            <p class="text-secondary small mb-3 flex-grow-1" style="display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">
                                                ${trip.notes != null && !trip.notes.isEmpty() ? trip.notes : 'No extra notes recorded.'}
                                            </p>

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

                                            <div class="d-flex gap-2 pt-3 border-top mt-auto">
                                                <a href="${pageContext.request.contextPath}/trip-details?id=${trip.id}" class="btn btn-sm btn-tm-primary w-50">
                                                    <i class="bi bi-sliders me-1"></i> Manage
                                                </a>
                                                <a href="${pageContext.request.contextPath}/trip-summary?id=${trip.id}" class="btn btn-sm btn-light border w-50">
                                                    <i class="bi bi-printer me-1"></i> Summary
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>

                                <c:forEach var="strip" items="${sharedTrips}">
                                    <div class="col-md-6">
                                        <div class="card tm-card h-100 border p-4 d-flex flex-column border-info">
                                            <div class="d-flex justify-content-between align-items-start mb-2">
                                                <h5 class="fw-bold text-dark mb-0">${strip.title}</h5>
                                                <span class="badge bg-info text-dark rounded-pill" style="font-size: 0.7rem;">
                                                    COLLABORATOR
                                                </span>
                                            </div>
                                            <div class="text-muted small mb-3">
                                                <i class="bi bi-geo-alt-fill text-danger me-1"></i> ${strip.destinationName}
                                                <span class="mx-1">•</span>
                                                <i class="bi bi-calendar3 me-1"></i> ${strip.formattedStartDate} - ${strip.formattedEndDate}
                                            </div>
                                            <div class="d-flex gap-2 pt-3 border-top mt-auto">
                                                <a href="${pageContext.request.contextPath}/trip-details?id=${strip.id}" class="btn btn-sm btn-outline-primary w-100">
                                                    <i class="bi bi-eye me-1"></i> View Itinerary &amp; Expenses
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- OWNED TRIPS TAB -->
                <div class="tab-pane fade" id="pane-owned">
                    <div class="row g-4">
                        <c:forEach var="trip" items="${ownedTrips}">
                            <div class="col-md-6">
                                <div class="card tm-card h-100 border p-4 d-flex flex-column">
                                    <h5 class="fw-bold text-dark mb-1">${trip.title}</h5>
                                    <div class="text-muted small mb-3"><i class="bi bi-geo-alt text-danger"></i> ${trip.destinationName}</div>
                                    <div class="d-flex gap-2 mt-auto">
                                        <a href="${pageContext.request.contextPath}/trip-details?id=${trip.id}" class="btn btn-sm btn-tm-primary w-100">
                                            Manage Trip
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <!-- SHARED TRIPS TAB -->
                <div class="tab-pane fade" id="pane-shared">
                    <div class="row g-4">
                        <c:forEach var="strip" items="${sharedTrips}">
                            <div class="col-md-6">
                                <div class="card tm-card h-100 border p-4 d-flex flex-column border-info">
                                    <h5 class="fw-bold text-dark mb-1">${strip.title}</h5>
                                    <div class="text-muted small mb-3"><i class="bi bi-geo-alt text-danger"></i> ${strip.destinationName}</div>
                                    <div class="d-flex gap-2 mt-auto">
                                        <a href="${pageContext.request.contextPath}/trip-details?id=${strip.id}" class="btn btn-sm btn-outline-primary w-100">
                                            View Shared Trip
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
