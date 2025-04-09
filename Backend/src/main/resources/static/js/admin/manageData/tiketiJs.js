import { generateTable, initManagement } from "./manageDataJs.js";

let lastTimeout = null;

document.addEventListener("DOMContentLoaded", async () => {
    await initManagement("tiketi");
    init();
});

function init() {
    document.querySelector("#search-filter")
        .addEventListener("keyup", async e => {
            if (lastTimeout) clearTimeout(lastTimeout);

            lastTimeout = setTimeout(async () => {
                const order = document.querySelector("#by-column-filter").value;
                await generateTable("tiketi", 1, order, e.target.value);
            }, 500);
        });
}