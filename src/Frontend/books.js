const role = localStorage.getItem("role");

console.log("Current role:", role);


if (role === "student" || role === "librarian") {

    const actionHeader =
        document.getElementById("actionHeader");

    if (actionHeader) {
        actionHeader.style.display = "table-cell";
    }
}


if (role === "librarian") {

    const pageTitle =
        document.getElementById("pageTitle");

    if (pageTitle) {
        pageTitle.textContent = "Manage Books";
    }


    const pageDescription =
        document.getElementById("pageDescription");

    if (pageDescription) {
        pageDescription.textContent =
            "Add, update and manage library books";
    }


    const librarianControls =
        document.getElementById("librarianControls");

    if (librarianControls) {

        librarianControls.innerHTML = `
            <button type="button" onclick="addBook()">
                ➕ Add New Book
            </button>
        `;
    }
}



function loadBooks(
    url = "https://libary-management-system-hblc.onrender.com/books"
) {

    fetch(url)

        .then(response => {

            if (!response.ok) {
                throw new Error(
                    "Failed to load books"
                );
            }

            return response.json();
        })

        .then(books => {

            const bookList =
                document.getElementById("bookList");

            bookList.innerHTML = "";


            books.forEach(book => {

                const row =
                    document.createElement("tr");

                let actions = "";



                if (role === "student") {

                    actions = `
                        <td>

                            ${
                                book.available > 0
                                ? `
                                    <button
                                        type="button"
                                        onclick="borrowBook(${book.id})">
                                        📖 Borrow
                                    </button>
                                  `
                                : `
                                    <span>
                                        Not Available
                                    </span>
                                  `
                            }

                        </td>
                    `;
                }


                else if (role === "librarian") {

                    actions = `
                        <td>

                            ${
                                book.available > 0
                                ? `
                                    <button
                                        type="button"
                                        onclick="borrowBook(${book.id})">
                                        📖 Borrow
                                    </button>
                                  `
                                : `
                                    <span>
                                        Not Available
                                    </span>
                                  `
                            }

                            <button
                                type="button"
                                onclick="editBook(${book.id})">
                                ✏️ Edit
                            </button>

                            <button
                                type="button"
                                onclick="deleteBook(${book.id})">
                                🗑️ Delete
                            </button>

                        </td>
                    `;
                }


                row.innerHTML = `
                    <td>${book.bookCode}</td>

                    <td>${book.title}</td>

                    <td>${book.author}</td>

                    <td>${book.category}</td>

                    <td>${book.available}</td>

                    ${actions}
                `;

                bookList.appendChild(row);

            });

        })

        .catch(error => {

            console.error(
                "Error loading books:",
                error
            );
        });
}


function searchBooks() {

    const title =
        document.getElementById("searchTitle").value;


    if (title.trim() === "") {

        loadBooks();

        return;
    }


    loadBooks(
        "https://libary-management-system-hblc.onrender.com/books/search?title="
        + encodeURIComponent(title)
    );
}


function addBook() {

    document.getElementById("addBookForm")
        .style.display = "block";
}



function closeBookForm() {

    document.getElementById("addBookForm")
        .style.display = "none";
}


function saveBook() {

    const book = {

        title:
            document.getElementById("bookTitle").value,

        author:
            document.getElementById("bookAuthor").value,

        category:
            document.getElementById("bookCategory").value,

        available:
            Number(
                document.getElementById("bookAvailable").value
            )
    };


    fetch("https://libary-management-system-hblc.onrender.com/books", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(book)

    })

    .then(response => {

        if (!response.ok) {
            throw new Error(
                "Failed to add book"
            );
        }

        return response.json();

    })

    .then(() => {

        alert(
            "Book added successfully!"
        );

        closeBookForm();

        loadBooks();

    })

    .catch(error => {

        console.error(error);

        alert(
            "Error adding book"
        );
    });
}


function deleteBook(id) {

    if (!confirm(
        "Are you sure you want to delete this book?"
    )) {
        return;
    }


    fetch(
        `https://libary-management-system-hblc.onrender.com/books/${id}`,
        {
            method: "DELETE"
        }
    )

    .then(response => {

        if (!response.ok) {
            throw new Error(
                "Failed to delete book"
            );
        }

        alert(
            "Book deleted successfully!"
        );

        loadBooks();

    })

    .catch(error => {

        console.error(error);

        alert(
            "Error deleting book"
        );
    });
}



function editBook(id) {

    fetch(
        "https://libary-management-system-hblc.onrender.com/books"
    )

    .then(response => {

        if (!response.ok) {
            throw new Error(
                "Failed to load books"
            );
        }

        return response.json();

    })

    .then(books => {

        const book =
            books.find(
                book => book.id === id
            );


        if (!book) {

            alert(
                "Book not found"
            );

            return;
        }


        document.getElementById(
            "editBookId"
        ).value = book.id;


        document.getElementById(
            "editBookTitle"
        ).value = book.title;


        document.getElementById(
            "editBookAuthor"
        ).value = book.author;


        document.getElementById(
            "editBookCategory"
        ).value = book.category;


        document.getElementById(
            "editBookAvailable"
        ).value = book.available;


        document.getElementById(
            "editBookForm"
        ).style.display = "block";

    })

    .catch(error => {

        console.error(error);

        alert(
            "Error loading book"
        );
    });
}


function closeEditForm() {

    document.getElementById(
        "editBookForm"
    ).style.display = "none";
}


function updateBook() {

    const id =
        document.getElementById(
            "editBookId"
        ).value;


    const book = {

        title:
            document.getElementById(
                "editBookTitle"
            ).value,

        author:
            document.getElementById(
                "editBookAuthor"
            ).value,

        category:
            document.getElementById(
                "editBookCategory"
            ).value,

        available:
            Number(
                document.getElementById(
                    "editBookAvailable"
                ).value
            )
    };


    fetch(
        `https://libary-management-system-hblc.onrender.com/books/${id}`,
        {

            method: "PUT",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(book)
        }
    )

    .then(response => {

        if (!response.ok) {
            throw new Error(
                "Failed to update book"
            );
        }

        return response.json();

    })

    .then(() => {

        alert(
            "Book updated successfully!"
        );

        closeEditForm();

        loadBooks();

    })

    .catch(error => {

        console.error(error);

        alert(
            "Error updating book"
        );
    });
}


function goDashboard() {

    if (role === "student") {

        window.location.href =
            "studentpanel.html";

    }
    else if (role === "librarian") {

        window.location.href =
            "libarianpanel.html";

    }
    else {

        window.location.href =
            "index.html";
    }
}


function borrowBook(bookId) {

    let studentCode;


    if (role === "student") {

        studentCode =
            localStorage.getItem("studentCode");

        if (!studentCode) {

            alert(
                "Student information not found. Please login again."
            );

            return;
        }
    }



    else if (role === "librarian") {

        studentCode =
            prompt("Enter Student Code:");

        if (
            studentCode === null ||
            studentCode.trim() === ""
        ) {
            return;
        }

        studentCode =
            studentCode.trim();
    }



    fetch("https://libary-management-system-hblc.onrender.com/books")

        .then(response => {

            if (!response.ok) {
                throw new Error("Failed to load books");
            }

            return response.json();
        })

        .then(books => {

            const book =
                books.find(
                    book => book.id === bookId
                );


            if (!book) {

                throw new Error(
                    "Book not found"
                );
            }


            if (book.available <= 0) {

                throw new Error(
                    "No copies available"
                );
            }


            const borrow = {

                studentCode: studentCode,

                bookCode: book.bookCode
            };


            return fetch(
                "https://libary-management-system-hblc.onrender.com/borrows",
                {

                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(borrow)
                }
            );
        })


        .then(response => {

            if (!response.ok) {

                return response.text()
                    .then(message => {

                        throw new Error(message);
                    });
            }

            return response.json();
        })



        .then(() => {

            alert(
                "Book borrowed successfully!"
            );

            loadBooks();
        })


        .catch(error => {

            console.error(
                "Borrow error:",
                error
            );

            alert(
                "Error borrowing book: " +
                error.message
            );
        });
}
loadBooks();