<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <title>登录</title>
    <link rel="stylesheet" href="../css/zyq.css">
    <script type="text/javascript" src="../js/jquery-3.4.1.min.js"></script>
    <script type="text/javascript" src="../js/jquery.validate.js"></script>
    <style type="text/css">
        Body{
            background:url("../images/loginback.jpg") repeat-y;
            background-size: 100%;
        }
        input{
            outline-style: none;
            border: 1px solid rebeccapurple;
            border-radius: 3px;
            padding: 8px 5px;
            margin: 10px auto;
            width: 60%;
            font-weight: 300;
            font-family: "Microsoft soft";
        }
        button{
            padding: 5px 8px;
            background-image: linear-gradient(#f5c153, #ea920d);
            border: 1px solid rgba(0,0,0,.2);
            border-radius: .3em;
            box-shadow: 0 1px white inset;
            text-align: center;
            text-shadow: 0 1px 1px black;
            color:white;
            font-weight: bold;
        }

        button:hover {
            box-shadow: 0 1px white inset;
            border-color: rgba(0,0,0,.3);
            background: #f5c153;
        }
    </style>
</head>
<body>
<div style="position: absolute;width: 100%;height: 100px;">
    <form action="/user/login" method="get">
        <div style="margin: 20% auto 0 auto; width: 100%;text-align: center;color: white;">
            <h3>登录</h3>
        </div>
        <div style="margin: 5% auto; width: 100%;text-align: center;">
            <div style="width: 100%;color: red;text-align: center;">${message?if_exists}</div>
            <div style="width: 100%; color: white;">
                账号：<input type="text" name="userName" placeholder="请输入账号" required/>
            </div>
            <div style="width: 100%;color: white;">
                密码：<input type="password" name="passWord" placeholder="请输入密码" required/>
            </div>
            <div style="width: 100%;">
                <div style="float: left;width: 50%;">
                    <button type="reset" >重置</button>
                </div>
                <div style="float: left;width: 50%;">
                    <button type="submit" >登录</button>
                </div>
            </div>
        </div>
    </form>

</div>
</body>
</html>