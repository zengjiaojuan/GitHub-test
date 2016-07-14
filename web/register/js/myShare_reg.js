
$(document).ready(function(){
	
	setTelAndInviteCode();
 
// 点击 获取验证码
 $("#get-reg-code").click(function getRegCode () {
 	   var phoneNum =  $("#reg-tel").val();
 	   var reg = /^1[3|4|5|7|8][0-9]{9}$/;
 	   if (!reg.test(phoneNum)) {
 	   	  alert("您输入的手机号码无效");
 	   }else{
           judgeRegOrNot(); 
 	   }    
 });


//点击 完成注册下载APP
 $("#reg-next").click(function nextClickToReg(){
	var phoneStr = $("#reg-tel").val();//电话号码                                                                                                                          
	var codeStr = $("#reg-code").val();//验证码
	var passStr = $("#reg-pwd").val(); //密码
//	window.validCode                   //邀请码
                          
    //验证密码格式  "^[a-zA-Z0-9]{6,16}+$"   ^\w{6,16}$
    var reg = /^[a-zA-Z0-9]{6,16}$/;
    if (!reg.test(passStr)) {
    	   alert("您输入的密码格式不正确");
    }else{
    	   if (codeStr == window.validCode) {//验证码
    	   	    regAjax();
    	   } else{
    	   	  alert("您输入的验证码不正确!"+window.validCode);
    	   } 
    }
   });
});

//设置页面显示的 好友电话号码 红包钱数 邀请码
function setTelAndInviteCode(){
	var urlInfo=window.location.href;  
	var argsIndex=urlInfo.indexOf("?");  
	var args=urlInfo.substring((argsIndex+1)).split("&");  
	var argsInfo="";  
	var argResult = "";
	var tel = "";
	var code ="";
	var amount="";
	
	var telarg =args[0].split("=");
	var codearg=args[1].split("=");
	var amountarg=args[2].split("=");
	tel = telarg[1];
	code= codearg[1];
	amount = amountarg[1];
 
	 $("#reg-inviteWords").text(code);
	 $("#reg-phone").text(tel);
	 $("#reg-amount").text(amount);
	 
	 
}


 
 
  function judgeRegOrNot () {
   	console.log("发送ajax,判断此号码是否注册过");
   	 $.ajax({
   	 	type:"GET",
        url:"http://182.92.179.84:81/lcb/userInformation/checkMobile.shtml?"+"&signature=c1bc53c77324069f0f949d23e710838d&timestamp=1427373821045",
   	 	contentType:"text/plain ; charset=UTF-8",
   	 	dataType:'json',
   	 	data:"mUserTel="+$("#reg-tel").val(),
   	 	success:function(result){
   	 		console.log("判断手机号是否注册返回结果:"+result.result+result.status);
   	 		if (result.result==true) {  
                alert("该手机号已经注册！");
   	 		} else{//该手机号未注册
   	 			 getRegCodeAjax();
   	 		}
   	 		
   	 	},
   	 	error:function(res){
   	 		console.log(res);
   	 		alert (res.status);
   	 	}
   	 })
   	 
   }
// var headStr =  "http://182.92.179.84:81";
//	var trailStr = "&signature=c1bc53c77324069f0f949d23e710838d&timestamp=1427373821045";
//	var checkApi = "/lcb/userInformation/checkMobile.shtml?";//检查手机号是否注册API
//	var validApi = "/lcb/sendSMS/sendValidCode.shtml?";      //发送验证码API
 //获取注册验证码
   function getRegCodeAjax() {
        console.log('获取注册验证码，发送ajax');
        getCodeActive($("#get-reg-code"));
        $.ajax({
            type: "GET",
            url: "http://182.92.179.84:81/lcb/sendSMS/sendValidCode.shtml?"+"&signature=c1bc53c77324069f0f949d23e710838d&timestamp=1427373821045",
            contentType: "text/plain; charset=UTF-8",
            dataType: 'json',
            data: "mobile=" + $("#reg-tel").val(),
            success: function (result) {
            	console.log("获取验证码接口返回结果:"+result.result+result.status);
                if (result.status == 0) {
                  
                   
                    return true;
                } else {
                    window.validCode = result.result;
                    return false;
                }
            },
            error: function (res) {
                if (res.status == 401) {


                }
            }
        })
    }

 //注册ajax
  function regAjax() {
//      var data = {
//          "mUserTel": $("#reg-tel").val(),
//          "mUserPwd": $("#reg-pwd").val(),
//          "inviteCode": $("#reg-code").val()

//var url = getPartyUrl() + "party_order.html?partyitemname=" + escape($orderValue) 
//					+ "&partyitemid=" + $partyitemid + "&partyorderprice=" + $orderprice + "&partyallprice=" + $allprice
//					+ "&source=" + $_GET["source"];
//      };
        $.ajax({
            type: "POST",
            url: "http://182.92.179.84:81/lcb/userInformation/saveMobileUserForIOS.shtml?mUserTel="+ $("#reg-tel").val()+"&mUserPwd="+$("#reg-pwd").val()+"&inviteCode="+ $("#reg-inviteWords").val()+"&signature=c1bc53c77324069f0f949d23e710838d&timestamp=1427373821045",
            contentType: "text/plain; charset=UTF-8",
            dataType: 'json',
            data:"",
            success: function (result) {
            	console.log("注册按钮返回结果:"+result.result+result.status);
            	if (result.status==2) {//邀请码有误
            		alert("您输入的邀请码错误！");
            	} else if (result.status == 1){
            		alert("恭喜您注册成功");
            	}else{
            		alert(result.message);
            	}

            },
            error: function (res) {
                if (res.status == 401) {
                    console.log('error');
                }
            }
        });
    }
    
