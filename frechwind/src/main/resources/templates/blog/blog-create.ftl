<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <title>创建博客</title>
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
    <form action="/blog/blog-create" method="get">
        <div style="width: 90%;margin: 10px 5%;">
            <div><input type="text" name="title" placeholder="请输入标题"/></div>
        </div>
        <div style="width: 90%;margin: 10px 5%;">
            <div><textarea name="content" rows="30" placeholder="请输入内容"></textarea></div>
        </div>
        <div style="border-top: 1px solid #ddd; text-align: center;position: relative;background: #fff; margin: 0 5%;"></div>
        <div style="width: 100%;">
            <div style="float: left;width: 50%;">
                <button type="reset" >重置</button>
            </div>
            <div style="float: left;width: 50%;">
                <button type="submit" >登录</button>
            </div>
        </div>
    </form>

</div>
</body>
</html>