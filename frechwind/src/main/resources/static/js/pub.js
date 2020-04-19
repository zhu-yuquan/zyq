/**
 * Created by win7 on 2019/6/18.
 */
var app = {
    globalParas: {},
    pubAjaxGet: function (url, paras, successFun) {
        $.ajax({
            url: url,
            type: "GET",
            data: paras,
            headers: {'restSessionCode': getCookie("restSessionCode")},
            // dataType: "json",
            success: function (res) {
                successFun(res);
            },
            error: function (request, textStatus, errorThrown) {
                if (request.responseText) {
                    var restResult = eval("(" + request.responseText + ")");
                    if ("NoLogin" == restResult.code || "512" == restResult.code) {
                        var loginTimeOutFlag = getCookie("loginTimeOutFlag");
                        if(loginTimeOutFlag != 'Y'){
                            alert(restResult.title);
                            $.cookie("loginTimeOutFlag",'Y');
                        }
                        appLog(restResult.title,url);
                        window.location.href = "../login.html";
                    } else if (restResult.status = 404) {
                        alert('404错误（网页找不到）：' + url);
                        appLog("404错误（网页找不到）",url);
                    } else {
                        alert(restResult.title);
                        appLog(restResult.title,url);
                        // window.history.back();
                    }
                } else {
                    alert("出现错误");
                }
            }
        });
    },
    pubAjaxPost: function (url, paras, successFun) {
        $.ajax({
            url: url,
            type: "POST",
            traditional: true,
            data: paras,
            headers: {'restSessionCode': getCookie("restSessionCode")},
            // dataType: "json",//加载类型有text，和json,此处不写为好，以免得不到数据
            success: function (res) {
                successFun(res);
            },
            error: function (request, textStatus, errorThrown) {
                if (request.responseText) {
                    var restResult = eval("(" + request.responseText + ")");
                    if ("NoLogin" == restResult.code || "512" == restResult.code) {
                        var loginTimeOutFlag = getCookie("loginTimeOutFlag");
                        if(loginTimeOutFlag != 'Y'){
                            alert(restResult.title);
                            $.cookie("loginTimeOutFlag",'Y');
                        }
                        appLog(restResult.title,url);
                        window.location.href = "../login.html";
                    } else if (restResult.status = 404) {
                        alert('404错误（网页找不到）：' + url);
                        appLog("404错误（网页找不到）",url);
                    } else {
                        alert(restResult.title);
                        appLog(restResult.title,url);
                        // window.history.back();
                    }
                } else {
                    alert("出现错误");
                }
            }
        });
    },
    // 时间戳转换成时间
    timestampToTime: function (cjsj) {
        var date = new Date(cjsj); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
        var Y = date.getFullYear();
        var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1);
        var D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate());
        var h = ' ' + date.getHours() + ':';
        var m = date.getMinutes() + ':';
        var s = date.getSeconds();
        return Y + "年" + M + "月" + D + "日"
    },

    //格式化为时分秒
    unixPoorTrans: function (cjsj) {
        var date = new Date(cjsj);
        var hour = date.getHours();//时
        var minute = date.getMinutes(); //分
        var second = date.getSeconds();//秒
        date = hour + ':' + minute + ':' + second;
        return date;
    },

    formattingTime: function (cjsj) {
        var date = new Date(cjsj);
        //console.log(date);
        //console.log(date.getFullYear());

        var year = date.getFullYear();
        var month = date.getMonth() + 1;//获取当前月份的日期
        var day = date.getDate();
        var hour = date.getHours();
        var minute = date.getMinutes();

        if (hour < 10) {
            hour = '0' + hour;
        }

        if (minute < 10) {
            minute = '0' + minute;
        }

        var time = hour + ':' + minute;

        var today = new Date();

        var dates = '';

        if (this.isSameDay(today, date)) {
            dates = "今天 ";
        } else if (this.isSameDay(this.addDate(today, -1), date)) {
            dates = "昨天 ";
        } else if (this.isSameDay(this.addDate(today, -2), date)) {
            dates = "前天 ";
        } else if (this.isSameDay(this.addDate(today, 1), date)) {
            dates = "明天 ";
        } else if (this.isSameDay(this.addDate(today, 2), date)) {
            dates = "后天 ";
        } else if (year == today.getFullYear()) {
            dates = month + "/" + day + " ";
        } else {
            dates = year + "/" + month + "/" + day + " ";
        }
        return dates + time;
    },

    addDate: function (today, addDayCount) {
        var dd;
        if (today) {
            dd = new Date(today);
        } else {
            dd = new Date();
        }
        dd.setDate(dd.getDate() + addDayCount);//获取AddDayCount天后的日期
        var y = dd.getFullYear();
        var m = dd.getMonth() + 1;//获取当前月份的日期
        var d = dd.getDate();

        var hh = dd.getHours();
        var mm = dd.getMinutes();
        if (m < 10) {
            m = '0' + m;
        }
        if (d < 10) {
            d = '0' + d;
        }
        return new Date(y + "-" + m + "-" + d + ' ' + hh + ':' + mm);
    },

    isSameDay: function (dt1, dt2) {
        var y1 = dt1.getFullYear();
        var m1 = dt1.getMonth();
        var d1 = dt1.getDate();

        var y2 = dt2.getFullYear();
        var m2 = dt2.getMonth();
        var d2 = dt2.getDate();

        return y1 == y2 && m1 == m2 && d1 == d2;
    },

    //两个日期相减得到秒数
    intervalTime: function (cjsj) {
        var date1 = new Date(cjsj);  //开始时间
        var date2 = new Date();    //结束时间
        var date3 = (date2.getTime() - date1.getTime()) / 1000;  //时间差的秒数
        var min = this.s_to_hs(date3);
        return min;
    },

    //时间换算成小时
    inervalHour: function (cjsj) {
        var date1 = new Date(cjsj);  //开始时间
        var date2 = new Date();    //结束时间
        var date3 = (date2.getTime() - date1.getTime()) / 1000 / 60 / 60;
        return date3;
    },

    //秒数格式化
    s_to_hs: function (a) {
        var hh = parseInt(a / 3600);
        if (hh < 10) hh = "0" + hh;
        var mm = parseInt((a - hh * 3600) / 60);
        if (mm < 10) mm = "0" + mm;
        var ss = parseInt((a - hh * 3600) % 60);
        if (ss < 10) ss = "0" + ss;
        var length = hh + ":" + mm + ":" + ss;
        if (a > 0) {
            return length;
        } else {
            return "NaN";
        }
    },
    imgSize: function (imgSrc, width, height) {
        if (imgSrc) {
            var pos = imgSrc.lastIndexOf(".");
            var extName = "";
            if (pos != -1) {
                extName = imgSrc.substring(pos + 1);
                imgSrc = imgSrc.substring(0, pos);
            }
            imgSrc = imgSrc + "_" + width + "x" + height + "." + extName;
            // console.log(imgSrc);
            return imgSrc;
        }
    }
};

function getCookie(cookieName) {
    return $.cookie(cookieName);
}

//2019-08-21(没有时分秒)
function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();
    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;
    return [year, month, day].join('-');
}

//2019-08-21 18:36:03(有时分秒)
function formatsDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();
    var hour = d.getHours();//时
    var minute = d.getMinutes(); //分
    var second = d.getSeconds();//秒
    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;
    if (hour < 10) hour = '0' + hour;
    if (minute < 10) minute = '0' + minute;
    if (second < 10) second = '0' + second;


    return [year, month, day].join('-') + " " + [hour, minute, second].join(':');
}

//2019-08-21T18:36:03(有时分秒，当使用type类型为'datetime-local'。注意：此处‘T’)
function formatsDates(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();
    var hour = d.getHours();//时
    var minute = d.getMinutes(); //分
    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;
    if (hour < 10) hour = '0' + hour;
    console.log("minute=" + minute);
    if (minute < 10) minute = '0' + minute;

    return [year, month, day].join('-') + "T" + [hour, minute].join(':');
}


//获取地址传参信息
function getQueryString(name) {
    var v = getParaFromUrl(name);
    console.log("name" + name + "---" + v);
    if (!v) {
        var paraValue = app.globalParas[name];
        delete app.globalParas[name];
        if (paraValue != null) return decodeURI(paraValue);  //unescape处理中文有问题 建议用decodeURI
        return null;
    }
    return v;
}

function setInputFromReq(form, name){
    var v = getQueryString(name);
    if(v){
        form[name].value = v;
    }
}

function getParaFromUrl(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);  //unescape处理中文有问题 建议用decodeURI
    return null;
};

function loadgetParaFromUrl(name, url) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    if (!url) {
        return null;
    }
    var r = url.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);  //unescape处理中文有问题 建议用decodeURI
    return null;
};

//图片上传
(function ($) {
    $.fn.doUploadPost = function (url, formData, successFun) {
        $.ajax({
            url: url,
            processData: false,  // 这个必须为false，不转换的信息
            contentType: false, // 这个必须为false，不指定发送信息的编码类型
            type: "POST",
            data: formData,
            headers: {'restSessionCode': getCookie("restSessionCode")},
            // dataType: "json",//加载类型有text，和json,此处不写为好，以免得不到数据
            success: function (res) {
                successFun(res);
            },
            error: function (request, textStatus, errorThrown) {
                alert('出错了！错误信息：' + request.responseText);
                console.log(request.responseText);
            }
        });
    };
})(jQuery);

//title, img, desc, success
function pubShare(shareParas) {
    var signatureUrl = window.location.href;
    $(".content-wrapper").pubAjaxGet("/photo-rest/wxJsapiSignature", {
        signatureUrl: signatureUrl,
    }, function (res) {
        var wxJsapiSignature = res.data.wxJsapiSignature;
        if (wxJsapiSignature) {
            var shareTitle = shareParas.title;
            var shareLink = signatureUrl;
            var shareImg = shareParas.img;

            wx.config({
                debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                appId: wxJsapiSignature.appId, // 必填，公众号的唯一标识
                timestamp: wxJsapiSignature.timestamp, // 必填，生成签名的时间戳
                nonceStr: wxJsapiSignature.nonceStr, // 必填，生成签名的随机串
                signature: wxJsapiSignature.signature,// 必填，签名
                jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage'] // 必填，需要使用的JS接口列表
            });
            wx.ready(function () {
                wx.checkJsApi({
                    jsApiList: [
                        'getLocation',
                        'onMenuShareTimeline',
                        'onMenuShareAppMessage'
                    ],
                    success: function (res) {
//alert(JSON.stringify(res));
                    }
                });

// config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
//分享给朋友
                wx.onMenuShareAppMessage({
                    title: shareTitle, // 分享标题
                    desc: shareParas.desc, // 分享描述
                    link: shareLink, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                    imgUrl: shareImg, // 分享图标
                    type: 'link', // 分享类型,music、video或link，不填默认为link
                    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                    success: function () {
                        shareParas.success();
                    },
                    cancel: function () {
                        alert('已取消分享');
                    }
                });
//分享到朋友圈
                wx.onMenuShareTimeline({
                    title: shareTitle, // 分享标题
                    link: shareLink, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                    imgUrl: shareImg, // 分享图标
                    success: function () {
                        shareParas.success();
                    },
                    cancel: function () {
//                    alert('已取消分享');
                    }
                });
            });
            wx.error(function (res) {
// config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
            });
        }
    });
}

(function ($) {
    $.fn.myload = function (url, paras, callback) {
        for (var paraName in paras) {
            app.globalParas[paraName] = paras[paraName]
        }
        var divId = '.' + $(this)[0].className;
        if (!$(this)[0].className) {
            alert("调用myload的对象必须有一个class");
            return;
        }
        console.log('divId:' + divId);

        window.history.pushState({divId: divId, divUrl: url}, "", null);
        window.onpopstate = function (event) {
            var idurl = event.state;
            if (idurl) {
                $(idurl.divId).load(idurl.divUrl);
            }
        };
        $(divId).load(url);
    };
})(jQuery);

(function ($) {
    $.fn.pubAjaxGet = function (url, paras, successFun) {
        if (!url.startsWith("http")) {
            url = url;
        }
        //判断传进来的参数paras是否为'{}'
        if (JSON.stringify(paras) != '{}') {
            //判断参数是否为对象
            if (Object.prototype.toString.call(paras) === '[object Object]') {
                paras.restSessionCode = getCookie("restSessionCode");
            } else {
                //当参数为字符串是拼接'restSessionCode'
                paras += "&restSessionCode=" + getCookie("restSessionCode");
            }
        }
        if (JSON.stringify(paras) == '{}') {
            paras = {};
            paras.restSessionCode = getCookie("restSessionCode");
        }
        $.ajax({
            url: url,
            type: "GET",
            data: paras,
            headers: {
                //'restSessionCode': getCookie("restSessionCode")
            },
            dataType: "json", //确保返回类型都是json，请勿注释掉这句
            success: function (res) {
                successFun(res);
            },
            error: function (request, textStatus, errorThrown) {
                if (request.responseText) {
                    var restResult = eval("(" + request.responseText + ")");
                    if ("NoLogin" == restResult.code || "512" == restResult.code) {
                        var loginTimeOutFlag = getCookie("loginTimeOutFlag");
                        if(loginTimeOutFlag != 'Y'){
                            alert(restResult.title);
                            $.cookie("loginTimeOutFlag",'Y');
                        }
                        appLog(restResult.title,url);
                        window.location.href = "../login.html";
                    } else if (restResult.status = 404) {
                        alert('404错误（网页找不到）：' + url);
                        appLog("404错误（网页找不到）",url);
                    } else {
                        alert(restResult.title);
                        appLog(restResult.title,url);
                        // window.history.back();
                    }
                } else {
                    alert("出现错误");
                }
            }
        });
    };
})(jQuery);

function appLog(para,url) {
    var paras={errorMessage: para,url:url};
    $.ajax({
        url: "/rest-app-log/error-log",
        type: "POST",
        data: paras,
        headers: {
            'restSessionCode': getCookie("restSessionCode")
        },
    })
}



(function ($) {
    $.fn.pubAjaxPost = function (url, paras, successFun) {
        $.ajax({
            url: url,
            type: "POST",
            traditional: true,
            data:paras,
            headers: {'restSessionCode': getCookie("restSessionCode")},
            // dataType: "json",//加载类型有text，和json,此处不写为好，以免得不到数据
            success: function (res) {
                successFun(res);
            },
            error: function (request, textStatus, errorThrown) {

                //alert(request.responseText);
                var restResult = eval("(" + request.responseText + ")");
                alert(restResult.title);
                appLog(restResult.title,url);
                // window.location.href = "../login.html";
            }
        });
    };
})(jQuery);


(function ($) {
    $.fn.mathFormatOne = function (val) {
        val = Math.round(val * 10) / 10;
        var xsd = val.toString().split('.');
        if (xsd.length === 1) {
            val = val.toString();
            return val
        }
        return val;
    };
})(jQuery);

function iniValidate(form, callback) {
    $("input[required], input.required, select[required]").each(function () {
        var label = $(this).parent().prev().text();
        console.log(label);
        if (label.indexOf("*") == -1) {
            $(this).parent().prev().append("<div class='required-sign'>*</div>");
        }
    });
    form.validate({
        submitHandler: function (form) {
            callback(form);
        },
        errorPlacement: function(error, element) {
            //alert(element.parent().text());
            error.appendTo(element.parent());
        }
    });
};


// 用例 ：var num=number_format(1234567.089, 2, ".", ",");//1,234,567.09
function number_format(number, decimals, dec_point, thousands_sep) {
    /*
     * 参数说明：
     * number：要格式化的数字
     * decimals：保留几位小数
     * dec_point：小数点符号
     * thousands_sep：千分位符号
     * */
    number = (number + '').replace(/[^0-9+-Ee.]/g, '');
    var n = !isFinite(+number) ? 0 : +number,
        prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
        sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
        dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
        s = '',
        toFixedFix = function (n, prec) {
            var k = Math.pow(10, prec);
            return '' + Math.ceil(n * k) / k;
        };

    s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.'); //四舍五入round，floor向下取整
    var re = /(-?\d+)(\d{3})/;
    while (re.test(s[0])) {
        s[0] = s[0].replace(re, "$1" + sep + "$2");
    }

    if ((s[1] || '').length < prec) {
        s[1] = s[1] || '';
        s[1] += new Array(prec - s[1].length + 1).join('0');
    }
    return s.join(dec);
}