const role = localStorage.getItem("role");
const studentCode = localStorage.getItem("studentCode");


function loadStudents() {

    let url = "http://localhost:8080/students";

    // Student should only load their own profile
    if (role === "student") {

        if (!studentCode) {

            document.getElementById("studentList").innerHTML = `
                <tr>
                    <td colspan="4">
                        Student login information not found.
                    </td>
                </tr>
            `;

            return;
        }

        url =
            "http://localhost:8080/students/" +
            encodeURIComponent(studentCode);
    }


    fetch(url)

        .then(response => {

            if (!response.ok) {
                throw new Error("Failed to load student");
            }

            return response.json();
        })

        .then(data => {

            const studentList =
                document.getElementById("studentList");

            studentList.innerHTML = "";


            if (role === "student") {

                const student = data;

                const row =
                    document.createElement("tr");

                row.innerHTML = `
                    <td>${student.studentCode}</td>
                    <td>${student.name}</td>
                    <td>${student.email}</td>
                    <td>${student.phone}</td>
                `;

                studentList.appendChild(row);

                return;
            }



            const students = data;

            students.forEach(student => {

                const row =
                    document.createElement("tr");

                row.innerHTML = `
                    <td>${student.studentCode}</td>
                    <td>${student.name}</td>
                    <td>${student.email}</td>
                    <td>${student.phone}</td>
                `;

                studentList.appendChild(row);
            });


            if (students.length === 0) {

                studentList.innerHTML = `
                    <tr>
                        <td colspan="4">
                            No students found.
                        </td>
                    </tr>
                `;
            }

        })

        .catch(error => {

            console.error(
                "Error loading students:",
                error
            );

            document.getElementById("studentList").innerHTML = `
                <tr>
                    <td colspan="4">
                        Error loading student information.
                    </td>
                </tr>
            `;
        });
}


function searchStudent() {

    if (role === "student") {
        return;
    }


    const studentCode =
        document.getElementById("searchStudentCode").value.trim();


    if (studentCode === "") {

        loadStudents();

        return;
    }


    fetch(
        "http://localhost:8080/students/" +
        encodeURIComponent(studentCode)
    )

        .then(response => {

            if (!response.ok) {
                throw new Error("Student not found");
            }

            return response.json();
        })

        .then(student => {

            const studentList =
                document.getElementById("studentList");

            studentList.innerHTML = "";


            const row =
                document.createElement("tr");

            row.innerHTML = `
                <td>${student.studentCode}</td>
                <td>${student.name}</td>
                <td>${student.email}</td>
                <td>${student.phone}</td>
            `;

            studentList.appendChild(row);
        })

        .catch(error => {

            console.error(error);

            document.getElementById("studentList").innerHTML =
                `
                <tr>
                    <td colspan="4">
                        Student not found
                    </td>
                </tr>
                `;
        });
}


loadStudents();