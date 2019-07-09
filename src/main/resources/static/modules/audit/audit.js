$(function () {
    /*
     * 1. Define & initialize
     */
    let $table = $("#table-audit");


    /*
     * 2. Classes
     */
    $(".datetime").datetimepicker(defaultDatetimeOption);


    /*
     * 3. Main
     */
    $table.bootstrapTable({
        url: "/audit",
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


    // Test code
    {
        // let $form = $("#form-member-insert");
        // $("input[name=username]", $form).val("msgxxx");
        // $("input[name=name]", $form).val("WON SEOK AHN");
        // $("input[name=email]", $form).val("wsan@unisem.co.kr");
        // $("input[name=inputPassword]", $form).val("wsan123!@#");
    }
});