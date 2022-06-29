$(function(){ //页面加载完毕后执行
    /**
 *  用户名 3-9个字符,包含数字字母
 *  密码 3-10个字符, 包含字母数字符号, 不能有中文
 */
//表单验证函数 验证用户名
function verifyUserName() { // 获取用户名是否符合格式
    var usrReg = new RegExp("^[0-9a-zA-Z]{3,9}$");
    //获取账号
    var username = $("#name").val();
    //判断是否符合规则,并返回结果
    return usrReg.test(username);
}
//发送Ajax请求 get, 判断用户名是否已经存在
function isUserNameExist() {
    // 必须同步, 这样才能获取到数据
    var isExist = false;
    $.ajax({
        //url
        url: "/TalkRoom/user/userIsExist",
        //方式
        type: "GET",
        //转化json
        dataType: "json", //自动将数据转换为json
        //关闭异步请求!
        async: false,
        //请求数据
        data: "username=" + $("#name").val(),
        //成功访问
        success: function (data) {
            isExist = data.isExist;
        },
        //访问失败时的回调函数
        error: function () {
            console.log("请求出错!");
            isExist = null;
        }
    });
    return isExist;
}
//表单验证函数 验证密码1是否符合规则
function verifyPwd() {
    //校验密码
    var pwdReg = new RegExp("^[0-9a-zA-Z]{3,12}$");
    var password = $("#pwd").val();
    var pwdResult = pwdReg.test(password);
    return pwdResult;
}
//表单验证函数 验证密码2是否等于密码1
function verifyPwdIsSame() {
    return $("#pwd").val() === $("#pwd2").val();
}
//绑定输入框失去焦点判断
$("#pwd").on("blur", function () {
    if (verifyPwd()) {
        //密码格式正确
        $("#pwd").siblings(".tip").text("密码格式正确!").css("color", "green");
    } else {
        //设置密码不符合规则提示
        $("#pwd").siblings(".tip").text("密码不符合规则!").css("color", "red");
    }
    //调用输入框2的失去焦点时间
    if ($("#pwd2").val() != "") {
        $("#pwd2").blur();
    }
})
//绑定密码输入框2和1的失去焦点判断
$("#pwd2").on("blur", function () {
    if (verifyPwdIsSame()) {
        //密码格式正确
        $("#pwd2").siblings(".tip").text("两次密码相等!").css("color", "green");
    } else {
        //设置密码不相等提示
        $("#pwd2").siblings(".tip").text("两次密码不相等!").css("color", "red");
    }
});
//用户名输入框失去焦点判断
$("#name").on("blur", function () {
    //验证用户名是否符合规则和是否已经存在
    if (verifyUserName()) //验证通过
    {
        //验证用户名是否存在
        var isExist = isUserNameExist();
        if (isExist) {
            //存在,弹出提示框
            $("#name").siblings(".tip").text("用户名已经存在!").css("color", "red");
        } else if (isExist != null) { //用不存在并且符合规则,而且网络没有出错
            //将提示框内容清空
            $("#name").siblings(".tip").text("用户名可以使用!").css("color", "green");
        }
    } else {
        //不符合规则弹出提示框
        //存在,弹出提示框
        $("#name").siblings(".tip").text("用户名不符合规则!").css("color", "red");
    }
})
//给注册按钮绑定点击按钮
$(".register button").on("click", function (event) {
    //阻止表单中按钮的默认行为,比如说点击后会自动刷新页面
    event.preventDefault();
    //验证是否满足条件
    if (verifyUserName() && verifyPwd() && verifyPwdIsSame() && !isUserNameExist()) //如果都满足条件,发送注册请求
    {
        //发送Ajax请求注册账号
        $.ajax({
            //url
            url: "/TalkRoom/user/userRegister",
            //方式
            type: "POST",
            //请求数据
            data: {
                username: $("#name").val(),
                password: $("#pwd").val(),
            },
            //成功发送注册请求
            success: function (data) {
                if (data === "true") {
                    //弹出三秒后跳转到登录页面提醒
                    alert("注册成功! 三秒后跳转到登录界面!")
                    setTimeout(function () {
                        window.location = "http://127.0.0.1:8080/TalkRoom/pages/login.html";
                    }, 3000);
                } else {
                    alert("未知原因注册失败!")
                }
            },
            //访问失败时的回调函数
            error: function () {
                console.log("注册请求出错!");
            }
        })
    }
})
})