define(function (require, exports, module) {
	 return function setApp(app) {	     
	        	  app.controller('PuhuibaoApkUploadCtrl', ['$scope','ApkUpload','$filter',function ($scope,ApkUpload,$filter) { 
	        	        //------------------gridlist---------
	        	            $scope.pager = ApkUpload;
	        	          	$scope.params = {};
	        	          	$scope.refresh && $scope.refresh('first', true);  
	        	  
	        	        //------------------add/edit--------- 	
	        	          $scope.edit = function(item){ //click on edit link
	        	              	$scope.keye = angular.copy(item);
	        	            	$scope.keye.startDate = dateFormat(item.startDate, 'yyyy-MM-dd');
	        	            	$scope.keye.endDate = dateFormat(item.endDate, 'yyyy-MM-dd');
	        	            };
	        	            $scope.preview =function(item){
	        	            	$scope.keyv = angular.copy(item);
	        	            }; 
	        	            $scope.clearForm = function(){//reset botton
	        	                $scope.key="";
	        	                $scope.keye="";
	        	            };
	        	            $scope.addByTemplate = function(item){ //click on edit link
	        	              	$scope.key = angular.copy(item);
	        	              	$scope.key.productId = null;
	        	              	$scope.key.productSN = null;
	        	            };
	        	            $scope.preUpload=function(keye) {
	        	                 console.log('获取所上传apk的信息，发送ajax');
	        	                 $.ajax({
	        	                     type: "GET",
	        	                     url: "http://localhost:8080/lcb/apkUpload/uploadApk.shtml?"+"&signature=c1bc53c77324069f0f949d23e710838d&timestamp=1427373821045",
	        	                     contentType: "text/plain; charset=UTF-8",
	        	                     dataType: 'json',
	        	                     data: "describe=" + $("#describe-Content").val()+"&apk="+$("#apk-content").val(),
	        	                     success: function (result) {	        	                     
	        	                         if (result.status ==1) {
	        	                        	 item=result.apk;
	        	                        	 $scope.create(item);
	        	                        	 alert("上传成功！");
	        	                             return true;
	        	                         } else {
	        	                             window.validCode = result.result;
	        	                             return false;
	        	                         }
	        	                     },
	        	                     error: function (res) {
	        	                         if (res.status == 401) {


	        	                         }
	        	                     }
	        	                 })
	        	             }

	        	            $scope.create = function(item) {//add and edit
	        	     			if(item.id){ 
	        		               	ApkUpload.save(item,function(){
	        	                  		$scope.refresh('current',true);//refresh listgrid
	        	                     	$('#edit').modal('hide');
	        	                    });
	        	                }else{ 
	        		               	ApkUpload.put(item,function(){
	        		               		
	        	                      	$scope.refresh('current',true);//refresh listgrid
	        	                      	//$scope.clearForm();
	        	                        $('#add').modal('hide');
	        	                    });
	        	              	}
	        	            };
	        	        //-------------delete----------        
	        	           $scope.todelete = function(productId) {//click on DELETE link
	        	                $scope.tobedeleteId = productId;
	        	            };
	        	            $scope.comfirmDelete = function() {//confirm to delete on dialog
	        	   	            var params = {
	        	   	        		id : $scope.tobedeleteId
	        	   	   	        };
	        	   	            ApkUpload.remove({
	        	   	                params : angular.toJson(params)
	        	   	            }, function(jsonData) {
	        	   	                $scope.refresh('current', true);
	        	   	            });
	        	           };
	        	       }]);
	        	    };//set app
	        	              
	        	});//module 
	        	 