
document.addEventListener("DOMContentLoaded", async () => {
    if (sessionStorage.getItem("user") === null)
        window.location.href="http://localhost:8080/admin";
    else {
        // jwt token check
    }

    document.querySelector("#logout").addEventListener("click", () => logout());

    setActiveButton();

    document.querySelectorAll(".page-link").forEach(
        mb => mb.addEventListener("click", (e) => {
            //e.target.classList.toggle("active");
            let pathname = "/";
            if (e.target.id !== "dashboard") pathname = "/" + e.target.id;
            window.location.href = "http://localhost:8080/admin" + pathname;
        })
    );

    document.querySelectorAll(".menu-opener").forEach(
        mo => mo.addEventListener("click", (e) => {
            document.getElementById(e.target.id + "-menu").classList.toggle("open");
        })
    );
});

function setActiveButton() {
    let path = window.location.pathname.substring(6);

    if (path === "/") {
        document.querySelector("#dashboard").classList.add("active");
        return;
    }

    path = path.substring(1);

    document.getElementById(path).classList.add("active");

    if (path.search(/.+\/.+/g) > -1) {
        let menus = path.split("/");
        menus.pop();
        menus.forEach(menu => {
            document.getElementById(menu+"-menu").classList.toggle("open");
        });
    }
}

function logout() {
    sessionStorage.removeItem("user");
    window.location.href="http://localhost:8080/admin";
}