$(document).ready(function () {

    function updateTable({properties}) {
        const tableBody = $("#tableBody");
        const isEmptyProperties = $("#isEmptyProperties").innerHTML;
        console.log(isEmptyProperties)
        tableBody.html("");

        if (properties.length === 0) {
            tableBody.html(isEmptyProperties);
            return;
        }

        const newTableBody = isEmptyProperties + properties.reduce((innerHtml, property) => innerHtml + `
            <tr>
                <td>${property.name}</td>
               <td class="property-value" >
                   ${property.propertiesValue.map(item =>
            `<span 
                            class="badge bg-light-secondary rounded-3 py-2 text-secondary fs-2 d-inline-flex align-items-center me-3 mb-2 mb-md-0">
                            <span>${item.value}</span>
                            <i class="ti ti-square-rounded-minus fs-4 ms-3 cursor-pointer"
                                 product-property-id="${item.id}"></i> 
                        </span>`
        ).join("")}
                </td>
                <td>
                    ${property.info.canFilter ? `<span class="badge bg-primary fs-2 rounded-3 mx-2  mb-2 mb-md-0">Filter</span>` : ""}
                    ${property.info.canClassify ? `  <span class="badge bg-secondary fs-2 rounded-3 mx-2  mb-2 mb-md-0">Classify</span>` : ""}
                    ${property.info.isImportant ? `<span class="badge bg-danger fs-2 rounded-3 mx-2 mb-2 mb-md-0">Important</span>` : ""}
                </td>
            <td>
                <button type="button" class="btn btn-warning rounded-pill mb-2 mb-md-0" product-property-update="${property.name}">
                    <i class="ti ti-pencil fs-4"></i>
                </button>
                <button type="button" class="btn btn-danger rounded-pill ms-3 mb-2 mb-md-0" property-id="${property.info.id}">
                    <i class="ti ti-trash fs-4 "></i>
                </button>
            </td>
         </tr>
        `, "")
        tableBody.html(newTableBody)
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