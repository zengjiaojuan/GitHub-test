define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoUserloanCtrl', ['$scope','UserLoan','MobileUserLoan','MobileUser','LoanItem','UserRedpacket','ItemInvestment','$filter',function ($scope,UserLoan,MobileUserLoan,MobileUser,LoanItem,UserRedpacket,ItemInvestment,$filter) { 
     //------------------gridlist--------- 	 
       	 $scope.pager =  UserLoan;
      	 $scope.params = {};
      	 $scope.params.orderBy = 'create_time';
      	 $scope.params.order = 'desc';

     //------------reset botton---------------------
            $scope.clearForm = function(){//reset botton
                $scope.useritem="";
            }
            
     //------------------add/edit--------- ----------- 	
            $scope.preview =function(item){ //click on edit link
        		//$scope.keyv= angular.copy(item);
        		$scope.keyv= item;
            	MobileUser.query({isArray:true,params:{mUserId:item.mUserId}},function (list) {
            		if (list.length == 1) {
                    	$scope.mobileUser = list[0];
            		} else {
            			$scope.mobileUser = {};
            		}
                });
            	LoanItem.query({isArray:true,params:{loanId:item.loanId}},function (list) {
            		if (list.length == 1) {
                    	$scope.loanItem = list[0];
            		} else {
            			$scope.loanItem = {};
            		}
                });
            };
            $scope.approve =function(item){ //click on edit link
            	UserLoan.get({method:"get",id:item.loanId},function (result) {
            		//alert(item.loanId);
            		//alert(angular.toJson(result));
            		if (angular.toJson(result) == "{}") {
            			$scope.loan = {}
            			$scope.loan.loanId = item.loanId;
            			$scope.loan.status = item.status;
            		} else {
                    	$scope.loan = result;
            			$scope.loan.totalAmount = result.amount;
            			$scope.loan.period = result.period;
                    	var dateFormat = $filter('date');
                    	$scope.loan.giveDate = dateFormat(result.giveDate, 'yyyy-MM-dd');
            		}
                });
            	LoanItem.get({method:"unique",params:{loanId:item.loanId}},function (result) {
                	var dateFormat = $filter('date');
            		if (angular.toJson(result) == "{}") {
//            			$scope.loanItem = {}
                        $scope.loan.itemSN = 'L' + dateFormat(new Date, 'yyMMdd');
            			$scope.loan.paymentMethod = "到期还本，每月付息";
            			$scope.loan.guaranteeMethod = "100%本息担保";
            		} else {
//                    	$scope.loanItem = result;
                        $scope.loan.itemId = result.itemId;
                        $scope.loan.itemSN = result.itemSN;
            			$scope.loan.totalAmount = result.totalAmount;
            			$scope.loan.period = result.period;
            			$scope.loan.paymentMethod = result.paymentMethod;
            			$scope.loan.guaranteeMethod = result.guaranteeMethod;
            			$scope.loan.investmentAmountMin = result.investmentAmountMin;
            			$scope.loan.annualizedRate = result.annualizedRate;
            			$scope.loan.itemDesc = result.itemDesc;
                    	$scope.loan.startDate = dateFormat(item.startDate, 'yyyy-MM-dd');
                    	$scope.loan.endDate = dateFormat(item.endDate, 'yyyy-MM-dd');
            		}
                });
//            	LoanItem.query({isArray:true,params:{loanId:item.loanId}},function (list) {
//            		if (list.length == 1) {
//                    	$scope.loan = list[0];
//            		} else {
//            			$scope.loan = {};
//            			$scope.loan.loanId = item.loanId;
//                    	var dateFormat = $filter('date');
//                      $scope.loan.itemSN = 'L' + dateFormat(new Date, 'yyMMdd');
//            			$scope.loan.totalAmount = item.amount;
//            			$scope.loan.period = item.period;
//            			$scope.loan.paymentMethod = "到期还本，每月付息";
//            			$scope.loan.guaranteeMethod = "100%本息担保";
//            			$scope.loan.status = item.status;
//            		}
//                });

              	//$scope.loan = angular.copy(item);
            	//var dateFormat = $filter('date');
            	//$scope.loan.giveDate = dateFormat(item.giveDate, 'yyyy-MM-dd HH:mm:ss');
            };
            $scope.audit = function(mUserId, loanId){
            	MobileUserLoan.query({isArray:true,params:{mUserId:mUserId}},function (list) {
            		if (list.length == 1) {
                    	$scope.useritem = list[0];
            		} else {
            			$scope.useritem = {};
            		}
                	$scope.useritem.mUserId=mUserId;
                	$scope.useritem.loanId=loanId;
                });
            	MobileUser.query({isArray:true,params:{mUserId:mUserId}},function (list) {
                	$scope.mobileUser = list[0];
                });
            };
            
//            $scope.setStatus = function(status){
//                $scope.useritem.status = status;
//            };
//            $scope.give = function(loanId) {
//    			$scope.loan = {};
//    			$scope.loan.loanId = loanId;
//            }
//            $scope.updateGive = function(item) {
//	            UserLoan.save({
//	       			loanId:item.loanId,
//	       			giveDate:item.giveDate,
//	       			status:2
//	       		},function(result){
//	               	$scope.refresh('current',true);//refresh listgrid
//	               	//$scope.clearForm();
//	                $('#give').modal('hide');
//	            });
//            }

            $scope.create = function(item) {//add and edit
           		MobileUserLoan.put(item,function(){
                   	$scope.refresh('current',true);//refresh listgrid
                   	//$scope.clearForm();
                    $('#audit').modal('hide');
                });
//           		UserLoan.save({
//           			loanId:item.loanId,
//           			status:item.status
//           		},function(){
//                   	$scope.refresh('current',true);//refresh listgrid
//                   	//$scope.clearForm();
//                    $('#audit').modal('hide');
//                });
            }
            $scope.approveUpdate = function(item) {
//            	var status = 0;
//            	if (item.totalAmount > 0 && item.rate > 0 && item.annualizedRate > 0) {
//            		status = 1;
//            	}
            	if (item.status == 1) {
                	if (item.totalAmount <= 0 || item.rate <= 0 || item.annualizedRate <= 0 || item.investmentAmountMin <= 0 || item.period <= 0) {
                		alert("放款额度、贷款利率、年化利率、最小投资额、周期必须大于0");
	            	}
            	}
            	UserLoan.save({
            		loanId:item.loanId,
            		rate:item.rate,
            		giveDate:item.giveDate,
	       			status:item.status
            	},function(result){
 	         		if (result.success == "false"){
	         		    alert(result.message);
	         		    return;
 	         		}
                   	$scope.refresh('current',true);
                   	//$scope.clearForm();
                    $('#approve').modal('hide');
                });
//            	LoanItem.get({method:"unique",params:{itemSN:item.itemSN}},function (result) {
//            		if (angular.toJson(result) == "{}") {
//            		} else {
//            			item.itemId = result.itemId;
//            		}
//                });
            	if (item.status == 0) {
                   	$scope.refresh('current',true);
                    $('#approve').modal('hide');
                    alert("审查通过 ，项目信息才能保存！");
                	return;
            	}
            	if (item.itemId) { // edit
	            	LoanItem.save({
	            		itemId:item.itemId,
	            		loanId:item.loanId,
	            		itemSN:item.itemSN,
	            		totalAmount:item.totalAmount,
	            		annualizedRate:item.annualizedRate,
	            		paymentMethod:item.paymentMethod,
	            		guaranteeMethod:item.guaranteeMethod,
	            		investmentAmountMin:item.investmentAmountMin,
	            		period:item.period,
	            		itemDesc:item.itemDesc,
	            		startDate:item.startDate,
	            		endDate:item.endDate,
		       			status:0
	            	},function(result){
	 	         		if (result.success == "false"){
	    	         		alert(result.message);
	    	         		return;
	    	         	}
	                   	$scope.refresh('current',true);
	                   	//$scope.clearForm();
	                    $('#approve').modal('hide');
	                });
	        	} else { // add
	            	LoanItem.put({
	            		loanId:item.loanId,
	            		itemSN:item.itemSN,
	            		totalAmount:item.totalAmount,
	            		annualizedRate:item.annualizedRate,
	            		paymentMethod:item.paymentMethod,
	            		guaranteeMethod:item.guaranteeMethod,
	            		investmentAmountMin:item.investmentAmountMin,
	            		period:item.period,
	            		itemDesc:item.itemDesc,
	            		startDate:item.startDate,
	            		endDate:item.endDate,
		       			status:0
	            	},function(result){
	 	         		if (result.success == "false"){
	    	         		alert(result.message);
	    	         		return;
	    	         	}
	            		//window.location.reload();
	                   	$scope.refresh('current',true);
	                   	//$scope.clearForm();
	                    $('#approve').modal('hide');
	                });
	        	}
            }

            //-------------delete----------        
            $scope.todelete = function(id) {//click on DELETE link
                $scope.tobedeleteId = id;
            };
            $scope.comfirmDelete = function() {//confirm to delete on dialog
   	            var params = {
   	        		loanId : $scope.tobedeleteId
   	   	        };
   	            UserLoan.remove({
   	                params : angular.toJson(params)
   	            }, function(jsonData) {
   	                $scope.refresh('current', true);
   	            });
            };

            $scope.investment = function(id) {
            	$scope.key = {};
            	$scope.key.loanId = id;
            };
            $scope.next = function(muid) {
             	$('#next').modal('hide');
             	$('#itemInvestment').modal('show');
                UserRedpacket.query({isArray:true,'muid':muid},function (list){ //for combo
              		$scope.redpackets = list;
              	});
            }
            $scope.saveInvestment = function(item) {
            	redpacketId = '';
            	if (item.redpacket) {
                	redpacketId = item.redpacket.redpacketId;
            	}
            	ItemInvestment.put({method:'adminSave','muid':$scope.muid,'loanId':item.loanId,'investmentAmount':item.investmentAmount,'redpacketId':redpacketId}, function(result){
 	         		if (result.success == "false"){
	         		    alert(result.message);
	         		    return;
 	         		}
                   	$scope.refresh('current',true);//refresh listgrid
                    $('#itemInvestment').modal('hide');
                });
            }


        }]);
    }
});