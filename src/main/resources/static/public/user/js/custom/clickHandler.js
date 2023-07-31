$.clickHandler = function (
    {
        attrName,
        tagSelector,
        method = 'GET',
        url,
        paramName = "id",
        paramPush = {},
        success = () => {
        },
        onClick = () => {
        },
        error = () => {
        },
        getParams = () => {
        },
        sendRequest = true,
        ...passProps
    }
) {

    const selector = tagSelector ? `${tagSelector}[${attrName}]` : "*" + '[' + attrName + ']';

    const element = $(selector);

    element.click(function () {
        onClick($(this), element);
        const params = getParams($(this));

        let id = $(this).attr(attrName);

        if (sendRequest === false) return;

        $.ajax({
            url: url,
            type: method,
            data: {
                [paramName]: id,
                ...paramPush,
                ...params
            },
            success: function (data) {
                success(data, element);
            },
            error: function (data) {
                error(data.responseJSON, element);
            },
            ...passProps

        });
    });
}