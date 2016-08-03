define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoUseraccountCtrl', ['$scope','UserAccount',function ($scope,UserAccount) { 
     //------------------gridlist--------- 	 
       	 $scope.pager =  UserAccount;
      	 $scope.params = {};
      	 $scope.params.orderBy = 'a.create_time';
      	 $scope.params.order = 'desc';

     //------------reset botton---------------------
            $scope.clearForm = function(){//reset botton
                $scope.pageitem="";
            }
            
     //------------------add/edit--------- ----------- 	
            $scope.preview =function(item){ //click on edit link
        		$scope.keyv= angular.copy(item);
            };

            $scope.queryPay =function(orderId){
        		UserAccount.get({method:'queryTransaction', 'orderId':orderId},function (result){
         		    if (result.success == "false"){
	         			 alert(result.message);
	         		}
        			$scope.result = result;
              	});
            };
            $scope.queryRefund =function(orderId){
        		UserAccount.get({method:'queryRefund', 'orderId':orderId},function (result){
         		    if (result.success == "false"){
	         			 alert(result.message);
	         		}
        			$scope.result = result;
              	});
            };
            $scope.edit =function(tobeedit){ //click on edit link
              	$scope.pageitem = tobeedit;
            };
            $scope.create = function(item) {//add and edit
            	item.createTime = "";
                if (item.accountId) {
                	//item.method = 'update';
                	UserAccount.save({method:'update','accountId':item.accountId,'adminNote':item.adminNote,'adminUser':$scope.USER_INFO.id,'isPaid':1,'amount':item.amount},function(result){
     	         		if (result.success == "false"){
    	         		    alert(result.message);
    	         		    return;
     	         		}
                       	$scope.refresh('current',true);//refresh listgrid
                        $scope.clearForm();
                      	$('#edit').modal('hide');
                    });
               	}
//                else{
//               		item.adminUser = $scope.USER_INFO.userName;
//                	item.method = "adminCreate";
//               		UserAccount.put(item,function(result){
//     	         		if (result.success == "false"){
//    	         		    alert(result.message);
//    	         		    return;
//     	         		}
//                       	$scope.refresh('current',true);//refresh listgrid
//                       	//$scope.clearForm();
//                        $('#add').modal('hide');
//                    });
//               	}
            }
 
            
    //-------------delete----------        
            $scope.todelete = function(accountId) {//click on DELETE link
                $scope.tobedeleteId = accountId;
            };

 

//
            $scope.refund = function(orderId) {
                $scope.orderId = orderId;
            };

            $scope.comfirmRefund = function() {
            	UserAccount.post({method:'refund', 'orderId':$scope.orderId}, function(result){
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