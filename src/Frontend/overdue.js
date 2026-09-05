const role = localStorage.getItem("role");

// Get the currently logged-in student's code
const studentCode = localStorage.getItem("studentCode");


fetch("https://libary-management-system-hblc.onrender.com/borrows")

    .then(response => {

        if (!response.ok) {
            throw new Error("Failed to load borrow records");
        }

        return response.json();
    })

    .then(borrows => {

        const overdueList =
            document.getElementById("overdueList");

        overdueList.innerHTML = "";


        let overdueBooks;


        if (role === "student") {

            overdueBooks = borrows.filter(borrow =>

                borrow.studentCode === studentCode &&
                borrow.returned === false &&
                borrow.overdueDays > 0

            );

        }


        else {

            overdueBooks = borrows.filter(borrow =>

                borrow.returned === false &&
                borrow.overdueDays > 0

            );
        }



        overdueBooks.forEach(borrow => {

            const row =
                document.createElement("tr");

            row.innerHTML = `
                <td>${borrow.studentCode}</td>
                <td>${borrow.bookCode}</td>
                <td>${borrow.dueDate}</td>
                <td>${borrow.overdueDays}</td>
                <td>₹${borrow.fine}</td>
            `;

            overdueList.appendChild(row);
        });


        if (overdueBooks.length === 0) {

            const message =
                role === "student"
                    ? "You have no overdue books 🎉"
                    : "No overdue books 🎉";


            overdueList.innerHTML = `
                <tr>
                    <td colspan="5">
                        ${message}
                    </td>
                </tr>
            `;
        }

    })

    .catch(error => {

        console.error(
            "Error loading overdue books:",
            error
        );

    });

    function goDashboard() {

    const currentRole = localStorage.getItem("role");

    if (currentRole === "student") {
        window.location.href = "studentpanel.html";
    }
    else if (currentRole === "librarian") {
        window.location.href = "libarianpanel.html";
    }
    else {
        window.location.href = "index.html";
    }
}