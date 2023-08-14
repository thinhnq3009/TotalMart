$(document).ready(function () {

    $.updateUrl = function ({
                                elementSelector,
                                getParams = () => {
                                },
                                paramName,
                                value,
                                eventName = 'click',
                            }) {

        const $element = $(elementSelector);

        $element.on(eventName, function () {

            const element = $(this);

            const params = getParams(element);

            const url = new URL(window.location.href);

            if(Array.isArray(params)){
                params.forEach(item => {
                    url.searchParams.set(item.name, item.value);
                })
            } else if (params.name) {
                url.searchParams.set(params.name, params.value);
            } else {
                throw new Error('params must be array or object');
            }

            location.href = url.href;

        })

    }

    $.updateUrl({
        elementSelector: 'a[pagination-page], button[pagination-page]',
        getParams: (element) => {
            return [{
                name: 'page',
                value: element.attr('pagination-page')
            }]
        }
    })

    $.updateUrl({
        elementSelector: 'select[pagination-size]',
        getParams: (element) => {
            return [{
                name: 'size',
                value: element.val()
            }]
        },
        eventName: 'change'
    })



})