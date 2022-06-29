$(function () {
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
            //方式
            type: "POST",
            //请求体数据
            data: {
                username: $("#name").val(),
                password: $("#pwd").val()
            },
            //请求成功
            success: function (data) {
                console.log(data);
                data ? console.log("登陆成功") : console.log("登录失败");
            },
            //请求失败
            error: function () {
                console.log("网络请求错误!")

            }
        })
    })
})