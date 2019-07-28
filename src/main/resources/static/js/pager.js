/**
 * Paging library by won
 */

'use strict';

let Pager = function (id, _filter) {

    // Controller ID
    this.id = id;

    // Filter
    this.filter = $.extend({}, _filter);

    // Bootstrap-table
    this.table = null;

    // Filter form
    this.form = null;

    // Queue where data is to be stored (Fast paging only)
    this.log = [];

    // Navigation button elements (Fast paging only)
    this.navigationButtonGroup = {
        page: null,
        prev: null,
        next: null,
    };

    // Paging variables (Fast paging only)
    this.paging = null;


    // Check filtering
    this.isFiltered = function() {
        let isFiltered = false;
        $(".modal-body input[type=text]", this.form).each(function (i, elm) {
            if ($(elm).val().trim().length > 0) {
                isFiltered = true;
                return false;
            }
        });
        return isFiltered;
    }


    // Set sorting options (Fast paging only)
    this.setSort = function (name, order) {
        this.paging.sort = name + "," + order;

        // for Post request
        $("input[name=sort]", this.form).val(this.paging.sort);

        // for Ajax request of bootstrap-table
        this.table.bootstrapTable("refreshOptions", {
            sortName: name,
            sortOrder: order,
        });
    };


    // Fetch page (Fast paging only)
    this.fetchPage = function (direction, refresh) {
        if (direction === undefined) {
            direction = 0;
        }

        if (refresh === undefined) {
            refresh = false;
        }

        // Calculate page
        this.paging.no += direction; // Page to search
        if (this.paging.no < 1) {
            this.paging.no = 1;
        }
        this.paging.blockIndex = Math.floor((this.paging.no - 1) / this.paging.blockSize);

        // Fetch and display data
        if ((this.paging.blockIndex !== this.paging.blockIndex_before) || refresh) {
            let pagingParam = {
                page: Math.ceil(this.paging.no / this.paging.blockSize) - 1,
                size: this.paging.size * this.paging.blockSize,
                sort: this.paging.sort,
            };

            tuneFilterAndPageable(this.filter); // Deep copy and tune
            let url = "/" + ctrl + "?" + $.param(this.filter, true) + "&" + $.param(pagingParam, true);
            console.log("# fetch url: " + url);
            let c = this;
            $.ajax({
                url: url,
            }).done(function (data) {
                c.log = data;
                c.paging.dataLength = c.log.length;
                renderDataToTable(c.table, c.log, c.paging);
                updatePagingNavButtons(c.paging, c.navigationButtonGroup);
                this.navigationButtonGroup.page.text(this.paging.no);
            }).fail(function (jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
                Swal.fire("Error", jqXHR.message, 'warning');
            });
        } else {
            renderDataToTable(this.table, this.log, this.paging);
            updatePagingNavButtons(this.paging, this.navigationButtonGroup);
        }
        this.paging.blockIndex_before = this.paging.blockIndex;
    }


    this.initForm = function () {
        this.form = $("#form-" + id);

        $(".datetime", this.form).datetimepicker(defaultDatetimeOption);

        if (this.isFiltered()) {
            $(".filter", this.form).html('<i class="fa fa-filter txt-color-red"></i>');
        }
    }


    // Initialize paging variables
    this.initPaging = function () {
        this.paging = {
            no: 1, // Page number
            blockIndex: 0, // Block index
            blockIndex_before: -1, // Previous block index
            blockSize: 20, // fetch N pages of data at a time
            dataLength: 0,
            size: this.table.bootstrapTable("getOptions").pageSize, // Page size
            sort: this.table.bootstrapTable("getOptions").sortName + "," + this.table.bootstrapTable("getOptions").sortOrder,
        };
        console.log(this.paging);
    }


    // Initialize navigation button group
    this.initNavigationButtonGroup = function () {
        this.navigationButtonGroup.page = $(".btn-page-text", this.form);
        this.navigationButtonGroup.prev = $(".btn-page-prev", this.form);
        this.navigationButtonGroup.next = $(".btn-page-next", this.form);

        let c = this;
        $(".btn-move-page").click(function () {
            c.fetchPage($(this).data("direction"), false);
        });
    };


    // Initialize bootstrap-table for fast paging
    this.initTableForFastPaging = function () {
        let c = this;
        this.table = $("#table-" + id).bootstrapTable({
            sidePagination: "client", // Client-side pagination

        }).on("column-switch.bs.table", function () {
            // Store the state of the columns
            captureTableColumnsState($(this));

        }).on("refresh.bs.table", function () {
            // Fetch page
            c.fetchPage(0, true);

        }).on("sort.bs.table", function (e, name, order) { // Refresh
            c.setSort(name, order);
            c.fetchPage(0, true);
        });

        // Initialize columns
        restoreTableColumnsState(this.table);
    };


    // Initialize bootstrap-table for normal paging
    this.initTableForNormalPaging = function () {
        let c = this;
        this.table = $("#table-" + id).bootstrapTable({
            sidePagination: "server",
            url: "/" + this.id,
            queryParamsType: "", // DO NOT REMOVE. LEAVE BLANK
            pagination: true,
            queryParams: function (normalPagingParam) {
                tuneFilterAndPageable(c.filter, normalPagingParam);
                return $.param(c.filter, true);
            },
            responseHandler: function (data) {
                console.log(data);
                if (c.filter.fastPaging) {
                    return data;
                }
                return {
                    total: data.totalElements,
                    rows: data.content
                };
            }
        }).on("column-switch.bs.table", function () {
            captureTableColumnsState($(this));
        }).on("sort.bs.table", function (e, name, order) { // Refresh
            // for Post request
            $("input[name=sort]", c.form).val(name + "," + order);
        });

        // Initialize columns
        restoreTableColumnsState(this.table);

    };

    // Initialize
    this.init = function () {

        // Fast paging
        if (this.filter.fastPaging) {
            this.initTableForFastPaging();
            this.initForm();
            this.initPaging();
            this.initNavigationButtonGroup();

            return true;
        }

        // Normal paging
        this.initTableForNormalPaging();
        this.initForm();
    }

    this.init();

    if (this.filter.fastPaging) {
        this.fetchPage();
    }
};
