/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


let fileNameDisplay;
let fileInfo;
let isEditTask = false;

$(document).ready(function () {
    /**
     * Toggles the interface between View Mode and Edit Mode
     */

//    window.toggleEdit = function (enable) {
//        if (enable) {
//            $('.editable-field').not('#statusSelect').prop('readonly', false)
//                    .addClass('active-edit')
//                    .css('pointer-events', 'auto');
//            
//            $('#projNameInput').css({'border': '', 'padding-left': '0.75rem'});
//            $('#statusView').addClass('d-none');
//            $('#statusSelect').removeClass('d-none').addClass('active-edit');
//            $('#editActions').removeClass('d-none');
//            $('#editBtn, #deleteBtnHeader').addClass('d-none'); // Hide Delete button while editing
//        } else {
//            $('.editable-field').prop('readonly', true)
//                    .removeClass('active-edit')
//                    .css('pointer-events', 'none');
//            $('#projNameInput').css({'border': '1px solid transparent', 'padding-left': '0'});
//            $('#statusView').removeClass('d-none');
//            $('#statusSelect').addClass('d-none').removeClass('active-edit');
//            $('#editActions').addClass('d-none');
//            $('#editBtn, #deleteBtnHeader').removeClass('d-none');
//        }
//    };
    $('#editBtn').click(() => toggleEdit(true));
    $('#uploadModal').on('shown.bs.modal', function () {
        $(this).find('.modal-content').draggable({
            handle: ".modal-header",
            containment: "window"
        });
    });
    window.toggleEdit = function (enable) {
        const editableFields = document.querySelectorAll('.editable-field');
        const projNameInput = document.getElementById('projName');
        const editActions = document.getElementById('editActions');
        const editBtn = document.getElementById('editBtn');
        const deleteBtnHeader = document.getElementById('deleteBtnHeader');
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
        }
    };
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

//    function displayFile(file) {
//        if (file) {
//            fileInfo.classList.remove('d-none');
//            fileNameDisplay.innerText = file.name;
//            const docLabel = document.getElementById("docLabel");
//            // Optionally auto-fill Label if empty
//            if ($('#docLabel').val() === "") {
//
//                let cleanName = file.name
//                        .replace(/\.[^/.]+$/, "")   // remove extension
//                        .replace(/[_-]/g, " ")      // replace _ and -
//                        .trim();
//                docLabel.value = cleanName;
//                //$('#docLabel').val(file.name.split('.').slice(0, -1).join('.'));
//            }
//        }
//    }

    window.executeFolderDeletion = function (btn) {
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
//    window.handleModalUpload = function () {
//        // 1. You can add validation here
//        const label = $('#docLabel').val();
//        const file = $('#actualFile').val();
//
//        // More robust check for the file object
//        if (!label || fileInput.files.length === 0) {
//            alert("Please provide both a label and a file.");
//            return;
//        }
//
//        // 2. Call your existing upload function
//        addFile();
//
//        // 3. Close the modal after triggering upload
//        bootstrap.Modal.getInstance(document.getElementById('uploadModal')).hide();
//
//        // 4. Clear the form for next time
//        document.getElementById('uploadForm').reset();
//    };

    window.toggleArchiveWarning = function (val) {
        const notice = document.getElementById('archiveNotice');
        // We change this to 'Archive' to match your <option value="Archive">
        if (val === 'Archive') {
            notice.classList.remove('d-none');
        } else {
            notice.classList.add('d-none');
        }
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
document.addEventListener("DOMContentLoaded", async function () {
    console.log("doc panedd opened");
    populateViewTableDocument();
});
let counter = 0;
async function populateViewTableDocument() {

    const response = await fetch(`DocumentServlet?action=fetchdocuments&project_id=${projectId}`);
    const result = await response.json();


    console.log("Result Document : ", result.documentData);
    console.log(result.documentData);

    // ✅ 1. Clear old data
    document.getElementById("documentRegistry").innerHTML = "";
    console.log("cleaned", counter);
    result.documentData.forEach((item) => {
        try {
            appendFileRow(item);
        } catch (e) {
            console.error("Error at index", e);
        }
    });
}

// intialise taskModel
const taskModel = document.getElementById("uploadModal");
//Insert the document name based on the doc type selescted
//const doctype = taskModel.querySelector("#docType");
//doctype.addEventListener("change", () => {
//    const docTypedata = doctype.value;
//    taskModel.querySelector("#docLabel").value = `${docTypedata}_P${projectId}`;
//});


//  send document data to doPost of the DocumentServlet.java
async function sendData(formData) {

    const response = await fetch("DocumentServlet", {
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
async function handleModalUpload() {
    let action = "insert";

    const docType = taskModel.querySelector("#docType").value;
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

    const document_pdf = taskModel.querySelector("#actualFile").files[0] || null;
    formData.append("document_nameSys", docType + "_" + projectId);
    formData.append("document_pdf", document_pdf);

    const result = await sendData(formData);

    if (action === "insert" && result.status === "Success") {
        formData.append("document_id", result.document_id);
        formData.append("document_path", result.document_path);
        appendFileRow(formData);
    }

    taskModel.querySelector("#docLabel").value = "";

    taskModel.querySelector("#confirmDocBtn").innerText = "Confirm Upload";
    console.log("doc inserted successfully");

    //refresh or repopulate the view table
    populateViewTableDocument();

    // show view pane
    const triggerEl = projectDocModel.querySelector('#viewNavBtn');
    const tab = new bootstrap.Tab(triggerEl);
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
    const link = e.target.closest(".doc-edit-link");

    // VIEW button clicked
    if (viewNavBtn) {
        projectDocModel.querySelector("#confirmDocBtn")
                .classList.add("d-none");
        console.log("viewNavBtn clicked");
    }

    // UPLOAD button clicked
    else if (uploadNavBtn) {
        projectDocModel.querySelector("#confirmDocBtn").innerText = "Confirm Upload";
        projectDocModel.querySelector("#confirmDocBtn")
                .classList.remove("d-none");
    }
    // Submit upload form   
    else if (confirmDocBtn) {
        console.log("Doc Submit btn is clicked");
        handleModalUpload();
    } else if (confirmDocDeletion) {

        const row = e.target.closest("tr");
        console.log("Deletion btn clicked");

        deletionModal = document.getElementById("deleteDocModal");

        const docName = row.name;
        const docId = row.id;

        deletionModal.querySelector("#documentNameDel").innerText = docName;
        deletionModal.dataset.id = docId;
        const modal = new bootstrap.Modal(deletionModal);
        modal.show();

    } else if (e.target.closest(".docDownloadBtn")) {

    } else if (link) {
        e.preventDefault();
        const docId = link.dataset.id;
        const docName = link.dataset.name;
//        const docType = link.dataset.type;
        console.log("Link Edit Initiated  for id: ", docName, docId);
        editForm(docId);

    }

});
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

    isEditTask = true;

    const responseMeta = await fetch(`DocumentServlet?action=fetchdocument_meta&document_id=${document_id}`);
    const result = await responseMeta.json();
    console.log(result.document_name);
    console.log(result.document_name);

//    const responseContent = await fetch(`DocumentServlet?action=fetchdocument_content`);


    taskModel.querySelector("#docLabel").value = result.documentData.document_name;
    taskModel.querySelector("#docType").value = result.documentData.document_type;

    const file = {};
    file.name = result.documentData.document_name;

    taskModel.querySelector("#document_id").value = document_id;
    displayFile(file);

    projectDocModel.querySelector("#confirmDocBtn").innerText = "Confirm Edit";
    projectDocModel.querySelector("#confirmDocBtn")
            .classList.remove("d-none");

    const triggerEl = document.getElementById("deleteDocModal");
    console.log(triggerEl);
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
    newRow.innerHTML = `
                         <td>
                            <a href="#" 
                               class="fw-bold text-decoration-none doc-edit-link"
                               data-id="${data.document_id}"
                               data-name="${document_name}">
                                ${document_name}
                                
                            </a>
                        </td>
                         <td><span class="badge bg-secondary-subtle text-secondary border">PNG</span></td>
                         <td class="text-center pe-4">
                            <button class="btn btn-sm btn-light border p-1 px-2"><i class="fas fa-eye text-muted"></i></button>
                            <button class="btn btn-sm btn-light border p-1 px-2">
                                <i class="fas fa-download text-muted"></i>
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
