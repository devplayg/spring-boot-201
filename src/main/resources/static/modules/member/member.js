$(function () {

    /*
     * 1. Define
     */
    let $table = $("#table-member"),
        $form = $("#form-member-add");

    // Test
    $("input[name=username]", $form).val("wsan");
    $("input[name=name]", $form).val("WON SEOK AHN");
    $("input[name=email]", $form).val("wsan@unisem.co.kr");
    $("input[name=inputPassword]", $form).val("wsan123!@#");


    /*
     * 2. Initialize
     */
    window.memberCommandEvents = {
        "click .update": function (e, val, row, idx) {
            updateMember(row);
        },
        "click .delete": function (e, val, row, idx) {
            deleteMember(row);
        }
    };
    $table.bootstrapTable();


    /*
     * 3. Events
     */
    $form.validate({
        submitHandler: function (form, e) {
            e.preventDefault();
            //
            console.log($(form).serialize());

            // var $form = $(form);
            $.ajax({
                type: 'POST',
                async: true,
                url: '/members',
                data: $(form).serialize()
            }).done(function (result) {
                if (result.error === null) {
                    $('#table-member').bootstrapTable('refresh');
                    $form.closest('.modal').modal('hide');
                } else {
                    $form.find('.msg').text(result.error);
                    $form.find('.alert').removeClass('hide').addClass('in');
                }
            }).always(function () {
            });
        },
        ignore: "input[type='hidden']",
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

    // let memberCommandEvents = {
    //     'click .like': function (e, val, row, idx) {
    //
    //     },
    //     'click .like': function (e, val, row, idx) {
    //
    //     }
    // };

    // console.log(3);
    // window.memberCommandEvents = {
    //     "click .update": function (e, val, row, idx) {
    //         updateMember(row);
    //     },
    //     "click .delete": function (e, val, row, idx) {
    //         deleteMember(row);
    //     }
    // };


    /*
     * 3. Functions
     */
    function updateMember(row) {
        console.log(row);
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