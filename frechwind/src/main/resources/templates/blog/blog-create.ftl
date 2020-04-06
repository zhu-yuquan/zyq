<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <title>创建博客</title>
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
        img[src=""],img:not([src]){
            opacity:0;
        }
    </style>
    <script>
        function uploadPhoto() {
            $("#photoFile").click();
        }

        /**
         * 上传图片
         */
        function upload() {
            if ($("#photoFile").val() == '') {
                return;
            }
            var formData = new FormData();
            formData.append('uploadFile', document.getElementById('photoFile').files[0]);
            formData.append("ownerType", "blogImage");
            formData.append("ownerId", "${userId?if_exists}");
            formData.append("bizType", "Blog");
            $.ajax({
                url:"/rest-cms-upload/upload",
                type:"post",
                data: formData,
                contentType: false,
                processData: false,
                success: function(data) {
                    if (data.status == "success") {
                        $("#preview_photo").attr("src", data.src);
                        $("#productImg").val(data.filename);
                    } else {
                        alert(data.ownerType);
                    }
                },
                error:function(data) {
                    alert("上传失败")
                }
            });
        }
    </script>
</head>
<body>
<div style="width: 100%;">
    <div style="padding-left: 5%;color: rebeccapurple;">
        <div><h5>我的领地我做主</h5></div>
    </div>
    <form action="/blog/blog-create" method="post">
        <input type="hidden" name="userId" value="${userId?if_exists}"/>
        <div style="width: 90%;margin: 10px 5%;">
            <div><input type="text" name="title" placeholder="请输入标题" required/></div>
        </div>
        <div style="width: 90%;margin: 10px 5%;">
            <textarea name="content" rows="8" placeholder="请输入内容" required></textarea>
        </div>
        <div style="border-top: 1px solid #ddd; text-align: center;position: relative;background: #fff; margin: 0 5%;"></div>
        <div style="width: 90%;margin: 10px 5%;">
            <input type="file" id="photoFile" style="display: none;" onchange="upload()">
            <a href="javascript:void(0)" onclick="uploadPhoto()">
                <img src="../images/upload.jpg" width="80px" height="80px"/>
            </a>
            <img id="preview_photo" src="" width="100px" height="100px">
        </div>

        <div style="border-top: 1px solid #ddd; text-align: center;position: relative;background: #fff; margin: 0 5%;"></div>
        <div style="width: 90%;margin: 10px 5%;">
            <div style="float: left;width: 50%;">
                <button type="reset" class="button">重置</button>
            </div>
            <div style="float: left;width: 50%;text-align: right;">
                <button type="submit" class="button">提交</button>
            </div>
        </div>
    </form>

</div>
</body>
</html>