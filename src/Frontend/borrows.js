const role = localStorage.getItem("role");

const studentCode =
    localStorage.getItem("studentCode");


const borrowTitle =
    document.getElementById("borrowTitle");

const borrowDescription =
    document.getElementById("borrowDescription");

const borrowHeader =
    document.getElementById("borrowHeader");


// ======================================
// PAGE SETUP
// ======================================

if (role === "student") {

    borrowTitle.textContent =
        "My Borrowed Books";

    borrowDescription.textContent =
        "View your borrowed books and extend the due date";

    borrowHeader.innerHTML = `
        <th>Book Code</th>
        <th>Borrow Date</th>
        <th>Due Date</th>
        <th>Status</th>
        <th>Fine</th>
        <th>Action</th>
    `;

} else {

    borrowTitle.textContent =
        "Borrow Management";

    borrowDescription.textContent =
        "Manage all library borrow records";

    borrowHeader.innerHTML = `
        <th>Student Code</th>
        <th>Book Code</th>
        <th>Borrow Date</th>
        <th>Due Date</th>
        <th>Status</th>
        <th>Fine</th>
        <th>Action</th>
    `;
}


// ======================================
// LOAD BORROWS
// ======================================

function loadBorrows() {

    fetch("http://localhost:8080/borrows")

        .then(response => {

            if (!response.ok) {

                throw new Error(
                    "Failed to load borrows"
                );
            }

            return response.json();
        })

        .then(borrows => {

            const borrowList =
                document.getElementById("borrowList");

            borrowList.innerHTML = "";


            // ==================================
            // STUDENT
            // ==================================

            if (role === "student") {

                // Only logged-in student's records
                const myBorrows =
                    borrows.filter(
                        borrow =>
                            borrow.studentCode === studentCode
                    );


                myBorrows.forEach(borrow => {

                    const row =
                        document.createElement("tr");


                    const status =
                        borrow.returned
                            ? "Returned"
                            : "Active";


                    let action = "";


                    if (borrow.returned) {

                        action = "Returned";

                    } else if (
                        borrow.extensionCount >= 1
                    ) {

                        action = `
                            <span>
                                Already Extended
                            </span>
                        `;

                    } else {

                        action = `
                            <button
                                type="button"
                                onclick="extendBook(${borrow.id})">
                                Extend
                            </button>
                        `;
                    }


                    row.innerHTML = `

                        <td>
                            ${borrow.bookCode}
                        </td>

                        <td>
                            ${borrow.borrowDate}
                        </td>

                        <td>
                            ${borrow.dueDate}
                        </td>

                        <td>
                            ${status}
                        </td>

                        <td>
                            ₹${borrow.fine}
                        </td>

                        <td>
                            ${action}
                        </td>

                    `;


                    borrowList.appendChild(row);
                });


                // No records
                if (myBorrows.length === 0) {

                    borrowList.innerHTML = `

                        <tr>

                            <td colspan="6">
                                You have no borrowed books.
                            </td>

                        </tr>

                    `;
                }
            }


            // ==================================
            // LIBRARIAN
            // ==================================

            else {

                borrows.forEach(borrow => {

                    const row =
                        document.createElement("tr");


                    const status =
                        borrow.returned
                            ? "Returned"
                            : "Active";


                    let action = "";


                    if (borrow.returned) {

                        action = "Returned";

                    } else {

                        action = `

                            <button
                                type="button"
                                onclick="returnBook(${borrow.id})">
                                Return
                            </button>

                            ${
                                borrow.extensionCount >= 1

                                ? `
                                    <span>
                                        Already Extended
                                    </span>
                                  `

                                : `
                                    <button
                                        type="button"
                                        onclick="extendBook(${borrow.id})">
                                        Extend
                                    </button>
                                  `
                            }

                        `;
                    }


                    row.innerHTML = `

                        <td>
                            ${borrow.studentCode}
                        </td>

                        <td>
                            ${borrow.bookCode}
                        </td>

                        <td>
                            ${borrow.borrowDate}
                        </td>

                        <td>
                            ${borrow.dueDate}
                        </td>

                        <td>
                            ${status}
                        </td>

                        <td>
                            ₹${borrow.fine}
                        </td>

                        <td>
                            ${action}
                        </td>

                    `;


                    borrowList.appendChild(row);
                });


                if (borrows.length === 0) {

                    borrowList.innerHTML = `

                        <tr>

                            <td colspan="7">
                                No borrow records found.
                            </td>

                        </tr>

                    `;
                }
            }

        })

        .catch(error => {

            console.error(
                "Error loading borrows:",
                error
            );
        });
}


// ======================================
// RETURN BOOK
// ======================================

function returnBook(id) {

    if (
        !confirm(
            "Are you sure you want to return this book?"
        )
    ) {
        return;
    }


    fetch(
        `http://localhost:8080/borrows/return/${id}`,
        {
            method: "PUT"
        }
    )

    .then(async response => {

        if (!response.ok) {

            const errorMessage =
                await response.text();

            throw new Error(
                errorMessage ||
                "Failed to return book"
            );
        }

        return response.text();
    })

    .then(() => {

        alert(
            "Book returned successfully!"
        );

        loadBorrows();
    })

    .catch(error => {

        console.error(
            "Return error:",
            error
        );

        alert(
            error.message
        );
    });
}


// ======================================
// EXTEND BOOK
// ======================================

function extendBook(id) {

    if (
        !confirm(
            "Are you sure you want to extend this book by 14 days?"
        )
    ) {
        return;
    }


    fetch(
        `http://localhost:8080/borrows/extend/${id}`,
        {
            method: "PUT"
        }
    )

    .then(async response => {

        if (!response.ok) {

            const errorMessage =
                await response.text();

            throw new Error(
                errorMessage ||
                "Failed to extend book"
            );
        }

        return response.json();
    })

    .then(() => {

        alert(
            "Book extended successfully!"
        );

        loadBorrows();
    })

    .catch(error => {

        console.error(
            "Extension error:",
            error
        );

        alert(
            error.message
        );
    });
}


// ======================================
// INITIAL LOAD
// ======================================

loadBorrows();