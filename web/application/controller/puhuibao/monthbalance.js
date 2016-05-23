define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoMonthbalanceCtrl', ['$scope','MonthBalance',function ($scope,MonthBalance) { 
     //------------------gridlist--------- 	 
       	 $scope.pager =  MonthBalance;
      	 $scope.params = {};
      	 $scope.params.orderBy = 'yyyymm';
      	 $scope.params.order = 'desc';
     //------------reset botton---------------------
            $scope.clearForm = function() {//reset botton
                $scope.pageitem="";
            }
//
        }]);
    }
});