<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<footer class="tm-footer">
    <div class="container">
        <div class="row g-4 py-4">
            <div class="col-lg-4 col-md-6">
                <div class="d-flex align-items-center gap-2 mb-3 text-white">
                    <i class="bi bi-compass-fill fs-3 text-primary"></i>
                    <span class="fs-4 fw-bold">TripMate</span>
                </div>
                <p class="small text-secondary mb-3">
                    Smart Travel Planning and Trip Management System built for modern travelers. Plan day-wise itineraries, track budgets, collaborate with friends, and manage checklists effortlessly.
                </p>
                <div class="d-flex gap-3 text-secondary">
                    <a href="#" class="fs-5"><i class="bi bi-facebook"></i></a>
                    <a href="#" class="fs-5"><i class="bi bi-instagram"></i></a>
                    <a href="#" class="fs-5"><i class="bi bi-twitter-x"></i></a>
                    <a href="#" class="fs-5"><i class="bi bi-github"></i></a>
                </div>
            </div>

            <div class="col-lg-2 col-md-6 col-6">
                <h6 class="text-white fw-bold mb-3">Quick Navigation</h6>
                <ul class="list-unstyled small d-flex flex-column gap-2 mb-0">
                    <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/destinations">Explore Spots</a></li>
                    <li><a href="${pageContext.request.contextPath}/trips">Trip Planner</a></li>
                    <li><a href="${pageContext.request.contextPath}/favourites">Saved Favorites</a></li>
                </ul>
            </div>

            <div class="col-lg-2 col-md-6 col-6">
                <h6 class="text-white fw-bold mb-3">Features</h6>
                <ul class="list-unstyled small d-flex flex-column gap-2 mb-0">
                    <li><a href="#">Day-wise Itinerary</a></li>
                    <li><a href="#">Expense Tracker</a></li>
                    <li><a href="#">Packing Checklist</a></li>
                    <li><a href="#">Group Collaboration</a></li>
                </ul>
            </div>

            <div class="col-lg-4 col-md-6">
                <h6 class="text-white fw-bold mb-3">Advanced Java Programming</h6>
                <p class="small text-secondary mb-2">
                    College Group Project developed with Java Servlets (Jakarta EE 10), JSP, JSTL, and MongoDB Java Sync Driver.
                </p>
                <div class="p-3 bg-dark bg-opacity-50 rounded-3 border border-secondary border-opacity-25 small text-secondary">
                    <i class="bi bi-database-check text-success me-1"></i> Connected to <strong>MongoDB Atlas / Local</strong> (`tripmate_db`)
                </div>
            </div>
        </div>

        <div class="border-top border-secondary border-opacity-25 pt-4 mt-2 text-center small text-secondary">
            &copy; 2026 <strong>TripMate</strong>. Advanced Java Programming Project. All rights reserved.
        </div>
    </div>
</footer>

<!-- Bootstrap 5.3.3 Bundle JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
