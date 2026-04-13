<%-- 
    Document   : ganttChart
    Created on : 26 Dec 2025, 12:47:05 am
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <title>Timeline | SPMMS Console</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

        <link rel="stylesheet" href="https://cdn.dhtmlx.com/gantt/edge/dhtmlxgantt.css">

        <link rel="stylesheet" href="css\projectPage.css">
        <script src="https://cdn.dhtmlx.com/gantt/edge/dhtmlxgantt.js"></script>
        <script src="https://export.dhtmlx.com/gantt/api.js"></script>
        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

        <link href="css\ganttChart.css" rel="stylesheet">
    </head>

    <body>
        <div id="wrapper">
            <nav id="sidebar" title="Double-click to toggle">
                <div class="sidebar-brand">
                    <span class="brand-text">SPMMS CONSOLE</span>
                </div>

                <div class="nav flex-column mt-3">
                    <a href="dashboard.jsp" class="nav-link">
                        <i class="fas fa-chart-pie me-3"></i><span>Dashboard</span>
                    </a>

                    <div class="nav-divider my-2 mx-3" style="border-bottom: 1px solid rgba(255, 255, 255, 0.1);"></div>

                    <a href="projectPage.jsp" class="nav-link">
                        <i class="fas fa-briefcase me-3"></i><span>Projects</span>
                    </a>
                    <a href="ganttChart.jsp" class="nav-link active">
                        <i class="fas fa-stream me-3"></i><span>Timeline</span>
                    </a>
                    <a href="teamMembersPage.jsp" class="nav-link">
                        <i class="fas fa-users-gear me-3"></i><span>Team</span>
                    </a>
                    <a href="projectAnalytics.jsp" class="nav-link">
                        <i class="fas fa-file-contract me-3"></i><span>Reports</span>
                    </a>

                    <div class="mt-auto">
                        <div class="nav-divider my-2 mx-3" style="border-bottom: 1px solid rgba(255, 255, 255, 0.1);"></div>
                        <a href="login_signUpServlet?processType=logOut" class="nav-link text-danger">
                            <i class="fas fa-sign-out-alt me-3"></i><span>Logout</span>
                        </a>
                    </div>
                </div>
            </nav>

            <div id="content-wrapper">
                <header class="top-nav px-4 pt-3">
                    <nav aria-label="breadcrumb" class="mb-1">
                        <ol class="breadcrumb small mb-0">
                            <li class="breadcrumb-item text-muted">Management</li>
                            <li class="breadcrumb-item active fw-bold text-dark" aria-current="page">Timeline</li>
                        </ol>
                    </nav>

                    <div class="d-flex justify-content-between align-items-center pb-3">
                        <div class="d-flex align-items-center">
                            <h1 class="header-title me-3 mb-0">Project Timeline</h1>
                            <span class="status-badge-pill">ACTIVE</span>
                        </div>

                        <div class="header-right-group d-flex align-items-center">
                            <div class="action-buttons me-4 pe-4 border-end">
                                <button class="btn btn-export-consistent" onclick="exportCenteredGantt()">
                                    <i class="fas fa-file-pdf me-2"></i>Export PDF
                                </button>
                            </div>

                            <div class="user-profile-section d-flex align-items-center">
                                <div class="user-info-text d-none d-sm-block text-end me-3">
                                    <div class="user-name-label">${user.username}</div>
                                    <div class="user-role-label">${user.user_role}</div>
                                </div>
                                <div class="avatar-wrapper">
                                    <img src="https://ui-avatars.com/api/?name=${user.username}&background=2563eb&color=fff"
                                         class="rounded-circle border" width="38" alt="Profile">
                                </div>
                            </div>
                        </div>
                    </div>
                </header>

                <main id="gantt_container">
                    <div id="gantt_here" style='width:100%; height:100%;'></div>
                </main>
            </div>
        </div>

        <script>
            /**
             * SPMMS CONSOLE - TIMELINE LOGIC
             * Includes: Sidebar Toggle, Gantt Interactivity, and Centered PDF Export
             */

            // 1. SIDEBAR INTERACTION
            const sidebar = document.getElementById('sidebar');

            // Double-click to toggle collapse state
            sidebar.addEventListener('dblclick', () => {
                sidebar.classList.toggle('collapsed');
            });

            // Re-render Gantt when sidebar transition ends to prevent layout breaking
            sidebar.addEventListener('transitionend', () => {
                if (window.gantt) {
                    gantt.render();
                }
            });


            // 2. GANTT CHART CONFIGURATION (EDITABLE MODE)
            // Enable double-click on bars to open the detailed edit popup
            gantt.config.details_on_dblclick = true;

            // Enable drag-and-drop features for the timeline
            gantt.config.drag_move = true;   // Move tasks horizontally
            gantt.config.drag_resize = true; // Change task duration
            gantt.config.drag_links = true;  // Connect tasks with dependency arrows
            gantt.config.drag_project = true; // Allow moving the entire project branch

            // Set the date format for parsing
            gantt.config.date_format = "%Y-%m-%d";

            // 3. INLINE EDITORS (Click text to edit directly in the grid)
            const textEditor = {type: "text", map_to: "text"};
            const dateEditor = {type: "date", map_to: "start_date"};
            const durationEditor = {type: "number", map_to: "duration", min: 0, max: 100};

            gantt.config.columns = [
                {name: "text", label: "Task Name", tree: true, width: 200, editor: textEditor},
                {name: "start_date", label: "Start", align: "center", width: 90, editor: dateEditor},
                {name: "duration", label: "Days", align: "center", width: 60, editor: durationEditor},
                {name: "add", label: "", width: 44} // The '+' button to add subtasks
            ];


            // 4. INITIALIZATION & DATA LOADING
            gantt.init("gantt_here");

            // Parse your sample data
            gantt.parse({
                data: [
                    {id: 1, text: "Standardizing Design", start_date: "2025-01-01", duration: 10, open: true},
                    {id: 2, text: "Sidebar Symmetry", start_date: "2025-01-11", duration: 5, parent: 1},
                    {id: 3, text: "Backend Integration", start_date: "2025-01-16", duration: 8, parent: 1}
                ],
                links: [
                    {id: 1, source: 1, target: 2, type: "0"} // Finish-to-Start dependency
                ]
            });


            // 5. DATA PROCESSOR (Captures Create, Update, Delete actions for Backend)
            gantt.createDataProcessor(function (entity, action, data, id) {
                console.log(`Action: ${action} | Task ID: ${id}`, data);

                /** * To connect to your MySQL backend:
                 * Use fetch() or AJAX here to send 'data' to your PHP/Node.js file.
                 */
                return Promise.resolve();
            });


            // 6. CENTERED PDF EXPORT FUNCTION
            function exportCenteredGantt() {
                gantt.exportToPDF({
                    name: "Project_Timeline.pdf",
                    format: "A4",
                    orientation: "landscape", // Best for horizontal centering
                    raw: true, // Crucial to process the custom CSS below
                    header: `
                <style>
                    /* Apply centering to the PDF engine only */
                    .gantt_container {
                        margin: 0 auto !important;
                        width: 95% !important;
                    }
                    h1 { 
                        text-align: center; 
                        font-family: 'Inter', sans-serif; 
                        color: #0f172a; 
                        margin-top: 10px;
                        margin-bottom: 20px; 
                    }
                </style>
            `
                });
            }

            // 7. RESPONSIVENESS
            window.addEventListener("resize", () => {
                if (window.gantt)
                    gantt.render();
            });
        </script>
    </body>

</html>

<c:forEach var="user" items="${projectTeamAssignmentData}">
    <c:if test="${user.user_role == 'Project Manager'}">

        <div class="col-xl-3 col-md-6">
            <div class="team-card p-3 d-flex align-items-center position-relative">
                <img src="https://ui-avatars.com/api/?name=${user.username}&background=eff6ff&color=2563eb"
                     class="avatar-md me-3" />

                <div class="flex-grow-1">
                    <div class="fw-bold text-dark small">${user.username}</div>
                    <div class="text-muted" style="font-size: 11px;">${user.email}</div>
                </div>

                <c:if test="${user.user_role == 'Project Manager'}">
                    <button type="button" class="btn-delete-member" data-bs-toggle="modal" 
                            data-bs-target="#deleteMemberModal" 
                            data-project-id="${project.projectId}"
                            data-user-id="${user.user_id}"
                            name="Delete" value="Delete"
                            > Delete
                    </button>
                </c:if>

            </div>
        </div>

    </c:if>
</c:forEach>

<c:forEach var="user1" items="${projectTeamAssignmentData}">
    <c:if test="${user1.user_role == 'Product Owner'}">

        <div class="col-xl-3 col-md-6">
            <div class="team-card p-3 d-flex align-items-center position-relative">
                <img src="https://ui-avatars.com/api/?name=${user1.username}&background=eff6ff&color=2563eb"
                     class="avatar-md me-3" />

                <div class="flex-grow-1">
                    <div class="fw-bold text-dark small">${user1.username} </div>
                    <div class="text-muted" style="font-size: 11px;">${user1.email}</div>
                </div>

                <c:if test="${user.user_role == 'Project Manager'}">
                    <button type="button" class="btn-delete-member" data-bs-toggle="modal" 
                            data-bs-target="#deleteMemberModal" 
                            data-project-id="${project.projectId}"
                            data-user-id="${user1.user_id}"
                            name="Delete" value="Delete"
                            > Delete
                    </button>
                </c:if>
            </div>
        </div>

    </c:if>
</c:forEach>

<c:forEach var="user1" items="${projectTeamAssignmentData}">
    <c:if test="${user1.user_role == 'Scrum Master'}">

        <div class="col-xl-3 col-md-6">
            <div class="team-card p-3 d-flex align-items-center position-relative">
                <img src="https://ui-avatars.com/api/?name=${user1.username}&background=eff6ff&color=2563eb"
                     class="avatar-md me-3" />

                <div class="flex-grow-1">
                    <div class="fw-bold text-dark small">${user1.username}</div>
                    <div class="text-muted" style="font-size: 11px;">${user1.email}</div>
                </div>

                <c:if test="${user.user_role == 'Project Manager'}">
                    <button type="button" class="btn-delete-member" data-bs-toggle="modal" 
                            data-bs-target="#deleteMemberModal" 
                            data-project-id="${project.projectId}"
                            data-user-id="${user1.user_id}"
                            name="Delete" value="Delete"
                            > Delete
                    </button>
                </c:if>
            </div>
        </div>

    </c:if>
</c:forEach>


<c:forEach var="user1" items="${projectTeamAssignmentData}">
    <c:if test="${user1.user_role == 'Developer'}">

        <div class="col-xl-3 col-md-6">
            <div class="team-card p-3 d-flex align-items-center position-relative">
                <img src="https://ui-avatars.com/api/?name=${user1.username}&background=eff6ff&color=2563eb"
                     class="avatar-md me-3" />

                <div class="flex-grow-1">
                    <div class="fw-bold text-dark small">${user1.username}</div>
                    <div class="text-muted" style="font-size: 11px;">${user.email}</div>
                </div>

                <c:if test="${user.user_role == 'Project Manager'}">
                    <button type="button" class="btn-delete-member" data-bs-toggle="modal" 
                            data-bs-target="#deleteMemberModal" 
                            data-project-id="${project.projectId}"
                            data-user-id="${user1.user_id}"
                            name="Delete" value="Delete"
                            > Delete
                    </button>
                </c:if>
            </div>
        </div>

    </c:if>
</c:forEach>
