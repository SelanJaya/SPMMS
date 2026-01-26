<%-- 
    Document   : projectAnalytics
    Created on : 26 Dec 2025, 12:49:52 am
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <title>Reports | SPMMS Console</title>

        <script src="https://cdn.tailwindcss.com"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

        <link href="css\common.css" rel="stylesheet">
        <link rel="css\stylesheet" href="projectPage.css">
    </head>

    <body>

        <div id="wrapper" class="flex">
            <nav id="sidebar">
                <div class="sidebar-brand text-center">SPMMS </div>
                <div class="nav flex-column mt-3">
                    <a href="dashboard.jsp" class="nav-link"><i class="fas fa-chart-pie me-3"></i> Dashboard</a>

                    <div class="nav-divider my-2 mx-3" style="border-bottom: 1px solid rgba(255, 255, 255, 0.1);"></div>

                    <a href="projectPage.jsp" class="nav-link"><i class="fas fa-briefcase me-3"></i> Projects</a>

                    <a href="sprint.jsp" class="nav-link "><i class="fas fa-briefcase me-3"></i> Sprint</a>


                    <a href="backlog.jsp" class="nav-link">
                        <i class="fas fa-list-check me-3"></i><span>Backlog</span>
                    </a>

                    <a href="teamMembersPage.jsp" class="nav-link"><i class="fas fa-users-gear me-3"></i> Team</a>
                    <a href="projectAnalytics.jsp" class="nav-link active"><i class="fas fa-chart-line me-3"></i> Reports</a>
                    <div class="mt-auto">
                        <div class="nav-divider my-2 mx-3" style="border-bottom: 1px solid rgba(255, 255, 255, 0.1);"></div>
                        <a href="login_signUpServlet?processType=logOut" class="nav-link text-danger">
                            <i class="fas fa-sign-out-alt me-3"></i><span>Logout</span>
                        </a>
                    </div>

                </div>
            </nav>

            <div id="content-wrapper">
                <nav class="top-nav">
                    <div class="small text-muted">Analytics / <span class="fw-semibold text-dark">Performance Reports</span>
                    </div>
                    <div class="flex items-center gap-3">
                        <div class="user-info">
                            <div class="user-details d-none d-sm-block">
                                <span class="user-name">${user.username}</span>
                                <span class="user-role">${user.user_role}</span>
                            </div>
                            <img src="https://ui-avatars.com/api/?name=DM&background=2563eb&color=fff"
                                 class="rounded-circle border" width="34">
                        </div>
                    </div>
                </nav>

                <main class="container-fluid p-4">
                    <header class="d-flex justify-content-between align-items-center mb-4">
                        <div>
                            <h1 class="h3 fw-bold text-dark mb-1">Project Performance</h1>
                            <p class="text-muted small mb-0">Real-time breakdown of team velocity and sprint health</p>
                        </div>
                        <div class="d-flex gap-2">
                            <button class="btn btn-outline-secondary btn-sm fw-bold d-flex align-items-center gap-2">
                                <i class="fas fa-file-pdf text-danger"></i> Export PDF
                            </button>
                            <button class="btn btn-primary btn-sm fw-bold">
                                Refresh Data
                            </button>
                        </div>
                    </header>

                    <div class="row g-4">
                        <div class="col-12 col-lg-6">
                            <div class="card shadow-sm p-4 border-0">
                                <div class="d-flex justify-content-between align-items-center mb-4">
                                    <h5 class="fw-bold text-dark mb-0">Team Velocity</h5>
                                    <span class="badge bg-light text-dark border">Story Points</span>
                                </div>
                                <div style="height: 350px;">
                                    <canvas id="velocityChart"></canvas>
                                </div>
                            </div>
                        </div>

                        <div class="col-12 col-lg-6">
                            <div class="card shadow-sm p-4 border-0">
                                <div class="d-flex justify-content-between align-items-center mb-4">
                                    <h5 class="fw-bold text-dark mb-0">Sprint Burndown</h5>
                                    <span class="badge bg-light text-dark border">Remaining Effort</span>
                                </div>
                                <div style="height: 350px;">
                                    <canvas id="burndownChart"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>

                <script>
                    // Chart Styling to match professional theme
                    Chart.defaults.color = '#64748b';
                    Chart.defaults.font.family = "'Inter', sans-serif";

                    // Velocity Chart
                    new Chart(document.getElementById('velocityChart'), {
                        type: 'bar',
                        data: {
                            labels: ['Sprint 1', 'Sprint 2', 'Sprint 3', 'Sprint 4', 'Sprint 5'],
                            datasets: [
                                {label: 'Planned', data: [25, 30, 28, 35, 32], backgroundColor: '#94a3b8', borderRadius: 4},
                                {label: 'Completed', data: [22, 30, 32, 30, 34], backgroundColor: '#2563eb', borderRadius: 4}
                            ]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {legend: {position: 'bottom', labels: {boxWidth: 12, usePointStyle: true}}},
                            scales: {y: {grid: {display: true, drawBorder: false, color: '#f1f5f9'}}, x: {grid: {display: false}}}
                        }
                    });

                    // Burndown Chart
                    new Chart(document.getElementById('burndownChart'), {
                        type: 'line',
                        data: {
                            labels: ['Day 1', 'Day 2', 'Day 3', 'Day 4', 'Day 5', 'Day 6', 'Day 7', 'Day 8', 'Day 9', 'Day 10'],
                            datasets: [
                                {
                                    label: 'Ideal Burn',
                                    data: [40, 36, 32, 28, 24, 20, 16, 12, 8, 0],
                                    borderColor: '#cbd5e1',
                                    borderDash: [5, 5],
                                    borderWidth: 2,
                                    pointRadius: 0
                                },
                                {
                                    label: 'Actual Burn',
                                    data: [40, 38, 35, 35, 25, 22, 15, 10, 5, 0],
                                    borderColor: '#2563eb',
                                    backgroundColor: 'rgba(37, 99, 235, 0.05)',
                                    fill: true,
                                    tension: 0.4,
                                    borderWidth: 3
                                }
                            ]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {legend: {position: 'bottom', labels: {boxWidth: 12, usePointStyle: true}}},
                            scales: {y: {grid: {color: '#f1f5f9'}}, x: {grid: {display: false}}}
                        }
                    });
                </script>
                </body>

                </html>