function requireStudent() {

    const currentRole = localStorage.getItem("role");

    if (currentRole !== "student") {

        alert("Please login as a student.");

        window.location.href = "index.html";
    }
}


function requireLibrarian() {

    const currentRole = localStorage.getItem("role");

    if (currentRole !== "librarian") {

        alert("Please login as a librarian.");

        window.location.href = "index.html";
    }
}


function logout() {

    localStorage.clear();

    window.location.href = "index.html";
}