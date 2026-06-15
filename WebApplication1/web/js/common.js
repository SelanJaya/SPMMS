/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


document.addEventListener("DOMContentLoaded", function () {

//    var projStart = document.getElementById("ProjStart");
//    var projEnd = document.getElementById("ProjEnd");
//    var formSubbtn = document.getElementById("formSubbtn");
//    var formCanbtn = document.getElementById("formCanbtn");
//    var errorContainer = document.getElementById("errorMsgStartDate");
//    //                functon parseDate(date){
//    //                  cont[date,month,year] = date.split("/");  
//    //                  return 
//    //                };

//    function validateDates() {
//        if (projStart.value && projEnd.value) {
//
//            var start = new Date(projStart.value);
//            var end = new Date(projEnd.value);
//
//            if (start > end) {
//                errorContainer.innerHTML = `<div class="alert alert-danger py-2 mt-2" style="font-size: 0.8rem;">
//                        <i class="fas fa-exclamation-circle me-1"></i>
//                        <strong>Invalid!</strong> Start date cannot be after end date.
//                    </div>`;
//                formSubbtn.disabled = true;
//            } else {
//                errorContainer.innerHTML = "";
//                formSubbtn.disabled = false;
//            }
//        }
//    }
//    ;
//
//    // 5. Add event listeners so it checks every time the user picks a date
//    projStart.addEventListener("change", validateDates);
//    projEnd.addEventListener("change", validateDates);
//
//    formCanbtn.addEventListener("click", function () {
//        errorContainer.innerHTML = "";
//        errorContainer.style.display = "none"; // Completely hides the floating "box"
//    });



// Automatically hide success alert after 5 seconds
//    setTimeout(function () {
//        $("#successProcessTab").animate({
//            top: "-150px", // Pulls it up past the top of the screen
//            opacity: 0
//        }, 600, function () {
//            $(this).remove(); // Remove from DOM after it's gone
//        });
//    }, 3000);

setTimeout(function () {
    $("#successProcessTab").animate({
        top: "-150px",
        opacity: 0
    }, 600, function () {
        $(this).addClass("d-none");

        // Reset for next time
        $(this).css({
            top: "",
            opacity: 1
        });
    });
}, 3000);

}
);