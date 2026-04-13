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
                <a href="ProjectPageServlet?action=redirect&project_id=${project_id}" class="nav-link active"><i class="fas fa-briefcase me-3"></i> Projects</a>

                <a href="SprintServlet?action=redirect&project_id=${project_id}" class="nav-link "><i class="fas fa-briefcase me-3"></i> Sprint</a>


                <a href="BacklogServlet?action=redirect&project_id=${project_id}" class="nav-link">
                    <i class="fas fa-list-check me-3"></i><span>Backlog</span>
                </a>

                <a href="teamAssignmentServlet?action=redirect&project_id=${project_id}" class="nav-link"><i class="fas fa-users-gear me-3"></i> Team</a>
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
            <div id="statusTab"></div>
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

                <div class="project-header-mini mb-4">
                    <div class="header-main-row">
                        <h1 id="projectName-badge"></h1>

                        <div id="id-badge" class="id-badge">
                            <i class="fas fa-fingerprint me-1"></i> ID: ${project.projectId}
                        </div>

                        <span id="status-badge" class="status-badge status-active">
                            ${project.projectStatus}
                        </span>
                    </div>
                </div>

                <div class="row">
                    <div class="col-lg-12 mb-4">
                        <div class="card">
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <h6 class="m-0 fw-bold text-primary" style="font-size: 0.9rem;">General Information</h6>
                                <div class="d-flex gap-2">
                                    <button id="manageDocBtn" 
                                            class="btn btn-sm btn-primary px-2 rounded-pill fw-bold"
                                            <i class="fas fa-folder-open me-1"></i> Documents
                                    </button>
                                    <c:if test="${user.user_role == 'Project Manager'}">
                                        <button id="editBtn" class="btn btn-sm btn-outline-primary px-3 rounded-pill fw-bold"
                                                style="font-size: 0.75rem;">Edit Details</button>
                                        <button id="deleteBtnHeader" class="btn btn-sm btn-delete-folder px-3 rounded-pill fw-bold" 
                                                data-bs-toggle="modal" data-bs-target="#deleteFolderModal">
                                            <i class="fas fa-folder-minus me-1"></i> Delete Project Folder
                                        </button>
                                    </c:if>
                                </div>
                            </div>
                            <div class="card-body">
                                <form id="projectForm" >
                                    <!--                                    <input type="hidden" name="processType" id="projectInfoUpdate" value="projectInfoUpdate">
                                                                        <input type="hidden" name="projectId" >-->
                                    <div class="row g-4">
                                        <div class="col-md-4">
                                            <span class="label-style">Project Name</span>
                                            <div class="input-group input-group-text bg-light">
                                                <span class="me-2">
                                                    <i class="fas fa-folder text-muted"></i>
                                                </span>

                                                <input  type="text" name="projectName" id="projName"
                                                        class="form-control editable-field fw-bold" value="${project.projectName}"
                                                        readonly>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <span class="label-style">Project Type</span>
                                            <div class="input-group input-group-text bg-light">
                                                <span class="me-2">
                                                    <i class="fas fa-briefcase text-muted"></i>
                                                </span>
                                                <select name="projectType" id="projectType" 
                                                        class="form-select editable-field">
                                                    <option value="Finance" ${project.projectType == 'Finance' ? 'selected' : ''}>Finance</option>
                                                    <option value="Academic" ${project.projectType == 'Academic' ? 'selected' : ''}>Academic</option>
                                                    <option value="Student" ${project.projectType == 'Student' ? 'selected' : ''}>Student</option>
                                                    <option value="PTJ" ${project.projectType == 'PTJ' ? 'selected' : ''}>PTJ</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <span class="label-style">Client Name</span>
                                            <div class="input-group input-group-text bg-light">
                                                <span class="me-2">
                                                    <i class="fas fa-user-tie text-muted"></i>
                                                </span>
                                                <input type="text"  name="projClient" id="projClient" 
                                                       class="form-control editable-field" 
                                                       value="${project.projectClient}" 
                                                       placeholder="Enter client or stakeholder name" readonly>
                                            </div>
                                        </div>           

                                        <div class="col-12">
                                            <span class="label-style">Project Description</span>
                                            <div class="input-group input-group-text bg-light border-end-0">
                                                <span class="me-2 align-items-start pt-2">
                                                    <i class="fas fa-align-left text-muted"></i>
                                                </span>
                                                <textarea name="projectDesc" id="projDesc" class=" px-2 form-control editable-field" rows="2"
                                                          readonly>${project.projectDesc}</textarea>
                                            </div>
                                        </div>

                                        <div class="col-md-4">
                                            <span class="label-style">Project Status</span>
                                            <div class="input-group input-group-text bg-light  ">
                                                <span class="me-2">
                                                    <i class="fas fa-circle-dot text-muted"></i> </span>


                                                <input type="text"  name="projStatus" id="projStatus" 
                                                       class="form-control editable-field" 
                                                       value="${project.projectStatus}" 
                                                       placeholder="Enter client or stakeholder name">
                                            </div>
                                        </div>


                                        <div class="col-md-4 position-relative">
                                            <span class="label-style">Start Date</span>
                                            <div class="input-group input-group-text bg-light  ">
                                                <span class="me-2">
                                                    <i class="fas fa-calendar-days text-muted"></i>
                                                </span>
                                                <input type="date" name="projStartDate" id="ProjStart" class="form-control editable-field"
                                                       value="${project.projStartDate}" readonly>
                                            </div> <span class=" position-absolute start-0 ms-3"><p id="errorMsgStartDate"></p></span>
                                        </div>

                                        <div class="col-md-4">
                                            <span class="label-style">Deadline</span>
                                            <div class="input-group input-group-text bg-light ">
                                                <span class="me-2">
                                                    <i class="fas fa-calendar-check text-muted"></i>
                                                </span>
                                                <input type="date" name="projEndDate" id="ProjEnd" class="form-control editable-field"
                                                       value="${project.projEndDate}" readonly>
                                            </div>
                                        </div>

                                        <div class="col-md-4">
                                            <span class="label-style">Date Created</span>
                                            <div class="input-group input-group-text bg-light">
                                                <span class="me-2">
                                                    <i class="fas fa-calendar-plus text-muted"></i>
                                                </span>

                                                <input type="text" name="projDate" id="projDate" class="form-control editable-field"
                                                       value="${project.projCreatedAt}" readonly>

                                            </div>
                                        </div>
                                    </div>

                                    <div id="editActions" class="d-none mt-4 pt-3 border-top text-end">
                                        <button id="formCanbtn" type="button" class="btn btn-sm btn-light px-3 me-2 fw-bold"
                                                style="font-size: 0.8rem;" onclick="toggleEdit(false)">Cancel</button>
                                        <button id="formSubbtn" type="submit" class="btn btn-sm btn-primary px-4 fw-bold"
                                                style="font-size: 0.8rem;" >Save Changes</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>



                    <!-- Delete tab -->
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
                                        <button type="button" id="deleteBtn" class="btn btn-sm btn-danger fw-bold px-3" ">
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
                </div>

                <!--Project level document pop up tab -->
                <div class="modal fade" id="projectDocModal" tabindex="-1" data-bs-backdrop="false">
                    <div class="modal-dialog">
                        <div class="modal-content shadow-lg">

                            <div class="modal-header bg-primary text-white py-3 shadow-sm" style="cursor: move;">
                                <h5 class="modal-title fw-bold">
                                    <i class="fas fa-folder-open me-2"></i>Documents: <span id="modalBacklogTitle">Initial Database
                                        Schema</span>
                                </h5>
                                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                            </div>

                            <div class="modal-body p-0">
                                <ul class="nav nav-tabs nav-fill bg-white border-bottom" id="docTabs" role="tablist">
                                    <li class="nav-item">
                                        <button class="nav-link active" id="viewNavBtn" data-bs-toggle="tab" data-bs-target="#viewPane">
                                            <i class="fas fa-th-list me-2"></i>View Files
                                        </button>
                                    </li>
                                    <c:if test="${user.user_role == 'Project Manager'}">
                                        <li class="nav-item">
                                            <button class="nav-link" data-bs-toggle="tab" id="uploadNavBtn" data-bs-target="#uploadModal">
                                                <i class="fas fa-plus-circle me-2"></i>Upload New
                                            </button>
                                        </li>
                                    </c:if>
                                </ul>


                                <div class="tab-content">
                                    <div class="tab-pane fade show active p-4" id="viewPane">
                                        <div class="table-responsive">
                                            <table class="table align-middle table-hover">
                                                <thead class="text-muted">
                                                    <tr>
                                                        <th style="width: 30%;">Document Name</th>
                                                        <th style="width: 15%;">Type</th>
                                                        <th class="action-col no-sort text-center" style="width: 20%;" class="text-end">Actions</th>
                                                    </tr>
                                                </thead>
                                                <tbody id="documentRegistry">
                                                    <!--<tr>-->
                                                    <!--                                                            <td><span class="fw-bold">ERD_Final.png</span></td>
                                                                                                                <td><span class="badge bg-secondary-subtle text-secondary border">PNG</span>
                                                                                                                </td>
                                                                                                                <td class="text-center pe-4">
                                                                                                                    <button class="btn btn-sm btn-light border p-1 px-2"><i class="fas fa-eye text-muted"></i></button>
                                                                                                                    <button class="btn btn-sm btn-light border p-1 px-2">
                                                                                                                        <i class="fas fa-download text-muted"></i>
                                                                                                                    </button>
                                                    <c:if test="${user.user_role == 'Project Manager'}">
                                                        <button id="docDeleteBtn" class="btn btn-sm btn-light border p-1 px-2 ms-1">
                                                            <i class="fas fa-trash-alt text-danger"></i>
                                                        </button>
                                                    </c:if>
                                                </td>
                                            </tr>-->
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>

                                    <div class="tab-pane fade p-4" id="uploadModal">
                                        <div class="mb-3">
                                            <input type="hidden" id="document_id">
                                            <label class="label-style mb-2">Document Name</label>
                                            <input type="text" id="docLabel" class="form-control"
                                                   placeholder="e.g. Project Charter">
                                        </div>

                                        <div class="mb-3">
                                            <label class="label-style mb-2">Document Category</label>
                                            <select id="docType" class="form-select">
                                                <option value="" selected disabled>Select Category...</option>
                                                <option value="Project Charter">Project Charter</option>
                                                <option value="Project Plan">Project Plan</option>
                                                <option value="Requirement Specification">Requirement Specification</option>
                                                <option value="System Architecture Document">System Architecture Document</option>
                                                <option value="Risk Register">Risk Register</option>
                                                <option value="Stakeholder Register">Stakeholder Register</option>
                                                <option value="Project Sign Off">Project Sign Off</option>
                                            </select>
                                        </div>

                                        <div class="mb-2">
                                            <label class="label-style mb-2">Attachment</label>
                                            <div id="dropZone" class="dropzone-container">
                                                <input type="file" id="actualFile" class="d-none">
                                                <img src="https://img.icons8.com/fluency/96/cloud-lighting.png" width="60"
                                                     class="mb-2">
                                                <h6 class="fw-bold mb-1">Drag and drop file here</h6>
                                                <p class="text-muted small mb-0">or click to browse from computer</p>

                                                <div id="fileInfo" class="mt-3 d-none">
                                                    <span class="badge bg-primary rounded-pill py-2 px-3">
                                                        <i class="fas fa-file-alt me-2"></i>
                                                        <span id="selectedFileName" class="fw-semibold"></span>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="modal-footer bg-light border-top">
                                <button type="button" class="btn btn-sm btn-outline-secondary fw-bold px-3"
                                        data-bs-dismiss="modal">Close</button>
                                <button type="button" id="confirmDocBtn" class="btn btn-sm btn-primary fw-bold px-4 d-none">Confirm
                                    Upload</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal fade" id="deleteDocModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content border-0 shadow">

                            <div id="initialBody">
                                <div class="modal-header bg-danger text-white">
                                    <h5 class="modal-title fw-bold" style="font-size: 1rem;">
                                        <i class="fas fa-exclamation-triangle me-2"></i> Permanent Document Deletion
                                    </h5>
                                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body p-4 text-center">
                                    <div class="mb-3">
                                        <i class="fas fa-folder-open text-danger fa-4x opacity-25"></i>
                                    </div>
                                    <h5 class="fw-bold">Destroy Document?</h5>
                                    <p class="text-muted">You are about to delete the document <strong id="documentNameDel"></strong>. This will <strong>permanently remove</strong> the uploaded files.</p>
                                    <div class="alert alert-danger p-2 mb-0">
                                        <small class="fw-bold text-uppercase"><i class="fas fa-info-circle me-1"></i> This action is irreversible.</small>
                                    </div>
                                </div>
                                <div class="modal-footer bg-light">
                                    <button type="button" class="btn btn-sm btn-secondary fw-bold" data-bs-dismiss="modal">Keep Folder</button>
                                    <button type="button" id="deletedocBtnCfm" class="btn btn-sm btn-danger fw-bold px-3" ">
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

            </div>
        </div>
    </div>
    <script>
        const projectId = "${project_id}";
        const user_role = "${user.user_role}";
        console.log(projectId);
        console.log("User Role : ", user_role);
    </script>
    <script src="js/common.js"></script>
    <script src="js/projectPage.js"></script>
</body>

</html>