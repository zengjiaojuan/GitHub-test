define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoCreditratioCtrl', ['$scope','CreditMatching','$filter','$timeout','$http',function ($scope,CreditMatching,$filter,$timeout,$http) { 
     //------------------gridlist--------- 	 
       	 $scope.pager =  CreditMatching;
      	 $scope.params = {};
      	 $scope.params.orderBy = 'create_time';
      	 $scope.params.order = 'desc';
      
      	 
         $scope.borrowerjob=[
                           {jobid:'1',jobname:'私营业主'}, 
                           {jobid:'2',jobname:'工薪'}, 
                           {jobid:'3',jobname:'企业高管'} 
                           ];
         $scope.borrowerwarrant=[
                             {warrantid:'1',warrantname:'质贷'} 
                              
                           ];
         $scope.borrowerusage=[
                            {usageid:'1',usagename:'扩大经营'},
                            {usageid:'2',usagename:'资金周转'},
                            {usageid:'3',usagename:'个人消费'}
                          ];
         
         
         
         $scope.saveRatio = function() { 
        	 if($scope.cantsave=1){
        		 $scope.createErrorMessage="已经保存!";
      			$timeout(function(){
        			$scope.createErrorMessage ="";
        			
           	    } , 3000);
      			return;
        	 }
        	 for(var i=0;i<$scope.limitArr.length;i++){
            	 CreditMatching.put($scope.limitArr[i],function(){
 
                   });
            	 $scope.createErrorMessage = '保存成功！';
        		 
        	 }

      	  };  
      	  
      	 var pathName = window.document.location.pathname;
  	     var projectName = pathName.substring(0, pathName.substr(1).indexOf('/')+1);
  	     var sumurl = projectName + "/customerManagement/getSum.shtml?&signature=c1bc53c77324069f0f949d23e710838d&timestamp=1427373821045";
        	 
      	 $http.get(sumurl)
         .success(function(response) {
        	 $scope.sum = response.sumstring;
        	 $scope.count = response.countstring;
        	 });	 
      
         
         $scope.creatematching = function() { 
         	
             var sum =3400000

 	         var url = projectName + "/creditMatching/getRatio.shtml?&signature=c1bc53c77324069f0f949d23e710838d&timestamp=1427373821045&sum="+sum;
         	
 	         
 	        $http({
				method : 'post',
				url :  url
			}).success(function(result) {
     			//错误等信息提示
     			if(result.status == 0){
     				$scope.createErrorMessage = result.message;
     			}else if(result.status == 2){//已经产生过了
     				$scope.createErrorMessage = result.message;
     				$scope.cantsave=1;
     			}else{
     				$scope.limitArr = result.result;
  
     			}
     			$timeout(function(){
        			$scope.createErrorMessage ="";
        			
           	    } , 3000);
     			 
     		});
 	         
  
         }
         
         
         $scope.save = function(pageitem) { 
        	 var dateFIlter = $filter('date'); 
        	 pageitem.contractStartdate  = dateFIlter(pageitem.contractStartdate, 'yyyy-MM-dd');
        	 pageitem.contractExpiredate = dateFIlter(pageitem.contractExpiredate, 'yyyy-MM-dd');
        	 pageitem.contractAmount = pageitem.contractAmount*10000;
        	 BorrowerManagement.put(pageitem,function(){
                 	$scope.refresh('current',true);
//                     $scope.pageitem="";
                     
                     $('#addandedit').modal('hide');
                 });
         	  };  
 
            $scope.clearForm = function(){//reset botton
                $scope.pageitem="";
            }
            
     
            $scope.deletec = function(id) {//confirm to delete on dialog
            	 var params = {
            			 recordId : id
        	   	        };
            	 BorrowerManagement.remove({
        	                params : angular.toJson(params)
        	            }, function(jsonData) {
        	                $scope.refresh('current', true);
        	            });
            };
             
 

           
           


        }]);
    }
});