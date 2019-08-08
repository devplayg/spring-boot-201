$(function () {

    /**
     * 1. Define and initialize
     */

    $('.mask-username').mask('Z',{translation:  {'Z': {pattern: /[a-zA-Z0-9 ]/, recursive: true}}});

    let Member = function () {

        // Selected ID
        this.selected = null;

        // Table
        this.$table = $("#table-" + ctrl).bootstrapTable();

        // Forms
        this.$createForm = $("#form-" + ctrl + "-create");
        this.$updateForm = $("#form-" + ctrl + "-update");
        this.$passwordForm = $("#form-" + ctrl + "-password");

        // Create member
        // this.create = function () {
        //     let $this = this;
        //
        //     $.ajax({
        //         method: "POST",
        //         url: "/members",
        //         data: this.$createForm.serialize()
        //     }).done(function () {
        //         $this.$createForm.find(".modal").modal("hide");
        //     }).fail(function (jqXHR, textStatus, errorThrown) {
        //         $this.$createForm.find(".msg").text(jqXHR.responseText);
        //         $this.$createForm.find(".alert").removeClass("hide").addClass("in");
        //     });
        // };

        // Show member
        this.show = function (row) {
            this.selected = row;

            let $this = this;
            $.ajax({
                url: "/members/" + this.selected.id,
            }).done(function (data) {
                $("input[name=username]", $this.$updateForm).val(data.username);
                $("input[name=email]", $this.$updateForm).val(data.email);
                $("input[name=name]", $this.$updateForm).val(data.name);
                $("select[name=timezone]", $this.$updateForm).val(data.timezone).selectpicker("refresh");
                $("input[name=enabled]", $this.$updateForm).prop("checked", data.enabled);
                $.each(data.roleList, function (i, role) {
                    $("#revoke_" + role).prop("checked", true);
                });

                // Allowed IP list
                let accessibleIpListText = data.accessibleIpList.map(function (r, i) {
                    if (r.ipCidr.endsWith("/32")) {
                        return r.ipCidr.substr(0, r.ipCidr.indexOf("/"));
                    }
                    console.log(r.ipCidr);
                    return r.ipCidr;
                }).join("\n");
                $("textarea[name=accessibleIpListText]", $this.$updateForm).val(accessibleIpListText);
                $this.$updateForm.find(".modal").modal("show");
            }).fail(function (jqXHR, textStatus, errorThrown) {
                Swal.fire(jqXHR.responseJSON.message, row.username, "warning");
            });
        };

        // // Updte member
        // this.update = function () {
        //     let $this = this;
        //     $.ajax({
        //         method: "PATCH",
        //         url: "/members/" + this.selected.id,
        //         data: $this.$updateForm.serialize()
        //     }).done(function (data) {
        //         console.log(data);
        //         $this.$updateForm.find(".modal").modal("hide");
        //     }).fail(function (jqXHR, textStatus) {
        //         console.log(jqXHR);
        //         Swal.fire("failed to update", jqXHR.responseJSON.message, "error");
        //     });
        // };

        // Update member
        this.updatePassword = function () {
            let $this = this;
            $.ajax({
                method: "PATCH",
                url: "/members/" + this.selected.id + "/password",
                data: this.$passwordForm.serialize()
            }).done(function (data) {
                console.log(data);
                $this.$passwordForm.find(".modal").modal("hide");
            }).fail(function (jqXHR, textStatus) {
                console.log(jqXHR);
                Swal.fire("Error", jqXHR.responseJSON.message, "error");
            });
        };

        this.delete = function (row) {
            this.selected = row;
            let $this = this;

            Swal.fire({
                title: "Are you sure?",
                text: this.selected.username,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Yes, delete it!"
            }).then((result) => {
                if (result.value) {
                    $.ajax({
                        method: "DELETE",
                        url: "/members/" + $this.selected.id,
                    }).done(function (data) {
                        console.log(data);
                        $this.$table.bootstrapTable("refresh");
                    }).fail(function (jqXHR, textStatus) {
                        Swal.fire('failed to delete', row.username, "warning");
                    });
                }
            })
        };

        // Show password form
        this.showPasswordChangeForm = function (row) {
            this.selected = row;
            $("#modal-" + ctrl + "-password").modal("show");
        };

        // Validation
        this.$createForm.validate({
            submitHandler: function (form) {
                $.ajax({
                    method: "POST",
                    url: "/members",
                    data: $(form).serialize()
                }).done(function () {
                    $(form).find(".modal").modal("hide");
                }).fail(function (jqXHR) {
                    $(form).find(".msg").text(jqXHR.responseText);
                    $(form).find(".alert").removeClass("hide").addClass("in");
                });
            },
            rules: {
                username: {
                    required: true,
                    minlength: 3,
                    maxlength: 16,
                    username: true
                },
                name: {
                    minlength: 2,
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

        this.$updateForm.validate({
            submitHandler: function (form) {
                $.ajax({
                    method: "PATCH",
                    url: "/members/" + this.id,
                    data: $(form).serialize()
                }).done(function (data) {
                    console.log(data);
                    $(form).find(".modal").modal("hide");
                }).fail(function (jqXHR, textStatus) {
                    console.log(jqXHR);
                    Swal.fire("failed to update", jqXHR.responseJSON.message, "error");
                });
            },
            rules: {
                name: {
                    minlength: 2,
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

        this.$passwordForm.validate({
            submitHandler: function (form, e) {
                e.preventDefault();
                member.updatePassword(form);
            },
            rules: {
                password: {
                    required: true,
                    password: true
                },
                passwordConfirm: {
                    equalTo: "#" + this.$passwordForm.attr("id") + " input[name=password]"
                },
            }
        });
    };


    /**
     * 2. Events
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
            $form.validate().resetForm();
            $form.get(0).reset();
            $(".alert", $form).addClass("hide").removeClass("in");
            $(".alert .message", $form).empty();

            member.$table.bootstrapTable("refresh");
        })
        .on("shown.bs.modal", function () {
            let $form = $(this).closest("form");
            $form.find("input:not(readonly)[type=text],textarea").filter(":visible:first").focus().select();
        });

    /*
     * 4. Main
     */

    let member = new Member();



    // // Test code
    // {
        let $form = $("#form-member-create");
        $("input[name=username]", $form).val("won1");
        $("input[name=name]", $form).val("DEV Play G");
        $("input[name=email]", $form).val("devplayg@korea.com");
        $("input[name=inputPassword]", $form).val("dev123!@#");
    // }
});