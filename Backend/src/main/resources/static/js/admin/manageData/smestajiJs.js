import {API_KEY} from "../../config";

document.addEventListener("DOMContentLoaded", async () => {
    await init();
});

async function init() {
    await makeTable(1);

    // temporary
    const allS = await fetch("http://localhost:8080/api/allS", {
        headers: {
            "Api-Key-Header": API_KEY
        }
    }).then((response) => response.json());

    const pages = allS.length;
    const pages_element = document.getElementById("pages");

    for (let i = 0; i < pages/10; i++) {
        const element = document.createElement("div");
        element.textContent = (i+1).toString();
        element.id = "page" + (i + 1);

        pages_element.insertBefore(element.cloneNode(true), null);
    }

    document.getElementById("page1").classList.add("active-page");
}

async function makeTable(page) {
    const table = await fetch("http://localhost:8080/api/admin/allS?offset="+(page-1), {
        headers: {
            "Api-Key-Header": API_KEY
        }
    }).then((response) => response.json());

    const table_element = document.getElementById("table");
    table.forEach(smestaj => {
        let data = document.createElement("li");
        let row = document.createElement("ul");
        row.classList.add("table-row");
        let col = document.createElement("li");

        col.textContent = smestaj.id;
        row.append(col.cloneNode(true));

        col.textContent = smestaj.ime_smestaja;
        row.append(col.cloneNode(true));

        col.textContent = smestaj.drzava;
        row.append(col.cloneNode(true));

        col.textContent = smestaj.grad;
        row.append(col.cloneNode(true));

        col.textContent = smestaj.ocena;
        row.append(col.cloneNode(true));

        data.insertBefore(row, null);
        table_element.insertBefore(data, null);
    });
}

async function changePage(np) {
    const active_page = document.querySelector(".active-page");
    const active_page_num = parseInt(active_page.id.substring(4));
    let changed_page_num = active_page_num;

    if (np) changed_page_num += 1;
    else changed_page_num -= 1;

    if (changed_page_num < 1 || changed_page_num > document.getElementById("pages").children.length) {
        console.log("err");
        return;
    }
    console.log(changed_page_num,active_page_num);

    document.getElementById("table").innerHTML = "";

    await makeTable(changed_page_num);
    active_page.classList.toggle("active-page");
    document.querySelector("#page"+changed_page_num).classList.toggle("active-page");
}