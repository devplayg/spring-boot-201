$(function () {
    console.log(3);
    /*
     * 1. Define
     */
    var $table = $( "#table-members" ),
        $form = $( "#form-member-add" );


    /*
     * 2. Initialize
     */
    var fillTextData = function() {
        $("input[name=username]", $form).val("wsan");
        $("input[name=name]", $form).val("WON SEOK AHN");
        $("input[name=email]", $form).val("wsan@unisem.co.kr");
        $("input[name=inputPassword]", $form).val("wsan123!@#");
    };
    fillTextData();
    $table.bootstrapTable({
        url: $table.data( "url" )
    });

    /*
     * 3. Events
     */
    $form.validate({
        submitHandler: function (form, e) {
            e.preventDefault();
            //
            console.log( $(form).serialize() );

            // var $form = $(form);
            $.ajax({
                type: 'POST',
                async: true,
                url: '/members',
                data: $(form).serialize()
            }).done(function(result) {
                if (result.error === null) {
                    $('#table-members').bootstrapTable('refresh');
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


    /*
     * 3. Functions
     */



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