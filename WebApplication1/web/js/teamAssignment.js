/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

let selectedUser;
$(document).ready(function () {
    let employeeData = []; // global Cache for Live Search

    // 1. Triggered when Role changes
    $('#roleSelector').on('change', function () {
        const selectedRole = $(this).val();
        if (selectedRole) {
            loadUsersByRole(selectedRole);
            $('#employeeSearch').prop('disabled', false)
                    .attr('placeholder', 'Search ' + selectedRole + 's...');
        }
    });


    async function loadUsersByRole(roleName) {
        try {
            const response = await fetch(`teamAssignmentServlet?action=fetchUsers&roleType=${encodeURIComponent(roleName)}`);

            if (!response.ok)
                throw new Error("Network response was not ok");

            const result = await response.json();
            console.log(result);

            if (result.status === "Success") {
                console.log("Cargo Arrived:", result.count, "users.");

                // 1. Store the data globally so the 'input' event can search it later
                employeeData = result.userData;
                console.log(employeeData);

                // 2. Clear the search input and results list for the new role
                document.getElementById('employeeSearch').value = "";
                document.getElementById('searchSuggestions').classList.add('d-none');

                // Optional: Show a message saying 'X users found'
                console.log("Ready to search initials for role: " + roleName);
            }
        } catch (error) {
            console.error("Fetch error:", error);
        }
    }

    employeeSearch.addEventListener('focus', function () {
        if (employeeData.length > 0) {
            renderResultsList(employeeData); // show all employees for the selected role
        }
    });

    document.getElementById('employeeSearch').addEventListener('input', function (e) {
        const term = e.target.value.toLowerCase();

        // If empty, show all users
        const filtered = term.length < 1 ? employeeData : employeeData.filter(user =>
            user.username.toLowerCase().includes(term) ||
                    user.email.toLowerCase().includes(term)
        );

        renderResultsList(filtered);
    });

    function renderResultsList(users) {
        const container = document.getElementById('searchSuggestions');
        if (!container)
            return;

        container.innerHTML = "";

        if (users.length === 0) {
            container.innerHTML = '<div class="list-group-item text-muted small">No matches found.</div>';
        } else {
            users.forEach(user => {
                const item = document.createElement("div");
                // Use 'list-group-item-action' to help Bootstrap handle the hover
                item.className = "list-group-item list-group-item-action";
                item.innerHTML = `
                <div class="d-flex flex-column">
                    <span class="fw-bold" style="font-size: 0.9rem;">${user.username}</span>
                    <span class="text-muted" style="font-size: 0.75rem;">${user.email}</span>
                </div>
            `;

                item.onclick = () => {
                    selectedUser = {
                        username: user.username,
                        email: user.email,
                        user_role: document.getElementById('roleSelector').value,
                        assign_to : user.user_id
                    };
                    
                    console.log("Clicked data", selectedUser);
                    document.getElementById('employeeSearch').value = user.username;
                    console.log("user id", user_id);
                    document.getElementById('finalAssigntoEmail').value = user.email;
                    container.classList.add('d-none'); // Hide after selection
                    window.selectedUserId = user.user_id; // Store for the Invite button
                };

                container.appendChild(item);
            });
        }

        // FINAL STEP: Ensure it is visible
        container.classList.remove('d-none');
    }

    function selectUser(user) {
        document.getElementById('employeeSearch').value = user.username;
        document.getElementById('searchSuggestions').classList.add('d-none');

        // Set global variable for the final invite send
        selectedUserId = user.user_id;
        console.log("Selected User ID:", selectedUserId);
    }

    // 5. Selection Logic
    $(document).on('click', '.select-emp', function () {
        const email = $(this).data('email');
        const userId = $(this).data('user_id');
        const name = $(this).data('name');
        $('#employeeSearch').val(name);
        $('#finalUserInput').val(userId);
        $('#searchSuggestions').addClass('d-none');
    });
});

document.addEventListener("DOMContentLoaded", populateAssignedUser());

async function populateAssignedUser() {

    const response = await fetch(`teamAssignmentServlet?action=fetchTeamAssignment&project_id=${project_id}`);
    const result = await response.json();

    console.log(result);

    result.userAssignedData.forEach((item) => {
        console.log(item);
        appendUser_Role(item);
    });

}

const roleDiveMap = {
    "Project Manager": document.getElementById("projectManager_div"),
    "Scrum Master": document.getElementById("scumMaster_div"),
    "Product Owner": document.getElementById("productOwner_div"),
    "Developer": document.getElementById("developer_div")
};

function cleanseAssignedData() {
    //to convert object to array
    Object.values(roleDiveMap).forEach((item) => {
        if (item) {
            item.innerHTML = "";
        }
    });
}

function appendUser_Role(data) {

    const selected_div = roleDiveMap[data.user_role];
    console.log(selected_div);
    const action = (user_role === "Project Manager" && data.user_role !== "Project Manager") ? `<button type="button"
            class="btn btn-danger btn-sm btn-delete-member">
        Delete
    </button>` : ``;

    if (selected_div) {
        createdDiv = document.createElement("div");
        createdDiv.className = "col-xl-3 col-md-6";
        createdDiv.id = data.user_id || data.assign_to;
        createdDiv.dataset.email = data.email;
        createdDiv.dataset.assignToUsername = data.username;
        createdDiv.innerHTML = `
                                <div class="team-card">

    <!-- Avatar -->
    <img src="https://ui-avatars.com/api/?name=${data.username}&background=eff6ff&color=2563eb"
         alt="${data.username}"
         class="team-avatar">

    <!-- User Info -->
    <div class="team-info">
        <div class="team-name">${data.username}</div>
        <div class="team-email">${data.email}</div>
    </div>

    <!-- Delete Button -->
    ${action}

</div>`;

        selected_div.appendChild(createdDiv);
    } else {
        console.log("unknown user Role");
    }
}
//
//document.querySelector(".btn-delete-member").addEventListener("click", function (e) {
//    const deleteMemberModalDOM = document.getElementById("deleteMemberModal");
//    const deleteMemberModal = bootstrap.Modal.getOrCreateInstance(deleteMemberModalDOM);
//
//    const username = e.target.closest(".team-card").querySelector(".team-name");
//    document.getElementById("deleteUsername").innerText = username;
//    document.getElementById("removalReason").innerText = "";
//
//    deleteMemberModal.show();
//});

let deleteDocModel;
document.addEventListener("click", (e) => {
    const deleteAssignmentbtn = e.target.closest(".btn-delete-member");

    if (deleteAssignmentbtn) {
        const capsule = e.target.closest(".col-xl-3");
        console.log(capsule);

        console.log("delete is clicked");
        deleteDocModel = document.getElementById("deleteMemberModal");
        deleteDocModel.dataset.id = capsule.id;
        console.log(capsule.id);
        deleteDocModel.dataset.email = capsule.dataset.email;

        const assignToUsername = capsule.dataset.assignToUsername;
        deleteDocModel.dataset.assigntoUsername = assignToUsername;
        document.getElementById("deleteUsername").innerText = assignToUsername;
        console.log(assignToUsername);
        if (!assignToUsername) {
            console.log("it is null T T ");
        }
        document.getElementById("removalReason").value = "";

        const deleteMemberModalDOM = document.getElementById("deleteMemberModal");
        const deleteMemberModal = bootstrap.Modal.getOrCreateInstance(deleteMemberModalDOM);
        
        deleteMemberModal.show();
    }
});


// DELETE MEMBER LOGIC
// Use delegation $(document).on('click', 'selector') to ensure it works with JSTL loops
document.getElementById("deleteCfmBtn").addEventListener("click", async function deleteAssignment(e) {
    console.log("Delete Button Clicked");

    const assign_to = deleteDocModel.dataset.id;
    const assigntoUsername = deleteDocModel.dataset.assigntoUsername;
    const assigntoEmail = deleteDocModel.dataset.email;
    const removalReason = document.getElementById("removalReason").value.trim();
    console.log(removalReason);

    // 2. Check for an empty string ("") instead of null
    if (removalReason === "") {
        const validationMessage = document.getElementById("validationMessage");
        validationMessage.innerText = "Please enter the rejection reason to proceed!";
        validationMessage.classList.remove("d-none");
        // Highlight the box red (optional, based on our previous setup)
        document.getElementById("removalReason").classList.add("is-invalid");
        // THIS will successfully block the rest of your code from running
        return;
    }

    const data = {
        action: "delete",
        project_id: project_id,
        assign_to: assign_to,
        asign_to_Email: assigntoEmail,
        assign_to_username: assigntoUsername,
        assign_by: user_id,
        removed_by: user_id,
        removal_reason: removalReason
    };

    console.log(data);
    const result = await sendData_Assigment(data);

    cleanseAssignedData();
    populateAssignedUser();

    const deleteDocModelBoot = bootstrap.Modal.getOrCreateInstance(deleteDocModel);
    deleteDocModelBoot.hide();
});

document.getElementById("removalReason").addEventListener("click", () => {
    const validationMessage = document.getElementById("validationMessage");
    validationMessage.classList.add("d-none");
    // Highlight the box red (optional, based on our previous setup)
    document.getElementById("removalReason").classList.remove("is-invalid");
});

async function sendData_Assigment(data) {

    const response = await fetch("teamAssignmentServlet", {
        method: "POST",
        headers: {
            "Content_Type": "application/json"
        },
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        throw new Error(response.status);
    }
    ;
    const result = await response.json();
    console.log(result);
    displayMessage(result.message, result.status);
    return result;
}


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

let inviteModal;
document.getElementById("inviteMemberSubmit_btn").addEventListener("click", async function handleSubmitForm() {

    console.log(document.getElementById("finalUserInput"));
    console.log(selectedUser);

    const data = {
        action: "teamAssignment",
        project_id: project_id,
        assign_to: selectedUser.assign_to,
        asign_to_Email: selectedUser.email,
        assign_to_username: selectedUser.username,
        assign_by: user_id
    };
    console.log(data);

    const result = await sendData_Assigment(data);

    data.user_role = document.getElementById("roleSelector").value;

    if (result.status === "Failed") {
        console.log("Insertion assigment failed");
    } else if (result.status === "Success") {
        console.log("Data assignto" , data.assignTo);
        appendUser_Role(selectedUser);
        console.log(selectedUser);
        selectedUser = "";
        console.log(selectedUser);
    }

    inviteModal = document.getElementById("inviteModal");
    const inviteModalBoot = bootstrap.Modal.getOrCreateInstance(inviteModal);
    inviteModalBoot.hide();

});

function prepareDeleteReason(data) {
    document.getElementById("usernameRemove").innerHTML = data.assign_to_username;

    const removeProjectMember = document.getElementById("removeProjectMember");
    removeProjectMember.dataset.data = data;
    const removeProjectMemberBoot = new bootstrap.Modal.getOrCreateInstance(removeProjectMember);
    removeProjectMemberBoot.show();
}

async function deleteAssignmentReason() {
    const removeProjectMember = document.getElementById("removeProjectMember");
    const data = removeProjectMember.dataset.data;
    data.reason = document.getElementById("rejectionReason").value;
    console.log(dara.reason);
    const removeProjectMemberBoot = new bootstrap.Modal.getOrCreateInstance(removeProjectMember);
    removeProjectMemberBoot.show();
}