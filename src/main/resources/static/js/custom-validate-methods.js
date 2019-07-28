$.validator.addMethod('username', function (value) {
    let pattern = /^[a-zA-Z]{1}[a-zA-Z0-9_]{3,16}$/;
    if (value.match(pattern)) {
        return true;
    }
}, 'invalid username');

$.validator.addMethod('ipv4_cidr', function (value, element) {
    if (this.optional(element)) {
        return true;
    }

    let regex_ipv4 = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/,
        regex_ipv4_cidr = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))$/;

    if (value.match(regex_ipv4) || value.match(regex_ipv4_cidr)) {
        return true;
    }
}, 'invalid IP');

$.validator.addMethod('password', function (value) {
    if (value.length == 0) {
        return true;
    }
    let regex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{9,16}$/;
    //let regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{9,16}$/;
    // if (__USE_EASY_PASSWORD__ !== undefined) {
    //     if (__USE_EASY_PASSWORD__) {
    //         regex = /^.{4,16}$/;
    //     }
    // }
    if (value.match(regex)) {
        return true;
    }
}, 'invalid password');

$.validator.addMethod('ipv4', function (value) {
    if (value.length == 0) return true;

    let regex = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/;

    if (value.match(regex)) {
        return true;
    }
}, 'invalid IP');


$.validator.addMethod('ipv4_repeat', function (value) {
    if (value.length == 0) {
        return true;
    }

    let regex = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/;

    let arr = value.replace(/(\n|\t| )/g, ",").split(",");
    for (let i = 0; i < arr.length; i++) {
        arr[i] = $.trim(arr[i]);
        if (arr[i].length > 0) {
            if (!arr[i].match(regex)) {
                return false;
            }
        }
    }
    return true;
}, 'invalid IP');

$.validator.addMethod('member_name', function (value) {
    let pattern = /^[^\d].*/;

    if (value.match(pattern)) {
        return true;
    }
}, "invalid name");



