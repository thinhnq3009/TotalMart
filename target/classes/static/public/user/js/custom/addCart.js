$(document).ready(function () {

    $('input#quantity').change(function () {
        const target = $(this);
        const quantity = target.val();
        const max = target.attr('max');
        const min = target.attr('min');
        if (quantity > max) {
            target.val(max);
        }
        if (quantity < min) {
            target.val(min);
        }

        $('[btn-add-cart]').attr('quantity', quantity);
    })

    $.clickHandler({
        attrName: "btn-add-cart",
        url: "/api/v1/carts/add",
        method: "POST",
        paramName: "productId",

        getParams: function (target) {
            const quantity = target.attr('quantity');

            return {
                quantity: quantity || 1
            }
        },
        success: function (data) {
            console.log(data);
            const message = data.message;
            notificer.addSuccess(message);
        },
        error: function (error) {
            error.responseJSON = error.responseJSON || {};
            console.log(error);
            const message = error.responseJSON.message;
            notificer.addError(message);
        }
    })


})