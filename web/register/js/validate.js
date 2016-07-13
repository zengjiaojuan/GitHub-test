
//验证手机号格式
function regPhoneNum(regPhoneNum,regNotice){
    var $regPhoneNum=$(regPhoneNum);
    var $regNotice=$(regNotice);
    $regPhoneNum.on("blur",function(){
        var reg=/^1[3|4|5|7|8][0-9]{9}$/;
        if(reg.test($regPhoneNum.val())){
            $regNotice.text("");
            return true;
        }
        else{
            $regNotice.text("您输入的手机号无效");
            return false;
        }
    });
}
//验证密码格式
function regPwd(regPwd,regNotice){
    var $regPwd=$(regPwd);
    var $regNotice=$(regNotice);
    $regPwd.on("blur",function(){
        var reg=/^\w{6,12}$/;
        if(reg.test($regPwd.val())){
            $regNotice.text("");
            return true;
        }
        else{
            $regNotice.text("密码长度应为6~12个字符（数字、字母、下划线）");
            return false;
        }
    });
}
//判断验证码为6位数
function regCode(regCode,regNotice){
    var $regCode=$(regCode);
    var $regNotice=$(regNotice);
    $regCode.on("blur",function(){
        if($regCode.val().length == 6){
            return true;
        }
        else {
            $regNotice.text("请输入正确的验证码");
            return false;
        }
    });
}
/*点击获取验证码 ，验证码开始倒计时*/
function getCodeActive(obj){
    console.log("已发送验证码");
    var $getRegCode = obj;
    if (!$getRegCode.hasClass('disable')) {
        $getRegCode.addClass('disable');
        var iSecond = 60;
        var timer;
        $getRegCode.text(iSecond + "s");
        iSecond--;
        timer = setInterval(function () {
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