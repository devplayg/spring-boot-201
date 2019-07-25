$(function () {

    /*
     * 1. Define and initialize
     */
    let $table = $("#table-" + ctrl);

    $(".datetime").datetimepicker(defaultDatetimeOption);

    $table.bootstrapTable({
        url: "/" + ctrl,
        queryParamsType: "", // DO NOT REMOVE. LEAVE BLANK
        pagination: true,
        sidePagination: "server",
        queryParams: function (params) {
            let _filter = tuneDateAndPaging(filter, params);
            return $.param(_filter, true);
        },
        responseHandler: function (res) {
            console.log(res);
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


    // $('table').bootstrapTable({locale:'en-US'});
    // $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['en-US']);
});