/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

console.log("Executed");
let result;
let successProcessTab;
let failedProcessTab;

document.addEventListener("DOMContentLoaded", async () => {

    populateProjectFolder();
    if (userRole === "Project Manager" || userRole === "Product Owner" || userRole === "Scrum Master") {
        populateMyActive_PM_PO();
    } else if (userRole === "Developer") {
        populateMyActive_Dev();
    }

    successProcessTab = document.getElementById("successProcessTab");
    failedProcessTab = document.getElementById("failedProcessTab");
    // Grab the elements
    const toggleBtn = document.getElementById('toggleActivitiesBtn');
    const feedContainer = document.getElementById('activityFeedContainer');
    // Add the click listener
    toggleBtn.addEventListener('click', function () {

        // 1. Toggle the visual colors on the bell icon
        this.classList.toggle('text-info');
        this.classList.toggle('text-muted');
        // 2. Vanilla JS Slide Toggle logic
        // If it's currently shrunk to 0, expand it back to 280px
        if (feedContainer.style.maxHeight === "0px") {
            feedContainer.style.maxHeight = "280px";
        } else {
            // Otherwise, shrink it to 0px to hide it
            feedContainer.style.maxHeight = "0px";
        }
    });

    document.getElementById("projectSearch").addEventListener("input", function () {

        const keyword = this.value.toLowerCase();

        document.querySelectorAll(".project-folder-card").forEach(card => {

            const text = card.textContent.toLowerCase();

            card.style.display = text.includes(keyword) ? "" : "none";
        });
    });
});

async function populateMyActive_PM_PO() {
    const response = await fetch(`DashboardServlet?processType=dashboardInsights&user_id=${user_id}&userRole=${userRole}`);

    if (!response.ok) {
        console.error("profileInfo missing", result);
        return;
    }

    result = await response.json();
    if(userRole !== "Product Owner"){
        populateRecentActivity_PMSM(result.DashboardInsightData.activitys);
    }
    console.log(result);


    if (userRole === "Project Manager") {
        populateKPICards_PM(result);
    }

    const myActiveTitle = document.getElementById("myActiveTitle");
    myActiveTitle.innerText = "My Active Project";
    const myActiveBody = document.getElementById("myActiveBody");
    myActiveBody.innerHTML = "";
    result.DashboardInsightData.activeProjects.forEach(item => {
        myActiveBody.innerHTML += `
                                <div class="d-flex align-items-center mb-3 pb-3 border-bottom">
                                    <div class="me-3 text-primary">
                                        <i class="fas fa-code-branch fa-lg"></i>
                                    </div>

                                    <div class="flex-grow-1">
                                        <p class="mb-1 fw-bold" style="font-size: 0.9rem;">
                                            ${item.project_name}
                                        </p>

                                        <span class="badge bg-success me-1">
                                            ${item.completedBacklog} Completed
                                        </span>

                                        <span class="badge bg-primary me-1">
                                            ${item.activeBacklog} Active
                                        </span>

                                        <span class="badge bg-warning text-dark">
                                            ${item.pendingBacklog} Pending
                                        </span>
                                    </div>
                                </div>`;
    });
}

function populateKPICards_PM(result) {

    let cardName_1 = "";
    let cardValue_1 = 0;
    let cardName_2 = "";
    let cardValue_2 = 0;
    let cardName_3 = "";
    let cardValue_3 = 0;
    switch (userRole) {
        case "Project Manager":
            cardName_1 = "Total Projects";
            cardName_2 = "Pending Recruits";
            cardName_3 = "Overdue";
            cardValue_1 = result.DashboardInsightData.totalProject;
            cardValue_2 = result.DashboardInsightData.noMissingRole;
            cardValue_3 = result.DashboardInsightData.sumTaskOverdue;
            break;
    }


//Add KPI card based on the user role
    const KPIDiv = document.getElementById("KPIDiv");
    KPIDiv.classList.remove("d-none");
    if (userRole === "Project Manager") {



        KPIDiv.innerHTML = `<div class="col-xl-4 col-md-6 mb-3 mb-xl-0">
                        <div class="card border-0 shadow-sm" style="border-radius: 10px;">
                            <div class="card-body px-3 py-2 d-flex align-items-center">
                                <div class="bg-primary bg-opacity-10 text-primary rounded d-flex align-items-center justify-content-center me-3" style="width: 38px; height: 38px;">
                                    <i class="fas fa-folder-open"></i>
                                </div>
                                <div>
                                    <p class="text-muted fw-bold mb-0 text-uppercase" style="font-size: 0.7rem; letter-spacing: 0.5px;">${cardName_1}</p>
                                    <h5 class="fw-bold mb-0 text-dark">${cardValue_1}</h5> 
                                </div>
                            </div>
                        </div>
                    </div>

                    <div id="KPI2" class="col-xl-4 col-md-6 mb-3 mb-xl-0" style="cursor: pointer;">
                        <div class="card border-0 shadow-sm" style="border-radius: 10px;">
                            <div class="card-body px-3 py-2 d-flex align-items-center">
                                <div class="bg-success bg-opacity-10 text-success rounded d-flex align-items-center justify-content-center me-3" style="width: 38px; height: 38px;">
                                    <i class="fas fa-users"></i>
                                </div>
                                <div>
                                    <p class="text-muted fw-bold mb-0 text-uppercase" style="font-size: 0.7rem; letter-spacing: 0.5px;">${cardName_2}</p>
                                    <h5 class="fw-bold mb-0 text-dark">${cardValue_2}</h5>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div id="KPI3" class="col-xl-4 col-md-6 mb-3 mb-xl-0" >
                        <div class="card border-0 shadow-sm" style="border-radius: 10px;">
                            <div class="card-body px-3 py-2 d-flex align-items-center">
                                <div class="bg-danger bg-opacity-10 text-danger rounded d-flex align-items-center justify-content-center me-3" style="width: 38px; height: 38px;">
                                    <i class="fas fa-exclamation-triangle"></i>
                                </div>
                                <div>
                                    <p class="text-muted fw-bold mb-0 text-uppercase" style="font-size: 0.7rem; letter-spacing: 0.5px;">${cardName_3}</p>
                                    <h5 class="fw-bold mb-0 text-dark">${cardValue_3}</h5>
                                </div>
                            </div>
                        </div>
                    </div>`;
    }

    document.getElementById("KPI2").addEventListener("click", () => populatePendingRecruitmentModal(result));
    document.getElementById("KPI3").addEventListener("click", () => populateOverdueTaskModal(result));
}

function populatePendingRecruitmentModal(result) {

    const PRDiv = document.getElementById("pendingRecruitmentContainer");
    let html = "";
    Object.entries(result.DashboardInsightData.assignmentPending_project)
            .forEach(([projectName, positions]) => {

                let positionsHtml = "";
                positions.forEach(position => {
                    positionsHtml += `
                    <div class="position-item">
                        ${position}
                    </div>
                `;
                });
                html += `
                <div class="row g-0 border-bottom py-3 px-3 align-items-start recruitment-row">

                    <div class="col-md-4">
                        <span class="fw-semibold text-primary">
                            ${projectName}
                        </span>
                    </div>

                    <div class="col-md-8">
                        ${positionsHtml}
                    </div>

                </div>
            `;
            });
    PRDiv.innerHTML = html;
    const modal = bootstrap.Modal.getOrCreateInstance(
            document.getElementById("pendingRecruitmentModal")
            );
    modal.show();
}


// ===============================
// Modal Open
// ===============================
if (userRole === "Project Manager") {
    document.getElementById("newProjectBtn").addEventListener("click", () => {

        resetProjectForm();
        const modal = bootstrap.Modal.getOrCreateInstance(
                document.getElementById("createProjectModal")
                );
        modal.show();
    });
}
// ===============================
// Elements
// ===============================

const projName = document.getElementById("ProjName");
const projDesc = document.getElementById("ProjDesc");
const projType = document.getElementById("ProjType");
const projClient = document.getElementById("ProjClient");
const projStart = document.getElementById("ProjStart");
const projEnd = document.getElementById("ProjEnd");
const errorProjName = document.getElementById("errorProjName");
const errorProjDesc = document.getElementById("errorProjDesc");
const errorProjType = document.getElementById("errorProjType");
const errorProjClient = document.getElementById("errorProjClient");
const errorProjStart = document.getElementById("errorProjStart");
const errorProjEnd = document.getElementById("errorProjEnd");
const errorDateRange = document.getElementById("errorDateRange");
const formSubbtn = document.getElementById("formSubbtn");

// ===============================
// Validation Helpers
// ===============================

function showError(input, errorElement, message) {

    input.classList.add("is-invalid");
    errorElement.textContent = message;
    errorElement.classList.add("show");
}

function clearError(input, errorElement) {

    input.classList.remove("is-invalid");
    errorElement.textContent = "";
    errorElement.classList.remove("show");
}

// ===============================
// Clear Validation Only
// ===============================

function clearValidation() {

    [
        [projName, errorProjName],
        [projDesc, errorProjDesc],
        [projStart, errorProjStart],
        [projEnd, errorProjEnd],
        [projType, errorProjType],
        [projClient, errorProjClient]
    ].forEach(([input, errorElement]) => {

        clearError(input, errorElement);
    });
    errorDateRange.textContent = "";
    errorDateRange.classList.remove("show");
    projStart.classList.remove("is-invalid");
    projEnd.classList.remove("is-invalid");
}

// ===============================
// Reset Form
// ===============================

function resetProjectForm() {

    clearValidation();
    document.getElementById("ProjectForm").reset();
    formSubbtn.disabled = false;
}

// ===============================
// Auto Clear Validation
// ===============================

[
    [projName, errorProjName],
    [projDesc, errorProjDesc],
    [projClient, errorProjClient]
].forEach(([input, errorElement]) => {

    input.addEventListener("input", () => {
        clearError(input, errorElement);
    });
});
[
    [projType, errorProjType],
    [projStart, errorProjStart],
    [projEnd, errorProjEnd]
].forEach(([input, errorElement]) => {

    input.addEventListener("change", () => {
        clearError(input, errorElement);
    });
});
// ===============================
// Shared Date Validation Cleanup
// ===============================

projStart.addEventListener("change", validateDateRange);
projEnd.addEventListener("change", validateDateRange);
function validateDateRange() {

    if (!projStart.value || !projEnd.value) {
        return;
    }

    const start = new Date(projStart.value);
    const end = new Date(projEnd.value);
    if (start <= end) {

        projStart.classList.remove("is-invalid");
        projEnd.classList.remove("is-invalid");
        errorDateRange.textContent = "";
        errorDateRange.classList.remove("show");
    }
}

// ===============================
// Form Validation
// ===============================

formSubbtn.addEventListener("click", function (e) {

    e.preventDefault();
    let isValid = true;
    clearValidation();
    // Project Name
    if (projName.value.trim() === "") {

        showError(
                projName,
                errorProjName,
                "Project name is required."
                );
        isValid = false;
    } else if (projName.value.trim().length < 3) {

        showError(
                projName,
                errorProjName,
                "Project name must contain at least 3 characters."
                );
        isValid = false;
    }

// Description
    if (projDesc.value.trim() === "") {

        showError(
                projDesc,
                errorProjDesc,
                "Description is required."
                );
        isValid = false;
    }

// Project Type
    if (projType.value === "") {

        showError(
                projType,
                errorProjType,
                "Please select a project type."
                );
        isValid = false;
    }

// Project Client
    if (projClient.value.trim() === "") {

        showError(
                projClient,
                errorProjClient,
                "Project client is required."
                );
        isValid = false;
    }

// Start Date
    if (!projStart.value) {

        showError(
                projStart,
                errorProjStart,
                "Start date is required."
                );
        isValid = false;
    }

// End Date
    if (!projEnd.value) {

        showError(
                projEnd,
                errorProjEnd,
                "End date is required."
                );
        isValid = false;
    }

// Date Comparison
    if (projStart.value && projEnd.value) {

        const start = new Date(projStart.value);
        const end = new Date(projEnd.value);
        if (start > end) {

            projStart.classList.add("is-invalid");
            projEnd.classList.add("is-invalid");
            errorDateRange.textContent =
                    "End date must be on or after the start date.";
            errorDateRange.classList.add("show");
            isValid = false;
        }
    }

// Success
    if (isValid) {
        const projectData = getProjectFormData();
        console.log(projectData);
        sendData_Project(projectData);
    }

});
function populateOverdueTaskModal(result) {

    const overdueContainer =
            document.getElementById("overdueTaskContainer");
    overdueContainer.innerHTML = "";
    const overdueProjects =
            result.DashboardInsightData.tasksOverdue || [];
    let html = "";
    overdueProjects.forEach(project => {

        html += `
            <div class="row g-0 border-bottom py-3 px-3 align-items-center">

                <div class="col-md-6">
                    <span class="fw-semibold text-danger">
                        ${project.project_name}
                    </span>
                </div>

                <div class="col-md-6 text-center">
                    <span class="badge bg-danger">
                        ${project.taskOverdue}
                    </span>
                </div>

            </div>
        `;
    });
    overdueContainer.innerHTML = html;
    document.getElementById("overdueProjectCount").textContent =
            `${overdueProjects.length} project(s) require attention`;
    const modal = bootstrap.Modal.getOrCreateInstance(
            document.getElementById("overdueTaskModal")
            );
    modal.show();
}


function getProjectFormData() {

    const data = {
        projectName: document.getElementById("ProjName").value.trim(),
        projectDesc: document.getElementById("ProjDesc").value.trim(),
        projectType: document.getElementById("ProjType").value,
        projectStatus: "Planned",
        projCreatedBy: user_id,
        projectClient: document.getElementById("ProjClient").value.trim(),
        projStartDate: document.getElementById("ProjStart").value,
        projEndDate: document.getElementById("ProjEnd").value
    };
    return data;
}

async function sendData_Project(data) {

    const response = await fetch("DashboardServlet", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });
    const result = await response.json();
    // close the project creation model 
    const modalElement = document.getElementById("createProjectModal");
    const modal = bootstrap.Modal.getInstance(modalElement);
    if (modal) {
        modal.hide();
    }

//project creation failed
    if (result.status === "Success") {
//project creation success
        populateProjectFolder();
        populateKPICard();
        successProcessTab.classList.remove("d-none");
        successProcessTab.innerText = result.processMsg;
    } else {
        failedProcessTab.classList.remove("d-none");
        failedProcessTab.innerText = result.processMsg;
    }


}

async function populateProjectFolder() {

    const response = await fetch(`DashboardServlet?processType=projectInfo&user_id=${user_id}`);
    const result = await response.json();
    console.log(result);
    if (!result.profileInfo) {
        console.error("profileInfo missing", result);
        return;
    }

    const projectContainerDiv = document.getElementById("projectContainer");

    projectContainerDiv.innerHTML = "";
    //add the project folder
    result.profileInfo.forEach((item, index) => {
        const riskScoreIcon =
                item.project_risk_score <= 30 ? '<i class="fas fa-gauge-high text-success"></i>' :
                item.project_risk_score <= 60 ? '<i class="fas fa-gauge-high text-warning"></i>' : '<i class="fas fa-gauge-high text-danger"></i>';
        projectContainerDiv.innerHTML += `
                                <div class="col-xl-4 col-md-6 col-sm-12 mb-4">
                                    <div class="project-folder-card">
                                        <div class="folder-tab"></div>
                                        <div class="folder-content p-4 position-relative">

                                            <div class="position-absolute" style="top: 1.5rem; right: 1.5rem; text-align: center;">
                                                <span class="label-style text-muted" style="font-size: 0.75rem; font-weight: bold; letter-spacing: 0.5px;">RISK</span>
                                                <div class="d-flex flex-column align-items-center mt-1 text-primary" style="font-size: 1.1rem;">
                                                <!-- Low Risk -->
                                                ${riskScoreIcon}
                                                </div>
                                            </div>

                                            <div class="folder-icon mb-2">
                                                <i class="fas fa-folder text-primary" style="font-size: 2rem;"></i>
                                            </div>
                                            <span class="label-style text-muted" style="font-size: 0.75rem; font-weight: bold; letter-spacing: 0.5px;">PROJECT</span>
                                            <h6 class="project-id text-muted mt-1">${item.projectId}</h6>
                                            <h6 class="project-title fw-bold mt-2">${item.projectName}</h6>

                                            <a href="ProjectPageServlet?action=redirect&project_id=${item.projectId}"
                                               class="btn btn-sm btn-outline-primary w-100 rounded-pill fw-bold mt-3">
                                                Open Project
                                            </a>
                                        </div>
                                    </div>
                                </div>`;
    });
}

async function populateKPICard_PO() {
    const response = await fetch(`DashboardServlet?processType=dashboardInsights&user_id=${user_id}&userRole=${userRole}`);
    result = await response.json();

}
;


async function populateMyActive_Dev() {

    const response = await fetch(`DashboardServlet?processType=dashboardInsights&user_id=${user_id}&userRole=${userRole}`);

    if (!response.ok) {
        console.error("profileInfo missing", result);
        return;
    }

    result = await response.json();

    populateRecentActivity_Dev(result.DashboardInsightData.activitys);


    console.log(result);
    const myActiveTitle = document.getElementById("myActiveTitle");
    myActiveTitle.innerText = "My Active Project";
    const myActiveBody = document.getElementById("myActiveBody");
    myActiveBody.innerHTML = "";
    result.DashboardInsightData.activeTasks.forEach(item => {
        myActiveBody.innerHTML += `
                                <div class="d-flex align-items-center mb-3 pb-3 border-bottom">
                                    <div class="me-3 text-primary">
                                        <i class="fas fa-code-branch fa-lg"></i>
                                    </div>

                                    <div class="flex-grow-1">
                                        <p class="mb-1 fw-bold" style="font-size: 0.9rem;">
                                            ${item.project_name} 
                                        </p>

                                        <span class="badge bg-success me-1">
                                            ${item.taskDone} Completed task
                                        </span>

                                        <span class="badge bg-primary me-1">
                                            ${item.taskInProgress} Active task
                                        </span>

                                        <span class="badge bg-warning text-dark">
                                            ${item.taskToDo} Pending task
                                        </span>
                                    </div>
                                </div>`;
    });
}

function populateRecentActivity_Dev(activities) {
    const container = document.getElementById("recentActivitiesCollapse");

    container.innerHTML = "";

    activities.forEach(activity => {

        let color = "bg-primary";

        switch (activity.activityType) {
            case "PROJECT_JOIN":
                color = "bg-success";
                break;

            case "PROJECT_REMOVE":
                color = "bg-danger";
                break;

            case "TASK_ASSIGN":
                color = "bg-warning";
                break;

            case "TASK_REMOVE":
                color = "bg-danger";
                break;
        }

        container.innerHTML += `
        <div class="d-flex mb-3 pb-3 border-bottom">
            <div class="mt-1 me-3">
                <span class="d-inline-block ${color} rounded-circle shadow-sm"
                      style="width:10px;height:10px;"></span>
            </div>

            <div>
                <p class="mb-0 text-dark lh-sm" style="font-size:0.85rem;">
                    ${activity.activity}
                </p>

                <small class="text-muted" style="font-size:0.7rem;">
                    ${activity.activityDate}
                </small>
            </div>
        </div>
    `;
    });
}

function populateRecentActivity_PMPO(activities) {
    const container = document.getElementById("recentActivitiesCollapse");

    container.innerHTML = "";

    activities.forEach(activity => {

        let color = "bg-primary";
        let actionButton = "";

        switch (activity.activityType) {
            case "PROJECT_JOIN":
                color = "bg-success";
                break;

            case "PROJECT_REMOVE":
                color = "bg-danger";

                actionButton = `
                    <button class="btn btn-sm btn-outline-primary"
                            onclick="editRemovalReason('${activity.assignmentId}')">
                        <i class="bi bi-pencil-square"></i>
                    </button>
                `;
                break;

            case "TASK_ASSIGN":
                color = "bg-warning";
                break;

            case "TASK_REMOVE":
                color = "bg-danger";
                break;
        }

        container.innerHTML += `
            <div class="d-flex justify-content-between mb-3 pb-3 border-bottom">

                <div class="d-flex">
                    <div class="mt-1 me-3">
                        <span class="d-inline-block ${color} rounded-circle shadow-sm"
                              style="width:10px;height:10px;"></span>
                    </div>

                    <div>
                        <p class="mb-0 text-dark lh-sm" style="font-size:0.85rem;">
                            ${activity.activity}
                        </p>

                        <small class="text-muted" style="font-size:0.7rem;">
                            ${activity.activityDate}
                        </small>
                    </div>
                </div>

                ${actionButton}

            </div>
        `;
    });
}