define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoBorrowermanagementCtrl', ['$scope','BorrowerManagement','$filter',function ($scope,BorrowerManagement,$filter) { 
     //------------------gridlist--------- 	 
       	 $scope.pager =  BorrowerManagement;
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
         
         
         
         $scope.creatematching = function() { 
        	 alert(1);
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