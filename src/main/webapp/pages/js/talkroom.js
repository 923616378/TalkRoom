$(function () {
    //全局用户变量,存储用户和好友信息
    var user = {};
    //立刻执行函数
    (function load() {
        //发送ajax请求获取当前登录用户
        $.ajax({
            //url
            url: "/TalkRoom/user/getCurrentUser",
            //方式
            type: "GET",
            //自动转化json
            dataType: "json", //自动将数据转换为json
            //同步
            async: false,
            //成功执行的方法
            success: function (data) {
                user = data;
            },
            //失败方法
            error: function () {
                console.log("获取用户信息失败!");
            }
        })
        //将里面的用户名设置到页面的userMsg
        $("#userMsg").text(user.username);
        //遍历friends,将其添加到好友栏
        $.each(user.friends, function (i, ele) {
            //1.创建li标签 
            var li = $("<li class='friend offline' index=" + 0 + "><img src='img/头像.jpg' class='friendImg'><div class='friendMsg'>"+ele.username+"</div><div class='mask'></div></li>");
            //2.添加到friend栏
            $(".Friends").append(li);
        })
    })();

})