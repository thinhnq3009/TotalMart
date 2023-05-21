$(document).ready(function () {

    $("*[del-category-id]").click(function () {
        const categoryId = $(this).attr("del-category-id");


        $.ajax({
            url: "/api/v1/categories/get?slug=" + categoryId,
            type: "GET",
            dataType: "json",
            success: function ({data}) {
                if (data.length === 0) {
                    notificer.addError("Can't find category with id: " + categoryId);
                } else {
                    data = data[0];
                    const modal = $("#confirmDeleteCategory");
                    modal.modal('show');
                    modal.find("#categoryName").text(data.name);
                    modal.find("#categorySlug").text(data.id);
                    modal.find(".productCount").text(data.productCount);
                    modal.find("#categoryId").val(data.id);
                }
            }
        })
    });

    $("#btnDeleteCategory").click(function () {
        const categoryId = $("#categoryId").val();

        if (!categoryId) return;

        $.ajax({
            url: "/api/v1/categories/delete",
            type: "DELETE",
            dataType: "json",
            data: {id: categoryId},
            success: function ({action, data}) {
                if (action === "delete") {
                    notificer.addSuccess(`Category ${data.name} is deleted`);
                    $("tr#" + categoryId).remove()
                } else {
                    notificer.addError("Delete category fail");
                }
            }
        });
    });

    $("#btnCloseConfirmModal").click(function () {
        this.find("span.mustClear").text("")
        this.find("input.mustClear").val("")
    })
});