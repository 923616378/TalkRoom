$(function () {
    //全局websocket对象
    var websocket = null;
    //全局用户变量,存储用户和好友信息和发送的消息
    var user = {};
    //全局聊天对象, 初始状态聊天对象为自己
    var talkObj = {
        uid: -1, //当用户登录成功时,被赋值为当前用户uid
        online: true, //当前聊天对象默认在线
        index: -1 //当前聊天对象在user.friends数组的索引号
    };
    //消息对象 消息类型,消息,接受者id
    //消息类型为1时, 代表这是发送给用户的信息
    function Message(msgType,message,receiverId) {
        this.senderId = user.uid, //发送者uid
        this.msgType = msgType,
        this.message = message,
        this.receiverId = receiverId
    }
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
                talkObj.uid = user.uid;
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
            var li = $("<li class='friend offline' index=" + 0 + "><img src='img/头像.jpg' class='friendImg'><div class='friendMsg'>" + ele.username + "</div><div class='mask'></div></li>");
            //2.添加到friend栏
            $(".Friends").append(li);
        })
    })();
    function loadMsg(){};
    //给好友栏绑定点击事件,通过事件委托的方式
    $(".Friends").on("click",".friend",function(){
        //更改当前li的id
        talkObj.index = $(this).index();
        console.log(talkObj);
        //1. 当点击还有栏时,清空消息栏里面的所有li
            $(".messages").empty();
        //2. 加载当前用户的所有聊天数据
            loadMsg()
    })
    //给发送按钮绑定点击事件
    $("#msgSendBtn").on("click",function(){
        //获取当前点击li的id
        var index =  talkObj.index;
        console.log(index);
        console.log(user.friends);
        talkObj.uid = user.friends[index].uid;

        //获取文本域消息
        var sendMsg = $("#message-input").val();
        console.log("索引id:"+index," uid:"+talkObj.uid," 待发送消息:"+sendMsg)
        //如果消息不为控才能发送,弹出消息不能为空的提示
        if(sendMsg != ""){
            //封装成消息对象
            var msg = new Message(1,sendMsg,talkObj.uid);
            console.log(msg);
            //使用websocket发送, 转化为JSON对象
            console.log("json格式:"+JSON.stringify(msg));
            websocket.send(JSON.stringify(msg));
        }else{

        }
       

    })
    //获取webSocket 判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://127.0.0.1:8080/TalkRoom/websocket");
        console.log("link success")
    } else {
        alert('Not support websocket')
    }
    //连接发生错误的回调方法
    websocket.onerror = function () {
        // alert("error");
    };
    //连接成功建立的回调方法
    websocket.onopen = function (event) {
        console.log("服务器socket连接成功!")
    }
    //接收到消息的回调方法
    websocket.onmessage = function received(event) {
        console.log(event.data);
        //将数据转换为对象
        var message = JSON.parse(event.data);
    }
    //连接关闭的回调方法
    websocket.onclose = function () {
        // alert("close");
    }
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        websocket.close();
    }


}) 
