$(document).ready(function () {
    const handelRestore = ({attrName, element, urlHandler}) => {
        const seletor = `${element}[${attrName}]`;

        $(seletor).click(function () {
            const button = $(this);

            $.ajax({
                url: urlHandler,
                method: "PUT",
                dataType: "json",
                data: {id: button.attr(attrName)},
                success: ({message, data}) => {
                    notificer.addSuccess(message)
                    button.parentsUntil("tbody").remove()
                }
            })

        })
    }

    handelRestore({
        attrName : 'restore-product-id',
        element: 'button',
        urlHandler: '/api/v1/products/restore'
    })

    handelRestore({
        attrName: 'restore-category-id',
        element: 'button',
        urlHandler: '/api/v1/categories/restore'
    })

    handelRestore({
        attrName: 'restore-group-id',
        element: 'button',
        urlHandler: '/api/v1/categories/group/restore'
    })
});