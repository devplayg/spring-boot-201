$(function () {
    /*
     * 1. Definition
     */
    let $table = $("#table-" + ctrl);


    /*
     * 2. Initialize
     */
    $(".datetime").datetimepicker(defaultDatetimeOption);
    $table.bootstrapTable({
        url: "/" + ctrl,
        method: "get",
        sidePagination: "server",
        queryParams: function(p) {
            refineJavaDateWithPaging(filter, p);
            return $.param(filter, true);
        },
        responseHandler: function(res) {
            return {
                total: res.totalElements,
                rows: res.content
            };
        }
    });


    /*
     * 3. Event
     */


    /*
     * 4. Function
     */


    // Test code
    // {
        // let $form = $("#form-member-insert");
        // $("input[name=username]", $form).val("msgxxx");
        // $("input[name=name]", $form).val("WON SEOK AHN");
        // $("input[name=email]", $form).val("wsan@korea.com");
        // $("input[name=inputPassword]", $form).val("wsan123!@#");
    // }
});