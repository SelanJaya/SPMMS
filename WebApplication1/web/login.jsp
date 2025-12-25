<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Authentication | SPMMS Console</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <link rel="Login Style stylesheet" href="css/login.css">
    </head>

    <body>

        <div class="container-box" id="container">

            <div class="form-container signup-container">
                <form action="login_signUpServlet" method="post">

                    <input type="hidden" name="processType" value="signUp" >

                    <h4 class="fw-bold mb-1 w-100 text-dark">Create Account</h4>
                    <p class="text-muted small mb-4 w-100">Join the SPMMS network today.</p>

                    <div class="row w-100 g-2">
                        <div class="col-6">
                            <label class="label-style">Username</label>
                            <input type="text" name="username" id="username" class="form-control" placeholder="douglas_m">
                        </div>
                        <div class="col-6">
                            <label class="label-style">Phone</label>
                            <input type="text" name="phone_number" id="phone_number" class="form-control" placeholder="+60">
                        </div>
                    </div>

                    <div class="row w-100 g-2">
                        <div class="col-6">
                            <label class="label-style">Email</label>
                            <input type="email" name="email" id="email" class="form-control" placeholder="name@company.com">
                        </div>
                        <div class="col-6">
                            <label class="label-style">Role</label>
                            <select name="role" id="role" class="form-select">
                                <option selected disabled>Select role</option>
                                <option value="Project Manager">Project Manager</option>
                                <option value="Product Owner">Product Owner</option>
                                <option value="Scrum Master">Scrum Master</option>
                                <option value="Designer">Designer</option>
                                <option value="Tester">Tester</option>
                                <option value="Developer">Developer</option>
                            </select>
                        </div>
                    </div>

                    <div class="row w-100 g-2">
                        <div class="col-6 pass-group">
                            <label class="label-style">Password</label>
                            <input name="password" id="password" type="password" class="form-control pass-field">
                            <i class="fas fa-eye eye-toggle"></i>
                        </div>
                        <div class="col-6 pass-group">
                            <label class="label-style">Confirm</label>
                            <input name="confirmPassword" id="confirmPassword" type="password" class="form-control pass-field">
                            <i class="fas fa-eye eye-toggle"></i>
                        </div>
                    </div>

                    <button type="submit" class="btn-main">Register Account</button>
                </form>
            </div>

            <div class="form-container login-container">
                <form action="login_signUpServlet" method="post">
                    <input type="hidden" name="processType" value="login" >
                    <h4 class="fw-bold mb-1 w-100 text-dark">Welcome Back</h4>
                    <p class="text-muted small mb-4 w-100">Enter your credentials to continue.</p>

                    <% if (request.getAttribute("errorMessage") != null) {%>
                    <div class="error-container animate-fade-in">
                        <div class="d-flex align-items-center">
                            <div class="error-icon">
                                <i class="fas fa-circle-exclamation"></i>
                            </div>
                            <div class="ms-3">
                                <h6 class="mb-0 fw-bold text-dark" style="font-size: 0.85rem;">Authentication Failed</h6>
                                <p class="mb-0 small text-muted"><%= request.getAttribute("errorMessage")%></p>
                            </div>
                        </div>
                    </div>
                    <% }%>

                    <label class="label-style">Email Address</label>
                    <input name="email" id="email" type="email" class="form-control" placeholder="douglas@spmms.com" />

                    <div class="pass-group">
                        <label class="label-style">Password</label>
                        <input name="password" id="password" type="password" class="form-control pass-field" placeholder="********" />
                        <i class="fas fa-eye eye-toggle"></i>
                    </div>

                    <div class="w-100 d-flex justify-content-between my-2">
                        <div class="small text-muted"><input type="checkbox"> Remember me</div>
                        <a href="#" class="small text-primary text-decoration-none fw-bold">Forgot Password?</a>
                    </div>

                    <button class="btn-main">Login to Console</button>
                </form>
            </div>

            <div class="overlay-container">
                <div class="overlay">
                    <div class="overlay-panel overlay-left">
                        <h2 class="fw-bold"><i class="fas fa-layer-group text-primary me-2"></i>SPMMS</h2>
                        <p class="small opacity-75 mt-2">Manage your projects with enterprise-grade security.</p>
                        <button class="btn-ghost" id="signIn">Return to Login</button>
                    </div>
                    <div class="overlay-panel overlay-right">
                        <h2 class="fw-bold"><i class="fas fa-layer-group text-primary me-2"></i>SPMMS</h2>
                        <p class="small opacity-75 mt-2">Join the next generation of project monitoring.</p>
                        <button class="btn-ghost" id="signUp">Create Account</button>
                    </div>
                </div>
            </div>
        </div>

        <script>
            // 1. Panel Switching Logic
            const signUpButton = document.getElementById('signUp');
            const signInButton = document.getElementById('signIn');
            const container = document.getElementById('container');
            const body = document.body;

            signUpButton.addEventListener('click', () => {
                container.classList.add("active");
                body.classList.add("signup-active");
            });

            signInButton.addEventListener('click', () => {
                container.classList.remove("active");
                body.classList.remove("signup-active");
            });

            // 2. Custom Eye Toggle Logic
            document.querySelectorAll('.eye-toggle').forEach(eye => {
                eye.addEventListener('click', function () {
                    const input = this.previousElementSibling;
                    const type = input.getAttribute('type') === 'password' ? 'text' : 'password';
                    input.setAttribute('type', type);
                    this.classList.toggle('fa-eye');
                    this.classList.toggle('fa-eye-slash');
                });
            });

            // 3. Password Match Validation
            const signupForm = document.querySelector('.signup-container form');
            const password = signupForm.querySelector('input[name="password"]');
            const confirmPassword = signupForm.querySelector('input[name="confirmPassword"]');

            // Create error message element
            const errorDisplay = document.createElement('div');
            errorDisplay.className = 'text-danger small mt-1';
            errorDisplay.style.display = 'none';
            errorDisplay.innerText = "Passwords do not match!";
            confirmPassword.parentNode.appendChild(errorDisplay);

            signupForm.addEventListener('submit', (e) => {
                if (password.value !== confirmPassword.value) {
                    e.preventDefault(); // Stop form submission
                    errorDisplay.style.display = 'block';
                    confirmPassword.style.borderColor = '#dc3545';
                }
            });

            // Clear error while typing
            confirmPassword.addEventListener('input', () => {
                if (confirmPassword.value === password.value) {
                    errorDisplay.style.display = 'none';
                    confirmPassword.style.borderColor = '';
                }
            });
        </script>
    </body>

</html>