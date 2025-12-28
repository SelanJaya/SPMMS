<%-- 
    Document   : projectArchive
    Created on : 27 Dec 2025, 10:25:38 pm
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>


        <link href="css/common.css" rel="stylesheet">
        <link href="css/projectArchive.css" rel="stylesheet">

    </head>

    <body>

        <nav id="sidebar">
            <div class="sidebar-brand">SPMMS CONSOLE</div>
            <div class="nav flex-column mt-3">
                <a href="profileServlet" class="nav-link">
                    <i class="fas fa-user-circle me-3"></i> Profile
                </a>
                <a href="dashboardServlet?userId=${user.user_id}&processType=projectInfo" class="nav-link"><i class="fas fa-chart-pie me-3"></i> Dashboard</a>
                <a href="projectArchive.jsp" class="nav-link active"><i class="fas fa-box-archive me-3"></i> Archived Projects</a>
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
                        <h2 class="fw-bold text-dark mb-1" style="letter-spacing: -1px; font-size: 1.6rem;">Archive Vault</h2>
                        <p class="text-muted small mb-0">Manage long-term project storage and history.</p>
                    </div>

                    <div class="d-flex align-items-center gap-3">
                        <div class="input-group input-group-sm" style="width: 300px;">
                            <span class="input-group-text bg-white border-end-0 text-muted">
                                <i class="fas fa-search"></i>
                            </span>
                            <input type="text" class="form-control border-start-0 ps-0 shadow-none" 
                                   placeholder="Search by name or description..." 
                                   style="font-size: 0.85rem;">
                        </div>
                    </div>
                </div>
                <!--                <div class="d-flex justify-content-between align-items-end mb-4">
                                    <div>
                                        <h4 class="fw-bold mb-1">Archive Vault</h4>
                                        <p class="text-muted small mb-0">Manage long-term project storage and history.</p>
                                    </div>
                                    <div class="input-group input-group-sm" style="width: 300px;">
                                        <span class="input-group-text bg-white border-end-0"><i class="fas fa-search text-muted"></i></span>
                                        <input type="text" id="vaultSearch" class="form-control border-start-0" placeholder="Search by name or description...">
                                    </div>
                                </div>-->

                <div class="card border-0 shadow-sm">
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table vault-table table-hover mb-0">
                                <thead>
                                    <tr>
                                        <th>Project Name</th>
                                        <th>Description Details</th>
                                        <th>Created At</th>
                                        <th>Management</th>
                                    </tr>
                                </thead>
                                <tbody id="vaultBody">
                                    <c:forEach var="project" items="${profileInfo}">
                                        <tr class="vault-row" data-search="${project.projectName.toLowerCase()} ${project.projectDesc.toLowerCase()}">
                                            <td>
                                                <a href="projectPageServlet?projectId=${project.projectId}" class="project-title-link">
                                                    <i class="fas fa-file-invoice text-muted me-2"></i>${project.projectName}
                                                </a>
                                            </td>
                                            <td>
                                                <div class="description-snippet" ondblclick="toggleDescription(this)" title="Double-click to expand">
                                                    ${project.projectDesc}
                                                </div>
                                            </td>
                                            <td>
                                                <i class="far fa-calendar-alt text-muted me-2"></i> ${project.projCreatedAt}
                                            </td>
                                            <td>
                                                <div class="dropdown">
                                                    <button class="btn btn-sm btn-light border dropdown-toggle fw-bold" type="button" data-bs-toggle="dropdown">
                                                        Manage
                                                    </button>
                                                    <ul class="dropdown-menu shadow-sm">
                                                        <li><a class="dropdown-item" href="projectPageServlet?projectId=${project.projectId}"><i class="fas fa-folder-open text-primary me-2"></i> Open Vault</a></li>
                                                        <li><button class="dropdown-item"  data-bs-toggle="modal" 
                                                                    data-bs-target="#restoreFolderModal" 
                                                                    data-project-id="${project.projectId}"
                                                                    data-project-name="${project.projectName}" ><i class="fas fa-undo-alt text-success me-2"></i> Restore Project</button></li>
                                                        <li><hr class="dropdown-divider"></li>
                                                        <li><button class="dropdown-item text-danger"  data-bs-toggle="modal" data-bs-target="#deleteFolderModal"
                                                                    data-project-id="${project.projectId}"
                                                                    data-project-name="${project.projectName}"><i class="fas fa-trash-alt me-2"></i> Delete Permanently</button></li>
                                                    </ul>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty profileInfo}">
                                        <tr>
                                            <td colspan="4" class="text-center py-5 text-muted">No archived projects found.</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!--        <div class="modal fade" id="deleteFolderModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content border-0 shadow">
        
                            <div id="initialBody">
                                <div class="modal-header bg-danger text-white">
                                    <h5 class="modal-title fw-bold" style="font-size: 1rem;">
                                        <i class="fas fa-exclamation-triangle me-2"></i> Permanent Directory Deletion
                                    </h5>
                                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body p-4 text-center">
                                    <div class="mb-3">
                                        <i class="fas fa-folder-open text-danger fa-4x opacity-25"></i>
                                    </div>
                                    <h5 class="fw-bold">Destroy Project Folder?</h5>
                                    <p class="text-muted">You are about to delete the entire project directory for <strong>${project.projectName}</strong>. This will <strong>permanently remove</strong> all uploaded files.</p>
                                    <div class="alert alert-danger p-2 mb-0">
                                        <small class="fw-bold text-uppercase"><i class="fas fa-info-circle me-1"></i> This action is irreversible.</small>
                                    </div>
                                </div>
                                <div class="modal-footer bg-light">
                                    <button type="button" class="btn btn-sm btn-secondary fw-bold" data-bs-dismiss="modal">Keep Folder</button>
                                    <button type="button" id="deleteBtn" class="btn btn-sm btn-danger fw-bold px-3" onclick="executeFolderDeletion(this)">
                                        Yes, Delete Directory
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
                </div>-->

        <div class="modal fade" id="deleteFolderModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content modal-premium">

                    <div id="initialBody" class="modal-body p-5">
                        <button type="button" class="btn-close btn-modal-close shadow-none" data-bs-dismiss="modal" aria-label="Close"></button>

                        <div class="text-center">
                            <div class="icon-box-danger">
                                <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" viewBox="0 0 16 16">
                                <path d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47M8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5"/>
                                </svg>
                            </div>

                            <h4 class="modal-title-bold mb-2">Destroy Directory</h4>
                            <p class="modal-desc-text">
                                You are about to permanently delete <span id="displayProjectName" class="text-dark fw-bold">this project</span>. All uploaded files will be lost.
                            </p>

                            <div class="status-pill-danger">
                                <span class="dot-danger"></span>
                                <span class="pill-text-danger">Action is irreversible</span>
                            </div>

                            <div class="d-grid gap-2">
                                <button type="button" id="deleteBtn" class="btn btn-confirm-destruction shadow-sm">
                                    Confirm Destruction
                                </button>
                                <button class="btn btn-link btn-cancel-link" data-bs-dismiss="modal">
                                    Keep Folder
                                </button>
                            </div>
                        </div>
                    </div>

                    <div id="successBody" class="modal-body p-5 d-none">
                        <div class="text-center">
                            <i class="fas fa-check-circle text-success fa-4x mb-3"></i>
                            <h4>Deleted Successfully</h4>
                            <p>Redirecting to dashboard...</p>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <div class="modal fade" id="restoreFolderModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content modal-premium">
                    <div id="restoreInitialBody" class="modal-body p-5">
                        <button type="button" class="btn-close btn-modal-close shadow-none" data-bs-dismiss="modal" aria-label="Close"></button>
                        <div class="text-center">
                            <div class="icon-box-success" style="background: rgba(25, 135, 84, 0.1); width: 64px; height: 64px; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 20px;">
                                <i class="fas fa-undo-alt text-success fa-2x"></i>
                            </div>
                            <h4 class="modal-title-bold mb-2">Restore Directory</h4>
                            <p class="modal-desc-text">
                                You are about to restore <span id="displayRestoreName" class="text-dark fw-bold">this project</span>.
                            </p>
                            <div class="d-grid gap-2">
                                <button type="button" id="confirmRestoreBtn" class="btn btn-success shadow-sm fw-bold py-2" style="border-radius: 12px;">
                                    Confirm Restoration
                                </button>
                            </div>
                        </div>
                    </div>
                    <div id="restoreSuccessBody" class="modal-body p-5 d-none">
                        <div class="text-center">
                            <i class="fas fa-check-circle text-success fa-5x mb-3"></i>
                            <h4 class="fw-bold">Project Restored</h4>
                            <p>Redirecting...</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="decisionModal" tabindex="-1" aria-hidden="true" data-bs-backdrop="static">
            <div class="modal-dialog modal-dialog-centered" style="max-width: 400px;"> 
                <div class="modal-content modal-premium border-0 shadow-2xl" style="border-radius: 24px;">
                    <div class="modal-body p-5 text-center">
                        <button type="button" class="btn-close position-absolute end-0 top-0 m-4 shadow-none opacity-50" data-bs-dismiss="modal" aria-label="Close"></button>

                        <div class="icon-box-success mb-4 mx-auto">
                            <i class="fas fa-check fa-2x"></i>
                        </div>

                        <h3 class="fw-bold text-dark mb-2" style="letter-spacing: -0.5px;">Process Complete</h3>
                        <p class="text-secondary mb-4 mx-auto" style="font-size: 0.95rem; line-height: 1.6;">
                            The directory has been updated. <br>Where would you like to go next?
                        </p>

                        <div class="success-pill mb-4">
                            <span class="success-pill-dot"></span>
                            <span class="success-pill-text">Update Successful</span>
                        </div>

                        <div class="d-grid gap-2">
                            <a href="dashboardServlet?processType=projectInfo" class="btn btn-success-modern py-3 fw-bold">
                                View Dashboard
                            </a>

                            <button type="button" class="btn btn-link text-decoration-none text-muted fw-bold py-2 mt-1" onclick="location.reload()" style="font-size: 0.9rem;">
                                Stay in Archive
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>



        <script>
            let currentId = null;
            let currentMode = '';
            //const actionModal = new bootstrap.Modal('#actionModal');

            // Search Filter
            $('#vaultSearch').on('keyup', function () {
                let val = $(this).val().toLowerCase();
                $('.vault-row').each(function () {
                    $(this).toggle($(this).attr('data-search').indexOf(val) > -1);
                });
            });


            document.addEventListener('DOMContentLoaded', function () {

                // Dedicated Execute Action for Delete project 
                function executeFolderDeletion(btn, projectId, method, initialId, successId, redirect) {
                    const originalContent = btn.innerHTML;

                    btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Processing...';
                    btn.disabled = true;

                    $.ajax({
                        url: 'projectPageServlet',
                        type: 'GET',
                        data: {
                            processType: method,
                            projectId: projectId
                        },
                        dataType: 'json',
                        success: function (response) {
                            if (response.success) {
                                document.getElementById(initialId).classList.add('d-none');
                                document.getElementById(successId).classList.remove('d-none');
                                setTimeout(() => {
                                    window.location.href = redirect;
                                }, 2000);
                            } else {
                                alert("Error: " + response.message);
                                btn.innerHTML = originalContent;
                                btn.disabled = false;
                            }
                        },
                        error: function () {
                            alert("Network Error: Could not reach server.");
                            alert(
                                    "Network Error: Could not reach server.\n" +
                                    "Status: " + status + "\n" +
                                    "Error: " + error + "\n" +
                                    "Request Data: " + JSON.stringify(requestData) + "\n" +
                                    "Response Text: " + xhr.responseText
                                    );
                            btn.innerHTML = originalContent;
                            btn.disabled = false;
                        }
                    });
                }

                // 2. THE HANDSHAKE (Listens for any modal to open)
                document.addEventListener('show.bs.modal', function (event) {
                    const triggerBtn = event.relatedTarget;
                    if (!triggerBtn)
                        return;

                    const projectId = triggerBtn.getAttribute('data-project-id');
                    const projectName = triggerBtn.getAttribute('data-project-name');
                    const projectStatus = triggerBtn.getAttribute('data-project-status');

                    const modalId = event.target.getAttribute('id');

                    // Handle Delete Modal (GET)
                    if (modalId === 'deleteFolderModal') {
                        document.getElementById('displayProjectName').textContent = projectName;
                        document.getElementById('initialBody').classList.remove('d-none');
                        document.getElementById('successBody').classList.add('d-none');

                        document.getElementById('deleteBtn').onclick = function () {
                            executeFolderDeletion(this, projectId, 'deleteFolder', 'initialBody', 'successBody', 'dashboardServlet?processType=projectInfo');
                        };
                    }


                    if (modalId === 'restoreFolderModal') {
                        // 1. Extract multiple details from the button
                        const projectId = triggerBtn.getAttribute('data-project-id');
                        const projectName = triggerBtn.getAttribute('data-project-name');
//                        const userId = triggerBtn.getAttribute('data-user-id'); // Assuming you added this to the button
//                        const projectTag = triggerBtn.getAttribute('data-tag');     // Assuming you added this to the button

                        document.getElementById('displayRestoreName').textContent = projectName;
                        document.getElementById('restoreInitialBody').classList.remove('d-none');
                        document.getElementById('restoreSuccessBody').classList.add('d-none');

                        document.getElementById('confirmRestoreBtn').onclick = function () {

                            // 3. Pass the object to the engine
                            executeRestoreFolder(this, projectId, 'updateStatus', 'restoreInitialBody', 'restoreSuccessBody', 'projectPageServlet?processType=updateStatus');
                        };
                    }
                });

                // Funtion for restore project
                function executeRestoreFolder(btn, projectId, method, initialId, successId, redirect) {
                    const originalContent = btn.innerHTML;
                    btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Processing...';
                    btn.disabled = true;

                    $.ajax({
                        url: 'projectPageServlet',
                        type: 'POST',
                        data: {
                            processType: "updateStatus",
                            projectId: projectId,
                        },
                        dataType: 'json',
                        success: function (response) {
                            // 1. JS reads the 'success' boolean from your Servlet's JSON
                            if (response.success === true) {

                                // 1. Find the current open modal
                                const currentModalEl = document.querySelector('.modal.show');

                                if (currentModalEl) {
                                    // Attempt to get the existing instance
                                    let currentModal = bootstrap.Modal.getInstance(currentModalEl);

                                    // FIX: If instance is null, initialize it manually before hiding
                                    if (!currentModal) {
                                        currentModal = new bootstrap.Modal(currentModalEl);
                                    }

                                    currentModal.hide();
                                }

                                // 2. Short delay for smooth transition, then show Decision Modal
                                setTimeout(() => {
                                    const decisionModal = new bootstrap.Modal(document.getElementById('decisionModal'));
                                    decisionModal.show();
                                }, 500);

                            } else {
                                // Handle the 'false' reply from Servlet
                                alert("Error: " + response.message);
                                btn.disabled = false;
                                btn.innerHTML = "Retry Action";

                            }
                        },
                        error: function (xhr, status, error) {
                            // This runs if the Servlet crashes (500 error) or URL is wrong (404)
                            console.error("Technical Error:", error);
                            alert("Connection Error: Could not reach the server.");
                            btn.disabled = false;
                        }
                    });
                }

                document.querySelectorAll('.dropdown').forEach(dropdown => {
                    dropdown.addEventListener('show.bs.dropdown', function () {
                        const menu = this.querySelector('.dropdown-menu');
                        menu.style.opacity = '1';
                        menu.style.transform = 'translateY(0)';
                    });
                });
            });

            function toggleDescription(element) {
                element.classList.toggle('expanded');
            }

        </script>
    </body>
</html>