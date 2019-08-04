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

function tuneFilterAndPageable(filter, generalPagingParam) {
    if (filter.hasOwnProperty("startDate")) {
        filter.startDate = filter.startDate.replace("T", " ").substring(0, 16);
    }
    if (filter.hasOwnProperty("endDate")) {
        filter.endDate = filter.endDate.replace("T", " ").substring(0, 16);
    }

    delete filter.pageable;

    // Fast paging
    if (filter.pagingMode === PagingMode.FastPaging) {
        return filter;
    }

    // Normal paging
    return Object.assign(filter, {
        size: generalPagingParam.pageSize,
        page: generalPagingParam.pageNumber - 1,
        sort: generalPagingParam.sortName + "," + generalPagingParam.sortOrder,
    });
}

/**
 * Timer
 */
let sysInfo = null;
function fetchSystemInfo() {
    $.ajax({
        url: "/public/sysinfo",
    }).done(function (data) {
        // {"timezone":"Asia/Seoul","time":1564372825}
        sysInfo = data;
        updateSystemInfoText();
    });
}

// let sysTime = null;
function updateSystemInfoText() {
    if (sysInfo !== null) {
        let m = moment.unix(sysInfo.time).tz(sysInfo.timezone);
        $("#header .systemTime").text(m.format("MMM D, HH:mm:ss"));
    }
}
let sysTicker = function() {
    sysInfo.time++;
    updateSystemInfoText();
    if (sysInfo.time % 60 === 0) {
        fetchSystemInfo();
    }
}
fetchSystemInfo();
setInterval(sysTicker, 1000);

/**
 * Websocket
 */

let WebSocket = function () {

    const SERVER_SOCKET_API = "/websocket";
    const ENTER_KEY = 13;

    this.stompClient = null;

    this.connected = false;

    this.input = $("#message-box input[name=message]");
    this.output = $("#message-box .message-list");

    this.connect = function () {
        console.log(SERVER_SOCKET_API);
        let socket = new SockJS(SERVER_SOCKET_API);
        this.stompClient = Stomp.over(socket);

        let s = this;
        this.stompClient.connect({}, function (frame) {
            s.setConnected(true);
            console.log('Connected: ' + frame);
            s.stompClient.subscribe('/topic/system', function (packet) {
                s.printMessage(packet);
            });
        });
    };

    this.printMessage = function(packet) {
        $("#header .message-list").prepend(createMessage(packet));
    };

    this.disconnect = function() {
        if (this.stompClient !== null) {
            this.stompClient.disconnect();
        }
        this.setConnected(false);
        console.log("Disconnected");
    };

    this.send = function() {
        let message = $("#message-box input[name=message]").val().trim();
        message = JSON.stringify({'message': message});
        this.stompClient.send("/messaging/hello", {}, message);
        this.clearInput();
    };

    this.setConnected = function(connected) {
        this.connected = connected;
    };

    this.clearInput = function() {
        this.input.val("");
    };


    this.chatKeyDownHandler = function (e) {
        if (e.which === ENTER_KEY) {
            if ($(this).val().trim() !== "") {
                sendMessage();
                return false;
            }
            return false;
        }


        // if (e.which === ENTER_KEY && this.input.val().trim() !== "") {
        //     this.send();
        //     return false;
            // sendMessage(inputElm.value);
            // clear(inputElm);
        // }

    }

    this.init = function () {
        this.connect();
        // this.input.keydown(function() {
        //     return false;
        // });
        this.input.keydown(this.chatKeyDownHandler);
            // if (key.keyCode === ENTER_KEY) {
            //     return false;
            // }
        // });s
    };

    this.init();
};
let webSocket = new WebSocket();

function sendMessage() {
    webSocket.send();
}
// $("#btn-message-send").click(function (e) {
//     e.preventDefault();
//     webSocket.send();
// })

function createMessage(packet) {
    let message = JSON.parse(packet.body).message;
    return $("<div/>", {
        "class": 'alert alert-info fade in',
    }).html("<strong>Info</strong> " + message);
}