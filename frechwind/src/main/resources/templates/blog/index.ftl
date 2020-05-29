<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <title>个人博客</title>
    <link rel="stylesheet" href="../css/zyq.css">
    <script type="text/javascript" src="../js/jquery-3.4.1.min.js"></script>
    <script type="text/javascript" src="../js/main.js"></script>
    <script type="text/javascript">
        $(function () {

        })
        // function maxImg(el){
        //     alert("1234");
        //     alert("el=" + el);
        //     alert("el.src=" + el.src);
        // }
    </script>
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
        <div>
            <a href="/blog/blog-view?blogId=${blog.blogId}">
                <div style="width: 90%;margin: 10px 5%;">
                    <div>${blog.title?if_exists}${blog.createTime?string("yyyy-MM-dd HH:mm:ss")!}</div>
                    <div>${blog.content?if_exists}</div>
                </div>
            </a>
            <#if blog.uploadList?size != 0>
                <div style="position: relative;width: 90%;min-height: 115px;max-height: 340px;overflow: hidden;margin: 10px 5%;">
                    <#list blog.uploadList as upload>
                        <div class="upload-content-img">
                            <#if upload.type=="mp4">
                                <video width="320" height="240" controls>
                                    <source src="${upload.absolutePath?if_exists}"  type="video/mp4">
                                </video>
                            <#else>
                                <img src="${siteImgUtil.addImgSizeOnShow(upload.absolutePath?if_exists,206,206)}" width="103" height="103" class="preview" onclick="maxImg(this)"/>
                            </#if>

                        </div>
                    </#list>
                </div>
            </#if>
            <div class="btn-line"></div>
        </div>

    </#list>
</div>
</body>
</html>