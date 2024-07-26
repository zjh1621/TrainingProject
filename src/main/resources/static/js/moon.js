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
            if (response.code == 20000) {
                $("#content").val("");
            } else {
                if (response.code == 40101) {
                    let isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=Ov23li8yntZn5bSXzZH2&redirect_uri=http://localhost:8887/callback&scope=user&state=123")
                    }
                } else {
                    alert(response.message);
                }
            }
            console.log(response);
        },
        dataType: "JSON"
    });
}