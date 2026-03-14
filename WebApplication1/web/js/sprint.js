/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
let pendingBacklogIds = null;
let sprint_id = null;


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
    document.getElementById('modalTitle').innerText = "Initialize Project";
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


const sprintModalEl = document.getElementById('sprintModal');
sprintModalEl.addEventListener("shown.bs.modal", async function () {

    document.body.style.paddingRight = "0px";

    const backlogSelect = $('#s_backlog_links');

    const data = {
        action: "fetchBacklog",
        project_id: project_id,
        mode: "Create"
    };

    console.log("Edit Mode", isEditMode);
    if (isEditMode) {
        data.mode = "Edit";
        data.sprint_id = sprint_id;
    }

    try {
        const result = await sendData_Sprint(data);

        /* ✅ destroy old Select2 if exists */
        if (backlogSelect.hasClass("select2-hidden-accessible")) {
            backlogSelect.select2('destroy');
        }

        /* ✅ clear old options */
        backlogSelect.empty();

        result.backlogData.forEach(p => {

            const option = new Option(p.backlogI_title, p.backlogI_id);
            backlogSelect.append(option);
        });

        /* ✅ initialize once cleanly */
        backlogSelect.select2({
            placeholder: "Select backlog items",
            width: '100%',
            dropdownParent: $('#sprintModal')
        });

        if (pendingBacklogIds) {
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

    if (result.status !== "Success") {
        console.log("Backlog Retrive Failed");
    }
    return result;
}

//Fetch the backlog data when the page loaded
document.addEventListener("DOMContentLoaded",
        async function getBoardData() {

            const data = {
                action: "fetchSprint",
                project_id: project_id
            };

            try {
                const result = await sendData_Sprint(data);

                result.SprintData.forEach(item => {
                    renderBoard(item);
                });
            } catch (err) {
                console.error("Update Failed:", err);
                alert("Failed to save change");
            }
        });

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


    if (isEditMode) {
        sprint_id = document.getElementById('editBoardId').value;
        data.action = "Update";
        data.sprint_id = sprint_id;
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

async function deleteBoard(sprint_Id) {
    data = {
        action: "Delete",
        sprint_id: sprint_Id
    };

    const response = await sendData_Sprint(data);

    if (response.status === "Success") {
        const board = document.getElementById(sprint_Id);
        board.remove();
    }
}

//to generate tehe UI Scrum Board
function renderBoard(data) {
    const board = document.createElement('div');
    board.className = 'scrum-board-card';
    board.id = data.sprint_id;
    board._backlogData = data.backlog;

    const backlogHTML = (data.backlog && data.backlog.length > 0)
            ? data.backlog.map(b => `
        <span class="backlog-chip">
            ${b.backlogI_title}
            <span class="badge bg-secondary ms-2">
                ${b.story_point} SP
            </span>
        </span>
    `).join('')
            : `<span class="text-muted small">No backlog assigned</span>`;

    const backlogIds = (data.backlog && data.backlog.length > 0)
            ? data.backlog.map(b => b.backlogI_id).join(',')
            : '';

    board.innerHTML = `
                    <div class="board-header">
                        <div class="board-info">
                            <h2 class="val-name h4 fw-bold text-primary">${data.sprint_name}</h2>
                            <span class="info-label mt-2">Description / Goal</span><p class="val-goal">${data.sprint_goal}</p>
                            <span class="info-label">Timeline</span><div class="date-row"><i class="far fa-calendar-alt text-primary"></i> <span class="val-start">${data.sprint_start_date}</span> to <span class="val-end">${data.sprint_end_date}</span></div>
                        </div>
                        <div class="text-end">
                            <span class="info-label">Status</span><div class="badge bg-primary-subtle text-primary val-status rounded-pill px-3 py-2 mb-2">${data.sprint_status}</div><br>
                            <button class="btn btn-outline-secondary btn-sm" onclick="editBoard('${data.sprint_id}')"><i class="fas fa-edit me-1"></i> Edit Details</button>
                            <button class="btn btn-outline-danger btn-sm" onclick="deleteBoard('${data.sprint_id}')">
                                        <i class="fas fa-trash-alt me-1"></i> Delete
                                    </button>
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
            ${['TO DO', 'IN PROGRESS', 'DONE'].map(c => `<div class="column" data-status="${c}" ondragover="allowDrop(event)" ondrop="drop(event)">
            <h3>${c}</h3><div class="task-list"></div><button class="btn text-muted btn-sm fw-bold w-100 text-start" onclick="addTask(this)">+ Add Task</button></div>`).join('')}
                    </div>`;
    document.getElementById('scrum-container').appendChild(board);

    insertTask_Board(data.sprint_id);
}

//#################################################### TASK SECTION #############################################################################

let isTaskEdit = false;

async function insertTask_Board(sprint_id) {
    const data = {
        action: "fetchTasks",
        sprint_id: sprint_id
    };
    console.log(data);

    const response = await fetch(`TaskServlet?action=fetchTasks_Sprint&sprint_id=${sprint_id}`);

    const result = await response.json();
    console.log(result.tasks);

    result.tasks.forEach((item) => {
        let i = 0;
        // 3. Create the task card visually
        const task = document.createElement('div');
        task.className = 'task';
        task.draggable = true;
        task.id = "task-" + item.task_id;
        task.dataset.dependency = (item.taskDepedencies || []).join(",");
        // 4. Set InnerHTML with Hyperlink
        // Note: draggable="false" on the <a> tag prevents drag conflict
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
        const board = document.getElementById(sprint_id);
        const columns = board.querySelectorAll('.column');

        const targetList = board.querySelector(
                `[data-status="${item.task_status}"] .task-list`
                );
        if (targetList) {
            targetList.appendChild(task);
        }

    });
}
;

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
    // Identify the target column and board
    const taskModel = document.querySelector("#taskModal");
    taskModel.querySelector("#taskModel_title").innerText = "Add Task";

    const subBtn = taskModel.querySelector("#taskModel_Sbt");
    document.getElementById("deleteTaskBtn").style.display = "none";

    boardId = btn.closest('.scrum-board-card').id;
    console.log("ID IN ADDTASK()", boardId);
    const columnHeader = btn.closest('.column').querySelector('h3').innerText;

    // Reset modal and set hidden context
    document.querySelectorAll('#taskModal input, #taskModal textarea').forEach(el => el.value = '');
    document.getElementById('t_board_id').value = boardId;
    document.getElementById('t_column_id').value = columnHeader;

    loadTaskDepenecy();

    bsTaskModal.show();
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


// Get assignee details after user selected role
document.getElementById("assignee").addEventListener("focus", async function () {

    const user_role = document.getElementById("t_user_role").value;


    const response = await fetch(`TaskServlet?action=fetch_user&project_id=${project_id}&user_role=${user_role}`);

    const assignee = document.getElementById("assignee");

    console.log(response);

    if (!response.ok) {
        throw new Error(response.status);
    }

    const result = await response.json();

    assignee.innerHTML = "";

    result.userData.forEach((d) => {
        const option = document.createElement("option");
        option.value = d.user_id;
        option.textContent = d.username;

        assignee.append(option);
    });

    if (!result.userData || result.userData.length === 0) {
        assignee.innerHTML = "";

        const option = document.createElement("option");
        option.textContent = "No user found";
        option.disabled = true;
        option.selected = true;

        assignee.append(option);
    }
});


async function confirmAddTask() {
    
    console.log("confirmAddTask");
    
    const data = {
        action: "Insert",
        task_name: document.getElementById('t_name').value,
        task_desc: document.getElementById('t_desc').value,
        assignee: document.getElementById('assignee').value,
        task_start_date: document.getElementById('t_start').value,
        task_end_date: document.getElementById('t_end').value,
        sprint_Id: document.getElementById('t_board_id').value,
        task_status: document.getElementById('t_column_id').value,
        task_assigned_to: document.getElementById('assignee').value,
        task_assigned_by: user_id,
        taskDepedencies: $('#t_dependency').val().map(Number) || null
    };
   
    console.log("Task dependency :" + data.taskDepedencies);
    let task_id = null;
    if (isTaskEdit) {
        data.action = "UpdateTaskDetials";

        task_id = document.getElementById("task_id").value.replace("task-", "");
        ;
        console.log("Task id", task_id);
        data.task_id = task_id;

        const oldTask = document.getElementById("task-" + task_id);
        console.log("Task old :", oldTask);
        if (oldTask)
            oldTask.remove();
    }

    const result = await sendData_Task(data);

    if (result.status !== "Success") {
        bsTaskModal.hide();
        return;
    }


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

function drag(ev) {

    const task = ev.target;
    ev.dataTransfer.setData("taskId", task.id);
    task.dataset.oldStatus = task.closest('.column').dataset.status;
}

async function drop(ev) {

    ev.preventDefault();

    const taskId = ev.dataTransfer.getData("taskId").replace("task-", "");
    ;
    const task = document.getElementById("task-" + taskId);

    console.log("Task :", task);

    const newColumn = ev.target.closest('.column');
    if (!newColumn)
        return;

    const newStatus = newColumn.dataset.status;
    console.log(newStatus);
    const oldStatus = task.dataset.oldStatus;
    console.log(oldStatus);

    // optimistic UI move
    newColumn.querySelector('.task-list').appendChild(task);

    if (oldStatus === newStatus)
        return;

    try {

        const data = {
            action: "updateTaskStatus",
            task_id: taskId,
            task_status: newStatus
        };

        const result = await sendData_Task(data);

        if (result.status !== "Success") {
            throw new Error("Update failed");
        }

    } catch (err) {

        const oldColumn = document.querySelector(
                `[data-status="${oldStatus}"] .task-list`
                );

        if (oldColumn) {
            oldColumn.appendChild(task);
        }
        alert("Update failed, reverting change");
    }
}

async function getTaskDetails(task_id) {

    console.log("getTaskDetails Execute");
    console.log("Task Id : ", task_id);

    const response = await fetch(`TaskServlet?action=fetchTask_Edit&task_id=${task_id}`);

    const result = await response.json();

    console.log("Result : ", result);
    console.log(typeof result);

    const taskModel = document.querySelector("#taskModal");

    taskModel.querySelector("#taskModel_title").innerText = "Task Information";
    const subBtn = taskModel.querySelector("#taskModel_Sbt");
    subBtn.innerText = "Edit Task";
    subBtn.onclick = () => editTask();

//   Set Delete Btn
    const deletebtn = document.getElementById("deleteTaskBtn");
    deletebtn.style.display = "none";
    deletebtn.onclick = () => deleteTask(task_id);

    const task = document.getElementById("task-" + task_id);
    console.log("task during edit : " , task);
    const taskStatus = task.closest('.column').dataset.status;
    console.log("Task Status :", taskStatus);

    taskModel.querySelector("#t_column_id").value = taskStatus;

    const sprint_id = task.closest('.scrum-board-card').id;
    taskModel.querySelector("#t_board_id").value = sprint_id;

    console.log("Sprint id", sprint_id);
    const input = document.createElement("input");
    input.value = task_id;
    input.type = "hidden";
    input.id = "task_id";
    taskModel.append(input);

    taskModel.querySelector("#t_name").value = result.taskData.task_name;
    taskModel.querySelector("#t_desc").value = result.taskData.task_desc;
    taskModel.querySelector("#t_user_role").value = result.taskData.task_assigned_to_Role;

    const role = taskModel.querySelector("#t_user_role");
    role.value = result.taskData.taskAssignment.task_assigned_to_Role;

    const assignee = taskModel.querySelector("#assignee");
    const option = document.createElement("option");
    option.value = result.taskData.taskAssignment.task_assigned_to;
    option.textContent = result.taskData.taskAssignment.user_name;

    assignee.append(option);

//    board.querySelector("#assignee").value = ;
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

    result2.taskData.forEach(item => {

        const option = new Option(
                `TASK-${item.task_id} | ${item.task_name}`,
                item.task_id, );

        select.append(option);
    });

    select.trigger('change');

    $('#taskDependency').trigger('change');

    document.querySelectorAll("#taskModal input, #taskModal textarea, #taskModal select")
            .forEach(el => el.disabled = true);

    document.getElementById("deleteTaskBtn").style.display = "none";
    bsTaskModal.show();
}


async function deleteTask(task_id) {

    data = {
        action: "deleteTask",
        task_id: task_id
    };

    const result = await sendData_Task(data);

    if (result.status === "Success") {
        const oldTask = document.getElementById("task-" + task_id);
        console.log("Task old :", oldTask);
        if (oldTask)
            oldTask.remove();
    }
    
    
    bsTaskModal.hide();
}

function editTask() {

    document.querySelectorAll("#taskModal input, #taskModal textarea, #taskModal select")
            .forEach(el => el.disabled = false);

    const taskModel = document.querySelector("#taskModal");
    taskModel.querySelector("#taskModel_title").innerText = " Edit Task";
    const subBtn = taskModel.querySelector("#taskModel_Sbt");
    subBtn.innerText = "Edit Task";
    subBtn.onclick = () => confirmAddTask();

    document.getElementById("deleteTaskBtn").style.display = "block";


    isTaskEdit = true;
}


//
//// Edit for task
//function switchToEditMode() {
//    document.getElementById('v_modal_title').innerText = "Update Task";
//
//    // Enable all inputs
//    document.querySelectorAll('.view-mode').forEach(el => {
//        el.removeAttribute('readonly');
//        el.removeAttribute('disabled'); // For the select dropdown
//        el.classList.replace('bg-light', 'bg-white');
//        el.classList.add('border'); // Give visual cue it's editable
//    });
//
//    // Swap Buttons
//    document.getElementById('viewActions').classList.add('d-none');
//    document.getElementById('editActions').classList.remove('d-none');
//}

//
//function switchToViewMode() {
//    document.getElementById('v_modal_title').innerText = "Task Details";
//
//    document.querySelectorAll('.view-mode').forEach(el => {
//        el.setAttribute('readonly', true);
//        el.setAttribute('disabled', true);
//        el.classList.replace('bg-white', 'bg-light');
//        el.classList.remove('border');
//    });
//    document.getElementById('viewActions').classList.remove('d-none');
//    document.getElementById('editActions').classList.add('d-none');
//}
//
//function disableTaskEdit() {
//    // Restore readonly state
//    document.querySelectorAll('#viewTaskModal .form-control').forEach(el => {
//        el.setAttribute('readonly', true);
//        el.classList.add('border-0', 'bg-light');
//    });
//    document.getElementById('viewModeButtons').classList.remove('d-none');
//    document.getElementById('editModeButtons').classList.add('d-none');
//}