$(function () {
    let pager = new Pager(ctrl, filter);

    // Validation
    pager.form.validate({
        submitHandler: function (form, e) {
            e.preventDefault();
            form.submit();
        },
        rules: {
            ip: {
                required: false,
                ipv4_cidr: true,
            },
        },
    });
});
