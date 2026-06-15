/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

document.addEventListener("DOMContentLoaded", async function () {
    console.log("Success");

    const response = await fetch(`DashboardServlet?processType=achivedProject`);
    const result = await response.json();

    console.log(result);
    populateArchiveTable(result);
});

function populateArchiveTable(result) {

    const vaultBodyDiv = document.getElementById("vaultBody");

    let tableData = "";

    result.profileInfo.forEach(item => {

        tableData += `
            <tr id="${item.projectId}" class="vault-row" data-search="${item.projectName.toLowerCase()} ${item.projectDesc.toLowerCase()}">
                <td>
                    <a href="ProjectPageServlet?action=redirect&project_id=${item.projectId}" class="project-title-link">
                        <i class="fas fa-file-invoice text-muted me-2"></i>${item.projectName}
                    </a>
                </td>
                <td>
                    <div class="description-snippet" ondblclick="toggleDescription(this)" title="Double-click to expand">
                        ${item.projectDesc}
                    </div>
                </td>
                <td>
                    <i class="far fa-calendar-alt text-muted me-2"></i> ${item.projCreatedAt}
                </td>
                <td>
                    <div class="dropdown">
                        <button class="btn btn-sm btn-light border dropdown-toggle fw-bold" type="button" data-bs-toggle="dropdown">
                            Manage
                        </button>
                        <ul class="dropdown-menu shadow-sm">
                            <li>
                                <a href="ProjectPageServlet?action=redirect&project_id=${item.projectId}" class="dropdown-item">
                                    <i class="fas fa-folder-open text-primary me-2"></i> Open Vault
                                </a>
                            </li>
                            <li>
                               <button class="dropdown-item restore-btn"
                                        data-project-id="${item.projectId}"
                                        data-project-name="${item.projectName}"
                                        data-project-end-date="${item.projEndDate}">
                                    <i class="fas fa-undo-alt text-success me-2"></i>
                                    Restore Project
                               </button>
                            </li>
                        </ul>
                    </div>
                </td>
            </tr>`;
    });
    vaultBodyDiv.innerHTML = tableData;
}


document.getElementById("vaultBody").addEventListener("click", function (e) {
    console.log("executed");

    const restoreBtn = e.target.closest(".restore-btn");


    if (restoreBtn) {
        console.log("restorBtn clicked");
        const restoreFolderModalDOM = document.getElementById("restoreFolderModal");
        const restoreFolderModal = bootstrap.Modal.getOrCreateInstance(restoreFolderModalDOM);

        document.getElementById("displayEndDate").innerText = restoreBtn.dataset.projectEndDate;
        document.getElementById("displayRestoreName").innerText = restoreBtn.dataset.projectName;
        restoreFolderModalDOM.dataset.project_id = restoreBtn.dataset.projectId;

        restoreFolderModal.show();
    }

});

document.getElementById("confirmRestoreBtn").addEventListener("click", async function () {

    const isValid = validateRestoreDate();

    if (!isValid) {
        return;
    }

    const restoreFolderModalDOM = document.getElementById("restoreFolderModal");
    const restoreFolderModal = bootstrap.Modal.getOrCreateInstance(restoreFolderModalDOM);

    const data = {
        action: "restoreArchivedProject",
        projectId: restoreFolderModalDOM.dataset.project_id,
        projEndDate: document.getElementById("newDateInput").value
    };

    const result = await sendData_ArchiveProject(data);

    if (result.status === "Success") {
        document.getElementById(`${data.projectId}`).classList.add("d-none");
    }

    restoreFolderModal.hide();
});

function validateRestoreDate() {

    const newDateInput = document.getElementById("newDateInput");
    const errorMsg = document.getElementById("errorRestoreDate");

    const selectedDate = new Date(newDateInput.value);
    const today = new Date();

    // Remove time portion
    today.setHours(0, 0, 0, 0);

    if (!newDateInput.value) {
        errorMsg.innerText = "Please select a new end date.";
        errorMsg.classList.remove("d-none");
        return false;
    }

    if (selectedDate <= today) {
        errorMsg.innerText = "End date must be after today.";
        errorMsg.classList.remove("d-none");
        return false;
    }

    errorMsg.classList.add("d-none");
    errorMsg.innerText = "";
    return true;
}

document.getElementById("newDateInput").addEventListener("input", function () {

    const errorMsg = document.getElementById("errorRestoreDate");

    errorMsg.classList.add("d-none");
    errorMsg.innerText = "";
});

async function sendData_ArchiveProject(data) {
    try {
        const response = await fetch("ProjectPageServlet", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            throw new Error(`HTTP Error: ${response.status}`);
        }

        return await response.json();

    } catch (error) {
        console.error("Restore Project Error:", error);

        return {
            status: "Failed",
            message: error.message
        };
    }
}


let currentId = null;
let currentMode = '';
//const actionModal = new bootstrap.Modal('#actionModal');

// Search Filter
$('#vaultSearch').on('keyup', function () {
    let val = $(this).val().toLowerCase();
    $('.vault-row').each(function () {
        $(this).toggle($(this).attr('data-search').indexOf(val) > -1);
    });
});


document.addEventListener('DOMContentLoaded', function () {

    // Dedicated Execute Action for Delete project 
    function executeFolderDeletion(btn, projectId, method, initialId, successId, redirect) {
        const originalContent = btn.innerHTML;

        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Processing...';
        btn.disabled = true;

        $.ajax({
            url: 'projectPageServlet',
            type: 'GET',
            data: {
                processType: method,
                projectId: projectId
            },
            dataType: 'json',
            success: function (response) {
                if (response.success) {
                    document.getElementById(initialId).classList.add('d-none');
                    document.getElementById(successId).classList.remove('d-none');
                    setTimeout(() => {
                        window.location.href = redirect;
                    }, 2000);
                } else {
                    alert("Error: " + response.message);
                    btn.innerHTML = originalContent;
                    btn.disabled = false;
                }
            },
            error: function () {
                alert("Network Error: Could not reach server.");
                alert(
                        "Network Error: Could not reach server.\n" +
                        "Status: " + status + "\n" +
                        "Error: " + error + "\n" +
                        "Request Data: " + JSON.stringify(requestData) + "\n" +
                        "Response Text: " + xhr.responseText
                        );
                btn.innerHTML = originalContent;
                btn.disabled = false;
            }
        });
    }

    // 2. THE HANDSHAKE (Listens for any modal to open)
    document.addEventListener('show.bs.modal', function (event) {
        const triggerBtn = event.relatedTarget;
        if (!triggerBtn)
            return;

        const projectId = triggerBtn.getAttribute('data-project-id');
        const projectName = triggerBtn.getAttribute('data-project-name');
        const projectStatus = triggerBtn.getAttribute('data-project-status');

        const modalId = event.target.getAttribute('id');

        // Handle Delete Modal (GET)
        if (modalId === 'deleteFolderModal') {
            document.getElementById('displayProjectName').textContent = projectName;
            document.getElementById('initialBody').classList.remove('d-none');
            document.getElementById('successBody').classList.add('d-none');

            document.getElementById('deleteBtn').onclick = function () {
                executeFolderDeletion(this, projectId, 'deleteFolder', 'initialBody', 'successBody', 'dashboardServlet?processType=projectInfo');
            };
        }


        if (modalId === 'restoreFolderModal') {
            // 1. Extract multiple details from the button
            const projectId = triggerBtn.getAttribute('data-project-id');
            const projectName = triggerBtn.getAttribute('data-project-name');
//                        const userId = triggerBtn.getAttribute('data-user-id'); // Assuming you added this to the button
//                        const projectTag = triggerBtn.getAttribute('data-tag');     // Assuming you added this to the button

            document.getElementById('displayRestoreName').textContent = projectName;
            document.getElementById('restoreInitialBody').classList.remove('d-none');
            document.getElementById('restoreSuccessBody').classList.add('d-none');

            document.getElementById('confirmRestoreBtn').onclick = function () {

                // 3. Pass the object to the engine
                executeRestoreFolder(this, projectId, 'updateStatus', 'restoreInitialBody', 'restoreSuccessBody', 'projectPageServlet?processType=updateStatus');
            };
        }
    });

    // Funtion for restore project
    function executeRestoreFolder(btn, projectId, method, initialId, successId, redirect) {
        const originalContent = btn.innerHTML;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Processing...';
        btn.disabled = true;

        $.ajax({
            url: 'projectPageServlet',
            type: 'POST',
            data: {
                processType: "updateStatus",
                projectId: projectId,
            },
            dataType: 'json',
            success: function (response) {
                // 1. JS reads the 'success' boolean from your Servlet's JSON
                if (response.success === true) {

                    // 1. Find the current open modal
                    const currentModalEl = document.querySelector('.modal.show');

                    if (currentModalEl) {
                        // Attempt to get the existing instance
                        let currentModal = bootstrap.Modal.getInstance(currentModalEl);

                        // FIX: If instance is null, initialize it manually before hiding
                        if (!currentModal) {
                            currentModal = new bootstrap.Modal(currentModalEl);
                        }

                        currentModal.hide();
                    }

                    // 2. Short delay for smooth transition, then show Decision Modal
                    setTimeout(() => {
                        const decisionModal = new bootstrap.Modal(document.getElementById('decisionModal'));
                        decisionModal.show();
                    }, 500);

                } else {
                    // Handle the 'false' reply from Servlet
                    alert("Error: " + response.message);
                    btn.disabled = false;
                    btn.innerHTML = "Retry Action";

                }
            },
            error: function (xhr, status, error) {
                // This runs if the Servlet crashes (500 error) or URL is wrong (404)
                console.error("Technical Error:", error);
                alert("Connection Error: Could not reach the server.");
                btn.disabled = false;
            }
        });
    }

    document.querySelectorAll('.dropdown').forEach(dropdown => {
        dropdown.addEventListener('show.bs.dropdown', function () {
            const menu = this.querySelector('.dropdown-menu');
            menu.style.opacity = '1';
            menu.style.transform = 'translateY(0)';
        });
    });
});

function toggleDescription(element) {
    element.classList.toggle('expanded');
}





//                                        <tr>
//                                            <td colspan="4" class="text-center py-5 text-muted">No archived projects found.</td>
//                                        </tr>

//
//<li><hr class="dropdown-divider"></li>
//                            <li>
//                                <button id="" class="dropdown-item text-danger"  
//                                    data-bs-toggle="modal" 
///                                    data-bs-target="#deleteFolderModal"
//                                    data-project-id="${//item.projectId}"
  //                                  data-project-name="${//item.projectName}">
    //                                <i class="fas fa-trash-alt me-2"></i> Delete Permanently
      //                          </button>
        //                    </li>