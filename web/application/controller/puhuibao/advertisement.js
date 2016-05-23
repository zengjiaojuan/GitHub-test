define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoAdvertisementCtrl', ['$scope','Advertisement','$filter',function ($scope,Advertisement,$filter) { 
       	  $scope.pager = Advertisement;

          $scope.clearForm = function(){
        	  document.getElementById('image_upload').style.display = 'none';
              $scope.keye = "";
          }
          $scope.edit = function(item){ //click on edit link
        	  document.getElementById('image_upload').style.display = '';
           	  $scope.keye = angular.copy(item);
          	  var dateFormat = $filter('date');
        	  $scope.keye.startDate = dateFormat(item.startDate, 'yyyy-MM-dd HH:mm:ss');
        	  $scope.keye.endDate = dateFormat(item.endDate, 'yyyy-MM-dd HH:mm:ss');
          };
          $scope.create = function(item) { //edit
        	  if($scope.uploadImage.length>0){
        	      item.image = $scope.uploadImage[0].image;
        	  }
     		  if (item.adId){
 	        	  Advertisement.save(item, function(){
 	        		  //window.location.reload();
  	        		  $scope.clearForm();
 	                  $scope.refresh('current', true); // refresh listgrid
 	                  $('#addandedit').modal('hide');
 	              });
     		  } else {
   	        	  Advertisement.put(item, function(){
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
 	            	adId : $scope.todeleteId
 	   	        };
 	           Advertisement.remove({
 	                params : angular.toJson(params)
 	            }, function(jsonData) {
 	                $scope.refresh('current', true);
 	            });
         };

          
        }]);
    }
});