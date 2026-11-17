<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

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
                    <h4 class="fw-bold mb-1"><i class="bi bi-tags-fill text-primary me-2"></i> Category Management</h4>
                    <p class="text-muted small mb-0">Destination thematic classifications (Beach, Mountain, Cultural, etc.).</p>
                </div>
            </div>

            <div class="row g-3">
                <c:forEach var="cat" items="${categories}">
                    <div class="col-md-6">
                        <div class="card tm-card border p-3 d-flex flex-row align-items-center gap-3">
                            <div class="stat-icon bg-primary bg-opacity-10 text-primary rounded-3">
                                <i class="bi ${cat.icon} fs-3"></i>
                            </div>
                            <div class="flex-grow-1">
                                <h6 class="fw-bold mb-1">${cat.name}</h6>
                                <p class="text-muted small mb-0">${cat.description}</p>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
