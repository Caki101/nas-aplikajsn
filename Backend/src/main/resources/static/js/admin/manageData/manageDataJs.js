import {API_KEY} from "../../config.js";

// need to cache every page while it's on that table type
// for table need to cache upon first entering smestaj choice menu all smestajs

let created_page_numbers = false;
let current_type;
let keys;

export async function initManagement(type) {
    await generateTable(type,1, null);

    document.querySelector("#previous-page")
        .addEventListener("click", () => changePage(false));
    document.querySelector("#next-page")
        .addEventListener("click", () => changePage(true));

    const filter_div = document.querySelector("#filter-div");

    for (const key of keys) {
        const button = document.createElement("button");
        button.textContent = key;

        button.classList.add("filter-btn");
        if (key === "id") button.classList.add("active-filter");

        button.addEventListener("click", () => {
            document.getElementById("table").innerHTML = "";
            generateTable(current_type, 1, key==="smestaj"?"smestaj_id":key);
            document.querySelectorAll(".filter-btn")
                .forEach(el => el.classList.remove("active-filter"));
            button.classList.add("active-filter");

            document.querySelector(".active-page").classList.remove("active-page");
            document.querySelector("#page1").classList.add("active-page");
        });

        filter_div.appendChild(button);
    }
}

export async function generateTable(type,page,filter) {
    if (current_type !== type) created_page_numbers = false;
    current_type = type;
    switch (type) {
        case "korisnici":
            type = "allU";
            break;
        case "smestaji":
            type = "allS";
            break;
        case "tiketi":
            type = "allT";
            break;
        default:
            console.error("Unknown type");
            return;
    }

    let url = "http://localhost:8080/api/admin/"+type+"?offset="+(page-1);
    if (filter) url += "&filter="+filter;

    const table = await fetch(url, {
        headers: {
            "Api-Key-Header": API_KEY
        }
    }).then((response) => response.json());

    if (!table) return;

    await makeTable(table);

    if(!created_page_numbers) {
        created_page_numbers = true;
        makePageNums(type).finally();
    }
}

async function makeTable(table) {
    keys = Object.keys(table[0]);
    const table_element = document.getElementById("table");

    const data = document.createElement("li");
    const row = document.createElement("ul");
    row.classList.add("table-row");
    let col = document.createElement("li");
    col.classList.add("entity-attribute");

    row.id = "table-headers";
    for (const key of keys) {
        col.textContent = key.charAt(0).toUpperCase() + key.slice(1).replace("_", " ");
        row.append(col.cloneNode(true));
    }

    data.insertBefore(row.cloneNode(true), null);
    table_element.insertBefore(data.cloneNode(true), null);

    table.forEach(o => {
        data.innerHTML = "";
        row.innerHTML = "";
        col.innerHTML = "";

        row.id = "id" + o.id;

        for (const key of keys) {
            if (key === "smestaj") col.textContent = o[key].ime_smestaja;
            else col.textContent = o[key];

            col.classList.add(key);
            row.append(col.cloneNode(true));
            col.classList.remove(key);
        }

        data.insertBefore(row.cloneNode(true), null);
        table_element.insertBefore(data.cloneNode(true), null);
    });
}

async function makePageNums(type) {
    // temporary
    const all = await fetch("http://localhost:8080/api/"+type, {
        headers: {
            "Api-Key-Header": API_KEY
        }
    }).then((response) => response.json());

    const pages = all.length;
    const pages_element = document.getElementById("pages");

    for (let i = 0; i < pages/10; i++) {
        const element = document.createElement("div");
        element.textContent = (i+1).toString();
        element.id = "page" + (i + 1);

        pages_element.insertBefore(element.cloneNode(true), null);
    }

    document.getElementById("page1").classList.add("active-page");
}

export async function changePage(np) {
    const active_page = document.querySelector(".active-page");
    let changed_page_num = parseInt(active_page.id.substring(4));

    np?changed_page_num += 1:changed_page_num -= 1;

    if (changed_page_num < 1 || changed_page_num > document.getElementById("pages").children.length) return;

    document.getElementById("table").innerHTML = "";

    let filter = document.querySelector(".active-filter");
    if (filter !== null) filter = filter.innerText;

    await generateTable(current_type, changed_page_num, filter==="smestaj"?"smestaj_id":key);
    active_page.classList.toggle("active-page");
    document.querySelector("#page"+changed_page_num).classList.toggle("active-page");
}