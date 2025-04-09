import { initManagement } from "./manageDataJs.js";
import { API_KEY } from "../../config.js";

document.addEventListener("DOMContentLoaded", async () => {
    await initManagement("korisnici");
    await init();
});

async function init() {
    document.querySelector("#promote-btn")
        .addEventListener("click", async _ => {
            const username = document.querySelector("#user-input").value;

            if (username === "") {
                console.log("Please enter username");
                return;
            }

            const res = await fetch("http://localhost:8080/api/promote", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Api-Key-Header": API_KEY
                },
                body: JSON.stringify({
                    "username": username,
                })
            });
            console.log(await res.text());
        });
}