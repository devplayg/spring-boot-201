function dateFormatter(val, row, idx) {
    return convertToUserTime(val);
}

function datePrettyFormatter(val, row, idx) {
    var dt = convertToUserTime(val),
        m = moment(dt);

    return '' + m.format("YYYY-MM-DD")
        + '<span class="txt-color-grayLight">T</span>'
        + '' + m.format("HH:mm:ss") + ''
        + '<span class="txt-color-grayLight">' + m.format("Z") + '</span>';
}

function commonActionFormatter(val, row, idx) {
    return [
        '<a class="update s14" href="javascript:void(0)" title="Update"><i class="fa fa-pencil"></i></a>',
        '<a class="delete s14 txt-color-red" href="javascript:void(0)" title="Delete"><i class="fa fa-times"></i></a>',
    ].join(' ');
}

function intIpFormatter(val, row, idx) {
    return intToip(val);
}

function emailFormatter(val, row, idx) {
    return createEmailLink(val);
}