function memberRoleListFormatter(val, row, idx) {
    let role = [];
    $.each(val, function(i, r) {
        var tag = "";
        if (r === "ADMIN") {
            tag = '<button type="bntton" class="btn btn-danger btn-xs"><i class="fa fa-star"></i> ' + userRoles[r] + '</button>';
            role.push(tag);
            return true;
        }

        tag = '<button type="bntton" class="btn btn-primary btn-xs">' + userRoles[r] + '</button>';
        role.push(tag);

    });
    return role.join('&nbsp;');
}

function memberAccessibleIpListFormatter(list, row, idx) {
    if (list === null || list.length < 1) {
        return;
    }

    var arr = [];
    $.each(list, function(i, ip) {
        if (ip.ipCidr.endsWith("/32")) {
            arr.push( ip.ipCidr.substring(0, ip.ipCidr.indexOf("/") ));
            return true;
        }
       arr.push(ip.ipCidr);
    });
    return arr.join("<br/>");
}

function memberActionFormatter(val, row, idx) {
    return commonActionFormatter(val, row, idx)
            + '<a class="password s14" href="javascript:void(0)" title="Change password"><i class="fa fa-key"></i></a>';
}