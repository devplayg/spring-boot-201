$(function () {

    /*
     * 1. Define and initialize
     */
    let $table = $("#table-" + ctrl),
        logs = [];

    $(".datetime").datetimepicker(defaultDatetimeOption);
    $table.bootstrapTable();
    console.log($table.bootstrapTable("getOptions"));

    $table.bootstrapTable({
        sidePagination: "client", // Client-side pagination
    }).on("column-switch.bs.table", function (e, field, checked) { // Memorize columns state
        captureTableColumnsState($(this));
    }).on("refresh.bs.table", function () { // Refresh
        movePage(PageCurrent, true);
    });

    restoreTableColumnsState($table);

    // Page information
    let paging = {
        no: 1, // Page number
        size: $table.bootstrapTable("getOptions").pageSize, // Page size
        blockIndex: 0, // Block index
        blockIndex_before: -1, // Previous block index
        blockSize: 3 // block size
    };


    /*
     * 2. Event
     */

    // fast paging
    $(".btn-move-page").click(function (e) {
        e.preventDefault();
        movePage($(this).data("direction"), false);
    });


    /*
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
            tuneDate(filter);

            let url = "/" + ctrl + "?" + $.param(filter, true) + "&" + $.param(pageable, true);
            $.ajax({
                type: "GET",
                async: true,
                url: url
            }).done(function (data) {
                logs = data;
                renderDataToTable($table, logs, paging);
                updatePagingNavButtons();
            }).fail(function (jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
                // Swal.fire(jqXHR.message, "", 'warning');
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
        $(".btn-page-prev").prop("disabled", paging.no == 1)
    }



    // 테이블 이벤트
    movePage(PageCurrent, true);
});