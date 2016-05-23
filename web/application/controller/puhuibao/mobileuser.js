define(function (require, exports, module) {
    return function setApp(app) {
        app.controller('PuhuibaoMobileuserCtrl', ['$scope','MobileUser','ProductBid','UserRedpacket','UserInvestment','ItemInvestment','LoanItem',function ($scope,MobileUser,ProductBid,UserRedpacket,UserInvestment,ItemInvestment,LoanItem) { 
     //------------------gridlist--------- 	 
       	$scope.pager =  MobileUser;

     //------------reset botton---------------------
            $scope.clearForm = function(){//reset botton
                $scope.pageitem="";
            }
            
     //------------------add/edit--------- ----------- 	
            $scope.preview =function(item){ //click on edit link
        		$scope.keyv= angular.copy(item);
            }; 
            $scope.edit =function(tobeedit){ //click on edit link
              	$scope.pageitem = tobeedit;
            };     
            $scope.create = function(item) {//add and edit
                if (item.mUserId) {
                	var user = {};
                	user.mUserId = item.mUserId;
                	user.isAudit = item.isAudit;
                	MobileUser.save(user,function(){
                       	$scope.refresh('current',true);//refresh listgrid
                        $scope.clearForm();
                      	$('#audit').modal('hide');
                    });
               	}else{
                	item.createTime = "";
               		MobileUser.put(item,function(){
                       	$scope.refresh('current',true);//refresh listgrid
                       	//$scope.clearForm();
                        $('#addandedit').modal('hide');
                    });
               	}
            }
            $scope.adminCreate = function(item) {
                if (item.mUserId) {
                	item.createTime = "";
                	MobileUser.save(item,function(result){
     	         		if (result.success == "false"){
        	         		alert(result.message);
        	         		return;
        	         	}
                       	$scope.refresh('current',true);//refresh listgrid
                        $scope.clearForm();
                      	$('#addandedit').modal('hide');
                    });
               	}else{
                	item.method = "adminCreate";
               		MobileUser.put(item,function(result){
     	         		if (result.success == "false"){
        	         		alert(result.message);
        	         		return;
        	         	}
                       	$scope.refresh('current',true);//refresh listgrid
                       	//$scope.clearForm();
                        $('#addandedit').modal('hide');
                    });
               	}
            }
            $scope.update = function(item) {//add and edit
            	var user = {};
            	user.mUserId = item.mUserId;
            	user.mUserName = item.mUserName;
            	user.idNumber = item.idNumber;
            	MobileUser.save(user,function(){
                   	$scope.refresh('current',true);//refresh listgrid
                    $scope.clearForm();
                  	$('#edit').modal('hide');
                });
            }
            $scope.frozen = function(item) {
            	MobileUser.save({
            		mUserId:item.mUserId,
            		frozenMoney:item.mUserMoney
	       		},function(){
	               	$scope.refresh('current',true);//refresh listgrid
	            });
            }
            $scope.unfrozen = function(id) {
            	MobileUser.save({
            		mUserId:id,
            		frozenMoney:0
	       		},function(){
	               	$scope.refresh('current',true);//refresh listgrid
	            });
            }
            $scope.getChildren = function(id) {
            	$scope.limitArrParent = MobileUser.query({isArray:true, params:{
            		parentId : id
            	}});
            }
            
    //-------------delete----------        
            $scope.todelete = function(mUserId) {//click on DELETE link
                $scope.tobedeleteId = mUserId;
            };

            $scope.comfirmDelete = function() {//confirm to delete on dialog
	            var params = {
	            		mUserId : $scope.tobedeleteId
	            };
	            MobileUser.remove({
	                params : angular.toJson(params)
	            }, function(jsonData) {
	                $scope.refresh('current', true);
	            });
            };
//
            $scope.investment = function(muid) {
            	$scope.key = {};
                UserRedpacket.query({isArray:true,'muid':muid},function (list){ //for combo
              		$scope.redpackets = list;
              	});
                ProductBid.query({isArray:true,'muid':muid},function (list){ //for combo
              		$scope.bids = list;
              	});
                $scope.muid = muid;
            }
            $scope.saveInvestment = function(item) {
            	redpacketId = '';
            	if (item.redpacket) {
                	redpacketId = item.redpacket.redpacketId;
            	}
            	UserInvestment.put({method:'adminSave','muid':$scope.muid,'bidSN':item.bid.bidSN,'investmentAmount':item.investmentAmount,'redpacketId':redpacketId}, function(result){
 	         		if (result.success == "false"){
	         		    alert(result.message);
	         		    return;
 	         		}
                   	$scope.refresh('current',true);//refresh listgrid
                    $('#investment').modal('hide');
                });
            }
            $scope.itemInvestment = function(muid) {
            	$scope.key = {};
                UserRedpacket.query({isArray:true,'muid':muid},function (list){ //for combo
              		$scope.redpackets = list;
              	});
                LoanItem.query({isArray:true,'muid':muid},function (list){ //for combo
              		$scope.items = list;
              	});
                $scope.muid = muid;
            }
            $scope.saveItemInvestment = function(item) {
            	redpacketId = '';
            	if (item.redpacket) {
                	redpacketId = item.redpacket.redpacketId;
            	}
            	alert($scope.muid);
            	alert(item.item.loanId);
            	ItemInvestment.put({method:'adminSave','muid':$scope.muid,'loanId':item.item.loanId,'investmentAmount':item.investmentAmount,'redpacketId':redpacketId}, function(result){
 	         		if (result.success == "false"){
	         		    alert(result.message);
	         		    return;
 	         		}
                   	$scope.refresh('current',true);//refresh listgrid
                    $('#itemInvestment').modal('hide');
                });
            }
//
        }]);
    }
});