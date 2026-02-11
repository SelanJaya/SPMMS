/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


// JS logic for Search, Drag&Drop, Edit, and Save (remains identical to your established logic)
let isEditMode = false;
const bsModal = new bootstrap.Modal(document.getElementById('sprintModal'));
function openModal() {
    isEditMode = false;
    document.getElementById('modalTitle').innerText = "Initialize Project";
    document.querySelectorAll('#sprintModal input, #sprintModal textarea').forEach(el => el.value = '');
    bsModal.show();
}
document.getElementById('boardSearch').addEventListener('input', function (e) {
    const term = e.target.value.toLowerCase();
    document.querySelectorAll('.scrum-board-card').forEach(board => {
        const text = board.innerText.toLowerCase();
        board.classList.toggle('hidden-board', !text.includes(term));
    });
});
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


function editBoard(boardId) {
    isEditMode = true;
    const board = document.getElementById(boardId);
    document.getElementById('editBoardId').value = boardId;
    document.getElementById('modalTitle').innerText = "Edit Project Details";
    document.getElementById('m_name').value = board.querySelector('.val-name').innerText;
    document.getElementById('m_goal').value = board.querySelector('.val-goal').innerText;
    document.getElementById('m_start').value = board.querySelector('.val-start').innerText;
    document.getElementById('m_end').value = board.querySelector('.val-end').innerText;
    document.getElementById('m_status').value = board.querySelector('.val-status').innerText;
    document.getElementById('m_review').value = board.querySelector('.val-review').innerText;
    document.getElementById('m_retro').value = board.querySelector('.val-retro').innerText;
    bsModal.show();
}
function saveBoard() {
    const d = {name: document.getElementById('m_name').value, goal: document.getElementById('m_goal').value, start: document.getElementById('m_start').value, end: document.getElementById('m_end').value, status: document.getElementById('m_status').value, review: document.getElementById('m_review').value, retro: document.getElementById('m_retro').value};
    if (isEditMode) {
        const b = document.getElementById(document.getElementById('editBoardId').value);
        b.querySelector('.val-name').innerText = d.name;
        b.querySelector('.val-goal').innerText = d.goal;
        b.querySelector('.val-start').innerText = d.start;
        b.querySelector('.val-end').innerText = d.end;
        b.querySelector('.val-status').innerText = d.status;
        b.querySelector('.val-review').innerText = d.review;
        b.querySelector('.val-retro').innerText = d.retro;
    } else {
        renderNewBoard(d);
    }
    bsModal.hide();
}
function renderNewBoard(d) {
    const id = "board-" + Date.now();
    const board = document.createElement('div');
    board.className = 'scrum-board-card';
    board.id = id;
    board.innerHTML = `
                    <div class="board-header">
                        <div class="board-info">
                            <h2 class="val-name h4 fw-bold text-primary">${d.name}</h2>
                            <span class="info-label">Description / Goal</span><p class="val-goal">${d.goal}</p>
                            <span class="info-label">Timeline</span><div class="date-row"><i class="far fa-calendar-alt text-primary"></i> <span class="val-start">${d.start}</span> to <span class="val-end">${d.end}</span></div>
                        </div>
                        <div class="text-end">
                            <span class="info-label">Status</span><div class="badge bg-primary-subtle text-primary val-status rounded-pill px-3 py-2 mb-2">${d.status}</div><br>
                            <button class="btn btn-outline-secondary btn-sm" onclick="editBoard('${id}')"><i class="fas fa-edit me-1"></i> Edit Details</button>
                        </div>
                    </div>
                    <div class="notes-grid">
                        <div class="note-box"><span class="info-label">Review Notes</span><div class="note-content val-review">${d.review || 'None'}</div></div>
                        <div class="note-box"><span class="info-label">Retrospective</span><div class="note-content val-retro">${d.retro || 'None'}</div></div>
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

// function enableTaskEdit() {
//     // Remove readonly and change background
//     document.querySelectorAll('#viewTaskModal .form-control').forEach(el => {
//         el.removeAttribute('readonly');
//         el.classList.remove('border-0', 'bg-light');
//     });
//     document.getElementById('viewModeButtons').classList.add('d-none');
//     document.getElementById('editModeButtons').classList.remove('d-none');
// }

function disableTaskEdit() {
    // Restore readonly state
    document.querySelectorAll('#viewTaskModal .form-control').forEach(el => {
        el.setAttribute('readonly', true);
        el.classList.add('border-0', 'bg-light');
    });
    document.getElementById('viewModeButtons').classList.remove('d-none');
    document.getElementById('editModeButtons').classList.add('d-none');
}  