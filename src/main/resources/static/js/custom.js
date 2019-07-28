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
 * jquery-validation default settings
 */

jQuery.validator.setDefaults({
    // debug: true,
    errorClass: "help-block",
    highlight: function (element) {
        $(element).closest(".form-group").addClass("has-error");
    },
    unhighlight: function (element) {
        $(element).closest(".form-group").removeClass("has-error");
    },
});


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

// Update paging buttons
function updatePagingNavButtons(paging, navigationButtonGroup) {
    let offset = ((paging.no - 1) % paging.blockSize) * paging.size;
    navigationButtonGroup.prev.prop("disabled", paging.no === 1)
    navigationButtonGroup.next.prop("disabled", (paging.dataLength - offset < paging.size));
}

/**
 * Network functions
 */

function ipToint(ip) {
    return ip.split('.').reduce(function (ipInt, octet) {
        return (ipInt << 8) + parseInt(octet, 10)
    }, 0) >>> 0;
}

function intToip(ipInt) {
    return ((ipInt >>> 24) + '.' + (ipInt >> 16 & 255) + '.' + (ipInt >> 8 & 255) + '.' + (ipInt & 255));
}


/**
 * Date and paging function
 */

function convertToUserTime(dt) {
    return moment(dt).tz(userTz).format();
}

function tuneFilterAndPageable(filter, normalPagingParam) {
    if (filter.hasOwnProperty("startDate")) {
        filter.startDate = filter.startDate.replace("T", " ").substring(0, 16);
    }
    if (filter.hasOwnProperty("endDate")) {
        filter.endDate = filter.endDate.replace("T", " ").substring(0, 16);
    }

    delete filter.pageable;

    // Fast paging
    if (filter.fastPaging) {
        return filter;
    }

    // Normal paging
    return Object.assign(filter, {
        size: normalPagingParam.pageSize,
        page: normalPagingParam.pageNumber - 1,
        sort: normalPagingParam.sortName + "," + normalPagingParam.sortOrder,
    });
}