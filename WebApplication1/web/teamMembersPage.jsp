<%-- 
    Document   : teamMembers
    Created on : 26 Dec 2025, 12:48:36 am
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <title>Team Management | SPMMS Console</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>


        <link href="css\common.css" rel="stylesheet">
        <link href="css\teamMembersPage.css" rel="stylesheet">
    </head>

    <body>

        <div id="wrapper">
            <nav id="sidebar">
                <div class="sidebar-brand text-center">SPMMS </div>
                <div class="nav flex-column mt-3">
                    <a href="dashboard.jsp" class="nav-link"><i class="fas fa-chart-pie me-3"></i> Dashboard</a>

                    <div class="nav-divider my-2 mx-3" style="border-bottom: 1px solid rgba(255, 255, 255, 0.1);"></div>

                    <a href="ProjectPageServlet?action=redirect&project_id=${project_id}" class="nav-link"><i class="fas fa-briefcase me-3"></i> Projects</a>

                    <a href="SprintServlet?action=redirect&project_id=${project_id}" class="nav-link "><i class="fas fa-briefcase me-3"></i> Sprint</a>


                    <a href="BacklogServlet?action=redirect&project_id=${project_id}" class="nav-link">
                        <i class="fas fa-list-check me-3"></i><span>Backlog</span>
                    </a>

                    <a href="teamAssignmentServlet?action=redirect&project_id=${project_id}" class="nav-link active"><i class="fas fa-users-gear me-3"></i> Team</a>
                    <a href="projectAnalyticsServlet?action=redirect&project_id=${project_id}" class="nav-link"><i class="fas fa-chart-line me-3"></i> Reports</a>
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
                    <div class="small text-muted fw-medium">Management/Dashboard/Project_${project_id}/<span class="text-dark fw-bold">Team</span></div>

                    <div class="user-info">
                        <div class="user-details d-none d-sm-block">
                            <span class="user-name">${user.username}</span>
                            <span class="user-role">${user.user_role}</span>
                        </div>
                        <img src="https://ui-avatars.com/api/?name=DM&background=2563eb&color=fff" class="rounded-circle border" width="34">
    <!--                        <img src="https://ui-avatars.com/api/?name=${user.username}&background=eff6ff&color=2563eb"
                                 class="avatar-md me-3" />-->
                        <!--                        <img src="https://ui-avatars.com/api/?name=DM&background=2563eb&color=fff"
                                                     class="rounded-circle border" width="34"> -->
                    </div>
                </nav>

                <div class="container-fluid p-4">
                    <div class="d-flex justify-content-between align-items-center page-header-mini">
                        <h1>Team Management</h1>
                        <c:if test="${user.user_role == 'Project Manager'}">
                            <button class="btn btn-primary btn-sm px-3 rounded-pill fw-bold" data-bs-toggle="modal"
                                    data-bs-target="#inviteModal">
                                <i class="fas fa-user-plus me-1"></i> Invite Member
                            </button>
                        </c:if>
                    </div>


                    <%-- 2. The Condition: Checks if the role matches --%>
                    <div class="role-header">Project Managers
                        <hr>
                    </div>
                    <div class="row g-3 mb-4" id="projectManager_div">

                    </div>
                    <div class="role-header">Product Owners
                        <hr>
                    </div>
                    <div class="row g-3 mb-4" id="productOwner_div">

                    </div> 

                    <div class="role-header">Scrum Master
                        <hr>
                    </div>
                    <div class="row g-3 mb-4" id="scumMaster_div">

                    </div> 

                    <div class="role-header">Developers
                        <hr>
                    </div>
                    <div class="row g-3 mb-4" id="developer_div">

                    </div> 
                </div>

                <!--                <div class="modal fade" id="deleteMemberModal" tabindex="-1" aria-hidden="true">
                                    <div class="modal-dialog modal-dialog-centered">
                                        <div class="modal-content modal-premium">
                
                                            <div id="initialBody" class="modal-body p-5">
                                                <button type="button" class="btn-close btn-modal-close shadow-none" data-bs-dismiss="modal" aria-label="Close"></button>
                
                                                <div class="text-center">
                                                    <div class="icon-box-danger">
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" viewBox="0 0 16 16">
                                                        <path d="M11 5a3 3 0 1 1-6 0 3 3 0 0 1 6 0m-9 8c0 1 1 1 1 1h10s1 0 1-1-1-4-6-4-6 3-6 4m3-11a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-1 0v-2a.5.5 0 0 1 .5-.5M1.5 3.5a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-1 0v-1a.5.5 0 0 1 .5-.5m13 0a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-1 0v-1a.5.5 0 0 1 .5-.5M1 11a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-1 0v-1a.5.5 0 0 1 .5-.5m14 0a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-1 0v-1a.5.5 0 0 1 .5-.5M5 9.5a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-1 0v-2a.5.5 0 0 1 .5-.5m6 0a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-1 0v-2a.5.5 0 0 1 .5-.5"/>
                                                        </svg>
                                                    </div>
                
                                                    <h4 class="modal-title-bold mb-2">Remove Team Member</h4>
                                                    <p class="modal-desc-text">
                                                        You are about to remove <span id="displayUserName" class="text-dark fw-bold">the selected user</span> from this project. They will no longer have access to these tasks.
                                                    </p>
                
                                                    <div class="status-pill-danger">
                                                        <span class="dot-danger"></span>
                                                        <span class="pill-text-danger">Action will unasign member</span>
                                                    </div>
                
                
                                                    <button type="submit" id="deleteCfmBtn" class="btn btn-confirm-destruction shadow-sm w-100" data-bs-dismiss="modal">
                                                        Confirm Removal
                                                    </button>
                
                                                    <button class="btn btn-link btn-cancel-link"  data-bs-dismiss="modal">
                                                        Keep Member
                                                    </button>
                                                </div>
                                            </div>
                                        </div>-->

                <div class="modal fade" id="deleteMemberModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered" style="max-width: 600px;">
                        <div class="modal-content modal-premium">

                            <div id="initialBody" class="modal-body p-5">
                                <button type="button" class="btn-close btn-modal-close shadow-none" data-bs-dismiss="modal" aria-label="Close"></button>

                                <div class="text-center">
                                    <div class="icon-box-danger">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" viewBox="0 0 16 16">
                                        <path d="M11 5a3 3 0 1 1-6 0 3 3 0 0 1 6 0m-9 8c0 1 1 1 1 1h10s1 0 1-1-1-4-6-4-6 3-6 4m3-11a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-1 0v-2a.5.5 0 0 1 .5-.5M1.5 3.5a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-1 0v-1a.5.5 0 0 1 .5-.5m13 0a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-1 0v-1a.5.5 0 0 1 .5-.5M1 11a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-1 0v-1a.5.5 0 0 1 .5-.5m14 0a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-1 0v-1a.5.5 0 0 1 .5-.5M5 9.5a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-1 0v-2a.5.5 0 0 1 .5-.5m6 0a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-1 0v-2a.5.5 0 0 1 .5-.5"/>
                                        </svg>
                                    </div>

                                    <h4 class="modal-title-bold mb-2">Remove Team Member</h4>
                                    <p class="modal-desc-text">
                                        You are about to remove <span id="displayUserName" class="text-dark fw-bold">the selected user</span> from this project. They will no longer have access to these tasks.
                                    </p>

                                    <div class="status-pill-danger mb-4">
                                        <span class="dot-danger"></span>
                                        <span class="pill-text-danger">Action will unassign member</span>
                                    </div>

                                    <!-- Form wrapper added to enforce 'required' field validation -->
                                    <!--                                    <form id="removeMemberForm">-->
                                    <!-- Reason Input Field -->
                                    <div class="text-start mb-4">
                                        <label for="removalReason" class="form-label fw-bold" style="font-size: 0.9rem;">
                                            Reason for removal <span class="text-danger">*</span>
                                        </label>
                                        <textarea class="form-control shadow-none" id="removalReason" rows="3" placeholder="Briefly explain why this member is being removed..."></textarea>
                                        <p id="validationMessage" class="d-none ps-0 " style="color: #dc3545; font-size: 0.85rem; font-weight: 500; transition: opacity 0.2s ease-in-out;"></p>
                                    </div>
                                    <button type="submit" id="deleteCfmBtn" class="btn btn-confirm-destruction shadow-sm w-100">
                                        Confirm Removal
                                    </button>

                                    <button type="button" class="btn btn-link btn-cancel-link w-100 mt-2" data-bs-dismiss="modal">
                                        Keep Member
                                    </button>
                                    <!--                                    </form>-->
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="successBody" class="modal-body p-5 d-none">
                    <div class="text-center">
                        <i class="fas fa-check-circle text-success fa-4x mb-3"></i>
                        <h4>Member Removed</h4>
                        <p>Updating project team...</p>
                    </div>
                </div>

            </div>
        </div>
    </div>

    <!-- Deletion pop up tab -->
    <div class="modal fade" id="removeProjectMember" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow">

                <div id="initialBody">
                    <div class="modal-header bg-danger text-white">
                        <h5 class="modal-title fw-bold" style="font-size: 1rem;">
                            <i class="fas fa-exclamation-triangle me-2"></i> Remove Project Team Member
                        </h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4 text-center">
                        <div class="mb-3">
                            <i class="fas fa-folder-open text-danger fa-4x opacity-25"></i>
                        </div>
                        <h5 class="fw-bold">Remove Project Team Member?</h5>
                        <p class="text-muted">You are about to remove <strong id="usernameRemove"></strong>. This will <strong>permanently remove</strong> all uploaded files.</p>
                        <div class="alert alert-danger p-2 mb-0">
                            <small class="fw-bold text-uppercase"><i class="fas fa-info-circle me-1"></i> This action is irreversible.</small>
                        </div>
                    </div>
                    <div class="modal-footer bg-light">
                        <button type="button" class="btn btn-sm btn-secondary fw-bold" data-bs-dismiss="modal">Cancel</button>
                        <button type="button" id="deleteModelBtn" class="btn btn-sm btn-danger fw-bold px-3">
                            Yes, Proceed
                        </button>
                    </div>
                </div>
                <div id="successBody" class="d-none">
                    <div class="modal-body p-5 text-center">
                        <div class="mb-3">
                            <i class="fas fa-check-circle text-success fa-5x animate__animated animate__bounceIn"></i>
                        </div>
                        <h5 class="fw-bold">Project Deleted</h5>
                        <p class="text-muted mb-0">The directory and all data have been successfully removed.</p>
                        <p class="small text-muted mt-2">Redirecting to Dashboard...</p>
                    </div>
                </div                                                            >
            </div>
        </div>
    </div>

    <div class="modal fade" id="inviteModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content shadow">
                <div class="modal-body p-4">
                    <div class="text-center mb-4">
                        <div class="mb-2"><i class="fas fa-paper-plane text-primary fa-2x"></i></div>
                        <h6 class="fw-bold text-dark">Invite Team Member</h6>
                        <p class="text-muted small">Collaborate with your team by sending an invite.</p>
                    </div>

                    <input type="hidden" id="finalUserInput" name="userId">
                    <input type="hidden" id="finalAssigntoEmail" name="finalAssigntoEmail">

                    <label>1. Select Role to Filter Employees</label>
                    <select id="roleSelector" class="form-select">
                        <option value="" selected disabled>Choose a role...</option>
                        <option value="Product Owner">Product Owner</option>
                        <option value="Scrum Master">Scrum Master</option>
                        <option value="Developer">Developer</option>
                    </select>

                    <div class="mb-3 mt-3 position-relative">
                        <label for="employeeSearch" class="form-label fw-bold">2. Search Available Employees</label>
                        <div class="input-group">
                            <span class="input-group-text bg-white border-end-0"><i class="bi bi-search"></i></span>
                            <input type="text" class="form-control border-start-0" id="employeeSearch" 
                                   placeholder="Type initials, name or email..." autocomplete="off">
                        </div>

                        <div id="searchSuggestions" class="list-group shadow-sm position-absolute w-100 mt-1 d-none" 
                             style="z-index: 1050; max-height: 200px; overflow-y: auto;">
                        </div>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" id="inviteMemberSubmit_btn" class="btn btn-primary fw-bold py-2 rounded-pill" data-bs-dismiss="modal">Send
                            Invitation</button>
                        <button type="button" class="btn btn-link text-muted small text-decoration-none"
                                data-bs-dismiss="modal">Cancel</button>
                    </div>

                </div>
            </div>
        </div>
    </div>
    <script>
        const project_id = ${project_id};
        console.log(project_id);
        const user_id = "${userId}";
        console.log(user_id);
        const user_role = "${user.user_role}";
        console.log(user_role);
    </script>
    <script src="js/teamAssignment.js"></script>

</body>

</html>
