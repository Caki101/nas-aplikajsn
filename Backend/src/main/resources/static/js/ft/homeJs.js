import {API_KEY} from "../config.js";

document.addEventListener("DOMContentLoaded", async _ => {
    await init();
});

async function init() {
    const res = await fetch("http://localhost:8080/api/best-offers", {
        headers: {
            "Content-Type": "application/json",
            "Api-Key-Header": API_KEY
        }
    });

    if (!res.ok) {
        console.error(res);
        return;
    }

    const data = await res.json();

    let cc = 1;
    for (const row of data) {
        const boc = document.querySelector("#boc" + cc);
        boc.style.backgroundImage = "url(/public-api/first-image/" + row[0] + ")";

        boc.href = "/ft/destination/" + row[0];

        document.querySelector("#bot" + cc).textContent = row[1];

        document.querySelector("#bo-info" + cc).textContent = parseFloat(row[2]).toFixed(0) + "$ / night";

        document.querySelector("#bor" + cc).textContent = "★" + parseFloat(row[3]).toFixed(1);

        cc += 1;
    }
}