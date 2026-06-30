/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


let fileNameDisplay;
let fileInfo;
let isEditTask = false;
let cacheData;

document.addEventListener("DOMContentLoaded", async function () {
    console.log("DOM LOADED");
    const response = await fetch(`ProjectPageServlet?action=fetchProjectinfo&project_id=${projectId}`);
    const result = await response.json();
    console.log("Project Data", result);
    document.getElementById("id-badge").innerText = "ID: " + result.projectData.projectId;

    const statusBadge = document.getElementById("status-badge");
    const projStatus = result.projectData.projectStatus;
    console.log(projStatus);
    if (projStatus === "Active") {
        statusBadge.classList.add("badge-soft-success");
    } else if (projStatus === "Delayed") {
        statusBadge.classList.add("badge-soft-danger");
    } else if (projStatus === "Archive") {
        statusBadge.classList.add("badge-soft-archive");
    } else if (projStatus === "Planned"){
        statusBadge.classList.add("badge-soft-planned");
    }

    statusBadge.innerHTML =
            `<i class="fas fa-circle me-1"></i> Status: ${projStatus}`;

    document.getElementById("projectName-badge").innerText = result.projectData.projectName;
    document.getElementById("projName").value = result.projectData.projectName;
    document.getElementById("projectType").value = result.projectData.projectType;
    document.getElementById("projClient").value = result.projectData.projectClient;
    document.getElementById("projDesc").value = result.projectData.projectDesc;
    document.getElementById("projStatus").value = result.projectData.projectStatus;
    document.getElementById("ProjStart").value = result.projectData.projStartDate;
    document.getElementById("ProjEnd").value = result.projectData.projEndDate;
    //document.getElementById("projDate").value = result.projectData.projCreatedAt;

    // 1. Grab the raw date string from your database result
    const rawDateString = result.projectData.projCreatedAt;

    // 2. Format it nicely (e.g., "Jan 21, 2026, 12:49 PM")
    const formattedDate = new Date(rawDateString).toLocaleString('en-US', {
        month: 'short',
        day: 'numeric',
        year: 'numeric',
        hour: 'numeric',
        minute: '2-digit',
        hour12: true
    });
    console.log(formattedDate);

    // 3. Target your clean text element using textContent instead of .value
    document.getElementById("projDate").value = formattedDate;
    
    //for status Badge
    const riskScore = result.projectData.project_risk_score;
    const riskBadgeClass =
            riskScore <= 30
            ? "badge-soft-success"
            : riskScore <= 60
            ? "badge-soft-warning"
            : "badge-soft-danger";

    document.getElementById("riskScore").innerText = riskScore;

    console.log("project Date", result.projectData.projEndDate);
});

function validateProjectForm() {

    console.log("check 2");
    // Retrieve Input Values   
    const projectName = document.getElementById("projName").value.trim();
    const projectType = document.getElementById("projectType").value.trim();
    const projectClient = document.getElementById("projClient").value.trim();
    const projectDesc = document.getElementById("projDesc").value.trim();
    const projectStatus = document.getElementById("projStatus").value.trim();
    const projStartDate = document.getElementById("ProjStart").value;
    const projEndDate = document.getElementById("ProjEnd").value;

    // Error Elements
    const errorProjectName = document.getElementById("errorProjectName");
    const errorProjectType = document.getElementById("errorProjectType");
    const errorClientName = document.getElementById("errorClientName");
    const errorProjectDesc = document.getElementById("errorProjectDesc");
    const errorProjectStatus = document.getElementById("errorProjectStatus");
    const errorMsgStartDate = document.getElementById("errorMsgStartDate");
    const errorDateRange = document.getElementById("errorDateRange");

    // Clear Previous Errors
    errorProjectName.textContent = "";
    errorProjectType.textContent = "";
    errorClientName.textContent = "";
    errorProjectDesc.textContent = "";
    errorProjectStatus.textContent = "";
    errorMsgStartDate.textContent = "";
    errorDateRange.textContent = "";

    let isValid = true;


    // Project Name Validation
    if (projectName === "") {
        errorProjectName.textContent = "Project name is required";
        errorProjectName.classList.remove("d-none");
        isValid = false;
    } else if (projectName.length < 3) {
        errorProjectName.textContent = "Project name must be at least 3 characters";
        errorProjectName.classList.remove("d-none");
        isValid = false;
    }

    // Project Type Validation
    if (projectType === "") {
        errorProjectType.textContent = "Please select project type";
        errorProjectType.classList.remove("d-none");
        isValid = false;
    }

    // Client Name Validation
    if (projectClient === "") {
        errorClientName.textContent = "Client name is required";
        errorClientName.classList.remove("d-none");
        isValid = false;
    } else if (projectClient.length < 3) {
        errorClientName.textContent = "Client name is too short";
        errorClientName.classList.remove("d-none");
        isValid = false;
    }

    // Description Validation
    if (projectDesc === "") {
        errorProjectDesc.textContent = "Project description is required";
        errorProjectDesc.classList.remove("d-none");
        isValid = false;
    } else if (projectDesc.length < 10) {
        errorProjectDesc.textContent = "Description must be at least 10 characters";
        errorProjectDesc.classList.remove("d-none");
        isValid = false;
    }

    // Project Status Validation
    if (projectStatus === "") {
        errorProjectStatus.textContent = "Project status is required";
        errorProjectStatus.classList.remove("d-none");
        isValid = false;
    }

    // Start Date Validation
    if (projStartDate === "") {
        errorMsgStartDate.textContent = "Start date is required";
        errorMsgStartDate.classList.remove("d-none");
        isValid = false;
    }

    // Deadline Validation
    if (projEndDate === "") {
        errorDateRange.textContent = "Deadline is required";
        errorDateRange.classList.remove("d-none");
        isValid = false;
    }

    // Date Range Validation
    if (projStartDate !== "" && projEndDate !== "") {

        const start = new Date(projStartDate);
        const end = new Date(projEndDate);

        if (end < start) {
            errorDateRange.textContent =
                    "Deadline cannot be earlier than start date";
            errorDateRange.classList.remove("d-none");
            isValid = false;
        }
    }

    const data = {
        action: "projectInfoUpdate",
        projectId: projectId,
        projectName: projectName,
        projectDesc: projectDesc,
        projectType: projectType,
        projectClient: projectClient,
        projStartDate: projStartDate,
        projEndDate: projEndDate
    };

    if (isValid) {
        return data;
    } else {
        return;
    }

}
document.getElementById("projectForm").addEventListener("click", async function (e) {

    // =====================================
    // Hide Validation Message On Click
    // =====================================

    const field = e.target;

    const errorMap = {
        "projName": "errorProjectName",
        "projectType": "errorProjectType",
        "projClient": "errorClientName",
        "projDesc": "errorProjectDesc",
        "projStatus": "errorProjectStatus",
        "ProjStart": "errorMsgStartDate",
        "ProjEnd": "errorDateRange"
    };

    if (errorMap[field.id]) {

        const errorElement =
                document.getElementById(errorMap[field.id]);

        errorElement.classList.add("d-none");
        errorElement.textContent = "";
    }

    // =====================================
    // Submit Button Click
    // =====================================

    const formSubbtn = e.target.closest("#formSubbtn");
    const formCanbtn = e.target.closest("#formCanbtn");

    if (formSubbtn) {

        e.preventDefault();

        console.log("UpdateInstantiated");

        const data = validateProjectForm();

        console.log(data);

        // Stop if validation failed
        if (!data) {
            return;
        }

        const result = await sendData_project(data);

        console.log(result);

        if (result.status === "Success") {

            populateProjectDetails(data);

            toggleEdit(false);

            displayMessage(result.message, result.status);
        }
    } else if (formCanbtn) {
        populateProjectDetails(cacheData);
        document.querySelectorAll(".validation-message").forEach(error => {

            error.classList.add("d-none");
            error.textContent = "";
        });
    }
});


function populateProjectDetails(data) {

    document.getElementById("projName").value = data.projectName;
    document.getElementById("projectType").value = data.projectType;
    document.getElementById("projClient").value = data.projectClient;
    document.getElementById("projDesc").value = data.projectDesc;
    document.getElementById("ProjStart").value = data.projStartDate;
    document.getElementById("ProjEnd").value = data.projEndDate;
}

async function sendData_project(data) {
    console.log("Send Data Project ");
    const response = await fetch("ProjectPageServlet", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });
    if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const result = await response.json();
    return result;
}

if (user_role === "Project Manager") {

    let uploadModel;
    document.getElementById("editBtn").addEventListener("click", function () {
        //save the old data 
        cacheData = {
            projectName: document.getElementById("projName").value.trim(),
            projectType: document.getElementById("projectType").value.trim(),
            projectClient: document.getElementById("projClient").value.trim(),
            projectDesc: document.getElementById("projDesc").value.trim(),
            projectStatus: document.getElementById("projStatus").value.trim(),
            projStartDate: document.getElementById("ProjStart").value,
            projEndDate: document.getElementById("ProjEnd").value,
        };

        toggleEdit(true);
    });
}

function toggleEdit(enable) {
    console.log("VAnilla togle edit");
    const editableFields = document.querySelectorAll('.editable-field');
    const projNameInput = document.getElementById('projName');
    const editActions = document.getElementById('editActions');
    const editBtn = document.getElementById('editBtn');
    const deleteBtnHeader = document.getElementById('deleteBtnHeader');
    const manageDocBtn = document.getElementById('manageDocBtn');
    if (enable) {
// Handle all editable fields except statusSelect
        editableFields.forEach(field => {
            if (field.id !== 'projStatus' && field.id !== 'projDate') {
                field.readOnly = false;
                field.classList.add('active-edit');
                field.style.pointerEvents = 'auto';
                console.log("Reached");
            }
        });
        // UI Adjustments for "Edit Mode"
        projNameInput.style.border = '';
        projNameInput.style.paddingLeft = '0.75rem';
        console.log("Reached2");
        editActions.classList.remove('d-none');
        // Hide primary buttons
        editBtn.classList.add('d-none');
        deleteBtnHeader.classList.add('d-none');
        manageDocBtn.classList.add('d-none');
    } else {
// Handle all editable fields (resetting)
        editableFields.forEach(field => {
            field.readOnly = true;
            field.classList.remove('active-edit');
            field.style.pointerEvents = 'none';
        });
        // UI Adjustments for "View Mode"
        projNameInput.style.border = '1px solid transparent';
        projNameInput.style.paddingLeft = '0';
        editActions.classList.add('d-none');
        // Show primary buttons
        editBtn.classList.remove('d-none');
        deleteBtnHeader.classList.remove('d-none');
        manageDocBtn.classList.remove('d-none');
    }
}
;
//Project folder delet
document.getElementById("deleteBtn").addEventListener("click", async function () {

    const data = {
        action: "delete",
        projectId: projectId
    };
    const result = await sendData_project(data);
    console.log(result);
    if (result.status === "Success") {
// 3. SUCCESS UI SWAP
// Hide the header and footer area
        document.getElementById('initialBody').classList.add('d-none');
        // Show the success checkmark body
        document.getElementById('successBody').classList.remove('d-none');
        // 4. Final Redirect after 2 seconds
        setTimeout(() => {
            window.location.href = "dashboard.jsp";
        }, 2000);
    } else if (result.status === "failed") {
        displayFile(result.message, result.status);
    }
});
function displayMessage(msg, status) {

    let alertClass = "alert-success";
    let icon = "fa-check-circle";
    if (status === "failed") {
        alertClass = "alert-danger";
        icon = "fa-circle-xmark";
    }
    const messageDiv = document.getElementById("statusTab");
    messageDiv.innerHTML = `<div id="successProcessTab" class="alert ${alertClass} alert-dismissible fade show shadow-lg border-0 d-flex align-items-center" role="alert">
                        <div class="icon-container me-3">
                            <i class="fas ${icon} fa-lg"></i>
                        </div>
                        <div class="message-content">
                            <h6 class="alert-heading mb-0 fw-bold" style="font-size: 0.9rem;">${msg}</h6>
                        </div>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>`;
    setTimeout(() => {
        messageDiv.innerHTML = "";
    }, 5000);
    return;
}

$(document).ready(function () {
    /**
     * Toggles the interface between View Mode and Edit Mode
     */

    $('#uploadModal').on('shown.bs.modal', function () {
        $(this).find('.modal-content').draggable({
            handle: ".modal-header",
            containment: "window"
        });
    });
    const dropZone = document.getElementById('dropZone');
    const fileInput = document.getElementById('actualFile');
    fileInfo = document.getElementById('fileInfo');
    console.log("File info : ", fileInfo);
    fileNameDisplay = document.getElementById('selectedFileName');
    // Click to select
    dropZone.addEventListener('click', () => fileInput.click());
    // Handle file selection via input
    fileInput.addEventListener('change', function () {
        displayFile(this.files[0]);
    });
    // Drag and drop events
    ['dragover', 'dragleave', 'drop'].forEach(eventName => {
        dropZone.addEventListener(eventName, e => {
            e.preventDefault();
            e.stopPropagation();
        });
    });
    dropZone.addEventListener('dragover', () => dropZone.classList.add('dragover'));
    dropZone.addEventListener('dragleave', () => dropZone.classList.remove('dragover'));
    dropZone.addEventListener('drop', (e) => {
        dropZone.classList.remove('dragover');
        const files = e.dataTransfer.files;
        if (files.length > 0) {
            fileInput.files = files; // Assign dropped file to the hidden input
            displayFile(files[0]);
        }
    });
    window.executeFolderDeletion = function (btn) {
// 1. Loading State on button
        const originalContent = btn.innerHTML;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Processing...';
        btn.disabled = true;
        // 2. AJAX Request to your Servlet
        $.ajax({
            url: 'ProjectPageServlet',
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
    const tx = document.getElementsByTagName("textarea");
    for (let i = 0; i < tx.length; i++) {
        tx[i].setAttribute("style", "height:" + (tx[i].scrollHeight) + "px;overflow-y:hidden;");
        tx[i].addEventListener("input", OnInput, false);
    }

    function OnInput() {
        this.style.height = "auto";
        this.style.height = (this.scrollHeight) + "px";
    }
});
function displayFile(file) {
    if (file) {
        fileInfo.classList.remove('d-none');
        fileNameDisplay.innerText = file.name;
        const docLabel = document.getElementById("docLabel");
        // Optionally auto-fill Label if empty
        if ($('#docLabel').val() === "") {

            let cleanName = file.name
                    .replace(/\.[^/.]+$/, "")   // remove extension
                    .replace(/[_-]/g, " ")      // replace _ and -
                    .trim();
            docLabel.value = cleanName;
            //$('#docLabel').val(file.name.split('.').slice(0, -1).join('.'));
        }
    }
}


// Load the document when the document model opens
document.getElementById("manageDocBtn").addEventListener("DOMContentLoaded", async function () {
    console.log("doc panedd opened");
    populateViewTableDocument();
});
async function populateViewTableDocument() {

    const response = await fetch(`ProjectDocumentServlet?action=fetchdocuments&project_id=${projectId}`);
    const result = await response.json();
    console.log("Result Document : ", result.documentData);
    console.log(result.documentData);
    // ✅ 1. Clear old data
    document.getElementById("documentRegistry").innerHTML = "";
    result.documentData.forEach((item) => {
        try {
            appendFileRow(item);
        } catch (e) {
            console.error("Error at index", e);
        }
    });
    return;
}

// intialise taskModel
//const taskModel = document.getElementById("uploadModal");
//Insert the document name based on the doc type selescted
//const doctype = taskModel.querySelector("#docType");
//doctype.addEventListener("change", () => {
//    const docTypedata = doctype.value;
//    taskModel.querySelector("#docLabel").value = `${docTypedata}_P${projectId}`;
//});


//  send document data to doPost of the DocumentServlet.java
async function sendData(formData) {

    const response = await fetch("ProjectDocumentServlet", {
        method: "POST",
        body: formData
    });
    if (!response.ok) {
        throw new Error("Server error" + response.status);
    }

    const result = await response.json();
    return result;
}
;
// get the data from the docUpload form 
// intialise taskModel
const taskModel = document.getElementById("uploadModal");
async function handleModalUpload() {
    let action = "insert";
    const docType = taskModel.querySelector("#docType").value;

    // creation formdata to send data 
    const formData = new FormData();
    if (isEditTask) {
        action = "edit";
        formData.append("document_id", taskModel.querySelector("#document_id").value);
    } else {
        formData.append("project_id", projectId);
        formData.append("document_nameSys", docType + "_" + projectId);
    }

    formData.append("action", action);
    formData.append("document_name", taskModel.querySelector("#docLabel").value);
    formData.append("document_type", docType);
    formData.append("document_nameSys", docType + "_" + projectId);

    const document_pdf = taskModel.querySelector("#actualFile").files[0] || null;
    formData.append("document_pdf", document_pdf);

    const result = await sendData(formData);

    if (action === "insert" && result.status === "Success") {
        formData.append("document_id", result.document_id);
        formData.append("document_path", result.document_path);
        appendFileRow(formData);
    }

    taskModel.querySelector("#docLabel").value = "";
    //taskModel.querySelector("#confirmDocBtn").innerText = "Confirm Upload";
    console.log("doc inserted successfully");
    //refresh or repopulate the view table
    populateViewTableDocument();
    // show view pane
    const triggerEl = projectDocModel.querySelector('#viewNavBtn');
    const tab = bootstrap.Tab.getOrCreateInstance(triggerEl);
    tab.show();
}
;
// To populate the doc table when the doc model opens
let bsTaskModal = null;
document.getElementById("manageDocBtn").addEventListener("click", async function () {

// ✅ 1. Clear old data
    document.getElementById("documentRegistry").innerHTML = "";
    populateViewTableDocument();
    bsTaskModal = new bootstrap.Modal(document.getElementById('projectDocModal'));
    bsTaskModal.show();
    const triggerEl = projectDocModel.querySelector('#viewNavBtn');
    const tab = new bootstrap.Tab(triggerEl);
    tab.show();
});
// add a single lister to entire pane covers(navigation bar, edit, delete and view)
let deletionModal;
const projectDocModel = document.getElementById("projectDocModal");
projectDocModel.addEventListener("click", function (e) {

    const viewNavBtn = e.target.closest("#viewNavBtn");
    const uploadNavBtn = e.target.closest("#uploadNavBtn");
    const confirmDocBtn = e.target.closest("#confirmDocBtn");
    const confirmDocDeletion = e.target.closest(".docDeleteBtn");
    const documentViewbtn = e.target.closest(".docViewBtn");
    const documentDownloadbtn = e.target.closest(".docDownloadBtn");
    const link = e.target.closest(".doc-edit-link");
    const docType = e.target.closest("#docType");
    const dropZone = e.target.closest("#dropZone");

    // VIEW button clicked
    if (viewNavBtn) {
        projectDocModel.querySelector("#confirmDocBtn")
                .classList.add("d-none");
        console.log("viewNavBtn clicked");
    } else if (documentViewbtn) {
        console.log("View Btn Clicked");
        const row = e.target.closest("tr");
        window.open(`ProjectDocumentServlet?action=fetchdocument_view&document_id=${row.id}`);
    }
    // UPLOAD button clicked
    else if (uploadNavBtn) {
        cleanUploadPane();
        projectDocModel.querySelector("#confirmDocBtn").innerText = "Confirm Upload";
        projectDocModel.querySelector("#confirmDocBtn")
                .classList.remove("d-none");
    }
    // Submit upload form   
    else if (confirmDocBtn) {
        console.log("Doc Submit btn is clicked");

        const isvalid = validateDocumentForm();
        if (!isvalid) {
            return;
        }

        handleModalUpload();
    } else if (confirmDocDeletion) {

        const row = e.target.closest("tr");
        console.log("Deletion btn clicked");
        deletionModal = document.getElementById("deleteDocModal");
        const docName = row.name;
        const docId = row.id;
        console.log(docId);
        deletionModal.querySelector("#documentNameDel").innerText = docName;
        deletionModal.dataset.id = docId;
        const modal = new bootstrap.Modal(deletionModal);
        modal.show();

    } else if (documentDownloadbtn) {
        console.log("document Download initialized ");
        const row = e.target.closest("tr");
        window.open(`ProjectDocumentServlet?action=downloadDocument&document_id=${row.id}`);
    } else if (link) {
        e.preventDefault();
        const row = e.target.closest("tr");
        const docId = row.id;
        const docName = link.dataset.name;
//        const docType = link.dataset.type;
        console.log("Link Edit Initiated  for id: ", docName, docId);
        editForm(docId);
    }
    // Hide category error
    else if (docType) {

        const errorDocType =
                document.getElementById("errorDocType");

        errorDocType.classList.add("d-none");
        errorDocType.textContent = "";
    }

    // Hide attachment error
    else if (dropZone) {

        const errorAttachment =
                document.getElementById("errorAttachment");

        errorAttachment.classList.add("d-none");
        errorAttachment.textContent = "";
    }



});


function validateDocumentForm() {

    // =====================================
    // Retrieve Values
    // =====================================

    const docType =
            document.getElementById("docType").value;

    const actualFile =
            document.getElementById("actualFile").files[0];

    // =====================================
    // Error Elements
    // =====================================

    const errorDocType =
            document.getElementById("errorDocType");

    const errorAttachment =
            document.getElementById("errorAttachment");

    // =====================================
    // Clear Previous Errors
    // =====================================

    errorDocType.textContent = "";
    errorDocType.classList.add("d-none");

    errorAttachment.textContent = "";
    errorAttachment.classList.add("d-none");

    let isValid = true;

    // =====================================
    // Document Category Validation
    // =====================================

    if (!docType || docType.trim() === "") {

        errorDocType.textContent =
                "Please select document category";

        errorDocType.classList.remove("d-none");

        isValid = false;
    }

    // =====================================
    // File Validation
    // =====================================

    if (!actualFile) {

        errorAttachment.textContent =
                "Please upload an attachment";

        errorAttachment.classList.remove("d-none");

        isValid = false;
    } else {

        // =====================================
        // File Type Validation
        // =====================================

        const allowedTypes = [
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        ];

        if (!allowedTypes.includes(actualFile.type)) {

            errorAttachment.textContent =
                    "Only PDF or Word documents are allowed";

            errorAttachment.classList.remove("d-none");

            isValid = false;
        }

        // =====================================
        // File Size Validation
        // 5MB Limit
        // =====================================

        const maxSize = 5 * 1024 * 1024;

        if (actualFile.size > maxSize) {

            errorAttachment.textContent =
                    "File size cannot exceed 5MB";

            errorAttachment.classList.remove("d-none");

            isValid = false;
        }
    }

    // =====================================
    // Return Result
    // =====================================

    return isValid;
}


function cleanUploadPane() {

    console.log("cleanUploadPane executed");

    document.getElementById("actualFile").value = "";

    document.getElementById("selectedFileName")
            .textContent = "";

    document.getElementById("docLabel").innerText = "";

    document.getElementById("docType").value = "";

    document.getElementById("fileInfo")
            .classList.add("d-none");


    document.querySelectorAll(".validation-message")
            .forEach(error => {

                error.classList.add("d-none");

                error.textContent = "";
            });
}


document.getElementById("deletedocBtnCfm").addEventListener("click", async function () {

    const formData = new FormData();
    formData.append("action", "delete");
    formData.append("document_id", deletionModal.dataset.id);
    console.log(formData);
    const result = await sendData(formData);
    if (result.status === "Success") {

        const modal = bootstrap.Modal.getInstance(deletionModal);
        console.log(modal);
        modal.hide();
        //refresh or repopulate the view table
        populateViewTableDocument();
        const triggerEl = projectDocModel.querySelector('#viewNavBtn');
        const tab = new bootstrap.Tab(triggerEl);
        tab.show();
    }
});

async function editForm(document_id) {
    console.log("Edit form");
    isEditTask = true;
    const responseMeta = await fetch(`ProjectDocumentServlet?action=fetchdocument_meta&document_id=${document_id}`);
    const result = await responseMeta.json();
    console.log(result.document_name);
    console.log(result.document_name);
    taskModel.querySelector("#docLabel").value = result.documentData.document_name;
    taskModel.querySelector("#docType").value = result.documentData.document_type;
    const file = {};
    file.name = result.documentData.document_name;
    taskModel.querySelector("#document_id").value = document_id;
    displayFile(file);
    projectDocModel.querySelector("#confirmDocBtn").innerText = "Confirm Edit";
    projectDocModel.querySelector("#confirmDocBtn")
            .classList.remove("d-none");
    const triggerEl = projectDocModel.querySelector('#uploadNavBtn');
    const tab = new bootstrap.Tab(triggerEl);
    tab.show();
}


function appendFileRow(data) {

    console.log("New Row Added");
    const docDiv = document.getElementById("documentRegistry");
    const document_id = data.document_id;
    const document_name = data instanceof FormData
            ? data.get("document_name")
            : data.document_name;
//    const document_type = data instanceof FormData
//            ? data.get("document_type")
//            : data.document_type;

    const newRow = document.createElement("tr");
    newRow.id = data.document_id;
    newRow.name = data.document_name;
//    const deletebtn = (userRole === "Project Manager") ? ` <button class="btn btn-sm btn-light border p-1 px-2 ms-1 docDeleteBtn">
//                                <i class="fas fa-trash-alt text-danger"></i>
//                            </button>` : ``;

    newRow.innerHTML = `
                         <td>
                            <a href="#" 
                               class="fw-bold text-decoration-none doc-edit-link">
                                ${document_name}
                            </a>
                        </td>
                         <td><span class="badge bg-secondary-subtle text-secondary border">PNG</span></td>
                         <td class="text-center pe-4">
                            <button class="btn btn-sm btn-light border p-1 px-2 docViewBtn">
                                <i class="fas fa-eye text-muted"></i>
                            </button>
                            <button class="btn btn-sm btn-light border p-1 px-2 docDownloadBtn">
                                <i class="fas fa-download text-muted "></i>
                            </button>
                            <button class="btn btn-sm btn-light border p-1 px-2 ms-1 docDeleteBtn">
                                <i class="fas fa-trash-alt text-danger"></i>
                            </button>
            </td>
            `;
    // ✅ append to table
    docDiv.appendChild(newRow);
//    // ✅ correct way to close modal
//    let modal = bootstrap.Modal.getInstance(taskModel);
//    if (!modal) {
//        modal = new bootstrap.Modal(taskModel);
//    }


}
