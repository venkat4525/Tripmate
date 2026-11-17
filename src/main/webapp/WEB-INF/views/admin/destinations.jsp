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
                    <h4 class="fw-bold mb-1"><i class="bi bi-pin-map-fill text-primary me-2"></i> Destination Catalog</h4>
                    <p class="text-muted small mb-0">Manage published tourist destinations and reviews.</p>
                </div>
            </div>

            <c:if test="${not empty param.success}">
                <div class="alert alert-success alert-dismissible fade show small" role="alert">
                    <i class="bi bi-check-circle-fill me-2"></i> ${param.success}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <div class="card tm-card border-0 shadow-sm overflow-hidden">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light small">
                            <tr>
                                <th>Destination</th>
                                <th>Location</th>
                                <th>Rating</th>
                                <th>Avg. Cost</th>
                                <th class="text-end">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="d" items="${destinations}">
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center gap-2">
                                            <img src="${d.imageUrl}" class="rounded-2 object-fit-cover" style="width: 44px; height: 44px;" alt="">
                                            <div class="fw-bold text-dark">${d.name}</div>
                                        </div>
                                    </td>
                                    <td class="small text-secondary">${d.city}, ${d.country}</td>
                                    <td>
                                        <span class="badge bg-warning text-dark"><i class="bi bi-star-fill"></i> ${d.rating}</span>
                                    </td>
                                    <td class="fw-semibold text-dark">$<fmt:formatNumber value="${d.averageCost}" pattern="#,##0.00"/></td>
                                    <td class="text-end">
                                        <a href="${pageContext.request.contextPath}/destination-details?id=${d.id}" class="btn btn-sm btn-light border">
                                            Preview
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/destination-delete?id=${d.id}" 
                                           class="btn btn-sm btn-outline-danger ms-1" onclick="return confirm('Delete this destination?');">
                                            <i class="bi bi-trash"></i>
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
