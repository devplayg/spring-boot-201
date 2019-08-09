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

function createEmailLink(email) {
    return '<a href="mailto:' + email + '">' + email + '</a>';
}


/**
 * Date and paging function
 */

function convertToUserTime(dt) {
    return moment.tz(dt, systemTz).tz(userTz);
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
        let m = moment.unix(sysInfo.time).tz(userTz);
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

    const SERVER_SOCKET_API = "/ws";
    const ENTER_KEY = 13;

    this.stompClient = null;

    this.connected = false;

    this.input = $("#message-input input[name=message]");
    this.output = $("#message-output");

    this.connect = function () {
        let socket = new SockJS(SERVER_SOCKET_API);
        this.stompClient = Stomp.over(socket);
        this.stompClient.debug = null

        let $this = this;
        this.stompClient.connect({}, function (frame) {
            $this.setConnected(true);
            // s.stompClient.send("/messaging/join", {}, JSON.stringify({'message': "I'm in"}));
            $this.stompClient.subscribe('/topic/public', function (packet) {
                $this.printMessage(packet);
                $this.checkBadge();
            });
        });
    };

    this.checkBadge = function() {
        let $badge = $('#activity > .badge');
        if (parseInt($badge.text()) > 0) {
            $badge.addClass("bg-color-red bounceIn animated");
        }
    }

    this.printMessage = function(packet) {
        this.output.prepend(createMessage(packet));
        let alarmCount = parseInt($("#activity .badge").text());
        $("#activity .badge").text(++alarmCount);
    };

    this.disconnect = function() {
        if (this.stompClient !== null) {
            // this.stompClient.send("/messaging/join", {}, JSON.stringify({'message': "I'm out"}));
            this.stompClient.disconnect();
        }
        this.setConnected(false);
        console.log("Disconnected");
    };

    this.sendInput = function() {
        this.send(this.input.val().trim());
        this.clearInput();
    };

    this.send = function(message) {
        this.stompClient.send("/messaging/talk", {}, JSON.stringify({'message': message}));
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
    };

    this.init = function () {
        this.connect();
        this.input.keydown(this.chatKeyDownHandler);
    };

    this.init();
};
let webSocket = new WebSocket();

function sendMessage() {
    webSocket.sendInput();
}

function createMessage(packet) {
    console.log(packet);
    let message = JSON.parse(packet.body);
    return $("<div/>", {
        "class": 'alert alert-info fade in',
    }).html("<strong>[" + message.username + "]</strong> " + message.message);
}

