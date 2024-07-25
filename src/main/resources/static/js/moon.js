function postComment() {
    var questionId = $("#questionId").val();
    var content = $("#content").val();
    let commentator = $("#commentator").val();
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "commentator": commentator,
            "parentId": questionId,
            "content": content,
            "type": 0
        }),
        success: function (response) {
            if(response.code==200){
                $("#comment_section").hide();
            }else{
                alert(response.message());
            }
            console.log(response);
        },
        dataType: "JSON"
    });
}