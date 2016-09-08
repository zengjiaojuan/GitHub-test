 $(function () {      
       
//     var investmentDeadline = document.getElementById("item-date");    //投资期限
//		var annualRate = document.getElementById("item-rate");            //年化收益率
//		var backWay = document.getElementById("item-backWay");            //还款方式
//		var loanDesc = document.getElementById("item-desc");              //借款描述
//		var basicInfo = document.getElementById("item-nameAndAge");       //基本信息 姓名 年龄
//		var workUnitInfo = document.getElementById("item-workInfo");      //单位信息
//		var workAdd = document.getElementById("item-workAddress");        //工作地点
//		var monthlyIncome = document.getElementById("item-monthlyIncome");//月均收入
//		var marriage = document.getElementById("item-marriage");          //婚育状况
//		var homeAdd = document.getElementById("item-homeAddress");        //户籍地
//		var idCard = document.getElementById("img-idcard");               //身份证
//		var loanContract = document.getElementById("img-loanContract");   //借款合同
		
		
//		investmentDeadline.textContent = "12个月";
//		annualRate.textContent = "18"+"%"; //需要把小数转换成百分数
		backWay.textContent = "到期还本付息";
		loanDesc.textContent = "本项目借款用于个人资金周转。借款人工作稳定，资产丰厚，还款来源于个人工资收入，信用较好，有足够偿还能力。为保障预期收回借款，本债权以其自有的宝马轿车设定质押，市场估价为50万元，高于借款金额。";
		basicInfo.textContent = "鹿晗"+" 23"+"岁";
		workUnitInfo.textContent = "文化传媒";
		workAdd.textContent = "中国";
		monthlyIncome.textContent = "500000.00"+"元";
		marriage.textContent = "未婚";
		homeAdd.textContent = "北京市海淀区";
		idCard.src = "img/3.jpg";
		loanContract.src = "img/2.jpg";
		
     function getDetailAjax(){
     	 $.ajax({
            type: "POST",
            url: "",
            contentType: "text/plain; charset=UTF-8",
            dataType: 'json',
            data:"",
            success: function (result) {
                if (result.status == 0) {
                  //投资期限
                  $("#item-date").text(result.period);
                  //年化收益率
                  $("#item-rate").text(result.);
                  //还款方式
                  $("#item-backWay").text(result.payment_method);
                  //借款描述
                  $("#item-desc").text(result.product_desc);
                  //基本信息 姓名 年龄
                  $("#item-nameAndAge").text(result.borrower_name+" "+result.borrower_age+"岁");
                  //单位信息
                  $("#item-workInfo").text(result.borrower_job);         
                  //户籍地
                  $("#item-homeAddress").text(result.borrower_province);
                  //身份证
//                $("#img-idcard").src(result.)
                  //借款合同
                  
                    return true;
                } else {
                  
                    return false;
                }
            },
            error: function (res) {
                if (res.status == 401) {
                    console.log('error');
                }
            }
        });
     };
});
