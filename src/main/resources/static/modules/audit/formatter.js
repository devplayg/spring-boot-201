function auditMemberFormatter(member, row, idx) {
    if (member === null) {
        return;
    }

    var badge = "";
    $.each(member.roleList, function(i, r) {
        badge = '<button class="btn btn-default btn-xs">' + r + '</button>';
        console.log(r);
    });
    return member.username; // + '<span class="pull-right">' + badge + '</span>';
}

function auditMessageFormatter(msg, row, idx) {
    if (msg === null || msg === "") {
        return;
    }
}