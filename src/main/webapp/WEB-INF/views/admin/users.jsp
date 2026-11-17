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
                    <h4 class="fw-bold mb-1"><i class="bi bi-people-fill text-primary me-2"></i> User Management</h4>
                    <p class="text-muted small mb-0">View registered travelers and toggle account activation status.</p>
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
                                <th>Name</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Role</th>
                                <th>Status</th>
                                <th class="text-end">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${users}">
                                <tr>
                                    <td class="fw-semibold text-dark">${u.fullName}</td>
                                    <td class="small text-secondary">${u.email}</td>
                                    <td class="small text-secondary">${not empty u.phone ? u.phone : 'N/A'}</td>
                                    <td>
                                        <span class="badge ${u.admin ? 'bg-warning text-dark' : 'bg-primary'} small">${u.role}</span>
                                    </td>
                                    <td>
                                        <span class="badge ${u.active ? 'bg-success' : 'bg-danger'} small">${u.active ? 'Active' : 'Disabled'}</span>
                                    </td>
                                    <td class="text-end">
                                        <c:if test="${!u.admin}">
                                            <a href="${pageContext.request.contextPath}/admin/user-toggle?id=${u.id}" 
                                               class="btn btn-sm ${u.active ? 'btn-outline-danger' : 'btn-outline-success'}">
                                                ${u.active ? 'Disable' : 'Enable'}
                                            </a>
                                        </c:if>
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
