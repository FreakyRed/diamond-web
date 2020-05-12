function fillStars(Object){
    let stars = document.getElementsByName(Object.name);
    let numberOfStars = parseInt(Object.id);

    stars.forEach((i) => {
        (i <= Object.id) ? stars[i].addClass("filled") : stars[i].addClass("notFilled");
    })

    document.getElementById("numberOfStars").value = numberOfStars + 1;
}

$('.stars a').on('click', function(){
    $('.stars span, .stars a').removeClass('active');

    $(this).addClass('active');
    $('.stars span').addClass('active');
});