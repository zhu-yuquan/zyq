<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <title>test</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <script type="text/javascript" src="../js/jquery-3.4.1.min.js"></script>
    <style>
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
            formData.append("ownerId", "40288182714ecdaf01714ece75cf0000");
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
    <input type="file" id="photoFile" style="display: none;" onchange="upload()">
    <a href="javascript:void(0)" onclick="uploadPhoto()">选择图片</a>
    <img id="preview_photo" src="" width="200px" height="200px">
</body>
</html>