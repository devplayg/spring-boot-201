/**
 * jquery-mask default settings
 */

$(".mask-yyyymmddhhii").mask("0000-00-00 00:00");
$(".mask-ipv4-cidr").mask("099.099.099.099/09");
$(".mask-09999").mask("09999");
$(".mask-0999").mask("0999");
$(".mask-099").mask("099");
$(".mask-09").mask("09");


/**
 * jquery-mask default settings
 */
// Noty.setMaxVisible(6);


/**
 * Form utils
 */

function objectifyForm($form) {//serialize data function
    // Check multiple select
    let multipleSelect = {};
    $("select", $form).each(function(i, e) {
        multipleSelect[$(e).attr("name")] = $(e).prop("multiple");
    });

    let arr = $form.serializeArray(),
        obj = {};

    $.map(arr, function(e, i){
        let name = e["name"],
            value = e["value"];
        if (multipleSelect[name] !== undefined && multipleSelect[name] === true) {
            if ($.isArray(obj[name])) {
                obj[name].push(value);
                return;
            }
            obj[name] = [value];
            return;
        }

        // PagingMode must be integer type
        if (name === "pagingMode") {
            obj[name] = parseInt(value, 10);
            return;
        }

        obj[name] = e["value"];
    });

    return obj;
}

/**
 * WaitMe
 */

let waitMeOptions = {
    effect : "stretch",
    text : 'Loading..',
    bg : "rgba(255,255,255,0.7)",
    color : "#616469"
};


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
    classes: 'table table-hover',
    showRefresh: true,
    showColumns: true,
    pageSize: 15,
});

// Capture table column state
function captureTableColumnsState($table) {
    // console.log("memory table columns");

    let cols = [],
        key = getTableKey($table);
    $table.find("th").each(function (i, th) {
        let col = $(th).data("field");
        cols.push(col);
    });
    Cookies.set(key, cols.join(","), {expires: 365});
}

// Generate table key
function getTableKey($table) {
    return 'tk-' + $table.attr("id");
}

// Restore table column state
function restoreTableColumnsState($table) {
    // let key = getTableKey($table);
    // if (Cookies.get(key) !== undefined) {
    //     let h = {};
    //     $.map(Cookies.get(key).split(","), function (col, i) {
    //         h[col] = true;
    //         try {
    //             $table.bootstrapTable("showColumn", col);
    //         } catch {
    //
    //         }
    //     });
    //
    //     $table.find("th").each(function (i, th) {
    //         let col = $(th).data("field");
    //         if (h[col]) {
    //             $table.bootstrapTable("showColumn", col);
    //         } else {
    //             $table.bootstrapTable("hideColumn", col);
    //         }
    //     });
    // }
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
    return moment.tz(dt, systemTz).tz(userTz);
}


/**
 * String
 */
function dashToCamelCase(str) {
    return str.replace(/-([a-z])/g, function (g) { return g[1].toUpperCase(); });
}

function camelCaseToDash (str) {
    return str.replace(/([a-zA-Z])(?=[A-Z])/g, '$1-').toLowerCase()
}



/**
 * Timer
 */

let sysInfo = null;

function getLastFactoryEventId() {
    let val = localStorage.getItem("lastFactoryEventId");
    if (val === null ) {
        return 0;
    }
    return parseInt(val)
}

function updateSystemInfoText() {
    if (sysInfo !== null) {
        sysInfo.m = moment.unix(sysInfo.time).tz(userTz);
        $("#header .systemTime").text(sysInfo.m.format("ddd") + ", "  + sysInfo.m.format("ll") + ", " + sysInfo.m.format("HH:mm:ss"));

        $(".momentjs").each(function(i, e) {
            let format = $(this).data("format");
            if (format !== undefined) {
                $(this).text(sysInfo.m.format(format));
            }
        });
    }
}

let sysTicker = function() {
    if (sysInfo === null) {
        return;
    }

    sysInfo.time++;
    updateSystemInfoText();
};

function tr(key) {
    if (langMap[key] !== undefined) {
        return langMap[key];
    }
    return key;
}
