
function dateFormatter(val, row, idx) {
    return moment(val).tz(userTz).format();
}

function commonActionFormatter(val, row, idx) {
    return [
        '<a class="update" href="javascript:void(0)" title="Update">',
        '*',
        '</a>',

        '<a class="delete" href="javascript:void(0)" title="Delete">',
        'X',
        '</a>'
    ].join('');
}

function intIpFormatter(val, row, idx) {
    return intToip(val);
}