$(function() {
    $("#form-login").validate({
        submitHandler: function (form, e) {
            e.preventDefault();
            form.submit();

        },
        rules: {
            app_username: {
                required: true,
            },
            app_password: {
                required: true,
            },
        }
    });
});
