<%-- 
    Document   : projectPage
    Created on : 26 Dec 2025, 12:45:57 am
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Project Console | SPMMS</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

        <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
        <link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>


        <link href="css\common.css" rel="stylesheet">
        <link href="css\projectPage.css" rel="stylesheet">
    </head>

    <body>

        <nav id="sidebar">
            <div class="sidebar-brand text-center">SPMMS </div>
            <div class="nav flex-column mt-3">
                <a href="dashboard.jsp" class="nav-link"><i class="fas fa-grid-2 me-3 fa-chart-pie"></i> Dashboard</a>
                <div class="nav-divider my-2 mx-3" style="border-bottom: 1px solid rgba(255, 255, 255, 0.1);"></div>
                <a href="projectPage.jsp" class="nav-link active"><i class="fas fa-briefcase me-3"></i> Projects</a>

                <a href="sprint.jsp" class="nav-link "><i class="fas fa-briefcase me-3"></i> Sprint</a>


                <a href="backlog.jsp" class="nav-link">
                    <i class="fas fa-list-check me-3"></i><span>Backlog</span>
                </a>

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
                                    <c:if test="${user.user_role == 'Project Manager'}">
                                        <button id="deleteBtnHeader" class="btn btn-sm btn-delete-folder px-3 rounded-pill fw-bold" 
                                                data-bs-toggle="modal" data-bs-target="#deleteFolderModal">
                                            <i class="fas fa-folder-minus me-1"></i> Delete Project Folder
                                        </button>

                                        <button id="editBtn" class="btn btn-sm btn-outline-primary px-3 rounded-pill fw-bold"
                                                style="font-size: 0.75rem;">Edit Details</button>
                                    </c:if>
                                </div>
                            </div>
                            <div class="card-body">
                                <form id="projectForm" action="projectPageServlet" method="post">
                                    <input type="hidden" name="processType" id="projectInfoUpdate" value="projectInfoUpdate">
                                    <input type="hidden" name="projectId" value="${project.projectId}">
                                    <div class="row g-4">
                                        <div class="col-md-4">
                                            <span class="label-style">Project Name</span>
                                            <input type="text" name="projectName" id="projName"
                                                   class="form-control editable-field fw-bold" value="${project.projectName}"
                                                   readonly>
                                        </div>
                                        <div class="col-md-4">
                                            <span class="label-style">Project Type</span>
                                            <select name="projectType" id="projectType" 
                                                    class="form-select editable-field">
                                                <option value="Finance" ${project.projectType == 'Finance' ? 'selected' : ''}>Finance</option>
                                                <option value="Academic" ${project.projectType == 'Academic' ? 'selected' : ''}>Academic</option>
                                                <option value="Student" ${project.projectType == 'Student' ? 'selected' : ''}>Student</option>
                                                <option value="PTJ" ${project.projectType == 'PTJ' ? 'selected' : ''}>PTJ</option>
                                            </select>
                                        </div>
                                        <div class="col-md-4">
                                            <span class="label-style">Client Name</span>
                                            <div class="input-group">
                                                <span class="input-group-text bg-light border-end-0">
                                                    <i class="fas fa-user-tie text-muted"></i>
                                                </span>
                                                <input type="text" name="projClient" id="projClient" 
                                                       class="form-control editable-field border-start-0" 
                                                       value="${project.projectClient}" 
                                                       placeholder="Enter client or stakeholder name" readonly>
                                            </div>
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
                                                    onchange="toggleArchiveWarning(this.value)" >
                                                <option value="Active" ${project.projectStatus == 'Active' ? 'selected' : ''}>Active</option>
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
                        <div class="card shadow-sm border-0">
                            <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                                <h6 class="m-0 fw-bold text-dark" style="font-size: 0.9rem;">
                                    <i class="fas fa-paperclip me-2 text-primary"></i>Project Attachments
                                </h6>
                                <c:if test="${user.user_role == 'Project Manager'}">
                                    <button type="button" class="btn btn-sm btn-primary px-3 rounded-pill fw-bold" 
                                            data-bs-toggle="modal" data-bs-target="#uploadModal">
                                        <i class="fas fa-plus me-1"></i> Add Document
                                    </button>
                                </c:if>
                            </div>
                            <div class="card-body p-0">
                                <div class="table-responsive">
                                    <table class="table align-middle mb-0" style="font-size: 0.85rem;">
                                        <thead class="bg-light text-muted">
                                            <tr>
                                                <th class="ps-4">Document Name</th>
                                                <th>Type</th>
                                                <th>Path</th>
                                                <th class="text-end pe-4">Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody id="fileRegistry">
                                            <tr>
                                                <td class="ps-4">
                                                    <div class="d-flex align-items-center">
                                                        <div class="file-icon-box bg-pdf me-2"><i class="fas fa-file-pdf"></i></div>
                                                        <div class="fw-bold">Architecture.pdf</div>
                                                    </div>
                                                </td>
                                                <td><span class="badge bg-danger-subtle text-danger">PDF</span></td>
                                                <td><code>/uploads/p01/arch.pdf</code></td>
                                                <td class="text-end pe-4">
                                                    <button class="btn btn-sm btn-light border p-1 px-2"><i class="fas fa-eye text-muted"></i></button>
                                                        <c:if test="${user.user_role == 'Project Manager'}">
                                                        <button onclick="deleteFile(this)" class="btn btn-sm btn-light border p-1 px-2 ms-1">
                                                            <i class="fas fa-trash-alt text-danger"></i>
                                                        </button>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>                        

                    <!--                    <div class="col-lg-12">
                                            <div class="card">
                                                <div class="card-header border-0">
                                                    <h6 class="m-0 fw-bold" style="font-size: 0.9rem;">Attachments</h6>
                                                </div>
                                                <div class="card-body p-0">
                                                    <div class="p-3 bg-light border-bottom">
                    <c:if test="${user.user_role == 'Project Manager'}">
                        <div class="row g-2">
                            <div class="col-md-4"><input type="text" id="docLabel"
                                                         class="form-control form-control-sm" placeholder="Label"></div>
                            <div class="col-md-5"><input type="file" id="actualFile"
                                                         class="form-control form-control-sm"></div>

                            <div class="col-md-3"><button class="btn btn-sm btn-primary w-100 fw-bold"
                                                          onclick="addFile()">Upload</button></div>
                    </c:if>
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
                    <c:if test="${user.user_role == 'Project Manager'}">
                    <button onclick="deleteFile(this)"
                            class="btn btn-sm btn-light border p-1 px-2 ms-1"><i
                            class="fas fa-trash-alt text-danger"></i></button>
                    </c:if>

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
</div>-->


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

                    <!--                    <div class="modal fade" id="uploadModal" tabindex="-1" aria-labelledby="uploadModalLabel" aria-hidden="true">
                                            <div class="modal-dialog modal-dialog-centered">
                                                <div class="modal-content border-0 shadow">
                                                    <div class="modal-header bg-primary text-white">
                                                        <h5 class="modal-title fw-bold" id="uploadModalLabel" style="font-size: 1rem;">
                                                            <i class="fas fa-file-upload me-2"></i>Upload New Document
                                                        </h5>
                                                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                                                    </div>
                                                    <div class="modal-body p-4">
                                                        <form id="uploadForm">
                                                            <div class="mb-3">
                                                                <label class="form-label fw-bold text-muted small">Document Name</label>
                                                                <input type="text" id="docLabel" class="form-control" placeholder="e.g. Project Charter">
                                                            </div>
                    
                                                            <div class="mb-3">
                                                                <label class="form-label fw-bold text-muted small">Document Category</label>
                                                                <select id="docType" class="form-select">
                                                                    <option value="" selected disabled>Select Category...</option>
                                                                    <option value="Charter">Project Charter</option>
                                                                    <option value="SRS">Requirements (SRS)</option>
                                                                    <option value="Design">Design Document (SDD)</option>
                                                                    <option value="Testing">Test Plan</option>
                                                                </select>
                                                            </div>
                    
                                                            <div class="mb-3">
                                                                <label class="form-label fw-bold text-muted small">Attachment</label>
                                                                <div id="dropZone" class="border border-2 border-dashed rounded-3 p-4 text-center bg-light transition" style="cursor: pointer;">
                                                                    <input type="file" id="actualFile" class="d-none">
                                                                    <i class="fas fa-cloud-upload-alt fa-3x text-primary mb-3"></i>
                                                                    <p class="mb-1 fw-bold">Drag and drop file here</p>
                                                                    <p class="text-muted small mb-0">or click to browse from computer</p>
                                                                    <div id="fileInfo" class="mt-3 d-none">
                                                                        <span class="badge bg-primary rounded-pill py-2 px-3">
                                                                            <i class="fas fa-file me-2"></i><span id="selectedFileName"></span>
                                                                        </span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </form>
                                                    </div>
                                                    <div class="modal-footer bg-light">
                                                        <button type="button" class="btn btn-sm btn-secondary fw-bold px-3" data-bs-dismiss="modal">Cancel</button>
                                                        <button type="button" class="btn btn-sm btn-primary fw-bold px-4" onclick="handleModalUpload()">
                                                            Confirm Upload
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>-->

                    <div class="modal fade" id="uploadModal" tabindex="-1" aria-labelledby="uploadModalLabel" aria-hidden="true" data-bs-backdrop="false">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content border-0 shadow-lg">
                                <div class="modal-header bg-primary text-white py-3" style="cursor: move;">
                                    <h5 class="modal-title fw-bold" id="uploadModalLabel" style="font-size: 1rem;">
                                        <i class="fas fa-file-upload me-2"></i>Upload New Document
                                    </h5>
                                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>

                                <div class="modal-body p-4">
                                    <form id="uploadForm">
                                        <div class="mb-3">
                                            <label class="form-label fw-bold text-muted small">Document Name</label>
                                            <input type="text" id="docLabel" class="form-control shadow-sm" placeholder="e.g. Project Charter">
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label fw-bold text-muted small">Document Category</label>
                                            <select id="docType" class="form-select shadow-sm">
                                                <option value="" selected disabled>Select Category...</option>
                                                <option value="Charter">Project Charter</option>
                                                <option value="SRS">Requirements (SRS)</option>
                                                <option value="Design">Design Document (SDD)</option>
                                                <option value="Testing">Test Plan</option>
                                            </select>
                                        </div>

                                        <div class="mb-0">
                                            <label class="form-label fw-bold text-muted small">Attachment</label>
                                            <div id="dropZone" class="custom-dropzone py-5">
                                                <input type="file" id="actualFile" class="d-none">
                                                <img src="https://img.icons8.com/fluency/96/cloud-lighting.png" width="65" alt="upload-icon" class="mb-3">

                                                <h6 class="fw-bold text-dark mb-1">Drag and drop file here</h6>
                                                <p class="text-muted small mb-0">or click to browse from computer</p>

                                                <div id="fileInfo" class="mt-3 d-none">
                                                    <span class="badge bg-primary-subtle text-primary border border-primary-subtle rounded-pill py-2 px-3">
                                                        <i class="fas fa-check-circle me-2"></i>
                                                        <span id="selectedFileName" class="fw-semibold"></span>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>

                                <div class="modal-footer bg-light border-top">
                                    <button type="button" class="btn btn-sm btn-secondary fw-bold px-3" data-bs-dismiss="modal">Cancel</button>
                                    <button type="button" class="btn btn-sm btn-primary fw-bold px-4 shadow-sm" onclick="handleModalUpload()">
                                        Confirm Upload
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <script src="js\projectPage.js"></script>
                    </body>

                    </html>