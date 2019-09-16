$(function () {
    /**
     * 1. Initialize
     */

    let pager = new Pager({
        id: ctrl,
        showLoading: true,
        rules: {
            ip: {
                required: false,
                ipv4_cidr: true,
            },
        },
    }).run();

});
