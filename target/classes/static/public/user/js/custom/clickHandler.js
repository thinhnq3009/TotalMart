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
        ...passProps
    }
) {

    const selector = tagSelector ? `${tagSelector}[${attrName}]` : "*" + '[' + attrName + ']';

    const element = $(selector);

    element.click(function () {
        onClick($(this), element);
        const params = getParams($(this));

        let id = $(this).attr(attrName);
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
                error(data, element);
            },
            ...passProps

        });
    });
}