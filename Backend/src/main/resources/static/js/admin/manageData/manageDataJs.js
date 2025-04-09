import { API_KEY } from "../../config.js";

let created_page_numbers = false;
let current_type;
let keys;

export async function initManagement(type) {
    await generateTable(type,1, null, null);

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
    else {
        document.querySelector("#promote-help")
            .addEventListener("click", _ => generateOverlay("Help", promoteHelpOverlayContent()));
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

export async function generateTable(type,page,filter,search) {
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
            // need error handling
            console.error("Unknown type");
            return;
    }

    let url = "http://localhost:8080/api/admin/"+type+"?page="+(page-1);
    if (filter) url += "&sort="+filter;
    if (search) url+= "&search="+search;

    const res = await fetch(url, {
        headers: {
            "Api-Key-Header": API_KEY
        }
    });

    const data = await res.json();
    const table = data.content;

    // need error handling
    if (table.length === 0 || !table) console.error("Empty table");
    else keys = Object.keys(table[0]);

    await makeTable(table);

    if(!created_page_numbers) {
        created_page_numbers = true;
        makePageNums(data.totalElements).finally();
    }
}

export async function makeTable(table) {
    const table_element = document.getElementById("table");
    table_element.innerHTML = "";

    const thead = document.createElement("thead");
    const tbody = document.createElement("tbody");

    const rowh = document.createElement("tr");
    rowh.classList.add("table-row");

    const colh = document.createElement("th");
    colh.classList.add("entity-attribute");
    rowh.id = "table-headers";
    for (const key of keys) {
        colh.textContent = key.charAt(0).toUpperCase() + key.slice(1).replace("_", " ");
        rowh.append(colh.cloneNode(true));
    }

    colh.textContent = "Actions";
    colh.colSpan = 2;
    rowh.append(colh.cloneNode(true));

    thead.appendChild(rowh);
    table_element.appendChild(thead);
    table_element.appendChild(tbody);

    table.forEach(o => {
        const row = document.createElement("tr");
        row.classList.add("table-row");
        row.id = "id" + o.id;

        tbody.appendChild(row);

        for (const key of keys) {
            const col = document.createElement("td");
            col.classList.add("entity-attribute");

            if (key === "smestaj") {
                col.textContent = o[key].ime_smestaja;
                col.id = o[key].id;
            }
            else col.textContent = o[key];

            col.classList.add(key);
            row.append(col);
        }

        modifyAndDeleteBtns(row, o);
    });

    document.querySelectorAll("td").forEach(o => {
        if (o.classList.contains("smestaj")) {
            o.addEventListener("click", _ => generateOverlay("Smestaj", showSmestajInfo(o,table)));
        }
    });
}

async function makePageNums(total) {
    const pages_element = document.getElementById("pages");

    for (let i = 0; i < total/10; i++) {
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

    let filter = document.querySelector("#by-column-filter").value;

    const search = document.querySelector("#search-filter").value;
    await generateTable(current_type, changed_page_num, filter==="smestaj"?"smestaj_id":filter, search);
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

    const inputs = makeInputs(false);
    input_div.appendChild(inputs);

    const save_btn = document.createElement("button");
    save_btn.textContent = "Save";
    save_btn.classList.add("save-btn");
    save_btn.addEventListener("click", async _ => {
        const obj = {};

        for (const key of keys) {
            if (key !== "id") obj[key] = document.querySelector("#"+key+"-input").value;
        }

        const res = await fetch("http://localhost:8080/api/so" + current_type[0].toUpperCase() + current_type.substring(1), {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Api-Key-Header": API_KEY
            },
            body: JSON.stringify(obj)
        });

        if (res.status === 201) await regenerateTable();
    });
    input_div.appendChild(save_btn);
}

function makeInputs(overlay) {
    const inputs_div = document.createElement("div");
    inputs_div.className = "inputs-div";
    const numeric = ["ocena","trajanje_odmora","broj_osoba","broj_tiketa","cena"];

    for(const key of keys) {
        if (key === "id") continue;
        const label = document.createElement("label");
        label.className = "overlay-input-label"
        label.textContent = key + ":";
        label.htmlFor = key + "-input";

        const input = document.createElement("input");
        if (overlay) input.id = "overlay-" + key + "-input";
        else input.id = key + "-input";
        input.className = "overlay-input";
        input.placeholder = key + "..";
        if (numeric.find(n => n === key)) input.type = "number";

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
        "<b>Order by:</b> Orders table by the given attribute." +
        "<br><br>" +
        "<b>Note:</b> For Tiketi table clicking on row's smestaj column opens overlay of more details about that Smestaj.";

    body_div.appendChild(p);

    return body_div;
}

function modifyAndDeleteBtns(row, entity) {
    if (current_type !== "korisnici") {
        const col_modify = document.createElement("td");
        col_modify.classList.add("entity-attribute");
        col_modify.classList.add("entity-modify");
        col_modify.textContent = "✎";

        row.appendChild(col_modify);

        col_modify.addEventListener("click", async _ => {
            generateOverlay("Modify Entity #" + entity.id, modifyOverlayContent(entity));
        });
    }

    const col_delete = document.createElement("td");
    col_delete.classList.add("entity-attribute");
    col_delete.classList.add("entity-delete");
    col_delete.textContent = "🗑";

    row.appendChild(col_delete);

    col_delete.addEventListener("click", async _ => {
        const confirmed = confirm("Are you sure you want to delete this?");

        if (confirmed) {
            const res = await fetch("http://localhost:8080/api/do-" + current_type, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    "Api-Key-Header": API_KEY
                },
                body: JSON.stringify(entity)
            });

            if (res.status === 200 || res.status === 204) await regenerateTable();
        }
    });
}

function showSmestajInfo(o,table) {
    const smestaj = table.filter(obj => obj.id === parseInt(o.parentElement.id.substring(2)))[0]["smestaj"];
    let smestaj_to_string = "";
    smestaj_to_string += "id: " + smestaj["id"] + "<br>";
    smestaj_to_string += "ime_smestaja: " + smestaj["ime_smestaja"] + "<br>";
    smestaj_to_string += "drzava: " + smestaj["drzava"] + "<br>";
    smestaj_to_string += "grad: " + smestaj["grad"] + "<br>";
    smestaj_to_string += "ocena: " + smestaj["ocena"];

    const body_div = document.createElement("div");
    body_div.className = "oc-body";

    const p = document.createElement("p");
    p.innerHTML = smestaj_to_string;

    body_div.appendChild(p);

    return body_div;
}

function modifyOverlayContent(entity) {
    const body_div = document.createElement("div");
    body_div.className = "oc-body";

    body_div.appendChild(makeInputs(true));

    for (const key of keys) {
        if (key === "id") continue;
        body_div.querySelector("#overlay-" + key + "-input").value = entity[key];
    }

    const save_button = document.createElement("button");
    save_button.textContent = "Save";

    body_div.appendChild(save_button);

    save_button.addEventListener("click", async _ => {
        const confirmed = confirm("Are you sure you want to update this?");

        if (!confirmed) {return;}

        for (const key of keys) {
            if (key === "id") continue;
            entity[key] = document.querySelector("#overlay-" + key + "-input").value;
        }

        const res = await fetch("http://localhost:8080/api/uo-" + current_type, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Api-Key-Header": API_KEY
            },
            body: JSON.stringify(entity)
        });
        if (res.status === 200 || res.status === 204) {
            document.querySelector(".exit-btn").click();
            await regenerateTable();
        }
    });

    return body_div;
}

function promoteHelpOverlayContent() {
    const body_div = document.createElement("div");
    body_div.className = "oc-body";

    const p = document.createElement("p");
    p.innerHTML = "Input username of the user you wish to promote," +
        "and then choose which level of promotion he should receive." +
        "<br><br>" +
        "<b>Admin:</b> Admin page access.";

    body_div.appendChild(p);

    return body_div;
}

async function regenerateTable() {
    const page = document.querySelector(".active-page").textContent;
    const filter = document.querySelector("#by-column-filter").value;
    const search = document.querySelector("#search-filter").value;
    await generateTable(current_type,parseInt(page),filter,search);
}