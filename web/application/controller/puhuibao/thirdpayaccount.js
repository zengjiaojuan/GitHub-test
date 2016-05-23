define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoThirdpayaccountCtrl', ['$scope','ThirdPayAccount',function ($scope,ThirdPayAccount) { 
     //------------------gridlist--------- 	 
       	 $scope.pager =  ThirdPayAccount;
      	 $scope.params = {};
      	 $scope.params.orderBy = 'pay_date';
      	 $scope.params.order = 'desc';

     //------------reset botton---------------------
            $scope.clearForm = function(){//reset botton
                $scope.pageitem="";
            }
            
     //------------------add/edit--------- ----------- 	
            $scope.preview =function(item){ //click on edit link
            	ThirdPayAccount.get(item, function(result){
 	         		  if (result.success == "false"){
 	         			 alert(result.message);
 	         		  } else {
 	         			$scope.keyv= result;
 	         		  }
	              });
            }; 
 
            $scope.check = function(params) {
            	var reg = /^(\d{4})-(\d{2})-(\d{2})$/
            	if (!reg.test(params.startdate) || !reg.test(params.enddate)) { 
            		alert("日期格式不正确!"); 
            		return false; 
            	} 
         	    var pathName = window.document.location.pathname;
        	    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/')+1);
   	        	window.location = projectName + "/userAccount/checkAccount.shtml?startdate=" + params.startdate + "&enddate=" + params.enddate;
	            // by ajax $scope.refresh('current', true); // refresh listgrid
            };
            $scope.checkRefund = function(params) {
            	var reg = /^(\d{4})-(\d{2})-(\d{2})$/
            	if (!reg.test(params.startdate) || !reg.test(params.enddate)) { 
            		alert("日期格式不正确!"); 
            		return false; 
            	} 
         	    var pathName = window.document.location.pathname;
        	    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/')+1);
   	        	window.location = projectName + "/userAccount/checkAccountRefund.shtml?startdate=" + params.startdate + "&enddate=" + params.enddate;
	            // by ajax $scope.refresh('current', true); // refresh listgrid
            };
//
            $scope.refund = function(entity) { // click on DELETE link
            	$scope.entity = {};
                $scope.entity.orderid = entity.orderid;
                $scope.entity.yborderid = entity.yborderid;
                $scope.entity.amount = entity.amount;
            };

            $scope.comfirmRefund = function() {
            	ThirdPayAccount.post($scope.entity, function(result){
       	         		  if (result.success == "false"){
       	         			 alert(result.message);
       	         		  }
   	        		  //window.location.reload();
    	        	  //$scope.clearForm();
   	                  $scope.refresh('current', true); // refresh listgrid
   	                  //$('#addandedit').modal('hide');
   	              });
            };
//
        }]);
    }

});