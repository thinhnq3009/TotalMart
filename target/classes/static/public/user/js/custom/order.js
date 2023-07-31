$(document).ready(function () {
    $("#dayAgo").change(function () {
        const value = $("#dayAgo").val();
        const url = new URL(window.location.href);
        url.searchParams.set('dayAgo', value);
        location.href = url.href;
    });
    $("#status").change(function () {
        const value = $("#status").val();
        const url = new URL(window.location.href);
        url.searchParams.set('status', value);
        location.href = url.href;
    });

    $.clickHandler({
        tagSelector: "button",
        attrName: "data-order-confirm-id",
        method: "PUT",
        url: "/api/v1/order/confirm",
        paramName: "id",
        success: function (data, element) {
            notificer.addSuccess(data.message);
            setTimeout(() => {
                location.reload();
            }, 1000);
        },
        error: function (data, element) {
            console.log(data)
            notificer.addError(data.message);
        }
    })

    $.clickHandler({
        tagSelector: "button",
        attrName: "data-order-cancel-id",
        method: "PUT",
        url: "/api/v1/order/cancel",
        paramName: "id",
        success: function (data, element) {
            notificer.addSuccess(data.message);
            setTimeout(() => {
                location.reload();
            }, 1000);
        },
        error: function (data, element) {
            console.log(data)
            notificer.addError(data.message);
        }
    })

    $.clickHandler({
        tagSelector: "button",
        attrName: "data-order-delivering-id",
        method: "PUT",
        url: "/api/v1/order/delivery",
        paramName: "id",
        success: function (data, element) {
            notificer.addSuccess(data.message);
            setTimeout(() => {
                location.reload();
            }, 1000);
        },
        error: function (data, element) {
            console.log(data)
            notificer.addError(data.message);
        }
    })


})