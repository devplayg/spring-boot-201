function auditMemberFormatter(member, row, idx) {
    if (member === null) {
        return;
    }

    return member.username;
}