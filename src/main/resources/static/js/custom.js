
const PageCurrent = 0;

/**
 * jquery-mask default settings
 */
$(".mask-yyyymmddhhii").mask("0000-00-00 00:00");
$(".mask-ipv4-cidr").mask("099.099.099.099/09");
$(".mask-09999").mask("09999");
$(".mask-0999").mask("0999");
$(".mask-099").mask("099");


/**
 * Bootstrap-datetimepicker default settings
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
 * Bootstrap-Table default settings
 */

// Default settings
$.extend($.fn.bootstrapTable.defaults, {
    showRefresh: true,
    showColumns: true,
    pageSize: 15,
});

// Generate table key
function getTableKey($table) {
    return 'tk-' + $table.attr("id");
}

// Render data to table
function renderDataToTable($table, logs, paging) {
    let offset = ((paging.no - 1) % paging.blockSize) * paging.size;
    $table.bootstrapTable("load", logs.slice(offset, offset + paging.size));
}

// Capture table column state
function captureTableColumnsState($table) {
    console.log($table);
    let cols = [],
        key = getTableKey($table);
    $table.find("th").each(function (i, th) {
        let col = $(th).data("field");
        cols.push(col);
    });
    Cookies.set(key, cols.join(","), {expires: 365});
}

// Restore table column state
function restoreTableColumnsState($table) {
    let key = getTableKey($table);
    // console.log(Cookies.get())
    if (Cookies.get(key) !== undefined) {
        let h = {};
        $.map(Cookies.get(key).split(","), function (col, i) {
            h[col] = true;
            $table.bootstrapTable("showColumn", col);
        });

        $table.find("th").each(function (i, th) {
            let col = $(th).data("field");
            if (h[col]) {
                $table.bootstrapTable("showColumn", col);
            } else {
                $table.bootstrapTable("hideColumn", col);
            }
        });
    }
}


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

function tuneDate(filter) {
    if (filter.hasOwnProperty("startDate")) {
        filter.startDate = filter.startDate.replace("T", " ").substring(0, 16);
    }
    if (filter.hasOwnProperty("endDate")) {
        filter.endDate = filter.endDate.replace("T", " ").substring(0, 16);
    }
}

function tuneDateAndPaging(filter, param) {
    tuneDate(filter);

    Object.assign(filter, {
        size: param.pageSize,
        page: param.pageNumber - 1,
        sort: param.sortName + "," + param.sortOrder,
    });
}

function convertToUserTime(dt) {
    return moment(dt).tz(userTz).format();
}
