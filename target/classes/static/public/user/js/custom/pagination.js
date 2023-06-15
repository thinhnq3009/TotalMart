$(document).ready(function () {

    const pagination = $('.pagination');
    const url = new URL(window.location.href);
    let size =  $("[page-last]").attr('page-last');
    size = Number(size);

    pagination.find('a[page-first]').click(function () {
        url.searchParams.set('page', "0");
        location.href = url.href;
    });

    pagination.find('a[page-last]').click(function () {
        url.searchParams.set('page', size);
        location.href = url.href;
    });

    //page-index-active
    pagination.find('a[page-index]')
        .each(function () {
            const target = $(this);
            let page = target.attr('page-index');
            page = Number(page);
            target.text(Number(page) + 1);

            if (page < 0) {
                target.parent().addClass('d-none');
            }

            console.log(page, size)

            if (page > size) {
                target.parent().addClass('d-none');
            }
        })
        .click(function () {
        const target = $(this);
        const page = target.attr('page-index');

        target.text(page + 1);



        url.searchParams.set('page', page);
        location.href = url.href;

    })

    $('#paginationSize').change(function () {
        const size = $(this).val();
        url.searchParams.set('size', size);
        url.searchParams.set('page', "0");
        location.href = url.href;
    })

})