/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

$(document).ready(function () {
    /**
     * Toggles the interface between View Mode and Edit Mode
     */

//    window.toggleEdit = function (enable) {
//        if (enable) {
//            $('.editable-field').not('#statusSelect').prop('readonly', false)
//                    .addClass('active-edit')
//                    .css('pointer-events', 'auto');
//            
//            $('#projNameInput').css({'border': '', 'padding-left': '0.75rem'});
//            $('#statusView').addClass('d-none');
//            $('#statusSelect').removeClass('d-none').addClass('active-edit');
//            $('#editActions').removeClass('d-none');
//            $('#editBtn, #deleteBtnHeader').addClass('d-none'); // Hide Delete button while editing
//        } else {
//            $('.editable-field').prop('readonly', true)
//                    .removeClass('active-edit')
//                    .css('pointer-events', 'none');
//            $('#projNameInput').css({'border': '1px solid transparent', 'padding-left': '0'});
//            $('#statusView').removeClass('d-none');
//            $('#statusSelect').addClass('d-none').removeClass('active-edit');
//            $('#editActions').addClass('d-none');
//            $('#editBtn, #deleteBtnHeader').removeClass('d-none');
//        }
//    };
    $('#editBtn').click(() => toggleEdit(true));

    $('#uploadModal').on('shown.bs.modal', function () {
        $(this).find('.modal-content').draggable({
            handle: ".modal-header",
            containment: "window"
        });
    });

    window.toggleEdit = function (enable) {
        const editableFields = document.querySelectorAll('.editable-field');
        const projNameInput = document.getElementById('projName');
        const editActions = document.getElementById('editActions');
        const editBtn = document.getElementById('editBtn');
        const deleteBtnHeader = document.getElementById('deleteBtnHeader');

        if (enable) {
            // Handle all editable fields except statusSelect
            editableFields.forEach(field => {
                if (field.id !== 'projStatus' && field.id !== 'projDate') {
                    field.readOnly = false;
                    field.classList.add('active-edit');
                    field.style.pointerEvents = 'auto';
                    console.log("Reached");
                }
            });

            // UI Adjustments for "Edit Mode"
            projNameInput.style.border = '';
            projNameInput.style.paddingLeft = '0.75rem';
            console.log("Reached2");


            editActions.classList.remove('d-none');

            // Hide primary buttons
            editBtn.classList.add('d-none');
            deleteBtnHeader.classList.add('d-none');

        } else {
            // Handle all editable fields (resetting)
            editableFields.forEach(field => {
                field.readOnly = true;
                field.classList.remove('active-edit');
                field.style.pointerEvents = 'none';
            });

            // UI Adjustments for "View Mode"
            projNameInput.style.border = '1px solid transparent';
            projNameInput.style.paddingLeft = '0';

            editActions.classList.add('d-none');

            // Show primary buttons
            editBtn.classList.remove('d-none');
            deleteBtnHeader.classList.remove('d-none');
        }
    };

    const dropZone = document.getElementById('dropZone');
    const fileInput = document.getElementById('actualFile');
    const fileInfo = document.getElementById('fileInfo');
    const fileNameDisplay = document.getElementById('selectedFileName');

    // Click to select
    dropZone.addEventListener('click', () => fileInput.click());

    // Handle file selection via input
    fileInput.addEventListener('change', function () {
        displayFile(this.files[0]);
    });

    // Drag and drop events
    ['dragover', 'dragleave', 'drop'].forEach(eventName => {
        dropZone.addEventListener(eventName, e => {
            e.preventDefault();
            e.stopPropagation();
        });
    });

    dropZone.addEventListener('dragover', () => dropZone.classList.add('dragover'));
    dropZone.addEventListener('dragleave', () => dropZone.classList.remove('dragover'));

    dropZone.addEventListener('drop', (e) => {
        dropZone.classList.remove('dragover');
        const files = e.dataTransfer.files;
        if (files.length > 0) {
            fileInput.files = files; // Assign dropped file to the hidden input
            displayFile(files[0]);
        }
    });

    function displayFile(file) {
        if (file) {
            fileInfo.classList.remove('d-none');
            fileNameDisplay.innerText = file.name;
            // Optionally auto-fill Label if empty
            if ($('#docLabel').val() === "") {
                $('#docLabel').val(file.name.split('.').slice(0, -1).join('.'));
            }
        }
    }

    window.executeFolderDeletion = function (btn) {
        // 1. Loading State on button
        const originalContent = btn.innerHTML;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Processing...';
        btn.disabled = true;
        // 2. AJAX Request to your Servlet
        $.ajax({
            url: 'projectPageServlet',
            type: 'GET',
            data: {
                processType: 'deleteFolder',
                projectId: projectId
            },
            dataType: 'json',
            success: function (response) {
                if (response.success) {
                    // 3. SUCCESS UI SWAP
                    // Hide the header and footer area
                    document.getElementById('initialBody').classList.add('d-none');
                    // Show the success checkmark body
                    document.getElementById('successBody').classList.remove('d-none');
                    // 4. Final Redirect after 2 seconds
                    setTimeout(() => {
                        window.location.href = "dashboardServlet?processType=projectInfo";
                    }, 2000);
                } else {
                    alert("DELETION FAILED: " + response.message);
                    btn.innerHTML = originalContent;
                    btn.disabled = false;
                }
            },
            error: function (xhr, status, error) {
                alert("Network Error: Could not reach the server.");
                btn.innerHTML = originalContent;
                btn.disabled = false;
            }
        });
    };

    window.handleModalUpload = function () {
        // 1. You can add validation here
        const label = $('#docLabel').val();
        const file = $('#actualFile').val();

        // More robust check for the file object
        if (!label || fileInput.files.length === 0) {
            alert("Please provide both a label and a file.");
            return;
        }

        // 2. Call your existing upload function
        addFile();

        // 3. Close the modal after triggering upload
        bootstrap.Modal.getInstance(document.getElementById('uploadModal')).hide();

        // 4. Clear the form for next time
        document.getElementById('uploadForm').reset();
    };

    window.toggleArchiveWarning = function (val) {
        const notice = document.getElementById('archiveNotice');
        // We change this to 'Archive' to match your <option value="Archive">
        if (val === 'Archive') {
            notice.classList.remove('d-none');
        } else {
            notice.classList.add('d-none');
        }
    };

    const tx = document.getElementsByTagName("textarea");
    for (let i = 0; i < tx.length; i++) {
        tx[i].setAttribute("style", "height:" + (tx[i].scrollHeight) + "px;overflow-y:hidden;");
        tx[i].addEventListener("input", OnInput, false);
    }

    function OnInput() {
        this.style.height = "auto";
        this.style.height = (this.scrollHeight) + "px";
    }
});


