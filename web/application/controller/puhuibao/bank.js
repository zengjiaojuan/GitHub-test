define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoBankCtrl', ['$scope','Bank',function ($scope,Bank) { 
       	  $scope.pager = Bank;

          $scope.clearForm = function(){
              $scope.keye = "";
          }
          $scope.edit = function(item){ //click on edit link
           	  $scope.keye = angular.copy(item);
          };
          $scope.create = function(item) { //edit
     		  if (item.bankId){
     			 Bank.save(item, function(){
  	        		  $scope.clearForm();
 	                  $scope.refresh('current', true); // refresh listgrid
 	                  $('#addandedit').modal('hide');
 	              });
     		  } else {
     			 Bank.put(item, function(){
  	        		  $scope.clearForm();
 	                  $scope.refresh('current', true); // refresh listgrid
 	                  $('#addandedit').modal('hide');
 	              });
     		  }
          };
          //-------------delete----------
          $scope.todelete = function(id) { // click on DELETE link
              $scope.todeleteId = id;
          };
          $scope.comfirmDelete = function() { // confirm to delete on dialog
 	            var params = {
 	            	bankId : $scope.todeleteId
 	   	        };
 	           Bank.remove({
 	                params : angular.toJson(params)
 	            }, function(jsonData) {
 	                $scope.refresh('current', true);
 	            });
         };

          
        }]);
    }
});