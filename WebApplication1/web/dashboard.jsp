<%-- 
    Document   : dashboard
    Created on : 25 Dec 2025, 1:38:58 am
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

        <link rel="stylesheet" href="css/common.css" />
        <link rel="dashboard stylesheet" href="css/dashboard.css" />
    </head>

    <body>

        <div id="wrapper">
            <nav id="sidebar">
                <div class="sidebar-brand text-center">SPMMS </div>
                <div class="nav flex-column mt-3">
                    <a href="profileServlet" class="nav-link ">
                        <i class="fas fa-user-circle me-3"></i> Profile
                    </a>
                    <a href="dashboard.jsp" class="nav-link active"><i class="fas fa-chart-pie me-3"></i> Dashboard</a>
                    <a href="dashboardServlet?userId=${user.user_id}&processType=achivedProject" class="nav-link active-archive"><i class="fas fa-box-archive me-3"></i>
                        Archived Projects</a>
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
                    <div class="small text-muted">Management / <span class="fw-semibold text-dark">Dashboard</span></div>
                    <div class="user-info">
                        <div class="user-details d-none d-sm-block">
                            <span class="user-name">${user.username}</span>
                            <span class="user-role">${user.user_role}</span>
                        </div>
                        <img src="https://ui-avatars.com/api/?name=DM&background=2563eb&color=fff"
                             class="rounded-circle border" width="34">
                    </div>
                </nav>

                <div class="container-fluid p-4">
                    <div class="d-flex justify-content-between align-items-center pb-3 mb-4 border-bottom">
                        <div>
                            <h2 class="fw-bold text-dark mb-1" style="letter-spacing: -1.2px; font-size: 1.6rem;">Project Overview</h2>
                            <p class="text-muted small mb-0">Monitor active development cycles and track real-time project performance.</p>
                        </div>

                        <div class="d-flex align-items-center gap-3">
                            <div class="input-group input-group-sm" style="width: 320px;">
                                <span class="input-group-text bg-white border-end-0 text-muted" style="border-radius: 10px 0 0 10px; border-color: #e2e8f0;">
                                    <i class="fas fa-search"></i>
                                </span>
                                <input type="text" class="form-control border-start-0 ps-0 shadow-none" 
                                       placeholder="Search by name or description..." 
                                       style="border-radius: 0 10px 10px 0; border-color: #e2e8f0; font-size: 0.85rem; height: 38px;">
                            </div>

                            <c:if test="${userInfo.user_role == 'Project Manager'}">
                                <button class="btn btn-primary px-4 rounded-pill fw-bold shadow-sm" 
                                        data-bs-toggle="modal" 
                                        data-bs-target="#createProjectModal"
                                        style="background-color: #0d6efd; border: none; font-size: 0.85rem; height: 38px;">
                                    <i class="fas fa-plus me-2"></i>New Project
                                </button>
                            </c:if>
                        </div>
                    </div>
                </div>

                <div class="row px-3" id="projectContainer">
                    <c:forEach var="project" items="${profileInfo}">
                        <div class="col-xl-3 col-md-4 col-sm-6 mb-4">
                            <div class="project-folder-card">
                                <div class="folder-tab"></div>

                                <div class="folder-content p-4">
                                    <div class="folder-icon mb-2">
                                        <i class="fas fa-folder"></i>
                                    </div>

                                    <span class="label-style">Project</span>

                                    <h6 class="project-id">${project.projectId}</h6>

                                    <h6 class="project-title">${project.projectName}</h6>

                                    <a href="projectPageServlet?projectId=${project.projectId}"
                                       class="btn btn-sm btn-outline-primary w-100 rounded-pill fw-bold mt-3">
                                        Open Project
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>


        <div class="modal fade" id="createProjectModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content shadow">
                    <div class="modal-body p-4">
                        <h6 class="fw-bold text-dark mb-4 text-center">Initialize Project</h6>
                        <form action="dashboardServlet" method="post" id="ProjectForm">
                            <div class="mb-3">
                                <span class="label-style">Project Name</span>
                                <input type="text" name="ProjName" id="ProjName" class="form-control" placeholder="Enter title..."
                                       required>
                            </div>
                            <div class="mb-3">
                                <span class="label-style">Description</span>
                                <textarea name="ProjDesc" id="ProjDesc" class="form-control" rows="2" style="resize:none;"
                                          required></textarea>
                            </div>
                            <div class="row mb-3">
                                <div class="col-6">
                                    <span class="label-style">Start Date</span>
                                    <input type="date" name="ProjStart" id="ProjStart" class="form-control" required>
                                </div>
                                <div class="col-6">
                                    <span class="label-style">End Date</span>
                                    <input type="date" name="ProjEnd" id="ProjEnd" class="form-control" required>
                                </div>
                            </div>
                            <div class="mb-4">
                                <span class="label-style">Project Type</span>
                                <select name="ProjType" id="ProjType" class="form-select" required>
                                    <option value="Finance">Finance</option>
                                    <option value="Academic">Academic</option>
                                    <option value="Student">Student</option>
                                    <option value="PTJ">PTJ</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <span class="label-style">Project Client</span>
                                <input type="text" name="ProjClient" id="ProjClient" class="form-control" placeholder="Enter Project Client..."
                                       required>
                            </div>
                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary fw-bold py-2 rounded-pill btn-small">Create
                                    Project</button>
                                <button type="button" class="btn btn-link text-muted small text-decoration-none"
                                        data-bs-dismiss="modal">Cancel</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

        <script>
            $(document).ready(function () {
                $('#newProjectForm').on('submit', function (e) {
                    e.preventDefault();

                    // Get values from form
                    const projectName = $('#newProjName').val();

                    // Create a new folder card template
                    const newCard = `
                        <div class="col-xl-3 col-md-6 mb-4">
                            <div class="project-folder-card p-4">
                                <div class="folder-icon"><i class="fas fa-folder"></i></div>
                                <span class="label-style">Folder</span>
                                <h6 class="fw-bold text-dark mb-3" style="font-size: 0.9rem;">${projectName}</h6>
                                <a href="projectPage.html" class="btn btn-sm btn-outline-primary w-100 rounded-pill fw-bold btn-small">Open Console</a>
                            </div>
                        </div>
                    `;

                    // Append to container
                    $('#projectContainer').append(newCard);

                    // Close modal and reset form
                    $('#createProjectModal').modal('hide');
                    this.reset();
                });
            });
        </script>
    </body>

</html>
