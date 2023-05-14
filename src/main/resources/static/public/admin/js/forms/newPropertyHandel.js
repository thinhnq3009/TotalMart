$(document).ready(function () {

    function updateTable({ properties}) {
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
                   ${property.propertiesValue.map( item => 
                            ` <span 
                                class="badge bg-light-secondary rounded-3 py-2 text-secondary fs-2 d-inline-flex align-items-center me-3">
                        <span>${item.value}</span>
                        <i class="ti ti-square-rounded-minus fs-4 ms-3"></i>
                    </span>`
                       ).join("")}
                </td>
                <td>
                    ${property.info.canFilter ? `<span class="badge bg-primary fs-2 rounded-3 mx-2">Filter</span>` : ""}
                    ${property.info.canClassify ? `  <span class="badge bg-secondary fs-2 rounded-3 mx-2">Classify</span>` : ""}
                    ${property.info.isImportant ? `<span class="badge bg-danger fs-2 rounded-3 mx-2">Important</span>` : ""}
                </td>
            <td>
                <button type="button" class="btn btn-warning rounded-pill" product-property-update="${property.name}">
                    <i class="ti ti-pencil fs-4"></i>
                </button>
                <button type="button" class="btn btn-danger rounded-pill ms-3" product-property-id="${property.info.id}">
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

    $("#newPropertySave").click(function () {
        const name = $("#newPropertyName").val();
        const value = $("#newPropertyValue").val();
        const types = {};
        $('input[type=checkbox]:checked').each(function () {
            const key = $(this).attr("name");
            types[key] = true;
        });
        addProperty({
            name, value, types
        });
    })

    $("#addProperty").click(function () {
        const name = $("#propertyName").val();
        const value = $("#propertyValue").val();

        addProperty({name, value})

    })

    $('#tableBody').on( "click", "button[product-property-id]", function (){
        const productPropertyId = $(this).attr("product-property-id");
        $.ajax({
            url: "/api/properties/delete",
            method: "DELETE",
            data: {productPropertyId},
            dataType: "json",
            success: function (resp) {
                console.log(resp);
                updateTable(resp.data);
                if (resp.status === "success") {
                    notificer.addSuccess(`Property deleted successfully`);
                }
            }
        })
    }).on("click", "button[product-property-update]", function () {
        // const propertyName = $(this).attr("product-property-update");
        // const propertyValue = $(this).parent().parent().find(".property-value").text();
        // $("#propertyName").val(propertyName).trigger("change");
        // $("#propertyValue").val(propertyValue)


    })
})