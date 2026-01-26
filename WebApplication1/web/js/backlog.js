///* 
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
// */


/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


$(document).ready(function () {
    const sidebar = document.getElementById('sidebar');
    const table = $('#backlogTable').DataTable({
        paging: false, info: false, searching: true,
        columnDefs: [{targets: 'no-sort', orderable: false}]
    });

    // 1. Sidebar Toggle
    function toggleSidebar() {
        sidebar.classList.toggle('collapsed');
        setTimeout(() => {
            table.columns.adjust().draw();
        }, 400);
    }
    sidebar.addEventListener('dblclick', toggleSidebar);
    $('#sidebarToggle').on('click', toggleSidebar);

    // 2. Drag & Drop Reordering
    Sortable.create(document.getElementById('sortableBody'), {
        handle: '.drag-handle',
        animation: 200,
        onEnd: () => {
            $('.priority-rank').each(function (i) {
                $(this).text(i + 1);
            });
            table.rows().invalidate().draw(false);
        }
    });

    typeof Sortable;

    // 3. Moveable Modal Setup
    $('#backlogDocModal').on('shown.bs.modal', function () {
        $(this).find('.modal-content').draggable({
            handle: ".modal-header",
            containment: "window"
        });
    });

    // 4. Document Manager: Tab & File Logic
    const dz = $('#modalDropZone'), fi = $('#modalFileField'), fn = $('#fileNameText'), preview = $('#filePreview');

    $('button[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
        const target = $(e.target).attr('data-bs-target');
        target === '#uploadPane' ? $('#confirmBtn').removeClass('d-none') : $('#confirmBtn').addClass('d-none');
    });

    dz.on('click', () => fi.click());
    fi.on('change', function () {
        if (this.files[0]) {
            fn.text(this.files[0].name);
            preview.removeClass('d-none');
            if ($('#docNameInput').val() === "") {
                $('#docNameInput').val(this.files[0].name.split('.').slice(0, -1).join('.'));
            }
        }
    });

    dz.on('dragover', (e) => {
        e.preventDefault();
        dz.addClass('bg-primary-subtle border-primary');
    });
    dz.on('dragleave', () => dz.removeClass('bg-primary-subtle border-primary'));
    dz.on('drop', (e) => {
        e.preventDefault();
        dz.removeClass('bg-primary-subtle border-primary');
        const files = e.originalEvent.dataTransfer.files;
        if (files.length > 0) {
            fi[0].files = files;
            fi.trigger('change');
        }
    });

    // 5. Add New Row (Logic preserved and optimized)
    $('#confirmAddBtn').on('click', function () {
        const title = $('#m_title').val();
        if (!title)
            return alert("Please enter a title.");

        const rank = $('#sortableBody tr').length + 1;
        const newRow = table.row.add([
            '<i class="fas fa-grip-vertical"></i>',
            rank,
            `<div class="editable-cell" contenteditable="true">${title}</div>`,
            `<div class="editable-cell" contenteditable="true">${$('#m_desc').val()}</div>`,
            `<div class="editable-cell" contenteditable="true">${$('#m_crit').val()}</div>`,
            `<div class="editable-cell" contenteditable="true">${$('#m_pts').val()}</div>`,
            '<button class="btn-delete"><i class="fas fa-trash-alt text-danger"></i></button>'
        ]).draw(false).node();

        $(newRow).find('td').eq(0).addClass('drag-handle');
        $(newRow).find('td').eq(1).addClass('priority-rank');

        $('#addItemModal').modal('hide');
        $('#addItemForm')[0].reset();
    });

    // 6. Delete Row (Event Delegation Fix)
    $('#backlogTable').on('click', '.btn-delete', function () {
        table.row($(this).parents('tr')).remove().draw();
        $('.priority-rank').each(function (i) {
            $(this).text(i + 1);
        });
    });
});





//$(document).ready(function () {
//    const sidebar = document.getElementById('sidebar');
//    const content = document.getElementById('content-wrapper');
//
//    function toggleSidebar() {
//        sidebar.classList.toggle('collapsed');
//        setTimeout(() => {
//            table.columns.adjust().draw();
//        }, 400); // Matches transition speed
//    }
//
//    sidebar.addEventListener('dblclick', toggleSidebar);
//    $('#sidebarToggle').on('click', toggleSidebar);
//
//    const table = $('#backlogTable').DataTable({
//        paging: false, info: false, searching: true,
//        columnDefs: [{targets: 'no-sort', orderable: false}]
//    });
//
//    Sortable.create(document.getElementById('sortableBody'), {
//        handle: '.drag-handle',
//        animation: 200,
//        onEnd: () => {
//            $('.priority-rank').each(function (i) {
//                $(this).text(i + 1);
//            });
//            table.rows().invalidate().draw(false);
//        }
//    });
//    
//    
//    // Enable Draggable only
//            $('#backlogDocModal').on('shown.bs.modal', function () {
//                $(this).find('.modal-content').draggable({
//                    handle: ".modal-header",
//                    containment: "window"
//                });
//            });
//
//            // Tab Switching Logic for Footer Button
//            $('button[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
//                const target = $(e.target).attr('data-bs-target');
//                target === '#uploadPane' ? $('#confirmBtn').removeClass('d-none') : $('#confirmBtn').addClass('d-none');
//            });
//
//            // Drag & Drop Functionality
//            const dz = $('#modalDropZone'), fi = $('#modalFileField'), fn = $('#fileNameText'), preview = $('#filePreview');
//
//            dz.on('click', () => fi.click());
//            fi.on('change', function () {
//                if (this.files[0]) {
//                    fn.text(this.files[0].name);
//                    preview.removeClass('d-none');
//                    // Auto-fill Name if empty
//                    if ($('#docNameInput').val() === "") {
//                        $('#docNameInput').val(this.files[0].name.split('.').slice(0, -1).join('.'));
//                    }
//                }
//            });
//
//            dz.on('dragover', (e) => { e.preventDefault(); dz.addClass('bg-primary-subtle border-primary'); });
//            dz.on('dragleave', () => dz.removeClass('bg-primary-subtle border-primary'));
//            dz.on('drop', (e) => {
//                e.preventDefault();
//                dz.removeClass('bg-primary-subtle border-primary');
//                const files = e.originalEvent.dataTransfer.files;
//                if (files.length > 0) {
//                    fi[0].files = files;
//                    fi.trigger('change');
//                }
//            });
//    
//    
//    
//});
//
//$('#confirmAddBtn').on('click', function () {
//    const title = $('#m_title').val();
//    if (!title)
//        return alert("Please enter a title.");
//
//    const rank = $('#sortableBody tr').length + 1; // Auto-calculate Priority Rank
//
//    const newRow = table.row.add([
//        '<i class="fas fa-grip-vertical"></i>',
//        rank,
//        `<div class="editable-cell" contenteditable="true">${title}</div>`,
//        `<div class="editable-cell" contenteditable="true">${$('#m_desc').val()}</div>`,
//        `<div class="editable-cell" contenteditable="true">${$('#m_crit').val()}</div>`,
//        `<div class="editable-cell" contenteditable="true">${$('#m_pts').val()}</div>`,
//        '<button class="btn-delete"><i class="fas fa-trash-alt text-danger"></i></button>'
//    ]).draw(false).node();
//
//    // Re-apply essential classes to the new row
//    $(newRow).find('td').eq(0).addClass('drag-handle');
//    $(newRow).find('td').eq(1).addClass('priority-rank');
//
//    // Close Modal and Reset Form
//    $('#addItemModal').modal('hide');
//    $('#addItemForm')[0].reset();
//});