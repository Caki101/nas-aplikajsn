import { generateTable, initManagement } from "./manageDataJs.js";

document.addEventListener("DOMContentLoaded", async () => {
    await initManagement("tiketi");
    init();
});

function init() {
    document.querySelector("#search-filter")
        .addEventListener("keyup", async e => {
            setTimeout(async () => {
                const order = document.querySelector("#by-column-filter").value;
                await generateTable("tiketi", 1, order, e.target.value);
            }, 500);
        });
}