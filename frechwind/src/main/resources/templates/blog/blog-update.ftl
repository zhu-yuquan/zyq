<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <title>修改博客</title>
    <link rel="stylesheet" href="../css/zyq.css">
    <script type="text/javascript" src="../js/jquery-3.4.1.min.js"></script>
    <style>

    </style>
</head>
<body>
<div style="width: 100%;">
    <div style="padding-left: 5%;color: rebeccapurple;">
        <h5>我的领地我做主</h5>
    </div>
    <#list blogList as blog>
        <div style="width: 90%;margin: 10px 5%;">
            <div>${blog.title?if_exists}: ${blog.content?if_exists}</div>
        </div>
        <div style="border-top: 1px solid #ddd; text-align: center;position: relative;background: #fff; margin: 0 5%;"></div>
    </#list>
</div>
</body>
</html>