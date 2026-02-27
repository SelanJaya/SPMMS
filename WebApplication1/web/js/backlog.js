///* 
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
// */


/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

// fetch the backlog data from the db after the backlog page is displayed
document.addEventListener("DOMContentLoaded", async function () {

    const response = await fetch(`BacklogServlet?project_id=${projectId}&action=fetchData`);
    const result = await response.json();

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

    async function handleAddBacklog() {

        console.log("handleAddBacklog 1");

        try {
            // Call Server API
            const result = await sendBacklog(getBacklogData());
            console.log("Server response:", result);
        } catch (err) {
            console.error("Save Failed", err);
            alert("Failed to save backlog");
        }
    }

//    // 5. Add New Row (Logic preserved and optimized)
//    $('#confirmAddBtn').on('click', function () {
//
//
////        const rank = $('#sortableBody tr').length + 1;
//    });

    let backlogid = null;
    document.getElementById("backlogTable").addEventListener("click", function (e) {


        const button = e.target.closest(".btn-delete");
        console.log("Button ", button);
        if (!button)
            return;

        backlogid = button.getAttribute("data-bs-id");
        const title = button.getAttribute("data-bs-title");

        document.getElementById("backlogTitle").textContent = title;
        deleteModal.show();
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

            const rowId = $(this.node()).data('id');

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
        backlogI_priority: $('#backlog_priority').val(),
        backlogI_title: $('#backlog_title').val(),
        backlogI_desc: $('#backlog_description').val(),
        acceptance_cri: $('#backlog_ACriteria').val(),
        mandays: $('#backlog_Mdys').val(),
        story_point: $('#backlog_SPts').val()
    };

    if (key !== null) {
        data.backlogI_id = key;
    }

    return data;
}

async function handleAddBacklog() {

    console.log("handleAddBacklog 1");

    try {
        // Call Server API
        const result = await sendBacklog(getBacklogData());
        console.log("Server response:", result);
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

    const result = await response.json();


    if (result.status === "Success") {
        const key = result.key;
        addbacklogToTable(getBacklogData(key));
    } else {
        console.log("Error");
    }
}

function addbacklogToTable(data) {

    console.log("Data", data);
    console.log("ROLE", userRole);

    const actionHtml = (userRole === "Product Owner") ? `
<div class="d-flex justify-content-center align-items-center gap-2">
    <button type="button"
        class="btn btn-sm btn-outline-primary shadow-sm"
        data-bs-toggle="modal"
        data-bs-target="#backlogDocModal">
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
        if (confirmAddBtn) confirmAddBtn.disabled = false;
        return;
    }

    if (existingPriorities.includes(enteredPriority)) {

        $(this).addClass('is-invalid');
        $('#alertPriority').text("Priority already exists.");

        if (confirmAddBtn) confirmAddBtn.disabled = true;

    } else {

        $(this).removeClass('is-invalid');
        $('#alertPriority').text('');

        if (confirmAddBtn) confirmAddBtn.disabled = false;
    }

});


