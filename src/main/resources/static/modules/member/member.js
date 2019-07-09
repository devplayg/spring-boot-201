$(function () {

    /*
     * 1. Define & initialize
     */
    let $table = $("#table-member"),
        $createForm = $("#form-member-create"),
        $updateForm = $("#form-member-update"),
        selectedMemberID = null;

    /*
     * 2. Classes
     */
    class Member {
        constructor (data) {
            this.data = data || null;
        }

        show() {
            $.get("/members/" + this.data.id, function () {
            }).done(function (rs) {
                if (rs.error !== null) {
                    return;
                }
                selectedMemberID = rs.data.id;
                $("input[name=username]", $updateForm). val(rs.data.username);
                $("input[name=email]", $updateForm).val(rs.data.email);
                $("input[name=name]", $updateForm).val(rs.data.name);
                $("select[name=timezone]", $updateForm).val(rs.data.timezone).selectpicker("refresh");
                $("input[name=enabled]", $updateForm).prop("checked", rs.data.enabled);
                $.each(rs.data.roleList, function (i, role) {
                    $("#revoke_" + role).prop("checked", true);
                });

                $updateForm.find(".modal").modal("show");
            });
        }

        delete() {
            let id = this.data.id;

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
                        url: "/members/" + id,
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
    }


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

    $createForm.validate({
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
                url: "/members/" + selectedMemberID,
                data: $(form).serialize()
            }).done(function (rs) {
                if (rs.error !== null) {
                    console.log(rs);
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
     * 4. Main
     */
    $table.bootstrapTable();


    // Test code
    {
        let $form = $("#form-member-create");
        $("input[name=username]", $form).val("msgxxx");
        $("input[name=name]", $form).val("WON SEOK AHN");
        $("input[name=email]", $form).val("wsan@korean.com");
        $("input[name=inputPassword]", $form).val("wsan123!@#");
    }
});