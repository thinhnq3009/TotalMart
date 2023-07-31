$(document).ready(function () {
    $.ajax({
        url: GHN_URL + "/master-data/province",
        method: "GET",
        dataType: "json",
        headers: {
            token: GHN_TOKEN
        },
        success: function ({data}) {
            data = [
                {NameExtension: ["Chọn tỉnh"], ProvinceID: undefined},
                ...data
            ]


            const addressBox = $('#province').empty();
            if (data) {
                data.map((item, index) => {
                    const selectItem = $('<option>').text(item.NameExtension[0])
                        .val(item.ProvinceID);
                    addressBox.append(selectItem);
                })
            }

            addressBox.change(getDistrict);
        }
    })

    function getDistrict() {
        const provinceCode = $("#province").val();
        $.ajax({
            url: GHN_URL + "/master-data/district",
            method: "GET",
            dataType: "json",
            headers: {
                token: GHN_TOKEN
            },
            data: {
                province_id: provinceCode
            },
            success: function ({data}) {
                // Sort data by name extension a to z
                data = [
                    {NameExtension: ["Chọn quận/huyện"], DistrictID: undefined},
                    ...data
                ]
                const addressBox = $('#district').empty();
                if (data) {
                    data.map((item) => {
                        const selectItem = $('<option>').text(item.NameExtension[0])
                            .val(item.DistrictID);
                        addressBox.append(selectItem);
                    })
                }

                addressBox.change(getWard);
            }
        })
    }

    function getWard() {
        const districtCode = $("#district").val();
        $.ajax({
            url: GHN_URL + "/master-data/ward",
            method: "GET",
            dataType: "json",
            headers: {
                token: GHN_TOKEN
            },
            data: {
                district_id: districtCode
            },
            success: function ({data}) {
                data = [
                    {NameExtension: ["Chọn phường/xã"], WardCode: undefined},
                    ...data
                ]
                const addressBox = $('#ward').empty();
                if (data) {
                    data.map((item, index) => {
                        const selectItem = $('<option>').text(item.NameExtension[0])
                            .val(item.WardCode);
                        addressBox.append(selectItem);
                    })
                }
            }
        })
    }

    $("#saveAddress").click(function () {
        const data = {}
        $("#addressForm *[name]").each(function () {
            const name = $(this).attr("name");
            const value = $(this).val();
            if (value === "") {
                notificer.addError("Vui lòng nhập đầy đủ thông tin");
                return;
            }

            if ($(this).prop("tagName") === "SELECT") {
                console.log("Thinh")
                data[name + "Name"] = $(this).find("option:selected").text();
                data[name + "Id"] = value;
            } else {
                data[name] = value;
            }
        })
        console.log(data)

        $.ajax({
            url: "/api/v1/address/new",
            method: "POST",
            dataType: "json",
            data: {...data},
            success: function ({message, status}) {
                if (status === "success") {
                    notificer.addSuccess("Thêm địa chỉ thành công");
                    setTimeout(function () {
                        location.reload();
                    }, 1000)
                } else {
                    notificer.addError(message);
                }
            },
        })
    })

    // Xoá địa chỉ
    $.clickHandler({
        tagSelector: 'button',
        attrName: 'data-address-id',
        method: 'DELETE',
        url: '/api/v1/address/delete',
        success: function ({message, status}) {
            if (status === "success") {
                notificer.addSuccess("Xóa địa chỉ thành công");
                setTimeout(function () {
                    location.reload();
                }, 1000)
            } else {
                notificer.addError(message);
            }
        }
    })

    // Chọn địa chỉ
    $.clickHandler({
        tagSelector: 'div',
        attrName: 'data-address-id',
        method: 'GET',
        url: '/api/v1/shipping-fee',
        paramName: 'addressId',
        paramPush: {
            cartIds: $("li[data-cart-id]").map(function () {
                return $(this).attr("data-cart-id");
            }).get()
        },
        dataType: 'json',
        onClick: function (element, elementList) {
            element.addClass("shadow border-primary border-1");
            elementList.not(element).removeClass("shadow border-primary border-1");
        },
        success: function ({code, data}) {
            if (code === 200) {
                $("#shipping-fee").attr("data-money", data.total);

                const itemSubtotal = $("#itemSubtotal").attr("data-money");
                const discount = $("#discount").attr("data-money");

                const total = Number(itemSubtotal) - Number(discount) + Number(data.total);

                $("#subtotal").attr("data-money", total);
                renderMoney();
            }
        }
    })

    // Chọn phương thước thanh toán
    $.clickHandler({
        tagSelector: 'div',
        attrName: 'data-payment-method-name',
        onClick: function (element, elementList) {
            element.addClass("shadow border-primary border-1");
            elementList.not(element).removeClass("shadow border-primary border-1");
        },
        sendRequest: false

    })


    // Checkout Button
    $("#completeOrder").click(function () {
        const addressId = $("div[data-address-id].border-primary").attr("data-address-id");
        const shippingFee = $("#shipping-fee").attr("data-money");
        const cartIds = $("li[data-cart-id]").map(function () {
            return $(this).attr("data-cart-id");
        }).get();
        const voucherCode = $("#voucherCode").val();
        const paymentMethodName = $("div[data-payment-method-name].border-primary").attr("data-payment-method-name");
        const note = $("#note").val();

        if (addressId === undefined) {
            notificer.addError("Vui lòng chọn địa chỉ giao hàng");
            return;
        }

        const data = {
            addressId,
            shippingFee,
            cartIds,
            voucherCode,
            paymentMethodName,
            note
        }

        $.ajax({
            url: "/api/v1/order/checkout",
            method: "POST",
            dataType: "json",
            data: {...data},
            success: function ({message, status, data}) {
                if (status === "success") {
                    notificer.addSuccess("Đặt hàng thành công");
                    setTimeout(function () {
                        if (data.linkToPay && !data.paid) {
                            location.href = data.linkToPay;
                        } else {
                            location.href = `/order/${data.id}`;
                        }

                    }, 1000)
                } else {
                    notificer.addError(message);
                }
            }
        })

    })
})