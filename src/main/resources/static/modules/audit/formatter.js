function auditMemberFormatter(member, row, idx) {
    if (member === null) {
        return;
    }

    let badge = "";
    $.each(member.roleList, function(i, r) {
        badge = '<button class="btn btn-default btn-xs">' + r + '</button>';
    });
    return member.username; // + '<span class="pull-right">' + badge + '</span>';
}

function auditMessageFormatter(msg, row, idx) {
    if (msg === null || msg === "") {
        return;
    }
    return msg;
}