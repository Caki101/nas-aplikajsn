
async function submitPassword() {
    let email = window.location.search.substring(7);

    let password = document.getElementById("password").value;
    let confirmPassword = document.getElementById("confirm-password").value;

    if (password !== confirmPassword) return;

    let response = await fetch("http://localhost:8080/auth/change_password", {
        method: "POST",
        headers:{
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: email,
            password: password
        })
    });

    if (response.status === 200) console.log("Success, you can close this tab.");
    else console.log("Unsuccessful");
}