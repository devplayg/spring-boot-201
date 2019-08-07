$(function () {
    /**
     * 1. Initialize
     */
    let pager = new Pager(ctrl, filter);


    /**
     * 2. Events
     */

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
