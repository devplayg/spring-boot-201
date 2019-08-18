$(function() {

    let player1 = new CloudPlayerSDK('player1', {
        autohide: 3000,
        useOnlyPlayerFormat: 'html5', // hls force enabled
        debug: false,
    });

    $("#modal-camera-live-video")
        .on("hidden.bs.modal", function () {
            player1.stop();
            $(".to-be-empty", $(this)).empty();
        })
        .on("show.bs.modal", function (e) {
            let $btn = $(e.relatedTarget),
                camera = JSON.parse(decodeURIComponent($btn.data("encoded")));
            $(".camera-name", $(this)).text(camera.name);

            // Play
            player1.setSource(camera.apiKey);
            player1.play();
        })
        .on("shown.bs.modal", function () {
        });


    $("#modal-camera-live-images")
        .on("hidden.bs.modal", function () {
            $(".to-be-empty", $(this)).empty();
            let $carouse = $("#carousel-camera-live-images"),
                $carouselInner = $(".carousel-inner", $carouse),
                $carouselIndicators = $(".carousel-indicators", $carouse);
            $carouse.carousel('pause');
            $carouselInner.children('.item').remove();
        })
        .on("show.bs.modal", function (e) {
        })
        .on("shown.bs.modal", function (e) {
            let $btn = $(e.relatedTarget),
                camera = JSON.parse(decodeURIComponent($btn.data("encoded"))),
                url = "/cameras/" + camera.uid + "/images?size=30",
                $carouse = $("#carousel-camera-live-images"),
                $carouselInner = $(".carousel-inner", $carouse),
                $carouselIndicators = $(".carousel-indicators", $carouse),
                $modalTitle = $(".camera-name", $(this)),
                $timeRangeInfo = $(".time-range-info", $(this));

            $(".camera-live-icon", $(this)).html(getCameraIcon(camera));

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
                                '<span class="live-time-on-image p5">' + convertNvrTimeToUserTime(r.time) + '</span>',
                            '</div>',
                        '</div>'
                    ];
                    $(item.join('')).appendTo($carouselInner);
                });
                $("#carousel-camera-live-images").carousel(0);
            }).fail(function (jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
            });
        });

    $("#modal-camera-urls")
        .on("hidden.bs.modal", function () {
            $(".to-be-empty", $(this)).empty();
        })
        .on("show.bs.modal", function (e) {
        })
        .on("shown.bs.modal", function (e) {
            let $btn = $(e.relatedTarget),
                camera = JSON.parse(decodeURIComponent($btn.data("encoded"))),
                url = "/cameras/" + camera.uid + "/live/urls",
                $modalTitle = $(".camera-name", $(this));

            $.ajax({
                url: url,
            }).done(function (data) {
                $modalTitle.text(camera.name);
                console.log(data);

                $(".camera-url-rtsp").text(data.rtsp);
                $(".camera-url-hls").text(data.hls);
                $(".camera-url-rtmp").text(data.rtmp);
                $(".camera-url-rtc").text(data.rtc);
                $(".camera-url-expire").text(data.expire);
            }).fail(function (jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
            });
        });

    $(".btn-reset").click(function(e) {
        $("#carousel-camera-live-images").carousel(0);
    });

    $(".btn-copy-streaming-url").click(function() {
        let $p = $(this).siblings("p");
        copy($p);
    });


    function convertNvrTimeToUserTime(time) {
        return moment.tz(time.substr(0, 19), "UTC").tz(userTz).format("lll");
    }

    function copy(selector){
        var $temp = $("<div>");
        $("body").append($temp);
        $temp.attr("contenteditable", true)
            .html($(selector).html()).select()
            .on("focus", function() { document.execCommand('selectAll',false,null); })
            .focus();
        document.execCommand("copy");
        $temp.remove();
    }

});