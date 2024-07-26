/**
 * 提交对文章的评论
 */
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

/**
 * 展开二级评论
 */
function foldingComments(e) {
    let id = e.getAttribute("data-id");
    let comments = $("#comment-" + id);

    //判断二级评论展开状态
    let folding = e.getAttribute("data-folding");
    if(folding){
        //折叠二级评论
        comments.removeClass("in");
        //移除二级评论展开状态
        e.removeAttribute("data-folding");
        e.classList.remove("folding-active");
    }else{
        //展开二级评论
        comments.addClass("in");
        //标记二级评论展开状态
        e.setAttribute("data-folding","in");
        e.classList.add("folding-active");
    }


}