<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="common/header.jsp" />
<jsp:include page="common/navbar.jsp" />

<!-- Hero Search Section -->
<section class="position-relative text-white py-5" style="background: linear-gradient(135deg, #0f172a 0%, #1e1b4b 60%, #312e81 100%);">
    <div class="container py-5 text-center position-relative">
        <span class="badge bg-primary bg-opacity-25 text-info rounded-pill px-3 py-2 fw-semibold mb-3 border border-primary border-opacity-25">
            <i class="bi bi-compass me-1"></i> Smart Travel Planning &amp; Trip Management
        </span>
        <h1 class="display-4 fw-extrabold mb-3" style="letter-spacing: -1.5px;">
            Plan Smarter. Travel Better.<br>
            <span style="background: linear-gradient(90deg, #38bdf8 0%, #818cf8 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">
                Experience Together.
            </span>
        </h1>
        <p class="lead text-light text-opacity-75 mx-auto mb-4" style="max-width: 650px;">
            Create day-wise itineraries, track shared expenses, coordinate packing checklists, and invite friends to collaborate on unforgettable adventures.
        </p>

        <!-- Search Bar -->
        <div class="bg-white p-2 rounded-4 shadow-lg border mx-auto" style="max-width: 640px;">
            <form action="${pageContext.request.contextPath}/destinations" method="get" class="row g-2 align-items-center">
                <div class="col-md-5">
                    <div class="input-group">
                        <span class="input-group-text bg-transparent border-0 text-secondary ps-3"><i class="bi bi-search"></i></span>
                        <input type="text" name="q" class="form-control border-0 shadow-none ps-1" placeholder="Where to? (e.g. North India, Himachal, Jaipur)">
                    </div>
                </div>
                <div class="col-md-4">
                    <select name="category" class="form-select border-0 shadow-none text-secondary">
                        <option value="">All Categories</option>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.id}">${cat.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <button type="submit" class="btn btn-tm-primary w-100 py-2">
                        <i class="bi bi-compass me-1"></i> Explore
                    </button>
                </div>
            </form>
        </div>
    </div>
</section>

<!-- Curated Experiences: Explore by Travel Theme -->
<section id="themes" class="py-5 bg-white border-bottom">
    <div class="container py-3">
        <div class="text-center mb-5">
            <div class="text-primary fw-bold small text-uppercase mb-1" style="letter-spacing: 1px;">
                CURATED EXPERIENCES
            </div>
            <h2 class="display-6 fw-extrabold text-dark mb-2">Explore by Travel Theme</h2>
            <p class="text-muted small mb-0">Find the perfect escape tailored to your style of journey.</p>
        </div>

        <!-- 5 Horizontal Theme Cards -->
        <div class="row g-3 justify-content-center">
            <c:forEach var="cat" items="${categories}">
                <div class="col-lg-2 col-md-4 col-6">
                    <a href="${pageContext.request.contextPath}/destinations?category=${cat.id}" class="text-decoration-none">
                        <div class="p-4 rounded-4 text-center border tm-card h-100 d-flex flex-column align-items-center justify-content-center bg-white shadow-sm" style="border-radius: 18px;">
                            <div class="rounded-circle bg-primary bg-opacity-10 text-primary d-inline-flex align-items-center justify-content-center mb-3" style="width: 58px; height: 58px;">
                                <i class="bi ${cat.icon} fs-4"></i>
                            </div>
                            <h6 class="fw-bold text-dark mb-0 small text-center">${cat.name}</h6>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<!-- Popular Spots: Trending Travel Packages -->
<section class="py-5 bg-light">
    <div class="container py-3">
        <div class="d-flex justify-content-between align-items-end mb-4">
            <div>
                <div class="text-primary fw-bold small text-uppercase mb-1" style="letter-spacing: 1px;">
                    POPULAR SPOTS
                </div>
                <h2 class="display-6 fw-extrabold text-dark mb-0">Trending Tour Packages</h2>
            </div>
            <a href="${pageContext.request.contextPath}/destinations" class="btn btn-outline-primary rounded-pill px-4 fw-semibold small shadow-sm">
                View All <i class="bi bi-arrow-right ms-1"></i>
            </a>
        </div>

        <!-- Tour Cards Grid (Exact Style from User Screenshot) -->
        <div class="row g-4">
            <c:forEach var="dest" items="${topDestinations}">
                <div class="col-xl-3 col-lg-4 col-md-6">
                    <div class="card tm-card h-100 border-0 shadow-sm overflow-hidden d-flex flex-column" style="border-radius: 12px;">
                        <!-- Photo -->
                        <div class="position-relative" style="height: 190px; overflow: hidden;">
                            <img src="${dest.imageUrl}" alt="${dest.name}" class="w-100 h-100 object-fit-cover" style="transition: transform 0.4s ease;">
                            <span class="position-absolute top-0 end-0 m-2 badge bg-warning text-dark shadow-sm px-2 py-1 rounded-2 fw-bold small">
                                <i class="bi bi-star-fill text-dark me-1"></i> <fmt:formatNumber value="${dest.rating}" pattern="#0.0#"/>
                            </span>
                        </div>

                        <!-- Card Body -->
                        <div class="card-body p-3 d-flex flex-column">
                            <div class="text-muted mb-2 small text-truncate" title="${dest.city}" style="font-size: 0.78rem;">
                                <i class="bi bi-geo-alt text-secondary me-1"></i> ${dest.city}
                            </div>

                            <h6 class="fw-bold text-dark mb-2" style="font-family: Georgia, 'Times New Roman', serif; font-size: 1.05rem; line-height: 1.35; min-height: 2.7rem; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">
                                ${dest.name}
                            </h6>

                            <div class="mb-3 fw-semibold small d-flex align-items-center gap-1" style="color: #d97706; font-size: 0.85rem;">
                                <i class="bi bi-clock"></i>
                                <span>${dest.duration}</span>
                            </div>

                            <div class="d-flex justify-content-between align-items-center pt-2 border-top mt-auto">
                                <div>
                                    <div class="text-muted small" style="font-size: 0.7rem;">From</div>
                                    <div class="fw-bold text-primary fs-6">
                                        $<fmt:formatNumber value="${dest.averageCost}" pattern="#,##0"/>
                                    </div>
                                </div>
                                <a href="${pageContext.request.contextPath}/destination-details?id=${dest.id}" class="btn btn-sm btn-tm-primary px-3 rounded-2 fw-semibold">
                                    Explore <i class="bi bi-arrow-right ms-1"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<!-- Call to Action Banner -->
<section class="py-5" style="background: linear-gradient(135deg, #1e1b4b 0%, #0f172a 100%);">
    <div class="container py-4 text-center text-white">
        <h2 class="display-6 fw-bold mb-3">Ready to Plan Your Next Journey?</h2>
        <p class="lead text-light text-opacity-75 mx-auto mb-4" style="max-width: 600px;">
            Join TripMate today and experience smart, organized travel planning with full MongoDB cloud support.
        </p>
        <div class="d-flex justify-content-center gap-3">
            <a href="${pageContext.request.contextPath}/register" class="btn btn-tm-primary px-4 py-2 fs-6">
                <i class="bi bi-person-plus-fill me-1"></i> Create Free Account
            </a>
            <a href="${pageContext.request.contextPath}/destinations" class="btn btn-outline-light px-4 py-2 fs-6 rounded-3">
                <i class="bi bi-compass me-1"></i> Browse Destinations
            </a>
        </div>
    </div>
</section>

<jsp:include page="common/footer.jsp" />
