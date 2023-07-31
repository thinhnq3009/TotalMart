const getChart = ({cost, revenue, profit,shippingFee}) => {
    return {
        series: [
            {
                name: "Revenue",
                data: revenue
            },
            {
                name: "Cost",
                data: cost
            },
            {
                name: "Profit",
                data: profit
            }

        ],
        labels: ["Jan", "Feb", "March", "April", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
        chart: {
            height: 350,
            type: "area",
            toolbar: {
                show: false
            }
        },
        dataLabels: {
            enabled: false
        },
        markers: {
            size: 5,
            hover: {
                size: 6,
                sizeOffset: 3
            }
        },
        colors: [ "#F266AB",  "#EEBB4D", "#0aad0a",],
        stroke: {
            curve: "smooth",
            width: 2
        },
        grid: {
            borderColor: window.theme.gray300
        },
        xaxis: {
            labels: {
                show: true,
                align: "right",
                minWidth: 0,
                maxWidth: 160,
                style: {
                    fontSize: "12px",
                    fontWeight: 400,
                    colors: [window.theme.gray600],
                    fontFamily: '"Inter", "sans-serif"'
                }
            },
            axisBorder: {
                show: true,
                color: window.theme.gray300,
                height: 1,
                width: "100%",
                offsetX: 0,
                offsetY: 0
            },
            axisTicks: {
                show: true,
                borderType: "solid",
                color: window.theme.gray300,
                height: 6,
                offsetX: 0,
                offsetY: 0
            }
        },
        legend: {
            position: "top",
            fontWeight: 600,
            color: window.theme.gray600,
            markers: {
                width: 8,
                height: 8,
                strokeWidth: 0,
                strokeColor: "#fff",
                fillColors: undefined,
                radius: 12,
                customHTML: undefined,
                onClick: undefined,
                offsetX: 0,
                offsetY: 0
            },
            labels: {
                colors: window.theme.gray600,
                useSeriesColors: false
            }
        },
        yaxis: {
            labels: {
                formatter: function (value) {
                    return (value / 1000000).toFixed(2) + "M";
                },
                show: true,
                align: "right",
                minWidth: 0,
                maxWidth: 160,
                style: {
                    fontSize: "12px",
                    fontWeight: 400,
                    colors: window.theme.gray600,
                    fontFamily: '"Inter", "sans-serif"'
                }
            }
        }
    };
};

$(document).ready(function () {

    const showChart = (year) => {
        $.ajax({
            url: "/api/v1/dashboard/chart-data",
            type: "GET",
            data: {
                year
            },
            success: function ({data}) {
                const container = document.querySelector("#revenueChart");
                container.innerHTML = "";
                new ApexCharts(container, getChart(data)).render();
            }
        })
    }

    showChart(-1);

    $("#year").change(function () {
        showChart($(this).val());
    })


});
