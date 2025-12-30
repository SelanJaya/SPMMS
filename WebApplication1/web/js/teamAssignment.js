/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


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
//    // 2. The Engine to fetch users
//    async function loadUsersByRole(roleName) {
//        try {
//            // FIX: Added '?' and 'userRole='
////                        const response = await fetch(`projectAssignmentServlet?userRole=`);
//            const response = await fetch(`teamAssignmentServlet?roleType=${encodeURIComponent(roleName)}`);
//            const result = await response.json();
//            if (result.success) {
//                employeeData = result.data; // Update cache
//                displayUsers(result.data); // Render table
//                console.log(`Cargo Arrived: ${result.count} users.`);
//            } else {
//                alert("Error: " + result.message);
//            }
//        } catch (error) {
//            console.error("Network/System Error:", error);
//        }
//    }

    async function loadUsersByRole(roleName) {
        try {
            const response = await fetch(`teamAssignmentServlet?roleType=${encodeURIComponent(roleName)}`);

            if (!response.ok)
                throw new Error("Network response was not ok");

            const result = await response.json();

            if (result.success) {
                console.log("Cargo Arrived:", result.count, "users.");

                // 1. Store the data globally so the 'input' event can search it later
                employeeData = result.data;

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

// DELETE MEMBER LOGIC
// Use delegation $(document).on('click', 'selector') to ensure it works with JSTL loops
$(document).on('click', '.btn-delete-member', function () {
    console.log("Delete Button Clicked");

    // 1. Extract data from the clicked button using jQuery .data()
    // Note: jQuery handles 'data-user-id' as 'user-id'
    const userId = $(this).attr('data-user-id');
    const projectId = $(this).attr('data-project-id');

    console.log("Target User ID:", userId);
    console.log("Target Project ID:", projectId);

    // 2. Find the username in the parent card to show in the modal
    const userName = $(this).closest('.team-card').find('.fw-bold').first().text();

    // 3. Inject data into the Modal's hidden inputs
    $('#modalUserId').val(userId);
    $('#modalProjectId').val(projectId);

    // 4. Update the text in the modal to show the name
    $('#displayUserName').text(userName);
});




//
//document.addEventListener('DOMContentLoaded', function () {
//    // 1. Select all delete buttons
//    const deleteButtons = document.querySelectorAll('.btn-delete-member');
//    console.log("ATACHED");
//
//    deleteButtons.forEach(button => {
//        button.addEventListener('click', function () {
//            // 2. Extract data from the clicked button's data attributes
//            const userId = this.getAttribute('data-user-id');
//            const projectId = this.getAttribute('data-project-id');
//
//            console.log("UserId : " + userId);
//            // 3. Populate the Modal form fields
//            document.getElementById('modalUserId').value = userId;
//            document.getElementById('modalProjectId').value = projectId;
//        });
//    });
//});

// Add this to your teamAssignment.js
//    $(document).on('click', '.btn-delete-member', function () {
//        console.log("ATTACHED");
//        // Extract data from button attributes
//        const userId = $(this).data('user-id');
//        const projectId = $(this).data('project-id');
//
//        // Find the username in the card to show a confirmation message
//        const userName = $(this).closest('.team-card').find('.fw-bold').text();
//
//        // Inject data into the Modal's hidden inputs
//        $('#modalUserId').val(userId);
//        $('#modalProjectId').val(projectId);
//        $('#displayUserName').text(userName);
//    });


