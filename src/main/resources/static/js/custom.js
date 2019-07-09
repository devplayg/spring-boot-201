/**
 * Default masks
 */
$(".mask-yyyymmddhhii").mask("0000-00-00 00:00");
$(".mask-ipv4-cidr").mask("099.099.099.099/09");
$(".mask-09999").mask("09999");
$(".mask-0999").mask("0999");
$(".mask-099").mask("099");


/**
 * Default datetime
 */
let defaultDatetimeOption = {
    format: "yyyy-mm-dd hh:ii",
    pickerPosition: "bottom-left",
    todayHighlight: 1,
    minView: 2,
    maxView: 4,
    autoclose: true
};

/**
 * Default table settings
 */
$.extend($.fn.bootstrapTable.defaults, {
    pagination: true,
    showRefresh: true,
    showColumns: true,
    pageSize: 15,
});

/**
 * Functions
 */
function ipToint(ip) {
    return ip.split('.').reduce(function (ipInt, octet) {
        return (ipInt << 8) + parseInt(octet, 10)
    }, 0) >>> 0;
}

function intToip(ipInt) {
    return ((ipInt >>> 24) + '.' + (ipInt >> 16 & 255) + '.' + (ipInt >> 8 & 255) + '.' + (ipInt & 255));
}

function refineJavaDate(filter) {
    if (filter.hasOwnProperty("startDate")) {
        filter.startDate = filter.startDate.replace("T", " ").substring(0, 16);
    }
    if (filter.hasOwnProperty("endDate")) {
        filter.endDate = filter.endDate.replace("T", " ").substring(0, 16);
    }
}

function refineJavaDateWithPaging(filter, param) {
    refineJavaDate(filter);

    Object.assign(filter, {
        size: param.pageSize,
        page: param.pageNumber - 1,
        sort: param.sortName + "," + param.sortOrder,
    });
}

function convertToUserTime(dt) {
    return moment(dt).tz(userTz).format();
}