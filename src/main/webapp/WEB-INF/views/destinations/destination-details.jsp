<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb small">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none text-primary">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/destinations" class="text-decoration-none text-primary">Destinations</a></li>
            <li class="breadcrumb-item active text-muted" aria-current="page">${destination.name}</li>
        </ol>
    </nav>

    <!-- Top Destination Hero Showcase Card -->
    <div class="card tm-card border-0 shadow-sm p-4 mb-4" style="border-radius: 20px;">
        <div class="row g-4 align-items-center">
            <!-- Left: Hero Image -->
            <div class="col-lg-6">
                <div class="position-relative overflow-hidden rounded-4 shadow-sm" style="height: 380px;">
                    <img src="${destination.imageUrl}" alt="${destination.name}" class="w-100 h-100 object-fit-cover">
                </div>
            </div>

            <!-- Right: Title, Stats & Primary Action -->
            <div class="col-lg-6 ps-lg-4">
                <span class="badge bg-primary bg-opacity-10 text-primary rounded-pill px-3 py-2 fw-semibold small mb-3">
                    ${destination.categoryName != null ? destination.categoryName : 'Curated Destination'}
                </span>

                <h1 class="display-6 fw-extrabold text-dark mb-2" style="letter-spacing: -0.5px;">
                    ${destination.name}
                </h1>

                <div class="d-flex align-items-center text-muted mb-4 fs-6">
                    <i class="bi bi-geo-alt-fill text-danger me-2"></i>
                    <span>${destination.city}, ${destination.country}</span>
                </div>

                <!-- Rating & Est. Cost Stat Boxes -->
                <div class="row g-3 mb-4">
                    <div class="col-6">
                        <div class="p-3 bg-light bg-opacity-50 rounded-4 text-center border">
                            <div class="text-muted small fw-semibold mb-1">Rating</div>
                            <div class="fw-bold fs-5 text-dark">
                                <i class="bi bi-star-fill text-warning me-1"></i> <fmt:formatNumber value="${destination.rating}" pattern="#0.0#"/> / 5
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="p-3 bg-light bg-opacity-50 rounded-4 text-center border">
                            <div class="text-muted small fw-semibold mb-1">Est. Cost</div>
                            <div class="fw-bold fs-5 text-primary">
                                $<fmt:formatNumber value="${destination.averageCost}" pattern="#,##0"/>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Main CTA Button -->
                <c:choose>
                    <c:when test="${not empty sessionScope.currentUser}">
                        <a href="${pageContext.request.contextPath}/trip-create?destId=${destination.id}" 
                           class="btn btn-tm-primary w-100 py-3 fw-bold fs-6 rounded-3 shadow-sm d-flex align-items-center justify-content-center gap-2">
                            <i class="bi bi-airplane-fill" style="transform: rotate(45deg);"></i>
                            Plan a Trip to ${destination.name}
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login?error=Please sign in to plan a trip" 
                           class="btn btn-tm-primary w-100 py-3 fw-bold fs-6 rounded-3 shadow-sm d-flex align-items-center justify-content-center gap-2">
                            <i class="bi bi-airplane-fill" style="transform: rotate(45deg);"></i>
                            Plan a Trip to ${destination.name}
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Bottom Split Content: Overview + Highlights on Left, Quick Travel Facts on Right -->
    <div class="row g-4">
        <!-- Left: Overview & Key Highlights -->
        <div class="col-lg-8">
            <div class="card tm-card border-0 shadow-sm p-4 p-md-5 mb-4" style="border-radius: 20px;">
                <!-- Overview -->
                <div class="mb-4">
                    <h5 class="fw-bold text-dark mb-3 d-flex align-items-center gap-2">
                        <i class="bi bi-card-text text-primary"></i> Overview
                    </h5>
                    <p class="text-secondary leading-relaxed mb-0" style="font-size: 1.05rem; line-height: 1.7;">
                        ${destination.description}
                    </p>
                </div>

                <hr class="my-4 text-muted text-opacity-25">

                <!-- Key Highlights -->
                <div>
                    <h5 class="fw-bold text-dark mb-3 d-flex align-items-center gap-2">
                        <i class="bi bi-stars text-warning"></i> Key Highlights
                    </h5>
                    <div class="p-4 bg-light rounded-4 border text-dark fw-semibold" style="font-size: 0.95rem;">
                        ${not empty destination.highlights ? destination.highlights : 'Panoramic Viewpoints, Guided Excursions, Local Cuisine, Scenic Transfers'}
                    </div>
                </div>

                <!-- Tags -->
                <c:if test="${not empty destination.tags}">
                    <div class="mt-4 pt-3 border-top">
                        <div class="d-flex flex-wrap gap-2">
                            <c:forEach var="tag" items="${destination.tags}">
                                <span class="badge bg-light text-secondary border px-3 py-2 rounded-pill small">${tag}</span>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>

        <!-- Right: Quick Travel Facts Card -->
        <div class="col-lg-4">
            <div class="card tm-card border-0 shadow-sm p-4" style="border-radius: 20px;">
                <h5 class="fw-bold text-dark mb-4 d-flex align-items-center gap-2">
                    <i class="bi bi-info-circle-fill text-primary"></i> Quick Travel Facts
                </h5>

                <div class="d-flex flex-column gap-3 mb-4">
                    <div class="d-flex justify-content-between align-items-center pb-2 border-bottom">
                        <span class="text-muted small">Best Season</span>
                        <span class="fw-bold text-dark small">${destination.bestSeason}</span>
                    </div>

                    <div class="d-flex justify-content-between align-items-center pb-2 border-bottom">
                        <span class="text-muted small">Country</span>
                        <span class="fw-bold text-dark small">${destination.country}</span>
                    </div>

                    <div class="d-flex justify-content-between align-items-center pb-2 border-bottom">
                        <span class="text-muted small">Category</span>
                        <span class="fw-bold text-dark small">${destination.categoryName}</span>
                    </div>

                    <div class="d-flex justify-content-between align-items-center pb-2">
                        <span class="text-muted small">Est. Budget</span>
                        <span class="fw-bold text-primary fs-5">$<fmt:formatNumber value="${destination.averageCost}" pattern="#,##0"/></span>
                    </div>
                </div>

                <!-- Create Itinerary Button -->
                <c:choose>
                    <c:when test="${not empty sessionScope.currentUser}">
                        <a href="${pageContext.request.contextPath}/trip-create?destId=${destination.id}" 
                           class="btn btn-tm-primary w-100 py-2 fw-semibold rounded-3 shadow-sm">
                            <i class="bi bi-plus-lg me-1"></i> Create Itinerary
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login?error=Please log in to create an itinerary" 
                           class="btn btn-tm-primary w-100 py-2 fw-semibold rounded-3 shadow-sm">
                            <i class="bi bi-plus-lg me-1"></i> Create Itinerary
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
