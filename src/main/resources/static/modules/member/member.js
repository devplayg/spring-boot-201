$(function () {

    /*
     * 1. Define & initialize
     */
    let $table = $("#table-member"),
        $insertForm = $("#form-member-insert"),
        $updateForm = $("#form-member-update");


    /*
     * 2. Events
     */

    window.memberCommandEvents = {
        "click .update": function (e, val, row, idx) {
            showMember(row);
        },
        "click .delete": function (e, val, row, idx) {
            deleteMember(row);
        }
    };

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
     * 3. Validate forms
     */
    $insertForm.validate({
        submitHandler: function (form, e) {
            e.preventDefault();

            $.post("/members", $(form).serialize(), function () {
            }).done(function (rs) {
                if (rs.error !== null) {
                    $(form).find(".msg").text(rs.error);
                    $(form).find(".alert").removeClass("hide").addClass("in");
                    return;
                }
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

    $updateForm.validate({
        submitHandler: function (form, e) {
            e.preventDefault();

            $.ajax({
                method: "PATCH",
                url: "/members/" + $("input[name=id]", $(form)).val(),
                data: $(form).serialize()
            }).done(function (rs) {
                if (rs.error !== null) {
                    return;
                }
                $(form).find(".modal").modal("hide");
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


    /*
     * 4. Functions
     */
    function showMember(row) {
        $.get("/members/" + row.id, function () {
        }).done(function (rs) {
            if (rs.error !== null) {
                return;
            }
            $("input[name=id]", $updateForm).val(rs.data.id);
            $("input[name=username]", $updateForm).val(rs.data.username);
            $("input[name=email]", $updateForm).val(rs.data.email);
            $("input[name=name]", $updateForm).val(rs.data.name);
            $("select[name=timezone]", $updateForm).val(rs.data.timezone).selectpicker("refresh");
            $("input[name=enabled]", $updateForm).prop("checked", rs.data.enabled);
            $.each(rs.data.roleList, function (i, role) {
                $("#revoke_" + role.role).prop("checked", true);
            });

            $updateForm.find(".modal").modal("show");
        });

    }

    function deleteMember(row) {
        Swal.fire({
            title: 'Are you sure?',
            text: "You won't be able to revert this!",
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then((result) => {
            if (result.value) {
                $.ajax({
                    method: "DELETE",
                    url: "/members/" + row.id,
                }).done(function (rs) {
                    if (rs.error !== null) {
                        return;
                    }
                    $table.bootstrapTable("refresh");
                }).fail(function (jqXHR, textStatus) {
                });
            }
        })
    }

    // Test code
    {
        // let $form = $("#form-member-insert");
        // $("input[name=username]", $form).val("wsan");
        // $("input[name=name]", $form).val("WON SEOK AHN");
        // $("input[name=email]", $form).val("wsan@unisem.co.kr");
        // $("input[name=inputPassword]", $form).val("wsan123!@#");
    }

    $table.bootstrapTable();
});