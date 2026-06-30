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
        <link rel="stylesheet" href="css/dashboard.css" />
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
                    <a href="projectArchive.jsp" class="nav-link active-archive"><i class="fas fa-box-archive me-3"></i>
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
                <div id="statusTab"></div>

                <div id="successProcessTab" class="d-none alert alert-success alert-dismissible fade show shadow-lg border-0 d-flex align-items-center" role="alert">
                    <div class="icon-container me-3">
                        <i class="fas fa-check-circle fa-lg"></i>
                    </div>
                    <div class="message-content">
                        <h6 class="alert-heading mb-0 fw-bold" style="font-size: 0.9rem;">Project Created</h6>
                        <p id="successProcessmsg" class="mb-0 small"></p>
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>


                <div id="failedProcessTab" class="d-none alert alert-danger alert-dismissible fade show shadow-lg border-0 d-flex align-items-center" role="alert">
                    <div class="icon-container me-3">
                        <i class="fas fa-times-circle fa-lg"></i>
                    </div>
                    <div class="message-content">
                        <h6 class="alert-danger mb-0 fw-bold" style="font-size: 0.9rem;">Project Creation Failed</h6>
                        <p id="failedProcessmsg" class="mb-0 small"></p>
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>


                <nav class="top-nav">         
                    <%-- Clear message after displaying --%>

                    <div class="small text-muted">Management/<span class="fw-semibold text-dark">Dashboard</span></div>
                    <div class="user-info">
                        <div class="user-details d-none d-sm-block">
                            <span class="user-name">${userInfo.username}</span>
                            <span class="user-role">${userInfo.user_role}</span>
                        </div>
                        <img src="https://ui-avatars.com/api/?name=DM&background=2563eb&color=fff"
                             class="rounded-circle border" width="34">
                    </div>
                </nav>

                <div class="container-fluid p-3">
                    <div class="d-flex justify-content-between align-items-center pb-2 mb-2 border-bottom">
                        <div>
                            <h2 class="fw-bold text-dark mb-1" style="letter-spacing: -1.2px; font-size: 1.6rem;">Project Overview</h2>
                            <p class="text-muted small mb-1">Monitor active development cycles and track real-time project performance.</p>
                        </div>

                        <div class="d-flex align-items-center gap-3">
                            <div class="input-group input-group-sm" style="width: 320px;">
                                <span class="input-group-text bg-white border-end-0 text-muted" style="border-radius: 10px 0 0 10px; border-color: #e2e8f0;">
                                    <i class="fas fa-search"></i>
                                </span>
                                <input id="projectSearch" type="text" class="form-control border-start-0 ps-0 shadow-none" 
                                       placeholder="Search by name or description..." 
                                       style="border-radius: 0 10px 10px 0; border-color: #e2e8f0; font-size: 0.85rem; height: 38px;">
                            </div>

                            <c:if test="${userInfo.user_role == 'Project Manager'}">
                                <button id="newProjectBtn" class="btn btn-primary px-4 rounded-pill fw-bold shadow-sm" 
                                        style="background-color: #0d6efd; border: none; font-size: 0.85rem; height: 38px;">
                                    <i class="fas fa-plus me-2"></i>New Project
                                </button>
                            </c:if>
                        </div>
                    </div>
                </div>

                <div id="KPIDiv" class="d-none row px-1 mb-4"></div>

                <div class="row px-1">
                    <div class="project-section col-lg-8 mb-4">
                        <div class="row" id="projectContainer"></div>
                    </div>

                    <div class="col-lg-4">
                        <div class="card border-0 shadow-sm mb-4" style="border-radius: 12px; overflow: hidden;">
                            <div class="card-header bg-white border-bottom-0 py-3 px-4" 
                                 style="cursor: pointer;" 
                                 data-bs-toggle="collapse" 
                                 data-bs-target="#myActiveCollapse" 
                                 aria-expanded="true" 
                                 aria-controls="myActiveCollapse">
                                <h6 class="fw-bold text-dark mb-0 d-flex justify-content-between align-items-center">
                                    <div>
                                        <i class="fas fa-folder-open me-2 text-primary"></i>
                                        <span id="myActiveTitle">My Active Tasks</span>
                                    </div>
                                    <i class="fas fa-chevron-down text-muted" style="font-size: 0.8rem;"></i>
                                </h6>
                            </div>

                            <div id="myActiveCollapse" class="collapse show">
                                <div id="myActiveBody" class="card-body custom-scrollbar pt-0 px-4" style="max-height: 200px;">
                                </div>
                            </div>
                        </div>

                        <div class="card border-0 shadow-sm" style="border-radius: 12px; overflow: hidden;">
                            <div class="card-header bg-white border-bottom-0 py-3 px-4"
                                 style="cursor: pointer;" 
                                 data-bs-toggle="collapse" 
                                 data-bs-target="#recentActivitiesCollapse" 
                                 aria-expanded="true" 
                                 aria-controls="recentActivitiesCollapse">
                                <h6 class="fw-bold text-dark mb-0 d-flex justify-content-between align-items-center">
                                    <div>
                                        <i id="toggleActivitiesBtn" class="fas fa-bell me-2 text-info"></i>
                                        Recent Activities
                                    </div>
                                    <i class="fas fa-chevron-down text-muted" style="font-size: 0.8rem;"></i>

                                </h6>
                            </div>

                            <div id="recentActivitiesCollapse" class="collapse show">
                                <div class="card-body custom-scrollbar pt-0 px-4" id="activityFeedContainer" style="max-height: 260px; overflow-y: auto;">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Open Positions Modal -->
            <div class="modal fade" id="pendingRecruitmentModal" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
                    <div class="modal-content shadow-lg border-0">

                        <!-- Header -->
                        <div class="modal-header bg-primary text-white py-3 shadow-sm">
                            <h5 class="modal-title fw-bold">
                                <i class="fas fa-users me-2"></i>
                                Open Positions
                            </h5>

                            <button type="button"
                                    class="btn-close btn-close-white"
                                    data-bs-dismiss="modal">
                            </button>
                        </div>

                        <!-- Body -->
                        <div class="modal-body p-4">

                            <p class="text-muted small mb-4">
                                Review positions requiring assignment across active projects.
                            </p>

                            <div class="border rounded-3 overflow-hidden">

                                <!-- Table Header -->
                                <div class="row g-0 bg-light border-bottom fw-semibold text-secondary small py-3 px-3">
                                    <div class="col-md-4">
                                        Project
                                    </div>

                                    <div class="col-md-8">
                                        Open Positions
                                    </div>
                                </div>
                                <div id="pendingRecruitmentContainer"></div>
                            </div>

                        </div>

                        <!-- Footer -->
                        <div class="modal-footer justify-content-between">

                            <small class="text-muted">
                                3 projects require recruitment
                            </small>

                            <button type="button"
                                    class="btn btn-light border"
                                    data-bs-dismiss="modal">
                                Close
                            </button>

                        </div>

                    </div>
                </div>
            </div>



            <!--            <div class="modal fade" id="createProjectModal" tabindex="-1" aria-hidden="true">
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
                                                    <p id="errorMsgStartDate"></p>
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
                                                <button id="formSubbtn" type="submit" class="btn btn-primary fw-bold py-2 rounded-pill btn-small" >Create
                                                    Project</button>
                                                <button id="formCanbtn" type="button"  class="btn btn-link text-muted small text-decoration-none"
                                                        data-bs-dismiss="modal">Cancel</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>-->

            <div class="modal fade" id="createProjectModal" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content shadow border-0">

                        <!-- Header -->
                        <div class="modal-header bg-primary text-white py-3 shadow-sm">
                            <h5 class="modal-title fw-bold">
                                <i class="fas fa-folder-plus me-2"></i>
                                Initialize Project
                            </h5>

                            <button type="button"
                                    class="btn-close btn-close-white"
                                    data-bs-dismiss="modal">
                            </button>
                        </div>

                        <!-- Body -->
                        <div class="modal-body p-4">
                            <form  id="ProjectForm">

                                <!-- Project Name -->
                                <div class="mb-3">
                                    <span class="label-style">Project Name</span>
                                    <input type="text"
                                           name="ProjName"
                                           id="ProjName"
                                           class="form-control"
                                           placeholder="Enter project name...">

                                    <p id="errorProjName"class=" validation-message text-danger small mb-0 mt-1"></p>
                                </div>

                                <!-- Description -->
                                <div class="mb-3">
                                    <span class="label-style">Description</span>

                                    <textarea name="ProjDesc"
                                              id="ProjDesc"
                                              class="form-control"
                                              rows="3"
                                              style="resize:none;"
                                              ></textarea>

                                    <p id="errorProjDesc"
                                       class="validation-message text-danger small mb-0 mt-1"></p>
                                </div>

                                <!-- Dates -->
                                <div class="row mb-3">

                                    <div class="col-md-6">
                                        <span class="label-style">Start Date</span>

                                        <input type="date"
                                               name="ProjStart"
                                               id="ProjStart"
                                               class="form-control"
                                               >

                                        <p id="errorProjStart"
                                           class="validation-message text-danger small mb-0 mt-1"></p>
                                    </div>

                                    <div class="col-md-6">
                                        <span class="label-style">End Date</span>

                                        <input type="date"
                                               name="ProjEnd"
                                               id="ProjEnd"
                                               class="form-control"
                                               >

                                        <p id="errorProjEnd"
                                           class="validation-message text-danger small mb-0 mt-1"></p>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <p id="errorDateRange"
                                       class="validation-message"></p>
                                </div>

                                <!-- Project Type -->
                                <div class="mb-3">
                                    <span class="label-style">Project Type</span>

                                    <select name="ProjType"
                                            id="ProjType"
                                            class="form-select"
                                            >

                                        <option value="">Select Project Type</option>
                                        <option value="Finance">Finance</option>
                                        <option value="Academic">Academic</option>
                                        <option value="Student">Student</option>
                                        <option value="PTJ">PTJ</option>

                                    </select>

                                    <p id="errorProjType"
                                       class="validation-message text-danger small mb-0 mt-1"></p>
                                </div>

                                <!-- Client -->
                                <div class="mb-4">
                                    <span class="label-style">Project Client</span>

                                    <input type="text"
                                           name="ProjClient"
                                           id="ProjClient"
                                           class="form-control"
                                           placeholder="Enter project client..."
                                           >

                                    <p id="errorProjClient"
                                       class="validation-message text-danger small mb-0 mt-1"></p>
                                </div>

                                <!-- Buttons -->
                                <div class="d-grid gap-2">

                                    <button id="formSubbtn"
                                            type="submit"
                                            class="btn btn-primary fw-bold py-2 rounded-pill">
                                        Create Project
                                    </button>

                                    <button id="formCanbtn"
                                            type="button"
                                            class="btn btn-link text-muted text-decoration-none"
                                            data-bs-dismiss="modal">
                                        Cancel
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Overdue Tasks Modal -->
            <div class="modal fade" id="overdueTaskModal" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered" style="max-width: 760px;">
                    <div class="modal-content shadow-lg border-0">

                        <!-- Header -->
                        <div class="modal-header bg-danger text-white py-3 shadow-sm">
                            <h5 class="modal-title fw-bold">
                                <i class="fas fa-exclamation-triangle me-2"></i>
                                Overdue Tasks
                            </h5>

                            <button type="button"
                                    class="btn-close btn-close-white"
                                    data-bs-dismiss="modal">
                            </button>
                        </div>

                        <!-- Body -->
                        <div class="modal-body p-4">

                            <p class="text-muted small mb-4">
                                Review projects containing overdue tasks requiring attention.
                            </p>

                            <div class="border rounded-3 overflow-hidden">

                                <!-- Table Header -->
                                <div class="row g-0 bg-light border-bottom fw-semibold text-secondary small py-3 px-3">
                                    <div class="col-md-6">
                                        Project
                                    </div>

                                    <div class="col-md-6 text-center">
                                        Overdue Tasks
                                    </div>


                                </div>

                                <!-- Dynamic Content -->
                                <div id="overdueTaskContainer"></div>

                            </div>

                        </div>

                        <!-- Footer -->
                        <div class="modal-footer justify-content-between">

                            <small class="text-muted" id="overdueProjectCount">
                                0 projects require attention
                            </small>

                            <button type="button"
                                    class="btn btn-light border"
                                    data-bs-dismiss="modal">
                                Close
                            </button>

                        </div>

                    </div>
                </div>
            </div>
            
            <div class="modal fade" id="assignmentRemovalModal" data-action="removal" data-task_id="" tabindex="-1" aria-labelledby="removalModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content custom-modal border-0 shadow-lg rounded-4 overflow-hidden">

                        <div class="modal-header bg-primary text-white position-relative border-0 py-3" >
                            <h5 class="modal-title fw-bold w-100 text-center" id="removalModalLabel">
                                Remove Assignment
                            </h5>
                            <button type="button" class="btn-close btn-close-white position-absolute end-0 me-3" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>

                        <div class="modal-body px-4 pt-4 pb-3">
                            <p id="removalPromptMessage" class="text-muted mb-4">
                               
                            </p>
                            
                                <div>
                                    <label for="removalReason" class="form-label fw-bold text-dark">Removal Reason</label>
                                    <textarea class="form-control rounded-3" id="removalReason" rows="4"
                                              placeholder="e.g., Scope changes, workload balancing, reassigning to another team member, or project hold..."
                                              required></textarea>
                                </div>
                            
                        </div>

                        <div id="assignmentRemovalModalAction" class="modal-footer border-0 px-4 pb-4 pt-0">
                            <div class="row w-100 mx-0 g-2">
                                <div class="col-6">
                                    <button type="button" class="btn btn-outline-secondary w-100 fw-bold rounded-pill" data-bs-dismiss="modal">
                                        Cancel
                                    </button>
                                </div>
                                <div class="col-6">
                                    <button type="button" id="confirmAssignmentRemovalBtn" class="btn bg-primary text-white w-100 fw-bold rounded-pill">
                                        Edit
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script>

                const userRole = "${empty userInfo.user_role ? 'dont have value' : userInfo.user_role}";
                const user_id = ${userInfo.user_id};
                console.log("User Role : ", ${userInfo.user_id});
            </script>

            <script src="js/dashboard.js"></script>
            <script src="js/common.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>



    </body>
</html>
