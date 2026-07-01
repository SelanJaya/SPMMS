/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

const chartOptions = {responsive: true, maintainAspectRatio: false, plugins: {legend: {position: 'bottom'}}};

document.addEventListener('DOMContentLoaded', async function () {

    loadBurndownDropDown();

    const response = await fetch(`projectAnalyticsServlet?action=fetchInsight&project_id=${project_id}`);
    const result = await response.json();
    console.log(result);

    if (result.status === "failed") {
        throw new Error("Server response failed");
    }

    const data = result.analyticsData;
    const fallbackText = "Not started"; // Replaced "Not begin"

    document.getElementById("avgVelocity").innerText = data.AVGVelocity ?? fallbackText;
    document.getElementById("sprintSuccRate").innerText = data.sprintSuccRate ?? fallbackText;
    document.getElementById("cycleTime").innerText = data.CycleTime ?? fallbackText;
    document.getElementById("rejectionRate").innerText = data.rejectionRate ?? fallbackText;

//    //velocity Chart
//    // 1. Extract the array from your JSON response
//    const velocityData = result.analyticsData.velocityGraphData;
//
//    // 2. Map the data into the arrays that Chart.js expects
//    // We generate the labels dynamically based on the number of items in the array (e.g., Sprint 1, Sprint 2)
//    const sprintLabels = velocityData.map((_, index) => `Sprint ${index + 1}`);
//
//    // Extract 'totalTask' for the Planned dataset and 'completedTask' for the Completed dataset
//    const plannedTasks = velocityData.map(data => data.totalTask);
//    const completedTasks = velocityData.map(data => data.completedTask);
//
//    // 3. Render the Chart
//    new Chart(document.getElementById('velocityChart'), {
//        type: 'bar',
//        data: {
//            labels: sprintLabels,
//            datasets: [
//                {
//                    label: 'Planned',
//                    data: plannedTasks,
//                    backgroundColor: '#94a3b8'
//                },
//                {
//                    label: 'Completed',
//                    data: completedTasks,
//                    backgroundColor: '#2563eb'
//                }
//            ]
//        },
//        options: chartOptions // Assuming you have chartOptions defined elsewhere
//    });


    // 1. Extract the array from your JSON response
    const velocityData = result.analyticsData.velocityGraphData;
    const chartContainer = document.getElementById('velocityChart').parentElement;

// 2. Check if data is empty
    if (!velocityData || velocityData.length === 0) {
        // Hide the canvas and show a message
        document.getElementById('velocityChart').style.display = 'none';

        // Create or show an empty state message
        const emptyMsg = document.createElement('div');
        emptyMsg.className = 'text-center py-5 text-muted';
        emptyMsg.innerHTML = '<i class="fas fa-chart-bar fa-3x mb-3 opacity-50"></i><p>No sprint data available to display.</p>';
        chartContainer.appendChild(emptyMsg);
    } else {
        // 3. Map the data and render the chart
        const sprintLabels = velocityData.map((_, index) => `Sprint ${index + 1}`);
        const plannedTasks = velocityData.map(data => data.totalTask);
        const completedTasks = velocityData.map(data => data.completedTask);

        new Chart(document.getElementById('velocityChart'), {
            type: 'bar',
            data: {
                labels: sprintLabels,
                datasets: [
                    {
                        label: 'Planned',
                        data: plannedTasks,
                        backgroundColor: '#94a3b8'
                    },
                    {
                        label: 'Completed',
                        data: completedTasks,
                        backgroundColor: '#2563eb'
                    }
                ]
            },
            options: chartOptions
        });
    }

    //BurnDown Chart
    displayBurndownChart(result.analyticsData.burnDownChartData);


    // ==========================================
    // Approval Chart (Pie Chart)
    // ==========================================

    // 1. Extract the totals from your 'data' object (result.analyticsData)
    // Using fallback to 0 just in case the properties are missing
    const approvedCount = data.totalApprovedTask ?? 0;
    const rejectedCount = data.totalRejectedTask ?? 0;

    const approvalData = [approvedCount, rejectedCount];

    // 2. Check if both are exactly 0
    const isApprovalAllZeros = approvalData.every(value => value === 0);

    const approvalCanvas = document.getElementById('approvalChart');
    const approvalContainer = approvalCanvas.parentElement;

    if (isApprovalAllZeros) {
        // 3A. Hide the canvas
        approvalCanvas.style.display = 'none';

        // Create and show the alternative message
        if (!document.getElementById('approval-empty-msg')) {
            const msgElement = document.createElement('div');
            msgElement.id = 'approval-empty-msg';
            msgElement.innerText = "No tasks have been approved or rejected yet.";

            // Basic styling to match the dashboard
            msgElement.style.textAlign = 'center';
            msgElement.style.padding = '40px 20px';
            msgElement.style.color = '#64748b';
            msgElement.style.fontStyle = 'italic';

            approvalContainer.appendChild(msgElement);
        }
    } else {
        // 3B. Show the canvas and remove the message if it exists
        approvalCanvas.style.display = 'block';
        const existingMsg = document.getElementById('approval-empty-msg');
        if (existingMsg) {
            existingMsg.remove();
        }

        // Render the Pie Chart
        new Chart(approvalCanvas, {
            type: 'pie',
            data: {
                labels: ['Approved', 'Rejected'],
                datasets: [{
                        data: approvalData,
                        backgroundColor: ['#10b981', '#ef4444']
                    }]
            },
            options: chartOptions
        });
    }

    // 1. Extract the rework array from your JSON response
    const reworkGraphData = result.analyticsData.reworkGraphData;

// 2. Dynamically create labels (Sprint 1, Sprint 2, etc.) and extract 'rejectedTask'
    const reworkLabels = reworkGraphData.map((_, index) => `Sprint ${index + 1}`);
    const reworkDataValues = reworkGraphData.map(data => data.rejectedTask);

// 3. Check if EVERY value in the rework array is exactly 0
    const isAllZeros = reworkDataValues.every(value => value === 0);

    const canvas = document.getElementById('reworkChart');
    const container = canvas.parentElement;

    if (isAllZeros) {
        // Hide the canvas
        canvas.style.display = 'none';

        // Create and show the alternative message
        if (!document.getElementById('rework-empty-msg')) {
            const msgElement = document.createElement('div');
            msgElement.id = 'rework-empty-msg';
            msgElement.innerText = "No rework or rejected tasks reported. Great job!";

            // Basic styling
            msgElement.style.textAlign = 'center';
            msgElement.style.padding = '40px 20px';
            msgElement.style.color = '#64748b';
            msgElement.style.fontStyle = 'italic';

            container.appendChild(msgElement);
        }
    } else {
        // Show the canvas and remove the message if it exists
        canvas.style.display = 'block';
        const existingMsg = document.getElementById('rework-empty-msg');
        if (existingMsg) {
            existingMsg.remove();
        }

        // Render the Chart using the dynamic data
        new Chart(canvas, {
            type: 'line',
            data: {
                labels: reworkLabels,
                datasets: [{
                        label: 'Rejected Tasks',
                        data: reworkDataValues,
                        borderColor: '#f59e0b',
                        tension: 0.3
                    }]
            },
            options: chartOptions
        });
    }
});

async function loadBurndownDropDown() {

    const response = await fetch(
            `projectAnalyticsServlet?action=fetchSprintBurndown&project_id=${project_id}`
            );

    const result = await response.json();
    console.log(result);

    const sprintType = document.getElementById("sprintSelect");

    // prevent duplicate options
    sprintType.innerHTML = '<option value="">Select Sprint</option>';


    result.sprintData.forEach(item => {
        const option = document.createElement("option");

        option.textContent = item.sprint_name;
        option.value = item.sprint_id;

        sprintType.appendChild(option);
    });

}
;

function displayBurndownChart(result) {
    //BurnDown Chart
    const burnDownData = result;

// Labels = actual dates from backend
    const labels = burnDownData.map(item => {

        const date = item.taskEndDate || item.taskStartDate;
        return new Date(date).toLocaleDateString('en-GB', {
            day: '2-digit',
            month: 'short'
        });
    });

// Remaining tasks
    const remainingTasks = burnDownData.map(item => item.remainingTask);

// Destroy old chart if exists
    if (window.burndownChartInstance) {
        window.burndownChartInstance.destroy();
    }

// Create chart
    window.burndownChartInstance = new Chart(
            document.getElementById('burndownChart'),
            {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                            label: 'Remaining Tasks',
                            data: remainingTasks,
                            borderColor: '#2563eb',
                            backgroundColor: 'rgba(37, 99, 235, 0.2)',
                            tension: 0.3,
                            fill: true
                        }]
                },
                options: chartOptions
            }
    );

    const selectType = document.getElementById("sprintSelect");
    if (!selectType.value) {
        if (Array.isArray(result) && result.length > 0) {
            document.getElementById("sprintSelect").value =
                    String(result[0].sprintId);
        }
    }
}

document.getElementById("sprintSelect").addEventListener("change", async (e) => {

    const sprint_id = e.target.value;
    console.log(sprint_id);
    const response = await fetch(`projectAnalyticsServlet?action=fetchBurnDownData&sprint_id=${sprint_id}`);

    const result = await response.json();
    console.log(result);

    displayBurndownChart(result.analyticsData);
});