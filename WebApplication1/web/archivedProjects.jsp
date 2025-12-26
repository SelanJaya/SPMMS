<%-- 
    Document   : archivedProjects
    Created on : 26 Dec 2025, 7:56:03 pm
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link href="css/common.css" rel="stylesheet">
        <style>
            /* Sidebar Active State for this page */
            #sidebar .nav-link.active-archive {
                color: white;
                background: rgba(255, 255, 255, 0.05);
                border-left: 4px solid var(--primary-blue);
            }

            /* Archive Card Design */
            .archive-card {
                transition: transform 0.2s ease, box-shadow 0.2s ease;
                border: none;
                border-top: 4px solid #64748b; /* Gray to indicate "stored" status */
                border-radius: 12px;
                background: #ffffff;
            }

            .archive-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08) !important;
            }

            /* Multiline Text Truncation for Descriptions */
            .text-truncate-3 {
                display: -webkit-box;
                -webkit-line-clamp: 3;
                -webkit-box-orient: vertical;
                overflow: hidden;
                line-height: 1.6;
                color: #64748b;
            }

            /* Success Animation for Restore */
            .animate-bounce-in {
                animation: bounceIn 0.6s ease;
            }

            @keyframes bounceIn {
                0% {
                    transform: scale(0.3);
                    opacity: 0;
                }
                50% {
                    transform: scale(1.05);
                    opacity: 1;
                }
                70% {
                    transform: scale(0.9);
                }
                100% {
                    transform: scale(1);
                }
            }

            /* Empty State Styling */
            .empty-vault-icon {
                font-size: 5rem;
                color: #e2e8f0;
                margin-bottom: 1.5rem;
            }
        </style>
    </head>
    <body>
        <nav id="sidebar">
            <div class="sidebar-brand">SPMMS CONSOLE</div>
            <div class="nav flex-column mt-3">
                <a href="dashboardServlet" class="nav-link"><i class="fas fa-chart-pie me-3"></i> Dashboard</a>
                <a href="projectPage.jsp" class="nav-link"><i class="fas fa-briefcase me-3"></i> Projects</a>
                <a href="archiveVaultServlet" class="nav-link active-archive"><i class="fas fa-box-archive me-3"></i> Archive Vault</a>

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
                <div class="small text-muted">Management / <span class="fw-semibold text-dark">Archive Vault</span></div>
                <div class="d-flex align-items-center">
                    <div class="text-end me-3">
                        <div class="small fw-bold lh-1">Douglas McGee</div>
                        <small class="text-muted" style="font-size: 10px;">Administrator</small>
                    </div>
                    <img src="https://ui-avatars.com/api/?name=DM&background=2563eb&color=fff" class="rounded-circle border" width="34">
                </div>
            </nav>

            <div class="container-fluid p-4">
                <div class="row align-items-center mb-4">
                    <div class="col-md-6">
                        <h4 class="fw-bold mb-1">Archive Vault</h4>
                        <p class="text-muted small mb-0">Recover historical project data and descriptions.</p>
                    </div>
                    <div class="col-md-6 d-flex justify-content-end gap-2">
                        <div class="input-group input-group-sm w-50">
                            <span class="input-group-text bg-white border-end-0"><i class="fas fa-search text-muted"></i></span>
                            <input type="text" id="archiveSearch" class="form-control border-start-0" placeholder="Search name or description...">
                        </div>
                        <select id="yearFilter" class="form-select form-select-sm w-25">
                            <option value="all">All Years</option>
                            <option value="2025">2025</option>
                            <option value="2024">2024</option>
                        </select>
                    </div>
                </div>

                <div class="row g-4" id="archiveGrid">
                    <c:forEach var="proj" items="${archivedProjects}">
                        <div class="col-md-6 col-lg-4 archive-item" 
                             data-name="${proj.projectName.toLowerCase()}" 
                             data-desc="${proj.projectDesc.toLowerCase()}">
                            <div class="card h-100 archive-card shadow-sm">
                                <div class="card-body p-4">
                                    <div class="d-flex justify-content-between mb-2">
                                        <h6 class="fw-bold mb-0">${proj.projectName}</h6>
                                        <small class="text-muted">${proj.projCreatedAt}</small>
                                    </div>
                                    <p class="text-truncate-3 small">
                                        ${proj.projectDesc}
                                    </p>
                                    <div class="d-flex justify-content-between align-items-center mt-4 pt-3 border-top">
                                        <a href="projectPageServlet?projectId=${proj.projectId}" class="btn btn-sm btn-light border px-3 fw-bold">View Files</a>
                                        <button class="btn btn-sm btn-outline-success border-0 fw-bold" 
                                                onclick="initRestore(${proj.projectId}, '${proj.projectName}')">
                                            <i class="fas fa-undo me-1"></i> Restore
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <c:if test="${empty archivedProjects}">
                        <div class="col-12 text-center py-5">
                            <i class="fas fa-box-archive empty-vault-icon"></i>
                            <h5 class="text-muted fw-bold">The Vault is Empty</h5>
                            <p class="text-muted small">No projects have been archived yet.</p>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>

        <div class="modal fade" id="restoreModal" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow">
                    <div id="initialRestoreBody" class="modal-body p-4 text-center">
                        <div class="mb-3 text-success opacity-50"><i class="fas fa-rotate-left fa-3x"></i></div>
                        <h5 class="fw-bold">Restore Project?</h5>
                        <p class="text-muted small">This will move <strong id="restoreProjName"></strong> back to the main dashboard.</p>
                        <div class="d-flex justify-content-center gap-2 mt-4">
                            <button class="btn btn-light btn-sm px-3 fw-bold" data-bs-dismiss="modal">Cancel</button>
                            <button class="btn btn-success btn-sm px-4 fw-bold" id="confirmRestoreBtn">Confirm Restore</button>
                        </div>
                    </div>
                    <div id="successRestoreBody" class="modal-body p-5 text-center d-none">
                        <i class="fas fa-check-circle text-success fa-5x mb-3 animate-bounce-in"></i>
                        <h5 class="fw-bold">Restored Successfully!</h5>
                        <p class="text-muted small">Redirecting to active dashboard...</p>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                                    $(document).ready(function () {
                                                        // Instant Search Discovery
                                                        $('#archiveSearch').on('input', function () {
                                                            let query = $(this).val().toLowerCase();
                                                            $('.archive-item').each(function () {
                                                                let name = $(this).data('name');
                                                                let desc = $(this).data('desc');
                                                                $(this).toggle(name.includes(query) || desc.includes(query));
                                                            });
                                                        });

                                                        // Restore Logic
                                                        let activeId = null;
                                                        const restoreModal = new bootstrap.Modal('#restoreModal');

                                                        window.initRestore = (id, name) => {
                                                            activeId = id;
                                                            $('#restoreProjName').text(name);
                                                            restoreModal.show();
                                                        };

                                                        $('#confirmRestoreBtn').click(function () {
                                                            const btn = $(this);
                                                            btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span>');

                                                            $.ajax({
                                                                url: 'projectPageServlet',
                                                                type: 'GET',
                                                                data: {processType: 'restoreProject', projectId: activeId},
                                                                dataType: 'json',
                                                                success: function (res) {
                                                                    if (res.success) {
                                                                        $('#initialRestoreBody').addClass('d-none');
                                                                        $('#successRestoreBody').removeClass('d-none');
                                                                        setTimeout(() => window.location.href = "dashboardServlet", 1800);
                                                                    }
                                                                }
                                                            });
                                                        });
                                                    });
        </script>
    </body>
</html>