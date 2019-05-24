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


    // class Member {
    //     constructor (data) {
    //         this.data = data || null;
    //     }
    //
    //     show() {
    //         $.get("/members/" + this.data.id, function () {
    //         }).done(function (rs) {
    //             if (rs.error !== null) {
    //                 return;
    //             }
    //             $("input[name=id]", $updateForm).val(rs.data.id);
    //             $("input[name=username]", $updateForm).val(rs.data.username);
    //             $("input[name=email]", $updateForm).val(rs.data.email);
    //             $("input[name=name]", $updateForm).val(rs.data.name);
    //             $("select[name=timezone]", $updateForm).val(rs.data.timezone).selectpicker("refresh");
    //             $("input[name=enabled]", $updateForm).prop("checked", rs.data.enabled);
    //             $.each(rs.data.roleList, function (i, role) {
    //                 $("#revoke_" + role.role).prop("checked", true);
    //             });
    //
    //             $updateForm.find(".modal").modal("show");
    //         });
    //     }
    //
    //     delete() {
    //         let id = this.data.id;
    //
    //         Swal.fire({
    //             title: 'Are you sure?',
    //             text: "You won't be able to revert this!",
    //             type: 'warning',
    //             showCancelButton: true,
    //             confirmButtonColor: '#3085d6',
    //             cancelButtonColor: '#d33',
    //             confirmButtonText: 'Yes, delete it!'
    //         }).then((result) => {
    //             if (result.value) {
    //                 $.ajax({
    //                     method: "DELETE",
    //                     url: "/members/" + id,
    //                 }).done(function (rs) {
    //                     if (rs.error !== null) {
    //                         return;
    //                     }
    //                     $table.bootstrapTable("refresh");
    //                 }).fail(function (jqXHR, textStatus) {
    //                 });
    //             }
    //         })
    //     }
    // }


    /*
     * 3. Events
     */
    window.memberCommandEvents = {
        "click .update": function (e, val, row, idx) {
            new Member(row).show();
        },
        "click .delete": function (e, val, row, idx) {
            new Member(row).delete();
        }
    };

    $(".modal-form")
        .on("hidden.bs.modal", function () {
            let $form = $(this).closest("form");
            $form.validate().resetForm();
            $form.get(0).reset();
            $(".alert", $form).addClass("hide").removeClass("in");
            $(".alert .message", $form).empty();

            $table.bootstrapTable("refresh");
        });

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