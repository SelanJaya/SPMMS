<%-- 
    Document   : projectPage
    Created on : 26 Dec 2025, 12:45:57 am
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Project Console | SPMMS</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>


        <link href="css\common.css" rel="stylesheet">
        <link href="css\projectPage.css" rel="stylesheet">
    </head>

    <body>

        <nav id="sidebar">
            <div class="sidebar-brand">SPMMS CONSOLE</div>
            <div class="nav flex-column mt-3">
                <a href="dashboard.jsp" class="nav-link"><i class="fas fa-grid-2 me-3 fa-chart-pie"></i> Dashboard</a>
                <div class="nav-divider my-2 mx-3" style="border-bottom: 1px solid rgba(255, 255, 255, 0.1);"></div>
                <a href="projectPage.jsp" class="nav-link active"><i class="fas fa-briefcase me-3"></i> Projects</a>
                <a href="ganttChart.jsp" class="nav-link"><i class="fas fa-stream me-3"></i> Timeline</a>
                <a href="teamMembersPage.jsp" class="nav-link"><i class="fas fa-users-gear me-3"></i> Team</a>
                <a href="projectAnalytics.jsp" class="nav-link"><i class="fas fa-chart-line me-3"></i> Reports</a>
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
                <div class="small text-muted">Management / <span class="fw-semibold text-dark">Project Console</span></div>
                <div class="d-flex align-items-center">
                    <div class="text-end me-3">
                        <div class="small fw-bold lh-1">Douglas McGee</div>
                        <small class="text-muted" style="font-size: 10px;">Administrator</small>
                    </div>
                    <img src="https://ui-avatars.com/api/?name=DM&background=2563eb&color=fff" class="rounded-circle border"
                         width="34">
                </div>
            </nav>

            <div class="container-fluid p-4">

                <div class="project-header-mini">
                    <h1>${project.projectName}</h1>
                    <span class="status-badge" id="headerStatusBadge">${project.projectStatus}</span>
                </div>

                <div class="row">
                    <div class="col-lg-12 mb-4">
                        <div class="card">
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <h6 class="m-0 fw-bold text-primary" style="font-size: 0.9rem;">General Information</h6>
                                <div class="d-flex gap-2">
                                    <button id="deleteBtnHeader" class="btn btn-sm btn-delete-folder px-3 rounded-pill fw-bold" 
                                            data-bs-toggle="modal" data-bs-target="#deleteFolderModal">
                                        <i class="fas fa-folder-minus me-1"></i> Delete Project Folder
                                    </button>
                                    <button id="editBtn" class="btn btn-sm btn-outline-primary px-3 rounded-pill fw-bold"
                                            style="font-size: 0.75rem;">Edit Details</button>
                                </div>
                            </div>
                            <div class="card-body">
                                <form id="projectForm" action="projectPageServlet" method="post">
                                    <input type="hidden" name="processType" id="projectInfoUpdate" value="projectInfoUpdate">
                                    <input type="hidden" name="projectId" value="${project.projectId}">
                                    <div class="row g-4">
                                        <div class="col-md-12">
                                            <span class="label-style">Project Name</span>
                                            <input type="text" name="projectName" id="projName"
                                                   class="form-control editable-field fw-bold" value="${project.projectName}"
                                                   readonly>
                                        </div>
                                        <div class="col-12">
                                            <span class="label-style">Project Description</span>
                                            <textarea name="projectDesc" id="projDesc" class="form-control editable-field" rows="2"
                                                      readonly>${project.projectDesc}</textarea>
                                        </div>
                                        <div class="col-md-4">
                                            <span class="label-style">Project Status</span>
                                            <div id="statusView" class="fw-semibold" style="font-size: 0.9rem;">${project.projectStatus}</div>

                                            <select id="statusSelect" name="projectStatus" 
                                                    class="form-select form-select-sm editable-field d-none" 
                                                    onchange="toggleArchiveWarning(this.value)">
                                                <option value="Pending" ${project.projectStatus == 'Pending' ? 'selected' : ''}>Pending</option>
                                                <option value="Completed" ${project.projectStatus == 'Completed' ? 'selected' : ''}>Completed</option>
                                                <option value="On Hold" ${project.projectStatus == 'On Hold' ? 'selected' : ''}>On Hold</option>
                                                <option value="Archive" ${project.projectStatus == 'Archive' ? 'selected' : ''}>Archived</option>
                                            </select>

                                            <div id="archiveNotice" class="alert alert-warning mt-2 d-none">
                                                <small><i class="fas fa-info-circle me-1"></i> 
                                                    Note: Setting status to <strong>Archived</strong> will hide this project from your main dashboard.
                                                </small>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <span class="label-style">Start Date</span>
                                            <input type="date" name="projStartDate" id="projStart" class="form-control editable-field"
                                                   value="${project.projStartDate}" readonly>
                                        </div>
                                        <div class="col-md-4">
                                            <span class="label-style">Deadline</span>
                                            <input type="date" name="projEndDate" id="projEnd" class="form-control editable-field"
                                                   value="${project.projEndDate}" readonly>
                                        </div>
                                        <div class="col-md-3">
                                            <span class="label-style">Date Created</span>
                                            <div class="text-muted py-1" style="font-size: 0.9rem;">
                                                <i class="far fa-calendar-check me-1"></i>
                                                <span id="displayCreatedAt">${project.projCreatedAt}</span>
                                            </div>
                                        </div>
                                    </div>

                                    <div id="editActions" class="d-none mt-4 pt-3 border-top text-end">
                                        <button type="button" class="btn btn-sm btn-light px-3 me-2 fw-bold"
                                                style="font-size: 0.8rem;" onclick="toggleEdit(false)">Cancel</button>
                                        <button type="submit" class="btn btn-sm btn-primary px-4 fw-bold"
                                                style="font-size: 0.8rem;" >Save Changes</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-12">
                        <div class="card">
                            <div class="card-header border-0">
                                <h6 class="m-0 fw-bold" style="font-size: 0.9rem;">Attachments</h6>
                            </div>
                            <div class="card-body p-0">
                                <div class="p-3 bg-light border-bottom">
                                    <div class="row g-2">
                                        <div class="col-md-4"><input type="text" id="docLabel"
                                                                     class="form-control form-control-sm" placeholder="Label"></div>
                                        <div class="col-md-5"><input type="file" id="actualFile"
                                                                     class="form-control form-control-sm"></div>
                                        <div class="col-md-3"><button class="btn btn-sm btn-primary w-100 fw-bold"
                                                                      onclick="addFile()">Upload</button></div>
                                    </div>
                                </div>
                                <div class="table-responsive">
                                    <table class="table align-middle mb-0" style="font-size: 0.85rem;">
                                        <thead class="bg-light">
                                            <tr>
                                                <th class="ps-4">Document</th>
                                                <th>Type</th>
                                                <th>Path</th>
                                                <th class="text-end pe-4">Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody id="fileRegistry">
                                            <tr>
                                                <td class="ps-4">
                                                    <div class="d-flex align-items-center">
                                                        <div class="file-icon-box bg-pdf"><i class="fas fa-file-pdf"></i>
                                                        </div>
                                                        <div>
                                                            <div class="fw-bold">Architecture.pdf</div>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td><span class="badge bg-danger-subtle text-danger"
                                                          style="font-size: 0.65rem;">PDF</span></td>
                                                <td><code>/uploads/p01/arch.pdf</code></td>
                                                <td class="text-end pe-4">
                                                    <a href="#" class="btn btn-sm btn-light border p-1 px-2" title="View"><i
                                                            class="fas fa-eye text-muted"></i></a>
                                                    <button onclick="deleteFile(this)"
                                                            class="btn btn-sm btn-light border p-1 px-2 ms-1"><i
                                                            class="fas fa-trash-alt text-danger"></i></button>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    
    <div class="modal fade" id="deleteFolderModal" tabindex="-1" aria-hidden="true">
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
</div>


<script>
    $(document).ready(function () {
        /**
         * Toggles the interface between View Mode and Edit Mode
         */
        window.toggleEdit = function (enable) {
            if (enable) {
                $('.editable-field').prop('readonly', false)
                        .addClass('active-edit')
                        .css('pointer-events', 'auto');
                $('#projNameInput').css({'border': '', 'padding-left': '0.75rem'});
                $('#statusView').addClass('d-none');
                $('#statusSelect').removeClass('d-none').addClass('active-edit');
                $('#editActions').removeClass('d-none');
                $('#editBtn, #deleteBtnHeader').addClass('d-none'); // Hide Delete button while editing
            } else {
                $('.editable-field').prop('readonly', true)
                        .removeClass('active-edit')
                        .css('pointer-events', 'none');
                $('#projNameInput').css({'border': '1px solid transparent', 'padding-left': '0'});
                $('#statusView').removeClass('d-none');
                $('#statusSelect').addClass('d-none').removeClass('active-edit');
                $('#editActions').addClass('d-none');
                $('#editBtn, #deleteBtnHeader').removeClass('d-none');
            }
        };
        $('#editBtn').click(() => toggleEdit(true));
        /**
         * Confirms before submitting the form
         */
        window.confirmSave = function () {
            if (confirm("Are you sure you want to save these changes?")) {
                // Dynamically update UI and then submit the actual form
                saveDataLocal();
                $('#projectForm').submit();
            }
        };
        
        
        window.executeFolderDeletion = function (btn) {
            const projectId = "${project.projectId}";
// 1. Loading State on button
            const originalContent = btn.innerHTML;
            btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Processing...';
            btn.disabled = true;
// 2. AJAX Request to your Servlet
            $.ajax({
                url: 'projectPageServlet',
                type: 'GET',
                data: {
                    processType: 'deleteFolder',
                    projectId: projectId
                },
                dataType: 'json',
                success: function (response) {
                    if (response.success) {
                        // 3. SUCCESS UI SWAP
                        // Hide the header and footer area
                        document.getElementById('initialBody').classList.add('d-none');
                        // Show the success checkmark body
                        document.getElementById('successBody').classList.remove('d-none');
                        // 4. Final Redirect after 2 seconds
                        setTimeout(() => {
                            window.location.href = "dashboardServlet?processType=projectInfo";
                        }, 2000);
                    } else {
                        alert("DELETION FAILED: " + response.message);
                        btn.innerHTML = originalContent;
                        btn.disabled = false;
                    }
                },
                error: function (xhr, status, error) {
                    alert("Network Error: Could not reach the server.");
                    btn.innerHTML = originalContent;
                    btn.disabled = false;
                }
            });
        };
        
        
        function toggleArchiveWarning(val) {
            const notice = document.getElementById('archiveNotice');
            if (val === 'ARCHIVED') {
                notice.classList.remove('d-none');
            } else {
                notice.classList.add('d-none');
            }
        }


//                                                            window.deleteFile = function (btn) {
//                                                                if (confirm("Remove file?")) {
//                                                                    $(btn).closest('tr').fadeOut(200, function () {
//                                                                        $(this).remove();
//                                                                    });
//                                                                }
//                                                            };

        window.toggleArchiveWarning = function (val) {
            const notice = document.getElementById('archiveNotice');
            // We change this to 'Archive' to match your <option value="Archive">
            if (val === 'Archive') {
                notice.classList.remove('d-none');
            } else {
                notice.classList.add('d-none');
            }
        }
    });
</script>
</body>

</html>