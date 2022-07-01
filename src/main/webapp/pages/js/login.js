$(function () {
    function setCookie(cname, cvalue, exdays) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        var expires = "expires=" + d.toGMTString();
        document.cookie = cname + "=" + cvalue + "; " + expires;
    }
    function getCookie(cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i].trim();
            if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
        }
        return "";
    }
    function load() {
        //判断是否存在cookie
        var user = getCookie("user");
        if (user) {
            user = JSON.parse(user);
            $("#name").val(user.username);
            $("#pwd").val(user.password);
            //设置记住密码为选中
            $("#remember").prop("checked", true);
        }
    }
    //加载
    load();
    //登录按钮绑定点击事件
    $(".loginBtn").on("click", function () {
        //阻止表单中按钮的默认行为,比如说点击后会自动刷新页面
        event.preventDefault();
        //发送Ajax请求
        $.ajax({
            //url
            url: "/TalkRoom/user/userLogin",
            //json转换
            dataType: "json", //自动将数据转换为json
            //同步
            async: false,
            //方式
            type: "POST",
            //请求体数据
            data: {
                username: $("#name").val(),
                password: $("#pwd").val(),
            },
            //请求成功
            success: function (isLoginSuccess) {
                if (isLoginSuccess) //如果为true,则登录成功
                {
                    //如果记住了密码 保存账号密码30天
                    if ($("#remember").prop("checked") == true) {
                        //设置一个cookie
                        setCookie("user", JSON.stringify({
                            username: $("#name").val(),
                            password: $("#pwd").val()
                        }), 30);
                    }
                    //跳转到聊天室页面
                    window.location = "http://127.0.0.1:8080/TalkRoom/pages/talkroom.html"
                }
            },
            //请求失败
            error: function () {
                console.log("网络请求错误!")
            }
        })
    })
})