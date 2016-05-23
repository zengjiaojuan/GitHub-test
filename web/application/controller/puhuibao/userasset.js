define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoUserassetCtrl', ['$scope','UserAsset','UserAccountLog',function ($scope,UserAsset,UserAccountLog) { 
     //------------------gridlist--------- 	 
       	 $scope.pager =  UserAsset;
     //------------reset botton---------------------
            $scope.clearForm = function() {//reset botton
                $scope.pageitem="";
            }

     //------------------add/edit--------- ----------- 	
            $scope.transactionList = function(id) {
            	$scope.pager2 =  UserAccountLog;
            	$scope.params2 = {mUserId:id};
           		//清缓存的，保证每次点击规则模板都能查到相应的数据集
           		setTimeout(function(){
           			$scope.refresh2 && $scope.refresh2('current2' , true); 	
           		},1);
            };
// 保留两位小数
            $scope.toFixed = function(value){
            	return value.toFixed(2);
            }
//
        }]);
    }
});