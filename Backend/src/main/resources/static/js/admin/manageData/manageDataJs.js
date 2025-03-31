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
    if (type !== "korisnici") {
        inputDivContent();

        const input_btn = document.querySelector("#input-btn");
        const add_entity_div = document.querySelector(".add-entity-div");

        input_btn.addEventListener("click", _ => {
            input_btn.classList.add("hidden");
            add_entity_div.classList.remove("hidden");
        });
        document.querySelector("#exit-adding")
            .addEventListener("click", _ => {
                add_entity_div.classList.add("hidden");
                input_btn.classList.remove("hidden");
            });
        document.querySelector("#create-help")
            .addEventListener("click", _ => generateOverlay("Help", createHelpOverlayContent()));
    }
    document.querySelector("#table-help")
        .addEventListener("click", _ => generateOverlay("Help", tableHelpOverlayContent()));

    const bcf = document.querySelector("#by-column-filter");

    for (const key of keys) {
        const option = document.createElement("option");

        option.textContent = key;
        option.classList.add("column-options");

        bcf.appendChild(option);
    }

    bcf.addEventListener("change", e => generateTable(current_type, 1, e.target.value));
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
    table_element.innerHTML = "";

    const thead = document.createElement("thead");
    const tbody = document.createElement("tbody");

    const row = document.createElement("tr");
    row.classList.add("table-row");

    const colh = document.createElement("th");
    colh.classList.add("entity-attribute");
    row.id = "table-headers";
    for (const key of keys) {
        colh.textContent = key.charAt(0).toUpperCase() + key.slice(1).replace("_", " ");
        row.append(colh.cloneNode(true));
    }

    colh.textContent = "Actions";
    colh.colSpan = 2;
    row.append(colh.cloneNode(true));

    thead.appendChild(row.cloneNode(true));
    table_element.appendChild(thead);

    table.forEach(o => {
        row.innerHTML = "";
        row.id = "id" + o.id;

        for (const key of keys) {
            const col = document.createElement("td");
            col.classList.add("entity-attribute");

            if (key === "smestaj") col.textContent = o[key].ime_smestaja;
            else col.textContent = o[key];

            col.classList.add(key);
            row.append(col);
        }

        modifyAndDeleteBtns(row);

        tbody.appendChild(row.cloneNode(true));
    });
    table_element.insertBefore(tbody, null);
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

function generateOverlay(header_name, content) {
    const overlay = document.createElement("div");
    overlay.className = "overlay";

    const main_div = document.createElement("div");
    main_div.className = "overlay-content";

    const header_div = document.createElement("div");
    header_div.className = "oc-header-div";

    const header = document.createElement("h2");
    header.textContent = header_name;
    header.classList.add("oc-header");

    const button = document.createElement("button");
    button.textContent = "X";
    button.classList.add("exit-btn");
    button.addEventListener("click", _ => {
        document.body.removeChild(overlay);
    });

    header_div.appendChild(header);
    header_div.appendChild(button);
    main_div.appendChild(header_div);

    main_div.appendChild(content);

    overlay.appendChild(main_div);
    document.body.append(overlay);
}

function inputDivContent() {
    const input_div = document.querySelector(".add-entity-div");

    const inputs = makeInputs();
    input_div.appendChild(inputs);

    const save_btn = document.createElement("button");
    save_btn.textContent = "Save";
    save_btn.classList.add("overlay-save-btn");
    save_btn.addEventListener("click", async _ => {
        console.log(current_type);
        //await fetch("http://localhost:8080/api/so"+);
    });
    input_div.appendChild(save_btn);
}

function makeInputs() {
    const inputs_div = document.createElement("div");
    inputs_div.className = "inputs-div";

    for(const key of keys) {
        const label = document.createElement("label");
        label.className = "overlay-input-label"
        label.textContent = key + ":";
        label.htmlFor = key + "-input";

        const input = document.createElement("input");
        input.id = key + "-input";
        input.className = "overlay-input";
        input.placeholder = key + "..";

        label.appendChild(input);
        inputs_div.appendChild(label);
    }

    return inputs_div;
}

function createHelpOverlayContent() {
    const body_div = document.createElement("div");
    body_div.className = "oc-body";

    const p = document.createElement("p");
    p.innerHTML = "<b>Input:</b> Will make an overlay with input fields to add single data to the active table." +
        "<br><br>" +
        "<b>Json:</b> Used for testing mostly. Through json format, able to add multiple data to the active table." +
        "<br><br>" +
        "Json structure:" +
        "<br>" +
        "<pre>{<br>" +
        "   \"col1\": value,<br>" +
        "   \"col2\": value,<br>" +
        "   \"col3\": value<br>" +
        "   ...<br>" +
        "}</pre>" +
        "<br><br>" +
        "<b>Notes:</b>" +
        "<pre>" +
        "   - Do not include id column,<br>" +
        "   - Every col# should be exactly named as columns in table." +
        "</pre>" +
        "<br>" +
        "<i><b>EXCEPTION:</b> For tiket data structure instead of smestaj field it needs to be smestaj_id.</i>";

    body_div.appendChild(p);

    return body_div;
}

function tableHelpOverlayContent() {
    const body_div = document.createElement("div");
    body_div.className = "oc-body";

    const p = document.createElement("p");
    p.innerHTML = "Table shows all entities of selected table and allows admin to modify/delete entities." +
        "<br><br>" +
        "<b>Order by:</b> Orders table by the given attribute.";

    body_div.appendChild(p);

    return body_div;
}

function modifyAndDeleteBtns(row) {
    const col_modify = document.createElement("td");
    col_modify.classList.add("entity-attribute");
    col_modify.classList.add("entity-modify");
    col_modify.textContent = "✎";
    col_modify.addEventListener("click", async _ => {
        // const response = await fetch("http://localhost:8080/api/");
        //
        // if (response.ok) {
        //     console.log(response);
        // }
    });

    const col_delete = document.createElement("td");
    col_delete.classList.add("entity-attribute");
    col_delete.classList.add("entity-delete");
    col_delete.textContent = "🗑";
    col_delete.addEventListener("click", async _ => {
        // const response = await fetch("http://localhost:8080/api/");
        //
        // if (response.ok) {
        //     console.log("Ok");
        // }
        // else console.log(response);
    });

    row.appendChild(col_modify);
    row.appendChild(col_delete);
}