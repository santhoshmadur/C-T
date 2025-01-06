document.addEventListener('DOMContentLoaded', () => {
    var pfsCard = [...document.getElementsByClassName('pfs-card')];
    var closeIcon = [...document.getElementsByClassName('close_icon')];
    console.log(pfsCard, "pfs-card");

    pfsCard.forEach((element) => {
        element.addEventListener('click', (e) => {
            console.log(e, e.srcElement.offsetParent.className, element);
            if (element.classList.contains('active')) {
                element.classList.remove('active');
            } else {
                element.classList.add('active');
            }
        });
    });
});