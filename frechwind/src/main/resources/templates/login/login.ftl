<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <title>登录</title>
    <link rel="stylesheet" href="../css/zyq.css">
    <script type="text/javascript" src="../js/jquery-3.4.1.min.js"></script>
    <style type="text/css">
        Body{
            background:url("../images/loginback.jpg") repeat-y;
            background-size: 100%;
        }
        input{
            border: 1px solid rebeccapurple;
            margin: 10px auto;
        }
        .button {
            background: #3498db;
            background-image: -webkit-linear-gradient(top, #3498db, #2980b9);
            background-image: -moz-linear-gradient(top, #3498db, #2980b9);
            background-image: -ms-linear-gradient(top, #3498db, #2980b9);
            background-image: -o-linear-gradient(top, #3498db, #2980b9);
            background-image: linear-gradient(to bottom, #3498db, #2980b9);
            -webkit-border-radius: 28;
            -moz-border-radius: 28;
            border-radius: 28px;
            font-family: Arial;
            color: #ffffff;
            font-size: 16px;
            padding: 12px 30px 12px 30px;
            text-decoration: none;
        }

        .button:hover {
            background: #2980b9;
            background-image: -webkit-linear-gradient(top, #2980b9, #3498db);
            background-image: -moz-linear-gradient(top, #2980b9, #3498db);
            background-image: -ms-linear-gradient(top, #2980b9, #3498db);
            background-image: -o-linear-gradient(top, #2980b9, #3498db);
            background-image: linear-gradient(to bottom, #2980b9, #3498db);
            color: #ffffff;
            text-decoration: none;
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
                账号：<input type="text" name="userName"/>
            </div>
            <div style="width: 100%;color: white;">
                密码：<input type="password" name="passWord"/>
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