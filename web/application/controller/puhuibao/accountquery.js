define(function(require, exports, module) {
	// 资金账目查询
	return function setApp(app) {
		app.controller('PuhuibaoAccountqueryCtrl', [ '$scope', 'AccountQuery',function($scope, AccountQuery) {
					//-------------------------------------------------------------------------
					//$scope.params = {};
					//-------------------------------------------------------------------------
					//-------------------------------------------------------------------------
//		            $scope.payInfomation =function(muserTel){ 
//		            	AccountQuer.query({method:'Query',params:{'mUserTel':mUserTel}},function (result){
//		         		    if (result.success == "false"){
//			         			 alert(result.message);
//			         		}
//		        			$scope.result = result;
//		              	});
//		            };
		            alert(muserTel)
		            $scope.select =function(muserTel){ 
		            	AccountQuer.query({method:'Query','mUserTel':mUserTel},function (result){
		         		    if (result.success == "false"){
			         			 alert(result.message);
			         		}
		        			$scope.result = result;
		              	});
		            };
		            
		           //$scope.params = {};
		           //$scope.refresh('current',true);
		            
//		            $scope.limitArr=function(item){
//		            	AccountQuer.query({method:'Query',params:{'mUserTel':mUserTel}},function (result){
//		         		    if (result.success == "false"){
//			         			 alert(result.message);
//			         		}
//		        			$scope.limitArr = result;
//		              	});
//		            };
		            

				} ]);
	};
});