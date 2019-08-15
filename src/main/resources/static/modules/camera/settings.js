$(function() {
    $("#table-"+ctrl).bootstrapTable();

    $(".btn-camera-sync").click(function() {
        $.ajax({
            url: '/'+ctrl+'s/sync',
        }).done(function (data) {
            console.log($("#table-"+ctrl));
           $("#table-"+ctrl).bootstrapTable("refresh");
        }).fail(function (jqXHR, textStatus, errorThrown) {
        });
    });

});