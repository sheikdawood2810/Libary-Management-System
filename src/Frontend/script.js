const role = localStorage.getItem("role");
const studentCode = localStorage.getItem("studentCode");


fetch("https://libary-management-system-hblc.onrender.com/books")
    .then(response => response.json())
    .then(books => {

        document.getElementById("totalBooks").innerText =
            books.length;

    })
    .catch(error => {

        console.error("Books error:", error);

    });

if (role === "librarian") {

    fetch("https://libary-management-system-hblc.onrender.com/students")
        .then(response => response.json())
        .then(students => {

            document.getElementById("totalStudents").innerText =
                students.length;

        })
        .catch(error => {

            console.error("Students error:", error);

        });
}


fetch("https://libary-management-system-hblc.onrender.com/borrows")
    .then(response => response.json())
    .then(borrows => {

        let active;
        let overdue;


        if (role === "student") {

            const myBorrows = borrows.filter(
                borrow =>
                    borrow.studentCode === studentCode
            );

            active = myBorrows.filter(
                borrow =>
                    borrow.returned === false
            );

            overdue = myBorrows.filter(
                borrow =>
                    borrow.returned === false &&
                    borrow.overdueDays > 0
            );

        } else {

            active = borrows.filter(
                borrow =>
                    borrow.returned === false
            );

            overdue = borrows.filter(
                borrow =>
                    borrow.returned === false &&
                    borrow.overdueDays > 0
            );
        }


        document.getElementById("activeBorrows").innerText =
            active.length;

        document.getElementById("overdueBooks").innerText =
            overdue.length;

    })
    .catch(error => {

        console.error("Borrows error:", error);

    });


function showBooks() {
    window.location.href = "books.html";
}

function showStudents() {
    window.location.href = "students.html";
}

function showBorrows() {
    window.location.href = "borrows.html";
}

function showOverdueBooks() {
    window.location.href = "overdue.html";
}