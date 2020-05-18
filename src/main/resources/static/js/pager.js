/**
 * Paging library by won
 */

'use strict';
let Pager = function (pager) {

    // Controller ID
    this.id = pager.id;

    // Form
    this.form = null;

    // Modal search filter
    this.modal = null;

    // Bootstrap table
    this.table = null;

    // Filter
    this.filter = null;

    // URL to request
    this.url = null;

    // Data queue (fast-paging-only)
    this.log = null;

    // Navigation button elements (fast-paging-only)
    this.navigationButtonGroup = {
        page: null,
        prev: null,
        next: null,
    };

    // Paging variables (fast-paging-only)
    this.paging = null;

    // Previous paging mode
    this.pagingMode = null;

    // Extra parameter
    this.extraParam = null;

    // Show loading
    this.showLoading = null;

    this.isInModal = null;

    this.paginationVAlign = null;

    // Check filtering in modal
    this.isFilteredInModal = function() {
        let filtered = false;

        // Check text boxes
        $(".modal-body input[type=text]", this.form).not(".exception").each(function (i, elm) {
            if ($(elm).val().trim().length > 0) {
                filtered = true;
                return false;
            }
        });

        // Check select box
        $(".modal-body select", this.form).each(function (i, elm) {
            if ($("option:selected", $(elm)).length > 0) {
                filtered = true;
                return false;
            }
        });

        // console.debug(" - filtered in modal? " + filtered);
        return filtered;
    };


    // Set sorting options (fast-paging-only)
    this.setSort = function (name, order) {
        this.paging.sort = name + "," + order;

        // for Ajax request of bootstrap-table
        this.table.bootstrapTable("refreshOptions", {
            sortName: name,
            sortOrder: order,
        });

        // console.debug("sorting is changed to " + name + " / " + order);
    };

    this.setExtraParam = function(param) {
        this.extraParam = param;
    };

    // Refresh table (General paging only)
    this.refreshTable = function() {
        this.table.bootstrapTable("refresh");
    };

    // Fetch page (fast-paging-only)
    this.fetchPage = function (direction, refresh) {
        if (direction === undefined) {
            direction = 0;
        }

        if (refresh === undefined) {
            refresh = false;
        }
        // console.debug("move page: direction=" + direction + ", refresh=" + refresh);

        // Calculate page
        this.paging.no += direction; // Page to search
        if (this.paging.no < 1) {
            this.paging.no = 1;
        }
        this.paging.blockIndex = Math.floor((this.paging.no - 1) / this.paging.blockSize);
        // console.debug(this.paging);

        // Fetch and display data
        if ((this.paging.blockIndex !== this.paging.blockIndex_before) || refresh) {
            // console.debug(" - need to fetch data");
            let pagingParam = {
                page: Math.ceil(this.paging.no / this.paging.blockSize) - 1,
                size: this.paging.size * this.paging.blockSize,
                sort: this.paging.sort,
            };
            // console.debug(pagingParam);

            let url = "/" + this.api + "?" + $.param(this.filter, true) + "&" + $.param(pagingParam, true);
            if (this.extraParam !== null) {
                url += "&" + $.param(this.extraParam, true);
            }
            let c = this;

            // Show loading
            if (this.showLoading) {
                c.table.bootstrapTable("showLoading");
            }
            $.ajax({
                url: url,
            }).done(function (data) {
                c.log = data;

                // Set row number
                $.each(c.log, function(i) {
                    c.log[i]["__ROWNUM__"] = (c.paging.blockIndex * c.paging.size * c.paging.blockSize) + i + 1;
                });

                c.paging.dataLength = c.log.length;
                c.renderDataToTable(c.log);
                c.updatePagingNavButtons();
                c.navigationButtonGroup.page.text(c.paging.no);
            }).fail(function (jqXHR, textStatus, errorThrown) {
                console.error(jqXHR);
                Swal.fire("Error", jqXHR.message, "warning");
            }).always(function() {
                // Hide loading
                if (c.showLoading) {
                    c.table.bootstrapTable("hideLoading");
                }
            });
        } else {
            this.renderDataToTable(this.log);
            this.updatePagingNavButtons();
            this.navigationButtonGroup.page.text(this.paging.no);
        }
        this.paging.blockIndex_before = this.paging.blockIndex;
    };


    // Render data to table (fast-paging-only)
    this.renderDataToTable = function(logs) {
        let begin = ((this.paging.no - 1) % this.paging.blockSize) * this.paging.size,
            end = begin + this.paging.size;
        this.table.bootstrapTable("load", logs.slice(begin, end));

        // console.debug("render data to table: queueSize="+logs.length + ", begin="+begin+", end="+end);
        $("[rel=tooltip]").tooltip();
    };


    this.initForm = function () {

        // Set form
        this.form = $("#form-" + this.id);

        // Set modal of filter
        this.isInModal = false;
        if (pager.isInModal !== undefined){
            this.isInModal = pager.isInModal;
        }
        if (this.isInModal) {
            this.modal = $("#modal-" + this.id);
            // console.debug("pager is in modal");
        } else {
            this.modal = $("#modal-" + this.id + "-filter");
        }

        // Set datetime input
        $(".datetime", this.form).datetimepicker(defaultDatetimeOption);

        // Set URL to request
        this.api = this.id;
        if (pager.api !== undefined) {
            this.api = pager.api;
        }

        // Set loading
        this.showLoading = true;
        if (pager.showLoading !== undefined) {
            this.showLoading = pager.showLoading;
        }

        this.paginationVAlign = "both";
        if (pager.paginationVAlign !== undefined) {
            this.paginationVAlign = pager.paginationVAlign;
        }

        // Update filter
        this.updateFilter();

        // Set paging mode
        this.pagingMode = this.filter.pagingMode;
        // console.debug("set paging mode: " + this.pagingMode);
    };

    this.initFormValidation = function() {

        // Set validation rules of form
        let rules =  {
            pageSize: {
                required: true,
                min: 5,
                max: 200,
            }
        };
        if (pager.rules !== undefined) {
            Object.assign(rules, pager.rules);
        }

        let c = this;
        this.form.validate({
            submitHandler: function (form, e) {
                e.preventDefault();

                // Update filter
                c.updateFilter();

                if (c.pagingMode !== c.filter.pagingMode) {
                    form.submit();
                    return true;
                }
                // console.debug("old paging mode: " + c.pagingMode);
                // console.debug("--------submitted--------");

                // if modal is opened, close it
                if (c.modal.hasClass("in")) {
                    c.modal.modal("toggle");
                }

                if (c.filter.pagingMode === PagingMode.FastPaging) {
                    c.table.bootstrapTable("refreshOptions", {
                        pageSize: c.filter.pageSize,
                    });
                    c.initPaging();
                    c.fetchPage(0, true);
                    return true;
                }

                c.table.bootstrapTable("selectPage", 1);
            },
            rules: rules
        });
    };


    // Update filter
    this.updateFilter = function() {
        this.filter = objectifyForm(this.form);
        this.filter.pagingMode = this.filter.pagingMode || PagingMode.FastPaging;
        // console.debug("filter is updated: pagingMode="+ this.filter.pagingMode);
        // console.debug(this.filter);

        if (this.isFilteredInModal()) {
            $(".filter", this.form).html('<i class="fa fa-filter txt-color-red"></i>');
        }
    };


    // Initialize paging variables (fast-paging-only)
    this.initPaging = function () {
        this.paging = {
            no: 1, // Page number
            blockIndex: 0, // Block index
            blockIndex_before: -1, // Previous block index
            blockSize: pager.blockSize || 20, // fetch N pages of data at a time
            dataLength: 0,
            size: parseInt(this.table.bootstrapTable("getOptions").pageSize, 10), // Page size
            sort: this.table.bootstrapTable("getOptions").sortName + "," + this.table.bootstrapTable("getOptions").sortOrder,
        };
        // console.debug("paging is initialized");
    }


    // Initialize navigation button group (fast-paging-only)
    this.initNavigationButtonGroup = function () {
        this.navigationButtonGroup.page = $(".btn-page-text", this.form);
        this.navigationButtonGroup.prev = $(".btn-page-prev", this.form);
        this.navigationButtonGroup.next = $(".btn-page-next", this.form);

        let c = this;
        $(".btn-move-page", this.form).click(function () {
            c.fetchPage($(this).data("direction"), false);
        });
    };


    // Initialize bootstrap-table for fast paging
    this.initTableForFastPaging = function () {
        let c = this;

        this.table = $("#table-" + this.id).bootstrapTable({
            sidePagination: "client", // Client-side pagination
            pageSize: c.filter.pageSize,

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
        // console.debug("table is initialized for fast paging");

        // Initialize paging
        this.initPaging();
    };


    // Initialize bootstrap-table for general paging
    this.initTableForGeneralPaging = function () {
        let c = this;
        this.table = $("#table-" + this.id).bootstrapTable({
            sidePagination: "server",
            url: "/" + this.api,
            queryParamsType: "", // DO NOT REMOVE. LEAVE BLANK
            pagination: true,
            paginationVAlign: c.paginationVAlign,
            queryParams: function (param) {
                let filter =  $.extend({}, c.filter);
                Object.assign(filter, {
                    size: param.pageSize,
                    page: param.pageNumber - 1,
                    sort: param.sortName + "," + param.sortOrder,
                });
                return $.param(filter, true);
            },
            responseHandler: function (data) {
                return {
                    total: data.totalElements,
                    rows: data.content
                };
            }
        }).on("column-switch.bs.table", function () {
            captureTableColumnsState($(this));

        }).on("load-success.bs.table", function () {
            $("[rel=tooltip]").tooltip();

        }).on("sort.bs.table", function (e, name, order) { // Refresh
            // for Post request
            //$("input[name=sort]", c.form).val(name + "," + order);
        });
    };

    this.updatePagingNavButtons = function() {
        let offset = ((this.paging.no - 1) % this.paging.blockSize) * this.paging.size;
        this.navigationButtonGroup.prev.prop("disabled", this.paging.no === 1)
        this.navigationButtonGroup.next.prop("disabled", (this.paging.dataLength - offset) < this.paging.size);
    };


    // Initialize module
    this.init = function () {
        // console.debug("##### Pager is initializing: " + pager.id + " #####");

        // Initialize form and validation
        this.initForm();
        this.initFormValidation();

        // Fast paging mode
        if (this.filter.pagingMode === PagingMode.FastPaging) {
            this.initTableForFastPaging();
            this.initNavigationButtonGroup();
            restoreTableColumnsState(this.table);
            return true;
        }

        // General paging mode
        this.initTableForGeneralPaging();

        // Initialize table columns
        restoreTableColumnsState(this.table);
    };

    this.run = function() {
        // console.debug("run pager");
        this.updateFilter();

        if (this.filter.pagingMode === PagingMode.FastPaging) {
            this.fetchPage();
        }
    };

    this.reset = function() {
        this.initPaging();
        this.log = [];
        this.table.bootstrapTable("removeAll");
        this.extraParam = null;
    };

    this.init();
};