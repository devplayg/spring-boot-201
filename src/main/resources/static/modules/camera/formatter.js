function cameraStateFormatter(enabled, row, idx) {
    if (enabled) {
        return '<span class="text-primary">Active</span>'
    }

    return '<span class="text-danger strong">Offline</span>'
}

function cameraLiveFormatter(val, row, idx) {
    let videoLink = $('<a/>', {
        href: "#",
        "data-toggle": "modal",
        "data-target": "#modal-camera-live-video",
        "data-encoded": encodeURIComponent(JSON.stringify(row)),
    }).html("<i class='fa fa-video-camera'></i>")[0].outerHTML;

    let imageLink = $('<a/>', {
        href: "#",
        "data-toggle": "modal",
        "data-target": "#modal-camera-live-images",
        "data-encoded": encodeURIComponent(JSON.stringify(row)),
    }).html("<i class='fa fa-image'></i>")[0].outerHTML;

    return videoLink + '<span class="mlr5"></span>' + imageLink;
}

function cameraWatchUrlFormatter(val, row, idx){
    return $('<a/>', {
        href: "#",
        "data-toggle": "modal",
        "data-target": "#modal-camera-urls",
        "data-encoded": encodeURIComponent(JSON.stringify(row)),
    }).html("<i class='fa fa-file-text-o'></i>")[0].outerHTML;
}