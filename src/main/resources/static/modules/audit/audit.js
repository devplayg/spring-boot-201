$(function () {

    /*
     * 1. Define and initialize
     */
    let $table = $("#table-" + ctrl);

    $(".datetime").datetimepicker(defaultDatetimeOption);

    $table.bootstrapTable({
        url: "/" + ctrl,
        method: "get",
        queryParamsType: "", // DO NOT REMOVE. LEAVE BLANK
        pagination: true,
        sidePagination: "server",
        queryParams: function (p) {
            tuneDateAndPaging(filter, p);
            return $.param(filter, true);
        },
        responseHandler: function (res) {
            if (filter.fastPaging) {
                return res;
            }
            return {
                total: res.totalElements,
                rows: res.content
            };
        }
    }).on("column-switch.bs.table", function (e, field, checked) {
        captureTableColumnsState($(this));
    });

    /*
     * 2. Event
     */


    /*
     * 3. Function
     */
});