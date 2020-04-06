<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <title>个人博客</title>
    <link rel="stylesheet" href="../css/zyq.css">
    <script type="text/javascript" src="../js/jquery-3.4.1.min.js"></script>
    <style>

    </style>
</head>
<body>
<div style="width: 100%;">
    <div style="margin: 0 5%;width: 90%;color: rebeccapurple;">
        <div style="width: 50%;float: left;"><h5>我的领地我做主</h5></div>
        <div style="width: 50%;float: left;text-align: right;">
            <a href="/blog/blog-add?userId=${userId}">
                <h5>发布我的博客</h5>
            </a>
        </div>
    </div>
    <#list blogList as blog>
        <a href="/blog/blog-view?blogId=${blog.blogId}">
            <div style="width: 90%;margin: 10px 5%;">
                <div>${blog.title?if_exists}</div>
                <div>${blog.content?if_exists}</div>
            </div>
        </a>
        <div style="position: relative;width: 90%;margin: 10px 5%;">
            <#list blog.uploadList as upload>
                <div style="float: left;width: 31%;margin: 1.1%;">
                    <img src="${upload.absolutePath?if_exists}" width="100%" height="100%"/>
                </div>
            </#list>
        </div>
        <div style="border-top: 1px solid #ddd; text-align: center;position: relative;background: #fff; margin: 0 5%;"></div>
    </#list>
</div>
</body>
</html>