define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoVersionCtrl', ['$scope','Version','$filter',function ($scope,Version,$filter) { 
       	  $scope.pager = Version;

          $scope.clearForm = function(){
              $scope.keye = "";
          }
          $scope.edit = function(item){ //click on edit link
           	  $scope.keye = angular.copy(item);
          	  var dateFormat = $filter('date');
        	  $scope.keye.startDate = dateFormat(item.startDate, 'yyyy-MM-dd HH:mm:ss');
        	  $scope.keye.endDate = dateFormat(item.endDate, 'yyyy-MM-dd HH:mm:ss');
          };
          $scope.create = function(item) { //edit
     		  if (item.versionId){
     			 Version.save(item, function(){
 	        		  //window.location.reload();
  	        		  $scope.clearForm();
 	                  $scope.refresh('current', true); // refresh listgrid
 	                  $('#addandedit').modal('hide');
 	              });
     		  } else {
     			 Version.put(item, function(){
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
 	            	versionId : $scope.todeleteId
 	   	        };
 	           Version.remove({
 	                params : angular.toJson(params)
 	            }, function(jsonData) {
 	                $scope.refresh('current', true);
 	            });
         };

          
        }]);
    }
});