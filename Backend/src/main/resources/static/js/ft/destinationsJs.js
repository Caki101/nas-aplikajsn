import { API_KEY } from "../config.js";

document.addEventListener("DOMContentLoaded", async _ => {
    await init();
});

async function init() {
    const res = await fetch("/api/available-countries", {
        headers: {
            "Api-Key-Header": API_KEY
        }
    });
    if (!res.ok) {
        console.error(res);
        return;
    }

    const data = await res.json();

    for (const country of data) {
        drawCountryCard(country);
    }
}

function drawCountryCard(country) {
    const country_div = document.createElement("div");
    country_div.id = country;
    country_div.className = "country-card";
    document.querySelector(".segment-body").appendChild(country_div);
    country_div.style.backgroundImage = "url(/public-api/image/" + country.replaceAll(" ","-").toLowerCase() + ")";

    const ci_wrapper = document.createElement("div");
    ci_wrapper.className = "country-info-wrapper";
    country_div.appendChild(ci_wrapper);

    const ci = document.createElement("div");
    ci.className = "country-info";
    ci.textContent = country;
    ci_wrapper.appendChild(ci);


}