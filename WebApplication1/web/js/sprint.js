/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
let pendingBacklogIds = null;
let sprint_id = null;

let newAssignments = null;
let removedAssignments = null;
document.addEventListener("DOMContentLoaded", function () {

    if (typeof project_id !== "undefined") {
        console.log("Project id:", project_id);
    } else {
        console.warn("project_id is not defined");
    }

    if (typeof initPage === "function") {
        initPage();
    }
});
// JS logic for Search, Drag&Drop, Edit, and Save (remains identical to your established logic)
let isEditMode = false;
const bsModal = new bootstrap.Modal(document.getElementById('sprintModal'));
function openModal() {
    isEditMode = false;
    document.getElementById('modalTitle').innerText = "Initialize Scrum Board";
    document.getElementById("SprintSubBtn").innerText = "Create Sprint";
    hideAllErrorMsg();
    document.querySelectorAll('#sprintModal input, #sprintModal textarea').forEach(el => el.value = '');
    bsModal.show();
}



//Search Logic
document.getElementById('boardSearch').addEventListener('input', function (e) {
    const term = e.target.value.toLowerCase();
    document.querySelectorAll('.scrum-board-card').forEach(board => {
        const text = board.innerText.toLowerCase();
        board.classList.toggle('hidden-board', !text.includes(term));
    });
});
function editBoard(boardId) {
    console.log("Board ID", boardId);
    isEditMode = true;
    const board = document.getElementById(boardId);
    console.log("Board :", board);
    // load options first
//    await loadBacklogOptions(board._backlogData);

    const backlogIds =
            board.dataset.backlogs
            ? board.dataset.backlogs.split(',')
            : [];
    console.log("BacklogIDS Edit B:", backlogIds);
    sprint_id = boardId;
    document.getElementById('editBoardId').value = boardId;
    document.getElementById('modalTitle').innerText = "Edit Project Details";
    document.getElementById('SprintSubBtn').innerText = "Edit Sprint";
    document.getElementById('s_name').value = board.querySelector('.val-name').innerText;
    document.getElementById('s_goal').value = board.querySelector('.val-goal').innerText;
    document.getElementById('s_start').value = board.querySelector('.val-start').innerText;
    document.getElementById('s_end').value = board.querySelector('.val-end').innerText;
//    document.getElementById('s_status').value = board.querySelector('.val-status').innerText;
    document.getElementById('s_review').value = board.querySelector('.val-review').innerText;
    document.getElementById('s_retro').value = board.querySelector('.val-retro').innerText;
    pendingBacklogIds = backlogIds;
    //console.log("Pending IDs:", pendingBacklogIds);

    bsModal.show();
}

document.addEventListener('show.bs.modal', () => {
    document.body.style.overflow = 'auto';
});
document.addEventListener('hidden.bs.modal', () => {
    document.body.style.overflow = 'auto';
});

document.getElementById("scrum-container"), addEventListener("click", (e) => {
    const deleteSprintBtn = e.target.closest(".deleteSprint-btn");
    const editSprintBtn = e.target.closest(".editSprint-btn");
    const backlogPill = e.target.closest(".backlog-chip");
    const verifySprintEnd = e.target.closest(".verifySprintEnd-btn");
    if (editSprintBtn) {
        console.log("editbtn is clicked");
        sprint_div = e.target.closest(".scrum-board-card");
        sprint_id = sprint_div.id;
        editBoard(sprint_id);
    } else if (deleteSprintBtn) {
        console.log("deletebtn is clicked");
        const deleteSprintModalDOM = document.getElementById("deleteSprintModal");
        const deleteSprintModal = bootstrap.Modal.getOrCreateInstance(deleteSprintModalDOM);

        sprint_div = e.target.closest(".scrum-board-card");
        deleteSprintModalDOM.dataset.sprintId = sprint_div.id;
//        deleteBoard(sprint_id);

        deleteSprintModal.show();
    } else if (backlogPill) {
        const backlog_id = backlogPill.dataset.id;
        backlogModal = document.getElementById("backlogDocModal");
        backlogModal.dataset.backlogId = backlog_id;
        backlogModalBoot = new bootstrap.Modal(backlogModal);
        backlogModalBoot.show();
        displayBacklogDetails(backlog_id);
    } else if (verifySprintEnd) {
        console.log("verifySprintEnd clicked");
        sprint_div = e.target.closest(".scrum-board-card");
        sprint_id = sprint_div.id;
        console.log(sprint_id);
        const sprintName = e.target.closest(".scrum-board-card").querySelector(".val-name").textContent;
        populateSprintReview(sprint_id, sprintName);
    }
});

const sprintModalEl = document.getElementById('sprintModal');
sprintModalEl.addEventListener("shown.bs.modal", async function () {

    document.body.style.paddingRight = "0px";
    const backlogSelect = $('#s_backlog_links');
    let mode = "Create";
    let sprint_Id = "";
    console.log("Edit Mode", isEditMode);
    if (isEditMode) {
        mode = "Edit";
        sprint_Id = sprint_id;
    }

    try {
        const response = await fetch(`SprintServlet?action=fetchBacklog&project_id=${project_id}&mode=${mode}&sprint_id=${sprint_Id}`);
        const result = await response.json();
        console.log(result);
        /* ✅ destroy old Select2 if exists */
        if (backlogSelect.hasClass("select2-hidden-accessible")) {
            backlogSelect.select2('destroy');
        }

        /* ✅ clear old options */
        backlogSelect.empty();
        result.backlogData.forEach(p => {
            console.log(p);
            const option = new Option(p.backlogI_title, String(p.backlogI_id));
            backlogSelect.append(option);
        });
        /* ✅ initialize once cleanly */
        backlogSelect.select2({
            placeholder: "Select backlog items",
            width: '100%',
            dropdownParent: $('#sprintModal')
        });
        if (pendingBacklogIds) {
            pendingBacklogIds = pendingBacklogIds.map(id => String(id));
            backlogSelect
                    .val(pendingBacklogIds)
                    .trigger('change');
            pendingBacklogIds = null;
        }

        /* ✅ reveal after stable render */
        backlogSelect.css("visibility", "visible");
    } catch (err) {
        console.error("Fetch Failed:", err);
        alert("Failed to load backlog");
    }

    sprint_id = null;
});
$('#s_backlog_links').on('select2:unselect', function (e) {

    const removedId = e.params.data.id;
    const removedText = e.params.data.text;
    console.log("Removed backlog ID:", removedId);
    console.log("Removed title:", removedText);
});
//Common function to send custom data to servlet
async function sendData_Sprint(data) {
    let method;
    console.log("sendData triggered");
    const response = await fetch("SprintServlet", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });
    if (!response.ok) {
        throw new Error("Server error" + response.status);
    }

    const result = await response.json();
    if (result.status === "Success") {
        displaySuccessProcessTab(result.message);
    } else if (result.status !== "Failed") {
        console.log("Backlog Retrive Failed");
        displayFailedProcessTab(result.message);
    }
    return result;
}

function displaySuccessProcessTab(msg) {
    console.log(msg);
    const successProcessTabDOM = document.getElementById("successProcessTab");
    console.log(successProcessTabDOM);
    document.getElementById("successProcessmsg").innerText = msg;
    successProcessTabDOM.classList.remove("d-none");
}

function displayFailedProcessTab(msg) {
    const failedProcessTabDOM = document.getElementById("failedProcessTab");
    document.getElementById("failedProcessmsg").innerText = msg;
    failedProcessTabDOM.classList.remove("d-none");
}

//Fetch the backlog data when the page loaded
document.addEventListener("DOMContentLoaded",
        async function getBoardData() {

            const response = await fetch(`SprintServlet?action=fetchSprint&project_id=${project_id}`);
            const result = await response.json();
            console.log(result);
            if (result.status === "Success") {
                try {
//                    result.SprintData.forEach(item => {
//                        renderBoard(item);
//                    });

                    if (!result.SprintData || result.SprintData.length === 0) {

                        // --> IT IS EMPTY: Do your empty state logic or send your keyword here
                        console.log("No sprints found. Triggering empty state.");
                        // Example: Trigger the empty UI function we discussed earlier
                        showEmptySprintUI();
                    } else {

                        // --> IT HAS DATA: Loop through and render the boards
                        result.SprintData.forEach(item => {
                            renderBoard(item);
                        });
                    }
                } catch (err) {
                    console.error("Update Failed:", err);
                    alert("Failed to save change");
                }
            } else {
                console.log(response.status);
            }
        });
//validate the sprint form 
function validateSprintForm(data) {

    let isValid = true;
    const sprintName = data.sprint_name.trim();
    const sprintGoal = data.sprint_goal.trim();
    const startDate = data.sprint_start_date;
    const endDate = data.sprint_end_date;
    const selectedBacklogs = data.backlog_item_id;
    document.getElementById("errorSprintName").innerText = "";
    document.getElementById("errorSprintGoal").innerText = "";
    document.getElementById("errorSprintStart").innerText = "";
    document.getElementById("errorSprintEnd").innerText = "";
    document.getElementById("errorSprintDateRange").innerText = "";
    document.getElementById("errorSprintBacklog").innerText = "";
    if (sprintName === "") {
        const errorSprintName = document.getElementById("errorSprintName");
        errorSprintName.innerText = "Sprint name is required.";
        errorSprintName.classList.remove("d-none");
        isValid = false;
    }

    if (sprintGoal === "") {
        const errorSprintGoal = document.getElementById("errorSprintGoal");
        errorSprintGoal.innerText = "Sprint goal is required.";
        errorSprintGoal.classList.remove("d-none");
        isValid = false;
    }

    if (startDate === "") {
        const errorSprintStart = document.getElementById("errorSprintStart");
        errorSprintStart.innerText = "Start date is required.";
        errorSprintStart.classList.remove("d-none");
        isValid = false;
    }

    if (endDate === "") {
        const errorSprintEnd = document.getElementById("errorSprintEnd");
        errorSprintEnd.innerText = "End date is required.";
        errorSprintEnd.classList.remove("d-none");
        isValid = false;
    }

    if (startDate && endDate) {
        if (new Date(endDate) < new Date(startDate)) {
            const errorSprintDateRange = document.getElementById("errorSprintDateRange");
            errorSprintDateRange.innerText = "End date must be later than or equal to Start date.";
            errorSprintDateRange.classList.remove("d-none");
            isValid = false;
        }
    }

    if (!selectedBacklogs || selectedBacklogs.length === 0) {
        const errorSprintBacklog = document.getElementById("errorSprintBacklog");
        errorSprintBacklog.innerText = "Please select at least one backlog item.";
        errorSprintBacklog.classList.remove("d-none");
        isValid = false;
    }

    return isValid;
}

document.getElementById("sprintModal").addEventListener("click", function (e) {

    const mapping = {
        "s_name": "errorSprintName",
        "s_goal": "errorSprintGoal",
        "s_start": "errorSprintStart",
        "s_end": "errorSprintEnd",
        "s_backlog_links": "errorSprintBacklog"
    };
    const field = e.target.id;
    if (mapping[field]) {
        document.getElementById(mapping[field])
                .classList.add("d-none");
    }

    const sprintSubBtn = e.target.closest("#SprintSubBtn");
    if (sprintSubBtn) {
        saveBoard();
    }

});

//hdie when the page load
function hideAllErrorMsg() {

    const errorField = [
        "errorSprintName",
        "errorSprintGoal",
        "errorSprintStart",
        "errorSprintEnd",
        "errorSprintBacklog"
    ];
    errorField.forEach(item => {
        document.getElementById(item).classList.add("d-none");
    });
}

//Function to create the Scrum board
async function saveBoard() {
    let sprint_id = null;
    const data = {
        action: "Insert",
        project_id: project_id,
        sprint_name: document.getElementById('s_name').value,
        sprint_status: "Active",
        sprint_goal: document.getElementById('s_goal').value,
        sprint_start_date: document.getElementById('s_start').value,
        sprint_end_date: document.getElementById('s_end').value,
        review_notes: document.getElementById('s_review').value,
        restrospective_notes: document.getElementById('s_retro').value,
        // in JQuery 
        backlog_item_id: $('#s_backlog_links').val().map(Number)
    };
    console.log(data);
    if (isEditMode) {
        sprint_id = document.getElementById('editBoardId').value;
        data.action = "Update";
        data.sprint_id = sprint_id;
    }

    let isValid = validateSprintForm(data);
    if (!isValid) {
        return;
    }


//    console.log("Data ", data);

    const result = await sendData_Sprint(data);
    data.sprint_id = result.sprint_id;
    delete data.backlogI_id;
    data.backlog = result.backlogData || [];
    console.log("Data.backlog : ", data.backlog);
    console.log("BACKLOG DATA RETRiEVE :" + result.backlogData);
    if (isEditMode) {
        console.log("Status ", isEditMode);
        const b = document.getElementById(sprint_id);
        b.querySelector('.val-name').innerText = data.sprint_name;
        b.querySelector('.val-goal').innerText = data.sprint_goal;
        b.querySelector('.val-start').innerText = data.sprint_start_date;
        b.querySelector('.val-end').innerText = data.sprint_end_date;
        b.querySelector('.val-status').innerText = data.sprint_status;
        b.querySelector('.val-review').innerText = data.review_notes;
        b.querySelector('.val-retro').innerText = data.restrospective_notes;
        // 🔥 UPDATE BACKLOG LIST
        const backlogContainer = b.querySelector('.backlog-list');
        console.log(backlogContainer);
        backlogContainer.innerHTML = "";
        data.backlog.forEach(item => {
            const chip = document.createElement("span");
            chip.className = "backlog-chip";
            chip.innerHTML = `
        ${item.backlogI_title}
        <span class="badge bg-secondary ms-2">
            ${item.story_point} SP
        </span>
    `;
            backlogContainer.appendChild(chip);
        });
        // ✅ Update dataset so next edit uses correct backlog
        const backlogIds = (data.backlog && data.backlog.length > 0)
                ? data.backlog.map(b => b.backlogI_id).join(',')
                : '';
        b.dataset.backlogs = backlogIds;
    } else {
        renderBoard(data);
    }
    bsModal.hide();
}
;


document.getElementById("deleteSprintBtn").addEventListener("click", async function deleteBoard() {

    const deleteSprintModalDOM = document.getElementById("deleteSprintModal");
    const deleteSprintModal = bootstrap.Modal.getOrCreateInstance(deleteSprintModalDOM);

    const sprint_Id = deleteSprintModalDOM.dataset.sprintId;
    data = {
        action: "Delete",
        sprint_id: sprint_Id
    };
    const response = await sendData_Sprint(data);
    if (response.status === "Success") {
        const board = document.getElementById(sprint_Id);
        board.remove();

        const isSprintExist = document.querySelector(".scrum-board-card");

        if (!isSprintExist) {
            showEmptySprintUI();
        }
    }

    deleteSprintModal.hide();
});

//to generate tehe UI Scrum Board
function renderBoard(data) {

    console.log(data);

    // remove if the sprint empty container div exist
    const isSprintContainerEmpty = document.getElementById("emptySprintContainer");
    ;

    if (isSprintContainerEmpty) {
        isSprintContainerEmpty.classList.add("d-none");
    }

    const board = document.createElement('div');
    board.className = 'scrum-board-card';
    board.id = data.sprint_id;
    board._backlogData = data.backlog;
    const backlogHTML = (data.backlog && data.backlog.length > 0)
            ? data.backlog.map(b => `
        <span data-id = ${b.backlogI_id} class="backlog-chip">
            ${b.backlogI_title}
            <span class="badge bg-secondary ms-2">
                ${b.story_point} SP
            </span>
        </span>
    `).join('')
            : `<span class="text-muted small">No backlog assigned</span>`;
    let action_SM = (user_role === "Scrum Master") ? `<button class="btn btn-outline-secondary btn-sm editSprint-btn" ><i class="fas fa-edit me-1"></i> Edit Details</button>
                            <button class="btn btn-outline-danger btn-sm deleteSprint-btn">
                                        <i class="fas fa-trash-alt me-1"></i> Delete
                            </button>` : ``;
    let action_PO = ( data.sprint_status === "Completed") ? ` <button class="btn btn-outline-secondary btn-sm verifySprintEnd-btn">
        <i class="fas fa-file-alt"></i> Review tasks
    </button>` : ``;
    const backlogIds = (data.backlog && data.backlog.length > 0)
            ? data.backlog.map(b => b.backlogI_id).join(',')
            : '';
    board.dataset.backlogs = backlogIds;
    const taskDragAndDrop = user_role === "Scrum Master" || user_role === "Developer" ? `ondragover="allowDrop(event)" ondrop="drop(event)"` : ``;
    const addtaskBtn = user_role === "Scrum Master" ? `<button class="btn text-muted btn-sm fw-bold w-100 text-start" onclick="addTask(this)">+ Add Task</button>` : ``;
    board.innerHTML = `
                    <div class="board-header">
                        <div class="board-info">
                            <h2 class="val-name h4 fw-bold text-primary">${data.sprint_name}</h2>
                            <span class="info-label mt-2">Description / Goal</span><p class="val-goal">${data.sprint_goal}</p>
                            <span class="info-label">Timeline</span><div class="date-row"><i class="far fa-calendar-alt text-primary"></i> <span class="val-start">${data.sprint_start_date}</span> to <span class="val-end">${data.sprint_end_date}</span></div>
                        </div>
                        <div class="text-end">
                            <span class="info-label">Status</span>
                            <div class="badge bg-primary-subtle text-primary val-status rounded-pill px-3 py-2 mb-2">${data.sprint_status}</div><br>
                            ${action_SM} ${action_PO}
                        </div>
                    </div>
                    <div class="notes-grid">
                        <div class="note-box"><span class="info-label">Review Notes</span><div class="note-content val-review">${data.review_notes || 'None'}</div></div>
                        <div class="note-box"><span class="info-label">Retrospective</span><div class="note-content val-retro">${data.restrospective_notes || 'None'}</div></div>
                    </div>
    
                    <!-- ================= SPRINT BACKLOG ================= -->
                    <div class="note-box sprint-backlog-box">
                        <span class="info-label">Sprint Backlog</span>

                        <div class="backlog-list mt-2">
                            ${backlogHTML}
                        </div>
                    </div>
                    <div class="board-layout mt-4">
            ${['TO DO', 'IN PROGRESS', 'DONE'].map(c => `<div class="column" data-status="${c}" ${taskDragAndDrop}>
            <h3>${c}</h3><div class="task-list"></div>  ${addtaskBtn} </div>`).join('')}
                    </div>`;
    document.getElementById('scrum-container').appendChild(board);
    insertTask_Board(data.sprint_id);
}

function showEmptySprintUI() {
    const container = document.getElementById('scrum-container');
    // Safety check to ensure the container exists
    if (!container)
        return;
    // Check if the current user is a Scrum Master
    const isScrumMaster = (typeof user_role !== 'undefined' && user_role === 'Scrum Master');
    // Generate the HTML for the empty state
    const emptyStateHTML = `
                        <div id="emptySprintContainer" class="card border-0 bg-transparent shadow-sm rounded-4 mx-auto mt-5"
                             style="max-width: 600px;">

                            <div class="card border-0 shadow-sm rounded-3">
                                <div class="card-body text-center py-5">

                                    <i class="fas fa-tasks fa-2x text-secondary mb-3"></i>

                                    <h5 class="fw-semibold mb-2">
                                        No Sprints Found
                                    </h6>

                                    <p class="text-muted mb-4">
                                        ${
            isScrumMaster
            ? "No sprint has been created for this project yet."
            : "No sprint is currently available for this project."
            }
                                    </p>

                                    ${
            isScrumMaster
            ? `
                                        <button class="btn btn-primary btn-sm px-3 rounded-pill fw-bold"
                                                data-bs-toggle="modal"
                                                data-bs-target="#sprintModal">
                                            Create Sprint
                                        </button>
                                        `
            : ""
            }

                                </div>
                            </div>
                        `;
    // Inject the empty state into the DOM
    container.innerHTML = emptyStateHTML;
}



//#################################################### TASK SECTION #############################################################################

let isTaskEdit = false;

//populate board with task
async function insertTask_Board(sprint_id) {

    const response = await fetch(`TaskServlet?action=fetchTasks_Sprint&sprint_id=${sprint_id}`);
    const result = await response.json();
    console.log(result.tasks);

    // 1. Check if the tasks array is null, undefined, or empty
    if (!result.tasks || result.tasks.length === 0) {

        // Find the specific board and target its "TO DO" column
        displayEmptyBoardUI(sprint_id);
    } else {
        // 2. If tasks exist, loop through and render them

        // Optional: Clear out any previous empty state text before appending
        const board = document.getElementById(sprint_id);
        if (board) {
            board.querySelectorAll('.task-list').forEach(list => list.innerHTML = '');
        }

        result.tasks.forEach((item) => {
            // 3. Create the task card visually
            const task = document.createElement('div');
            task.className = 'task';
            task.draggable = true;
            task.id = "task-" + item.task_id;
            task.dataset.dependency = (item.taskDependencies || [])
                    .map(dep => dep.depend_on_task_id)
                    .join(",");
            // 4. Set InnerHTML with Hyperlink
            task.innerHTML = `
                <a href="javascript:void(0)" 
                   onclick="getTaskDetails('${item.task_id}')"
                   draggable="false" 
                   class="fw-bold text-decoration-none text-primary task-link">
                        ${item.task_name}
                </a>
                <div class="small text-muted mt-1" style="font-size: 10px;">
                    <i class="far fa-calendar-check"></i> ${item.task_start_date} to ${item.task_end_date}
                </div>
        `;
            task.ondragstart = drag;
            // 5. Append to the correct column list
            const targetList = board.querySelector(
                    `[data-status="${item.task_status}"] .task-list`
                    );
            if (targetList) {
                targetList.appendChild(task);
            }
        });
    }
}
;


function displayEmptyBoardUI(sprint_id) {
    const board = document.getElementById(sprint_id);

    if (!board)
        return;

    board.querySelectorAll(".task-list").forEach(taskList => {

        if (!taskList.querySelector(".emptyTasks")) {
            taskList.insertAdjacentHTML("beforeend", `
                <div class="text-center p-3 text-muted small rounded-3 mt-2 emptyTasks"
                     style="border: 1px dashed #dee2e6; background-color: #f8f9fa;">
                    <i class="fas fa-tasks mb-1 opacity-50 d-block fa-lg"></i>
                    No tasks added yet
                </div>
            `);
        }
    });
}

function removeEmptyBoardUI(sprint_id) {
    const board = document.getElementById(sprint_id);

    if (!board)
        return;

    board.querySelectorAll(".emptyTasks").forEach(el => {
        el.remove();
    });
}


async function sendData_Task(data) {

    const response = await fetch("TaskServlet", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });
    if (!response.ok) {
        throw new Error(response.status);
    }
    ;
    const result = await response.json();
    return result;
}
;
const bsTaskModal = new bootstrap.Modal(document.getElementById('taskModal'));

function addTask(btn) {
    isTaskEdit = false;
    hideAllErrorMsg_Task();
    document.querySelectorAll("#taskModal input, #taskModal textarea, #taskModal select").forEach(el => el.disabled = false);

    // Identify the target column and board
    const taskModel = document.querySelector("#taskModal");
    taskModel.querySelector("#taskModel_title").innerText = "Add Task";
    document.getElementById("taskModel_Sbt").innerText = "Confirm";

    const subBtn = taskModel.querySelector("#taskModel_Sbt");
    document.getElementById("deleteTaskBtn").style.display = "none";

    boardId = btn.closest('.scrum-board-card').id;
    console.log("ID IN ADDTASK()", boardId);

    const columnHeader = btn.closest('.column').querySelector('h3').innerText;

    // Reset modal and set hidden context
    document.querySelectorAll('#taskModal input, #taskModal textarea').forEach(el => el.value = '');
    document.getElementById('t_board_id').value = boardId;
    document.getElementById('t_column_id').value = columnHeader;

    document.querySelector(".add-assignee-btn").classList.remove("d-none");

    document.getElementById("taskModel_Sbt").onclick = confirmAddTask;

    //Clear the main form container to show an empty state placeholder
    const assignedNamesContainer = document.getElementById("assignedNamesContainer");
    assignedNamesContainer.innerHTML = "";

    // 2. Clear the hidden input values
    //document.getElementById("selectedAssigneeIds").value = "";

    // 3. Reset the dropdown list container back to a clean state
    const assigneeListContainer = document.getElementById("assigneeListContainer");
    assigneeListContainer.innerHTML = ` 
                                    <div id="noUsersFoundMessage" class="text-center py-3 text-muted">
                                        <i class="fas fa-search text-secondary mb-2" style="font-size: 1rem; opacity: 0.5;"></i>
                                        <p class="small mb-0 fw-medium">Search for a team member</p>
                                    </div>`;


    loadTaskDepenecy();
    handleTaskBacklog();
    bsTaskModal.show();
}

//
//document.querySelector("#taskModal .modal-body").addEventListener("click", function (e) {
//
//    const targetId = e.target.id;
//    const errorMap = {
//        t_name: "errorTaskName",
//        t_desc: "errorTaskDesc",
//        t_backlog: "errorTaskBacklog",
//        assignee: "errorTaskAssignee",
//        t_start: "errorTaskStart",
//        t_end: "errorTaskEnd"
//    };
//    if (errorMap[targetId]) {
//        document.getElementById(errorMap[targetId])
//                .classList.add("d-none");
//    }
//
//    if (targetId === "t_start" || targetId === "t_end") {
//        document.getElementById("errorTaskDateRange")
//                .classList.add("d-none");
//    }
//});
//
//document.querySelector("#taskModal .modal-body")
//        .addEventListener("click", function (e) {
//
//            const mapping = {
//                "t_name": "errorTaskName",
//                "t_desc": "errorTaskDesc",
//                "t_backlog": "errorTaskBacklog",
//                "assignee": "errorTaskAssignee",
//                "t_start": "errorTaskStart",
//                "t_end": "errorTaskEnd"
//            };
//            const field = e.target.id;
//            if (mapping[field]) {
//
//                document.getElementById(mapping[field])
//                        .classList.add("d-none");
//                // Special case for date range
//                if (field === "t_start" || field === "t_end") {
//                    document.getElementById("errorTaskDateRange")
//                            .classList.add("d-none");
//                }
//            }
//        });
//


function hideAllErrorMsg_Task() {

    const errorField = [
        "errorTaskName",
        "errorTaskDesc",
        "errorTaskBacklog",
        "errorTaskAssignee",
        "errorTaskStart",
        "errorTaskEnd",
        "errorTaskDateRange"
    ];
    errorField.forEach(item => {
        document.getElementById(item).classList.add("d-none");
    });
}

async function loadTaskDepenecy() {

    const sprint_id = document.getElementById("t_board_id").value;
    console.log("Board id : " + sprint_id);
    const response = await fetch(`TaskServlet?action=fetchTask_dependency&sprint_id=${sprint_id}`);
    const result = await response.json();
    const select = $('#t_dependency');
    select.empty();
    if (!select.hasClass("select2-hidden-accessible")) {
        select.select2({
            placeholder: "Select task dependency",
            width: '100%',
            dropdownParent: $('#taskModal')
        });
    }
    console.log(result);
    result.taskData.forEach(item => {
        select.append(new Option(`TASK-${item.task_id} | ${item.task_name}`, item.task_id));
        console.log("Apended");
    });
    select.trigger('change');
}
;

// 1. Target the '+' button
const manageBtn = document.getElementById('manageAssignmentBtn');
const dropdownMenu = document.getElementById('assigneeDropdownMenu');

if (manageBtn && dropdownMenu) {

    // 1. Listen for clicks on the '+' button
    manageBtn.addEventListener('click', function (e) {
        e.preventDefault();
        e.stopPropagation(); // Stops the click from bubbling up to the document

        loadAsignmentDropDown();
    });

    // 2. Close the dropdown if the user clicks anywhere else on the page
    document.addEventListener('click', function (e) {


        // Check if the click happened outside both the button and the menu
        if (!manageBtn.contains(e.target) && !dropdownMenu.contains(e.target)) {
            dropdownMenu.classList.remove('show');
        }

    });
}

//Get assignee details after user selected role
async function loadAsignmentDropDown() {
    console.log("loadAsignmentDropDown triggered");

    try {
        // Removed the extra quotes around "Developer"
        const response = await fetch(`TaskServlet?action=fetch_user&project_id=${project_id}&user_role=Developer`);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.json();
        console.log(result);

        const assigneeListContainer = document.getElementById("assigneeListContainer");
        assigneeListContainer.innerHTML = "";

        const visibleAssignments =
                document.querySelectorAll(
                        "#assignmentDropdownWrapper #assignedNamesContainer > span:not(.d-none)"
                        );

        if (visibleAssignments.length > 0) {

            assigneeListContainer.innerHTML = `
                                                <div class="text-center py-4 text-muted">
                                                    <i class="fas fa-user-check text-secondary mb-2"
                                                       style="font-size: 1.2rem; opacity: 0.5;"></i>
                                                    <p class="small mb-0 fw-medium">
                                                        Assignee already selected
                                                    </p>
                                                    <span class="small" style="font-size: 0.75rem;">
                                                        Remove the current assignee before assigning another developer
                                                    </span>
                                                </div>`;

            const dropdownMenu =
                    document.getElementById("assigneeDropdownMenu");

            if (dropdownMenu) {
                dropdownMenu.classList.add("show");
            }

            return;
        }

        // 1. Handle the Empty State Properly
        if (!result.userData || result.userData.length === 0) {
            // Removed 'display: none' so it actually shows up!
            assigneeListContainer.innerHTML = `
                <div id="noUsersFoundMessage" class="text-center py-4 text-muted">
                    <i class="fas fa-search text-secondary mb-2" style="font-size: 1.2rem; opacity: 0.5;"></i>
                    <p class="small mb-0 fw-medium">No users found</p>
                    <span class="small" style="font-size: 0.75rem;">Try a different role or project</span>
                </div>
            `;

            // Toggle the menu open even if it's empty, then stop the function
            const dropdownMenu = document.getElementById('assigneeDropdownMenu');
            if (dropdownMenu)
                dropdownMenu.classList.add('show');
            return;
        }

        // 2. Build the List if Users Exist
        let dropDownItem = "";
        result.userData.forEach((d) => {
            dropDownItem += `
                <li class="assignee-item mb-1">
                    <div class="user-list-row d-flex align-items-center justify-content-between rounded-3 px-2 py-2" style="cursor: default;">
                        <div class="d-flex align-items-center gap-2">
                            <span class="badge bg-secondary bg-opacity-10 text-secondary border rounded-pill" style="font-size: 0.65rem;">ID: ${d.user_id}</span>
                            <span class="fw-medium small assignee-name">${d.username}</span>
                        </div>
                        <button type="button" data-userid='${d.user_id}' class="btn btn-primary btn-sm rounded-pill py-0 px-3 assign-btn flex-shrink-0 taskMemberAssignBtn" style="font-size: 0.75rem; height: 26px; width: max-content;">
                            Assign
                        </button>
                    </div>
                </li>`;
        });

        // 3. Inject the HTML
        assigneeListContainer.innerHTML = dropDownItem;

        // 4. Safely grab the dropdown menu and toggle it open
        const dropdownMenu = document.getElementById('assigneeDropdownMenu');
        if (dropdownMenu) {
            dropdownMenu.classList.add('show'); // Use 'add' instead of 'toggle' so clicking it rapidly doesn't accidentally close it
        }

    } catch (error) {
        console.error("Failed to load users:", error);
    }
}

document.getElementById("assigneeListContainer").addEventListener("click", function (e) {

    const assignBtn = e.target.closest(".taskMemberAssignBtn");
    const assign_Name = e.target.closest(".user-list-row").querySelector(".assignee-name").textContent;
    console.log(assignBtn);

    newAssignments = {
        task_assigned_to: assignBtn.dataset.userid,
        task_assigned_by: user_id
    };
    newAssignments.user_name = assign_Name;

    addAssignedPill("initialize", newAssignments);

    e.target.closest(".assignee-item").classList.add("d-none");
    console.log(newAssignments);
});

function addAssignedPill(action, data) {
    const assignedNamesContainer = document.getElementById("assignedNamesContainer");

    const assignedNamesDiv = `
        <span id="assignmentTo_${data.task_assigned_to}" class="badge bg-primary bg-opacity-10 text-primary border border-primary-subtle rounded-pill ps-2 pe-2 py-2 d-flex align-items-center gap-2 shadow-sm">

            <span class="badge bg-white text-primary border border-primary-subtle rounded-pill shadow-sm"
                  style="font-size: 0.7rem;">
                ID: ${data.task_assigned_to}
            </span>

            <span class="fw-semibold"
                  style="font-size: 0.85rem;">
                ${data.user_name}
            </span>
    
            <i id="removeAssignment" data-action=${action} data-task-id=${data.task_id} data-task-assigned-to=${data.task_assigned_to} class="fas fa-times ms-1 text-secondary remove-assignee-btn"
                            role="button"
                            title="Remove Assignee"
                            onclick="displayAssignmentRejectionModal(this)">
            </i>
        </span>
    `;

    assignedNamesContainer.innerHTML += assignedNamesDiv;

}

// Grab the new search input and the list of user items
const searchInput = document.getElementById('assigneeSearchInput');
const assigneeItems = document.querySelectorAll('.assignee-item');

if (searchInput) {
    searchInput.addEventListener('input', function (e) {
        // Convert whatever the user typed to lowercase for easy matching
        const searchTerm = e.target.value.toLowerCase().trim();

        // Loop through every user in the list
        assigneeItems.forEach(item => {
            // Grab the text of the ID and the Name inside this specific list item
            const idText = item.querySelector('.badge').innerText.toLowerCase();
            const nameText = item.querySelector('.assignee-name').innerText.toLowerCase();

            // If the search term is found in either the ID or the Name, show it. Otherwise, hide it.
            if (idText.includes(searchTerm) || nameText.includes(searchTerm)) {
                item.style.display = 'block';
            } else {
                item.style.display = 'none';
            }
        });
    });

    // Optional UI touch: Clear the search bar every time the dropdown is opened
    const manageBtn = document.getElementById('manageAssignmentBtn');
    manageBtn.addEventListener('show.bs.dropdown', () => {
        searchInput.value = '';
        assigneeItems.forEach(item => item.style.display = 'block'); // Reset the list visibility
    });
}

function validateTaskForm(data) {

    let isValid = true;
    const taskName = data.task_name.trim();
    const taskDesc = data.task_desc.trim();
    const backlogId = data.backlog_id;
    const assigneeId = data.assignee;
    const startDate = data.task_start_date;
    const endDate = data.task_end_date;
    // Task Name
    if (taskName === "") {
        document.getElementById("errorTaskName").innerText =
                "Task name is required.";
        document.getElementById("errorTaskName")
                .classList.remove("d-none");
        isValid = false;
    }

    // Description
    if (taskDesc === "") {
        document.getElementById("errorTaskDesc").innerText =
                "Task description is required.";
        document.getElementById("errorTaskDesc")
                .classList.remove("d-none");
        isValid = false;
    }

    // Backlog
    if (!backlogId || backlogId === "") {
        document.getElementById("errorTaskBacklog").innerText =
                "Please select a backlog item.";
        document.getElementById("errorTaskBacklog")
                .classList.remove("d-none");
        isValid = false;
    }

    // Assignee
    if ((!newAssignments || newAssignments.length === 0) && !document.querySelector("#assignedNamesContainer > span")) {
        document.getElementById("errorTaskAssignee").innerText =
                "Please select an assignee.";
        document.getElementById("errorTaskAssignee")
                .classList.remove("d-none");
        isValid = false;
    }

    // Start Date
    if (startDate === "") {
        document.getElementById("errorTaskStart").innerText =
                "Start date is required.";
        document.getElementById("errorTaskStart")
                .classList.remove("d-none");
        isValid = false;
    }

    // End Date
    if (endDate === "") {
        document.getElementById("errorTaskEnd").innerText =
                "End date is required.";
        document.getElementById("errorTaskEnd")
                .classList.remove("d-none");
        isValid = false;
    }

    // Date Range
    if (startDate !== "" && endDate !== "") {

        const start = new Date(startDate);
        const end = new Date(endDate);
        if (end < start) {
            document.getElementById("errorTaskDateRange").innerText =
                    "End date must be later than or equal to Start date.";
            document.getElementById("errorTaskDateRange")
                    .classList.remove("d-none");
            isValid = false;
        }
    }

    return isValid;
}


async function confirmAddTask() {

    console.log("confirmAddTask");
    console.log(isTaskEdit);

    const data = {
        action: "Insert",
        task_name: document.getElementById('t_name').value,
        task_desc: document.getElementById('t_desc').value,
        task_start_date: document.getElementById('t_start').value,
        task_end_date: document.getElementById('t_end').value,
        sprint_Id: document.getElementById('t_board_id').value,
        task_status: document.getElementById('t_column_id').value,
        taskAssignment: newAssignments,
        task_assigned_by: user_id,
        taskDepedencies: $('#t_dependency').val().map(Number) || null,
        backlog_id: document.getElementById("t_backlog").value.trim() || null
    };
    const isvalid = validateTaskForm(data);
    if (!isvalid) {
        return;
    }
    console.log("Task dependency :" + data.taskDepedencies);
    let task_id = null;
    if (isTaskEdit) {
        data.action = "UpdateTaskDetials";
        task_id = document.getElementById("task_id").value.replace("task-", "");
        console.log("Task id", task_id);
        console.log("Taskkk ", document.getElementById('taskModal').querySelector("#task_id").value.replace("task-", ""));
        data.task_id = task_id;
        console.log("Removed Assignment : ", removedAssignments);
        data.removedAssignment = removedAssignments;
        const oldTask = document.getElementById("task-" + task_id);
        console.log("Task old :", oldTask);
        if (oldTask)
            oldTask.remove();
    }

    console.log(data);
    const result = await sendData_Task(data);
    console.log(result);
    if (result.status !== "Success") {
        bsTaskModal.hide();
        return;
    }

    //clean the array
    newAssignments = null;
    removedAssignments = null;


    removeEmptyBoardUI(data.sprint_Id);
    displayMessage(result.status, result.message);
    // 3. Create the task card visually
    const task = document.createElement('div');
    task.className = 'task';
    task.draggable = true;
    task.id = "task-" + result.task_id;
    task.dataset.dependency = data.taskDepedencies;
    // 4. Set InnerHTML with Hyperlink
    // Note: draggable="false" on the <a> tag prevents drag conflict
    task.innerHTML = `

    <a href="javascript:void(0)" 
       onclick="getTaskDetails('${result.task_id}')"
       draggable="false" 
       class="fw-bold text-decoration-none text-primary task-link">
            ${data.task_name}
    </a>
            <div class="small text-muted mt-1" style="font-size: 10px;">
                <i class="far fa-calendar-check"></i> ${data.task_start_date} to ${data.task_end_date}
            </div>
        `;
    task.ondragstart = drag;
    // 5. Append to the correct column list
    console.log("Sprint id ", data.sprint_Id);
    const board = document.getElementById(data.sprint_Id);
    console.log("Board", board);
    //remove old task 


    const columns = board.querySelectorAll('.column');
    let targetList;
    columns.forEach(col => {
        if (col.dataset.status === data.task_status) {
            targetList = board
                    .querySelector(`.column[data-status="${data.task_status}"] .task-list`);
        }
    });
    if (targetList) {
        targetList.appendChild(task);
        bsTaskModal.hide(); // Hide the Bootstrap modal
    }
}

document.getElementById("taskModel_Cancle").addEventListener("click", () => {
    newAssignments.length = 0;
    removedAssignments.length = 0;
});

const bsViewModal = new bootstrap.Modal(document.getElementById('viewTaskModal'));
// Global Modal Instance
const taskModalElem = document.getElementById('viewTaskModal');
const bsTaskViewModal = new bootstrap.Modal(taskModalElem);
function viewTaskDetails(taskId) {
    // 1. Populate data (Replace with AJAX fetch later)
    document.getElementById('t_badge_id').innerText = "Task ID: " + taskId;
    // Example: fetch existing name from the card clicked
    const card = document.getElementById(taskId);
//    document.getElementById('t_name').value = card.querySelector('.task-link').innerText;

    // 2. Always start in View Mode
    switchToViewMode();
    bsTaskViewModal.show();
}

function allowDrop(ev) {
    ev.preventDefault();
}

//enable task drag
function drag(ev) {

    const task = ev.target;
    console.log("task drag ", task);
    ev.dataTransfer.setData("taskId", task.id);
    task.dataset.oldStatus = task.closest('.column').dataset.status;
}

// enables task drop
async function drop(ev) {

    ev.preventDefault();
    const taskId = ev.dataTransfer.getData("taskId").replace("task-", "");
    const task = document.getElementById("task-" + taskId);
    const board = task.closest('.scrum-board-card');
    console.log("board :", board);
    const newColumn = ev.target.closest('.column');
    if (!newColumn)
        return;
    const newStatus = newColumn.dataset.status;
    const oldStatus = task.dataset.oldStatus;
    // optimistic UI move
    newColumn.querySelector('.task-list').appendChild(task);
    if (oldStatus === newStatus)
        return;
    try {
        // get dependency
        const deps = task.dataset.dependency
                ? task.dataset.dependency.split(",").map(Number).filter(n => n > 0)
                : [];
        console.log("Dependency : ", deps);
        if (deps.length > 0) {
            // map to numberic to compare
            const statusMap = {
                "TO DO": 1,
                "IN PROGRESS": 2,
                "DONE": 3
            };
            for (const item of deps) {

                const depTask = document.getElementById("task-" + item);
                if (!depTask) {
                    throw new Error(`Dependency task ${item} not found`);
                }

                const column = depTask.closest('.column');
                if (!column) {
                    throw new Error(`Column not found for task ${item}`);
                }

                const depStatus = column.dataset.status;
                const depStatusNum = statusMap[depStatus];
                if (depStatusNum !== 3) {

                    const depTaskName = depTask.textContent.trim();
                    console.log("Task Name : ", depTaskName);
                    throw new Error(`${depTaskName} is not completed`);
                }
            }
        }

        // ✅ only runs if all dependencies are DONE
        const data = {
            action: "updateTaskStatus",
            task_id: taskId,
            task_status: newStatus
        };
        const result = await sendData_Task(data);
        if (result.status !== "Success") {
            throw new Error("Update failed");
        }

        displayMessage(result.status, result.message);
    } catch (err) {

        const oldColumn = board.querySelector(
                `[data-status="${oldStatus}"] .task-list`
                );
        console.log("OLD STATUS:", oldStatus);
        console.log("TASK:", task);
        console.log("OLD COLUMN:", oldColumn);
        if (oldColumn) {
            oldColumn.appendChild(task);
        }

        console.error(err);
        //alert("Update failed, reverting change");

        displayMessage("Failed", err.message);
    }
}


//#################################################################### Task ###############################################################################
//
//prepare the form for edit

async function getTaskDetails(task_id) {

    console.log("getTaskDetails Execute");
    console.log("Task Id : ", task_id);

    const taskModel = document.querySelector("#taskModal");
    taskModel.querySelector("#taskModel_title").innerText = "Task Information";
    const subBtn = taskModel.querySelector("#taskModel_Sbt");
    subBtn.innerText = "Edit Task";
    subBtn.onclick = () => editTask();
//   Set Delete Btn
    const deletebtn = document.getElementById("deleteTaskBtn");
    deletebtn.style.display = "none";
    deletebtn.onclick = () => deleteTask(task_id);
    document.querySelector(".add-assignee-btn").classList.add("d-none");

    // display task dependency
    const task = document.getElementById("task-" + task_id);
    console.log("task during edit : ", task);
    const taskStatus = task.closest('.column').dataset.status;
    console.log("Task Status :", taskStatus);
    taskModel.querySelector("#t_column_id").value = taskStatus;
    const sprint_id = task.closest('.scrum-board-card').id;
    taskModel.querySelector("#t_board_id").value = sprint_id;
    console.log("Sprint id", sprint_id);

    //get the task details
    const response = await fetch(`TaskServlet?action=fetchTask_Edit&task_id=${task_id}`);
    const result = await response.json();
    console.log("Result : ", result);
    console.log(typeof result);

    const input = document.createElement("input");
    input.value = task_id;
    input.type = "hidden";
    input.id = "task_id";
    taskModel.append(input);

    taskModel.querySelector("#t_name").value = result.taskData.task_name;
    taskModel.querySelector("#t_desc").value = result.taskData.task_desc;
    console.log(taskModel);

    addAssignedPill("edit", result.taskData.taskAssignment);

//    taskModel.querySelector("#t_user_role").value = result.taskData.task_assigned_to_Role
    const assiosiatedBacklog = taskModel.querySelector("#t_backlog");
    console.log("Backlog id selected : ", result.taskData.backlog_id);



    // task assignment fields
    const assignedNamesContainer = document.getElementById("assignedNamesContainer");
    assignedNamesContainer.innerHTML = "";
    let assignedNamesDiv = "";

    const item = result.taskData.taskAssignment;
    assignedNamesDiv = `
        <span id="assignmentTo_${item.task_assigned_to}" class="badge bg-primary bg-opacity-10 text-primary border border-primary-subtle rounded-pill ps-2 pe-2 py-2 d-flex align-items-center gap-2 shadow-sm">

            <span class="badge bg-white text-primary border border-primary-subtle rounded-pill shadow-sm"
                  style="font-size: 0.7rem;">
                ID: ${item.task_assigned_to}
            </span>

            <span class="fw-semibold"
                  style="font-size: 0.85rem;">
                ${item.user_name}
            </span>

            <i id="removeAssignment" data-action="edit" data-task-id=${task_id} data-task-assigned-to=${item.task_assigned_to} class="d-none fas fa-times ms-1 text-secondary remove-assignee-btn"
               role="button"
               title="Remove Assignee"
               onclick="displayAssignmentRejectionModal(this)">
            </i>

        </span>
    `;

    assignedNamesContainer.innerHTML = assignedNamesDiv;


    handleTaskBacklog(result.taskData.backlog_id);

    taskModel.querySelector("#t_start").value = result.taskData.task_start_date;
    taskModel.querySelector("#t_end").value = result.taskData.task_end_date;
    const select = $('#t_dependency');
    select.empty(); // clear previous selections

    if (!select.hasClass("select2-hidden-accessible")) {
        select.select2({
            placeholder: "Select task dependency",
            width: '100%',
            dropdownParent: $('#taskModal')
        });
    }

    console.log("task dependency :", result.taskData.taskDependencies);
    const response2 = await fetch(`TaskServlet?action=fetchTask_dependency&sprint_id=${sprint_id}&task_id=${task_id}`);
    const result2 = await response2.json();
    console.log("result2 : ", result2.taskData);
    result.taskData.taskDependencies.forEach(item => {

        const option = new Option(
                `TASK-${item.depend_on_task_id} | ${item.depend_on_task_Name}`,
                item.depend_on_task_id,
                true,
                true
                );
        select.append(option);
    });
    console.log("result 1");
    //Drop down in edit    
    result2.taskData.forEach(item => {

        const option = new Option(
                `TASK-${item.task_id} | ${item.task_name}`,
                item.task_id, );
        select.append(option);
    });
    console.log("result 1");
    select.trigger('change');
    $('#taskDependency').trigger('change');
    document.querySelectorAll("#taskModal input, #taskModal textarea, #taskModal select")
            .forEach(el => el.disabled = true);
    document.getElementById("deleteTaskBtn").style.display = "none";
    hideAllErrorMsg_Task();
    bsTaskModal.show();
}


//get the assignment removal reason
const assignmentRemovalModalDOM = document.getElementById("assignmentRemovalModal");
function displayAssignmentRejectionModal(btn) {

    console.log(btn);
    const action = btn.dataset.action;
    const taskAssignedTo = btn.dataset.taskAssignedTo;
    console.log(action, taskAssignedTo);

    if (action !== "edit") {
        console.log("btn", document.getElementById(`assignmentTo_${taskAssignedTo}`));
//        document.getElementById(`assignmentTo_${taskAssignedTo}`).classList.add("d-none");
        btn.parentElement.remove();
        console.log(btn.parentElement.className);

        return;
    }

    const assignmentRemovalModal = bootstrap.Modal.getOrCreateInstance(assignmentRemovalModalDOM);
    document.getElementById("rejectionAssignmentReason").innerText = "";
    assignmentRemovalModalDOM.dataset.task_Id = btn.dataset.taskId;
    assignmentRemovalModalDOM.dataset.taskAssignedTo = taskAssignedTo;

    assignmentRemovalModal.show();

}

// get the data from the rejection module
document.getElementById("confirmAssignmentRejectBtn").addEventListener("click", async function removeAssignedMember_task() {

    removedAssignments = {
        task_id: assignmentRemovalModalDOM.dataset.taskId,
        task_assigned_to: assignmentRemovalModalDOM.dataset.taskAssignedTo,
        removal_reason: document.getElementById("rejectionAssignmentReason").value
    };

    const assigneeBadge = document.getElementById(`assignmentTo_${removedAssignments.task_assigned_to}`);

    if (assigneeBadge) {
        assigneeBadge.classList.add("d-none");
    }

    const assignmentRemovalModal = bootstrap.Modal.getOrCreateInstance(assignmentRemovalModalDOM);
    assignmentRemovalModal.hide();
});


// delete task
async function deleteTask(task_id) {

    data = {
        action: "deleteTask",
        task_id: task_id
    };
    const result = await sendData_Task(data);

    if (result.status === "Success") {
        const oldTask = document.getElementById("task-" + task_id);
        const relatedBoard = oldTask.closest(".scrum-board-card");
        console.log("Task old :", oldTask);
        if (oldTask) {
            oldTask.remove();

            const isBoardEmpty = relatedBoard.querySelector('[id^="task-"]');

            if (!isBoardEmpty) {
                displayEmptyBoardUI(relatedBoard.id);
            }
        }
    }


    bsTaskModal.hide();
    displayMessage(result.status, result.message);
}


function editTask() {

    document.querySelectorAll("#taskModal input, #taskModal textarea, #taskModal select")
            .forEach(el => el.disabled = false);
    const taskModel = document.querySelector("#taskModal");
    taskModel.querySelector("#taskModel_title").innerText = " Edit Task";
    const subBtn = taskModel.querySelector("#taskModel_Sbt");
    subBtn.innerText = "Edit Task";
    subBtn.onclick = () => confirmAddTask();

    document.querySelector(".add-assignee-btn").classList.remove("d-none");
    document.querySelector(".remove-assignee-btn").classList.remove("d-none");
    document.getElementById("deleteTaskBtn").style.display = "block";
    isTaskEdit = true;
    console.log(isTaskEdit);
}

function displayMessage(status, msg) {

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
///////////////////////////////////////////////////////////////////////// SprintReview  ///////////////////////////////////////////////////////////////////

async function handleTaskBacklog(selectedId = null) {
    console.log("Selected backlog : ", selectedId);
    const sprint_id = document.getElementById("t_board_id").value;
    const response = await fetch(`SprintServlet?action=fetchSprint_backlog&sprint_id=${sprint_id}`);
    if (!response.ok) {
        console.log("Server Error : ");
    }

    const result = await response.json();
    console.log(result);
    const assosiatedBacklog = document.getElementById("t_backlog");
    console.log(assosiatedBacklog);
    // clear existing

    assosiatedBacklog.innerHTML = "";
// create placeholder
    const placeholder = document.createElement("option");
    placeholder.value = "";
    placeholder.textContent = "-- Associate Backlog Item --";
    placeholder.disabled = true;
    assosiatedBacklog.appendChild(placeholder);
    result.AssosiatedBacklog.forEach((item) => {
        const Option = document.createElement("option");
        Option.value = item.backlogI_id;
        Option.textContent = item.backlogI_title;
        assosiatedBacklog.append(Option);
    });
    // ✅ set selected AFTER options loaded
    if (selectedId) {
        assosiatedBacklog.value = String(selectedId);
    } else {
        placeholder.selected = true;
}
}
;
async function populateSprintReview(sprint_id, sprintName) {

    const response = await fetch(`TaskServlet?action=fetchTask_Backlog&sprint_id=${sprint_id}`);
    const result = await response.json();
    
    document.getElementById("sprintReviewModalTitle").innerText = sprintName;
    
    const sprintReviewContainer = document.getElementById("sprintReviewContainer");
    sprintReviewContainer.innerHTML = "";
    
    let backlog_id = null;
    let html = "";
    console.log(result);

    result.tasksData.forEach((item, index) => {
        console.log(item);
        const isNewGroup = backlog_id !== item.backlog.backlogI_id;
        const isApproved = item.taskApproval?.taskApproval_status === "approve";
        const isRejected = item.taskApproval?.taskApproval_status === "reject";

        let approveBtn = "";
        let rejectReasonSymbol = "";
        let rejectBtn = "";
//        if (user_role === "Product Owner") {
//            approveBtn = `
//                            <button value="Approve"
//                                class="btn btn-sm btn-outline-success approve-btn ${isApproved ? 'bg-success text-white' : ''}">
//                                <i class="fas fa-check me-1"></i> Approve
//                            </button>`;
//
//            rejectBtn = `<button value="Reject" class="btn btn-sm btn-outline-danger reject-btn ${isRejected ? `bg-danger text-white` : ``} me-1">
//                        <i class="fas fa-times me-2"></i> Reject
//                        </button>`;
//        } else{
//            approveBtn = `
//                            <button value="Approve"
//                                class="btn btn-sm btn-outline-success approve-btn ${isApproved ? 'bg-success text-white' : ''}">
//                                <i class="fas fa-check me-1"></i> Approve
//                            </button>`;
//
//            rejectBtn = `<button value="Reject" class="btn btn-sm btn-outline-danger reject-btn ${isRejected ? `bg-danger text-white` : ``} me-1">
//                        <i class="fas fa-times me-2"></i> Reject
//                        </button>`;
//        }

        if (user_role === "Product Owner") {
            // Product Owner gets fully interactive buttons
            approveBtn = `
        <button value="Approve"
            class="btn btn-sm btn-outline-success approve-btn ${isApproved ? 'bg-success text-white' : ''}">
            <i class="fas fa-check me-1"></i> Approve
        </button>`;

            rejectBtn = `
        <button value="Reject" 
            class="btn btn-sm btn-outline-danger reject-btn ${isRejected ? 'bg-danger text-white' : ''} me-1">
            <i class="fas fa-times me-2"></i> Reject
        </button>`;
        } else {
            // Others only see a single, unclickable status badge based on the state
            if (isApproved) {
                approveBtn = `
            <span class="badge bg-success p-2">
                <i class="fas fa-check me-1"></i> Approved
            </span>`;
                rejectBtn = ''; // Hide the reject button completely

            } else if (isRejected) {
                approveBtn = ''; // Hide the approve button completely
                rejectBtn = `
            <span class="badge bg-danger p-2">
                <i class="fas fa-times me-1"></i> Rejected
            </span>`;

            } else {
                // Optional: What others see if it hasn't been reviewed yet (Pending)
                approveBtn = `
            <span class="badge bg-secondary p-2">
                <i class="fas fa-clock me-1"></i> Pending Review
            </span>`;
                rejectBtn = '';
            }
        }

        rejectReasonSymbol = isRejected ? `<i id="rejectionReasonicon" class="fas fa-comment-dots me-1 cursor-pointer"></i>` : `<i id="rejectionReasonicon" class=" d-none fas fa-comment-dots me-1 cursor-pointer"></i>`;
        // 👉 close previous table BEFORE opening new one
        if (isNewGroup && backlog_id !== null) {
            html += `</tbody></table></div>`;
        }

        // 👉 open new group
        if (isNewGroup) {
            html += `
                <h5 class="fw-bold mb-3">${item.backlog.backlogI_title }</h5>
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
            `;
        }

        // 👉 add row
        html += `
            <tr id=${item.task_id} data-approval_id='${item.taskApproval.approval_id !== null ? item.taskApproval.approval_id : null}' class="taskRow">
                <td>${item.task_name}</td>
                <td><span class="badge bg-warning text-dark">${item.task_status}</span></td>
                <td class="text-end">
                    ${rejectReasonSymbol}
                    ${approveBtn} 
                    ${rejectBtn}
                </td>
            </tr>`;
        backlog_id = item.backlog.backlogI_id;
        // 👉 close LAST table at the end
        if (index === result.tasksData.length - 1) {
            html += `</tbody></table></div>`;
        }
    });
    // 👉 append ONCE (important)
    sprintReviewContainer.innerHTML = html;
    const sprintReviewModal = document.getElementById("sprintReviewModal");
    const sprintReviewModalBoot = new bootstrap.Modal(sprintReviewModal);
    sprintReviewModalBoot.show();
}

let taskRejectionUIUpdate_e;


document.getElementById("sprintReviewModal").addEventListener("click", async function (e) {
    console.log("clicked");

    const reject_btn = e.target.closest(".reject-btn");
    const approve_btn = e.target.closest(".approve-btn");
    const rejectionReasonicon = e.target.closest("#rejectionReasonicon");

    var status;
    let task_id;
    let taskApproval_id;

    if (reject_btn) {
        const taskRejectionModalDOM = document.getElementById("taskRejectionModal");

        console.log(taskRejectionModalDOM);
        const taskRejectionModal = bootstrap.Modal.getOrCreateInstance(taskRejectionModalDOM);
        taskRejectionModalDOM.dataset.action = "rejection";
        document.getElementById("confirmTaskRejectBtn").innerText = "Reject";
        document.getElementById("rejectionTaskReason").value = "";
        const row = e.target.closest("tr");
        console.log(row);
        taskApproval_id = row.dataset.approval_id;
        task_id = row.id;
        taskRejectionUIUpdate_e = e;
        taskRejectionModalDOM.dataset.task_id = task_id;
        taskRejectionModalDOM.dataset.taskApproval_id = taskApproval_id;
        console.log("reject", task_id, "ssss", taskApproval_id);
        taskRejectionModal.show();

    } else if (approve_btn) {
        const row = e.target.closest("tr");
        taskApproval_id = row.dataset.approval_id;
        task_id = row.id;
        console.log(task_id);
        status = "approve";
        console.log("approve", task_id, taskApproval_id);
        const result = await sendTask_approval(task_id, status, taskApproval_id);
        console.log(result);

        if (result.status === "Success") {
            const row = e.target.closest("tr");
            if (result.taskAproval_id && result.taskAproval_id !== null) {
                row.dataset.approval_id = result.taskAproval_id;
                console.log("after update", row);
            }
            updateTaskApproval_UI(e, status);
        }
    } else if (rejectionReasonicon) {
// val-name
            console.log("rejecteion icon clicked");
            const row = e.target.closest("tr");
            const approval_id = row.dataset.approval_id;

            const response = await fetch(`TaskServlet?action=fetchReason&reason_id=${approval_id}`);
            const result = await response.json();
            console.log(result);

            document.getElementById("rejectionTaskPromptMessage").innerText = "This Task is being rejected. Please provide a reason below. "
            const taskRejectionModalDOM = document.getElementById("taskRejectionModal");
            const taskRejectionModal = bootstrap.Modal.getOrCreateInstance(taskRejectionModalDOM);

            taskRejectionModalDOM.dataset.task_id = result.rejectionReason.task_id;
            document.getElementById("rejectionTaskReason").value = result.rejectionReason.remarks;


            document.getElementById("confirmTaskRejectBtn").innerText = "Edit";
            taskRejectionModalDOM.dataset.action = "editReason";
            console.log(taskRejectionModalDOM);
            
            if (user_role !== "Product Owner") {
                document.getElementById("rejectionTaskPromptMessage").innerText = "This task is rejected by product owner due to ";
                taskRejectionModalDOM.querySelector(".modal-footer").classList.add("d-none");
            }
            
            taskRejectionModal.show();
        
    }

//    const result = await sendTask_approval(task_id, status, taskApproval_id);
//    console.log(result);

});
//    
//} else {
//    document.getElementById("rejectionReasonicon").addEventListener("click", async function (e) {
//
//        console.log("rejecteion icon clicked");
//        const row = e.target.closest("tr");
//        const approval_id = row.dataset.approval_id;
//
//        const response = await fetch(`TaskServlet?action=fetchReason&reason_id=${approval_id}`);
//        const result = await response.json();
//        console.log(result);
//
//        document.getElementById("rejectionTaskPromptMessage").innerText = "This task is rejected by product owner due to "
//
//        const taskRejectionModalDOM = document.getElementById("taskRejectionModal");
//        const taskRejectionModal = bootstrap.Modal.getOrCreateInstance(taskRejectionModalDOM);
//
//        taskRejectionModalDOM.dataset.task_id = result.rejectionReason.task_id;
//        document.getElementById("rejectionTaskReason").value = result.rejectionReason.remarks;
//
//
//        document.getElementById("confirmTaskRejectBtn").innerText = "Edit";
//        taskRejectionModalDOM.dataset.action = "editReason";
//        console.log(taskRejectionModalDOM);
//        taskRejectionModal.show();
//    });
//}


document.getElementById("confirmTaskRejectBtn").addEventListener("click", async function () {

    console.log("reject btn clicked");
    const action = document.getElementById("taskRejectionModal").dataset.action;
    console.log("action ", action);

    if (action === "rejection") {
        const taskRejectionModalDOM = document.getElementById("taskRejectionModal");
        console.log(taskRejectionModalDOM);
        const taskRejectionModal = bootstrap.Modal.getOrCreateInstance(taskRejectionModalDOM);

        const task_id = taskRejectionModalDOM.dataset.task_id;

        const taskApproval_id = taskRejectionModalDOM.dataset.taskApproval_id;
        console.log("taskApproval_id");
        const reason = document.getElementById("rejectionTaskReason").value;

        const result = await sendTask_approval(task_id, "reject", taskApproval_id, reason);
        console.log(result);

        taskRejectionModal.hide();

        if (result.status === "Success") {
            const e = taskRejectionUIUpdate_e;
            const row = e.target.closest("tr");
            if (result.taskAproval_id && result.taskAproval_id !== null) {
                row.dataset.approval_id = result.taskAproval_id;
                console.log("after update", row);
            }
            updateTaskApproval_UI(e, "reject");
        }
    } else if (action === "editReason") {
        sendTask_rejection();
    }

});

async function sendTask_rejection() {

    const reason = document.getElementById("rejectionTaskReason").value;
    const taskRejectionModalDOM = document.getElementById("taskRejectionModal");
    const taskRejectionModal = bootstrap.Modal.getOrCreateInstance(taskRejectionModalDOM);

    const task_id = taskRejectionModalDOM.dataset.task_id;
    const data = {
        action: "updateTaskReason",
        task_id: task_id,
        remarks: reason
    };

    const result = sendData_TaskApproval(data);
    if (result.status === "Success") {
        console.log("saved");
    }

    taskRejectionModal.hide();
}


async function sendTask_approval(task_id, status, taskApproval_idStr, reason) {

    var action = "insert_taskApproval";
    const taskApproval_id = taskApproval_idStr && taskApproval_idStr !== "null" ? parseInt(taskApproval_idStr, 10) : null;
    console.log(taskApproval_id, typeof (taskApproval_id), (taskApproval_id !== null && taskApproval_id !== ""));

    if (taskApproval_id !== null && taskApproval_id !== 0 && taskApproval_id !== "" && taskApproval_id !== 0) {
        action = "update_taskApproval";
    }

    const data = {
        action: action,
        task_id: task_id,
        approval_id: taskApproval_id,
        taskApproval_status: status,
        approved_by: user_id,
        remarks: reason
    };
    console.log(data);
    const result = sendData_TaskApproval(data);
    return result;
}

async function sendData_TaskApproval(data) {
    try {
        const response = await fetch("TaskServlet", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });
        if (!response.ok) {
            throw new Error("Server error: " + response.status);
        }

        const result = await response.json();
        console.log("return Result");
        return result;
        console.log("Failed to return Result");
    } catch (error) {
        console.error("Error sending task approval:", error);
        return null;
    }
}


function updateTaskApproval_UI(event, status) {

    const button = event.target.closest("button");
    console.log(status);
    if (!button)
        return;

    const container = button.parentElement;
    const approveBtn = container.querySelector(".approve-btn");
    const rejectBtn = container.querySelector(".reject-btn");
    const rejectionReasonicon = container.querySelector("#rejectionReasonicon");

    // 🔥 RESET BOTH (FULL RESET)
    approveBtn.className = "btn btn-sm btn-outline-success approve-btn";
    rejectBtn.className = "btn btn-sm btn-outline-danger reject-btn";
    // 🔥 APPLY STATE
    if (status === "approve") {
        rejectionReasonicon.classList.add("d-none");
        approveBtn.className = "btn btn-sm btn-success text-white approve-btn";
    } else if (status === "reject") {
        rejectionReasonicon.classList.remove("d-none");
        rejectBtn.className = "btn btn-sm btn-danger text-white reject-btn";
    }

    taskRejectionUIUpdate_e = null;
}

//##############################################################  Document  ##################################################################


let backlogModal = document.getElementById("backlogDocModal");
async function displayBacklogDocument(backlog_id) {

    const response = await fetch(`BacklogDocumentServlet?action=fetchDocument&backlogItem_id=${backlog_id}`);
    const result = await response.json();
    console.log(result);
    if (!response.ok) {
        throw  new Error("Server error" + response.status);
    }

    const docDiv = document.getElementById("backlogFileRegistry");
    docDiv.innerHTML = "";
    const docs = Array.isArray(result.documentData)
            ? result.documentData
            : result.documentData
            ? [result.documentData]
            : [];
    console.log(docs);
    if (docs.length > 0) {
        result.documentData.forEach((item, index) => {

            console.log(item),
                    appendDocument(item, true);
        });
    } else {
        appendDocument([], false);
    }

    const backlogModalBoot = bootstrap.Modal.getOrCreateInstance(backlogModal);
    const tabTrigger = document.querySelector('[data-bs-target="#viewPane"]');
    const tab = new bootstrap.Tab(tabTrigger);
    tab.show();
}

async function displayBacklogDetails(backlog_id) {

    const response = await fetch(`BacklogServlet?backlog_id=${backlog_id}&action=fetchBacklogSprint`);
    const result = await response.json();
    console.log(result);
    document.getElementById("backlogDetails").innerHTML = `
                                            <tr>
                                                <td class="fw-bold">${result.backlogData.backlogI_title}</td>
                                                <td>${result.backlogData.backlogI_desc}</td>
                                                <td>${result.backlogData.acceptance_cri}</td>
                                                <td class="text-center">${result.backlogData.mandays}</td>
                                                <td class="text-center">${result.backlogData.story_point}</td>
                                            </tr>`;
    const backlogModalBoot = bootstrap.Modal.getOrCreateInstance(backlogModal);
    const tabTrigger = document.querySelector('[data-bs-target="#viewBacklogPane"]');
    const tab = new bootstrap.Tab(tabTrigger);
    tab.show();
}
backlogModal.addEventListener("click", (e) => {

    const viewDocNavBtn = e.target.closest("#viewDocNavBtn");
    const viewBacklogDetailsBtn = e.target.closest("#viewBacklogDetailsBtn");
    const viewDocBtn = e.target.closest(".docViewBtn");
    const downloadDocBtn = e.target.closest(".docDownloadBtn");
    if (viewBacklogDetailsBtn) {

        backlog_id = backlogModal.dataset.backlogId;
        displayBacklogDetails(backlog_id);
    } else if (viewDocNavBtn) {
        backlog_id = backlogModal.dataset.backlogId;
        displayBacklogDocument(backlog_id);
    } else if (viewDocBtn) {
        const row = e.target.closest("tr");
        window.open(`BacklogDocumentServlet?action=fetchDocument_view&document_id=${row.id}`);
    } else if (downloadDocBtn) {
        const row = e.target.closest("tr");
        window.open(`BacklogDocumentServlet?action=downloadDocument&document_id=${row.id}`);
    }
});
function appendDocument(data, status) {

    document_name = data !== null ? data.document_name : "";
    console.log(document_name);
    document_type = data !== null ? data.document_type : null;
    console.log(document_type);
    const docDiv = document.getElementById("backlogFileRegistry");
    const row = document.createElement("tr");
    row.id = data instanceof FormData ? data.get("document_id") : data.document_id;
    row.name = document_name;
    console.log(data);
    if (status) {

        row.innerHTML = `
            <td>
                <a href="#" class="fw-bold text-decoration-none doc-edit-link">
                    ${document_name}
                </a>
            </td>
            <td>
                <span class="badge bg-secondary-subtle text-secondary border">
                    ${document_type}
                </span>
            </td>
            <td class="text-center pe-4">
                <button class="btn btn-sm btn-light border p-1 px-2 docViewBtn">
                    <i class="fas fa-eye text-muted"></i>
                </button>
                <button class="btn btn-sm btn-light border p-1 px-2 docDownloadBtn">
                    <i class="fas fa-download text-muted"></i>
                </button>
            </td>
        `;
    } else {
        row.innerHTML = `
                                <td colspan="5">
                                    <div class="text-center py-4 text-muted">
                                        <i class="bi bi-file-earmark-x" style="font-size: 2rem;"></i>
                                        <p class="mt-2 mb-0">No documents associated with this backlog</p>
                                    </div>
                                </td>
                            
                        `;
    }
    docDiv.append(row);
    return;
}


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

