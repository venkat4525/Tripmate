<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-4">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <!-- Breadcrumb -->
            <nav aria-label="breadcrumb" class="mb-3">
                <ol class="breadcrumb small">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard" class="text-decoration-none">Dashboard</a></li>
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/trips" class="text-decoration-none">Trips</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Plan New Trip</li>
                </ol>
            </nav>

            <div class="card tm-card border-0 shadow-sm p-4 p-md-5">
                <div class="d-flex align-items-center gap-3 mb-4 border-bottom pb-3">
                    <div class="stat-icon bg-primary bg-opacity-10 text-primary rounded-3">
                        <i class="bi bi-calendar-plus fs-3"></i>
                    </div>
                    <div>
                        <h3 class="fw-bold text-dark mb-0">Plan a New Adventure</h3>
                        <p class="text-muted small mb-0">Fill in the details below to initialize your trip itinerary and budget.</p>
                    </div>
                </div>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show small" role="alert">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i> ${errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/trip-create" method="post" id="createTripForm">
                    <div class="mb-3">
                        <label for="title" class="form-label small fw-semibold text-secondary">Trip Name / Title <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="title" name="title" placeholder="e.g. Bali Summer Beach Vacation 2026" required>
                    </div>

                    <div class="mb-3">
                        <label for="destinationId" class="form-label small fw-semibold text-secondary">Destination <span class="text-danger">*</span></label>
                        <select class="form-select" id="destinationId" name="destinationId" required>
                            <option value="">-- Choose a Destination --</option>
                            <c:forEach var="dest" items="${destinations}">
                                <option value="${dest.id}" ${preselectedDestId == dest.id ? 'selected' : ''}>
                                    ${dest.name} (${dest.city}, ${dest.country})
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label for="startDate" class="form-label small fw-semibold text-secondary">Start Date <span class="text-danger">*</span></label>
                            <input type="date" class="form-control" id="startDate" name="startDate" required>
                        </div>
                        <div class="col-md-6">
                            <label for="endDate" class="form-label small fw-semibold text-secondary">End Date <span class="text-danger">*</span></label>
                            <input type="date" class="form-control" id="endDate" name="endDate" required>
                        </div>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label for="estimatedBudget" class="form-label small fw-semibold text-secondary">Estimated Total Budget ($)</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light">$</span>
                                <input type="number" step="0.01" min="0" class="form-control" id="estimatedBudget" name="estimatedBudget" placeholder="1500.00">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <label for="visibility" class="form-label small fw-semibold text-secondary">Trip Visibility</label>
                            <select class="form-select" id="visibility" name="visibility">
                                <option value="PRIVATE" selected>Private (Only invited members)</option>
                                <option value="SHARED">Shared (Collaborators can join)</option>
                            </select>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label for="notes" class="form-label small fw-semibold text-secondary">Trip Notes / Goals</label>
                        <textarea class="form-control" id="notes" name="notes" rows="3" placeholder="Key goals, hotel bookings, flight reminders, or travel checklist notes..."></textarea>
                    </div>

                    <div class="d-flex justify-content-between align-items-center pt-3 border-top">
                        <a href="${pageContext.request.contextPath}/trips" class="btn btn-light border px-4">
                            Cancel
                        </a>
                        <button type="submit" class="btn btn-tm-primary px-5 py-2">
                            <i class="bi bi-check2-circle me-1"></i> Save &amp; Start Planning
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
