define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoUsermessageCtrl', ['$scope','UserMessage',function ($scope,UserMessage) { 
       	 $scope.pager = UserMessage;
      	 $scope.params = {};
      	 $scope.params.orderBy = 'create_time';
      	 $scope.params.order = 'desc';
        }]);
    }
});