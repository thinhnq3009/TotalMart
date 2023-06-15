$(document).ready(function () {

    function updateTable({properties}) {
        $.ajax({
            url: location.pathname,
            method: "GET",
            success: function (resp) {
                $("#tableBody").empty();
                $(resp).find("#tableBody").children().appendTo("#tableBody")
            }
        })
    }

    function addProperty({name, value, types = {}}) {

        if (!value || !name) {
            return;
        }

        const productId = $("#productId").val();

        const data = {name, productId, value, ...types};

        $.ajax({
            url: "/api/v1/properties/add",
            method: "POST",
            data,
            dataType: "json",
            success: function (resp) {
                console.log(resp);
                updateTable(resp.data);
                if (resp.status === "success") {
                    if (resp.action === "update") {
                        notificer.addSuccess(`Property updated successfully`);
                    } else {
                        notificer.addSuccess(`Property added successfully`);
                    }
                }
            }
        })
    }

    function removeProperty({productId, propertyId, productPropertyId}) {
        const data
            = !productPropertyId ? {productId, propertyId} : {productPropertyId};

        console.log(data)
        $.ajax({
            url: "/api/v1/properties/delete",
            method: "DELETE",
            data,
            dataType: "json",
            success: function (resp) {
                console.log(resp);
                updateTable(resp.data);
                if (resp.status === "success") {
                    if (resp.action === "remove") {
                        notificer.addSuccess(`Remove property successfully`);
                    } else {
                        notificer.addError(`Remove property failed`);
                    }
                }
            }
        })
    }

    $("#newPropertySave").click(function () {
        const name = $("#newPropertyName")
        const value = $("#newPropertyValue")
        const types = {};
        $('input[type=checkbox]:checked').each(function () {
            const key = $(this).attr("name");
            types[key] = true;
        });
        addProperty({
            name: name.val(),
            value: value.val(),
            types
        });
        name.val("");
        value.val("");

    })

    $("#addProperty").click(function () {
        const name = $("#propertyName")
        const value = $("#propertyValue")

        addProperty({
            name: name.val(),
            value: value.val()
        })
        value.val("");
    })

    $('#tableBody').on("click", "button[property-id]", function () {
        const propertyId = $(this).attr("property-id");
        const productId = $("#productId").val();

        removeProperty({propertyId, productId})

    }).on("click", "button[product-property-update]", function () {
        // const propertyName = $(this).attr("product-property-update");
        // const propertyValue = $(this).parent().parent().find(".property-value").text();
        // $("#propertyName").val(propertyName).trigger("change");
        // $("#propertyValue").val(propertyValue)


    }).on("click", "i[product-property-id]", function () {
        const productPropertyId = $(this).attr("product-property-id");
        removeProperty({productPropertyId})
    })
})

