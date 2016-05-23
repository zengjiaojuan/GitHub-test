define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoFeedbackCtrl', ['$scope','Feedback',function ($scope,Feedback) { 
       	 $scope.pager = Feedback;
      	 $scope.params = {};
      	 $scope.params.orderBy = 'create_time';
      	 $scope.params.order = 'desc';
        }]);
    }
});