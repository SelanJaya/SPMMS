<%-- 
    Document   : sprint
    Created on : 21 Jan 2026, 2:14:06 am
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
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" >
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
        <link href="css\common.css" rel="stylesheet">
        <link href="css\sprint.css" rel="stylesheet">

        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>


    </head>

    <body>
        <nav id="sidebar">
            <div class="sidebar-brand text-center">SPMMS</div>
            <div class="nav flex-column mt-3">
                <a href="dashboard.jsp" class="nav-link"><i class="fas fa-chart-pie me-3"></i> Dashboard</a>

                <div class="nav-divider my-2 mx-3" style="border-bottom: 1px solid rgba(255, 255, 255, 0.1);"></div>
                <a href="ProjectPageServlet?action=redirect&project_id=${project_id}" class="nav-link"><i class="fas fa-briefcase me-3"></i> Projects</a>

                <a href="SprintServlet?action=redirect&project_id=${project_id}" class="nav-link active text-white"><i class="fas fa-briefcase me-3"></i> Sprint</a>

                <a href="BacklogServlet?action=redirect&project_id=${project_id}" class="nav-link ">
                    <i class="fas fa-list-check me-3"></i><span>Backlog</span>
                </a>
                <a href="teamAssignmentServlet?action=redirect&project_id=${project_id}" class="nav-link"><i class="fas fa-users-gear me-3"></i> Team</a>
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

            <div id="successProcessTab" class="d-none alert alert-success alert-dismissible fade show shadow-lg border-0 d-flex align-items-center" role="alert">
                <div class="icon-container me-3">
                    <i class="fas fa-check-circle fa-lg"></i>
                </div>
                <div class="message-content">
                    <h6 class="alert-heading mb-0 fw-bold" style="font-size: 0.9rem;"></h6>
                    <p id="successProcessmsg" class="mb-0 small"></p>
                </div>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>


            <div id="failedProcessTab" class="d-none alert alert-danger alert-dismissible fade show shadow-lg border-0 d-flex align-items-center" role="alert">
                <div class="icon-container me-3">
                    <i class="fas fa-times-circle fa-lg"></i>
                </div>
                <div class="message-content">
                    <h6 class="alert-danger mb-0 fw-bold" style="font-size: 0.9rem;"></h6>
                    <p id="failedProcessmsg" class="mb-0 small"></p>
                </div>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>

            <div id="statusTab"></div>
            <nav class="top-nav px-4 d-flex justify-content-between align-items-center">
                <div class="small text-muted">Management/Dashboard/Project_${project_id}/<span class="fw-semibold text-dark">Sprint</span></div>
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
                <div class="d-flex justify-content-between align-items-center page-header-mini">
                    <div>
                        <h1>Scrum Board Management</h1>
                        <p class="text-muted small">Monitor sprint progress and project milestones.</p>
                    </div>
                    <div class="d-flex gap-2">
                        <div class="input-group input-group-sm" style="width: 250px;">
                            <span class="input-group-text bg-white border-end-0"><i
                                    class="fas fa-search text-muted"></i></span>
                            <input type="text" id="boardSearch" class="form-control border-start-0"
                                   placeholder="Search boards...">
                        </div>
                        <c:if test="${user.user_role == 'Scrum Master'}">
                            <button class="btn btn-primary btn-sm px-3 rounded-pill fw-bold" onclick="openModal()">
                                <i class="fas fa-plus me-1"></i> New Sprint
                            </button>
                        </c:if>
                    </div>
                </div>

                <div class="scrum-container" id="scrum-container">
                </div>
            </div>
        </div>

        <div class="modal fade" id="sprintModal" tabindex="-1" aria-labelledby="modalTitle" aria-hidden="true">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg rounded-4 overflow-hidden">

                    <!-- Header -->
                    <div class="modal-header bg-primary text-white py-3 border-0 position-relative">
                        <h5 class="modal-title fw-bold w-50 text-center" id="modalTitle">
                            Create Sprint
                        </h5>

                        <button type="button"
                                class="btn-close btn-close-white position-absolute end-0 me-3"
                                data-bs-dismiss="modal"
                                aria-label="Close">
                        </button>
                    </div>

                    <!-- Body -->
                    <div class="modal-body px-2 p-md-5">

                        <input type="hidden" id="editBoardId">

                        <!-- Sprint Name & Goal -->
                        <div class="row g-3">

                            <div class="col-md-6">
                                <label for="s_name" class="fw-bold small mb-2 text-uppercase">
                                    Sprint Name
                                </label>

                                <input type="text"
                                       class="form-control rounded-3"
                                       id="s_name"
                                       placeholder="Enter sprint name...">

                                <p id="errorSprintName"
                                   class="d-none validation-message text-danger small mb-0 mt-1"></p>
                            </div>

                            <div class="col-md-6">
                                <label for="s_goal" class="fw-bold small mb-2 text-uppercase">
                                    Goal
                                </label>

                                <textarea class="form-control rounded-3"
                                          id="s_goal"
                                          rows="1"
                                          placeholder="Sprint objective..."></textarea>

                                <p id="errorSprintGoal"
                                   class="d-none validation-message text-danger small mb-0 mt-1"></p>
                            </div>

                        </div>

                        <!-- Dates -->
                        <div class="row g-3 mt-2">

                            <div class="col-md-6">
                                <label for="s_start" class="fw-bold small mb-2 text-uppercase">
                                    Start Date
                                </label>

                                <input type="date"
                                       class="form-control rounded-3"
                                       id="s_start">

                                <p id="errorSprintStart"
                                   class="d-none validation-message text-danger small mb-0 mt-1"></p>
                            </div>

                            <div class="col-md-6">
                                <label for="s_end" class="fw-bold small mb-2 text-uppercase">
                                    End Date
                                </label>

                                <input type="date"
                                       class="form-control rounded-3"
                                       id="s_end">

                                <p id="errorSprintEnd"
                                   class="d-none validation-message text-danger small mb-0 mt-1"></p>
                            </div>

                            <div class="col-12">
                                <p id="errorSprintDateRange"
                                   class="d-none validation-message text-danger small mb-0"></p>
                            </div>

                        </div>

                        <!-- Notes -->
                        <div class="row g-3 mt-2">

                            <div class="col-md-6">
                                <label for="s_review" class="fw-bold small mb-2 text-uppercase">
                                    Review Notes
                                </label>

                                <textarea class="form-control rounded-3"
                                          id="s_review"
                                          rows="2"
                                          placeholder="Review summary..."></textarea>

                                <p id="errorSprintReview"
                                   class="d-none validation-message text-danger small mb-0 mt-1"></p>
                            </div>

                            <div class="col-md-6">
                                <label for="s_retro" class="fw-bold small mb-2 text-uppercase">
                                    Retrospective Notes
                                </label>

                                <textarea class="form-control rounded-3"
                                          id="s_retro"
                                          rows="2"
                                          placeholder="Retrospective notes..."></textarea>

                                <p id="errorSprintRetro"
                                   class="d-none validation-message text-danger small mb-0 mt-1"></p>
                            </div>

                        </div>

                        <!-- Backlog -->
                        <div class="mt-3">

                            <label for="s_backlog_links"
                                   class="fw-bold small mb-2 text-uppercase">
                                Link Backlog Items
                            </label>

                            <select class="form-select rounded-3"
                                    id="s_backlog_links"
                                    multiple="multiple"
                                    style="width:100%;">
                            </select>

                            <p id="errorSprintBacklog"
                               class="d-none validation-message text-danger small mb-0 mt-1"></p>
                        </div>

                    </div>

                    <!-- Footer -->
                    <div class="modal-footer border-0 justify-content-center pb-4 pt-0">

                        <div class="d-flex gap-3" style="width:420px; max-width:100%;">

                            <button id="sprintSubBtn" type="button"
                                    class="btn btn-outline-secondary rounded-pill flex-fill"
                                    data-bs-dismiss="modal">
                                Cancel
                            </button>

                            <button type="button"
                                    class="btn btn-primary rounded-pill fw-bold flex-fill"
                                    id="SprintSubBtn">
                                Create Sprint
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="taskModal" tabindex="-1">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg rounded-5">

                    <!-- Header -->
                    <div class="modal-header border-0 justify-content-center position-relative bg-primary bg-gradient rounded-top-5">
                        <h5 id="taskModel_title" class="fw-bold text-white text-center p-0 m-0">Add Task</h5>
                        <button id="deleteTaskBtn" class="btn btn-outline-light btn-sm position-absolute end-0 me-3" style="display:none;">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                    <input type="hidden" id="t_board_id">
                    <input type="hidden" id="t_column_id" >
                    <hr class="m-0">

                    <!-- Body -->

                    <div class="modal-body px-4 py-3 bg-light">

                        <div class="mb-4">
                            <h6 class="fw-bold text-secondary border-bottom pb-1">Task Details</h6>

                            <div class="mb-3">
                                <label class="small fw-semibold text-secondary">Task Name</label>
                                <input type="text" class="form-control rounded-4 shadow-sm" id="t_name">

                                <p id="errorTaskName"
                                   class="validation-message text-danger small mt-1 mb-0 d-none"></p>
                            </div>

                            <div class="mb-3">
                                <label class="small fw-semibold text-secondary">Description</label>
                                <textarea class="form-control rounded-4 shadow-sm"
                                          id="t_desc"
                                          rows="2"></textarea>

                                <p id="errorTaskDesc"
                                   class="validation-message text-danger small mt-1 mb-0 d-none"></p>
                            </div>

                            <div class="row g-3">

                                <div class="col-md-6">
                                    <label class="small fw-semibold text-secondary">
                                        Associated Backlog
                                    </label>

                                    <select class="form-select rounded-4 shadow-sm" id="t_backlog">
                                    </select>

                                    <p id="errorTaskBacklog"
                                       class="validation-message text-danger small mt-1 mb-0 d-none"></p>
                                </div>

                                <div class="col-md-6">
                                    <label class="small fw-semibold text-secondary">
                                        Dependency
                                    </label>

                                    <select class="form-select rounded-4 shadow-sm"
                                            id="t_dependency"
                                            multiple>
                                    </select>
                                </div>

                            </div>
                        </div>


                        <div class="mb-4">
                            <label class="small fw-semibold text-secondary mb-2">Assignment</label>
                            <div class="col-md-6">

                                <div class="position-relative w-100" id="assignmentDropdownWrapper">

                                    <div class="d-flex align-items-center justify-content-between p-2 border rounded-4 bg-white shadow-sm" style="min-height: 48px;">

                                        <div id="assignedNamesContainer" class="d-flex flex-wrap gap-2 align-items-center flex-grow-1 ps-1">
                                        </div>
                                        <p id="errorTaskAssignee"
                                           class="validation-message text-danger small mt-1 mb-0 d-none"></p>

                                        <button type="button" 
                                                class="d-none btn btn-light btn-sm rounded-circle d-flex align-items-center justify-content-center text-secondary border add-assignee-btn flex-shrink-0 ms-2" 
                                                id="manageAssignmentBtn"
                                                style="width: 32px; height: 32px;"
                                                title="Add Assignee">
                                            <i class="fas fa-plus"></i>
                                        </button>

                                    </div>

                                    <ul class="dropdown-menu shadow-lg border-0 rounded-4 p-2 mt-1 position-absolute" 
                                        id="assigneeDropdownMenu"
                                        style="width: 100%; top: 100%; left: 0; z-index: 1050;">

                                        <li class="px-2 py-1 text-muted small fw-bold text-uppercase">Available Users</li>

                                        <li class="px-2 pb-2 mb-2 border-bottom">
                                            <div class="position-relative">
                                                <i class="fas fa-search position-absolute text-secondary" style="top: 50%; left: 16px; transform: translateY(-50%); font-size: 0.8rem;"></i>
                                                <input type="text" class="form-control form-control-sm rounded-pill ps-5 bg-light border-0 shadow-none" id="assigneeSearchInput" placeholder="Search name or ID...">
                                            </div>
                                        </li>

                                        <div id="assigneeListContainer" style="max-height: 200px; overflow-y: auto; overflow-x: hidden;">

                                            <li class="assignee-item mb-1">
                                                <div class="user-list-row d-flex align-items-center justify-content-between rounded-3 px-2 py-2" style="cursor: default;">
                                                    <div class="d-flex align-items-center gap-2">
                                                        <span class="badge bg-secondary bg-opacity-10 text-secondary border rounded-pill" style="font-size: 0.65rem;">ID: 103</span>
                                                        <span class="fw-medium small assignee-name">Alice Johnson</span>
                                                    </div>
                                                    <button type="button" class="btn btn-primary btn-sm rounded-pill py-0 px-3 assign-btn flex-shrink-0" style="font-size: 0.75rem; height: 26px; width: max-content;">
                                                        Assign
                                                    </button>
                                                </div>
                                            </li>

                                            <div id="noUsersFoundMessage" class="text-center py-4 text-muted" style="display: none;">
                                                <i class="fas fa-search text-secondary mb-2" style="font-size: 1.2rem; opacity: 0.5;"></i>
                                                <p class="small mb-0 fw-medium">No users found</p>
                                                <span class="small" style="font-size: 0.75rem;">Try a different ID or name</span>
                                            </div>

                                        </div>
                                    </ul>

                                </div>
                            </div>
                        </div>

                        <div class="mb-4">
                            <h6 class="fw-bold text-secondary border-bottom pb-1">Timeline</h6>

                            <div class="row g-3">

                                <div class="col-md-6">
                                    <label class="small fw-semibold text-secondary">Start</label>

                                    <input type="date"
                                           class="form-control rounded-4 shadow-sm"
                                           id="t_start">

                                    <p id="errorTaskStart"
                                       class="validation-message text-danger small mt-1 mb-0 d-none"></p>
                                </div>

                                <div class="col-md-6">
                                    <label class="small fw-semibold text-secondary">End</label>

                                    <input type="date"
                                           class="form-control rounded-4 shadow-sm"
                                           id="t_end">

                                    <p id="errorTaskEnd"
                                       class="validation-message text-danger small mt-1 mb-0 d-none"></p>
                                </div>

                                <div class="col-12">
                                    <p id="errorTaskDateRange"
                                       class="validation-message text-danger small mt-1 mb-0 d-none"></p>
                                </div>

                            </div>
                        </div>

                        <div class="d-flex justify-content-end gap-2 mt-1">

                            <button id="taskModel_Cancle" class="btn btn-secondary rounded-pill px-4 fw-semibold shadow-sm"
                                    data-bs-dismiss="modal">
                                Cancel
                            </button>

                            <button type="button"
                                    class="btn btn-primary rounded-pill px-4 fw-bold shadow-sm"
                                    id="taskModel_Sbt">
                                <i class="fas fa-plus-circle me-2"></i>
                                Add Task
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="deleteSprintModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow">

                    <!-- Initial State -->
                    <div id="initialBody">
                        <div class="modal-header bg-danger text-white">
                            <h5 class="modal-title fw-bold" style="font-size: 1rem;">
                                <i class="fas fa-trash-alt me-2"></i> Permanent Sprint Deletion
                            </h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body p-4 text-center">
                            <div class="mb-3">
                                <i class="fas fa-tasks text-danger fa-4x opacity-25"></i>
                            </div>
                            <h5 class="fw-bold">Delete Sprint?</h5>
                            <p class="text-muted">You are about to delete <strong>${sprint.sprintName}</strong>. This will <strong>permanently remove</strong> this sprint and all associated tasks and logs.</p>
                            <div class="alert alert-danger p-2 mb-0">
                                <small class="fw-bold text-uppercase"><i class="fas fa-info-circle me-1"></i> This action is irreversible.</small>
                            </div>
                        </div>
                        <div class="modal-footer bg-light">
                            <button type="button" class="btn btn-sm btn-secondary fw-bold" data-bs-dismiss="modal">Cancel</button>
                            <button type="button" id="deleteSprintBtn" class="btn btn-sm btn-danger fw-bold px-3">
                                Yes, Delete Sprint
                            </button>
                        </div>
                    </div>

                    <!-- Success State -->
                    <div id="successBody" class="d-none">
                        <div class="modal-body p-5 text-center">
                            <div class="mb-3">
                                <i class="fas fa-check-circle text-success fa-5x animate__animated animate__bounceIn"></i>
                            </div>
                            <h5 class="fw-bold">Sprint Deleted</h5>
                            <p class="text-muted mb-0">The sprint and all associated data have been successfully removed.</p>
                            <p class="small text-muted mt-2">Refreshing view...</p>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <!--Backlog level document pop up tab -->
        <div class="modal fade" id="backlogDocModal" data-backlogId  tabindex="-1" data-bs-backdrop="false">
            <input type="hidden" id="backlog_id">
            <div class="modal-dialog">
                <div class="modal-content shadow-lg">

                    <div class="modal-header bg-primary text-white py-3 shadow-sm" style="cursor: move;">
                        <h5 class="modal-title fw-bold">
                            <i class="fas fa-folder-open me-2"></i>Documents: <span id="modalBacklogTitle">Selected Backlog Details</span>
                        </h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>

                    <div class="modal-body p-0">
                        <ul class="nav nav-tabs nav-fill bg-white border-bottom" id="docTabs" role="tablist">

                            <li class="nav-item">
                                <button class="nav-link" id="viewBacklogDetailsBtn" data-bs-toggle="tab" data-bs-target="#viewBacklogPane">
                                    <i class="fas fa-plus-circle me-2"></i> Backlog Details
                                </button>
                            </li>

                            <li class="nav-item">
                                <button class="nav-link active" id="viewDocNavBtn" data-bs-toggle="tab" data-bs-target="#viewPane">
                                    <i class="fas fa-th-list me-2"></i>View Files
                                </button>
                            </li>
                        </ul>
                        <div class="d-flex justify-content-between mx-3 mt-2">
                            <input type="text" id="searchDoc" class="form-control w-50"
                                   placeholder="Search document name...">

                            <select id="filterType" class="form-select w-25 ms-2">
                                <option value="">All Types</option>
                                <option value="Requirement">Requirement</option>
                                <option value="Design">Design</option>
                                <option value="Acceptance">Acceptance</option>
                                <option value="Technical">Technical</option>
                                <option value="Reference">Reference</option>
                                <option value="Other">Other</option>
                            </select>
                        </div>

                        <div class="tab-content">
                            <div class="tab-pane fade show active p-4" id="viewPane">
                                <div class="table-responsive">
                                    <table class="table align-middle table-hover">
                                        <thead class="text-muted">
                                            <tr>
                                                <th style="width: 30%;">Document Name</th>
                                                <th style="width: 15%;">Type</th>

                                                <th class="action-col no-sort" style="width: 20%;" class="text-end">Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody id="backlogFileRegistry">
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            <div class="tab-pane fade p-4" id="viewBacklogPane">
                                <div class="table-responsive">
                                    <table class="table table-hover align-middle">
                                        <thead class="table-light">
                                            <tr>
                                                <th scope="col">Title</th>
                                                <th scope="col">Description</th>
                                                <th scope="col">Acceptance Criteria</th>
                                                <th scope="col" class="text-center">Mandays</th>
                                                <th scope="col" class="text-center">Points</th>
                                            </tr>
                                        </thead>
                                        <tbody id="backlogDetails">

                                        </tbody>
                                    </table>
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

        <div class="modal fade" id="sprintReviewModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg rounded-5">

                    <!-- Header -->
                    <div class="modal-header bg-gradient text-white rounded-top-5 py-3">
                        <div>
                            <h5 id="sprintReviewModalTitle" class="modal-title fw-bold mb-0">Sprint Review: Sprint 1</h5>
                            <small id="sprintReviewModalDate" class="text-light"></small>
                        </div>
                        <button type="button" class="btn-close btn-close-white ms-auto" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <!-- Body -->
                    <div id="sprintReviewContainer" class="modal-body p-4 bg-light rounded-bottom-5">
                        <h6 class="fw-bold mb-3">Backlogs</h6>
                        <div class="table-responsive">
                            <table class="table table-hover table-striped align-middle border rounded-3 shadow-sm">
                                <thead class="table-light">
                                    <tr>
                                        <th>Task Title</th>
                                        <th>Status</th>
                                        <th class="text-end">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>Design UI</td>
                                        <td><span class="badge bg-warning text-dark">Pending</span></td>
                                        <td class="text-end">
                                            <button class="btn btn-sm btn-outline-danger me-1">
                                                <i class="fas fa-times me-1"></i> Reject
                                            </button>
                                            <button class="btn btn-sm  btn-outline-success">
                                                <i class="fas fa-check me-1"></i> Approve
                                            </button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>Implement API</td>
                                        <td><span class="badge bg-warning text-dark">Pending</span></td>
                                        <td class="text-end">
                                            <button class="btn btn-sm btn-outline-danger me-1">
                                                <i class="fas fa-times me-1"></i> Reject
                                            </button>
                                            <button class="btn btn-sm btn-outline-success">
                                                <i class="fas fa-check me-1"></i> Approve
                                            </button>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Footer -->
                    <div class="modal-footer justify-content-between bg-light rounded-bottom-5">
                        <button class="btn btn-outline-secondary btn-sm">
                            <i class="fas fa-download me-1"></i> Download Report
                        </button>
                        <div>
                            <button type="button" class="btn btn-secondary rounded-pill px-4" data-bs-dismiss="modal">
                                Close
                            </button>
                            <button type="button" class="btn btn-success rounded-pill px-4 fw-bold">
                                <i class="fas fa-archive me-1"></i> Archive & Finalize Sprint
                            </button>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <div class="modal fade" id="viewTaskModal" tabindex="-1">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg rounded-4">
                    <div class="modal-body p-4">
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <h2 class="h3 fw-bold mb-0" id="v_modal_title">Task Details</h2>
                            <span class="badge bg-primary-subtle text-primary rounded-pill px-3 py-2 fw-semibold"
                                  id="t_badge_id">Task ID: 5021</span>
                        </div>

                        <div class="mb-4">
                            <label class="info-label text-muted fw-bold small mb-2">TASK NAME</label>
                            <input type="text" id="t_name"
                                   class="form-control form-control-lg bg-light border-0 py-3 task-input view-mode" readonly>
                        </div>

                        <div class="mb-4">
                            <label class="info-label text-muted fw-bold small mb-2">DESCRIPTION</label>
                            <textarea id="t_desc" class="form-control bg-light border-0 task-input view-mode" rows="4" readonly></textarea>
                        </div>

                        <div class="row g-3 mb-4">
                            <div class="col-6">
                                <label class="info-label text-muted fw-bold small mb-2">Assignee Role</label>
                                <select class="form-select bg-light border-0 py-2 task-input view-mode" id="t_user_role"
                                        disabled>
                                    <option value="Developer">Developer</option>
                                    <option value="Designer">Designer</option>
                                    <option value="QA Tester">QA Tester</option>
                                    <option value="Manager">Manager</option>
                                </select>
                            </div>
                            <div class="col-6">
                                <label class="info-label text-muted fw-bold small mb-2">Assignee Name</label>
                                <input type="text" id="t_user_name"
                                       class="form-control bg-light border-0 py-2 task-input view-mode" readonly>
                            </div>
                        </div>

                        <div class="row g-4 mb-4">
                            <div class="col-md-6">
                                <label class="info-label text-muted fw-bold small mb-2">START DATE</label>
                                <input type="date" id="t_start"
                                       class="form-control bg-light border-0 py-2 task-input view-mode" readonly>
                            </div>
                            <div class="col-md-6">
                                <label class="info-label text-muted fw-bold small mb-2">END DATE</label>
                                <input type="date" id="t_end"
                                       class="form-control bg-light border-0 py-2 task-input view-mode" readonly>
                            </div>
                        </div>

                        <div class="row g-4 mb-4">
                            <div class="col-md-6">
                                <label class="info-label text-muted fw-bold small mb-2">CURRENT STATUS</label>
                                <select id="t_status" class="form-select bg-light border-0 py-2 task-input view-mode"
                                        disabled>
                                    <option value="To Do">To Do</option>
                                    <option value="In Progress">In Progress</option>
                                    <option value="Done">Done</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="info-label text-muted fw-bold small mb-2">DEPENDENCY (PREREQUISITE TASK
                                    ID)</label>
                                <input type="number" id="t_dep"
                                       class="form-control bg-light border-0 py-2 task-input view-mode" readonly>
                            </div>
                        </div>

                        <div class="d-flex gap-3 mt-3" id="viewActions">
                            <button class="btn btn-primary px-3 py-1 rounded-pill fw-bold" onclick="switchToEditMode()">
                                <i class="fas fa-edit me-2"></i> Edit Task
                            </button>
                            <button class="btn btn-outline-secondary px-3 py-1 rounded-pill fw-bold"
                                    data-bs-dismiss="modal">Close</button>
                        </div>

                        <div class="d-flex gap-3 mt-3 d-none" id="editActions">
                            <button class="btn btn-success px-3 py-1 rounded-pill fw-bold" onclick="updateTaskData()">
                                <i class="fas fa-save me-2"></i> Save Changes
                            </button>
                            <button class="btn btn-outline-secondary px-3 py-1 rounded-pill fw-bold"
                                    onclick="switchToViewMode()">Cancel</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="taskRejectionModal" data-action="rejection" data-task_id="" tabindex="-1" aria-labelledby="rejectTaskModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content custom-modal border-0 shadow-lg rounded-4 overflow-hidden">

                    <div class="modal-header position-relative border-0 py-3" style="background-color: #5a5eb9; color: white;">
                        <h5 class="modal-title fw-bold w-100 text-center" id="rejectTaskModalLabel">
                            Reject Task
                        </h5>
                        <button type="button" class="btn-close btn-close-white position-absolute end-0 me-3" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <div class="modal-body px-4 pt-4 pb-3">
                        <p id="rejectionTaskPromptMessage" class="text-muted mb-4">
                            This Task is being rejected. Please provide a reason below.
                        </p>
                        <form id="rejectTaskForm">
                            <div>
                                <label for="rejectionTaskReason" class="form-label fw-bold text-dark">Rejection Reason</label>
                                <textarea class="form-control rounded-3" id="rejectionTaskReason" rows="4"
                                          placeholder="e.g., Fails acceptance testing, incomplete implementation, or blocked by a bug..."
                                          required>
                                </textarea>
                            </div>
                        </form>
                    </div>

                    <div class="modal-footer border-0 px-4 pb-4 pt-0">
                        <div class="row w-100 mx-0 g-2">
                            <div class="col-6">
                                <button type="button" class="btn btn-outline-secondary w-100 fw-bold rounded-pill" data-bs-dismiss="modal">
                                    Cancel
                                </button>
                            </div>
                            <div class="col-6">
                                <button type="button" id="confirmTaskRejectBtn" class="btn btn-danger w-100 fw-bold rounded-pill">
                                    Confirm
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="assignmentRemovalModal" data-action="rejection" data-task_id="" tabindex="-1" aria-labelledby="rejectAssignmentModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content custom-modal border-0 shadow-lg rounded-4 overflow-hidden">

                    <div class="modal-header position-relative border-0 py-3" style="background-color: #5a5eb9; color: white;">
                        <h5 class="modal-title fw-bold w-100 text-center" id="rejectAssignmentModalLabel">
                            Decline Assignment
                        </h5>
                        <button type="button" class="btn-close btn-close-white position-absolute end-0 me-3" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <div class="modal-body px-4 pt-4 pb-3">
                        <p id="rejectionAssignmentPromptMessage" class="text-muted mb-4">
                            You are declining this task assignment. Please provide a reason so the project manager can reassign it appropriately.
                        </p>
                        <form id="rejectAssignmentForm">
                            <div>
                                <label for="rejectionAssignmentReason" class="form-label fw-bold text-dark">Reason for Declining</label>
                                <textarea class="form-control rounded-3" id="rejectionAssignmentReason" rows="4"
                                          placeholder="e.g., Currently overloaded with other sprint tasks, conflicting schedule, or requires different domain expertise..."
                                          required></textarea>
                            </div>
                        </form>
                    </div>

                    <div class="modal-footer border-0 px-4 pb-4 pt-0">
                        <div class="row w-100 mx-0 g-2">
                            <div class="col-6">
                                <button type="button" class="btn btn-outline-secondary w-100 fw-bold rounded-pill" data-bs-dismiss="modal">
                                    Cancel
                                </button>
                            </div>
                            <div class="col-6">
                                <button type="button" id="confirmAssignmentRejectBtn" class="btn btn-danger w-100 fw-bold rounded-pill">
                                    Confirm Decline
                                </button>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <script>
            var project_id = ${project_id != null ? project_id : "null"};
            var user_id = ${userInfo.user_id}
            console.log("User id", user_id);
            const user_role = "${user.user_role}";
            console.log(user_role);
        </script>

        <script src="js/sprint.js"></script>
    </body>
</html>
