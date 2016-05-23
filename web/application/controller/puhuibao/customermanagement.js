define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoCustomermanagementCtrl', ['$scope','CustomerManagement','ContractManagement','$filter',function ($scope,CustomerManagement,ContractManagement,$filter) { 
     //------------------gridlist--------- 	 
       	 $scope.pager =  CustomerManagement;
      	 $scope.params = {};
      	 $scope.params.orderBy = 'create_time';
      	 $scope.params.order = 'desc';
      	 
      	 
      	 
         $scope.producttype=[
                           {productid:'1',productname:'郎月赢'}, 
                           {productid:'2',productname:'单季宝'}, 
                           {productid:'3',productname:'双季宝'},
                           {productid:'4',productname:'金元宝'},
                           {productid:'5',productname:'泰晟宝A'},
                           {productid:'6',productname:'泰晟宝B'},
                           {productid:'7',productname:'年年红'}
                           ];
         $scope.investtype=[
                             {investid:'1',investname:'首次投资'},
                             {investid:'2',investname:'续投'}
                           ];
         $scope.obligatoryright=[
                            {acceptid:'1',acceptname:'两者都选'},
                            {acceptid:'2',acceptname:'电子邮件'},
                            {acceptid:'3',acceptname:'信件'}
                          ];
         
         $scope.contractIsarchived=[
	                                 {archivedid:'0',archivedname:'否'},
	                                 {archivedid:'1',archivedname:'是'} 
                                   ];
         
         $scope.usergender=[
                            {genderid:'0',gendername:'女'},
                            {genderid:'1',gendername:'男'} 
                           ];
         $scope.managerdep=[
                            {depid:'1',depname:'北京'},
                            {depid:'2',depname:'本溪一部'},
                            {depid:'3',depname:'本溪二部'},
                            {depid:'4',depname:'张家口'} 
                           ];
         
         $scope.savecontract = function(item) { 
        	 item.userId = $scope.tobeAdduserId;
         		ContractManagement.put(item,function(){
                 	$scope.refresh('current',true);
//                     $scope.pageitem="";
                     
                     $('#addcontract').modal('hide');
                 });
         	  };  
         
         
         $scope.save = function(pageitem) { 
        	 
        	 CustomerManagement.put(pageitem,function(){
                 	$scope.refresh('current',true);
//                     $scope.pageitem="";
                     
                     $('#addandedit').modal('hide');
                 });
         	  };  
     //------------reset botton---------------------
            $scope.clearForm = function(){//reset botton
                $scope.pageitem="";
            }
            
     //------------------add/edit--------- ----------- 	
            $scope.preview =function(item){ //click on edit link
        	 
        		$scope.key= item;
            	 
            	 
            };
            $scope.newcontract = function(id) {//click on DELETE link
                $scope.tobeAdduserId = id;
            }; 
            
             
            //-------------delete----------        
            $scope.todelete = function(id) {//click on DELETE link
                $scope.tobedeleteId = id;
            };
            $scope.comfirmDelete = function() {//confirm to delete on dialog
   	            var newcm = {
   	            		userId : $scope.tobedeleteId,
   	            		isDeleted : 1
   	   	        };
   	         CustomerManagement.save(newcm,function(){
             	$scope.refresh('current',true);
            	     $('#addandedit').modal('hide');
            });
   	  
   	            
   	            
            };

           
           


        }]);
    }
});