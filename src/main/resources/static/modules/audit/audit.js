$(function() {
    var $table = $("#table-"),
        $form = $("#form-");

    function doSomething( param ) {
        $.ajax({
            method: "GET",
            url: "",
            data: $form.serialize()
        }).done(function (data) {

        }).fail( function ( jqXHR, textStatus ) {

        }).always( function() {

        });
    }
});