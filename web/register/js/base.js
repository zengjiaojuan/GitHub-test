
window.addEventListener('touchmove',function(e){
    e.preventDefault();
});

if ("micromessenger" == navigator.userAgent.toLowerCase().match(/MicroMessenger/i)) {
    $('body').prepend('<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>');
}
var _hmt = _hmt || [];
(function() {
    var hm = document.createElement("script");
    hm.src = "//hm.baidu.com/hm.js?cf625b2bfcabcb09092d52343055c405";
    var s = document.getElementsByTagName("script")[0];
    s.parentNode.insertBefore(hm, s);
})();


//设置最小高度,输入框和底部菜单问题
$('body').css('min-height', $(window).height());

//获取地址中的信息
function GetQueryString(name) {
    /*定义正则，用于获取相应参数*/
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    /*字符串截取，获取匹配参数值*/
    var r = window.location.search.substr(1).match(reg);
    /*返回参数值*/
    if (r != null) {
        return decodeURI(r[2]);
    } else {
        return null;
    }

}

/*cookie*/
function setCookie(name, value, oDay) {
    var oTime = new Date();
    var oDate = oTime.getDate();
    oTime.setDate(oDate + oDay);
    document.cookie = name + '=' + value + ';expires=' + oTime + ';path=/';

}

function getCookie(name) {
    var str = document.cookie.split('; ');

    for (i = 0; i < str.length; i++) {
        arr = str[i];
        arr1 = arr.split('=');
        if (arr1[0] == name)
            return arr1[1];

    }

    return '';

}

function removeCookie(name) {

    setCookie(name, '00', -1);

}



//验证手机号格式
function regPhoneNum(regPhoneNum, regNotice) {
    var $regPhoneNum = $(regPhoneNum);
    var $regNotice = $(regNotice);
    $regPhoneNum.on("blur", function() {
        var reg = /^1[3|4|5|7|8][0-9]{9}$/;
        if (reg.test($regPhoneNum.val())) {
            $regNotice.text("");
            return true;
        } else {
            $regNotice.text("您输入的手机号无效");
            $regPhoneNum.val('');
            return false;
        }
    });
}
//验证密码格式
function regPwd(regPwd, regNotice) {
    var $regPwd = $(regPwd);
    var $regNotice = $(regNotice);
    $regPwd.on("blur", function() {
        var reg = /^\w{6,20}$/;
        if (reg.test($regPwd.val())) {
            $regNotice.text("");
            return true;
        } else {
            $regNotice.text("密码长度应为6~20个字符（数字、字母、下划线）");
            return false;
        }
    });
}
/*点击获取验证码 ，验证码开始倒计时*/
function getCodeActive(obj) {
    console.log("已发送验证码");
    var $getRegCode = obj;
    if (!$getRegCode.hasClass('disable')) {
        $getRegCode.addClass('disable');
        var iSecond = 60;
        var timer;
        $getRegCode.text(iSecond + "s");
        iSecond--;
        timer = setInterval(function() {
            if (iSecond < 0) {
                $getRegCode.text("重新获取");
                clearInterval(timer);
                $getRegCode.removeClass('disable');
            } else {
                $getRegCode.text(iSecond + "s");
                iSecond--;
            }
        }, 1000);
    }
}
function birth() {
    $('#birth-text').swipe({
        tap: function(){
            if (!$(this).hasClass('disable')) {
                $('#sel-year').removeClass('dn');
                scrollYear.refresh();
                scrollYear.scrollToElement('li:nth-child(70)', 0);
            }
        }
    });
    var the_ul = $('#year-ul'),
     i =  1911,//开始年份在这里设置
     str = '',
     now = new Date().getFullYear()+1;
    for (;i<now;i++) {
        str+='<li>'+i+'</li>';
    }
    the_ul.append(str);
    var scrollYear = new IScroll('#scroll-year', {
        probeType: 3,
        mouseWheel: true,
        preventDefault: false
    });
    the_ul.swipe({
        tap: function(e,t){
            if('LI' === t.tagName){
                $('#birth-text').text($(t).text());
                $('#sel-year').addClass('dn');
            }
        }
    });
    // 返回按钮
    $('.back-dn').swipe({
        tap: function(){
           $(this).parent().parent().addClass('dn');
        }
    });
}

if (!window.sessionStorage.historyNum || isNaN(parseInt(window.sessionStorage.historyNum))) {
    window.sessionStorage.historyNum = 0;
    var historyNum = parseInt(window.sessionStorage.historyNum);
    window.sessionStorage['prevPage' + historyNum] = window.location.href;
}
var historyNum = parseInt(window.sessionStorage.historyNum) || 0;
if ('true' === window.sessionStorage.history) {
    window.sessionStorage.history = 'false';
} else if (window.sessionStorage['prevPage' + historyNum] !== window.location.href) {
    historyNum++;
    window.sessionStorage.historyNum = historyNum;
    window.sessionStorage['prevPage' + historyNum] = window.location.href;
}
$('.back').swipe({
    tap: function(e) {
        var historyNum = parseInt(window.sessionStorage.historyNum);
        historyNum--;
        var href = window.sessionStorage['prevPage' + historyNum] || '/index.html';
        window.sessionStorage.history = 'true';
        window.sessionStorage.historyNum = historyNum;
        if (href === window.location.href) {
            href = '/index.html';
        }
        window.location = href;
        e.preventDefault();
    }
});
$('.back').swipe({
    tap: function(e) {
        window.history.go(-1);
        e.preventDefault();
    }
});

function commonLink() {
    if (-1 === window.location.href.indexOf('index.html')) {
        $('.index').swipe({
            tap: function(e) {
                window.location = '/index.html';
                e.preventDefault();
            }
        });
    }
    if (-1 === window.location.href.indexOf('newFamily.html')) {
        $('.family').swipe({
            tap: function(e) {
                window.location = '/html/newFamily.html';
                e.preventDefault();
            }
        });
    }
    if (-1 === window.location.href.indexOf('doctorInfo.html')) {
        $('.doctor').swipe({
            tap: function(e) {
                window.location = '/html/doctorInfo.html';
                e.preventDefault();
            }
        });
    }
    if (-1 === window.location.href.indexOf('my.html')) {
        $('.me').swipe({
            tap: function(e) {
                window.location = '/html/my.html';
                e.preventDefault();
            }
        });
    }
}
if (-1 === window.location.href.indexOf('index.html')) {
    commonLink();
}
$(document).on('touchmove', function(e) {
    e.preventDefault();
});
(function($) {
    // Determine if we on iPhone or iPad
    var isiOS = false;
    var agent = navigator.userAgent.toLowerCase();
    if (agent.indexOf('iphone') >= 0 || agent.indexOf('ipad') >= 0) {
        isiOS = true;
    }

    $.fn.doubletap = function(onDoubleTapCallback, onTapCallback, delay) {
        var eventName, action;
        delay = delay == null ? 500 : delay;
        eventName = isiOS == true ? 'touchend' : 'click';

        $(this).bind(eventName, function(event) {
            var now = new Date().getTime();
            var lastTouch = $(this).data('lastTouch') || now + 1 /** the first time this will make delta a negative number */ ;
            var delta = now - lastTouch;
            clearTimeout(action);
            if (delta < 500 && delta > 0) {
                if (onDoubleTapCallback != null && typeof onDoubleTapCallback == 'function') {
                    onDoubleTapCallback(event);
                }
            } else {
                $(this).data('lastTouch', now);
                action = setTimeout(function(evt) {
                    if (onTapCallback != null && typeof onTapCallback == 'function') {
                        onTapCallback(evt);
                    }
                    clearTimeout(action); // clear the timeout
                }, delay, [event]);
            }
            $(this).data('lastTouch', now);
        });
    };
})(jQuery);

//usage:
$(document).doubletap(
    /** doubletap-dblclick callback */
    function(e) {
        e.preventDefault();
    },
    /** touch-click callback (touch) */
    function(event) {});
//加密
var seed = getCookie('seed');

function decrypt(s) {
    var fnl = "",
        code = 0;
    for (var i = 0; i < s.length >> 1; i++) {
        code = new Number("0x" + s.substr(i * 2, 2));
        fnl += String.fromCharCode((code ^ (seed << 7 - i % 8 | seed >> i % 8 | 0x80)) & 0x7f);
    }
    return fnl;
}

function loginTimeOut() {
    if (localStorage.openid) {
        console.log('微信自动登录');
        //weixinTimeOut();
    } else if (getCookie('user') && '00' !== getCookie('user')) {
        console.log('手机号密码自动登录');
        //mobileTimeOut();
    }
}

function mobileTimeOut() {
    var data = {
        "mobile": getCookie('user'),
        "password": decrypt(getCookie('pwd'))
    };
    $.ajax({
        type: "POST",
        url: ajaxUrl + "User/Login",
        contentType: "text/plain; charset=UTF-8",
        dataType: 'json',
        data: JSON.stringify(data),
        success: function(data) {
            console.log(data);
            if (data.code == 0) {
                console.log('yes');
                window.localStorage.sessionId = null;
                window.localStorage.selfUuid = null;
                window.localStorage.sessionId = data.data.sessionId;
                window.localStorage.selfUuid = data.data.uuid;
                window.localStorage.removeItem('openid');
                window.location.reload();
            } else {
                console.log(data.message);
            }
        },
        error: function(res) {
            if (res.status == 401) {
                myalert.tips({
                    txt: "请重新登录",
                    fnok: function() {
                        window.location = "../html/newLogin.html";
                    },
                    btn: 1
                });

            }
        }
    })
}

function weixinTimeOut() {
    function weixinUserReg(openid) {
        var data = {
            "openid": openid
        };
        $.ajax({
            type: "POST",
            url: "http://www-test.zhaoduiyisheng.com/api/User/WeixinLogin",
            contentType: "text/plain;charset=UTF-8",
            dataType: "json",
            data: JSON.stringify(data),
            success: function(data) {

                if (data.code == 0) {

                    window.localStorage.sessionId = data.data.sessionId;
                    window.localStorage.selfUuid = data.data.uuid;
                    setCookie('user', data.data.mobile, 30);
                    window.location.reload();
                    //window.localStorage.bootstrap = data.data.bootstrap;
                    //getHomeMember();
                } else {
                    window.location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx14da7f3cb2e1b8ff&redirect_uri=" + encodeURI("http://www-test.zhaoduiyisheng.com/index.html") + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
                }
            },
            error: function(res) {
                if (res.status == 401) {
                    myalert.tips({
                        txt: "会话超时，请重新登录",
                        fnok: function() {
                            window.location = "../html/newLogin.html";
                        },
                        btn: 1
                    });

                }
            }
        });
    }
    weixinUserReg(window.localStorage.openid);
}