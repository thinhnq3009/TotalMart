$(document).ready(function () {

    let canCheckout = [];
    const debouncing = 500;
    const bouncer = {};
    let voucher = null;
    let totalBill
    function updateCartDisplay() {
        totalBill = 0;
        $(".cart-item").each(function () {
            const input = $(this).find(".quantity-field");
            const quantity = input.val();
            const id = input.attr("data-cart-id");
            const price = $(this).find("*[data-price]").attr("data-price");
            const total = (quantity * price);
            const checkBox = $(this).find("input[type='checkbox']").prop("checked");
            totalBill += checkBox ? total : 0;
            checkBox && !canCheckout.includes(id) && canCheckout.push(id);
            !checkBox && canCheckout.includes(id) && canCheckout.splice(canCheckout.indexOf(id), 1);
            $(this).find(".cart-total-price").text(moneyFormat(total));
        })


        let discount = getDiscount(totalBill);

        console.log(discount)

        $("#total").text(moneyFormat(totalBill));
        $("#discount-value").text(moneyFormat(discount));

        const totalBillAfterDiscount = totalBill - discount;
        $("#total-bill").text(moneyFormat(totalBillAfterDiscount < 0 ? 0 : totalBillAfterDiscount));
    }

    function getDiscount(totalBill) {
        let result = 0;
        if (voucher) {
            if (voucher.type === "PERCENTAGE") {
                result =  totalBill * (voucher.discount / 100);
            } else if (voucher.type === "DIRECT") {
                result = voucher.discount;
            }
        } else {
            return 0;
        }

        return result > voucher.maxDiscount ? voucher.maxDiscount : result;
    }

    function updateQuantity(cartId, quantity) {


        if (bouncer[cartId]) {
            clearTimeout(bouncer[cartId]);
        }
        bouncer[cartId] = setTimeout(function () {
            $.ajax({
                url: "/api/v1/carts/update",
                method: "PUT",
                data: {
                    cartId: cartId,
                    quantity: quantity
                },
                success: function (response) {
                    if (response.status === "success") {
                        console.log(response)
                    } else {
                        notificer.addError(response.message);
                    }
                },
                error: function (response) {
                    notificer.addError(response.message);
                }
            })
        }, debouncing);
    }

    function checkRangeQuantity(newValue, input, cartId) {
        const max = parseInt(input.attr("max"));
        const min = parseInt(input.attr("min"));
        if (newValue <= max && newValue >= min) {
            input.val(newValue);
            // input.trigger("change");
        } else if (newValue > max) {
            input.val(max);
            notificer.addError("Số lượng sản phẩm tối đa là " + max);
        } else if (newValue < min) {
            input.val(min);
            notificer.addError("Số lượng sản phẩm không th nhỏ hơn 0");
        } else {
            input.val(1);
            notificer.addError("Số lượng sản phẩm không hợp lệ");
        }
        updateQuantity(cartId, input.val());
        updateCartDisplay();
    }


    $(".quantity-field").on("change", function () {
        const cartId = $(this).attr("data-cart-id");
        const value = parseInt($(this).val());
        checkRangeQuantity(value, $(this), cartId);
        updateCartDisplay();
    })

    $(".cart-item input[type='checkbox']").on("change", function () {
        updateCartDisplay();

    })

    $("#select-all").change(function () {
        const isSelectAll = $(this).prop("checked");
        $(".cart-item input[type='checkbox']").prop("checked", isSelectAll);
        updateCartDisplay();
    })

    $("#check-out").click(function () {
        if (canCheckout.length > 0) {
            const form = $("#check-out-form");
            canCheckout.forEach(function (cartId) {
                // Create a hidden input using jQuery syntax
                const input = $("<input>")
                    .attr("type", "hidden")
                    .attr("name", "i")
                    .val(cartId);
                // Append the input to the form
                form.append(input);
            });

            if (voucher) {
                const input = $("<input>")
                    .attr("type", "hidden")
                    .attr("name", "c")
                    .val(voucher.code);
                form.append(input);

                const discount = $("<input>")
                    .attr("type", "hidden")
                    .attr("name", "d")
                    .val(parseInt(getDiscount(totalBill)));
                form.append(discount);
            }

        } else {
            notificer.addError("Vui lòng chọn sản phẩm để thanh toán")
        }
    })

    $(".input-spinner").find("input[type='button']").on("click", function () {
        const input = $(this).parent().find("input[type='number']");
        const cartId = input.attr("data-cart-id");
        const value = parseInt(input.val());
        const step = parseInt(input.attr("step"));
        const newValue = $(this).hasClass("plus") ? value + step : value - step;
        checkRangeQuantity(newValue, input, cartId);
    })

    $("#discount-code").on("input", function () {

        const value = $(this).val();
        if (value.length > 0) {
            $.ajax({
                url: "/api/v1/voucher/find",
                method: "GET",
                data: {
                    code: value
                },
                success: function (response) {
                    if (response.data) {
                        voucher = response.data;
                        updateCartDisplay();
                    } else {
                        voucher = null;
                    }
                },
                error: function (response) {
                    notificer.addError(response.message);
                }
            })
        }

    })

})