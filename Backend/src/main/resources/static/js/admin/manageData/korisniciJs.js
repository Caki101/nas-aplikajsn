import { initManagement } from "./manageDataJs.js";

document.addEventListener("DOMContentLoaded", async () => {
    await initManagement("korisnici");
});