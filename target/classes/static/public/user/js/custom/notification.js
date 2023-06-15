class NotificationManager {
    constructor() {
        this.initContainer();
        this.notifications = [];
    }

    addSuccess(message, duration) {
        this.addNotification("primary", message, duration);
    }

    addError(message, duration) {
        this.addNotification("danger", message, duration);
    }

    addWarning(message, duration) {
        this.addNotification("warning", message, duration);
    }

    addNotification(type, message, duration) {

        if (!duration) {
            duration = 3000;
        }

        type = type.toLowerCase() === "error" ? "danger" : type.toLowerCase();

        const getIcon = (type) => {
            // ti Icon
            switch (type) {
                case "info":
                case "primary":
                    return "bi bi-info-circle-fill";
                case "danger":
                    return "bi bi-exclamation-triangle-fill";
                case "warning":
                    return "bi bi-exclamation-circle-fill";
                case "success":
                    return "bi bi-check-circle-fill";
                case "secondary":
                    return "bi bi-info-circle-fill";
                default:
                    throw new Error("Invalid notification type");
            }
        }

        const notification = $("<div/>", {
            class: `alert alert-${type} bg-${type} alert-dismissible text-white rounded-4
                            border-0 bounceInRight show d-flex align-items-center`,
            role: "alert",
            id: `notification-${this.notifications.length}`,
            text: message,
        });

        const icon = $("<i/>", {class: getIcon(type) + " fs-6 me-3"});
        notification.prepend(icon);


        const closeButton = $("<button/>", {
            type: "button", class: "btn-close btn-close-white", "data-bs-dismiss": "alert", "aria-label": "Close",
        });


        notification.append(closeButton);

        $("#notification-container").append(notification);

        const id = this.notifications.push(notification) - 1;


        setTimeout(() => {
            this.removeNotification(id);
        }, duration);
    }


    removeNotification(id) {
        this.notifications[id].removeClass("bounceInRight");
        this.notifications[id].addClass("backOutRight");
        setTimeout(() => {
            this.notifications[id].remove();
            this.notifications[id] = null;
        }, 1000);

    }

    initContainer() {
        // kiểm tra xem phần tử đã tồn tại hay chưa
        if ($("#notification-container").length === 0) {
            const notificationContainer = $("<div/>", {
                id: "notification-container", css: {
                    position: "fixed",
                    bottom: "1rem",
                    right: "1rem",
                    minWidth: "300px",
                    maxWidth: "500px",
                    zIndex: "9999",
                },
            });

            $("body").append(notificationContainer);
        }
    }

}

notificer = new NotificationManager();

$(document).ready(function () {
    setTimeout(function () {
            const notifierString = $("#notifier-json-wrapper").html();
            if (notifierString) {
                const notifier = JSON.parse(notifierString);
                notifier.forEach((notification) => {
                    notificer.addNotification(notification.type, notification.message, 3000);
                });
            }
        }, 500
    )
})

console.log('%cĐã khởi tạo thành công NotificationManager',
    'color: #539165; font-size: 18px; font-weight: bold;');

