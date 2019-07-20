$(function () {

    /*
     * 1. Class
     */

    class Member {

        create(form) {
            $.ajax({
                method: "POST",
                url: "/members",
                data: $(form).serialize()
            }).done(function (data) {
                $(form).find(".modal").modal("hide");
            }).fail(function (jqXHR, textStatus, errorThrown) {
                $(form).find(".msg").text(jqXHR.responseText);
                $(form).find(".alert").removeClass("hide").addClass("in");
            });
        }

        show(row) {
            $.ajax({
                url: "/members/" + row.id,
            }).done(function (data) {
                member.id = data.id;
                $("input[name=username]", $updateForm).val(data.username);
                $("input[name=email]", $updateForm).val(data.email);
                $("input[name=name]", $updateForm).val(data.name);
                $("select[name=timezone]", $updateForm).val(data.timezone).selectpicker("refresh");
                $("input[name=enabled]", $updateForm).prop("checked", data.enabled);
                $.each(data.roleList, function (i, role) {
                    $("#revoke_" + role).prop("checked", true);
                });

                // 접속 허용 IP
                let accessibleIpListText = data.accessibleIpList.map(function (r, i) {
                    if (r.ipCidr.endsWith("/32")) {
                        return r.ipCidr.substr(0, r.ipCidr.indexOf("/"));
                    }
                    console.log(r.ipCidr);
                    return r.ipCidr;
                }).join("\n");
                $("textarea[name=accessibleIpListText]", $updateForm).val(accessibleIpListText);

                $updateForm.find(".modal").modal("show");
            }).fail(function (jqXHR, textStatus, errorThrown) {
                Swal.fire(jqXHR.message, row.username, 'warning');
            });
        }

        update(form) {
            $.ajax({
                method: "PATCH",
                url: "/members/" + member.id,
                data: $(form).serialize()
            }).done(function (data) {
                console.log(data);
                $(form).find(".modal").modal("hide");
            }).fail(function (jqXHR, textStatus) {
                Swal.fire(jqXHR.message, username, 'warning');
            });
        }

        delete(row) {
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
                    }).done(function (data) {
                        console.log(data);
                        $table.bootstrapTable("refresh");
                    }).fail(function (jqXHR, textStatus) {
                        Swal.fire('failed to delete', row.username, 'warning');
                    });
                }
            })
        }
    }


    /*
     * 2. Define
     */
    let $table = $("#table-member"),
        $createForm = $("#form-member-create"),
        $updateForm = $("#form-member-update"),
        member = new Member();


    /*
     * 3. Event
     */
    window.memberCommandEvents = {
        "click .update": function (e, val, row, idx) {
            member.show(row);
        },
        "click .delete": function (e, val, row, idx) {
            member.delete(row);
        }
    };

    $(".modal-form")
        .on("hidden.bs.modal", function () {
            let $form = $(this).closest("form");
            // $form.validate().resetForm();
            // $form.get(0).reset();
            $(".alert", $form).addClass("hide").removeClass("in");
            $(".alert .message", $form).empty();

            $table.bootstrapTable("refresh");
        });

    $createForm.validate({
        submitHandler: function (form, e) {
            e.preventDefault();
            member.create(form);
        },
        ignore: 'input[type="hidden"]',
        errorClass: "help-block",
        rules: {
            username: {
                required: true,
                minlength: 3,
                maxlength: 16,
                username: true
            },
            name: {
                minlength: 4,
                maxlength: 32,
            },
            password: {
                required: true,
                minlength: 9,
                maxlength: 16,
            },
            email: {
                required: true,
                email: true,
            },
            accessibleIpListText: {
                required: true,
            },
        },
        highlight: function (element) {
            $(element).closest(".form-group").addClass("has-error");
        },
        unhighlight: function (element) {
            $(element).closest(".form-group").removeClass("has-error");
        },
    });

    $updateForm.validate({
        submitHandler: function (form, e) {
            e.preventDefault();
            new Member().update(form);

        },
        ignore: 'input[type="hidden"]',
        rules: {
            name: {
                minlength: 4,
                maxlength: 32,
            },
            email: {
                required: true,
                email: true,
            },
            accessibleIpListText: {
                required: true,
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
        $("input[name=username]", $form).val("won1");
        $("input[name=name]", $form).val("DEV Play G");
        $("input[name=email]", $form).val("devplayg@korea.com");
        $("input[name=inputPassword]", $form).val("dev123!@#");
    }
});