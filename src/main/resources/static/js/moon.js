/**
 * 提交对文章的评论
 */

function postCommentController(type, e) {
    if (type === 0) {
        //type=0代表回答问题,type=1代表回答另一个评论
        let parentId = $("#questionId").val();
        let content = $("#content").val();
        let commentator = $("#commentator").val();
        postComment(parentId, content, commentator, type);
    } else if (type === 1) {
        let parentId = e.getAttribute("data-id");
        let content = $("#secondComment-" + parentId).val();
        let commentator = $("#commentator").val();
        postComment(parentId, content, commentator, type);
    } else {
        alert("出错啦！");
    }
}

function postComment(parentId, content, commentator, type) {
    if (!content) {
        alert("不能回复空内容");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "commentator": commentator,
            "parentId": parentId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code === 20000) {
                window.location.reload();
                //$("#content").val("");
            } else {
                if (response.code === 40101) {
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
    if (folding) {
        //折叠二级评论
        comments.removeClass("in");
        //移除二级评论展开状态
        e.removeAttribute("data-folding");
        e.classList.remove("folding-active");
    } else {

        let subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length !== 1) {
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/getComment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    let mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-thumbnail",
                        "src": comment.user.avatarUrl
                    }));

                    let mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h4/>", {
                        "class": "media-heading gray-color",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "style": "margin-left: 15px;margin-top: 15px;",
                        "html": comment.content
                    })).append(($("<div/>", {
                        "class": "gray-color"
                    })).append($("<span/>", {
                        "class": "glyphicon glyphicon-thumbs-up comment-footer",
                    })).append($("<span/>", {
                        "style": "margin-left: 3px;",
                        "html": comment.likeCount
                    })).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD HH:mm')
                    })));

                    let mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    let commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 folding-comments",
                        "style": "style='margin-left: 20px;margin-right: 20px;'"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }

        //展开二级评论
        comments.addClass("in");
        //标记二级评论展开状态
        e.setAttribute("data-folding", "in");
        e.classList.add("folding-active");
    }
}

function showSelectTag() {
    $("#select-tag").show();
}

function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    let strings = previous.split(',');
    if (strings.indexOf(value) === -1) {
        if (previous) {
            $("#tag").val(previous + ',' + value);
        } else {
            $("#tag").val(value);
        }
    }
}