function dateFormatter(val, row, idx) {
    return convertToUserTime(val);
}

function datePrettyFormatter(val, row, idx) {
    var dt = convertToUserTime(val);
    var m = moment(dt);

    return '<small>' + m.format("YYYY-MM-DD")
        + '<span class="txt-color-grayLight">T</span>'
        + '' + m.format("HH:mm:ss") + ''
        + '<span class="txt-color-grayLight">' + m.format("Z") + '</span></small>';
}

function commonActionFormatter(val, row, idx) {
    return [
        '<a class="update" href="javascript:void(0)" title="Update"><i class="fa fa-pencil"></i></a>',
        '<a class="delete" href="javascript:void(0)" title="Delete"><i class="fa fa-trash-o"></i></a>',
    ].join(' ');
}

function intIpFormatter(val, row, idx) {
    return intToip(val);
}