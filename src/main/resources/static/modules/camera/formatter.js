function cameraStateFormatter(enabled, row, idx) {
    if (enabled) {
        return '<span class="text-primary">Active</span>'
    }

    return '<span class="text-danger strong">Offline</span>'
}

function cameraLiveFormatter(val, row, idx) {
    let cameraIcon = getCameraIcon(row),
        imageIcon = getImageIcon(row);

    return cameraIcon + '<span class="mlr5"></span>' + imageIcon;
}

function getCameraIcon(row) {
    return $('<a/>', {
        href: "#",
        "data-toggle": "modal",
        "data-target": "#modal-camera-live-video",
        "data-encoded": encodeURIComponent(JSON.stringify(row)),
    // }).html('<span class="label label-danger"><i class="fa fa-circle"></i> LIVE</span>')[0].outerHTML;
    }).html('<button class="btn btn-danger btn-xs"><i class="fa fa-circle"></i> LIVE</button>')[0].outerHTML;
    // }).html('<button class="btn btn-default btn-xs txt-color-grayDark bg-color-white"><i class="fa fa-circle txt-color-red"></i> LIVE</button>')[0].outerHTML;
}

function getImageIcon(row) {
    return $('<a/>', {
        href: "#",
        "data-toggle": "modal",
        "data-target": "#modal-camera-live-images",
        "data-encoded": encodeURIComponent(JSON.stringify(row)),
    // }).html('<span class="label label-primary">Images</span>')[0].outerHTML;
    }).html('<button class="btn btn-primary btn-xs">Images</button>')[0].outerHTML;
}

function cameraWatchUrlFormatter(val, row, idx){
    return $('<a/>', {
        href: "#",
        "data-toggle": "modal",
        "data-target": "#modal-camera-urls",
        "data-encoded": encodeURIComponent(JSON.stringify(row)),
    }).html("<i class='fa fa-file-text-o'></i>")[0].outerHTML;
}