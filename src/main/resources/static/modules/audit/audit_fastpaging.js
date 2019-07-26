$(function () {

    /**
     * 1. Define and initialize
     */

    // Variables
    let $table = $("#table-" + ctrl),

        // Log queue
        logs = [],

        // Paging
        paging = {
            no: 1, // Page number
            size: 0, // Page size
            blockIndex: 0, // Block index
            blockIndex_before: -1, // Previous block index
            blockSize: 20 // fetch N pages of data at a time
        };

    // Datetime
    $(".datetime").datetimepicker(defaultDatetimeOption);

    // Bootstra-table
    $table.bootstrapTable({
        sidePagination: "client", // Client-side pagination
    }).on("column-switch.bs.table", function (e, field, checked) { // Memorize columns state
        captureTableColumnsState($(this));
    }).on("refresh.bs.table", function () { // Refresh
        movePage(0, true);
    }).on("sort.bs.table", function (e, name, order) { // Refresh
        movePage(0, true);
    });
    paging.size = $table.bootstrapTable("getOptions").pageSize;
    restoreTableColumnsState($table);


    /**
     * 2. Event
     */

    // fast paging
    $(".btn-move-page").click(function (e) {
        e.preventDefault();
        movePage($(this).data("direction"), false);
    });


    /**
     * 3. Function
     */

    function movePage(direction, refresh) {

        // Calculate page
        paging.no += direction; // Page to search
        if (paging.no < 1) {
            paging.no = 1;
            return;
        }
        paging.blockIndex = Math.floor((paging.no - 1) / paging.blockSize);
        $(".btn-page-text").text(paging.no);

        // Fetch and display data
        if ((paging.blockIndex !== paging.blockIndex_before) || refresh) {
            let pageable = {
                page: Math.ceil(paging.no / paging.blockSize) - 1,
                size: paging.size * paging.blockSize,
                sort: $table.bootstrapTable("getOptions").sortName + "," + $table.bootstrapTable("getOptions").sortOrder,
            };
            console.log(pageable);

            let _filter = tuneDate(filter);
            let url = "/" + ctrl + "?" + $.param(_filter, true) + "&" + $.param(pageable, true);
            // console.log(url);
            // console.log(pageable);
            $.ajax({
                url: url
            }).done(function (data) {
                logs = data;
                renderDataToTable($table, logs, paging);
                updatePagingNavButtons();
            }).fail(function (jqXHR, textStatus, errorThrown) {
                Swal.fire("Error", jqXHR.message, 'warning');
            });
        } else {
            renderDataToTable($table, logs, paging);
            updatePagingNavButtons();
        }

        paging.blockIndex_before = paging.blockIndex;
    }

    function updatePagingNavButtons() {
        let offset = ((paging.no - 1) % paging.blockSize) * paging.size;
        $(".btn-page-next").prop("disabled", (logs.length - offset < paging.size));
        $(".btn-page-prev").prop("disabled", paging.no === 1)
    }

    // 테이블 이벤트
    movePage(0, true);
});