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
                <a href="projectPage.jsp" class="nav-link"><i class="fas fa-briefcase me-3"></i> Projects</a>
                <a href="sprint.jsp" class="nav-link active"><i class="fas fa-briefcase me-3"></i> Sprint</a>
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
            <nav class="top-nav px-4 d-flex justify-content-between align-items-center">
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
<!--                    <div class="scrum-board-card" id="board-101">
                        <div class="board-header">
                            <div class="board-info">
                                <h2 class="val-name h4 fw-bold text-primary">Sprint 1: Authentication</h2>
                                <span class="info-label">Description / Goal</span>
                                <p class="val-goal">Setup secure login and registration flows with database integration.</p>
                                <span class="info-label">Timeline</span>
                                <div class="date-row"><i class="far fa-calendar-alt text-primary"></i> <span
                                        class="val-start">2025-12-01</span> to <span class="val-end">2025-12-14</span></div>
                            </div>
                            <div class="text-end">
                                <span class="info-label">Status</span>
                                <div class="badge bg-primary-subtle text-primary val-status rounded-pill px-3 py-2 mb-2">
                                    Active</div><br>
                                    <c:if test="${user.user_role == 'Scrum Master'}">
                                    <button class="btn btn-outline-secondary btn-sm" onclick="editBoard('board-101')"><i
                                            class="fas fa-edit me-1"></i> Edit Details</button>

                                    <button class="btn btn-outline-danger btn-sm" onclick="deleteBoard('board-101')">
                                        <i class="fas fa-trash-alt me-1"></i> Delete
                                    </button>
                                </c:if>
                            </div>
                        </div>

                        <div class="notes-grid">
                            <div class="note-box"><span class="info-label">Review Notes</span>
                                <div class="note-content val-review">Client approved UI. Ready for testing.</div>
                            </div>
                            <div class="note-box"><span class="info-label">Retrospective</span>
                                <div class="note-content val-retro">Excellent communication. Documentation needed earlier.
                                </div>
                            </div>
                        </div>
                         ================= SPRINT BACKLOG ================= 
                        <div class="note-box sprint-backlog-box">

                            <span class="info-label">Sprint Backlog</span>

                            <div class="backlog-list mt-2">

                                <span class="backlog-chip">
                                    User Login Feature
                                    <span class="badge bg-secondary ms-2">5 SP</span>
                                </span>

                                <span class="backlog-chip">
                                    Registration Module
                                    <span class="badge bg-secondary ms-2">8 SP</span>
                                </span>

                                <span class="backlog-chip">
                                    Role Management
                                    <span class="badge bg-secondary ms-2">3 SP</span>
                                </span>

                            </div>

                        </div>

                        <div class="board-layout mt-4">
                            <div class="column" ondragover="allowDrop(event)" ondrop="drop(event)">
                                <h3>To Do</h3>
                                <c:if test="${user.user_role == 'Scrum Master'}">
                                    <div class="task-list"></div><button class="btn text-muted btn-sm fw-bold w-100 text-start"
                                                                         onclick="addTask(this)">+ Add Task</button>
                                    </c:if>
                            </div>
                            <div class="column" ondragover="allowDrop(event)" ondrop="drop(event)">
                                <h3>In Progress</h3>
                                <c:if test="${user.user_role == 'Scrum Master'}">
                                    <div class="task-list"></div><button class="btn text-muted btn-sm fw-bold w-100 text-start"
                                                                         onclick="addTask(this)">+ Add Task</button>
                                    </c:if>
                            </div>
                            <div class="column" ondragover="allowDrop(event)" ondrop="drop(event)">
                                <h3>Done</h3>
                                <c:if test="${user.user_role == 'Scrum Master'}">
                                    <div class="task-list"></div><button class="btn text-muted btn-sm fw-bold w-100 text-start"
                                                                         onclick="addTask(this)">+ Add Task</button>
                                    </c:if>
                            </div>
                        </div>
                    </div>-->
                </div>
            </div>
        </div>

        <div class="modal" id="sprintModal" tabindex="-1">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg rounded-4">

                    <div class="modal-body p-5">

                        <div class="text-center mb-4">
                            <h3 class="fw-bold" id="modalTitle">Create Sprint</h3>
                        </div>

                        <input type="hidden" id="editBoardId">

                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="fw-bold small mb-2">SPRINT NAME</label>
                                <input type="text" class="form-control rounded-3"
                                       id="s_name" placeholder="Enter sprint name...">
                            </div>

                            <div class="col-md-6">
                                <label class="fw-bold small mb-2">GOAL</label>
                                <textarea class="form-control rounded-3"
                                          id="s_goal" rows="1"
                                          placeholder="Sprint objective..."></textarea>
                            </div>
                        </div>


                        <div class="row g-3 mt-2">
                            <div class="col-md-6">
                                <label class="fw-bold small mb-2">START DATE</label>
                                <input type="date" class="form-control rounded-3" id="s_start">
                            </div>

                            <div class="col-md-6">
                                <label class="fw-bold small mb-2">END DATE</label>
                                <input type="date" class="form-control rounded-3" id="s_end">
                            </div>
                        </div>


                        <div class="row g-3 mt-2">
                            <div class="col-md-6">
                                <label class="fw-bold small mb-2">REVIEW NOTES</label>
                                <textarea class="form-control rounded-3"
                                          id="s_review" rows="2"
                                          placeholder="Review summary..."></textarea>
                            </div>

                            <div class="col-md-6">
                                <label class="fw-bold small mb-2">RETROSPECTIVE NOTES</label>
                                <textarea class="form-control rounded-3"
                                          id="s_retro" rows="2"
                                          placeholder="Retrospective notes..."></textarea>
                            </div>
                        </div>


                        <div class="mt-3">
                            <label class="fw-bold small mb-2">LINK BACKLOG ITEMS</label>
                            <select class="form-select rounded-3"
                                    id="s_backlog_links"
                                    size="1"
                                    multiple
                                    >

                            </select>
                        </div>


                        <div class="mt-4 d-grid">
                            <button class="btn btn-primary rounded-pill py-2 fw-bold" id="SprintSubBtn"
                                    onclick="saveBoard()">
                                Create Sprint
                            </button>

                            <button class="btn btn-link text-secondary mt-2"
                                    data-bs-dismiss="modal">
                                Cancel
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="modal fade" id="taskModal" tabindex="-1">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg rounded-4">

                    <div class="modal-body p-4">

                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <h4 id="taskModel_title"  class="fw-bold text-center flex-grow-1 m-0">Add Task</h4>


                            <button id="deleteTaskBtn"
                                    class="btn btn-outline-danger btn-sm"
                                    style="display:none;">
                                <i class="fas fa-trash"></i>
                            </button>
                        </div>
                        <input type="hidden" id="t_board_id">
                        <input type="hidden" id="t_column_id" >

                        <!-- TASK NAME -->
                        <div class="mb-3">
                            <label class="fw-bold small mb-2">TASK NAME</label>
                            <input type="text"
                                   class="form-control rounded-3"
                                   id="t_name">
                        </div>

                        <!-- DESCRIPTION -->
                        <div class="mb-3">
                            <label class="fw-bold small mb-2">DESCRIPTION</label>
                            <textarea class="form-control rounded-3"
                                      id="t_desc"
                                      rows="2"></textarea>
                        </div>

                        <!-- ROLE + USER -->
                        <div class="row g-3 mb-3">
                            <div class="col-md-6">
                                <label class="fw-bold small mb-2">ROLE</label>
                                <select class="form-select rounded-3"
                                        id="t_user_role">
                                    <option id="Developer">Developer</option>
                                    <option id="Designer">Designer</option>
                                    <option id="QA Tester">QA Tester</option>
                                </select>
                            </div>

                            <div class="col-md-6">
                                <label class="fw-bold small mb-2">ASSIGNEE</label>
                                <select class="form-select rounded-3"
                                        id="assignee">
                                </select>
                            </div>
                        </div>

                        <!-- DATES + DEPENDENCY -->
                        <div class="row g-3 mb-4">
                            <div class="col-md-4">
                                <label class="fw-bold small mb-2">START</label>
                                <input type="date"
                                       class="form-control rounded-3"
                                       id="t_start">
                            </div>

                            <div class="col-md-4">
                                <label class="fw-bold small mb-2">END</label>
                                <input type="date"
                                       class="form-control rounded-3"
                                       id="t_end">
                            </div>

                            <div class="col-md-4">
                                <label class="fw-bold small mb-2">
                                    DEPENDENCY
                                </label>
                                <select class="form-select rounded-3"
                                        id="t_dependency"
                                        size="1"
                                        multiple>

                                </select>
                            </div>
                        </div>

                        <!-- BUTTONS -->
                        <div class="d-grid gap-2">
                            <button class="btn btn-primary rounded-pill py-2 fw-bold" id="taskModel_Sbt"
                                    onclick="confirmAddTask()">
                                Add Task
                            </button>

                            <button class="btn btn-link text-secondary"
                                    data-bs-dismiss="modal">
                                Cancel
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
                            <textarea id="t_desc" class="form-control bg-light border-0 task-input view-mode" rows="4"
                                      readonly></textarea>
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
        <script>
        var project_id = ${projectId != null ? projectId : "null"};
        var user_id = ${userInfo.user_id}
        console.log("User id", user_id);

        </script>

        <script src="js/sprint.js"></script>
    </body>
</html>
