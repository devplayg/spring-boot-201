function memberRoleListFormatter(val, row, idx) {
    let str = "";
    $.each(val, function(i, r) {
        str += '<button type="bntton" class="btn btn-primary btn-xs">' + userRoles[r] + '</button>';
    });
    return str;
}