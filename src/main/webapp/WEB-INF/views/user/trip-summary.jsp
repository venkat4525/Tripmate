<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trip Summary - ${trip.title}</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body { font-family: 'Segoe UI', system-ui, sans-serif; background-color: #f8fafc; color: #1e293b; }
        .summary-card { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; }
        @media print {
            .no-print { display: none !important; }
            body { background-color: #fff !important; }
            .summary-card { border: none !important; box-shadow: none !important; }
        }
    </style>
</head>
<body class="py-4">

    <div class="container" style="max-width: 900px;">
        <!-- Action Toolbar -->
        <div class="d-flex justify-content-between align-items-center mb-4 no-print">
            <a href="${pageContext.request.contextPath}/trip-details?id=${trip.id}" class="btn btn-sm btn-outline-secondary">
                <i class="bi bi-arrow-left me-1"></i> Back to Trip Details
            </a>
            <button onclick="window.print()" class="btn btn-primary btn-sm px-4 shadow-sm">
                <i class="bi bi-printer me-1"></i> Print / Save as PDF
            </button>
        </div>

        <!-- Master Print Dossier -->
        <div class="summary-card p-4 p-md-5 shadow-sm">
            <!-- Header -->
            <div class="d-flex justify-content-between align-items-start border-bottom pb-4 mb-4">
                <div>
                    <div class="d-flex align-items-center gap-2 mb-1">
                        <i class="bi bi-compass-fill fs-3 text-primary"></i>
                        <span class="fs-4 fw-bold">TripMate Travel Dossier</span>
                    </div>
                    <h2 class="fw-extrabold text-dark mb-1">${trip.title}</h2>
                    <div class="text-secondary">
                        <i class="bi bi-geo-alt-fill text-danger me-1"></i> ${trip.destinationName}
                        <span class="mx-2">•</span>
                        <i class="bi bi-calendar3 me-1"></i> ${trip.formattedStartDate} to ${trip.formattedEndDate} (${trip.durationDays} Days)
                    </div>
                </div>
                <div class="text-end">
                    <span class="badge bg-primary px-3 py-2 rounded-pill fw-bold">${trip.status}</span>
                </div>
            </div>

            <!-- Financial Summary Box -->
            <div class="row g-3 mb-4 p-3 bg-light rounded-3 border">
                <div class="col-4 text-center border-end">
                    <div class="text-muted small fw-semibold">Estimated Budget</div>
                    <div class="fw-bold fs-5 text-dark">$<fmt:formatNumber value="${trip.estimatedBudget}" pattern="#,##0.00"/></div>
                </div>
                <div class="col-4 text-center border-end">
                    <div class="text-muted small fw-semibold">Actual Expenses</div>
                    <div class="fw-bold fs-5 text-primary">$<fmt:formatNumber value="${totalExpenses}" pattern="#,##0.00"/></div>
                </div>
                <div class="col-4 text-center">
                    <div class="text-muted small fw-semibold">Remaining Balance</div>
                    <div class="fw-bold fs-5 ${trip.remainingBudget < 0 ? 'text-danger' : 'text-success'}">
                        $<fmt:formatNumber value="${trip.remainingBudget}" pattern="#,##0.00"/>
                    </div>
                </div>
            </div>

            <!-- Notes -->
            <c:if test="${not empty trip.notes}">
                <div class="mb-4">
                    <h6 class="fw-bold text-dark mb-1"><i class="bi bi-card-text me-1 text-primary"></i> Trip Overview &amp; Notes</h6>
                    <p class="small text-secondary mb-0 bg-light p-3 rounded-2 border">${trip.notes}</p>
                </div>
            </c:if>

            <!-- Day-wise Itinerary -->
            <div class="mb-4">
                <h5 class="fw-bold text-dark border-bottom pb-2 mb-3">
                    <i class="bi bi-calendar2-range me-2 text-primary"></i> Day-wise Itinerary
                </h5>
                <c:choose>
                    <c:when test="${empty itineraryItems}">
                        <p class="text-muted small">No scheduled itinerary items recorded.</p>
                    </c:when>
                    <c:otherwise>
                        <table class="table table-bordered table-sm small align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th style="width: 80px;">Day</th>
                                    <th style="width: 100px;">Time</th>
                                    <th>Activity &amp; Location</th>
                                    <th style="width: 100px;" class="text-end">Est. Cost</th>
                                    <th style="width: 80px;" class="text-center">Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${itineraryItems}">
                                    <tr>
                                        <td class="fw-bold text-center">Day ${item.dayNumber}</td>
                                        <td class="text-secondary">${item.timeSlot}</td>
                                        <td>
                                            <div class="fw-semibold text-dark">${item.activityTitle}</div>
                                            <div class="text-muted small">${item.location} ${not empty item.notes ? ' - ' : ''} ${item.notes}</div>
                                        </td>
                                        <td class="text-end">$<fmt:formatNumber value="${item.estimatedCost}" pattern="#,##0.00"/></td>
                                        <td class="text-center">
                                            <span class="badge ${item.completed ? 'bg-success' : 'bg-secondary'} rounded-pill" style="font-size: 0.65rem;">
                                                ${item.completed ? 'Done' : 'Pending'}
                                            </span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Itemized Expenses -->
            <div class="mb-4">
                <h5 class="fw-bold text-dark border-bottom pb-2 mb-3">
                    <i class="bi bi-wallet2 me-2 text-success"></i> Expense Breakdown
                </h5>
                <c:choose>
                    <c:when test="${empty expenses}">
                        <p class="text-muted small">No itemized expenses logged.</p>
                    </c:when>
                    <c:otherwise>
                        <table class="table table-bordered table-sm small align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th>Date</th>
                                    <th>Expense Description</th>
                                    <th>Category</th>
                                    <th>Payment Method</th>
                                    <th class="text-end">Amount</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="exp" items="${expenses}">
                                    <tr>
                                        <td class="text-secondary">${exp.formattedExpenseDate}</td>
                                        <td class="fw-semibold text-dark">${exp.title}</td>
                                        <td><span class="badge bg-light text-dark border">${exp.category}</span></td>
                                        <td class="text-secondary">${exp.paymentMethod}</td>
                                        <td class="text-end fw-bold text-dark">$<fmt:formatNumber value="${exp.amount}" pattern="#,##0.00"/></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                            <tfoot class="table-light fw-bold">
                                <tr>
                                    <td colspan="4" class="text-end">Total Expenses:</td>
                                    <td class="text-end text-primary">$<fmt:formatNumber value="${totalExpenses}" pattern="#,##0.00"/></td>
                                </tr>
                            </tfoot>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Packing Checklist -->
            <div class="mb-4">
                <h5 class="fw-bold text-dark border-bottom pb-2 mb-3">
                    <i class="bi bi-check2-square me-2 text-primary"></i> Packing Checklist
                </h5>
                <c:choose>
                    <c:when test="${empty checklistItems}">
                        <p class="text-muted small">No checklist items recorded.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="row g-2">
                            <c:forEach var="chk" items="${checklistItems}">
                                <div class="col-sm-6">
                                    <div class="p-2 bg-light rounded-2 border d-flex align-items-center gap-2 small">
                                        <i class="bi ${chk.packed ? 'bi-check-square-fill text-success' : 'bi-square text-secondary'}"></i>
                                        <span class="${chk.packed ? 'text-decoration-line-through text-muted' : 'text-dark'}">${chk.itemName}</span>
                                        <span class="badge bg-secondary ms-auto" style="font-size: 0.6rem;">${chk.category}</span>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Group Members -->
            <div>
                <h5 class="fw-bold text-dark border-bottom pb-2 mb-3">
                    <i class="bi bi-people me-2 text-info"></i> Trip Collaborators
                </h5>
                <div class="d-flex flex-wrap gap-2">
                    <c:forEach var="mem" items="${members}">
                        <div class="px-3 py-2 bg-light rounded-3 border d-flex align-items-center gap-2 small">
                            <div class="fw-bold text-dark">${mem.userName != null ? mem.userName : mem.userEmail}</div>
                            <span class="badge bg-secondary rounded-pill" style="font-size: 0.65rem;">${mem.role}</span>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div class="text-center text-muted small border-top pt-4 mt-5">
                Generated via <strong>TripMate – Smart Travel Planning System</strong> &bull; Advanced Java Programming
            </div>
        </div>
    </div>

</body>
</html>
