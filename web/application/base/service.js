//接口集
define(function() {
	var app = angular.module('service', []);
	app.factory('Pushlet', function() {
		PL._init();
		PL.joinListen("/event/match");

		var cbList = {};
		window.onData = function(event) {
			console.log("onData:", event);
			if (cbList[event.getSubject()]) {
				cbList[event.getSubject()](event);
			}
		};

		return {
			on : function(name, cb) {
				cbList[name] = cb;
			}
		}
	});
	app.factory('Route', [ '$resource', function($resource) {
		return $resource('data/route.json');
	} ]);

	app.factory('Common', [ '$resource', function($resource) {
		return $resource('common.shtml');
	} ]);

	/*
	 * 全国中心与省接收资源访问链接
	 */
	app.factory('EsbRecv', [ '$resource', function($resource) {
		return $resource('esbRecv.shtml');
	} ]);

	app.factory('EsbSend', [ '$resource', function($resource) {
		return $resource('esbSend.shtml');
	} ]);

	app.factory('EsbDownload', [ '$resource', function($resource) {
		return $resource('esbDownload.shtml');
	} ]);

	app.factory('DataDic', [ '$resource', function($resource) {
		return $resource('dataDic.shtml');
	} ]);

	app.factory('PortExcp', [ '$resource', function($resource) {
		return $resource('portExcp.shtml');
	} ]);

	app.factory('DataCheck', [ '$resource', function($resource) {
		return $resource('dataCheck.shtml');
	} ]);

	app.factory('DataDownload', [ '$resource', function($resource) {
		return $resource('dataDownload.shtml');
	} ]);

	/*
	 * 知识库
	 */
	app.factory('Share', [ '$resource', function($resource) {
		return $resource('share.shtml');
	} ]);
	app.factory('Examine', [ '$resource', function($resource) {
		return $resource('examine.shtml');

	} ]);
	app.factory('Manage', [ '$resource', function($resource) {
		return $resource('manage.shtml', null, {
			getFileCnt : {
				method : 'get'
			}
		});
	} ]);
	app.factory('LabelLib', [ '$resource', function($resource) {
		return $resource('labelLib.shtml', null, {
			queryLowerLevelorgcd : {
				method : 'get',
				isArray : true
			}
		});
	} ]);

	app.factory('RelateLabel', [ '$resource', function($resource) {
		return $resource('relateLabel.shtml');
	} ]);
 
	// 系统运行管理--用户管理
	app.factory('User', [ '$resource', function($resource) {
		return $resource('user.shtml');
	} ]);
	// 用户角色配置
	app.factory('UserRoleRela', [ '$resource', function($resource) {
		return $resource('userRoleRela.shtml');
	} ]);
	// 系统运行管理--角色管理
	app.factory('Role', [ '$resource', function($resource) {
		return $resource('role.shtml');
	} ]);
	// 角色权限配置
	app.factory('RolePermissions', [ '$resource', function($resource) {
		return $resource('rolePermissions.shtml');
	} ]);

	// 系统运行管理--机构管理
	app.factory('Institution', [ '$resource', function($resource) {
		return $resource('institution.shtml');
	} ]);

	// 系统运行管理--菜单管理
	app.factory('Menu', [ '$resource', function($resource) {
		return $resource('menu.shtml');
	} ]);

	// 系统运行管理--公告管理
	app.factory('Announcement', [ '$resource', function($resource) {
		return $resource('announcement.shtml');
	} ]);
	
	app.factory('UserMonitor', [ '$resource', function($resource) {
		return $resource('userMonitor.shtml');
	} ]);
 
	// 上传下载
	app.factory('DataUploadDownloadLog', [ '$resource', function($resource) {
		return $resource('dataUploadDownloadLog.shtml');
	} ]);
    

	// 文件上传
	app.factory('Upload', [ '$resource', function($resource) {
		return $resource('repositoryupload.shtml');
	} ]);

 
	// 数据同步列表-数据同步
	app.factory('SynchronizationApply', [ '$resource', function($resource) {
		return $resource('synchronizationApply.shtml');
	} ]);
	// 数据同步审核-同步日志
	app.factory('SyncLog', [ '$resource', function($resource) {
		return $resource('syncLog.shtml');
	} ]);

	 
	//附件
	app.factory('UploadFile', [ '$resource', function($resource) {
		return $resource('uploadFile.shtml');
	} ]);
	//产生key
	app.factory('GeneratedKey', [ '$resource', function($resource) {
		return $resource('generatedKey.shtml');
	} ]);
	//
//	app.factory('UserInformation', [ '$resource', function($resource) {
//		return $resource('userInformation.shtml');
//	} ]);	
	// 省市
	app.factory('ProvCode', [ '$resource', function($resource) {
		return $resource('provCode.shtml');
	} ]);
	// 理财产品
	app.factory('AssetProduct', ['$resource', function($resource) {
		return $resource('assetProduct.shtml');
	} ]);
	//投资提醒
	app.factory('InvestmentAlert', ['$resource', function($resource) {
		return $resource('investmentAlert.shtml');
	} ]);
	// 个人账户
	app.factory('UserAccount', ['$resource', function($resource) {
		return $resource('userAccount.shtml');
	} ]);
	app.factory('UserCard', ['$resource', function($resource) {
		return $resource('userCard.shtml');
	} ]);
	app.factory('MobileUser', ['$resource', function($resource) {
		return $resource('userInformation.shtml');
	} ]);
	app.factory('UserLoan', ['$resource', function($resource) {
		return $resource('userLoan.shtml');
	} ]);
	app.factory('MobileUserLoan', ['$resource', function($resource) {
		return $resource('mobileUserLoan.shtml');
	} ]);
	app.factory('UserMessage', ['$resource', function($resource) {
		return $resource('userMessage.shtml');
	} ]);
	app.factory('Feedback', ['$resource', function($resource) {
		return $resource('feedback.shtml');
	} ]);
	app.factory('ExperienceProduct', ['$resource', function($resource) {
		return $resource('experienceProduct.shtml');
	} ]);
	app.factory('Advertisement', ['$resource', function($resource) {
		return $resource('advertisement.shtml');
	} ]);
	app.factory('Appreciation', ['$resource', function($resource) {
		return $resource('appreciation.shtml');
	} ]);
	app.factory('Holiday', ['$resource', function($resource) {
		return $resource('holiday.shtml');
	} ]);
	app.factory('ProductBid', ['$resource', function($resource) {
		return $resource('productBid.shtml');
	}]);
	app.factory('Version', ['$resource', function($resource) {
		return $resource('version.shtml');
	}]);
	app.factory('LoanItem', ['$resource', function($resource) {
		return $resource('loanItem.shtml');
	}]);
	app.factory('Bank', ['$resource', function($resource) {
		return $resource('bank.shtml');
	}]);
	app.factory('Notification', ['$resource', function($resource) {
		return $resource('notification.shtml');
	}]);
	app.factory('ThirdPayAccount', ['$resource', function($resource) {
		return $resource('thirdPayAccount.shtml');
	}]);
	app.factory('UserAccountLog', ['$resource', function($resource) {
		return $resource('userAccountLog.shtml');
	}]);
	app.factory('UserAsset', ['$resource', function($resource) {
		return $resource('userAsset.shtml');
	}]);
	app.factory('MonthBalance', ['$resource', function($resource) {
		return $resource('monthBalance.shtml');
	}]);
	app.factory('DayBalance', ['$resource', function($resource) {
		return $resource('dayBalance.shtml');
	}]);
	app.factory('UserRedpacket', ['$resource', function($resource) {
		return $resource('userRedpacket.shtml');
	}]);
	app.factory('UserInvestment', ['$resource', function($resource) {
		return $resource('userInvestment.shtml');
	}]);
	app.factory('ItemInvestment', ['$resource', function($resource) {
		return $resource('itemInvestment.shtml');
	}]);
 
	app.factory('InfoShow', ['$resource', function($resource) {
		return $resource('infoShow.shtml');
	}]);
	 
	app.factory('ExperienceValue', ['$resource', function($resource) {
		return $resource('experienceValue.shtml');
	}]);
	app.factory('GroupChat', ['$resource', function($resource) {
		return $resource('groupChat.shtml');
	}]);

	app.factory('ContractManagement', ['$resource', function($resource) {
		return $resource('contractManagement.shtml');
	}]);
	app.factory('CustomerManagement', ['$resource', function($resource) {
		return $resource('customerManagement.shtml');
	}]);
	app.factory('BorrowerManagement', ['$resource', function($resource) {
		return $resource('borrowerManagement.shtml');
	}]);
	app.factory('CreditMatching', ['$resource', function($resource) {
		return $resource('creditMatching.shtml');
	}]);
	
	//加息劵
	app.factory('AddRate', ['$resource', function($resource) {
		return $resource('addRate.shtml');
	}]);
	
	
	
	
});