define(function (require, exports, module) {
    //理财产品发标
    return function setApp(app) {
        app.controller('PuhuibaoProductbidCtrl', ['$scope','ProductBid','$filter','AssetProduct','UserRedpacket','UserInvestment',function ($scope,ProductBid,$filter,AssetProduct,UserRedpacket,UserInvestment) { 
        //------------------gridlist---------
            $scope.pager = ProductBid;
          	$scope.params = {};
         	 $scope.params.orderBy = 'a.start_date';
          	 $scope.params.order = 'desc';
          	$scope.refresh && $scope.refresh('first', true);
          	var dateFormat = $filter('date');
          	AssetProduct.query({isArray:true,params:{}},function (list){ //for combo
          		$scope.products = list;
          	});
            
            $scope.investment = function(id) {
            	$scope.key = {};
            	$scope.key.bidSN = id;
            };
            $scope.next = function(muid) {
             	$('#next').modal('hide');
             	$('#investment').modal('show');
                UserRedpacket.query({isArray:true,'muid':muid},function (list){ //for combo
              		$scope.redpackets = list;
              	});
            }
            $scope.saveInvestment = function(item) {
            	redpacketId = '';
            	if (item.redpacket) {
                	redpacketId = item.redpacket.redpacketId;
            	}
            	UserInvestment.put({method:'adminSave','muid':$scope.muid,'bidSN':item.bidSN,'investmentAmount':item.investmentAmount,'redpacketId':redpacketId}, function(result){
            		alert(result.success);
 	         		if (result.success == "false"){
	         		    alert(result.message);
	         		    return;
 	         		}
                   	$scope.refresh('current',true);//refresh listgrid
                    $('#investment').modal('hide');
                });
            }
            //------------------add/edit--------- 	
            $scope.edit = function(item){ //click on edit link
 				if (item.status!=0) {
 					alert("已开标，不可修改！");
 					return;
 				}
             	$('#addandedit').modal('show');
              	$scope.keye = angular.copy(item);
              	AssetProduct.query({isArray:true,params:{productSN:item.productSN}},function (list){ //for combo
              		if (list.length > 0) {
                	    $scope.product = list[0];
              		}
              	});
            };
            $scope.clearForm = function(){ //reset botton
                $scope.keye={};
                $scope.product = "";
            };
            $scope.createSN = function(product){
                $scope.keye.bidSN = product.productSN + dateFormat(new Date, 'yyMMdd')+Math.ceil(Math.random()*(9999-1000)+1000);
                $scope.keye.productSN = product.productSN;
                
                if(product.type==5){//债权则需要显示债权的下拉列表
                	$scope.showcontract=1;
                }else{
                	$scope.showcontract=0;
                }
                
                
            };
            
  
            $scope.create = function(item) {//add and edit
            	
            	$scope.uploader.upload();
            	
            	item.startDate = dateFormat(item.startDate, 'yyyy-MM-dd');
     			if(item.bidId){ // edit
	               	ProductBid.save(item,function(result){
     	         		if (result.success == "false"){
        	         		alert(result.message);
        	         		return;
        	         	}
	               		//window.location.reload();
                  		$scope.refresh('current',true);//refresh listgrid
                  		//$scope.clearForm();
                     	$('#addandedit').modal('hide');
                    });
                }else{ // add
	               	ProductBid.put(item,function(result){
     	         		if (result.success == "false"){
        	         		alert(result.message);
        	         		return;
        	         	}
                      	$scope.refresh('current',true);//refresh listgrid
                      	//$scope.clearForm();
                        $('#add').modal('hide');
                    });
              	}
            };

            $scope.changeFile = function(file) {
            	$scope.file = file;
            }
            $scope.uperror = function(file, type, ext) {
                if (type == 'extensions') {
                    alert("上传文件类型错误，请选择png,jpeg,jpg,bmp文件上传。");
                } else if (type == 'fileSingleSizeLimit') {
                    alert("上传文件超过最大限制，请选择其它文件。");
                } else if (type == 'emptyFile') {
                    alert("上传文件为空文件，请选择其它文件。");
                }
            }
            // 上传成功回调
            $scope.singleupsuccess = function() {
                setTimeout(function() {
                    
                    $scope.upsuccess( $scope.file);
                }, 10);
            }
            $scope.upsuccess = function(file) {
                // {"fileType":"txt","filePath":"","fileName":"1403331632090.txt","orgFileName":"tttt.txt","success":"true"}
                $scope.filePath = file.filePath;
                $scope.orgFileName = file.orgFileName;
                console.log(angular.toJson(file));
               
            }
            
            
        //-------------delete----------        
            $scope.todelete = function(item) {//click on DELETE link
            	
 				if (item.status!=0) {
 					alert("已开标，不可删除！");
 					return;
 				}
				$('#removeDialog').modal('show');
                $scope.tobedeleteId = item.bidId;
            };
            $scope.comfirmDelete = function() {//confirm to delete on dialog
   	            var params = {
   	        		bidId : $scope.tobedeleteId
   	   	        };
   	            ProductBid.remove({
   	                params : angular.toJson(params)
   	            }, function(jsonData) {
   	                $scope.refresh('current', true);
   	            });
           };
       }]);
    };
});