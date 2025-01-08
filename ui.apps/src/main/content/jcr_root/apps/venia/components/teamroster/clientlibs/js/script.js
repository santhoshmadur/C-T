document.addEventListener('DOMContentLoaded', () => {
    var pfsCard = [...document.querySelectorAll('.grid-3cl .pfs-card')];
    var closeIcon = [...document.querySelectorAll('.grid-3cl .pfs-card .close_icon')];
    console.log(pfsCard, "pfs-card");

    pfsCard.forEach((element) => {
        element.addEventListener('click', (e) => {
            console.log(e, e.target.offsetParent.className, element);
            if (element.classList.contains('active')) {
                element.classList.remove('active');
            } else {
                element.classList.add('active');
            }
        });
    });
});