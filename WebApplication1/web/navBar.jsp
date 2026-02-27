<%-- 
    Document   : navBar
    Created on : 25 Feb 2026, 8:29:02 am
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
        <link href="css\common.css" rel="stylesheet">
    </head>
    <body>
        <nav id="sidebar">
            <div class="sidebar-brand text-center">SPMMS </div>
            <div class="nav flex-column mt-3">
                <a href="dashboard.jsp" class="nav-link"><i class="fas fa-grid-2 me-3 fa-chart-pie"></i> Dashboard</a>
                <div class="nav-divider my-2 mx-3" style="border-bottom: 1px solid rgba(255, 255, 255, 0.1);"></div>
                <a href="projectPage.jsp" class="nav-link active"><i class="fas fa-briefcase me-3"></i> Projects</a>

                <a href="sprint.jsp" class="nav-link "><i class="fas fa-briefcase me-3"></i> Sprint</a>


                <a href="BacklogServlet?action=redirect&project_id=${project.projectId}" class="nav-link">
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
    </body>
</html>
