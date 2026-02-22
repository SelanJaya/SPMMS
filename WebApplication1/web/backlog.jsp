<%-- 
    Document   : backlog
    Created on : 21 Jan 2026, 2:57:42 am
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
        <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css">

        <link rel="stylesheet" href="css/common.css">
        <link rel="stylesheet" href="css/backlog.css">
        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
        <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sortablejs@1.15.0/Sortable.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

        <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>


    </head>

    <body>
        <div id="wrapper">
            <nav id="sidebar" title="Double-click to toggle">
                <div class="sidebar-brand">
                    <span class="brand-text text-center">SPMMS </span>
                </div>
                <div class="nav flex-column mt-3">
                    <a href="dashboard.jsp" class="nav-link"><i class="fas fa-chart-pie me-3"></i> Dashboard</a>

                    <div class="nav-divider my-2 mx-3" style="border-bottom: 1px solid rgba(255, 255, 255, 0.1);"></div>

                    <a href="projectPage.jsp" class="nav-link"><i class="fas fa-briefcase me-3"></i> Projects</a>

                    <a href="sprint.jsp" class="nav-link "><i class="fas fa-briefcase me-3"></i> Sprint</a>

                    <a href="backlog.jsp" class="nav-link active text-white">
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
                    <div class="d-flex align-items-center">
                        <button id="sidebarToggle" class="btn btn-sm text-dark me-3 border-0 bg-transparent"
                                style="font-size: 1.2rem;">
                            <i class="fas fa-bars"></i>
                        </button>
                        <div class="small text-muted">Management / <span class="fw-bold text-dark">Product Backlog</span>
                        </div>
                    </div>
                    <div class="user-info d-flex align-items-center">
                        <div class="user-details me-3 text-end d-none d-sm-block">
                            <div class="user-name">Douglas McGee</div>
                            <div class="user-role">Administrator</div>
                        </div>
                        <img src="https://ui-avatars.com/api/?name=DM&background=2563eb&color=fff"
                             class="rounded-circle border" width="34">
                    </div>
                </nav>

                <div class="container-fluid p-4">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h1 class="h3 fw-bold mb-0">Project Backlog</h1>
                        <c:if test="${user.user_role == 'Product Owner'}">
                            <button class="btn btn-primary px-4 shadow-sm" data-bs-toggle="modal"
                                    data-bs-target="#addItemModal">
                                <i class="fas fa-plus me-2"></i>New Backlog Item
                            </button>
                        </c:if>
                    </div>

                    <div class="backlog-card">
                        <table id="backlogTable" class="table align-middle" style="width:100%">
                            <thead>
                                <tr class="label-style">
                                    <th class="no-sort"></th>
                                    <th>PRIORITY</th>
                                    <th>TITLE</th>
                                    <th>DESCRIPTION</th>
                                    <th>ACCEPTANCE CRITERIA</th>
                                    <th>Mandays</th>
                                    <th>POINTS</th>
                                    <th class="no-sort">ACTION</th>
                                </tr>
                            </thead>
                            <tbody id="sortableBody">
                                <!--                                <tr>
                                                                    <td class="drag-handle"><i class="fas fa-grip-vertical"></i></td>
                                                                    <td class="priority-rank">1</td>
                                                                    <td>
                                                                        <div class="editable-cell" contenteditable="true">Initial Database Schema</div>
                                                                    </td>
                                                                    <td>
                                                                        <div class="editable-cell" contenteditable="true">Create tables for backlog and
                                                                            users.</div>
                                                                    </td>
                                                                    <td>
                                                                        <div class="editable-cell" contenteditable="true">MySQL scripts verified.</div>
                                                                    </td>
                                                                    <td>
                                                                        <div class="editable-cell" contenteditable="true">3</div>
                                                                    </td>
                                                                    <td>
                                                                        <div class="editable-cell" contenteditable="true">3</div>
                                                                    </td>
                                                                    
                                                                        <td class="text-center">
                                                                            <div class="d-flex justify-content-center align-items-center gap-2">
                                                                                <button type="button" 
                                                                                        class="btn btn-sm btn-outline-primary shadow-sm" 
                                                                                        data-bs-toggle="modal" 
                                                                                        data-bs-target="#backlogDocModal"
                                                                                        title="Manage Documents">
                                                                                    <i class="fas fa-file-alt"></i>
                                                                                </button>
                                
                                                                                <button type="button" 
                                                                                        class="btn btn-sm btn-outline-danger shadow-sm btn-delete" 
                                                                                        title="Delete Item">
                                                                                    <i class="fas fa-trash-alt"></i>
                                                                                </button>
                                                                            </div>
                                                                        </td>
                                                                    
                                                                </tr>-->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="addItemModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg" style="border-radius: 16px;">
                    <div class="modal-header border-0 pb-0 pt-4 px-4 justify-content-center">
                        <h5 class="fw-bold text-dark" style="font-size: 1.25rem;">Create Backlog Item</h5>
                    </div>

                    <div class="modal-body p-4">
                        <form id="addItemForm" action="backlogServlet" method="post">
                            <div class="mb-3">
                                <label class="label-style d-block mb-2">Backlog Title</label>
                                <input type="text" id="backlog_title" class="form-control" placeholder="Enter title..."
                                       style="border-radius: 8px; padding: 10px; border: 1px solid #e2e8f0;" required>
                            </div>

                            <div class="mb-3">
                                <label class="label-style d-block mb-2">Description</label>
                                <textarea id="backlog_description" class="form-control" rows="3" placeholder="Describe the task... example:
                                          As a [role],
                                          I want [feature],
                                          So that [business value]." style="border-radius: 8px; border: 1px solid #e2e8f0;"></textarea>
                            </div>

                            <div class="mb-3">
                                <label class="label-style d-block mb-2">Acceptance Criteria</label>
                                <textarea id="backlog_ACriteria" class="form-control" rows="2"
                                          placeholder="Conditions for completion..."
                                          style="border-radius: 8px; border: 1px solid #e2e8f0;"></textarea>
                            </div>

                            <div class="row mb-4">
                                <div class="col-md-4">
                                    <label class="label-style d-block mb-2">Story Points</label>
                                    <input type="number" id="backlog_SPts" class="form-control" value="0" min="0" max="10"
                                           style="border-radius: 8px; border: 1px solid #e2e8f0; padding: 10px;">
                                </div>

                                <div class="col-md-4">
                                    <label class="label-style d-block mb-2">Mandays</label>
                                    <input type="number" id="backlog_Mdys" class="form-control" value="0" min="0" max="10"
                                           style="border-radius: 8px; border: 1px solid #e2e8f0; padding: 10px;">
                                </div>

                                <div class="col-md-4">
                                    <label class="label-style d-block mb-2">Priority Rank</label>
                                    <input type="number" id="backlog_priority" class="form-control" placeholder="e.g. 1"
                                           style="border-radius: 8px; border: 1px solid #e2e8f0; padding: 10px;">
                                    <span id="alertPriority"></span>
                                </div>
                            </div>

                            <div class="d-grid gap-2">
                                <button type="button" class="btn btn-primary fw-bold py-2" id="confirmAddBtn"
                                        style="background-color: #2563eb; border-radius: 25px; border: none;">
                                    Create Item
                                </button>
                                <button type="button" class="btn btn-link text-muted text-decoration-none"
                                        data-bs-dismiss="modal">
                                    Cancel
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="backlogDocModal" tabindex="-1" data-bs-backdrop="false">
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
                                <button class="nav-link active" data-bs-toggle="tab" data-bs-target="#viewPane">
                                    <i class="fas fa-th-list me-2"></i>View Files
                                </button>
                            </li>
                            <li class="nav-item">
                                <button class="nav-link" data-bs-toggle="tab" data-bs-target="#uploadPane">
                                    <i class="fas fa-plus-circle me-2"></i>Upload New
                                </button>
                            </li>
                        </ul>

                        <div class="tab-content">
                            <div class="tab-pane fade show active p-4" id="viewPane">
                                <div class="table-responsive">
                                    <table class="table align-middle table-hover">
                                        <thead class="text-muted">
                                            <tr>
                                                <th style="width: 30%;">Document Name</th>
                                                <th style="width: 15%;">Type</th>
                                                <th style="width: 35%;">Path</th>
                                                <th style="width: 20%;" class="text-end">Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody id="backlogFileRegistry">
                                            <tr>
                                                <td><span class="fw-bold">ERD_Final.png</span></td>
                                                <td><span class="badge bg-secondary-subtle text-secondary border">PNG</span>
                                                </td>
                                                <td><code>/uploads/p01/arch.png</code></td>
                                                <td class="text-end">
                                                    <div class="dropdown">
                                                        <button class="btn btn-sm btn-light border dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false" data-bs-boundary="viewport">
                                                            <i class="fas fa-ellipsis-v text-secondary"></i>
                                                        </button>
                                                        <ul class="dropdown-menu dropdown-menu-end shadow-sm">
                                                            <li>
                                                                <a class="dropdown-item" href="#">
                                                                    <i class="fas fa-eye me-2 text-purple-brand"></i> View Details
                                                                </a>
                                                            </li>
                                                            <li>
                                                                <a class="dropdown-item" href="#">
                                                                    <i class="fas fa-download me-2 text-purple-brand"></i> Download
                                                                </a>
                                                            </li>
                                                            <li><hr class="dropdown-divider"></li>
                                                            <li>
                                                                <a class="dropdown-item text-danger" href="#">
                                                                    <i class="fas fa-trash-alt me-2"></i> Delete
                                                                </a>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                </td>
                                                <!--                                                <td class="text-end">
                                                                                                    <button class="btn btn-sm btn-light border"><i
                                                                                                            class="fas fa-download text-primary"></i></button>
                                                                                                    <button class="btn btn-sm btn-light border ms-1"><i
                                                                                                            class="fas fa-trash-alt text-danger"></i></button>
                                                                                                </td>-->
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            <div class="tab-pane fade p-4" id="uploadPane">
                                <div class="mb-3">
                                    <label class="label-style mb-2">Document Name</label>
                                    <input type="text" id="docNameInput" class="form-control"
                                           placeholder="e.g. Project Charter">
                                </div>

                                <div class="mb-3">
                                    <label class="label-style mb-2">Document Category</label>
                                    <select id="docCategory" class="form-select">
                                        <option value="" selected disabled>Select Category...</option>
                                        <option value="Charter">Project Charter</option>
                                        <option value="SRS">Requirements (SRS)</option>
                                        <option value="Design">Design Document</option>
                                    </select>
                                </div>

                                <div class="mb-2">
                                    <label class="label-style mb-2">Attachment</label>
                                    <div id="modalDropZone" class="dropzone-container">
                                        <input type="file" id="modalFileField" class="d-none">
                                        <img src="https://img.icons8.com/fluency/96/cloud-lighting.png" width="60"
                                             class="mb-2">
                                        <h6 class="fw-bold mb-1">Drag and drop file here</h6>
                                        <p class="text-muted small mb-0">or click to browse from computer</p>

                                        <div id="filePreview" class="mt-3 d-none">
                                            <span class="badge bg-primary rounded-pill py-2 px-3">
                                                <i class="fas fa-file-alt me-2"></i><span id="fileNameText"></span>
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
                        <button type="button" id="confirmBtn" class="btn btn-sm btn-primary fw-bold px-4 d-none">Confirm
                            Upload</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="deleteBacklogModal" tabindex="-1" aria-hidden="true">
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
                            <h5 class="fw-bold">Delete Backlog Item?</h5>
                            <p class="text-muted">You are about to delete the backlog item for <strong id="backlogTitle"></strong>. This will <strong>permanently remove</strong> all uploaded files.</p>
                            <div class="alert alert-danger p-2 mb-0">
                                <small class="fw-bold text-uppercase"><i class="fas fa-info-circle me-1"></i> This action is irreversible.</small>
                            </div>
                        </div>
                        <div class="modal-footer bg-light">
                            <button type="button" class="btn btn-sm btn-secondary fw-bold" data-bs-dismiss="modal">Keep Folder</button>
                            <button type="button" id="deleteModelBtn" class="btn btn-sm btn-danger fw-bold px-3">
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
            var projectId = ${project.projectId};
            var userRole = "${user.user_role}";
            let table;

//            table = $('#backlogTable').DataTable({
//                order: [[1, 'asc']],
//                paging: false, info: false, searching: true,
//                columnDefs: [{targets: 'no-sort', orderable: false}]
//            });

            if (userRole !== "Product Owner") {
                table.column(0).visible(false); // drag column
                table.column(7).visible(false); // action column
            }
        </script>
        <script src="js/backlog.js"></script>
    </body>

</html>