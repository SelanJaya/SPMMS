// */                                                                                                                                           
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
// */                                                                                                                                           


/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

let backlogDocModal = null;
let viewDocPane = null;
let uploadDocPane = null;
let isEdit;

// fetch the backlog data from the db after the backlog page is displayed
document.addEventListener("DOMContentLoaded", async function () {
    console.log(document.querySelector('.top-nav').offsetHeight);

    const response = await fetch(`BacklogServlet?project_id=${projectId}&action=fetchBacklogs`);
    const result = await response.json();


    if (result.data.length > 0) {
        const lastItem = result.data[result.data.length - 1];
        lowestPriority = Number(lastItem.backlogI_priority);
    } else {
        lowestPriority = 0;
    }
    for (const item of result.data) {
        addbacklogToTable(item);
    }
});


// Update the backlog items
document.addEventListener("DOMContentLoaded", () => {

    table = document.getElementById("backlogTable");

    //focusin work for contenteditable element
    table.addEventListener("focusin", (e) => {

        //react is the focused element has class "editable-cell"
        if (!e.target.classList.contains("editable-cell"))
            return;

        oldValue = e.target.textContent.trim();

    });

    //detect when the user leave the cell editable(editing finished)
    table.addEventListener("focusout", (e) => {

        console.log("focues out");
        getBacklogRowData(e);

    });
});


async function getBacklogRowData(e, option) {
    console.log("getBacklogRowData execute");
    let oldValue = "";

    const cell = e.target.closest(".editable-cell");
    console.log(cell);
    if (!cell)
        return;

    //Get the new value after editing 
    const newValue = e.target.textContent.trim();

    if (newValue === oldValue)
        return;

    const row = cell.closest("tr");
    console.log(row);
    const backlogId = row.dataset.id;

    const rowData = {backlogI_id: backlogId};
    console.log(cell.id);
    
    if(cell.id === "status") {
        rowData.action = "Update_status";
    }else{
        rowData.action = "Update_PO";
    }
    console.log(rowData);
    console.log(userRole);

    let value;
    let field;
    if (userRole === "Product Owner") {
        console.log("PO");

        const cells = row.querySelectorAll(".editable-cell");
        console.log(cells);
        cells.forEach(cell => {

            field = cell.dataset.field;
            console.log(field);

            // ✅ handle by event type cleanly
            if (e.type === "click" && field !== "status")
                return;
            if (e.type === "focusout" && field === "status")
                return;

            if (field === "status") {
                value = cell.dataset.status;
                console.log(value);
                if (value === "Rejected") {
                    if (value === "Rejected") {
                        rowData["rejection_reason"] = cell.dataset.reason || "";
                        rowData["last_updated_by"] = userId;
                    }
                }
            } else {
                value = cell.textContent.trim();
            }

            console.log(value);

//            if (field === "priority" || field === "storyPoint") {
//                // filter out the ui ...
//                if (!value || value.trim() === "..." || isNaN(value)) {
//                    value = 0;
//                }
//            }

            if ((field === "mandays" || field === "story_point") &&
                    (value === "..." || value.trim() === "")) {
                value = 0;
            }

            rowData[field] = value;
        });
    } else if (userRole === "Developer") {
        const cells = row.querySelectorAll(".editable-cell.dev-editable");

        cells.forEach(cell => {
            const field = cell.dataset.field;
            console.log(field);
            if (field === "backlogI_desc") {
                rowData["status"] = "Refined";
                rowData["last_updated_by"] = userId;

            }

            let value = cell.textContent.trim();
            console.log(field, value);

            if ((field === "mandays" || field === "story_point") &&
                    (value === "..." || value.trim() === "")) {
                value = 0;
            }

            // filter out the ui ...
            rowData[field] = value;
            option = row.querySelector(".status-dropdown");
            console.log(option);


        });
        rowData.action = "Update_Dev";
    }

    console.log(rowData);

//    try {
    //call cervlet to update the field
    const result = await updateBacklogRow(rowData);

    if (result.status === "Success") {
        console.log("Option : ", option);
        // 🔒 Lock developer editable fields after update
        if (userRole === "Developer") {
            const editableCells = row.querySelectorAll(".editable-cell.dev-editable");

            editableCells.forEach(cell => {
                cell.classList.remove("dev-editable");
                cell.setAttribute("contenteditable", "false");
            });
        }
        if (option) {
            //after user select option
            const newStatus = option.dataset.value || rowData.status || row.status.dataset.status;
            console.log(newStatus);

            //insert in the ui to fetch later
            const container = option.closest(".status-container");
            console.log(container);
            applyStatusUI(container, newStatus);
        }
    }
//    } catch (err) {
//        console.error("Update Failed:", err);
//        alert("Failed to save change");
//    }
}
;



//call the servlet and send the updated data 
async function updateBacklogRow(rowData) {

    const response = await fetch("BacklogServlet", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(rowData)
    });

    if (!response.ok) {
        throw new Error("Server error" + response.status);
    }

    const result = await response.json();

    if (result.status !== "Success") {
        throw new Error("Update failed");
    }

    return result;
}
;


$(document).ready(function () {

    table = $('#backlogTable').DataTable({
        order: [[1, 'asc']],
        paging: false, info: false, searching: true,
        columnDefs: [{targets: 'no-sort', orderable: false}]

    });

    if (userRole !== "Product Owner" && userRole !== "Developer") {
        table.column('.action-col').visible(false);
    }

//    const sidebar = document.getElementById('sidebar');
//    // 1. Sidebar Toggle
//    function toggleSidebar() {
//        sidebar.classList.toggle('collapsed');
//        setTimeout(() => {
//            table.columns.adjust().draw();
//        }, 400);
//    }

    //sidebar.addEventListener('dblclick', toggleSidebar);
    //$('#sidebarToggle').on('click', toggleSidebar);

    if (userRole === "Product Owner") {
        // 2. Drag & Drop Reordering
        Sortable.create(document.getElementById('sortableBody'), {
            handle: '.drag-handle',
            animation: 200,
            onEnd: async function (evt) {

                let newOrder = [];

                $('#sortableBody tr').each(function (index) {

                    // update visible priority
                    $(this).find('.priority-rank').text(index + 1);

                    newOrder.push({
                        backlogI_id: Number($(this).attr('data-id')),
                        backlogI_priority: index + 1
                    });
                });

                console.log("New Order:", newOrder);

                try {
                    await updateBacklogRow({
                        action: "Reorder",
                        order: newOrder
                    });

                } catch (err) {
                    console.error("Server failed");
                }
            }
        });
    }


    // 3. Moveable Modal Setup
    $('#backlogDocModal').on('shown.bs.modal', function () {
        $(this).find('.modal-content').draggable({
            handle: ".modal-header",
            containment: "window"
        });
    });

    // 4. Document Manager: Tab & File Logic
    const dz = $('#modalDropZone'), fi = $('#modalFileField'), fn = $('#fileNameText'), preview = $('#filePreview');
    $('button[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
        const target = $(e.target).attr('data-bs-target');
        target === '#uploadPane' ? $('#confirmBtn').removeClass('d-none') : $('#confirmBtn').addClass('d-none');
    });
    dz.on('click', () => fi.click());

    fi.on('change', function () {
        if (this.files[0]) {
            fn.text(this.files[0].name);
            preview.removeClass('d-none');
            if ($('#docNameInput').val() === "") {
                $('#docNameInput').val(this.files[0].name.split('.').slice(0, -1).join('.'));
            }
        }
    });

    dz.on('dragover', (e) => {
        e.preventDefault();
        dz.addClass('bg-primary-subtle border-primary');
    });
    dz.on('dragleave', () => dz.removeClass('bg-primary-subtle border-primary'));
    dz.on('drop', (e) => {
        e.preventDefault();
        dz.removeClass('bg-primary-subtle border-primary');
        const files = e.originalEvent.dataTransfer.files;
        if (files.length > 0) {
            fi[0].files = files;
            fi.trigger('change');
        }
    });

});

// START INSET ADD listener to the form submit button
document.getElementById('confirmAddBtn').addEventListener('click', handleAddBacklog);

let backlogid = null;
document.getElementById("backlogTable").addEventListener("click", async function (e) {
    let dropDown;
    const deletebtn = e.target.closest(".btn-delete");
    const manageDocbtn = e.target.closest(".btn-manageDoc");
    const option_div = e.target.closest(".status-dropdown");
    option = e.target.closest(".dropdown-item");
    const statusPill = e.target.closest(".status-container");

    console.log(statusPill);
    if (statusPill) {
        handleDropDownUI(e);
    }
    if (manageDocbtn) {
        const row = e.target.closest("tr");
        console.log(row);
        console.log("managebtn clicked");

        backlogid = row.getAttribute("data-id");
        console.log(backlogid);


        //get the managedocumentPane
        backlogDocModal = document.getElementById("backlogDocModal");
        backlogDocModal.querySelector("#backlog_id").value = backlogid;
        console.log(backlogDocModal);

        populateViewDocTable();
        backlogDocModalBoot = new bootstrap.Modal(backlogDocModal);
        backlogDocModal.querySelector("#backlog_id").value = backlogid;
        backlogDocModalBoot.show();
    }
    if (deletebtn) {
        console.log("Delete btn click");
        backlogid = deletebtn.getAttribute("data-bs-id");
        const title = deletebtn.getAttribute("data-bs-title");

        document.getElementById("backlogTitle").textContent = title;
        deleteModal.show();
    }
    if (option) {
//            //after user select option
        const newStatus = option.dataset.value;
//
//            //insert in the ui to fetch later
        const container = option.closest(".status-container");


        //Open the rejectResoning model to get the reason
        // 2. open the modal
        if (newStatus === "View_EditReason") {
            const row = e.target.closest("tr");
            backlogid = row.getAttribute("data-id");

            handleReasonModel(backlogid);
        } else {
            if (newStatus === "Rejected") {
                const modalEl = document.getElementById('rejectBacklogModal');
                const modal = new bootstrap.Modal(modalEl);


                modalEl._context = {
                    container: container,
                    event: e,
                    option: option
                };

                modal.show();
                return;
            }

            // 3. Store value in dataset (THIS is your injected value)
            container.dataset.status = newStatus;
            console.log(container.dataset.status);

            getBacklogRowData(e, option);
            // 4. Hide dropdown
            container.querySelector(".status-dropdown").classList.add("d-none");
        }
    }
    //new bootstrap.Modal(document.getElementById("deleteBacklogModal")).show();
});

async function handleReasonModel(backlogI_id) {
    const modalEl = document.getElementById('rejectBacklogModal');
    const modal = new bootstrap.Modal(modalEl);

    const response = await fetch(`BacklogServlet?action=fetchReason&backlogId=${backlogI_id}`);
    const result = await response.json();
    console.log(result);

    const reasonField = document.getElementById("rejectionReason");
    reasonField.value = result.rejection_reason;

    if (userRole === "Developer") {
        reasonField.readOnly = true;
        document.getElementById("rejectionPromptMessage").innerText = "This Backlog Item is rejected by Product Owner due to the reason below";
        document.getElementById("buttonDiv").classList.add("d-none");
    } else {
        const submitModelElBtn = modalEl.querySelector("#confirmRejectBtn");
        submitModelElBtn.innerText = "Confirm Edit";
        modalEl.dataset.action = "edit";
        modalEl.dataset.backlogI_id = backlogI_id;
    }
    modal.show();
    return;
}


document.getElementById("confirmRejectBtn").addEventListener("click", async function () {
    const modalEl = document.getElementById('rejectBacklogModal');
    console.log(modalEl);
    const action = modalEl.dataset.action;

    if (action === "insert") {
        const context = modalEl._context;

        const reasonInput = document.getElementById("rejectionReason");
        const reason = reasonInput.value.trim();

        if (!reason) {
            console.log("Reason required");
            return;
        }

        const {container, event, option} = context;

        // update dataset
        container.dataset.reason = reason;
        container.dataset.status = "Rejected";

        // update UI
        container.querySelector(".status-text").innerText = "Rejected";

        // close modal
        bootstrap.Modal.getInstance(modalEl).hide();

        // call your existing function
        getBacklogRowData(event, option);
    } else if (action === "edit") {
        const reasonInput = document.getElementById("rejectionReason");
        const reason = reasonInput.value.trim();

        const data = {
            action: "updateRejectionReason",
            backlogI_id: modalEl.dataset.backlogI_id,
            rejection_reason: reason,
            last_updated_by: userId
        };

        const result = await sendBacklog(data);

        if (result.status === "Success") {
            console.log("reason Update Success");

        }

    }
});

//insert the option into the UI Variable to be fetch later
function applyStatusUI(container, newStatus) {
    console.log("applyStatusUI invoked");
    const pill = container.querySelector(".status-pill");
    const text = container.querySelector(".status-text");

    // 1. Update visible text
    text.textContent = newStatus;

    const noclick_refined = newStatus === "Refined" ? `no-click` : ``;
    // 2. Update class (VERY IMPORTANT for color/style)

    if (newStatus) {
        pill.classList.remove("status-pending", "status-approved", "status-rejected", "status-pending");
        pill.className = `status-pill status-${newStatus.toLowerCase()} ${noclick_refined}`;
    }
    // 3. Store value in dataset (THIS is your injected value)
    container.dataset.selectedStatus = newStatus;
}

// handled the UI drop doen for status Option 
function handleDropDownUI(e) {

    console.log(userRole);
    if (userRole === "Developer") {

        const row = e.target.closest("tr");
        console.log(row);
        backlogid = row.getAttribute("data-id");

        handleReasonModel(backlogid);
    } else if (userRole === "Product Owner") {
        // 1. Check if the click was inside a status container
        const isInsideContainer = e.target.closest('.status-container');

        // 2. If the click was OUTSIDE any status container... (to close the dropdown)
        if (!isInsideContainer) {
            // ...find ALL dropdowns on the page and hide them
            document.querySelectorAll('.status-dropdown').forEach(d => {
                d.classList.add('d-none');
            });
            return; // Stop here
        }

        const pill = e.target.closest(".status-pill");
        const statusOptions = e.target.closest(".statusOptions");
        // 3. If the user clicked the PILL, toggle that specific dropdown (Drop the dropdown)
        if (pill || statusOptions) {
            console.log(pill);
            const currentDropdown = isInsideContainer.querySelector('.status-dropdown');

            // Optional: Close other dropdowns before opening this one
            document.querySelectorAll('.status-dropdown').forEach(d => {
                if (d !== currentDropdown)
                    d.classList.add('d-none');
            });

            currentDropdown.classList.toggle('d-none');
        }
    }
}

document.getElementById("deleteModelBtn").addEventListener("click", () => {
    if (backlogid !== null) {
        console.log("ID IS NULL");
        executeBacklogDeletion(backlogid);
    }
});

async function executeBacklogDeletion(backlogid) {
    console.log("Backlog DELETION EXECUTED ");
    console.log("ID ", backlogid);
    const response = await fetch(`BacklogServlet?action=Delete&backlogId=${backlogid}`, {
        method: "GET"
    });

    if (!response.ok) {
        throw new Error("Server Return Error");
    }

    const result = await response.json();

    console.log("RESULT", result);
    if (result.status === "Success") {
        console.log("STATUS SUCCESS");

//            document.getElementById("initialBody").classList.add("d-none");
        removeTableRow(backlogid);

    }

    console.log("executeBacklogDeletion Trigered");
}
;

function removeTableRow(backlogid) {

    const idNum = Number(backlogid); // convert once

    // Iterate through DataTables rows properly
    table.rows().every(function () {

        const rowId = Number($(this.node()).attr('data-id'));
        //const rowId = $(this.node()).data('id');

        if (rowId === idNum) {
            this.remove();   // remove correct row
        }
    });

    table.draw(); // refresh table

    // update priority numbers
    $('.priority-rank').each(function (i) {
        $(this).text(i + 1);
    });

    deleteModal.hide(); // close modal correctly
}




function injectStatus(e) {

    const option = e.target.closest(".dropdown-item");


    const newStatus = option.dataset.value;
    console.log(newStatus);

    const container = option.closest(".status-container");
    const pill = container.querySelector(".status-pill");
    const text = container.querySelector(".status-text");

    // 1. Update visible text
    text.textContent = newStatus;

    // 2. Update class (VERY IMPORTANT for color/style)
    pill.className = `status-pill status-${newStatus.toLowerCase()}`;

    // 3. Store value in dataset (THIS is your injected value)
    container.dataset.selectedStatus = newStatus;

    // 4. Hide dropdown
    container.querySelector(".status-dropdown").classList.add("d-none");
}
;


async function populateViewDocTable() {

    document.getElementById("backlogFileRegistry").innerHTML = "";
    console.log(backlogDocModal);
    const backlogid = backlogDocModal.querySelector("#backlog_id").value;
    console.log(backlogid);
    //request to get the document to display
    const response = await fetch(`BacklogDocumentServlet?action=fetchDocument&backlogItem_id=${backlogid}`);
    const result = await response.json();
    console.log(result);

    if (!response.ok) {
        throw  new Error("Server error" + response.status);
    }

    result.documentData.forEach((item, index) => {
        console.log(item),
                console.log(index),
                appendDocument(item);
    });

}
;

let deleteModal;  // global variable
document.addEventListener("DOMContentLoaded", () => {
    deleteModal = new bootstrap.Modal(
            document.getElementById("deleteBacklogModal")
            );
});

document.getElementById("addNewBacklogBtn").addEventListener("click", () => {
    const addItemModal = new bootstrap.Modal(document.getElementById("addItemModal"));

    if (userRole !== "Developer") {
        document.getElementById("devBacklogField").classList.add("d-none");
    }

    hideAllErrorMsg_backlog();
    addItemModal.show();
});

const addItemForm = document.getElementById("addItemForm");

function hideAllErrorMsg_backlog() {

    const errorIds = [
        "errorBacklogTitle",
        "errorBacklogDescription",
        "errorAcceptanceCriteria",
        "errorStoryPoints",
        "errorMandays"
    ];

    errorIds.forEach(id => {
        document.getElementById(id).classList.add("d-none");
    });

}

document.getElementById("addItemForm").addEventListener("input", function (e) {

    const fieldMap = {
        backlog_title: "backlogTitleError",
        backlog_description: "backlogDescriptionError",
        backlog_ACriteria: "backlogCriteriaError",
        backlog_SPts: "storyPointsError",
        backlog_Mdys: "mandaysError"
    };

    const errorId = fieldMap[e.target.id];

    if (errorId) {
        document.getElementById(errorId)?.classList.add("d-none");
    }
});

function validateBacklogForm(data) {

    let isValid = true;

    const title = data.backlogI_title;
    const description = data.backlogI_desc;
    const criteria = data.acceptance_cri;
    const storyPoints = data.story_point;
    const mandays = data.mandays;

    document.querySelectorAll("#addItemForm .validation-message")
            .forEach(error => error.classList.add("d-none"));

    if (!title || title.trim() === "") {
        const error = document.getElementById("errorBacklogTitle");
        error.textContent = "Backlog title is required.";
        error.classList.remove("d-none");
        isValid = false;
    }

    if (!description || description.trim() === "") {
        const error = document.getElementById("errorBacklogDescription");
        error.textContent = "Description is required.";
        error.classList.remove("d-none");
        isValid = false;
    }

    if (!criteria || criteria.trim() === "") {
        const error = document.getElementById("errorAcceptanceCriteria");
        error.textContent = "Acceptance criteria is required.";
        error.classList.remove("d-none");
        isValid = false;
    }

    if (storyPoints !== undefined) {

        const sp = parseInt(storyPoints);

        if (isNaN(sp) || sp < 0 || sp > 10) {
            const error = document.getElementById("errorStoryPoints");
            error.textContent = "Story points must be between 0 and 10.";
            error.classList.remove("d-none");
            isValid = false;
        }
    }

    if (mandays !== undefined) {

        const md = parseInt(mandays);

        if (isNaN(md) || md < 0 || md > 10) {
            const error = document.getElementById("errorMandays");
            error.textContent = "Mandays must be between 0 and 10.";
            error.classList.remove("d-none");
            isValid = false;
        }
    }

    return isValid;
}

function showError(id, message) {
    const error = document.getElementById(id);
    error.textContent = message;
    error.classList.remove("d-none");
}

document.getElementById("addItemForm").addEventListener("input", function (e) {

    const errorMap = {
        backlog_title: "errorBacklogTitle",
        backlog_description: "errorBacklogDescription",
        backlog_ACriteria: "errorAcceptanceCriteria",
        backlog_SPts: "errorStoryPoints",
        backlog_Mdys: "errorMandays"
    };

    const errorId = errorMap[e.target.id];

    if (errorId) {
        document.getElementById(errorId)
                ?.classList.add("d-none");
    }
});

function getBacklogData(key) {
    console.log("getbacklogData");
    const data = {
        //get the vales form the backlog form
        action: "Create",
        project_id: projectId,
        backlogI_title: $('#backlog_title').val(),
        backlogI_desc: $('#backlog_description').val(),
        acceptance_cri: $('#backlog_ACriteria').val(),
        mandays: $('#backlog_Mdys').val(),
        story_point: $('#backlog_SPts').val(),
        created_by: `${userId}`
    };

    const isValid = validateBacklogForm(data);
    console.log(isValid);

    if (!isValid) {
        return false;
    }

    if (userRole === "Product Owner") {
        data.status = "Approved";
    } else if (userRole === "Developer") {
        data.status = "Pending";
    }

    lowestPriority = lowestPriority + 1;
    data.backlogI_priority = lowestPriority;


    if (key !== null) {
        data.backlogI_id = key;
    }

    return data;
}

async function handleAddBacklog() {

    console.log("handleAddBacklog 1");

//    try {

    const backlogData = getBacklogData();

    if (backlogData === false) {
        return;
    }
    console.log(backlogData);
    // Call Server API
    const result = await sendBacklog(backlogData);
    console.log("Server response:", result);


//    if (result.status === "Success") {
//        displayMessage(result.status, result.message);
////        backlogData.backlogI_id = result.key;
////        addbacklogToTable(backlogData);
////        displaySuccessProcessTab(result.status);
////    } else if (result.status === "Failed") {
////        displayFailedProcessTab(result.status);
//    }
}


function displayMessage(status, msg) {
    console.log("displayMessage");
    let alertClass = "alert-success";
    let icon = "fa-check-circle";
    if (status === "Failed") {
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
;
//
//function displaySuccessProcessTab(msg) {
//    const successProcessTabDOM = document.getElementById("successProcessTab");
//    document.getElementById("successProcessmsg").innerText = msg;
//    successProcessTabDOM.classList.remove("d-none");
//}
//
//function displayFailedProcessTab(msg) {
//    const failedProcessTabDOM = document.getElementById("failedProcessTab");
//    document.getElementById("failedProcessmsg").innerText = msg;
//    failedProcessTabDOM.classList.remove("d-none");
//}

//Send data to servlet to save
async function sendBacklog(data) {

    console.log("Data", data);

//        const contextPath = window.location.pathname.split("/")[1];
    const URL = "BacklogServlet";

    const response = await fetch(URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        throw new Error("Server returned error");
    }

    const result = await response.json();
    console.log(result.status, result.message );

    displayMessage(result.status, result.massage);
    
    return await result;


//    if (result.status === "Success") {
//        const key = result.key;
//        addbacklogToTable(getBacklogData(key));
//    } else {
//        console.log("Error");
//    }
}

function addbacklogToTable(data) {

    console.log("Data", data);
//    console.log("ROLE", userRole);

    const actionPermitted = (userRole === "Developer" && data.created_by === userId);

    const canDelete = userRole === "Product Owner";
    const canManageDoc = (userRole === "Product Owner") || (userRole === "Developer");

    const actionHtml = `<div class="d-flex justify-content-center align-items-center gap-2">
                            ${canManageDoc ? `<button type="button"  
                                        class="btn btn-sm btn-outline-primary shadow-sm btn-manageDoc">
                                        <i class="fas fa-file-alt"></i>
                                        </button>` : ``}
                            ${canDelete ? `<button type="button"
                                        class="btn btn-sm btn-outline-danger shadow-sm btn-delete"
                                        data-bs-id="${data.backlogI_id}"
                                        data-bs-title="${data.backlogI_title}">
                                        <i class="fas fa-trash-alt"></i>
                                   </button>` : ``}

                            ${!canDelete && !canManageDoc ? `<span class="upload-restricted">Restricted</span>` : ``}
                        </div>`;


    const dragAndDropSymbol = (userRole === "Product Owner") ?
            '<i class="fas fa-grip-vertical"></i>' : '';

    const statusValue = data.status || "Unknown";
    console.log(statusValue);
    const statusClass = statusValue.toLowerCase();

    const isPO = userRole === "Product Owner";
    const isDev = userRole === "Developer";

    const roleClass = userRole === "Product Owner" || (userRole === "Developer" && data.status === "Rejected") ? "" : "no-click";

// PO-only fields
    const editableClass = (isPO) ? "editable-cell po-editable" : "";
    const editableAttr = (isPO) ? 'contenteditable="true"' : "";
    const editableDropDown = isPO ?
            `<svg class="chevron" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="m6 9 6 6 6-6"/></svg>` : ``;

//Special permmision for developer to edit rejected row
    const rejectedEditableClass = (isDev && data.status === "Rejected") ? "editable-cell dev-editable" : "";
    const rejectedEditableAttr = (isDev && data.status === "Rejected") ? 'contenteditable="true"' : "";

// PO + Dev fields
    const editableClassSpe = (isPO || isDev) ? "editable-cell dev-editable" : "";
    const editableAttrSpe = (isPO || isDev) ? 'contenteditable="true"' : "";

    const mandays = (data.mandays === null || data.mandays === 0) ? "..." : data.mandays;
    const story_point = (data.story_point === null || data.story_point === 0) ? "..." : data.story_point;

    //To display the option to display and edit the reson for reject
    const reasonOption = data.status === "Rejected" ? `<div class="dropdown-item view-reason-action border-top mt-1 pt-2 text-primary fw-semibold" data-value="View_EditReason">
    ✏️ View / Edit Reason </div>` : ``;

    const newRow = table.row.add([
        dragAndDropSymbol,
        `<div  data-field="backlogI_priority">${data.backlogI_priority}</div>`,
        `<div  class=" editable-cell ${editableClass} ${rejectedEditableClass}" ${editableAttr} data-field="backlogI_title">${data.backlogI_title}</div>`,
        `<div  class=" editable-cell ${editableClass} ${rejectedEditableClass}" ${rejectedEditableAttr} ${editableAttr} data-field="backlogI_desc">${data.backlogI_desc}</div>`,
        `<div  class=" editable-cell ${editableClass} ${rejectedEditableClass}" ${rejectedEditableAttr} ${editableAttr} data-field="acceptance_cri">${data.acceptance_cri}</div>`,

        ` <div class="editable-cell ${roleClass} status-container" id="status" data-field="status" data-status="${data.status}"
                data-reason="${data.rejection_reason || ''}"">
            <div class="status-pill status-${statusClass}" role="button" aria-haspopup="true">
                <span class="status-dot"></span>
                <span class="status-text">${data.status}</span>
                ${editableDropDown}
            </div>
        
            <div class="status-dropdown d-none statusOptions ">
                <div class="dropdown-header">Change Status</div>
                <div class="dropdown-item" data-value="Stakeholder">Stakeholder</div>
                <div class="dropdown-item" data-value="Pending">Pending</div>
                <div class="dropdown-item" data-value="Approved">Approved</div>
                <div class="dropdown-item" data-value="Rejected">Rejected</div>
                ${reasonOption}
            </div>
        </div> `,

        `<div  class=" editable-cell ${editableClassSpe}" ${editableAttrSpe} data-field="mandays">${mandays}</div>`,
        `<div  class="editable-cell ${editableClassSpe}" ${editableAttrSpe}  data-field="story_point">${story_point}</div>`,
        actionHtml
    ]).draw(false).node();

    if (userRole === "Product Owner") {
        // Setup drag + priority
        $(newRow).find('td').eq(0).addClass('drag-handle');
    }
// Store Row id
    $(newRow).attr("data-id", data.backlogI_id);

    $(newRow).find('td').eq(1).addClass('priority-rank');
//    bootstrap.Modal.getInstance(document.getElementById('addItemModal')).hide();
    $('#addItemModal').modal('hide');
    $('#addItemForm')[0].reset();
}
//
//function showEmptyBacklogUI() {
//    // Replace this ID with the ID of the div wrapping your <table>
//    const tableContainer = document.getElementById('tableWrapperId');
//    let emptyStateDiv = document.getElementById('emptyBacklogState');
//
//    if (isEmpty) {
//        // Hide the table container
//        if (tableContainer)
//            tableContainer.style.display = 'none';
//
//        // Create the empty state UI if it doesn't exist yet
//        if (!emptyStateDiv) {
//            emptyStateDiv = document.createElement('div');
//            emptyStateDiv.id = 'emptyBacklogState';
//            emptyStateDiv.className = 'd-flex flex-column align-items-center justify-content-center py-5 my-5 bg-white rounded-4 shadow-sm';
//            emptyStateDiv.style.border = '2px dashed #dee2e6';
//
//            const isProductOwner = (userRole === "Product Owner");
//
//            emptyStateDiv.innerHTML = `
//                <div class="text-muted mb-3 mt-4">
//                    <i class="fas fa-list-ul fa-4x opacity-25"></i>
//                </div>
//                <h4 class="fw-bold text-secondary mb-2">No Backlog Items Yet</h4>
//                <p class="text-muted mb-4 text-center" style="max-width: 400px;">
//                    ${isProductOwner
//                    ? "Start building your project's foundation by adding user stories, tasks, or bugs."
//                    : "The product backlog is currently empty. Please wait for the Product Owner to add items."
//                    }
//                </p>
//                
//                ${isProductOwner ? `
//                    <button class="btn btn-primary px-4 py-2 mb-4 rounded-pill fw-bold shadow-sm" 
//                            data-bs-toggle="modal" 
//                            data-bs-target="#addItemModal">
//                        <i class="fas fa-plus me-2"></i> Add First Item
//                    </button>
//                ` : ``}
//            `;
//
//            // Insert the empty state right next to the table container
//            if (tableContainer && tableContainer.parentNode) {
//                tableContainer.parentNode.insertBefore(emptyStateDiv, tableContainer.nextSibling);
//            }
//        } else {
//            // If it already exists, just make sure it's visible
//            emptyStateDiv.style.display = 'flex';
//        }
//    } else {
//        // Not empty: show the table, hide the empty state
//        if (tableContainer)
//            tableContainer.style.display = 'block';
//        if (emptyStateDiv)
//            emptyStateDiv.style.display = 'none';
//    }
//}


function getExistingPriorities() {
    let priorities = [];

    table.rows().every(function () {
        const rowNode = this.node();
        const priorityText = $(rowNode).find('.priority-rank').text().trim();
        priorities.push(Number(priorityText));
    });

    return priorities;
}

$('#backlog_priority').on('input', function () {

    const enteredPriority = parseInt($(this).val());
    const existingPriorities = getExistingPriorities();
    const confirmAddBtn = document.getElementById("confirmAddBtn");

    // If empty or not number → reset state
    if (isNaN(enteredPriority)) {
        $(this).removeClass('is-invalid');
        $('#alertPriority').text('');
        if (confirmAddBtn)
            confirmAddBtn.disabled = false;
        return;
    }

    if (existingPriorities.includes(enteredPriority)) {

        $(this).addClass('is-invalid');
        $('#alertPriority').text("Priority already exists.");

        if (confirmAddBtn)
            confirmAddBtn.disabled = true;

    } else {

        $(this).removeClass('is-invalid');
        $('#alertPriority').text('');

        if (confirmAddBtn)
            confirmAddBtn.disabled = false;
    }

});

////////////////////////////////////////////////////////////////////////// Document ///////////////////////////////////////////////////////////////////////////////



let deleteDocModal;
document.getElementById("backlogDocModal").addEventListener("click", async function (e) {
    const viewNavBtn = e.target.closest("#viewNavBtn");
    const uploadNavBtn = e.target.closest("#uploadNavBtn");
    const confirmDocBtn = e.target.closest("#confirmDocBtn");
    const link = e.target.closest(".doc-edit-link");
    const viewDocBtn = e.target.closest(".docViewBtn");
    const deleteDocBtn = e.target.closest(".docDeleteBtn");
    const downloadDocBtn = e.target.closest(".docDownloadBtn");

    if (uploadNavBtn) {

        console.log("uploadPane clicl");
        fileDropHandler();
        backlogDocModal.querySelector("#confirmDocBtn").classList.remove("d-none");

    } else if (viewNavBtn) {

        console.log("viewPane clicl");
        backlogDocModal.querySelector("#confirmDocBtn").classList.add("d-none");

    } else if (confirmDocBtn) {
        console.log("confirm btn is clciked");
        handleModelUpload();
    } else if (link) {
        const row = e.target.closest("tr");
        console.log(row);
        prepareEditForm(row.id);

    } else if (viewDocBtn) {
        const row = e.target.closest("tr");
        window.open(`BacklogDocumentServlet?action=fetchDocument_view&document_id=${row.id}`);
    } else if (downloadDocBtn) {
        const row = e.target.closest("tr");
        window.open(`BacklogDocumentServlet?action=downloadDocument&document_id=${row.id}`);
    } else if (deleteDocBtn) {
        deleteDocModal = document.getElementById("deleteDocModal");
        console.log(deleteDocModal);

        const row = e.target.closest("tr");
        const docId = row.id;
        const docName = row.name;

        deleteDocModal.querySelector("#documentNameDel").innerText = docName;
        deleteDocModal.dataset.id = docId;

        const deleteDocModelBoot = new bootstrap.Modal(deleteDocModal);
        deleteDocModelBoot.show();

    }

});

document.getElementById("deletedocBtnCfm").addEventListener("click", async function () {
    console.log("delete confirm clicked");
    const document_id = deleteDocModal.dataset.id;

    const formData = new FormData();
    formData.append("action", "delete");
    formData.append("document_id", document_id);

    const result = await sendData_Document(formData);

    if (result.status === "Failed") {
        console.log("Server response : ", result.status);
    }

    const deleteDocModelBoot = bootstrap.Modal.getInstance(deleteDocModal);
    deleteDocModelBoot.hide();


    populateViewDocTable();
    viewDocPane = document.querySelector(`[data-bs-target="#viewPane"]`);
    const viewDocTab = new bootstrap.Tab(viewDocPane);
    console.log(viewDocPane);
    viewDocTab.show();
});

document.getElementById("searchDoc").addEventListener("input", filterBacklogDocuments);
document.getElementById("filterType").addEventListener("change", filterBacklogDocuments);

function filterBacklogDocuments() {
    const searchValue = document.getElementById("searchDoc").value.toLowerCase();
    const selectedType = document.getElementById("filterType").value;

    const rows = document.querySelectorAll("#backlogFileRegistry tr");

    rows.forEach(row => {
        const name = row.children[0].innerText.toLowerCase();
        const type = row.children[1].innerText.trim();

        const matchName = name.includes(searchValue);
        const matchType = selectedType === "" || type.includes(selectedType);

        if (matchName && matchType) {
            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}


async function prepareEditForm(document_id) {
    isEdit = true;
    const response = await fetch(`BacklogDocumentServlet?action=fetchDocumentData&document_id=${document_id}`);
    const result = await response.json();

    console.log(result);

    document.getElementById("document_id").value = result.documentData.document_id;
    document.getElementById("docLabel").value = result.documentData.document_name;
    document.getElementById("docType").value = result.documentData.document_type;

    const file = {
        name: result.documentData.document_name
    };

    // initialise global variable for dispalyFile function
    fileNameDisplay = document.getElementById('selectedFileName');
    fileInfo = document.getElementById('fileInfo');
    displayFile(file);

    backlogDocModal.querySelector("#confirmDocBtn").classList.remove("d-none");

    uploadDocPane = document.querySelector('[data-bs-target="#uploadPane"]');
    uploadDocPanBoot = bootstrap.Tab.getOrCreateInstance(uploadDocPane);
    uploadDocPanBoot.show();
}
let fileNameDisplay;
let fileInfo;

function fileDropHandler() {
    console.log("file Drop Handler");
    const dropZone = document.getElementById('dropZone');
    console.log(dropZone);
    const fileInput = document.getElementById('actualFile');
    console.log(fileInput);
    fileInfo = document.getElementById('fileInfo');
    console.log(fileInfo);
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
}

//let backlogDocModel;
//async function prepareUploadModel(backlog_Id){
//    console.log("manage btn clicked");
//    const row = e.target.closest("tr");
//    console.log(row);
//    const backlog_id = row.dataset.id;
//    
//    
//    console.log(backlogDocModal);
//});



// display the pill of the document attached in the field
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

async function handleModelUpload() {
    console.log("handleModelUpload is reached");
    let action = "insert";

    const formData = new FormData();

    const actualFile = document.getElementById("actualFile").files[0] || null;
    console.log(backlogDocModal);

    if (isEdit) {
        action = "update";
        console.log(uploadDocPane);
        formData.append("document_id", document.getElementById("document_id").value);
    } else {
        const backlog_id = backlogDocModal.querySelector("#backlog_id").value;
        formData.append("backlog_item_id", backlog_id);
    }

    formData.append("action", action);

//    formData.append("project_id", projectId);
    formData.append("document_name", document.getElementById("docLabel").value);
    formData.append("document_type", document.getElementById("docType").value);
    formData.append("documentContent", actualFile);

    console.log(formData.get("action"));
    const result = await sendData_Document(formData);
    console.log(result.document_id);
    if (result.status === "Failed") {
        console.log("Failed");
    }

    if (isEdit) {
        populateViewDocTable();
    } else {
        formData.append("document_id", result.document_id);
        appendDocument(formData);
    }

    viewDocPane = document.querySelector(`[data-bs-target="#viewPane"]`);
    const viewDocTab = new bootstrap.Tab(viewDocPane);
    console.log(viewDocPane);
    viewDocTab.show();
}

async function sendData_Document(formData) {

    console.log("sendData_Document ", formData.get("action"));
    const response = await fetch("BacklogDocumentServlet", {
        method: "POST",
        body: formData
    });

    if (!response.ok) {
        throw  new Error("Server error" + response.status);
    }

    const result = await response.json();
    return  result;
}

function appendDocument(data) {

    document_name = data instanceof FormData ? data.get("document_name") : data.document_name;
    console.log(document_name);

    document_type = data instanceof FormData ? data.get("document_type") : data.document_type;
    console.log(document_type);

    const docDiv = document.getElementById("backlogFileRegistry");
    const row = document.createElement("tr");
    row.id = data instanceof FormData ? data.get("document_id") : data.document_id;
    row.name = document_name;
    const actionDelete = userRole === "Product Owner" ? `<button class="btn btn-sm btn-light border p-1 px-2 ms-1 docDeleteBtn">
                                                        <i class="fas fa-trash-alt text-danger"></i>
                                                    </button>` : ``;
    row.innerHTML = `
                        
                            <td>
                                <a href="#" class="fw-bold text-decoration-none doc-edit-link">
                                    ${document_name}
                                </a>
                            </td>
                            <td><span class="badge bg-secondary-subtle text-secondary border">${document_type}</span></td>

                            <td class="text-center pe-4">
                                                    <button class="btn btn-sm btn-light border p-1 px-2 docViewBtn">
                                                        <i class="fas fa-eye text-muted"></i>
                                                    </button>
                                                    <button class="btn btn-sm btn-light border p-1 px-2 docDownloadBtn">
                                                        <i class="fas fa-download text-muted "></i>
                                                    </button>
                                                    ${actionDelete}
                            </td>
                        `;
    docDiv.append(row);
    return;
}