<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reports | SPMMS Console</title>

        <script src="https://cdn.tailwindcss.com"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

        <link href="css/common.css" rel="stylesheet">
        <link rel="stylesheet" href="projectPage.css">
    </head>

    <body>
        <div id="wrapper" class="flex">
            <nav id="sidebar">
                <div class="sidebar-brand text-center">SPMMS </div>
                <div class="nav flex-column mt-3">
                    <a href="dashboard.jsp" class="nav-link"><i class="fas fa-chart-pie me-3"></i> Dashboard</a>
                    <div class="nav-divider my-2 mx-3" style="border-bottom: 1px solid rgba(255, 255, 255, 0.1);"></div>
                    <a href="ProjectPageServlet?action=redirect&project_id=${project_id}" class="nav-link"><i class="fas fa-briefcase me-3"></i> Projects</a>
                    <a href="SprintServlet?action=redirect&project_id=${project_id}" class="nav-link "><i class="fas fa-briefcase me-3"></i> Sprint</a>
                    <a href="BacklogServlet?action=redirect&project_id=${project_id}" class="nav-link ">
                        <i class="fas fa-list-check me-3"></i><span>Backlog</span>
                    </a>
                    <a href="teamAssignmentServlet?action=redirect&project_id=${project_id}" class="nav-link"><i class="fas fa-users-gear me-3"></i> Team</a>
                    <a href="projectAnalyticsServlet?action=redirect&project_id=${project_id}" class="nav-link active"><i class="fas fa-chart-line me-3"></i> Reports</a>
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
                    <div class="small text-muted">Management/Dashboard/Project_${project_id}/<span class="fw-semibold text-dark">Project_Analytics</span></div>
                    <div class="flex items-center gap-3">
                        <div class="user-info">
                            <div class="user-details d-none d-sm-block">
                                <span class="user-name">${user.username}</span>
                                <span class="user-role">${user.user_role}</span>
                            </div>
                            <img src="https://ui-avatars.com/api/?name=DM&background=2563eb&color=fff" class="rounded-circle border" width="34">
                        </div>
                    </div>
                </nav>

                <main class="container-fluid p-4 bg-gray-50">
                    <div class="row g-4 mb-4">
                        <div class="col-md-3">
                            <div class="report-card bg-gray-100 border-0 shadow-sm p-4 rounded-lg text-center h-100">
                                <p class="text-muted medium mb-1">Avg Velocity</p>
                                <h4 id="avgVelocity" class="fw-bold mb-0">28.4 pts</h4>
                            </div>
                        </div>

                        <div class="col-md-3">
                            <div class="report-card bg-gray-100 border-0 shadow-sm p-4 rounded-lg text-center h-100">
                                <p class="text-muted medium mb-1">Sprint Success Rate</p>
                                <h4 id="sprintSuccRate" class="fw-bold mb-0">92%</h4>
                            </div>
                        </div>

                        <div class="col-md-3">
                            <div class="report-card bg-gray-100 border-0 shadow-sm p-4 rounded-lg text-center h-100">
                                <p class="text-muted medium mb-1">Cycle Time</p>
                                <h4 id="cycleTime" class="fw-bold mb-0">3.2 days</h4>
                            </div>
                        </div>

                        <div class="col-md-3">
                            <div class="report-card bg-gray-100 border-0 shadow-sm p-4 rounded-lg text-center h-100">
                                <p class="text-muted medium mb-1">Rejection Rate</p>
                                <h4 id="rejectionRate" class="fw-bold mb-0 text-danger">4.2%</h4>
                            </div>
                        </div>
                    </div>

                    <div class="row g-4">
                        <div class="col-12 col-lg-6">
                            <div class="report-card bg-white border border-gray-200 shadow-sm rounded-3 p-4 transition-all hover:shadow-md">
                                <div class="border-bottom mb-4 pb-2">
                                    <h5 class="fw-bold text-dark mb-0 text-center">Team Velocity</h5>
                                </div>
                                <div style="position: relative; height: 300px; width: 100%;">
                                    <canvas id="velocityChart"></canvas>
                                </div>
                            </div>
                        </div>

                        <div class="col-12 col-lg-6">
                            <div class="report-card bg-white border border-gray-200 shadow-sm rounded-3 p-4 transition-all hover:shadow-md">
                                <div class="border-bottom mb-4 pb-2 d-flex justify-content-between align-items-center">

                                    <h5 class="fw-bold text-dark mb-0 text-center flex-grow-1">
                                        Sprint Burndown
                                    </h5>

                                    <select id="sprintSelect"
                                            class="form-select form-select-sm"
                                            style="width: 180px;">
                                    </select>

                                </div>
                                <div style="position: relative; height: 300px; width: 100%;">
                                    <canvas id="burndownChart"></canvas>
                                </div>
                            </div>
                        </div>

                        <div class="col-12 col-lg-6">
                            <div class="report-card bg-white border border-gray-200 shadow-sm rounded-3 p-4 transition-all hover:shadow-md">
                                <div class="border-bottom mb-4 pb-2">
                                    <h5 class="fw-bold text-dark mb-0 text-center">Approval Rate</h5>
                                </div>
                                <div style="position: relative; height: 300px; width: 100%;">
                                    <canvas id="approvalChart"></canvas>
                                </div>
                            </div>
                        </div>

                        <div class="col-12 col-lg-6">
                            <div class="report-card bg-white border border-gray-200 shadow-sm rounded-3 p-4 transition-all hover:shadow-md">
                                <div class="border-bottom mb-4 pb-2">
                                    <h5 class="fw-bold text22-dark mb-0 text-center">Rework Trends</h5>
                                </div>
                                <div style="position: relative; height: 300px; width: 100%;">
                                    <canvas id="reworkChart"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>

        <script>

            const project_id = ${project_id};
        </script>
        <script src="js/projectAnalytics.js"></script>
    </body>
</html>






<!--                    <div class="row g-4">
                        <div class="col-12 col-lg-6">
                            <div class="report-card">
                                <h5 class="fw-bold text-dark mb-3 text-center">Team Velocity</h5>
                                <div style="position: relative; height: 300px; width: 100%;"><canvas id="velocityChart"></canvas></div>
                            </div>
                        </div>
                        <div class="col-12 col-lg-6">
                            <div class="report-card">
                                <h5 class="fw-bold text-dark mb-3 text-center">Sprint Burndown</h5>
                                <div style="position: relative; height: 300px; width: 100%;"><canvas id="burndownChart"></canvas></div>
                            </div>
                        </div>
                        <div class="col-12 col-lg-6">
                            <div class="report-card">
                                <h5 class="fw-bold text-dark mb-3 text-center">Approval Rate</h5>
                                <div style="position: relative; height: 300px; width: 100%;"><canvas id="approvalChart"></canvas></div>
                            </div>
                        </div>
                        <div class="col-12 col-lg-6">
                            <div class="report-card">
                                <h5 class="fw-bold text-dark mb-3 text-center">Rework Trends</h5>
                                <div style="position: relative; height: 300px; width: 100%;"><canvas id="reworkChart"></canvas></div>
                            </div>
                        </div>
                    </div>-->