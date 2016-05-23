define(function (require, exports, module) {
    //理财产品发标
    return function setApp(app) {
        app.controller('PuhuibaoProductbidCtrl', ['$scope','ProductBid','$filter','AssetProduct','UserRedpacket','UserInvestment',function ($scope,ProductBid,$filter,AssetProduct,UserRedpacket,UserInvestment) { 
        //------------------gridlist---------
            $scope.pager = ProductBid;
          	$scope.params = {};
         	 $scope.params.orderBy = 'a.start_date';
          	 $scope.params.order = 'desc';
          	$scope.refresh && $scope.refresh('first', true);

          	AssetProduct.query({isArray:true,params:{}},function (list){ //for combo
          		$scope.products = list;
          	});
            
            $scope.investment = function(id) {
            	$scope.key = {};
            	$scope.key.bidSN = id;
            };
            $scope.next = function(muid) {
             	$('#next').modal('hide');
             	$('#investment').modal('show');
                UserRedpacket.query({isArray:true,'muid':muid},function (list){ //for combo
              		$scope.redpackets = list;
              	});
            }
            $scope.saveInvestment = function(item) {
            	redpacketId = '';
            	if (item.redpacket) {
                	redpacketId = item.redpacket.redpacketId;
            	}
            	UserInvestment.put({method:'adminSave','muid':$scope.muid,'bidSN':item.bidSN,'investmentAmount':item.investmentAmount,'redpacketId':redpacketId}, function(result){
            		alert(result.success);
 	         		if (result.success == "false"){
	         		    alert(result.message);
	         		    return;
 	         		}
                   	$scope.refresh('current',true);//refresh listgrid
                    $('#investment').modal('hide');
                });
            }
            //------------------add/edit--------- 	
            $scope.edit = function(item){ //click on edit link
            	var dateFormat = $filter('date');
 				if (dateFormat(new Date(), 'yyyy-MM-dd HH:mm:ss') > dateFormat(item.startDate, 'yyyy-MM-dd HH:mm:ss')) {
 					alert("已开标，不可修改！");
 					return;
 				}
             	$('#addandedit').modal('show');
              	$scope.keye = angular.copy(item);
            	$scope.keye.startDate = dateFormat(item.startDate, 'yyyy-MM-dd HH:mm:ss');
            	$scope.keye.endDate = dateFormat(item.endDate, 'yyyy-MM-dd HH:mm:ss');
              	AssetProduct.query({isArray:true,params:{productSN:item.productSN}},function (list){ //for combo
              		if (list.length > 0) {
                	    $scope.product = list[0];
              		}
              	});
            };
            $scope.clearForm = function(){ //reset botton
                $scope.keye={};
                $scope.product = "";
            };
            $scope.createSN = function(product){
            	var dateFormat = $filter('date');
                $scope.keye.bidSN = product.productSN + dateFormat(new Date, 'yyMMdd');
                $scope.keye.productSN = product.productSN;
            };
            $scope.create = function(item) {//add and edit
     			if(item.bidId){ // edit
	               	ProductBid.save(item,function(result){
     	         		if (result.success == "false"){
        	         		alert(result.message);
        	         		return;
        	         	}
	               		//window.location.reload();
                  		$scope.refresh('current',true);//refresh listgrid
                  		//$scope.clearForm();
                     	$('#addandedit').modal('hide');
                    });
                }else{ // add
	               	ProductBid.put(item,function(result){
     	         		if (result.success == "false"){
        	         		alert(result.message);
        	         		return;
        	         	}
                      	$scope.refresh('current',true);//refresh listgrid
                      	//$scope.clearForm();
                        $('#addandedit').modal('hide');
                    });
              	}
            };
        //-------------delete----------        
            $scope.todelete = function(item) {//click on DELETE link
            	var dateFormat = $filter('date');
 				if (dateFormat(new Date(), 'yyyy-MM-dd HH:mm:ss') > dateFormat(item.startDate, 'yyyy-MM-dd HH:mm:ss')) {
 					alert("已开标，不可删除！");
 					return;
 				}
				$('#removeDialog').modal('show');
                $scope.tobedeleteId = item.bidId;
            };
            $scope.comfirmDelete = function() {//confirm to delete on dialog
   	            var params = {
   	        		bidId : $scope.tobedeleteId
   	   	        };
   	            ProductBid.remove({
   	                params : angular.toJson(params)
   	            }, function(jsonData) {
   	                $scope.refresh('current', true);
   	            });
           };
       }]);
    };
});