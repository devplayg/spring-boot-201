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