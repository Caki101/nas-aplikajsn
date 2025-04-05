import {API_KEY} from "../config.js";

document.addEventListener("DOMContentLoaded", async _ => {
    await fillLatestTable();
    await tiketiGraph();
    await smestajiGraph();
});

async function fillLatestTable() {
    const lbt_tbody = document.querySelector(".latest-buys-table tbody");
    const req = await fetch("http://localhost:8080/api/admin/allKT?size=5&page=0&sort=id,asc", {
        headers: {
            "Api-Key-Header": API_KEY
        }
    });
    const data = await req.json();

    for (const item of data.content) {
        const row = document.createElement("tr");
        row.id = "id" + item.id;

        const col1 = document.createElement("td");
        col1.id = "id" + item.id;
        col1.textContent = item.id;
        row.appendChild(col1);
        lbt_tbody.appendChild(row);

        const col2 = document.createElement("td");
        col2.id = "tiket-id";
        col2.textContent = item.tiket.id;
        row.appendChild(col2);
        lbt_tbody.appendChild(row);

        const col3 = document.createElement("td");
        col3.id = "smestaj";
        col3.textContent = item.tiket.smestaj.ime_smestaja;
        row.appendChild(col3);
        lbt_tbody.appendChild(row);

        const col4 = document.createElement("td");
        col4.id = "sezona";
        col4.textContent = item.tiket.sezona;
        row.appendChild(col4);
        lbt_tbody.appendChild(row);

        const col5 = document.createElement("td");
        col5.id = "cena";
        col5.textContent = item.tiket.cena;
        row.appendChild(col5);
        lbt_tbody.appendChild(row);

        const col6 = document.createElement("td");
        col6.id = "korisnik";
        col6.textContent = item.user.username;
        row.appendChild(col6);
        lbt_tbody.appendChild(row);

        const col7 = document.createElement("td");
        col7.id = "datum-kupovine";
        col7.textContent = item.timestamp;
        row.appendChild(col7);
        lbt_tbody.appendChild(row);
    }
}

async function tiketiGraph() {
    const ctx = document.querySelector('#tikets-graph').getContext('2d');
    const req = await fetch("http://localhost:8080/api/admin/top5tikets", {
        headers: {
            "Api-Key-Header": API_KEY
        }
    });
    console.log(req);

    const data = await req.json();
    console.log(data);

    let labels_list = [];
    let data_list = [];
    for (const item of data) {
        labels_list.push((item[0].id).toString());
        data_list.push(item[1]);
    }

    new Chart(ctx, {
        type: 'doughnut', // this is what makes it a donut chart
        data: {
            labels: labels_list,
            datasets: [{
                label: 'Votes',
                data: data_list,
                backgroundColor: Array.from({ length: 5 },() => `rgb(${Math.floor(Math.random()*256)}, ${Math.floor(Math.random()*256)}, ${Math.floor(Math.random()*256)})`),
                borderWidth: 1
            }]
        },
        options: {
            cutout: '70%',
            responsive: true,
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

async function smestajiGraph() {
    const ctx = document.querySelector('#smestajs-graph').getContext('2d');
    const req = await fetch("http://localhost:8080/api/admin/top5smestajs", {
        headers: {
            "Api-Key-Header": API_KEY
        }
    });
    console.log(req);

    const data = await req.json();
    console.log(data);

    let labels_list = [];
    let data_list = [];
    for (const item of data) {
        labels_list.push((item[0].ime_smestaja).toString());
        data_list.push(item[1]);
    }

    new Chart(ctx, {
        type: 'doughnut', // this is what makes it a donut chart
        data: {
            labels: labels_list,
            datasets: [{
                label: 'Votes',
                data: data_list,
                backgroundColor: Array.from({ length: 5 },() => `rgb(${Math.floor(Math.random()*256)}, ${Math.floor(Math.random()*256)}, ${Math.floor(Math.random()*256)})`),
                borderWidth: 1
            }]
        },
        options: {
            cutout: '70%', // controls the size of the donut hole
            responsive: true,
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}