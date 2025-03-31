import { API_KEY } from '../config.js';

// need implementation for checking with jwt token if admin is logged in and if token is still valid

document.addEventListener('DOMContentLoaded', () => {
    document.querySelector("#submit")
        .addEventListener('click', _ => submitForm());
    document.addEventListener('keyup', e => {
        e.preventDefault();
        if (e.code === "Enter") submitForm().finally();
    });
});

async function submitForm() {
    const username = document.querySelector("#username").value;
    const password = document.querySelector("#password").value;

    if (!username || !password) {
        // error display
        return;
    }

    const response = await fetch("http://localhost:8080/api/admin_login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Api-Key-Header": API_KEY
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    });

    if (response.ok) {
        sessionStorage.setItem("user", username);
        window.location.href="http://localhost:8080/admin/";
    }
}