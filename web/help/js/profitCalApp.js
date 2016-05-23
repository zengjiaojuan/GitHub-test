$(function(){	
	verify();
	$(".doBlurCheck-ipt").blur(function(){verify();})
	$("input:radio[name='plan-rdo'],input:radio[name='planPe-rdo'],input:radio[name='loan-rdo']").click(function(){verify();})
	
})
//验证输入合法性
function verify(){
	var investAmount = $("#investAmount").val();
	var investTip = $("#investTip");
		if(investAmount == ''){
			investTip.html('投资金额请输入50万以内的数字').show();
			return;
		}
		if(isNaN(investAmount)){
			investTip.html('投资金额请输入50万以内的数字').show();
			return;
		}
		if(investAmount > 500000){
			investTip.html('投资金额请输入50万以内的数字').show();
			return;
		}
	if($('#proType').val() == 'loan')
	{
		var investTime = $("#investTime").val();
		if(investTime == ''){
			investTip.html('投资时长请输入60以内的数字').show();
			return;
		}
		if(isNaN(investTime)){
			investTip.html('投资时长请输入60以内的数字').show();
			return;
		}
		if(investTime > 60){
			investTip.html('投资时长请输入60以内的数字').show();
			return;
		}
		var anun = $("#anun").val();
		if(anun == ''){
			investTip.html('年化利率请输入24以内的数字').show();
			return;
		}
		if(isNaN(anun)){
			investTip.html('年化利率请输入24以内的数字').show();
			return;
		}
		if(anun > 24){
			investTip.html('年化利率请输入24以内的数字').show();
			return;
		}		
		 if($("input[name='loan-rdo']:checked").val() == 1){
			 //alert('复投')
				getData();
			}else{
			 //alert('不复投')
				getData2();
			}		
	}
	else{
		calcPlanProfit();
	}
	
}	
function initTip(){
    $("#investAmount").html('').hide();
    $("#investTimeTip").html('').hide();
    //$("#anunTip").html('').hide();
    //$("#monthResult").show();
   // $("#monthre").html(0);
    $("#totalmo").html(0);//利息收入
    $("#totalAunu").html(0);//本息合计
}

//创建月息通收益表格
function createLoanProfitTable(data){
    if($('#proType').val() == 'loan'){
        var to = 0,toIn = 0;
        var html = "<table><tr style='border-bottom:1px solid #E3E7E8'><th class='c1'>月份</th><th>本金余额(￥)</th><th>月收本金(￥)</th><th class='c4'>月收利息(￥)</th></tr></table>";
        if($("input[name='loan-rdo']:checked").val() == 1){
            for (var i = 0; i < data.length; i++) {
                to += data[i].plannedTermAmount * 1;
                toIn += data[i].plannedTermInterest * 1;
            }
            //$("#monthre").html(numFormat(data[0].plannedTermAmount,2));
            $("#totalmo").html(numFormat(to,2));
            $("#totalAunu").html(numFormat((to-Number($("#investAmount").val())),2));
            //$("#monthResult").show();
        }else{
            to = data[0].termRemainingPrincipal;
            for (var i = 0; i < data.length; i++) {
                to += data[i].plannedTermAmount * 1;
                toIn += data[i].plannedTermInterest * 1;
            }       
            $("#totalmo").html(numFormat(to,2));
            $("#totalAunu").html(numFormat((to-Number($("#investAmount").val())),2));
            //$("#monthResult").hide();
        }

        html += '<div class="timeline-line"><table><tr>';
        html += '<td class="c1"></td>';
        html += '<td>' + numFormat((Number(data[0].termRemainingPrincipal) + Number(data[0].plannedTermPrinciple)),2) + '</td>';
        html += '<td>' + numFormat(to,2) + '</td>';
        html += '<td class="c4"></td>';
        html += '</tr></table></div>';

        for(var i=0,len=data.length;i<len;i++){
            if($("#repayType").val() == 1){
                to -= data[i].plannedTermAmount * 1;
                toIn -= data[i].plannedTermInterest * 1;
                if(i == len-1){
                    to = 0;
                }
            }
            
            if (to < 0){
                to = 0;
            };
            if (toIn < 0){
                toIn = 0;
            };
            html += '<div class="timeline-line"><table><tr>';
            html += '<td class="c1">' + data[i].phaseNumber + '</td>';
            html += '<td>' + numFormat((Number(data[i].termRemainingPrincipal)),2) + '</td>';//本金余额
            //html += '<td>' + numFormat(to,2) + '</td>';//利息余额
            //html += '<td>' + numFormat((Number(data[i].plannedTermAmount)),2) + '</td>';//月收本息 (￥)
            html += '<td>' + numFormat((Number(data[i].plannedTermPrinciple)),2) + '</td>';//月收本金(￥)
            html += '<td class="c4">' + numFormat((Number(data[i].plannedTermInterest)),2) + '</td>';//利息(￥)
            html += '</tr></table></div>';
        }
        $("#timeline-show").html(html).slideDown();
    }
}

//等额本息
function getData() {
    var type = $.trim($("input[name='loan-rdo']:checked").val());
    var type=1;
    var amount = $.trim($("#investAmount").val());
    var reg = /^[0-9]*[1-9][0-9]*$/;
    var newPar = /^\d+(\.\d+)?$/;
    if (!reg.test(amount)) {
        return;
    };
    var ann = $.trim($("#anun").val());
    var count = $.trim($("#investTime").val());
    if (!newPar.test(ann)) {
        return;
    };
    if (!reg.test(count)) {
        return;
    };
    if (count > 120) {
        return;
    };
    //异步请求
	$.ajax({
		type: 'POST',
		url: "/global/getRepayPhaseDetail.do",
		data: {"repayType": type, "amount": amount, "termCount": count, "annualInterest":ann},
		success: function(data) {
			//错误等信息提示
			if(data.code < 0){
				alert(data.msg);
				return false;
			}
			createLoanProfitTable(data.data);
		}
	});//End...$.ajax
};

//利息复投
function getData2(){
    var amount = $.trim($("#investAmount").val());
    var reg = /^[0-9]*[1-9][0-9]*$/;
    var newPar = /^\d+(\.\d+)?$/;
    if (!reg.test(amount)) {
        return;
    };
    var ann = $.trim($("#anun").val());
    var count = $.trim($("#investTime").val());
    if (!newPar.test(ann)) {
        return;
    };
    if (!reg.test(count)) {
        return;
    };
    if (count > 120) {
        return;
    };

    function Data() {
        this.phaseNumber=0;

        this.plannedTermAmount=0;
        this.plannedTermInterest=0;
        this.plannedTermPrinciple=0;
        this.termRemainingPrincipal=0;
    }
    var monAnn = ann/(12*100);
    var dataArr = new Array();
    var temp = new Data();
    temp.phaseNumber = 1;
    temp.plannedTermAmount = (amount * monAnn).toFixed(2);
    temp.plannedTermInterest = temp.plannedTermAmount;
    temp.plannedTermPrinciple = Number(0);
    temp.termRemainingPrincipal = Number(amount);
    dataArr.push(temp);
    for(var i=1;i<count;i++){
        var temp = new Data();
        temp.phaseNumber = i+1;
        temp.plannedTermPrinciple = Number(0);
        temp.termRemainingPrincipal = Number(dataArr[i-1].termRemainingPrincipal) + Number(dataArr[i-1].plannedTermInterest);
        temp.plannedTermAmount = (temp.termRemainingPrincipal * monAnn).toFixed(2);
        temp.plannedTermInterest = temp.plannedTermAmount;
        dataArr.push(temp);
    }
    createLoanProfitTable(dataArr);
}

//创建定存宝收益表格
function createPlanProfitTable(data){
    var html = '<table><tr style="border-bottom:1px solid #E3E7E8"><th class="c1">月份</th><th>本金余额(￥)</th><th class="c3">收益(￥)</th></tr></table>';
    html += '<div class="timeline-line"><table><tr>';
    html += '<td class="c1">'+ data[0].phaseNumber +'</td>';
    html += '<td>'+ numFormat(data[0].principle,2) +'</td>';
    html += '<td class="c3">'+ data[0].profit +'</td>';
    html += '</tr></table></div>';
    
    for(var i=1,len=data.length;i<len;i++){
        html += '<div class="timeline-line"><table><tr>';
        html += '<td class="c1">'+ data[i].phaseNumber +'</td>';
        html += '<td>'+ numFormat(data[i].principle,2) +'</td>';
        html += '<td class="c3">'+ numFormat(data[i].profit,2) +'</td>';
        html += '</tr></table></div>';
    }
    $("#timeline-show").html(html).slideDown();
}

//定存宝
function calcPlanProfit(){
    var planType = $("input[name='plan-rdo']:checked").val();
//    var profitDis = $("input[name='planPe-rdo']:checked").val();
    var profitDis = 1;
    var investAmount = Number($("#investAmount").val());
    var anun = 0,lockedPeriod = 0;
    var anunForDisplay = 0;
    switch(planType){
        case "a": anun = 7.95; lockedPeriod=3; anunForDisplay=8; break;
        case "b": anun = 8.84; lockedPeriod=6; anunForDisplay=9; break;
        case "c": anun = 9.57; lockedPeriod=12; anunForDisplay=10; break;
        default:break;
    }

    function Data() {
        this.phaseNumber = 0;
        this.principle = 0;
        this.profit = 0;
    }
    var dataArr = new Array();
    if(profitDis == '1'){
        var profit = 0;
        var actualTotalProfit = 0;
        actualTotalProfit = round(investAmount*anunForDisplay*lockedPeriod/(100*12),2);
        var investAmountTotal = investAmount;
        for(var i = 0;i <= lockedPeriod;i++){
            var temp = new Data();
            temp.phaseNumber = i==0?"":i;
            investAmountTotal = round(investAmountTotal+profit,2);
            temp.principle = investAmountTotal.toFixed(2);
            if(i != lockedPeriod){
                profit = round(round(accMul(investAmountTotal,round((1+anun/(100*12)),8)),2) -investAmountTotal,2);
                temp.profit = i==0?"":0;
            }else{
            	temp.principle = round(investAmount+actualTotalProfit,2);
                temp.profit = i==0?"":(actualTotalProfit).toFixed(2);
            }
            dataArr.push(temp);
        }
        profit = investAmountTotal - investAmount;

        //$("#monthre").html(0);//每月收入
        $("#totalmo").html(numFormat(investAmount+actualTotalProfit,2));//本息合计
        $("#totalAunu").html(numFormat(actualTotalProfit,2));//利息收入
        //$("#monthResult").hide();//每月收款div
    }else if(profitDis == '2'){
        var profit = round(round(investAmount*round(anun/(12*100),8),2)*lockedPeriod,2);
        for(var i = 0;i <= lockedPeriod;i++){
            var temp = new Data();
            temp.phaseNumber = i==0?"":i;
            temp.principle = investAmount.toFixed(2);
            temp.profit = i==0?"":round(investAmount*round(anun/(12*100),8),2).toFixed(2);
            dataArr.push(temp);
        }
        //$("#monthre").html(numFormat(round(investAmount*round(anun/(12*100),8),2),2));
        $("#totalmo").html(numFormat((profit+investAmount),2));
        $("#totalAunu").html(numFormat(profit,2));
       // $("#monthResult").hide();
    }
    createPlanProfitTable(dataArr);
}

//四舍六入五成双
function round(num, digit){
    var ratio = Math.pow(10, digit),
//        _num = num * ratio,
        _num = accMul(num, ratio),
        mod = _num % 1,
        integer = Math.floor(_num);
    if(mod > 0.5){
        return (integer + 1) / ratio;
    }else if(mod < 0.5){
        return integer / ratio;
    }else{
        return (integer % 2 === 0 ? integer : integer + 1) / ratio;
    }
}

//乘法函数，用来得到精确的乘法结果
//说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
//调用：accMul(arg1,arg2)
//返回值：arg1乘以arg2的精确结果
function accMul(arg1,arg2){
	var m=0,s1=arg1.toString(),s2=arg2.toString();
	try{
	    m+=s1.split(".")[1].length;
	}catch(e){}
	try{
	    m+=s2.split(".")[1].length;
	}catch(e){}
	return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
}

function numFormat(s, n) {
    var t = '',
    r = '';
    var Str = function() {
        n = n > 0 && n <= 20 ? n: 2;
        s = parseFloat((s + '').replace(/[^\d\.-]/g, '')).toFixed(n) + '';
        var l = s.split('.')[0].split('').reverse();
        r = s.split('.')[1];
        for (var i = 0; i < l.length; i++) {
            t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? ',': '')
        }
    };
    if (n >= 0) {
        Str();
        return t.split('').reverse().join('') + '.' + r
    } else {
        Str();
        return t.split('').reverse().join('')
    }
};