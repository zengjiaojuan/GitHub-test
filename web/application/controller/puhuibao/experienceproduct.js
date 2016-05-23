define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoExperienceproductCtrl', ['$scope','ExperienceProduct',function ($scope,ExperienceProduct) { 
       	  $scope.pager = ExperienceProduct;

          $scope.edit = function(item){ //click on edit link
           	  $scope.keye = angular.copy(item);
          };
          $scope.update = function(item) { //edit
        	  ExperienceProduct.save(item, function(){
                  $scope.refresh('current', true); // refresh listgrid
                  $('#edit').modal('hide');
              });
          };
        
        }]);
    }
});