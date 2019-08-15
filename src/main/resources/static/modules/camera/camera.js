$(function() {
    console.log(3);

    $(".btn-camera-snapshot").click(function() {
        let $btn = $(this),
            uid = $btn.data("uid");

        $.ajax({
            url: '/'+ctrl+'s/' + uid + '/snapshot',
        }).done(function (data) {
            $("#snapshot-" + uid).attr("src", data.url);
        }).fail(function (jqXHR, textStatus, errorThrown) {
        });
    });

    $(".btn-camera-image").click(function() {
        let $btn = $(this),
            uid = $btn.data("uid");

        $.ajax({
            url: '/'+ctrl+'s/' + uid + '/images',
        }).done(function (data) {
            // console.log(data);
            // $("#snapshot-" + uid).attr("src", data.url);
        }).fail(function (jqXHR, textStatus, errorThrown) {
        });
    });

});