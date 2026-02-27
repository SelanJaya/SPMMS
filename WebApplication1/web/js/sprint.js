/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

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
    document.getElementById('editBoardId').value = boardId;
    document.getElementById('modalTitle').innerText = "Edit Project Details";
    document.getElementById('SprintSubBtn').innerText = "Edit Sprint",
            document.getElementById('s_name').value = board.querySelector('.val-name').innerText;
    document.getElementById('s_goal').value = board.querySelector('.val-goal').innerText;
    document.getElementById('s_start').value = board.querySelector('.val-start').innerText;
    document.getElementById('s_end').value = board.querySelector('.val-end').innerText;
//    document.getElementById('s_status').value = board.querySelector('.val-status').innerText;
    document.getElementById('s_review').value = board.querySelector('.val-review').innerText;
    document.getElementById('s_retro').value = board.querySelector('.val-retro').innerText;
    bsModal.show();
}


const sprintModalEl = document.getElementById('sprintModal');

//To fetch the TOP 5 Backlog item when the model is invoked
sprintModalEl.addEventListener("shown.bs.modal", async function () {
    console.log("CLICK SUCCESSFULL");
    const data = {
        action: "fetchBacklog",
        project_id: project_id
    };

    const backlogSelect = document.getElementById("s_backlog_links");
    try {
        const result = await sendData(data);
        console.log(result);

//            const select = document.getElementById("s_backlog_links");
        backlogSelect.innerHTML = "";

        result.backlogData.forEach(p => {
            const option = document.createElement("option");
            option.value = p.backlogI_id;
            option.textContent = p.backlogI_title;
            backlogSelect.appendChild(option);
            console.log("appended", p.backlogI_id);
        });

        // ✅ enhance dropdown AFTER append
        $('#s_backlog_links').select2({
            placeholder: "Select backlog items",
            width: '100%',
            dropdownParent: $('#sprintModal'),
            dropdownAutoWidth: true
        });
    } catch (err) {
        console.error("Update Failed:", err);
        alert("Failed to save change");
    }
});

//Common function to send custom data to servlet
async function sendData(data) {
    let method;

    console.log("sendData triggered");
    const response = await fetch("SprintServlet", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });
    console.log("REQUEST SEND");
    if (!response.ok) {
        throw new Error("Server error" + response.status);
    }

    const result = await response.json();

    if (result.status !== "Success") {
        console.log("Backlog Retrive Failed");
    }
    return result;
}

//Fetch the backlog data when  the page loaded
document.addEventListener("DOMContentLoaded",
        async function getBoardData() {

            const data = {
                action: "fetchSprint",
                project_id: project_id
            };

            try {

                const result = await sendData(data);

                result.SprintData.forEach(item => {
                    renderBoard(item);
                    console.log("sprint_end_date", item.sprint_end_date)
                });
            } catch (err) {
                console.error("Update Failed:", err);
                alert("Failed to save change");
            }

        });

//Function to create the Scrum board
async function saveBoard() {
    console.log("Trigered saveboard");
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
        backlogitems: document.getElementById('s_backlog_links').value
    };

    if (isEditMode) {
        console.log("Status ", isEditMode);
        sprint_id = document.getElementById('editBoardId').value;
        data.action = "Update";
        data.sprint_id = sprint_id;
    }

    console.log("Data ", data);

    const result = await sendData(data);
    data.sprint_id = result.sprint_id;


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

    const response = await sendData(data);

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
    board.innerHTML = `
                    <div class="board-header">
                        <div class="board-info">
                            <h2 class="val-name h4 fw-bold text-primary">${data.sprint_name}</h2>
                            <span class="info-label">Description / Goal</span><p class="val-goal">${data.sprint_goal}</p>
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
                    <div class="board-layout">
            ${['To Do', 'In Progress', 'Done'].map(c => `<div class="column" ondragover="allowDrop(event)" ondrop="drop(event)"><h3>${c}</h3><div class="task-list"></div><button class="btn text-muted btn-sm fw-bold w-100 text-start" onclick="addTask(this)">+ Add Task</button></div>`).join('')}
                    </div>`;
    document.getElementById('scrum-container').appendChild(board);
}


const bsViewModal = new bootstrap.Modal(document.getElementById('viewTaskModal'));

// Global Modal Instance
const taskModalElem = document.getElementById('viewTaskModal');
const bsTaskViewModal = new bootstrap.Modal(taskModalElem);

function viewTaskDetails(taskId) {
    // 1. Populate data (Replace with AJAX fetch later)
    document.getElementById('v_badge_id').innerText = "Task ID: " + taskId;
    // Example: fetch existing name from the card clicked
    const card = document.getElementById('task-' + taskId);
    document.getElementById('v_name').value = card.querySelector('.task-link').innerText;

    // 2. Always start in View Mode
    switchToViewMode();
    bsTaskViewModal.show();
}

// Edit for task
function switchToEditMode() {
    document.getElementById('v_modal_title').innerText = "Update Task";

    // Enable all inputs
    document.querySelectorAll('.view-mode').forEach(el => {
        el.removeAttribute('readonly');
        el.removeAttribute('disabled'); // For the select dropdown
        el.classList.replace('bg-light', 'bg-white');
        el.classList.add('border'); // Give visual cue it's editable
    });

    // Swap Buttons
    document.getElementById('viewActions').classList.add('d-none');
    document.getElementById('editActions').classList.remove('d-none');
}



function switchToViewMode() {
    document.getElementById('v_modal_title').innerText = "Task Details";

    document.querySelectorAll('.view-mode').forEach(el => {
        el.setAttribute('readonly', true);
        el.setAttribute('disabled', true);
        el.classList.replace('bg-white', 'bg-light');
        el.classList.remove('border');
    });
    document.getElementById('viewActions').classList.remove('d-none');
    document.getElementById('editActions').classList.add('d-none');
}



function disableTaskEdit() {
    // Restore readonly state
    document.querySelectorAll('#viewTaskModal .form-control').forEach(el => {
        el.setAttribute('readonly', true);
        el.classList.add('border-0', 'bg-light');
    });
    document.getElementById('viewModeButtons').classList.remove('d-none');
    document.getElementById('editModeButtons').classList.add('d-none');
}



function allowDrop(ev) {
    ev.preventDefault();
}
function drag(ev) {
    ev.dataTransfer.setData("elementId", ev.target.id);
    ev.target.classList.add('dragging');
}
function drop(ev) {
    ev.preventDefault();
    const id = ev.dataTransfer.getData("elementId");
    const elm = document.getElementById(id);
    if (!elm)
        return;
    elm.classList.remove('dragging');
    ev.target.closest('.column').querySelector('.task-list').appendChild(elm);
}

const bsTaskModal = new bootstrap.Modal(document.getElementById('taskModal'));

function addTask(btn) {
    // Identify the target column and board
    const boardId = btn.closest('.scrum-board-card').id;
    const columnHeader = btn.closest('.column').querySelector('h3').innerText;

    // Reset modal and set hidden context
    document.querySelectorAll('#taskModal input, #taskModal textarea').forEach(el => el.value = '');
    document.getElementById('t_board_id').value = boardId;
    document.getElementById('t_column_id').value = columnHeader;
    document.getElementById('t_status').value = columnHeader; // Sync status with column

    bsTaskModal.show();
}

function confirmAddTask() {
    const name = document.getElementById('t_name').value;
    const start = document.getElementById('t_start').value;
    const end = document.getElementById('t_end').value;
    const boardId = document.getElementById('t_board_id').value;
    const columnText = document.getElementById('t_column_id').value;

    // 1. Validate against Database Schema Requirements
    if (!name || !start || !end) {
        alert("Task Name, Start, and End dates are required by the system schema.");
        return;
    }

    // 2. Generate ID and Servlet URL
    const taskId = Date.now(); // This will be replaced by your DB Primary Key later
    const servletURL = `TaskDetailsServlet?taskId=${taskId}`;

    // 3. Create the task card visually
    const task = document.createElement('div');
    task.className = 'task';
    task.draggable = true;
    task.id = "task-" + taskId;

    // 4. Set InnerHTML with Hyperlink
    // Note: draggable="false" on the <a> tag prevents drag conflict
    task.innerHTML = `

    <a href="javascript:void(0)" 
       onclick="viewTaskDetails('${taskId}')"
       draggable="false" 
       class="fw-bold text-decoration-none text-primary task-link">
            ${name}
    </a>
            <div class="small text-muted mt-1" style="font-size: 10px;">
                <i class="far fa-calendar-check"></i> ${start} to ${end}
            </div>
        `;

    task.ondragstart = drag;

    // 5. Append to the correct column list
    const board = document.getElementById(boardId);
    const columns = board.querySelectorAll('.column');
    let targetList;

    columns.forEach(col => {
        if (col.querySelector('h3').innerText === columnText) {
            targetList = col.querySelector('.task-list');
        }
    });

    if (targetList) {
        targetList.appendChild(task);
        bsTaskModal.hide(); // Hide the Bootstrap modal
    }
}




// function switchToViewMode() {
//     document.getElementById('v_modal_title').innerText = "Task Details";

//     // Disable all inputs
//     document.querySelectorAll('.view-mode').forEach(el => {
//         el.setAttribute('readonly', true);
//         el.setAttribute('disabled', true);
//         el.classList.replace('bg-white', 'bg-light');
//         el.classList.remove('border');
//     });

//     // Swap Buttons back
//     document.getElementById('viewActions').classList.remove('d-none');
//     document.getElementById('editActions').classList.add('d-none');
// }


// function enableTaskEdit() {
//     // Remove readonly and change background
//     document.querySelectorAll('#viewTaskModal .form-control').forEach(el => {
//         el.removeAttribute('readonly');
//         el.classList.remove('border-0', 'bg-light');
//     });
//     document.getElementById('viewModeButtons').classList.add('d-none');
//     document.getElementById('editModeButtons').classList.remove('d-none');
// }