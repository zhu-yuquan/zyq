<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>注册</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="../layui/css/layui.css" media="all">
    <link rel="stylesheet" href="../layui/admin.css" media="all">
    <link rel="stylesheet" href="../layui/login.css" media="all">
</head>
<body>

<form action="/login/register" method="post">

<div class="layadmin-user-login layadmin-user-display-show" id="LAY-user-login" style="display: none;">
    <div class="layadmin-user-login-main">
        <div class="layadmin-user-login-box layadmin-user-login-header">
            <h2>行者笔记</h2>
            <p>个人领域，绝对隐私，公共区域待开发</p>
            <p style="color: #2F4056;">${message?if_exists}</p>
        </div>
        <div class="layadmin-user-login-box layadmin-user-login-body layui-form">
            <div class="layui-form-item">
                <label class="layadmin-user-login-icon layui-icon layui-icon-email" for="LAY-user-login-email"></label>
                <input type="text" name="email" id="LAY-user-login-cellphone" lay-verify="email" placeholder="邮箱" class="layui-input">
            </div>
            <div class="layui-form-item">
                <div class="layui-row">
                    <div class="layui-col-xs7">
                        <label class="layadmin-user-login-icon layui-icon layui-icon-vercode" for="LAY-user-login-vercode"></label>
                        <input type="text" name="verCode" id="LAY-user-login-vercode" lay-verify="required" placeholder="验证码" class="layui-input">
                    </div>
                    <div class="layui-col-xs5">
                        <div style="margin-left: 10px;">
                            <button type="button" class="layui-btn layui-btn-primary layui-btn-fluid" id="LAY-user-getsmscode">获取验证码</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layadmin-user-login-icon layui-icon layui-icon-password" for="LAY-user-login-password"></label>
                <input type="password" name="passWord" id="LAY-user-login-password" lay-verify="pass" placeholder="密码" class="layui-input">
            </div>
            <div class="layui-form-item">
                <label class="layadmin-user-login-icon layui-icon layui-icon-password" for="LAY-user-login-repass"></label>
                <input type="password" name="repass" id="LAY-user-login-repass" lay-verify="required" placeholder="确认密码" class="layui-input">
            </div>
            <div class="layui-form-item">
                <label class="layadmin-user-login-icon layui-icon layui-icon-username" for="LAY-user-login-nickname"></label>
                <input type="text" name="userName" id="LAY-user-login-nickname" lay-verify="nickname" placeholder="昵称" class="layui-input">
            </div>
            <div class="layui-form-item">
                <input type="checkbox" name="agreement" lay-skin="primary" title="同意用户协议" checked>
            </div>
            <div class="layui-form-item">
                <button class="layui-btn layui-btn-fluid" lay-submit lay-filter="LAY-user-reg-submit" type="submit">注 册</button>
            </div>
            <#--<div class="layui-trans layui-form-item layadmin-user-login-other">
                <label>社交账号注册</label>
                <a href="javascript:;"><i class="layui-icon layui-icon-login-qq"></i></a>
                <a href="javascript:;"><i class="layui-icon layui-icon-login-wechat"></i></a>
                <a href="javascript:;"><i class="layui-icon layui-icon-login-weibo"></i></a>

                <a href="login.html" class="layadmin-user-jump-change layadmin-link layui-hide-xs">用已有帐号登入</a>
                <a href="login.html" class="layadmin-user-jump-change layadmin-link layui-hide-sm layui-show-xs-inline-block">登入</a>
            </div>-->
        </div>
    </div>

    <div class="layui-trans layadmin-user-login-footer">

        <p>© 2020
            <!--<a href="http://www.layui.com/" target="_blank">layui.com</a>-->
        </p>
        <!--<p>-->
            <!--<span><a href="http://www.layui.com/admin/#get" target="_blank">获取授权</a></span>-->
            <!--<span><a href="http://www.layui.com/admin/pro/" target="_blank">在线演示</a></span>-->
            <!--<span><a href="http://www.layui.com/admin/" target="_blank">前往官网</a></span>-->
        <!--</p>-->
    </div>

</div>

</form>
<script src="../js/jquery-3.4.1.min.js"></script>
<script src="../js/jquery.cookie.js"></script>
<script src="../js/pub.js"></script>
<script src="../layui/layui.js"></script>
<script type="text/javascript">
    $(function () {
        $("#LAY-user-getsmscode").click(function () {
            var email = $("input[name='email']").val();
            // console.log("email=" + email)
            app.pubAjaxGet("/rest-user/send-email",{email: email}, function (res) {
                console.log("res=" + res)
            })
        })
    })
</script>
</body>
</html>