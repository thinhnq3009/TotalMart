$(document).ready(function () {

    // Handel category group change
    $("#categoryGroup").change(function () {
        const categoryId = $(this).val();
        $.ajax({
            dataType: "json",
            success: function (response) {

                $("#category").empty().append(response.data
                    .map(category => `
                        <option value="${category.id}">${category.name}</option>
                    `)
                    .join("")
                )
                ;


            },
            type: "GET",
            url: `/api/v1/categories/group/${categoryId}`
        })
    })

    // Handel upload image with DropzoneJS
    // Huỷ bỏ dropzone mặc định
    const dropzone = Dropzone.forElement("#preview-image-dropzone");
    if (dropzone) {

        dropzone.options.maxFiles = 10;
        dropzone.options.maxFilesize = 5;
        dropzone.options.acceptedFiles = ".jpeg,.jpg,.png,.gif";
        dropzone.options.addRemoveLinks = true;
        dropzone.options.dictRemoveFile = "Xóa";
        dropzone.options.hiddenInputContainer = "#preview-image-dropzone";

        // Handel response from server
        dropzone.on("success", function (file, response) {
            console.log(response);
            if (response.status === "success") {
                const value = response.data.url;
                const input = $("#imageNames");
                if (input.length > 0) {
                    const currentValue = JSON.parse(input.val() || "[]");
                    currentValue.push(value);
                    input.val(JSON.stringify(currentValue));
                } else {
                    $("#preview-image-dropzone").after(`
                        <input type="hidden" name="imageNames" id="imageNames" value='${JSON.stringify([value])}'/>
                    `)
                }
            }
        });


    }


})