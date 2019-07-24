$(function () {

    /*
     * 1. Class
     */

    class Member {

        create(form) {
            console.log(form);
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
            member.id = row.id;

            $.ajax({
                url: "/members/" + member.id,
            }).done(function (data) {
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
                Swal.fire(jqXHR.responseJSON.message, row.username, "warning");
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
                console.log(jqXHR);
                Swal.fire("Error", jqXHR.responseJSON.message, "error");
            });
        }

        updatePassword(form) {
            $.ajax({
                method: "PATCH",
                url: "/members/" + member.id + "/password",
                data: $(form).serialize()
            }).done(function (data) {
                console.log(data);
                $(form).find(".modal").modal("hide");
            }).fail(function (jqXHR, textStatus) {
                console.log(jqXHR);
                Swal.fire("Error", jqXHR.responseJSON.message, "error");
            });
        }

        delete(row) {
            Swal.fire({
                title: "Are you sure?",
                text: "You won't be able to revert this!",
                type: "warning",
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
                        Swal.fire('failed to delete', row.username, "warning");
                    });
                }
            })
        }

        showPasswordChangeForm(row) {
            member.id = row.id;

            $("#modal-" + ctrl + "-password").modal("show");
        }
    }


    /*
     * 2. Define and initialize
     */
    let $table = $("#table-" + ctrl),
        $createForm = $("#form-" + ctrl + "-create"),
        $updateForm = $("#form-" + ctrl + "-update"),
        $passwordForm = $("#form-" + ctrl + "-password"),
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
        },
        "click .password": function (e, val, row, idx) {
            member.showPasswordChangeForm(row);
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
        })
        .on("shown.bs.modal", function () {
            let $form = $(this).closest("form");
            $form.find("input:not(readonly)[type=text],textarea").filter(":visible:first").focus().select();
        });

    $createForm.validate({
        submitHandler: function (form, e) {
            e.preventDefault();
            console.log(333);
            console.log(member);
            member.create(form);
        },
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

    });

    $updateForm.validate({
        submitHandler: function (form, e) {
            e.preventDefault();
            member.update(form);
        },
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

    $passwordForm.validate({
        submitHandler: function (form, e) {
            e.preventDefault();
            console.log(444);
            member.updatePassword(form);
        },
        rules: {
            password: {
                required: true,
                password: true
            },
            passwordConfirm: {
                equalTo: "#" + $passwordForm.attr("id") + " input[name=password]"
            },
        }
    });


    /*
     * 4. Main
     */
    $table.bootstrapTable();
    console.log();

    // Test code
    {
        let $form = $("#form-member-create");
        $("input[name=username]", $form).val("won1");
        $("input[name=name]", $form).val("DEV Play G");
        $("input[name=email]", $form).val("devplayg@korea.com");
        $("input[name=inputPassword]", $form).val("dev123!@#");
    }
});