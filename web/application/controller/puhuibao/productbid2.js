define(function(require, exports, module) {
	// 理财产品发标
	return function setApp(app) {
		app.controller('PuhuibaoProductbid2Ctrl', [
				'$scope',
				'ProductBid2',
				'$filter',
				function($scope, ProductBid, $filter, AssetProduct,
						UserRedpacket, UserInvestment) {
					// ------------------gridlist---------
					$scope.pager = ProductBid;
					$scope.params = {};
					$scope.params.orderBy = 'a.bid_important';
					$scope.params.order = 'desc';
					$scope.params.status="1";
                    $scope.refresh && $scope.refresh('first', true);
					
					//==========================================
					$scope.edit=function(item){
						alert("我点了修改按钮");
						$scope.keyclone = angular.copy(item);
						$scope.baocun=function(keyclone){
							alert("我点了保存按钮");
							
							var  params={}
							ProductBid.save({})
							
							ProductBid.save({
								bidId:keyclone.bidId,
								bidImportant:keyclone.bidImportant
								
							},function() {
								        
										$scope.refresh('current', true);
										$('#edit').modal('hide');
									})
						}
						
						
					}
				
					
					// ====================推荐(recommend)按钮========================
					$scope.recommend = function(item) {
						alert("我点了推荐按钮")
						if (item.recommendStatus == 0) {
							if (confirm("你确定要推荐吗")) {
								alert("确定");
								// 修改本对象的推荐标识符 为1
								ProductBid.save({
									bidId : item.bidId,
									recommendStatus : 1
								// 0为不推荐，1为推荐

								}, function() {
									$scope.refresh('current', true);
								})

							} else {
								return;
							}
						} else {
							if (confirm("你确定要取消推荐吗")) {
								alert("确定");
								// 修改本对象的推荐标识符 为0
								ProductBid.save({
									bidId : item.bidId,
									recommendStatus : 0
								// 0为不推荐，1为推荐

								}, function() {
									$scope.refresh('current', true);
								})

							} else {
								return;
							}
						}
					}

				  } ]);
	};
});