define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoNotificationCtrl', ['$scope','Notification',function ($scope,Notification) { 
       	  $scope.pager = Notification;

          $scope.clearForm = function(){
              $scope.keye = "";
          }
          $scope.edit = function(item){ //click on edit link
           	  $scope.keye = angular.copy(item);
          };
          $scope.create = function(item) { //edit
     		  if (item.notificationId){
     			  Notification.post({
     				  notificationId:item.notificationId,
     				  title:item.title,
     				  content:item.content,
     				  status:item.status
     				  }, function(result){
     	         		  if (result.success == "false"){
     	         			 alert(result.message);
     	         		  }
 	        		  //window.location.reload();
  	        		  $scope.clearForm();
 	                  $scope.refresh('current', true); // refresh listgrid
 	                  $('#addandedit').modal('hide');
 	              });
     		  } else {
     			 Notification.put(item, function(result){
	         		  if (result.success == "false"){
  	         			 alert(result.message);
  	         		  }
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
 	            	notificationId : $scope.todeleteId
 	   	        };
 	           Notification.remove({
 	                params : angular.toJson(params)
 	            }, function(jsonData) {
 	                $scope.refresh('current', true);
 	            });
         };

          
        }]);
    }
});