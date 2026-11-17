<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-4">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <nav aria-label="breadcrumb" class="mb-3">
                <ol class="breadcrumb small">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/trips" class="text-decoration-none">Trips</a></li>
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/trip-details?id=${trip.id}" class="text-decoration-none">${trip.title}</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Edit Trip</li>
                </ol>
            </nav>

            <div class="card tm-card border-0 shadow-sm p-4 p-md-5">
                <div class="d-flex align-items-center justify-content-between mb-4 border-bottom pb-3">
                    <div>
                        <h3 class="fw-bold text-dark mb-0">Edit Trip Settings</h3>
                        <p class="text-muted small mb-0">Update title, dates, budget, or status for this trip.</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/trip-details?id=${trip.id}" class="btn btn-sm btn-outline-secondary">
                        <i class="bi bi-x-lg me-1"></i> Close
                    </a>
                </div>

                <form action="${pageContext.request.contextPath}/trip-edit" method="post">
                    <input type="hidden" name="id" value="${trip.id}">

                    <div class="mb-3">
                        <label for="title" class="form-label small fw-semibold text-secondary">Trip Name / Title <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="title" name="title" value="${trip.title}" required>
                    </div>

                    <div class="mb-3">
                        <label for="destinationId" class="form-label small fw-semibold text-secondary">Destination <span class="text-danger">*</span></label>
                        <select class="form-select" id="destinationId" name="destinationId" required>
                            <c:forEach var="dest" items="${destinations}">
                                <option value="${dest.id}" ${trip.destinationId == dest.id ? 'selected' : ''}>
                                    ${dest.name} (${dest.city}, ${dest.country})
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label for="startDate" class="form-label small fw-semibold text-secondary">Start Date <span class="text-danger">*</span></label>
                            <input type="date" class="form-control" id="startDate" name="startDate" value="${trip.inputStartDate}" required>
                        </div>
                        <div class="col-md-6">
                            <label for="endDate" class="form-label small fw-semibold text-secondary">End Date <span class="text-danger">*</span></label>
                            <input type="date" class="form-control" id="endDate" name="endDate" value="${trip.inputEndDate}" required>
                        </div>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-4">
                            <label for="estimatedBudget" class="form-label small fw-semibold text-secondary">Estimated Budget ($)</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light">$</span>
                                <input type="number" step="0.01" min="0" class="form-control" id="estimatedBudget" name="estimatedBudget" value="${trip.estimatedBudget}">
                            </div>
                        </div>
                        <div class="col-md-4">
                            <label for="status" class="form-label small fw-semibold text-secondary">Trip Status</label>
                            <select class="form-select" id="status" name="status">
                                <option value="PLANNING" ${trip.status == 'PLANNING' ? 'selected' : ''}>PLANNING</option>
                                <option value="ONGOING" ${trip.status == 'ONGOING' ? 'selected' : ''}>ONGOING</option>
                                <option value="COMPLETED" ${trip.status == 'COMPLETED' ? 'selected' : ''}>COMPLETED</option>
                                <option value="CANCELLED" ${trip.status == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label for="visibility" class="form-label small fw-semibold text-secondary">Visibility</label>
                            <select class="form-select" id="visibility" name="visibility">
                                <option value="PRIVATE" ${trip.visibility == 'PRIVATE' ? 'selected' : ''}>PRIVATE</option>
                                <option value="SHARED" ${trip.visibility == 'SHARED' ? 'selected' : ''}>SHARED</option>
                            </select>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label for="notes" class="form-label small fw-semibold text-secondary">Trip Notes</label>
                        <textarea class="form-control" id="notes" name="notes" rows="3">${trip.notes}</textarea>
                    </div>

                    <div class="d-flex justify-content-between align-items-center pt-3 border-top">
                        <a href="${pageContext.request.contextPath}/trip-delete?id=${trip.id}" 
                           class="btn btn-outline-danger px-3"
                           onclick="return confirm('Are you sure you want to permanently delete this trip and all its itineraries and expenses?');">
                            <i class="bi bi-trash me-1"></i> Delete Trip
                        </a>
                        <div>
                            <a href="${pageContext.request.contextPath}/trip-details?id=${trip.id}" class="btn btn-light border px-3 me-2">Cancel</a>
                            <button type="submit" class="btn btn-tm-primary px-4">Save Changes</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
