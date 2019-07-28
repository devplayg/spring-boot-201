$(function () {

    /*
     * 1. Define and initialize
     */
    let $table = $("#table-" + ctrl);

    $(".datetime").datetimepicker(defaultDatetimeOption);

    $table.bootstrapTable({
        url: "/" + ctrl,
        queryParamsType: "", // DO NOT REMOVE. LEAVE BLANK
        pagination: true,
        sidePagination: "server",
        queryParams: function (params) {
            tuneDateAndPaging(filter, params);
            return $.param(_filter, true);
        },
        responseHandler: function (res) {
            console.log(res);
            if (filter.fastPaging) {
                return res;
            }
            return {
                total: res.totalElements,
                rows: res.content
            };
        }
    }).on("column-switch.bs.table", function (e, field, checked) {
        captureTableColumnsState($(this));
    });

    /*
     * 2. Event
     */


    /*
     * 3. Function
     */


    // $('table').bootstrapTable({locale:'en-US'});
    // $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['en-US']);
});
//
//
// $(function () {
//
//     'use strict';
//
//     let Pager = {
//
//         // Bootstrap-table
//         table: $("#table-" + ctrl),
//
//         // Filter form
//         form: $("#form-" + ctrl),
//
//         // Queue where data is to be stored
//         log: [],
//
//         // Navigation button elements
//         navigation: {
//             page: null,
//             prev: null,
//             next: null,
//         },
//
//         // Paging variables
//         paging: {
//             no: 1, // Page number
//             size: 0, // Page size
//             blockIndex: 0, // Block index
//             blockIndex_before: -1, // Previous block index
//             blockSize: 20, // fetch N pages of data at a time
//             dataLength: 0,
//             sort: "",
//         },
//
//         setSort: function (name, order) {
//             this.paging.sort = name + "," + order;
//
//             // for Post request
//             $("input[name=sort]", this.form).val(this.paging.sort);
//
//             // for Ajax request of bootstrap-table
//             this.table.bootstrapTable("refreshOptions", {
//                 sortName: name,
//                 sortOrder: order,
//             });
//         },
//
//         init: function () {
//             console.log("# Initializing");
//
//             // Initialize datetime-picker
//             $(".datetime").datetimepicker(defaultDatetimeOption);
//
//             // Initialize bootstrap-table
//             this.initTable();
//
//             // Initialize paging
//             this.initPaging();
//
//             // Set page navigations
//             this.initNavigation();
//         },
//
//         initNavigation: function() {
//             this.navigation.page = $(".btn-page-text", this.form);
//             this.navigation.prev = $(".btn-page-prev", this.form);
//             this.navigation.next = $(".btn-page-next", this.form);
//         },
//
//         initPaging: function() {
//             this.paging.size = this.table.bootstrapTable("getOptions").pageSize;
//             this.paging.sort = this.table.bootstrapTable("getOptions").sortName
//                 + ","
//                 + this.table.bootstrapTable("getOptions").sortOrder;
//         },
//
//         initTable: function () {
//             let c = this;
//             this.table.bootstrapTable({
//                 sidePagination: "client", // Client-side pagination
//
//             }).on("column-switch.bs.table", function () {
//                 // Store the state of the columns
//                 captureTableColumnsState($(this));
//
//             }).on("refresh.bs.table", function () {
//                 c.fetchPage(0, true);
//
//             }).on("sort.bs.table", function (e, name, order) { // Refresh
//                 c.setSort(name, order);
//                 c.fetchPage(0, true);
//             });
//
//             // Initialize columns
//             restoreTableColumnsState(this.table);
//         },
//
//         fetchPage: function (direction, refresh) {
//             if (direction === undefined) {
//                 direction = 0;
//             }
//
//             if (refresh === undefined) {
//                 refresh = false;
//             }
//
//             // Calculate page
//             this.paging.no += direction; // Page to search
//             if (this.paging.no < 1) {
//                 this.paging.no = 1;
//             }
//
//             this.paging.blockIndex = Math.floor((this.paging.no - 1) / this.paging.blockSize);
//             this.navigation.page.text(this.paging.no);
//
//             // Fetch and display data
//             if ((this.paging.blockIndex !== this.paging.blockIndex_before) || refresh) {
//                 let pagingParam = {
//                     page: Math.ceil(this.paging.no / this.paging.blockSize) - 1,
//                     size: this.paging.size * this.paging.blockSize,
//                     sort: this.paging.sort,
//                 };
//
//                 let _filter = tuneFilterAndPageable(filter);
//                 let url = "/" + ctrl + "?" + $.param(_filter, true) + "&" + $.param(pagingParam, true);
//                 console.log("# fetch url: " + url);
//                 // console.log( pagingParam );
//                 // console.log( $.param(pagingParam, true) );
//                 // console.log(_filter);
//                 $.ajax({
//                     url: url,
//                     context: this,
//                 }).done(function (data) {
//                     this.log = data;
//                     this.paging.dataLength = this.log.length;
//                     renderDataToTable(this.table, this.log, this.paging);
//                     updatePagingNavButtons(this.paging, this.navigation.prev, this.navigation.next);
//                 }).fail(function (jqXHR, textStatus, errorThrown) {
//                     Swal.fire("Error", jqXHR.message, 'warning');
//                 });
//             } else {
//                 renderDataToTable(this.table, this.log, this.paging);
//                 updatePagingNavButtons(this.paging, this.navigation.prev, this.navigation.next);
//             }
//             this.paging.blockIndex_before = this.paging.blockIndex;
//         }
//
//     };
//
//     //pager = new Pager;;
//
//     // audit.init();
//     // console.log(audit.paging);
//
//     // audit.fetchPage();
//
//     $(".btn-move-page").click(function () {
//         // audit.fetchPage($(this).data("direction"), false);
//     });
// });
//
//
// // $(function () {
// //
// //     /**
// //      * 1. Define and initialize
// //      */
// //
// //         // Variables
// //     let $table = $("#table-" + ctrl),
// //
// //         // Log queue
// //         logs = [],
// //
// //         // Paging
// //         paging = {
// //             no: 1, // Page number
// //             size: 0, // Page size
// //             blockIndex: 0, // Block index
// //             blockIndex_before: -1, // Previous block index
// //             blockSize: 20 // fetch N pages of data at a time
// //         };
// //
// //     // Datetime
// //     $(".datetime").datetimepicker(defaultDatetimeOption);
// //
// //     // Bootstrap-table
// //     $table.bootstrapTable({
// //         sidePagination: "client", // Client-side pagination
// //     }).on("column-switch.bs.table", function (e, field, checked) { // Memorize columns state
// //         captureTableColumnsState($(this));
// //     }).on("refresh.bs.table", function () { // Refresh
// //         movePage(0, true);
// //     }).on("sort.bs.table", function (e, name, order) { // Refresh
// //         movePage(0, true);
// //     });
// //     paging.size = $table.bootstrapTable("getOptions").pageSize;
// //     restoreTableColumnsState($table);
// //
// //
// //     /**
// //      * 2. Event
// //      */
// //
// //     $(".btn-move-page").click(function() {
// //         movePage($(this).data("direction"), false);
// //     });
// //
// //
// //     /**
// //      * 3. Function
// //      */
// //
// //     function movePage(direction, refresh) {
// //
// //         // Calculate page
// //         paging.no += direction; // Page to search
// //         if (paging.no < 1) {
// //             paging.no = 1;
// //             return;
// //         }
// //         paging.blockIndex = Math.floor((paging.no - 1) / paging.blockSize);
// //         $(".btn-page-text").text(paging.no);
// //
// //         // Fetch and display data
// //         if ((paging.blockIndex !== paging.blockIndex_before) || refresh) {
// //             let pageable = {
// //                 page: Math.ceil(paging.no / paging.blockSize) - 1,
// //                 size: paging.size * paging.blockSize,
// //                 sort: $table.bootstrapTable("getOptions").sortName + "," + $table.bootstrapTable("getOptions").sortOrder,
// //             };
// //             console.log(pageable);
// //
// //             let _filter = tuneDate(filter);
// //             let url = "/" + ctrl + "?" + $.param(_filter, true) + "&" + $.param(pageable, true);
// //             // console.log(url);
// //             // console.log(pageable);
// //             $.ajax({
// //                 url: url
// //             }).done(function (data) {
// //                 logs = data;
// //                 renderDataToTable($table, logs, paging);
// //                 updatePagingNavButtons();
// //             }).fail(function (jqXHR, textStatus, errorThrown) {
// //                 Swal.fire("Error", jqXHR.message, 'warning');
// //             });
// //         } else {
// //             renderDataToTable($table, logs, paging);
// //             updatePagingNavButtons();
// //         }
// //
// //         paging.blockIndex_before = paging.blockIndex;
// //     }
// //
// //     function updatePagingNavButtons() {
// //         let offset = ((paging.no - 1) % paging.blockSize) * paging.size;
// //         $(".btn-page-next").prop("disabled", (logs.length - offset < paging.size));
// //         $(".btn-page-prev").prop("disabled", paging.no === 1)
// //     }
// //
// //     // 테이블 이벤트
// //     movePage(0, true);
// // });