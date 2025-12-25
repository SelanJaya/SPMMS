<%-- 
    Document   : profile
    Created on : 25 Dec 2025, 3:27:06 pm
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

        <link rel="stylesheet" href="css\profile.css">
    </head>

    <body>
        <div id="wrapper">
            <nav id="sidebar">
                <div class="sidebar-brand">SPMMS CONSOLE</div>
                <div class="nav flex-column mt-3">
                    <a href="profile.jsp" class="nav-link active"><i class="fas fa-user-circle me-3"></i> Profile</a>
                    <a href="dashboard.jsp" class="nav-link "><i class="fas fa-chart-pie me-3"></i> Dashboard</a>
                </div>
                <div class="mt-auto">
                    <div class="nav-divider my-2 mx-3" style="border-bottom: 1px solid rgba(255, 255, 255, 0.1);"></div>
                    <a href="login_signUpServlet?processType=logOut" class="nav-link text-danger">
                        <i class="fas fa-sign-out-alt me-3"></i><span>Logout</span>
                    </a>
                </div>
            </nav>

            <div id="content-wrapper">
                <nav class="top-nav">
                    <div class="small text-muted">User Settings / <span class="fw-bold text-dark">Profile</span></div>
                    <img src="https://ui-avatars.com/api/?name=Douglas+McGee&background=2563eb&color=fff"
                         class="rounded-circle border" width="34">
                </nav>

                <main class="p-4">
                    <div class="profile-header-card shadow-sm">
                        <img src="https://ui-avatars.com/api/?name=Douglas+McGee&background=2563eb&color=fff"
                             class="rounded-circle border" width="100">
                        <div>
                            <h2 class="fw-bold mb-1">${user.username}</h2>
                            <p class="text-muted mb-0">${user.user_role}</p>
                        </div>
                    </div>

                    <div class="row g-4">
                        <div class="col-lg-8">
                            <div class="settings-card shadow-sm">
                                <div class="card-header-spmms">
                                    <span>Personal & Security Information</span>
                                    <button class="btn-action" id="editToggleBtn" onclick="toggleEditMode()">
                                        <i class="fas fa-edit me-2"></i>Edit Profile
                                    </button>
                                </div>
                                <div class="p-4">
                                    <form action="profileServlet" method="post" id="profileForm">
                                        <input type="hidden" name="processType" id="processType" value="editProfile">
                                        <div class="row mb-4">
                                            <div class="col-md-6">
                                                <label class="label-style">Username</label>
                                                <span class="display-text">${user.username}</span>
                                                <input type="text" name="username" id="username" class="form-control edit-input" value="${user.username}">
                                            </div>
                                            <div class="col-md-6">
                                                <label class="label-style">Phone Number</label>
                                                <span class="display-text">${user.phone_number}</span>
                                                <input type="text" name="phone_number" id="phone_number" class="form-control edit-input" value="${user.phone_number}">
                                            </div>
                                        </div>
                                        <div class="row mb-4">
                                            <div class="col-md-6">
                                                <label class="label-style">Email Address</label>
                                                <span class="display-text">${user.email}</span>
                                                <input type="email" name="email" id="email" class="form-control edit-input" value="${user.email}">
                                            </div>
                                            <div class="col-md-6">
                                                <label class="label-style">User Role</label>
                                                <span class="display-text">${user.user_role}</span>
                                                <select name="user_role" class="form-select edit-input">
                                                    <option value="Admin" ${user.user_role == 'Admin' ? 'selected' : ''}>Admin</option>
                                                    <option value="Project Manager" ${user.user_role == 'Project Manager' ? 'selected' : ''}>Project Manager</option>
                                                    <option value="Product Owner" ${user.user_role == 'Product Owner' ? 'selected' : ''}>Product Owner</option>
                                                    <option value="Scrum Master" ${user.user_role == 'Scrum Master' ? 'selected' : ''}>Scrum Master</option>
                                                    <option value="Designer" ${user.user_role == 'Designer' ? 'selected' : ''}>Designer</option>
                                                    <option value="Tester" ${user.user_role == 'Tester' ? 'selected' : ''}>Tester</option>
                                                    <option value="Developer" ${user.user_role == 'Developer' ? 'selected' : ''}>Developer</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="row edit-input mb-4">
                                            <div class="col-md-6 pass-group">
                                                <label class="label-style text-primary">New Password</label>
                                                <input type="password" class="form-control pass-input"
                                                       placeholder="Leave empty to keep current">
                                                <i class="fas fa-eye eye-toggle"></i>
                                            </div>
                                            <div class="col-md-6 pass-group">
                                                <label class="label-style text-primary">Confirm Password</label>
                                                <input type="password" class="form-control pass-input"
                                                       placeholder="Repeat new password">
                                                <i class="fas fa-eye eye-toggle"></i>
                                            </div>
                                        </div>
                                        <div class="edit-input text-end">
                                            <button type="submit" class="btn btn-success px-5 rounded-3 fw-bold">Save
                                                Changes</button>
                                        </div>

                                        <div class="profile-meta-footer text-muted small">
                                            <div>
                                                <i class="far fa-calendar-alt me-2"></i>Account Created: <strong> ${user.created_at}</strong>
                                            </div>
                                            <div>
                                                <i class="fas fa-history me-2"></i>Last Login: <strong>${loginTime}</strong>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-4">
                            <div class="settings-card border-danger-subtle shadow-sm">
                                <div class="card-header-spmms text-danger">Danger Zone</div>
                                <div class="p-4 text-center">
                                    <p class="text-muted small mb-4">Account deletion is irreversible. Your project logs and
                                        data will be wiped permanently.</p>
                                    <button class="btn btn-danger w-100 fw-bold py-2" data-bs-toggle="modal"
                                            data-bs-target="#deleteModal">Delete Account</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>

        <div class="modal fade" id="deleteModal" tabindex="-1" data-bs-backdrop="static">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content shadow-lg" id="modalContent">
                    <div class="modal-body p-5 text-center" id="initialBody">
                        <div class="danger-circle shadow-sm"><i class="fas fa-trash-alt"></i></div>
                        <h4 class="fw-bold mb-2">Permanently Delete Account?</h4>
                        <p class="text-muted mb-4">This action cannot be undone. You will lose all access to the SPMMS
                            Console.</p>
                        <div class="d-grid gap-3">
                            <button type="button" class="btn btn-danger py-2 fw-bold"
                                    onclick="processDeletion(this)">Confirm Deletion</button>
                            <button type="button" class="btn btn-light py-2 fw-bold border"
                                    data-bs-dismiss="modal">Cancel</button>
                        </div>
                    </div>
                    <div class="modal-body p-5 text-center d-none" id="successBody">
                        <div class="success-circle shadow-sm"><i class="fas fa-check"></i></div>
                        <h4 class="fw-bold mb-2">Account Deleted</h4>
                        <p class="text-muted mb-0">Your account has been successfully removed. Redirecting to login...</p>
                    </div>
                </div>
            </div>
        </div>

        <script>
            function toggleEditMode() {
                document.body.classList.toggle('edit-mode');
                const btn = document.getElementById('editToggleBtn');
                if (document.body.classList.contains('edit-mode')) {
                    btn.innerHTML = '<i class="fas fa-times me-2"></i>Cancel Edit';
                    btn.classList.add('btn-secondary');
                } else {
                    btn.innerHTML = '<i class="fas fa-edit me-2"></i>Edit Profile';
                    btn.classList.remove('btn-secondary');
                }
            }

            document.querySelectorAll('.eye-toggle').forEach(eye => {
                eye.addEventListener('click', function () {
                    const input = this.parentElement.querySelector('.pass-input');
                    const type = input.getAttribute('type') === 'password' ? 'text' : 'password';
                    input.setAttribute('type', type);
                    this.classList.toggle('fa-eye');
                    this.classList.toggle('fa-eye-slash');
                });
            });
//
//            function processDeletion(btn) {
//                btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Processing...';
//                btn.disabled = true;
//
//                setTimeout(() => {
//                    document.getElementById('initialBody').classList.add('d-none');
//                    document.getElementById('successBody').classList.remove('d-none');
//
//                    setTimeout(() => {
//                        window.location.href = 'login.html';
//                    }, 2000);
//                }, 1500);
//            }

            function processDeletion(btn) {
                // 1. Show Loading State
                const originalContent = btn.innerHTML;
                btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Processing...';
                btn.disabled = true;

                // 2. Prepare Data
                const formData = new URLSearchParams();
                formData.append('processType', 'deleteProfile');

                // 3. Single Fetch Request
                fetch('profileServlet', {
                    method: 'POST',
                    body: formData
                })
                        .then(response => {
                            // First check if the server responded (e.g., not a 404 or 500 error)
                            if (!response.ok) {
                                throw new Error("Network response was not ok");
                            }
                            return response.text(); // Get the "true" or "false" text from Java
                        })
                        .then(statusText => {
                            // 4. Check the actual boolean-string sent by Java
                            if (statusText.trim() === "true") {
                                // Success Logic: Swap UI elements
                                document.getElementById('initialBody').classList.add('d-none');
                                document.getElementById('successBody').classList.remove('d-none');

                                // Final Redirect
                                setTimeout(() => {
                                    window.location.href = 'login.jsp';
                                }, 2000);
                            } else {
                                // Failure Logic: Java sent "false"
                                alert("Deletion failed: The server could not complete the request.");
                                btn.disabled = false;
                                btn.innerHTML = originalContent;
                            }
                        })
                        .catch(error => {
                            // 5. Handle Network/Connection Errors
                            console.error('Error:', error);
                            alert("A connection error occurred. Please try again.");
                            btn.disabled = false;
                            btn.innerHTML = originalContent;
                        });
            }
        </script>
    </body>

</html>
