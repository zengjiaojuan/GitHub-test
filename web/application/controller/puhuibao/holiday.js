define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoHolidayCtrl', ['$scope','Holiday','$filter',function ($scope,Holiday,$filter) { 
       	  $scope.pager = Holiday;
       	  $scope.params = {};
      	  $scope.params.orderBy = 'holiday_date';
      	  $scope.params.order = 'desc';

          $scope.clearForm = function(){
              $scope.keye = "";
          }
          $scope.create = function(item) { //edit
     		  if (item.HolidayId){
     		  } else {
     			 Holiday.put(item, function(){
  	        		  $scope.clearForm();
 	                  $scope.refresh('current', true); // refresh listgrid
 	                  $('#addandedit').modal('hide');
 	              });
     		  }
          };
          //-------------delete----------
          $scope.todelete = function(id) { // click on DELETE link
          	  var dateFormat = $filter('date');
        	  id = dateFormat(id, 'yyyy-MM-dd');
              $scope.todeleteId = id;
          };
          $scope.comfirmDelete = function() { // confirm to delete on dialog
 	            var params = {
 	            	holidayDate : $scope.todeleteId
 	   	        };
 	           Holiday.remove({
 	                params : angular.toJson(params)
 	            }, function(jsonData) {
 	                $scope.refresh('current', true);
 	            });
         };

          
        }]);
    }
});