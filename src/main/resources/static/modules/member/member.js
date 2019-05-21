$(function () {

    /*
     * 1. Define
     */
    let $table = $("#table-member");

    // Test
    {
        let $form = $("#form-member-add");
        $("input[name=username]", $form).val("wsan");
        $("input[name=name]", $form).val("WON SEOK AHN");
        $("input[name=email]", $form).val("wsan@unisem.co.kr");
        $("input[name=inputPassword]", $form).val("wsan123!@#");
    }


    /*
     * 1. Initialize
     */
    initialize();


    /*
     * 2. Events
     */
    $("#form-member-add").validate({
        submitHandler: function (form, e) {
            e.preventDefault();

            $.post("/members", $(form).serialize(), function () {
            }).done(function (rs) {
                if (rs.error !== null) {
                    $(form).find(".msg").text(rs.error);
                    $(form).find(".alert").removeClass("hide").addClass("in");
                    return;
                }
                // $("#table-member").bootstrapTable("refresh");
                $(form).find(".modal").modal("hide");
            });
        },
        ignore: 'input[type="hidden"]',
        rules: {
            username: {
                required: true,
                // username: true
            },
            name: {
                minlength: 4,
                maxlength: 32,
            },
            password: {
                required: true,
                minlength: 4,
                maxlength: 16,
                // password: true,
            },
            email: {
                required: true,
                // email: true,
            },
        }
    });

    $("#form-member-update").validate({
        submitHandler: function (form, e) {
            e.preventDefault();

            $.ajax({
                method: "PATCH",
                url: "/members/" + $("input[name=id]", $(form)).val(),
                data: $(form).serialize()
            }).done(function (rs) {
                console.log(rs);
                if (rs.error !== null) {
                    return;
                }
                // $("#modal-member-update").modal("hide");
            }).fail(function (jqXHR, textStatus) {
            });
        },
        ignore: 'input[type="hidden"]',
        rules: {
            username: {
                required: true,
                // username: true
            },
            name: {
                minlength: 4,
                maxlength: 32,
            },
            password: {
                required: true,
                minlength: 4,
                maxlength: 16,
                // password: true,
            },
            email: {
                required: true,
                // email: true,
            },
        }
    });

    $(".modal-form")
        .on("hidden.bs.modal", function () {
            let $form = $(this).closest("form");
            $form.validate().resetForm();
            $form.get(0).reset();
            $(".alert", $form).addClass("hidden");
            $(".alert .message", $form).empty();

            $table.bootstrapTable("refresh");
        });


    /*
     * 3. Functions
     */

    function initialize() {
        window.memberCommandEvents = {
            "click .update": function (e, val, row, idx) {
                showMember(row);
            },
            "click .delete": function (e, val, row, idx) {
                deleteMember(row);
            }
        };
        $table.bootstrapTable();
    }

    function showMember(row) {
        console.log(row);
        $.get("/members/" + row.id, function () {
        }).done(function (rs) {
            if (rs.error !== null) {
                return;
            }

            let $form = $("#form-member-update");
            $("input[name=id]", $form).val(rs.data.id);
            $("input[name=username]", $form).val(rs.data.username);
            $("input[name=email]", $form).val(rs.data.email);
            $("input[name=name]", $form).val(rs.data.name);
            $("select[name=timezone]", $form).val(rs.data.timezone).selectpicker("refresh");
            $("input[name=enabled]", $form).prop("checked", rs.data.enabled);
            $.each(rs.data.roleList, function (i, role) {
                $("#revoke_" + role.role).prop("checked", true);
            });
            $("#modal-member-update").modal("show");
        });

    }

    function deleteMember(row) {
        console.log(row);
    }


    // ------------------------------------------------------------------------------


    //
    // function doSomething(param) {
    //     $.ajax({
    //         method: "GET",
    //         url: "",
    //         data: $form.serialize()
    //     }).done(function (data) {
    //
    //     }).fail(function (jqXHR, textStatus) {
    //
    //     }).always(function () {
    //
    //     });
    // }


});