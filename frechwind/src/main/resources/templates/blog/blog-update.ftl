<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <title>修改博客</title>
    <link rel="stylesheet" href="../css/zyq.css">
    <script type="text/javascript" src="../js/jquery-3.4.1.min.js"></script>
    <script type="text/javascript" src="../js/jquery.validate.js"></script>
    <style type="text/css">
        input{
            outline-style: none;
            border: 1px solid #ccc;
            border-radius: 3px;
            padding: 8px 5px;
            width: 75%;
            font-size: 14px;
            font-weight: 500;
            font-family: "Microsoft soft";
        }
        textarea{
            border: 1px solid burlywood;
            border-radius: 4px;
            padding: 8px 5px;
            width: 96%;
            font-weight: 100;
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
<div style="width: 100%;">
    <div style="padding-left: 5%;color: rebeccapurple;">
        <h5>我的领地我做主</h5>
    </div>
    <form action="/blog/blog-update" method="post">
    <div style="width: 90%;margin: 10px 5%;">
        <div><input type="text" name="title" value="${blog.title?if_exists}" required/></div>
    </div>
    <div style="border-top: 1px solid #ddd; text-align: center;position: relative;background: #fff; margin: 0 5%;"></div>
    <div style="width: 90%;margin: 10px 5%;">
        <div><textarea name="content" rows="10" required>${blog.content?if_exists}</textarea></div>
    </div>
    <div style="border-top: 1px solid #ddd; text-align: center;position: relative;background: #fff; margin: 0 5%;"></div>
    <div style="width: 90%;margin: 10px 5%;">
        <div style="float: left;width: 50%;">
            <button type="button" onclick="javascript:history.back(-1);">返回</button>
        </div>
        <div style="float: left;width: 50%;text-align: right;">
            <button type="submit" >提交</button>
        </div>
    </div>
    </form>
</div>
</body>
</html>