const  renderMoney = () => {
    $("*[data-money]").each(function () {
        const money = $(this).attr("data-money");
        $(this).text(moneyFormat(Number(money)));
    })
}

$(document).ready(function () {
   renderMoney();
})