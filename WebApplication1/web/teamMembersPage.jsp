<%-- 
    Document   : teamMembers
    Created on : 26 Dec 2025, 12:48:36 am
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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

        <link href="css\teamMembersPage.css" rel="stylesheet">
    </head>

    <body>

        <div id="wrapper">
            <nav id="sidebar">
                <div class="sidebar-brand">SPMMS CONSOLE</div>
                <div class="nav flex-column mt-3">
                    <a href="dashboard.jsp" class="nav-link"><i class="fas fa-chart-pie me-3"></i> Dashboard</a>

                    <div class="nav-divider my-2 mx-3" style="border-bottom: 1px solid rgba(255, 255, 255, 0.1);"></div>

                    <a href="projectPage.jsp" class="nav-link"><i class="fas fa-briefcase me-3"></i> Projects</a>
                    <a href="ganttChart.jsp" class="nav-link"><i class="fas fa-stream me-3"></i> Timeline</a>
                    <a href="teamMembersPage.jsp" class="nav-link active"><i class="fas fa-users-gear me-3"></i> Team</a>
                    <a href="projectAnalytics.jsp" .html" class="nav-link"><i class="fas fa-chart-line me-3"></i> Reports</a>
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
                    <div class="small text-muted fw-medium">Management / <span class="text-dark fw-bold">Team</span></div>

                    <div class="user-info">
                        <div class="user-details d-none d-sm-block">
                            <span class="user-name">Douglas McGee</span>
                            <span class="user-role">Administrator</span>
                        </div>
                        <img src="https://ui-avatars.com/api/?name=Douglas+McGee&background=2563eb&color=fff"
                             class="rounded-circle border" width="36">
                    </div>
                </nav>

                <div class="container-fluid p-4">
                    <div class="d-flex justify-content-between align-items-center page-header-mini">
                        <h1>Team Management</h1>
                        <button class="btn btn-primary btn-sm px-3 rounded-pill fw-bold" data-bs-toggle="modal"
                                data-bs-target="#inviteModal">
                            <i class="fas fa-user-plus me-1"></i> Invite Member
                        </button>
                    </div>

                    <div class="role-header">Project Managers
                        <hr>
                    </div>
                    <div class="row g-3 mb-4">
                        <div class="col-xl-3 col-md-6">
                            <div class="team-card p-3 d-flex align-items-center">
                                <img src="https://ui-avatars.com/api/?name=Sarah+Connor&background=eff6ff&color=2563eb"
                                     class="avatar-md me-3">
                                <div>
                                    <div class="fw-bold text-dark small">Sarah Connor</div>
                                    <div class="text-muted" style="font-size: 11px;">sarah.c@spmms.com</div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="role-header">Product Owners
                        <hr>
                    </div>
                    <div class="row g-3 mb-4">
                        <div class="col-xl-3 col-md-6">
                            <div class="team-card p-3 d-flex align-items-center">
                                <img src="https://ui-avatars.com/api/?name=Sarah+Connor&background=eff6ff&color=2563eb"
                                     class="avatar-md me-3">
                                <div>
                                    <div class="fw-bold text-dark small">Sarah Connor</div>
                                    <div class="text-muted" style="font-size: 11px;">sarah.c@spmms.com</div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="role-header">Scrum Master
                        <hr>
                    </div>
                    <div class="row g-3 mb-4">
                        <div class="col-xl-3 col-md-6">
                            <div class="team-card p-3 d-flex align-items-center">
                                <img src="https://ui-avatars.com/api/?name=Mike+Ross&background=f0fdf4&color=16a34a"
                                     class="avatar-md me-3">
                                <div>
                                    <div class="fw-bold text-dark small">Mike Ross</div>
                                    <div class="text-muted" style="font-size: 11px;">mike.r@spmms.com</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-3 col-md-6">
                            <div class="team-card p-3 d-flex align-items-center">
                                <img src="https://ui-avatars.com/api/?name=Harvey+Spector&background=f0fdf4&color=16a34a"
                                     class="avatar-md me-3">
                                <div>
                                    <div class="fw-bold text-dark small">Harvey Spector</div>
                                    <div class="text-muted" style="font-size: 11px;">h.spector@spmms.com</div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="role-header">Developers
                        <hr>
                    </div>
                    <div class="row g-3 mb-4">
                        <div class="col-xl-3 col-md-6">
                            <div class="team-card p-3 d-flex align-items-center">
                                <img src="https://ui-avatars.com/api/?name=Rachel+Zane&background=fef2f2&color=dc2626"
                                     class="avatar-md me-3">
                                <div>
                                    <div class="fw-bold text-dark small">Rachel Zane</div>
                                    <div class="text-muted" style="font-size: 11px;">r.zane@spmms.com</div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="role-header">Testers
                        <hr>
                    </div>
                    <div class="row g-3 mb-4">
                        <div class="col-xl-3 col-md-6">
                            <div class="team-card p-3 d-flex align-items-center">
                                <img src="https://ui-avatars.com/api/?name=Rachel+Zane&background=fef2f2&color=dc2626"
                                     class="avatar-md me-3">
                                <div>
                                    <div class="fw-bold text-dark small">Rachel Zane</div>
                                    <div class="text-muted" style="font-size: 11px;">r.zane@spmms.com</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="role-header">Designers
                        <hr>
                    </div>
                    <div class="row g-3 mb-4">
                        <div class="col-xl-3 col-md-6">
                            <div class="team-card p-3 d-flex align-items-center">
                                <img src="https://ui-avatars.com/api/?name=Rachel+Zane&background=fef2f2&color=dc2626"
                                     class="avatar-md me-3">
                                <div>
                                    <div class="fw-bold text-dark small">Rachel Zane</div>
                                    <div class="text-muted" style="font-size: 11px;">r.zane@spmms.com</div>
                                </div>
                            </div>
                        </div>
                    </div>

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
                        <form id="inviteForm">
                            <div class="mb-3">
                                <span class="label-style">Email Address</span>
                                <input type="email" class="form-control" placeholder="name@company.com" required>
                            </div>
                            <div class="mb-4">
                                <span class="label-style">Assign System Role</span>
                                <select class="form-select" required>
                                    <option value="" disabled selected>Select a role...</option>
                                    <option>Project Manager</option>
                                    <option>Developer</option>
                                    <option>Tester</option>
                                </select>
                            </div>
                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary fw-bold py-2 rounded-pill">Send
                                    Invitation</button>
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
                $('#inviteForm').on('submit', function (e) {
                    e.preventDefault();
                    alert("Invitation successfully sent!");
                    bootstrap.Modal.getInstance($('#inviteModal')).hide();
                });
            });
        </script>
    </body>

</html>
