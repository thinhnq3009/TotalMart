// import {Quill} from "../../libs/quill/dist/quill.min";

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
            url: `/api/v1/categories/group/get/${categoryId}`
        })
    })

    // Handel upload image with DropzoneJS
    // Huỷ bỏ dropzone mặc định
    const dropzone = Dropzone.forElement("#preview-image-dropzone");
    if (dropzone) {

        dropzone.options.maxFiles = 10;
        dropzone.options.maxFilesize = 10;
        dropzone.options.acceptedFiles = ".jpeg,.jpg,.png,.gif";
        dropzone.options.addRemoveLinks = true;
        dropzone.options.dictRemoveFile = "Xóa";
        dropzone.options.hiddenInputContainer = "#preview-image-dropzone";

        // Handel response from server
        dropzone.on("success", function (file, response) {
            console.log(response);
            if (response.status === "success") {
                const value = response.data.url;
                file.url = value;

                notificer.addSuccess("Upload image success");

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

        // Handel remove image
        dropzone.on("removedfile", function (file) {
            const input = $("#imageNames");
            if (input.length > 0) {
                const currentValue = JSON.parse(input.val() || "[]");
                console.log(currentValue.filter(value => value !== file.url));
                input.val(JSON.stringify(currentValue.filter(value => value !== file.url)));
            }
    
            $.ajax({
                url: '/api/v1/images/delete',
                method: 'DELETE',
                data: {
                    url: file.url
                },
                success: function ({data}) {
                    notificer.addSuccess("Delete image success");
                }
            })




        });


        $.ajax({
            url: '/api/v1/images/get',
            method: 'GET',
            data: {
                productId: $("input[name=id]").val()
            },
            dataType: 'json',
            success: function (response) {
                // Hiển thị các ảnh đã tải lên trong Dropzone
                if (!response.data) return;

                response.data.forEach(function (image, index) {
                    // Tạo một mock file object để hiển thị ảnh
                    const mockFile = {name: `Image ${index}`, size: 12345, url: image.url};

                    // Thêm ảnh vào Dropzone
                    dropzone.emit("addedfile", mockFile);
                    dropzone.emit("thumbnail", mockFile, image.url);
                    dropzone.emit("complete", mockFile);
                    dropzone.files.push(mockFile);

                });
            },
            error: function (xhr, status, error) {
                console.error(error);
            }
        });

    }

    $("#formProduct").on("submit", function (e) {
        const des = $("#editor > .ql-editor").html();
        $("input[name=description]").val(des);
        e.submit();

    })


})