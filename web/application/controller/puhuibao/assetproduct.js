define(function (require, exports, module) {
    //理财产品管理
    return function setApp(app) {
        app.controller('PuhuibaoAssetproductCtrl', ['$scope','AssetProduct','$filter',function ($scope,AssetProduct,$filter) { 
        //------------------gridlist---------
            $scope.pager = AssetProduct;
          	$scope.params = {};
          	$scope.refresh && $scope.refresh('first', true);
        	var dateFormat = $filter('date');
        //------------------add/edit--------- 	
            $scope.edit = function(item){ //click on edit link
              	$scope.keye = angular.copy(item);
            	$scope.keye.startDate = dateFormat(item.startDate, 'yyyy-MM-dd HH:mm:ss');
            	$scope.keye.endDate = dateFormat(item.endDate, 'yyyy-MM-dd HH:mm:ss');
            };
            $scope.preview =function(item){
            	$scope.keyv = angular.copy(item);
            }; 
            $scope.clearForm = function(){//reset botton
                $scope.key="";
                $scope.keye="";
            };
            $scope.addByTemplate = function(item){ //click on edit link
              	$scope.key = angular.copy(item);
              	$scope.key.productId = null;
              	$scope.key.productSN = null;
            };
            $scope.create = function(item) {//add and edit
 				item.updateTime = null;
     			if(item.productId){ // edit
	               	AssetProduct.save(item,function(){
	               		//window.location.reload();
                  		$scope.refresh('current',true);//refresh listgrid
                  		//$scope.clearForm();
                     	$('#edit').modal('hide');
                    });
                }else{ // add
                  	item.startDate = dateFormat(item.startDate, 'yyyy-MM-dd HH:mm:ss');
                  	item.endDate = dateFormat(item.endDate, 'yyyy-MM-dd HH:mm:ss');
                	item.productId = 0;
	               	AssetProduct.put(item,function(){
                      	$scope.refresh('current',true);//refresh listgrid
                      	//$scope.clearForm();
                        $('#add').modal('hide');
                    });
              	}
            };
        //-------------delete----------        
            $scope.todelete = function(productId) {//click on DELETE link
                $scope.tobedeleteId = productId;
            };
            $scope.comfirmDelete = function() {//confirm to delete on dialog
   	            var params = {
   	        		productId : $scope.tobedeleteId
   	   	        };
   	            AssetProduct.remove({
   	                params : angular.toJson(params)
   	            }, function(jsonData) {
   	                $scope.refresh('current', true);
   	            });
           };
       }]);
    };
              
}); 
 


