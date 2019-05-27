$(function () {

    /*
     * 1. Define & initialize
     */
    let $table = $("#table-audit");


    /*
     * 2. Classes
     */
    $(".datetime").datetimepicker({
        format: "yyyy-mm-dd hh:ii",
        pickerPosition: "bottom-left",
        todayHighlight: 1,
        minView: 2,
        maxView: 4,
        autoclose: true
    });


    /*
     * 3. Events
     */
    // $(".modal-form")
    //     .on("hidden.bs.modal", function () {
    //         let $form = $(this).closest("form");
    //         $form.validate().resetForm();
    //         $form.get(0).reset();
    //         $(".alert", $form).addClass("hide").removeClass("in");
    //         $(".alert .message", $form).empty();
    //
    //         $table.bootstrapTable("refresh");
    //     });

    /*
     * 4. Main
     */
    $table.bootstrapTable();


    // Test code
    {
        // let $form = $("#form-member-insert");
        // $("input[name=username]", $form).val("msgxxx");
        // $("input[name=name]", $form).val("WON SEOK AHN");
        // $("input[name=email]", $form).val("wsan@unisem.co.kr");
        // $("input[name=inputPassword]", $form).val("wsan123!@#");
    }
});