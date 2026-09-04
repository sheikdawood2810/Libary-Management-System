function studentLogin() {

    document.getElementById("roleSelection")
        .style.display = "none";

    document.getElementById("studentLoginForm")
        .style.display = "block";
}


function librarianLogin() {

    document.getElementById("roleSelection")
        .style.display = "none";

    document.getElementById("librarianLoginForm")
        .style.display = "block";
}


function backToRoles() {

    document.getElementById("studentLoginForm")
        .style.display = "none";

    document.getElementById("studentRegisterForm")
        .style.display = "none";

    document.getElementById("librarianLoginForm")
        .style.display = "none";

    document.getElementById("roleSelection")
        .style.display = "block";

    document.getElementById("studentMessage")
        .textContent = "";

    document.getElementById("librarianMessage")
        .textContent = "";

    document.getElementById("registerMessage")
        .textContent = "";
}


function loginStudent() {

    const studentCode =
        document.getElementById("studentCode")
            .value.trim();

    const password =
        document.getElementById("studentPassword")
            .value;

    const message =
        document.getElementById("studentMessage");


    if (studentCode === "" || password === "") {

        message.textContent =
            "Please enter Student ID and password.";

        return;
    }


   fetch("https://libary-management-system-hblc.onrender.com/students/login", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({
            studentCode: studentCode,
            password: password
        })

    })

    .then(response => {

        if (!response.ok) {

            throw new Error(
                "Invalid credentials"
            );
        }

        return response.json();
    })

    .then(student => {

        localStorage.setItem(
            "role",
            "student"
        );

        localStorage.setItem(
            "studentId",
            student.id
        );

        localStorage.setItem(
            "studentCode",
            student.studentCode
        );

        localStorage.setItem(
            "studentName",
            student.name
        );

        window.location.href =
            "studentpanel.html";
    })

    .catch(error => {

        console.error(error);

        message.textContent =
            "Invalid Student ID or Password.";
    });
}


function loginLibrarian() {

    const username =
        document.getElementById("librarianUsername")
            .value.trim();

    const password =
        document.getElementById("librarianPassword")
            .value;

    const message =
        document.getElementById("librarianMessage");


    if (username === "" || password === "") {

        message.textContent =
            "Please enter username and password.";

        return;
    }


    fetch("https://libary-management-system-hblc.onrender.com/librarian/login", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({
            username: username,
            password: password
        })

    })

    .then(response => {

        if (!response.ok) {

            throw new Error(
                "Invalid credentials"
            );
        }

        return response.json();
    })

    .then(librarian => {

        localStorage.setItem(
            "role",
            "librarian"
        );

        localStorage.setItem(
            "librarianId",
            librarian.id
        );

        localStorage.setItem(
            "librarianUsername",
            librarian.username
        );

        window.location.href =
            "libarianpanel.html";
    })

    .catch(error => {

        console.error(error);

        message.textContent =
            "Invalid Username or Password.";
    });
}


function showRegisterForm() {

    document.getElementById(
        "studentLoginForm"
    ).style.display = "none";

    document.getElementById(
        "studentRegisterForm"
    ).style.display = "block";

    document.getElementById(
        "registerMessage"
    ).textContent = "";
}


function backToStudentLogin() {

    document.getElementById(
        "studentRegisterForm"
    ).style.display = "none";

    document.getElementById(
        "studentLoginForm"
    ).style.display = "block";

    document.getElementById(
        "registerMessage"
    ).textContent = "";
}


function registerStudent() {

    const name =
        document.getElementById(
            "registerName"
        ).value.trim();

    const email =
        document.getElementById(
            "registerEmail"
        ).value.trim();

    const phone =
        document.getElementById(
            "registerPhone"
        ).value.trim();

    const password =
        document.getElementById(
            "registerPassword"
        ).value;

    const confirmPassword =
        document.getElementById(
            "registerConfirmPassword"
        ).value;

    const message =
        document.getElementById(
            "registerMessage"
        );

        const registerButton =
    document.getElementById("registerButton");



    if (
        name === "" ||
        email === "" ||
        phone === "" ||
        password === "" ||
        confirmPassword === ""
    ) {

        message.textContent =
            "Please fill in all fields.";

        return;
    }



    const emailPattern =
        /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;


    if (!emailPattern.test(email)) {

        message.textContent =
            "Please enter a valid email address.";

        return;
    }



    if (password !== confirmPassword) {

        message.textContent =
            "Passwords do not match.";

        return;
    }


    if (password.length < 6) {

        message.textContent =
            "Password must be at least 6 characters.";

        return;
    }

      registerButton.disabled = true;
      registerButton.textContent = "Registering...";


    const student = {

        name: name,

        email: email,

        phone: phone,

        password: password
    };



   fetch(
    "https://libary-management-system-hblc.onrender.com/students",{

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(student)
        }
    )

    .then(async response => {

        if (!response.ok) {

            const errorMessage =
                await response.text();

            throw new Error(
                errorMessage
            );
        }

        return response.json();
    })



    .then(student => {

        console.log(
            "Student registered successfully"
        );


        alert(
            "Registration successful!\n\n" +
            "Your Student ID is:\n" +
            student.studentCode
        );


        // Clear registration fields

        document.getElementById(
            "registerName"
        ).value = "";

        document.getElementById(
            "registerEmail"
        ).value = "";

        document.getElementById(
            "registerPhone"
        ).value = "";

        document.getElementById(
            "registerPassword"
        ).value = "";

        document.getElementById(
            "registerConfirmPassword"
        ).value = "";


        // Hide registration form

        document.getElementById(
            "studentRegisterForm"
        ).style.display = "none";


        // Show student login form

        document.getElementById(
            "studentLoginForm"
        ).style.display = "block";


        // Automatically fill Student ID

        document.getElementById(
            "studentCode"
        ).value =
            student.studentCode;


        // Keep password empty

        document.getElementById(
            "studentPassword"
        ).value = "";


        // Clear registration message

        document.getElementById(
            "registerMessage"
        ).textContent = "";
    })



    .catch(error => {

        console.error(
            "Registration error:",
            error
        );

        registerButton.disabled = false;
        registerButton.textContent = "Register";


        message.textContent =
            error.message ||
            "Registration failed. Please try again.";
    });
}