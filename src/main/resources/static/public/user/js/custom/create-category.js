function convertToSlug(input) {
    return toLowerCaseNonAccentVietnamese(input.replaceAll(" ", "-"));
}

$(document).ready(function () {
    const bindingSlug = function (inputSelector, slugSelector) {
        $(inputSelector).on("keyup", function () {
            $(slugSelector).val(convertToSlug($(this).val()));
        });
    }


    const checkSlug = function ({nameSelector, slugSelector, btnSubmitSelector, url}) {
        let idTimeout = null;
        $(nameSelector).on("keyup", function () {

            if (idTimeout) {
                clearTimeout(idTimeout);
            }


            idTimeout = setTimeout(() => {

                const name = $(nameSelector).val();
                const slug = $(slugSelector).val();

                const disableSubmitButton = function (bool) {
                    $(btnSubmitSelector).attr("disabled", bool);
                }


                $.ajax({
                    url,
                    data: {name, slug},
                    dataType: "json",
                    success: ({data}) => {

                        if (data.length === 0) {
                            $(nameSelector).addClass("is-valid");
                            $(nameSelector).removeClass("is-invalid");
                            $(nameSelector).next().text("");

                            $(slugSelector).addClass("is-valid");
                            $(slugSelector).removeClass("is-invalid");
                            $(slugSelector).next().text("");

                            disableSubmitButton(false)
                        }

                        data.map(item => {
                            if (item.name === name && item.deleted === false) {
                                $(nameSelector).addClass("is-invalid");
                                $(nameSelector).removeClass("is-valid");
                                $(nameSelector).next().text("This name have been exists");
                                disableSubmitButton(true)
                            } else if (item.name === name && item.deleted === true) {
                                $(nameSelector).addClass("is-invalid");
                                $(nameSelector).removeClass("is-valid");
                                $(nameSelector).next().text("This name have been exists but deleted. Please change name or restore it");
                                disableSubmitButton(true)
                            }

                            if (item.id === slug && item.deleted === false) {
                                $(slugSelector).addClass("is-invalid");
                                $(slugSelector).removeClass("is-valid");
                                $(slugSelector).next().text("Slug have been exists");
                                disableSubmitButton(true)
                            } else if (item.id === slug && item.deleted === true) {
                                $(slugSelector).addClass("is-invalid");
                                $(slugSelector).removeClass("is-valid");
                                $(slugSelector).next().text("Slug have been exists but deleted. Please change slug or restore it");
                                disableSubmitButton(true)
                            }
                        })

                    }


                })

            }, 500)


        })
    }

    $("#groupSubmitButton").click(function () {
        $.ajax({
            url: "/api/v1/categories/group/create",
            method: "POST",
            data: {
                name: $("#groupName").val(),
                slug: $("#groupSlug").val()
            },
            dataType: "json",
            success: ({data}) => {
                if (data.status === "success") {
                    notificer.addSuccess(data.message)

                } else if (data.status === "error") {
                    notificer.addError(data.message)
                }
            }
        })
    })

    bindingSlug("#groupName", "#groupSlug");
    checkSlug({
        nameSelector: "#groupName",
        slugSelector: "#groupSlug",
        btnSubmitSelector: "#groupSubmitButton",
        url: "/api/v1/categories/group/get"
    })

    bindingSlug("#categoryName", "#categorySlug");
    checkSlug({
        nameSelector: "#categoryName",
        slugSelector: "#categorySlug",
        btnSubmitSelector: "#categorySubmitButton",
        url: "/api/v1/categories/get"
    })

    bindingSlug("#brandName", "#brandSlug");
    checkSlug({
        nameSelector: "#brandName",
        slugSelector: "#brandSlug",
        btnSubmitSelector: "#brandSubmitButton",
        url: "/api/v1/brands/get"
    })

})