<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-4">
    <!-- Breadcrumb & Actions Bar -->
    <div class="d-flex flex-wrap justify-content-between align-items-center mb-3 gap-2">
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb small mb-0">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard" class="text-decoration-none">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/trips" class="text-decoration-none">Trips</a></li>
                <li class="breadcrumb-item active" aria-current="page">${trip.title}</li>
            </ol>
        </nav>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/trip-summary?id=${trip.id}" class="btn btn-sm btn-outline-dark" target="_blank">
                <i class="bi bi-printer me-1"></i> Print / Summary View
            </a>
            <c:if test="${isOwner}">
                <a href="${pageContext.request.contextPath}/trip-edit?id=${trip.id}" class="btn btn-sm btn-light border">
                    <i class="bi bi-gear me-1"></i> Trip Settings
                </a>
            </c:if>
        </div>
    </div>

    <!-- Alert Messages -->
    <c:if test="${not empty param.success}">
        <div class="alert alert-success alert-dismissible fade show small" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i> ${param.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger alert-dismissible fade show small" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i> ${param.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Master Trip Overview Hero Card -->
    <div class="card tm-card border-0 shadow-sm p-4 p-md-5 mb-4 text-white position-relative" style="background: linear-gradient(135deg, #0f172a 0%, #1e1b4b 60%, #312e81 100%);">
        <div class="row align-items-center g-4">
            <div class="col-lg-8">
                <div class="d-flex flex-wrap gap-2 mb-2">
                    <span class="badge ${trip.status == 'PLANNING' ? 'bg-primary' : (trip.status == 'ONGOING' ? 'bg-success' : 'bg-secondary')} rounded-pill px-3 py-1">
                        ${trip.status}
                    </span>
                    <span class="badge bg-white bg-opacity-20 text-white rounded-pill px-3 py-1">
                        <i class="bi ${trip.visibility == 'SHARED' ? 'bi-people' : 'bi-lock'} me-1"></i> ${trip.visibility}
                    </span>
                    <c:if test="${isOwner}">
                        <span class="badge bg-warning text-dark rounded-pill px-3 py-1 fw-bold">YOU ARE OWNER</span>
                    </c:if>
                </div>

                <h1 class="display-6 fw-bold mb-2">${trip.title}</h1>
                <p class="text-light text-opacity-75 mb-3">
                    <i class="bi bi-geo-alt-fill text-danger me-1"></i> ${trip.destinationName}
                    <span class="mx-2">•</span>
                    <i class="bi bi-calendar3 me-1"></i> ${trip.formattedStartDate} to ${trip.formattedEndDate} (${trip.durationDays} Days)
                </p>

                <c:if test="${not empty trip.notes}">
                    <p class="small text-light text-opacity-75 mb-0" style="max-width: 600px;">
                        <i class="bi bi-card-text me-1"></i> ${trip.notes}
                    </p>
                </c:if>
            </div>

            <div class="col-lg-4">
                <div class="bg-white bg-opacity-10 p-3 rounded-4 border border-white border-opacity-25 text-white">
                    <div class="d-flex justify-content-between small text-light text-opacity-75 mb-1">
                        <span>Total Budget:</span>
                        <span class="fw-bold text-white">$<fmt:formatNumber value="${trip.estimatedBudget}" pattern="#,##0.00"/></span>
                    </div>
                    <div class="d-flex justify-content-between small text-light text-opacity-75 mb-2">
                        <span>Spent So Far:</span>
                        <span class="fw-bold text-white">$<fmt:formatNumber value="${trip.actualExpenses}" pattern="#,##0.00"/></span>
                    </div>

                    <div class="progress mb-2" style="height: 8px;">
                        <div class="progress-bar ${trip.budgetUsagePercentage > 90 ? 'bg-danger' : 'bg-info'}" 
                             role="progressbar" style="width: ${trip.budgetUsagePercentage}%;"></div>
                    </div>

                    <div class="d-flex justify-content-between small">
                        <span class="text-light text-opacity-75">Remaining:</span>
                        <span class="fw-bold ${trip.remainingBudget < 0 ? 'text-danger' : 'text-success'}">
                            $<fmt:formatNumber value="${trip.remainingBudget}" pattern="#,##0.00"/>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Main Navigation Tabs -->
    <ul class="nav nav-tabs border-bottom mb-4" id="tripTab" role="tablist">
        <li class="nav-item">
            <button class="nav-link py-3 px-4 fw-bold ${activeTab == 'itinerary' ? 'active text-primary' : 'text-secondary'}" 
                    id="itinerary-tab" data-bs-toggle="tab" data-bs-target="#itinerary" type="button">
                <i class="bi bi-calendar2-range me-1"></i> Day-wise Itinerary (${itineraryItems.size()})
            </button>
        </li>
        <li class="nav-item">
            <button class="nav-link py-3 px-4 fw-bold ${activeTab == 'expenses' ? 'active text-primary' : 'text-secondary'}" 
                    id="expenses-tab" data-bs-toggle="tab" data-bs-target="#expenses" type="button">
                <i class="bi bi-cash-stack me-1"></i> Budget &amp; Expenses (${expenses.size()})
            </button>
        </li>
        <li class="nav-item">
            <button class="nav-link py-3 px-4 fw-bold ${activeTab == 'checklist' ? 'active text-primary' : 'text-secondary'}" 
                    id="checklist-tab" data-bs-toggle="tab" data-bs-target="#checklist" type="button">
                <i class="bi bi-check2-square me-1"></i> Packing Checklist (${checklistItems.size()})
            </button>
        </li>
        <li class="nav-item">
            <button class="nav-link py-3 px-4 fw-bold ${activeTab == 'members' ? 'active text-primary' : 'text-secondary'}" 
                    id="members-tab" data-bs-toggle="tab" data-bs-target="#members" type="button">
                <i class="bi bi-people me-1"></i> Members &amp; Group (${members.size()})
            </button>
        </li>
    </ul>

    <!-- Tab Contents -->
    <div class="tab-content" id="tripTabContent">

        <!-- 1. ITINERARY TAB -->
        <div class="tab-pane fade ${activeTab == 'itinerary' ? 'show active' : ''}" id="itinerary">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h5 class="fw-bold mb-0">Daily Schedule &amp; Activities</h5>
                    <p class="text-muted small mb-0">Organize your daily activities hour by hour.</p>
                </div>
                <c:if test="${canEdit}">
                    <button class="btn btn-tm-primary btn-sm px-3" data-bs-toggle="modal" data-bs-target="#addActivityModal">
                        <i class="bi bi-plus-circle me-1"></i> Add Activity
                    </button>
                </c:if>
            </div>

            <c:choose>
                <c:when test="${empty itineraryItems}">
                    <div class="card tm-card border-0 shadow-sm p-5 text-center">
                        <i class="bi bi-calendar-x text-muted display-4 mb-3"></i>
                        <h6 class="fw-bold text-dark">No activities added yet</h6>
                        <p class="text-muted small mb-3">Add daily milestones such as hotel check-in, city tours, or sunset viewpoints.</p>
                        <c:if test="${canEdit}">
                            <div>
                                <button class="btn btn-sm btn-primary" data-bs-toggle="modal" data-bs-target="#addActivityModal">
                                    <i class="bi bi-plus-lg me-1"></i> Add Day 1 Activity
                                </button>
                            </div>
                        </c:if>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="d-flex flex-column gap-3">
                        <c:forEach var="item" items="${itineraryItems}">
                            <div class="card tm-card border p-3 ${item.completed ? 'bg-light text-muted' : 'bg-white'}">
                                <div class="row align-items-center g-3">
                                    <div class="col-auto">
                                        <span class="badge bg-primary rounded-pill px-3 py-2 fw-bold">
                                            Day ${item.dayNumber}
                                        </span>
                                    </div>
                                    <div class="col-md-2 col-auto text-secondary fw-semibold small">
                                        <i class="bi bi-clock me-1 text-primary"></i> ${item.timeSlot}
                                    </div>
                                    <div class="col-md-5 col-12">
                                        <h6 class="fw-bold mb-1 ${item.completed ? 'text-decoration-line-through' : 'text-dark'}">${item.activityTitle}</h6>
                                        <div class="small text-muted">
                                            <i class="bi bi-geo-alt me-1 text-danger"></i> ${item.location}
                                            <c:if test="${not empty item.notes}">
                                                <span class="mx-1">•</span> <i class="bi bi-chat-left-dots me-1"></i> ${item.notes}
                                            </c:if>
                                        </div>
                                    </div>
                                    <div class="col-md-2 col-auto fw-bold text-success">
                                        <c:if test="${item.estimatedCost > 0}">
                                            $<fmt:formatNumber value="${item.estimatedCost}" pattern="#,##0.00"/>
                                        </c:if>
                                    </div>
                                    <div class="col-md-2 col-auto ms-auto d-flex align-items-center gap-2">
                                        <c:if test="${canEdit}">
                                            <a href="${pageContext.request.contextPath}/itinerary-toggle?id=${item.id}&tripId=${trip.id}&completed=${!item.completed}" 
                                               class="btn btn-sm ${item.completed ? 'btn-success' : 'btn-outline-secondary'}" 
                                               title="Toggle Completion">
                                                <i class="bi ${item.completed ? 'bi-check-circle-fill' : 'bi-circle'}"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/itinerary-delete?id=${item.id}&tripId=${trip.id}" 
                                               class="btn btn-sm btn-outline-danger" 
                                               onclick="return confirm('Remove this activity?');" title="Delete Activity">
                                                <i class="bi bi-trash"></i>
                                            </a>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- 2. BUDGET & EXPENSES TAB -->
        <div class="tab-pane fade ${activeTab == 'expenses' ? 'show active' : ''}" id="expenses">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h5 class="fw-bold mb-0">Expense Ledger &amp; Budget Tracking</h5>
                    <p class="text-muted small mb-0">Log itemized travel expenses and keep group spending in check.</p>
                </div>
                <c:if test="${canEdit}">
                    <button class="btn btn-tm-primary btn-sm px-3" data-bs-toggle="modal" data-bs-target="#addExpenseModal">
                        <i class="bi bi-plus-circle me-1"></i> Add Expense
                    </button>
                </c:if>
            </div>

            <!-- Category Spend Badges -->
            <div class="card tm-card border-0 shadow-sm p-3 mb-4 bg-light">
                <div class="small fw-bold text-secondary mb-2">SPENDING BREAKDOWN BY CATEGORY:</div>
                <div class="d-flex flex-wrap gap-2">
                    <c:forEach var="entry" items="${expensesByCategory}">
                        <div class="p-2 bg-white rounded-3 border small d-flex align-items-center gap-2">
                            <span class="badge bg-secondary">${entry.key}</span>
                            <span class="fw-bold text-dark">$<fmt:formatNumber value="${entry.value}" pattern="#,##0.00"/></span>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- Expenses Table -->
            <div class="card tm-card border-0 shadow-sm overflow-hidden">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light small text-secondary">
                            <tr>
                                <th>Date</th>
                                <th>Title / Description</th>
                                <th>Category</th>
                                <th>Payment Method</th>
                                <th>Paid By</th>
                                <th class="text-end">Amount</th>
                                <th class="text-end">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty expenses}">
                                    <tr>
                                        <td colspan="7" class="text-center py-4 text-muted">
                                            No expenses logged for this trip yet.
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="exp" items="${expenses}">
                                        <tr>
                                            <td class="small text-secondary">${exp.formattedExpenseDate}</td>
                                            <td class="fw-semibold text-dark">${exp.title}</td>
                                            <td>
                                                <span class="badge badge-soft-primary small">${exp.category}</span>
                                            </td>
                                            <td class="small text-secondary">${exp.paymentMethod}</td>
                                            <td class="small">${exp.payerName != null ? exp.payerName : 'Trip Member'}</td>
                                            <td class="text-end fw-bold text-dark fs-6">
                                                $<fmt:formatNumber value="${exp.amount}" pattern="#,##0.00"/>
                                            </td>
                                            <td class="text-end">
                                                <c:if test="${canEdit}">
                                                    <a href="${pageContext.request.contextPath}/expense-delete?id=${exp.id}&tripId=${trip.id}" 
                                                       class="btn btn-sm btn-outline-danger py-0 px-2" onclick="return confirm('Delete this expense entry?');">
                                                        <i class="bi bi-trash"></i>
                                                    </a>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                        <tfoot class="table-light fw-bold">
                            <tr>
                                <td colspan="5" class="text-end">Total Logged Expenses:</td>
                                <td class="text-end text-primary fs-5">$<fmt:formatNumber value="${totalExpenses}" pattern="#,##0.00"/></td>
                                <td></td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>

        <!-- 3. PACKING CHECKLIST TAB -->
        <div class="tab-pane fade ${activeTab == 'checklist' ? 'show active' : ''}" id="checklist">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h5 class="fw-bold mb-0">Packing Checklist</h5>
                    <p class="text-muted small mb-0">Check off packed items to make sure nothing essential gets left behind.</p>
                </div>
                <c:if test="${canEdit}">
                    <button class="btn btn-tm-primary btn-sm px-3" data-bs-toggle="modal" data-bs-target="#addChecklistModal">
                        <i class="bi bi-plus-circle me-1"></i> Add Packing Item
                    </button>
                </c:if>
            </div>

            <c:choose>
                <c:when test="${empty checklistItems}">
                    <div class="card tm-card border-0 shadow-sm p-5 text-center">
                        <i class="bi bi-bag-check text-muted display-4 mb-3"></i>
                        <h6 class="fw-bold text-dark">No packing items added</h6>
                        <p class="text-muted small mb-3">Add clothes, passports, adapters, and travel medical supplies.</p>
                        <c:if test="${canEdit}">
                            <div>
                                <button class="btn btn-sm btn-primary" data-bs-toggle="modal" data-bs-target="#addChecklistModal">
                                    <i class="bi bi-plus-lg me-1"></i> Add First Item
                                </button>
                            </div>
                        </c:if>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row g-3">
                        <c:forEach var="item" items="${checklistItems}">
                            <div class="col-md-6">
                                <div class="card tm-card border p-3 d-flex flex-row align-items-center justify-content-between ${item.packed ? 'bg-light' : 'bg-white'}">
                                    <div class="d-flex align-items-center gap-3">
                                        <c:if test="${canEdit}">
                                            <a href="${pageContext.request.contextPath}/checklist-toggle?id=${item.id}&tripId=${trip.id}&packed=${!item.packed}" 
                                               class="fs-4 text-decoration-none ${item.packed ? 'text-success' : 'text-secondary'}">
                                                <i class="bi ${item.packed ? 'bi-check-square-fill' : 'bi-square'}"></i>
                                            </a>
                                        </c:if>
                                        <div>
                                            <div class="fw-bold ${item.packed ? 'text-decoration-line-through text-muted' : 'text-dark'}">${item.itemName}</div>
                                            <span class="badge bg-secondary bg-opacity-10 text-secondary border small">${item.category}</span>
                                        </div>
                                    </div>
                                    <c:if test="${canEdit}">
                                        <a href="${pageContext.request.contextPath}/checklist-delete?id=${item.id}&tripId=${trip.id}" 
                                           class="btn btn-sm text-danger" onclick="return confirm('Delete this packing item?');">
                                            <i class="bi bi-trash"></i>
                                        </a>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- 4. MEMBERS & GROUP COLLABORATION TAB -->
        <div class="tab-pane fade ${activeTab == 'members' ? 'show active' : ''}" id="members">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h5 class="fw-bold mb-0">Trip Members &amp; Group Collaboration</h5>
                    <p class="text-muted small mb-0">Collaborators who can view or edit this trip plan.</p>
                </div>
                <c:if test="${isOwner}">
                    <button class="btn btn-tm-primary btn-sm px-3" data-bs-toggle="modal" data-bs-target="#inviteMemberModal">
                        <i class="bi bi-person-plus me-1"></i> Invite Friend
                    </button>
                </c:if>
            </div>

            <div class="row g-3">
                <c:forEach var="m" items="${members}">
                    <div class="col-md-6">
                        <div class="card tm-card border p-3 d-flex flex-row align-items-center justify-content-between">
                            <div class="d-flex align-items-center gap-3">
                                <div class="rounded-circle bg-primary bg-opacity-10 text-primary d-flex align-items-center justify-content-center fw-bold" style="width: 44px; height: 44px;">
                                    ${m.userName != null ? m.userName.substring(0, 1).toUpperCase() : 'U'}
                                </div>
                                <div>
                                    <div class="fw-bold text-dark">${m.userName != null ? m.userName : 'Member'}</div>
                                    <div class="small text-muted">${m.userEmail}</div>
                                </div>
                            </div>
                            <div class="d-flex align-items-center gap-2">
                                <span class="badge ${m.role == 'OWNER' ? 'bg-primary' : (m.role == 'EDITOR' ? 'bg-success' : 'bg-secondary')} rounded-pill px-3 py-1">
                                    ${m.role}
                                </span>
                                <c:if test="${isOwner and m.role != 'OWNER'}">
                                    <a href="${pageContext.request.contextPath}/trip-remove-member?memberId=${m.id}&tripId=${trip.id}" 
                                       class="btn btn-sm text-danger" onclick="return confirm('Remove this member from trip?');">
                                        <i class="bi bi-x-circle"></i>
                                    </a>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<!-- ========================================== -->
<!-- MODALS (Activity, Expense, Checklist, Invite) -->
<!-- ========================================== -->

<!-- Modal 1: Add Itinerary Activity -->
<div class="modal fade" id="addActivityModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content border-0 shadow-lg rounded-4">
            <form action="${pageContext.request.contextPath}/itinerary-add" method="post">
                <input type="hidden" name="tripId" value="${trip.id}">
                <div class="modal-header border-bottom">
                    <h5 class="modal-title fw-bold"><i class="bi bi-calendar2-plus text-primary me-2"></i> Add Itinerary Activity</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body p-4">
                    <div class="row g-3 mb-3">
                        <div class="col-6">
                            <label class="form-label small fw-semibold">Day Number <span class="text-danger">*</span></label>
                            <input type="number" name="dayNumber" class="form-control" value="1" min="1" max="${trip.durationDays}" required>
                        </div>
                        <div class="col-6">
                            <label class="form-label small fw-semibold">Time Slot <span class="text-danger">*</span></label>
                            <input type="text" name="timeSlot" class="form-control" placeholder="e.g. 09:30 AM" required>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Activity Title <span class="text-danger">*</span></label>
                        <input type="text" name="activityTitle" class="form-control" placeholder="e.g. Visit Temple &amp; Sacred Grove" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Location / Address</label>
                        <input type="text" name="location" class="form-control" placeholder="e.g. Tegallalang, Ubud">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Estimated Cost ($)</label>
                        <input type="number" step="0.01" name="estimatedCost" class="form-control" placeholder="0.00">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Notes / Tips</label>
                        <textarea name="notes" class="form-control" rows="2" placeholder="Wear sunscreen, bring camera..."></textarea>
                    </div>
                </div>
                <div class="modal-footer border-top">
                    <button type="button" class="btn btn-light" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-tm-primary">Save Activity</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal 2: Add Expense -->
<div class="modal fade" id="addExpenseModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content border-0 shadow-lg rounded-4">
            <form action="${pageContext.request.contextPath}/expense-add" method="post">
                <input type="hidden" name="tripId" value="${trip.id}">
                <div class="modal-header border-bottom">
                    <h5 class="modal-title fw-bold"><i class="bi bi-wallet2 text-success me-2"></i> Log New Expense</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body p-4">
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Expense Title / Item <span class="text-danger">*</span></label>
                        <input type="text" name="title" class="form-control" placeholder="e.g. Hotel Deposit, Dinner, Rental Car" required>
                    </div>
                    <div class="row g-3 mb-3">
                        <div class="col-6">
                            <label class="form-label small fw-semibold">Amount ($) <span class="text-danger">*</span></label>
                            <input type="number" step="0.01" min="0.01" name="amount" class="form-control" placeholder="50.00" required>
                        </div>
                        <div class="col-6">
                            <label class="form-label small fw-semibold">Date</label>
                            <input type="date" name="expenseDate" class="form-control">
                        </div>
                    </div>
                    <div class="row g-3 mb-3">
                        <div class="col-6">
                            <label class="form-label small fw-semibold">Category <span class="text-danger">*</span></label>
                            <select name="category" class="form-select" required>
                                <option value="ACCOMMODATION">Accommodation</option>
                                <option value="FOOD">Food &amp; Dining</option>
                                <option value="TRANSPORTATION">Transportation</option>
                                <option value="ACTIVITIES">Activities &amp; Tours</option>
                                <option value="SHOPPING">Shopping</option>
                                <option value="MISCELLANEOUS" selected>Miscellaneous</option>
                            </select>
                        </div>
                        <div class="col-6">
                            <label class="form-label small fw-semibold">Payment Method</label>
                            <select name="paymentMethod" class="form-select">
                                <option value="CARD" selected>Credit / Debit Card</option>
                                <option value="CASH">Cash</option>
                                <option value="UPI">UPI / Digital Wallet</option>
                                <option value="OTHER">Other</option>
                            </select>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Notes</label>
                        <textarea name="notes" class="form-control" rows="2" placeholder="Optional notes..."></textarea>
                    </div>
                </div>
                <div class="modal-footer border-top">
                    <button type="button" class="btn btn-light" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-tm-primary">Record Expense</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal 3: Add Checklist Item -->
<div class="modal fade" id="addChecklistModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content border-0 shadow-lg rounded-4">
            <form action="${pageContext.request.contextPath}/checklist-add" method="post">
                <input type="hidden" name="tripId" value="${trip.id}">
                <div class="modal-header border-bottom">
                    <h5 class="modal-title fw-bold"><i class="bi bi-bag-plus text-primary me-2"></i> Add Packing Item</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body p-4">
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Item Name <span class="text-danger">*</span></label>
                        <input type="text" name="itemName" class="form-control" placeholder="e.g. Passport, Universal Adapter, Hiking Boots" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Category <span class="text-danger">*</span></label>
                        <select name="category" class="form-select" required>
                            <option value="DOCUMENTS">Documents &amp; Cards</option>
                            <option value="CLOTHING">Clothing &amp; Footwear</option>
                            <option value="ELECTRONICS">Electronics &amp; Chargers</option>
                            <option value="MEDICINE">Medicine &amp; Health</option>
                            <option value="TOILETRIES">Toiletries &amp; Hygiene</option>
                            <option value="MISCELLANEOUS" selected>Miscellaneous</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer border-top">
                    <button type="button" class="btn btn-light" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-tm-primary">Add Item</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal 4: Invite Friend -->
<div class="modal fade" id="inviteMemberModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content border-0 shadow-lg rounded-4">
            <form action="${pageContext.request.contextPath}/trip-invite" method="post">
                <input type="hidden" name="tripId" value="${trip.id}">
                <div class="modal-header border-bottom">
                    <h5 class="modal-title fw-bold"><i class="bi bi-envelope-plus text-primary me-2"></i> Invite Friend to Trip</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body p-4">
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Friend's Email Address <span class="text-danger">*</span></label>
                        <input type="email" name="inviteeEmail" class="form-control" placeholder="friend@example.com" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Collaboration Permission Role</label>
                        <select name="role" class="form-select">
                            <option value="EDITOR" selected>Editor (Can add/edit itineraries and expenses)</option>
                            <option value="VIEWER">Viewer (Read-only access)</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer border-top">
                    <button type="button" class="btn btn-light" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-tm-primary">Send Invitation</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
