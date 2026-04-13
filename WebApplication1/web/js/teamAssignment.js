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

//                // 1. Store the data globally so the 'input' event can search it later
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
                        user_role: document.getElementById('roleSelector').value
                    };
                    document.getElementById('employeeSearch').value = user.username;

                    document.getElementById('finalUserInput').value = user.user_id; // <-- inject here
                    container.classList.add('d-none'); // Hide after selection
                    window.selectedUserId = user.user_id; // Store for the Invite button
                };

                container.appendChild(item);
            });
        }

        // FINAL STEP: Ensure it is visible
        container.classList.remove('d-none');
    }

//    function renderResultsList(users) {
//        const container = document.getElementById('searchSuggestions');
//        container.innerHTML = "";
//
//        if (users.length === 0) {
//            container.innerHTML = '<div class="list-group-item text-muted p-3">No matching employees found.</div>';
//        } else {
//            users.forEach(user => {
//                const item = document.createElement("div");
//                item.className = "list-group-item list-group-item-action";
//                item.innerHTML = `
//                <span class="suggestion-name">${user.username}</span>
//                <span class="suggestion-email">${user.email}</span>
//            `;
//
//                // Interaction: Select the user on click
//                item.onclick = () => {
//                    document.getElementById('employeeSearch').value = user.username;
//                    container.classList.add('d-none');
//                    window.selectedUserId = user.user_id; // Store for invitation
//                };
//
//                container.appendChild(item);
//            });
//        }
//        container.classList.remove('d-none');
//    }

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
    const action = (user_role === "Project Manager" && data.user_role !== "Project Manager" )? `<button type="button" class="btn-delete-member" data-bs-toggle="modal" 
                                                    data-bs-target="#deleteMemberModal" 
                                                    name="Delete" value="Delete"
                                                    > Delete
                                            </button>` : ``;

    if (selected_div) {
        createdDiv = document.createElement("div");
        createdDiv.className = "col-xl-3 col-md-6";
        createdDiv.id = data.user_id;
        createdDiv.innerHTML = `
                                
                                    <div class="team-card p-3 d-flex align-items-center position-relative">
                                        <img src="https://ui-avatars.com/api/?name=${data.username}&background=eff6ff&color=2563eb"
                                             class="avatar-md me-3" />

                                        <div class="flex-grow-1">
                                            <div class="fw-bold text-dark small">${data.username}</div>
                                            <div class="text-muted" style="font-size: 11px;">${data.email}</div>
                                        </div>
                                        ${action}
                                    </div>
                                 `;

        selected_div.appendChild(createdDiv);
    } else {
        console.log("unknown user Role");
    }
}

let deleteDocModel;
document.addEventListener("click", (e) => {
    const deleteAssignmentbtn = e.target.closest(".btn-delete-member");

    if (deleteAssignmentbtn) {
        const capsule = e.target.closest(".col-xl-3");

        console.log("delete is clicked");
        deleteDocModel = document.getElementById("deleteMemberModal");
        deleteDocModel.dataset.id = capsule.id;
    }
});


// DELETE MEMBER LOGIC
// Use delegation $(document).on('click', 'selector') to ensure it works with JSTL loops
document.getElementById("deleteCfmBtn").addEventListener("click", async function deleteAssignment(e) {
    console.log("Delete Button Clicked");
    console.log(e);

    const assign_to = deleteDocModel.dataset.id;

    const data = {
        action: "delete",
        project_id: project_id,
        assign_to: assign_to,
        assign_by: user_id
    };

    const result = await sendData_Assigment(data);

    cleanseAssignedData();
    populateAssignedUser();

    const deleteDocModelBoot = new bootstrap.Modal(deleteDocModel);
    deleteDocModelBoot.hide();


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

    return result;
}
let inviteModal;
document.getElementById("inviteMemberSubmit_btn").addEventListener("click", async function handleSubmitForm() {

    console.log(document.getElementById("finalUserInput"));
    const data = {
        action: "teamAssignment",
        project_id: project_id,
        assign_to: document.getElementById("finalUserInput").value,
        assign_by: user_id
    };

    const result = await sendData_Assigment(data);

    data.user_role = document.getElementById("roleSelector").value;

    if (result.status === "Failed") {
        console.log("Insertion assigment failed");
    } else if (result.status === "Success") {
        appendUser_Role(selectedUser);
        selectedUser = "";
        console.log(selectedUser);
    }

    inviteModal = document.getElementById("inviteModal");
    const inviteModalBoot = new bootstrap.ModalgetOrCreateInstance(inviteModal);
    ;
    inviteModalBoot.hide();

});