define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoDaybalanceCtrl', ['$scope','DayBalance',function ($scope,DayBalance) {
        	DayBalance.get({method:'dayBalance'},function (result){
     		    if (result.success == "false"){
         			 alert(result.message);
         		}
    			$scope.item = result;
          	});
     //------------reset botton---------------------
            $scope.clearForm = function() {//reset botton
                $scope.pageitem="";
            }
//
        }]);
    }
});