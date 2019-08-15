$(function() {

    let player1 = new CloudPlayerSDK('player1', {
        autohide: 3000,
        useOnlyPlayerFormat: 'html5', // hls force enabled
    });

    $("#modal-camera-live-video")
        .on("hidden.bs.modal", function () {
            player1.stop();
        })
        .on("show.bs.modal", function (e) {
            let $btn = $(e.relatedTarget),
                camera = JSON.parse(decodeURIComponent($btn.data("encoded")));
            console.log(camera);
            player1.setSource(camera.apiKey);
            player1.play();
        })
        .on("shown.bs.modal", function () {
        });

    $("#modal-camera-live-images")
        .on("hidden.bs.modal", function () {
            $(".carousel-indicators", $(this)).empty();
            $(".carousel-inner", $(this)).empty();
            $(".modal-title", $(this)).empty();
            $(".time-range-info", $(this)).empty();
        })
        .on("show.bs.modal", function (e) {
            let $btn = $(e.relatedTarget),
                camera = JSON.parse(decodeURIComponent($btn.data("encoded"))),
                url = "/cameras/" + camera.uid + "/images",
                $carouselIndicators = $(".carousel-indicators", $(this)),
                $carouselInner = $(".carousel-inner", $(this)),
                $modalTitle = $(".modal-title", $(this)),
                $timeRangeInfo = $(".time-range-info", $(this));

            $.ajax({
                url: url,
            }).done(function (data) {
                if (data.objects.length < 1) {
                    return;
                }

                $modalTitle.text(camera.name);
                $timeRangeInfo.text(
                    convertNvrTimeToUserTime(data.objects[data.objects.length-1].time)
                    + " ~ "
                    + convertNvrTimeToUserTime(data.objects[0].time)

                );

                $.each(data.objects, function(i, r) {
                    let active = (i === 0) ? "active" : "";

                    // Indicators
                    $("<li />", {
                        "data-target": "#carousel-camera-live-images",
                        "data-slide-to": i,
                        class: active,
                    }).appendTo($carouselIndicators);

                    // Item
                    let item = [
                        '<div class="item ' + active + '">',
                            '<img src="' + r.url + '">',
                            '<div class="carousel-caption">',
                                '<h1>' + convertNvrTimeToUserTime(r.time) + '</h1>',
                                '<h3>' + camera.name + '</h3>',
                                '',
                            '</div>',
                        '</div>'
                    ];
                    $(item.join('')).appendTo($carouselInner);
                });
            }).fail(function (jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
            });
        })
        .on("shown.bs.modal", function () {
        });

    function convertNvrTimeToUserTime(time) {
        return moment.tz(time.substr(0, 19), "UTC").tz(userTz).format("lll");
    }

});