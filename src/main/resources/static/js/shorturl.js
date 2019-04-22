$(function () {
    var btns = document.querySelectorAll('.btn-copy');
    var clip = new Clipboard(btns);
    clip.on('success', function(e) {
        console.log("Success");
    });
    // 실패시
    clip.on('error', function(e) {
        console.log("Error");
    });

    // shorten
    $("#shorten-btn").on('click', function () {
        var url = $('#link').val();

        if (url == undefined || url =='') {
            $(".result-message").html('<h4>input url</h4>');

        } else {
            var decodeUrl = decodeURIComponent(url);
            $.ajax({
                url: '/api/v1/short-url/get/url',
                dataType: 'json',
                data: {
                    url: decodeUrl
                },
                success: function(cachedUrl){
                    $(".result-message").html('<h4>created shorten url</h4>');

                    var link = "http://localhost:8080/" + cachedUrl.code;
                    var data = ' <div class="col-lg-10 input-group url-input">' +
                        '<input id="link'+ cachedUrl.shortUrl.id +'" name="url" type="text" class="form-control input-lg" value='+link+'> ' +
                         '<button class="btn btn-default btn-lg btn-copy" data-clipboard-target=#link'+cachedUrl.shortUrl.id+' type="button">\n' +
                            '<span class="glyphicon glyphicon-share-alt"  data-clipboard-target="#link'+cachedUrl.shortUrl.id+'"></span>\n' +
                                'Copy\n' +
                            '</button>' +
                        '</div>';

                    $('.cached-url-row').append(data);

                    clip = new Clipboard(document.querySelectorAll('.btn-copy'));

                }, // success
                error: function(e){
                    $(".result-message").html('<h4>'+e+'</h4>');
                }
            });
        }
    });
});