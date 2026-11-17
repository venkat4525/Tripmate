<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="bg-primary text-white py-4" style="background: linear-gradient(135deg, #0f172a 0%, #1e1b4b 100%);">
    <div class="container">
        <h2 class="fw-bold mb-1"><i class="bi bi-compass me-2"></i> Explore Tour Packages &amp; Destinations</h2>
        <p class="text-light text-opacity-75 mb-0">Browse curated travel packages across India and global destinations with authentic itineraries, durations &amp; pricing.</p>
    </div>
</div>

<div class="container py-4">
    <!-- Filter & Search Toolbar -->
    <div class="card tm-card border-0 shadow-sm p-4 mb-4" style="border-radius: 18px;">
        <form action="${pageContext.request.contextPath}/destinations" method="get" class="row g-3">
            <div class="col-lg-4 col-md-6">
                <label class="form-label small fw-semibold text-secondary">Search Keyword</label>
                <div class="input-group">
                    <span class="input-group-text bg-light text-muted border-end-0"><i class="bi bi-search"></i></span>
                    <input type="text" name="q" class="form-control border-start-0" placeholder="Package name, city, state..." value="${paramQ != null ? paramQ : ''}">
                </div>
            </div>

            <div class="col-lg-3 col-md-6">
                <label class="form-label small fw-semibold text-secondary">Travel Theme / Category</label>
                <select name="category" class="form-select">
                    <option value="">All Categories</option>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat.id}" ${paramCategory == cat.id ? 'selected' : ''}>${cat.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="col-lg-3 col-md-6">
                <label class="form-label small fw-semibold text-secondary">Country / Region</label>
                <input type="text" name="country" class="form-control" placeholder="e.g. India, Switzerland" value="${paramCountry != null ? paramCountry : ''}">
            </div>

            <div class="col-lg-2 col-md-6 d-flex align-items-end">
                <button type="submit" class="btn btn-tm-primary w-100 py-2">
                    <i class="bi bi-filter me-1"></i> Apply Filters
                </button>
            </div>
        </form>
    </div>

    <!-- Results Count -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h5 class="fw-bold text-dark mb-0">
            Available Tour Packages <span class="badge bg-secondary rounded-pill small">${destinations.size()}</span>
        </h5>
        <c:if test="${not empty paramQ or not empty paramCategory or not empty paramCountry}">
            <a href="${pageContext.request.contextPath}/destinations" class="text-danger small fw-semibold text-decoration-none">
                <i class="bi bi-x-circle me-1"></i> Clear Filters
            </a>
        </c:if>
    </div>

    <!-- 4-Column Tour Package Cards Grid (Exact Design from User Screenshot) -->
    <c:choose>
        <c:when test="${empty destinations}">
            <div class="text-center py-5 bg-white rounded-4 border p-5">
                <i class="bi bi-search text-muted display-4 mb-3"></i>
                <h5 class="fw-bold text-secondary">No tour packages match your search</h5>
                <p class="text-muted small">Try broadening your search keywords or clearing selected filters.</p>
                <a href="${pageContext.request.contextPath}/destinations" class="btn btn-sm btn-outline-primary rounded-pill px-4">
                    Reset All Filters
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row g-4">
                <c:forEach var="dest" items="${destinations}">
                    <!-- 4 Columns on Large Screens (col-xl-3 col-lg-4 col-md-6) -->
                    <div class="col-xl-3 col-lg-4 col-md-6">
                        <div class="card tm-card h-100 border-0 shadow-sm overflow-hidden d-flex flex-column" style="border-radius: 12px;">
                            <!-- Tour Photo -->
                            <div class="position-relative" style="height: 190px; overflow: hidden;">
                                <img src="${dest.imageUrl}" alt="${dest.name}" class="w-100 h-100 object-fit-cover" style="transition: transform 0.4s ease;">
                                
                                <!-- Bookmark Favourite Button -->
                                <c:if test="${not empty sessionScope.currentUser}">
                                    <a href="${pageContext.request.contextPath}/toggle-favourite?id=${dest.id}" 
                                       class="position-absolute top-0 end-0 m-2 btn btn-sm bg-white rounded-circle shadow-sm border-0 d-flex align-items-center justify-content-center"
                                       style="width: 32px; height: 32px;" title="Save to Favourites">
                                        <i class="bi ${favouriteIds.contains(dest.id) ? 'bi-heart-fill text-danger' : 'bi-heart text-secondary'}"></i>
                                    </a>
                                </c:if>
                            </div>

                            <!-- Tour Details Body (Exact Match with Image) -->
                            <div class="card-body p-3 d-flex flex-column">
                                <!-- Location Pin -->
                                <div class="text-muted mb-2 small text-truncate" title="${dest.city}" style="font-size: 0.78rem;">
                                    <i class="bi bi-geo-alt text-secondary me-1"></i> ${dest.city}
                                </div>

                                <!-- Tour Title -->
                                <h6 class="fw-bold text-dark mb-2" style="font-family: Georgia, 'Times New Roman', serif; font-size: 1.05rem; line-height: 1.35; min-height: 2.7rem; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">
                                    ${dest.name}
                                </h6>

                                <!-- Duration Badge (Amber/Gold Text) -->
                                <div class="mb-3 fw-semibold small d-flex align-items-center gap-1" style="color: #d97706; font-size: 0.85rem;">
                                    <i class="bi bi-clock"></i>
                                    <span>${dest.duration}</span>
                                </div>

                                <!-- Bottom Price and Explore Button -->
                                <div class="d-flex justify-content-between align-items-center pt-2 border-top mt-auto">
                                    <div>
                                        <div class="text-muted small" style="font-size: 0.7rem;">From</div>
                                        <div class="fw-bold text-primary fs-6">
                                            $<fmt:formatNumber value="${dest.averageCost}" pattern="#,##0"/>
                                        </div>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/destination-details?id=${dest.id}" class="btn btn-sm btn-tm-primary px-3 rounded-2 fw-semibold">
                                        View Details
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="../common/footer.jsp" />
