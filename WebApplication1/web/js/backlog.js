///* 
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

    const response = await fetch(`BacklogServlet?project_id=${projectId}&action=fetchData`);
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

    let oldValue = "";

    //focusin work for contenteditable element
    table.addEventListener("focusin", (e) => {

        //react is the focused element has class "editable-cell"
        if (!e.target.classList.contains("editable-cell"))
            return;

        oldValue = e.target.textContent.trim();

    });

    //detect when the user leave the cell editable(editing finished)
    table.addEventListener("focusout", async (e) => {

        //Ignore the element is not editable-cell
        if (!e.target.classList.contains("editable-cell"))
            return;

        //Get the new value after editing
        const newValue = e.target.textContent.trim();

        if (newValue === oldValue)
            return;

        const row = e.target.closest("tr");
        const backlogId = row.dataset.id;

        const cells = row.querySelectorAll(".editable-cell");

        const rowData = {backlogI_id: backlogId};
        console.log(rowData);

        cells.forEach(cell => {
            const field = cell.dataset.field;
            const value = cell.textContent.trim();
            rowData[field] = value;
        });
        rowData.action = "Update";

        //console.log(rowData);

        try {

            //call cervlet to update the field
            await updateBacklogRow(rowData);

//            console.log("Row updated", rowData);

//            setTimeout(() => {
//                e.target.classList.remove("saved");
//            }, 800);
        } catch (err) {
            console.error("Update Failed:", err);
            alert("Failed to save change");
        }
    });
});

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

//    if(result.status !== "success"){
//        throw new Error("Update failed");
//    }

    return result;
}
;

$(document).ready(function () {

    table = $('#backlogTable').DataTable({
        order: [[1, 'asc']],
        paging: false, info: false, searching: true,
        columnDefs: [{targets: 'no-sort', orderable: false}]

    });

    if (userRole !== "Product Owner") {
//        table.column(0).visible(false); // drag column
//        table.column(7).visible(false); // action column
        table.column('.action-col').visible(false);
    }

    const sidebar = document.getElementById('sidebar');
    // 1. Sidebar Toggle
    function toggleSidebar() {
        sidebar.classList.toggle('collapsed');
        setTimeout(() => {
            table.columns.adjust().draw();
        }, 400);
    }

    sidebar.addEventListener('dblclick', toggleSidebar);
    $('#sidebarToggle').on('click', toggleSidebar);

//    Sortable.create(document.getElementById('sortableBody'), {
//        handle: '.drag-handle',
//        animation: 200,
//
//        onEnd: function (evt) {
//
//            const movedRow = $(evt.item);
//
//            // Priority of dragged row
//            const movedPriority = Number(
//                    movedRow.find('.priority-rank').text()
//                    );
//
//            // Start from the row BELOW the dragged row
//            let nextPriority = movedPriority + 1;
//
//            movedRow.nextAll('tr').each(function () {
//                $(this).find('.priority-rank').text(nextPriority++);
//            });
//
//            // Sync DataTables
//            table.rows().invalidate().draw(false);
//        }
//    });

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


    // START INSET ADD listener to the form submit button
    document.getElementById('confirmAddBtn').addEventListener('click', handleAddBacklog);
//
//                console.log("handleAddBacklog 1");
//
//                try {
//                    // Call Server API
//                    const result = await sendBacklog(getBacklogData());
//                    console.log("Server response:", result);
//                } catch (err) {
//                    console.error("Save Failed", err);
//                    alert("Failed to save backlog");
//                }
//            });

//    // 5. Add New Row (Logic preserved and optimized)
//    $('#confirmAddBtn').on('click', function () {
//
//
////        const rank = $('#sortableBody tr').length + 1;
//    });

    let backlogid = null;

    document.getElementById("backlogTable").addEventListener("click", async function (e) {

        const deletebtn = e.target.closest(".btn-delete");
        const manageDocbtn = e.target.closest(".btn-manageDoc");

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

//            //request to get the document to display
//            const response = await fetch(`BacklogDocumentServlet?action=fetchDocument&backlogItem_id=${backlogid}`);
//            const result = await response.json();
//            console.log(result);
//
//            if (!response.ok) {
//                throw  new Error("Server error" + response.status);
//            }
//
//            result.documentData.forEach((item, index) => {
//                console.log(item),
//                        console.log(index),
//                        appendDocument(item);
//            });

            populateViewDocTable();
            backlogDocModalBoot = new bootstrap.Modal(backlogDocModal);
            backlogDocModal.querySelector("#backlog_id").value = backlogid;
            backlogDocModalBoot.show();
        }
        if (deletebtn) {
            backlogid = deletebtn.getAttribute("data-bs-id");
            const title = deletebtn.getAttribute("data-bs-title");

            document.getElementById("backlogTitle").textContent = title;
            deleteModal.show();
        }
        //new bootstrap.Modal(document.getElementById("deleteBacklogModal")).show();
    });

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


});


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

function getBacklogData(key) {
    const data = {
        //get the vales form the backlog form
        action: "Create",
        project_id: projectId,
        backlogI_title: $('#backlog_title').val(),
        backlogI_desc: $('#backlog_description').val(),
        acceptance_cri: $('#backlog_ACriteria').val(),
        mandays: $('#backlog_Mdys').val(),
        story_point: $('#backlog_SPts').val()
    };

    lowestPriority = lowestPriority + 1;
    data.backlogI_priority = lowestPriority;


    if (key !== null) {
        data.backlogI_id = key;
    }

    return data;
}

async function handleAddBacklog() {

    console.log("handleAddBacklog 1");

    try {

        const backlogData = getBacklogData();

        // Call Server API
        const result = await sendBacklog(backlogData);
        console.log("Server response:", result);


        if (result.status === "Success") {
            backlogData.backlogI_id = result.key;
            addbacklogToTable(backlogData);
        }

    } catch (err) {
        console.error("Save Failed", err);
        alert("Failed to save backlog");
    }
}

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

    return await response.json();


//    if (result.status === "Success") {
//        const key = result.key;
//        addbacklogToTable(getBacklogData(key));
//    } else {
//        console.log("Error");
//    }
}

function addbacklogToTable(data) {

//    console.log("Data", data);
//    console.log("ROLE", userRole);

    const actionHtml = (userRole === "Product Owner") ? `
    <div class="d-flex justify-content-center align-items-center gap-2">
        <button type="button"  
            class="btn btn-sm btn-outline-primary shadow-sm btn-manageDoc">
            <i class="fas fa-file-alt"></i>
        </button>

        <button type="button"
            class="btn btn-sm btn-outline-danger shadow-sm btn-delete"
            data-bs-id="${data.backlogI_id}"
            data-bs-title="${data.backlogI_title}">
            <i class="fas fa-trash-alt"></i>
        </button>
    </div>
    ` : '';

    const dragAndDropSymbol = (userRole === "Product Owner") ?
            '<i class="fas fa-grip-vertical"></i>' : '';

    const editableClass = (userRole === "Product Owner") ? "editable-cell" : "";
    const editableAttr = (userRole === "Product Owner") ? 'contenteditable="true"' : "";

    const newRow = table.row.add([
        dragAndDropSymbol,
        `<div  data-field="backlogI_priority">${data.backlogI_priority}</div>`,
        `<div  class="${editableClass}" ${editableAttr} data-field="backlogI_title">${data.backlogI_title}</div>`,
        `<div  class="${editableClass}" ${editableAttr} data-field="backlogI_desc">${data.backlogI_desc}</div>`,
        `<div  class="${editableClass}" ${editableAttr} data-field="acceptance_cri">${data.acceptance_cri}</div>`,
        `<div  class="${editableClass}" ${editableAttr} data-field="mandays">${data.mandays}</div>`,
        `<div  class="${editableClass}" ${editableAttr}  data-field="story_point">${data.story_point}</div>`,
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

function getExistingPriorities() {
    let priorities = [];

    table.rows().every(function () {
        const rowNode = this.node();
        const priorityText = $(rowNode).find('.priority-rank').text().trim();
        priorities.push(Number(priorityText));
    });

    return priorities;
}


//async function handleAddBacklog() {
//
//    const enteredPriority = Number($('#backlog_priority').val());
//
//    if (!enteredPriority) {
//        alert("Priority is required");
//        return;
//    }
//
//    const existingPriorities = getExistingPriorities();
//
//    if (existingPriorities.includes(enteredPriority)) {
//        document.getElementById("alertPriority").innerHTML("Priority already exists. Please choose another.");
//        alert("Priority already exists. Please choose another.");
//        return;
//    }
//
//    try {
//        const result = await sendBacklog(getBacklogData());
//        console.log("Server response:", result);
//    } catch (err) {
//        console.error("Save Failed", err);
//        alert("Failed to save backlog");
//    }
//}
//
//
//$('#backlog_priority').on('input', function () {
//
//    const enteredPriority = parseInt($(this).val());
//    const existingPriorities = getExistingPriorities();
//    var formSubbtn = document.getElementById("formSubbtn");
// 
//    if (existingPriorities.includes(enteredPriority)) {
//
////        $('#alertPriority').text("Priority already exists. Please choose another.");
//        $(this).addClass('is-invalid');
//        $('#alertPriority').text("Priority already exists.");
//        formSubbtn.disabled = true;
////        alert("Priority already exists.");
//
//    } else {
//        document.getElementById("alertPriority").innerHTML = "";
//        formSubbtn.disabled = false;
//    }
//
//});

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
    //console.log(data.document_id);
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
                                                    <button class="btn btn-sm btn-light border p-1 px-2 ms-1 docDeleteBtn">
                                                        <i class="fas fa-trash-alt text-danger"></i>
                                                    </button>
                            </td>
                        `;
    docDiv.append(row);
    return;
}