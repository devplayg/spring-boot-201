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