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
    function Message(msgType, message, receiverId) {
        this.senderId = user.uid, //发送者uid
            this.msgType = msgType,
            this.message = message,
            this.receiverId = receiverId
    }
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
        //判断消息类型
        switch (message.msgType) {
            case 1: //消息类型为用户发送给用户的信息
                {
                    //获取发送者id
                    var senderId = message.senderId;
                    //根据发送id获取friends的索引
                    var friendIndex = null;
                    for (var i = 0; i < user.friends.length; i++) {
                        if (user.friends[i].uid === senderId) {
                            friendIndex = i;
                        }
                    }
                    //将message存储到friends数组中
                    user.friends[friendIndex].messageArrary.push(message);
                    //重新渲染聊天窗口函数
                    loadPage(friendIndex)
                }
                break;
            case 2: //消息类型为服务器发送的 确认收到消息
                {
                    //添加新的message到messageArrary数组
                    //获取接受者id
                    var receiverId = message.receiverId;
                    //根据发送id获取friends的索引
                    var friendIndex = null;
                    for (var i = 0; i < user.friends.length; i++) {
                        if (user.friends[i].uid === receiverId) {
                            friendIndex = i;
                        }
                    }
                    //将message存储到friends数组中
                    user.friends[friendIndex].messageArrary.push(message);
                    //重新渲染聊天窗口函数
                    loadPage(friendIndex)
                }
                break;
            case 3: //消息类型为上线用户uid, 如果该id是用户的好友,设置好友的样式
                {
                    var onlineUid = message.onlineUid;
                    //遍历用户好友中是否存在该id
                    for (var i = 0; i < user.friends.length; i++) {
                        if (user.friends[i].uid === onlineUid) {
                            //设置好友栏样式, 设置背景颜色为绿色,且移除类名
                            $(".friend").eq(i).css("backgroundColor", "green").removeClass("offline");;
                            break;
                        }
                    }
                }
                break;
            case 4: //消息类型为下线用户uid, 如果该id是用户的好友,设置好友的样式
                {
                    var offlineUid = message.offlineUid;
                    //遍历用户好友中是否存在该id
                    for (var i = 0; i < user.friends.length; i++) {
                        if (user.friends[i].uid === offlineUid) {
                            //设置好友栏样式, 设置背景颜色为白色,且添加类名
                            $(".friend").eq(i).css("backgroundColor", "white").addClass("offline");;
                            break;
                        }
                    }
                }
                break;
            case 5: //消息类型为5 获取所有已经上线好友的uid, 是一个数组
                {
                    var lineStatus = message.lineStatus;
                    //遍历数组
                    $.each(lineStatus,function(i,ele){
                        //如果ele为true,就设置对应索引的好友为上线
                        if(ele)
                        {
                             //设置好友栏样式, 设置背景颜色为绿色,且移除类名
                             $(".friend").eq(i).css("backgroundColor", "green").removeClass("offline");;
                        }
                    })
                }
                break;
            case 6: //消息类型为6 服务器返回登录准备工作完成
                {
                    //向服务器websocekt管道发送获取上线好友
                    websocket.send(JSON.stringify({
                        senderId: user.uid,
                        msgType: 2, //获取所有在线好友uid
                        message: "", //传递空字符串即可
                        receiverId: -1 //目前的想法,服务器并不需要这个
                    }));
                }
                break;
        }

    }
    //连接关闭的回调方法
    websocket.onclose = function () {
        // alert("close");
    }
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        websocket.close();
    }
    //插入字符串
    function insertStr(source, start, newStr) {
        return source.slice(0, start) + newStr + source.slice(start);
    }
    //判断消息中,数字和字母和汉字的个数
    function msgHandle(str) {
        var str2 = str;
        var OtherNum = 0;
        var chineseNum = 0 //汉字个数
        var brNum = 0; //插入的换行符个数
        var reg = new RegExp("[\\u4E00-\\u9FFF]");
        for (var i = 0; i < str.length; i++) {
            console.log(str[i]);
            if (reg.test(str[i])) {
                chineseNum++
            } else {
                OtherNum++
            }
            if (OtherNum * 12 + chineseNum * 20 >= 680) {
                //在字符串中插入换行符
                str2 = insertStr(str2, brNum * 4 + i + 1, "<br>")
                OtherNum = 0;
                chineseNum = 0;
                brNum++;
            }
        }
        return str2;
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
                //给每个好友添加消息数组
                $.each(user.friends, function (i, ele) {
                    ele.messageArrary = [];
                })
                console.log(user);
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

    //重新渲染页面
    function loadPage(index) {
        //1. 当点击好友栏时,清空消息栏里面的所有li
        $(".messages").empty();
        //2. 加载当前用户的所有聊天数据
        $.each(user.friends[index].messageArrary, function (i, ele) {
            //提取时间
            var msgSendTime = new Date(ele.sendTime);
            //拼接成指定格式的字符串 2022/7/1 12:10:33 月份是11进制的,真实的月份要加1
            var timeStr = msgSendTime.getFullYear() + "/" + (msgSendTime.getMonth() + 1) + "/" + msgSendTime.getDate() + " "
                + msgSendTime.getHours() + ":" + msgSendTime.getMinutes() + ":" + msgSendTime.getSeconds();
            console.log(timeStr);
            //获取头像url
            var headImgUrl = "img/头像.jpg"
            //判断用户是发送者还是接受者
            var who = user.uid === ele.senderId ? "myself" : "him";
            //创建li
            var li = $("<li class='msg " + who + "'><div class='now-time'>" + timeStr + "</div>" +
                "<img src=" + headImgUrl + " class='headImg'>" +
                "<div class='message'>" + ele.message + "</div></li>");
            //将li添加到
            $(".messages").append(li);
            //设置滚动条到最底部
            var messages = $(".messages")[0];
            messages.scrollTop = messages.scrollHeight;

        })
    }
    //给好友栏绑定点击事件,通过事件委托的方式
    $(".Friends").on("click", ".friend", function () {
        //更改li的背景颜色,其他变为白色
        $(this).css("backgroundColor", "skyblue").siblings(".friend").css("backgroundColor", "white");
        //更改当前li的id
        talkObj.index = $(this).index();
        console.log(talkObj);
        //发送ajax请求获取历史聊天消息
        $.ajax(
            {
                //url
                url: "/TalkRoom/message/getAllMessagesByUidAndFriendId",
                //方式为get
                type: "GET",
                //data
                data: {
                    uid: user.uid, //用户id
                    friendId: user.friends[talkObj.index].uid //朋友id
                },
                //成功时方法
                success: function (data) {
                    // 将数据赋值给消息数组
                    user.friends[talkObj.index].messageArrary = data;
                    //重新渲染聊天信息窗口
                    loadPage(talkObj.index)
                },
                //失败时方法
                error: function () {
                    console.log("获取朋友聊天信息失败")
                }
            }
        )
    })
    //给发送按钮绑定点击事件
    $("#msgSendBtn").on("click", function () {
        //获取当前点击li的id
        var index = talkObj.index;
        console.log(index);
        console.log(user.friends);
        talkObj.uid = user.friends[index].uid;

        //获取文本域消息
        var sendMsg = $("#message-input").val();
        //处理字符串,在适当位置插入换行符
        sendMsg = msgHandle(sendMsg);
        console.log("索引id:" + index, " uid:" + talkObj.uid, " 待发送消息:" + sendMsg)
        //如果消息不为空才能发送,弹出消息不能为空的提示
        if (sendMsg != "") {
            //封装成消息对象
            var msg = new Message(1, sendMsg, talkObj.uid);
            console.log(msg);
            //使用websocket发送, 转化为JSON对象
            console.log("json格式:" + JSON.stringify(msg));
            websocket.send(JSON.stringify(msg));
        } else {

        }
        //清空输入文本域
        $("#message-input").val("");
    })

}) 
