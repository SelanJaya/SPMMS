<%-- 
    Document   : dashboard
    Created on : 25 Dec 2025, 1:38:58 am
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

        <link rel="dashboard stylesheet" href="css\dashboard.css" />
    </head>

    <body>

        <div id="wrapper">
            <nav id="sidebar">
                <div class="sidebar-brand">SPMMS CONSOLE</div>
                <div class="nav flex-column mt-3">
                    <a href="profileServlet" class="nav-link ">
                        <i class="fas fa-user-circle me-3"></i> Profile
                    </a>
                    <a href="dashboard.html" class="nav-link active"><i class="fas fa-chart-pie me-3"></i> Dashboard</a>
                </div>
                <div class="mt-auto">
                    <div class="nav-divider my-2 mx-3" style="border-bottom: 1px solid rgba(255, 255, 255, 0.1);"></div>
                    <a href="login_signUpServlet?processType=logOut" class="nav-link text-danger">
                        <i class="fas fa-sign-out-alt me-3"></i><span>Logout</span>
                    </a>
                </div>
            </nav>

            <div id="content-wrapper">
                <nav class="top-nav">
                    <div class="small text-muted">Management / <span class="fw-semibold text-dark">Dashboard</span></div>
                    <div class="user-info">
                        <div class="user-details d-none d-sm-block">
                            <span class="user-name">Douglas McGee</span>
                            <span class="user-role">Administrator</span>
                        </div>
                        <img src="https://ui-avatars.com/api/?name=DM&background=2563eb&color=fff"
                             class="rounded-circle border" width="34">
                    </div>
                </nav>

                <div class="container-fluid p-4">
                    <div class="d-flex justify-content-between align-items-center page-header-mini">
                        <h1>Project Overview</h1>
                        <button class="btn btn-primary btn-sm px-3 rounded-pill fw-bold btn-small" data-bs-toggle="modal"
                                data-bs-target="#createProjectModal">
                            <i class="fas fa-plus me-1"></i> New Project
                        </button>
                    </div>

                    <div class="row" id="projectContainer">
                        <div class="col-xl-3 col-md-6 mb-4">
                            <div class="project-folder-card p-4">
                                <div class="folder-icon"><i class="fas fa-folder"></i></div>
                                <span class="label-style">Folder</span>
                                <h6 class="fw-bold text-dark mb-3 project-name" style="font-size: 0.9rem;">Demo Project</h6>
                                <a href="projectPage.html"
                                   class="btn btn-sm btn-outline-primary w-100 rounded-pill fw-bold btn-small">Open
                                    Console</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="createProjectModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content shadow">
                    <div class="modal-body p-4">
                        <h6 class="fw-bold text-dark mb-4 text-center">Initialize Project</h6>
                        <form id="newProjectForm">
                            <div class="mb-3">
                                <span class="label-style">Project Name</span>
                                <input type="text" id="newProjName" class="form-control" placeholder="Enter title..."
                                       required>
                            </div>
                            <div class="mb-3">
                                <span class="label-style">Description</span>
                                <textarea id="newProjDesc" class="form-control" rows="2" style="resize:none;"
                                          required></textarea>
                            </div>
                            <div class="row mb-3">
                                <div class="col-6">
                                    <span class="label-style">Start Date</span>
                                    <input type="date" id="newProjStart" class="form-control" required>
                                </div>
                                <div class="col-6">
                                    <span class="label-style">End Date</span>
                                    <input type="date" id="newProjEnd" class="form-control" required>
                                </div>
                            </div>
                            <div class="mb-4">
                                <span class="label-style">Initial Status</span>
                                <select id="newProjStatus" class="form-select" required>
                                    <option value="Planned">Planned</option>
                                    <option value="Active">Active</option>
                                </select>
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
