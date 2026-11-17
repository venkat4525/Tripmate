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
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold mb-1"><i class="bi bi-heart-fill text-danger me-2"></i> Saved Favourite Spots</h3>
                    <p class="text-muted small mb-0">Destinations you have bookmarked for your future travel plans.</p>
                </div>
                <a href="${pageContext.request.contextPath}/destinations" class="btn btn-sm btn-outline-primary rounded-pill px-3">
                    <i class="bi bi-plus-circle me-1"></i> Discover More
                </a>
            </div>

            <c:choose>
                <c:when test="${empty favourites}">
                    <div class="card tm-card border-0 shadow-sm p-5 text-center">
                        <i class="bi bi-bookmark-heart text-muted display-4 mb-3"></i>
                        <h5 class="fw-bold text-dark">No saved destinations yet</h5>
                        <p class="text-muted small mb-4">Click the heart icon on any destination card to bookmark it here.</p>
                        <div class="d-flex justify-content-center">
                            <a href="${pageContext.request.contextPath}/destinations" class="btn btn-tm-primary px-4 py-2">
                                <i class="bi bi-compass me-1"></i> Browse Destinations
                            </a>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row g-4">
                        <c:forEach var="fav" items="${favourites}">
                            <div class="col-md-6">
                                <div class="card tm-card h-100 border-0 shadow-sm overflow-hidden">
                                    <div class="position-relative" style="height: 180px;">
                                        <img src="${fav.destination.imageUrl}" alt="${fav.destination.name}" class="w-100 h-100 object-fit-cover">
                                        <a href="${pageContext.request.contextPath}/toggle-favourite?id=${fav.destination.id}&redirect=favourites" 
                                           class="position-absolute top-0 end-0 m-2 btn btn-sm bg-white rounded-circle shadow-sm border-0 d-flex align-items-center justify-content-center"
                                           style="width: 32px; height: 32px;" title="Remove from Favourites">
                                            <i class="bi bi-heart-fill text-danger"></i>
                                        </a>
                                        <span class="position-absolute bottom-0 start-0 m-2 badge bg-dark bg-opacity-75 text-white small">
                                            <i class="bi bi-geo-alt me-1"></i> ${fav.destination.city}, ${fav.destination.country}
                                        </span>
                                    </div>
                                    <div class="card-body p-3 d-flex flex-column">
                                        <h6 class="fw-bold text-dark mb-1">${fav.destination.name}</h6>
                                        <p class="text-muted small mb-3 flex-grow-1" style="display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">
                                            ${fav.destination.description}
                                        </p>
                                        <div class="d-flex justify-content-between align-items-center pt-2 border-top mt-auto">
                                            <span class="fw-bold text-primary">
                                                $<fmt:formatNumber value="${fav.destination.averageCost}" pattern="#,##0.00"/>
                                            </span>
                                            <div class="d-flex gap-2">
                                                <a href="${pageContext.request.contextPath}/destination-details?id=${fav.destination.id}" class="btn btn-sm btn-light border">
                                                    Details
                                                </a>
                                                <a href="${pageContext.request.contextPath}/trip-create?destId=${fav.destination.id}" class="btn btn-sm btn-tm-primary">
                                                    Plan Trip
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
