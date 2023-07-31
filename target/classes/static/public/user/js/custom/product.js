$(document).ready(function () {
    $("#btnSearch").click(function () {
        const value = $("#key").val();
        if (!value)  return;
        location.href = location.protocol + "?key=" + value;
    });

})