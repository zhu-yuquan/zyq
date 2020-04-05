<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <title>博客详情</title>
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
    <div style="width: 90%;margin: 10px 5%;">
        <div>标题：${blog.title?if_exists}</div>
        <div>${blog.title?if_exists}: ${blog.content?if_exists}</div>
    </div>
    <div style="border-top: 1px solid #ddd; text-align: center;position: relative;background: #fff; margin: 0 5%;"></div>
    <div style="width: 100%;text-align: center;">
        <a href="/blog/blog-edit?blogId=${blog.blogId?if_exists}">
            <div style="width: 62%;text-align: center;color: limegreen;margin: 15px auto;">
                编辑
            </div>
        </a>
        <a href="/blog/blog-delete?blogId=${blog.blogId?if_exists}" onclick="return confirm('确定删除吗')">
            <div style="width: 62%;text-align: center;color: #dddddd;margin: 15px auto;">
                删除
            </div>
        </a>

    </div>
</div>
</body>
</html>