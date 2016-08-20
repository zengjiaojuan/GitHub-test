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

	// 对内对外服务--客户管理
	app.factory('Customer', [ '$resource', function($resource) {
		return $resource('customer.shtml');
	} ]);
	// 对内对外服务--数据服务流程管理
	app.factory('DataServiceProcess', [ '$resource', function($resource) {
		return $resource('dataServiceProcess.shtml');
	} ]);
	// 对内对外服务--流程规则定制
	app.factory('FlowRuleCust', [ '$resource', function($resource) {
		return $resource('flowRuleCust.shtml');
	} ]);

	// 对内对外服务--运行流程
	app.factory('RunProcess', [ '$resource', function($resource) {
		return $resource('runProcess.shtml');
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

	// 系统运行管理--用户操作日志
	app.factory('Journal', [ '$resource', function($resource) {
		return $resource('journal.shtml');
	} ]);

	// 数据定义
	app.factory('DataDefinition', [ '$resource', function($resource) {
		return $resource('dataDefinition.shtml');
	} ]);
	// 数据定义字段
	app.factory('DataDefinitionAttr', [ '$resource', function($resource) {
		return $resource('dataDefinitionAttr.shtml');
	} ]);
	// 概念模型
	app.factory('ConceptModelDic', [ '$resource', function($resource) {
		return $resource('conceptModelDic.shtml');
	} ]);
	// 类型字典
	app.factory('DataTypeDic', [ '$resource', function($resource) {
		return $resource('dataTypeDic.shtml');
	} ]);
	// 数据集
	app.factory('DataSet', [ '$resource', function($resource) {
		return $resource('dataSet.shtml');
	} ]);
	// 数据集操作
	app.factory('DataSetContent', [ '$resource', function($resource) {
		return $resource('dataSetContent.shtml');
	} ]);
	// 上传下载日志
	app.factory('DataUploadDownloadLog', [ '$resource', function($resource) {
		return $resource('dataUploadDownloadLog.shtml');
	} ]);
	// 清洗管理
	app.factory('DataSetClean', [ '$resource', function($resource) {
		return $resource('dataSetClean.shtml');
	} ]);
	app.factory('DataSetView', [ '$resource', function($resource) {
		return $resource('dataSetView.shtml');
	} ]);
	// 地址清洗匹配
	app.factory('AddressClean', [ '$resource', function($resource) {
		return $resource('addressClean.shtml');
	} ]);
	// 邮编清洗匹配
	app.factory('PostClean', [ '$resource', function($resource) {
		return $resource('postClean.shtml');
	} ]);
	// 上传下载
	app.factory('DataUploadDownloadLog', [ '$resource', function($resource) {
		return $resource('dataUploadDownloadLog.shtml');
	} ]);
    // 系统代码
    app.factory('SystemCode', [ '$resource', function($resource) {
        return $resource('systemCode.shtml');
    } ]);

	// 排重管理
	app.factory('DataSetUnique', [ '$resource', function($resource) {
		return $resource('dataSetUnique.shtml');
	} ]);
	// 数据重构
	app.factory('Rebuild', [ '$resource', function($resource) {
		return $resource('rebuild.shtml');
	} ]);

	// 数据整合
	app.factory('Merge', [ '$resource', function($resource) {
		return $resource('merge.shtml');
	} ]);

	// 上传下载日志
	app.factory('DataSetAssess', [ '$resource', function($resource) {
		return $resource('assess.shtml');
	} ]);

	// 数据整合日志的查询
	app.factory('DataInteLog', [ '$resource', function($resource) {
		return $resource('dataInteLog.shtml');
	} ]);

	// 根据规则类型,查询脚本库
	app.factory('RuleScript', [ '$resource', function($resource) {
		return $resource('ruleScript.shtml');
	} ]);

	// 表整合规则查询
	app.factory('TableInteRule', [ '$resource', function($resource) {
		return $resource('tableInteRule.shtml');
	} ]);

	// 字段整合规则查询
	app.factory('ColumnInteRule', [ '$resource', function($resource) {
		return $resource('columnInteRule.shtml');
	} ]);

	// 标签列表
	app.factory('CommonLabel', [ '$resource', function($resource) {
		return $resource('commonLabel.shtml', null, {
			generaterPrimaryKey : {
				method : 'get',
				isArray : true
			}
		});
	} ]);

	// 查附件

	app.factory('CommonAttachment', [ '$resource', function($resource) {
		return $resource('commonAttachment.shtml');
	} ]);

	// 文件上传
	app.factory('Upload', [ '$resource', function($resource) {
		return $resource('repositoryupload.shtml');
	} ]);

	// ------王威------
	// 指标维护
	app.factory('Kpi', [ '$resource', function($resource) {
		return $resource('kpi.shtml');
	} ]);

	// 指标列表
	app.factory('SourceIndexs', [ '$resource', function($resource) {
		return $resource('sourceIndexs.shtml');
	} ]);
	// 数据同步列表
	app.factory('Synchronization', [ '$resource', function($resource) {
		return $resource('synchronization.shtml');
	} ]);

	// 数据同步列表-新增同步-指标主题
	app.factory('IndexTheme', [ '$resource', function($resource) {
		return $resource('indexTheme.shtml');
	} ]);

	// 数据同步列表-新增同步-源表列表
	app.factory('SourceTables', [ '$resource', function($resource) {
		return $resource('sourceTables.shtml');
	} ]);
	// 数据同步列表-新增同步-需求列表
	app.factory('DemandAvailable', [ '$resource', function($resource) {
		return $resource('demandAvailable.shtml');
	} ]);
	// 数据同步列表-同步新增-审批人
	app.factory('SyncAuditor', [ '$resource', function($resource) {
		return $resource('syncAuditor.shtml');
	} ]);
	// 数据同步列表-数据同步
	app.factory('SynchronizationApply', [ '$resource', function($resource) {
		return $resource('synchronizationApply.shtml');
	} ]);
	// 数据同步审核-同步日志
	app.factory('SyncLog', [ '$resource', function($resource) {
		return $resource('syncLog.shtml');
	} ]);

	// 分析过程参数化-分析主题
	app.factory('AnalysisTheme', [ '$resource', function($resource) {
		return $resource('analysisTheme.shtml');
	} ]);
	// 分析过程参数化-分析类型
	app.factory('AnalysisType', [ '$resource', function($resource) {
		return $resource('analysisType.shtml');
	} ]);
	// 分析过程参数化-输出表
	app.factory('ExportTables', [ '$resource', function($resource) {
		return $resource('exportTables.shtml');
	} ]);
	// 分析过程参数化-输出表
	app.factory('TableColumns', [ '$resource', function($resource) {
		return $resource('tableColumns.shtml');
	} ]);
	// 分析过程参数化-年份
	app.factory('ParaYearList', [ '$resource', function($resource) {
		return $resource('paraYearList.shtml');
	} ]);
	// 分析过程参数化-报刊类别
	app.factory('ParaPapers', [ '$resource', function($resource) {
		return $resource('paraPapers.shtml');
	} ]);
	// 分析过程参数化-行业
	app.factory('ParaIndustry', [ '$resource', function($resource) {
		return $resource('paraIndustry.shtml');
	} ]);

	// 分析过程参数化-省份
	app.factory('ParaProvince', [ '$resource', function($resource) {
		return $resource('paraProvince.shtml');
	} ]);

	// 分析过程参数化-重点客户
	app.factory('ParaImportantClient', [ '$resource', function($resource) {
		return $resource('paraImportantClient.shtml');
	} ]);

	// 分析过程参数化-定义大客户
	app.factory('ParaDefinitionBigClient', [ '$resource', function($resource) {
		return $resource('paraDefinitionBigClient.shtml');
	} ]);
	// 分析过程参数化-参数历史
	app.factory('ParaHistory', [ '$resource', function($resource) {
		return $resource('paraHistory.shtml');
	} ]);
	// 分析过程参数化-提交用的参数
	app.factory('ParameterList', [ '$resource', function($resource) {
		return $resource('parameterList.shtml');
	} ]);

	// 订阅管理--订阅信息
	app.factory('Subscribe', [ '$resource', function($resource) {
		return $resource('subscribe.shtml');
	} ]);

	// 订阅管理--主题，报表
	app.factory('DataRepor', [ '$resource', function($resource) {
		return $resource('dataRepor.shtml');
	} ]);

	// 订阅管理--权限
	app.factory('Empower', [ '$resource', function($resource) {
		return $resource('empower.shtml');
	} ]);

	// 订阅管理--邮件推送
	app.factory('EmailPush', [ '$resource', function($resource) {
		return $resource('emailPush.shtml');
	} ]);

	// 订阅管理--主题报表
	app.factory('Theme', [ '$resource', function($resource) {
		return $resource('theme.shtml');
	} ]);

	// 订阅管理--主题名称
	app.factory('ThemeRep', [ '$resource', function($resource) {
		return $resource('themeRep.shtml');
	} ]);

	// 订阅管理--订阅记录
	app.factory('SubsTail', [ '$resource', function($resource) {
		return $resource('subsTail.shtml');
	} ]);

	// 订阅管理--预览
	app.factory('Report', [ '$resource', function($resource) {
		return $resource('report.shtml');
	} ]);

	// ------------------王威-------------

	// 专题流程管理--需求管理
	app.factory('Demand', [ '$resource', function($resource) {
		return $resource('demand.shtml');
	} ]);
	// 专题流程管理--过程管理
	app.factory('Processes', [ '$resource', function($resource) {
		return $resource('processes.shtml');
	} ]);

	// 专题流程管理--成果管理
	app.factory('Achievement', [ '$resource', function($resource) {
		return $resource('achievement.shtml');
	} ]);
	// 专题流程管理--附件管理
	app.factory('NodeAnnex', [ '$resource', function($resource) {
		return $resource('nodeAnnex.shtml');
	} ]);
	// 专题流程管理--跟踪管理
	app.factory('Fllow', [ '$resource', function($resource) {
		return $resource('fllow.shtml');
	} ]);

	// 分析流程固化
	app.factory('AnalysisProcess', [ '$resource', function($resource) {
		return $resource('analysisProcess.shtml');
	} ]);
	app.factory('DepartmentManagement', [ '$resource', function($resource) {
		return $resource('departmentManagement.shtml');
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
	app.factory('MuserFollow', ['$resource', function($resource) {
		return $resource('muserFollow.shtml');
	}]);
	app.factory('ImpressionList', ['$resource', function($resource) {
		return $resource('impressionList.shtml');
	}]);
	app.factory('MuserImpression', ['$resource', function($resource) {
		return $resource('muserImpression.shtml');
	}]);	
	app.factory('InfoShow', ['$resource', function($resource) {
		return $resource('infoShow.shtml');
	}]);
	app.factory('HexagramCredit', ['$resource', function($resource) {
		return $resource('hexagramCredit.shtml');
	}]);	
	app.factory('MobileUserHexagram', ['$resource', function($resource) {
		return $resource('mobileUserHexagram.shtml');
	}]);
	app.factory('HexagramCharm', ['$resource', function($resource) {
		return $resource('hexagramCharm.shtml');
	}]);
	app.factory('HexagramAbility', ['$resource', function($resource) {
		return $resource('hexagramAbility.shtml');
	}]);
	app.factory('HexagramFortune', ['$resource', function($resource) {
		return $resource('hexagramFortune.shtml');
	}]);
	app.factory('HexagramFame', ['$resource', function($resource) {
		return $resource('hexagramFame.shtml');
	}]);
	app.factory('AlipayApi', ['$resource', function($resource) {
		return $resource('alipayApi.shtml');
	}]);
	
	app.factory('FriendInvite', ['$resource', function($resource) {
		return $resource('friendInvite.shtml');
	}]);
	app.factory('ResourceNotice', ['$resource', function($resource) {
		return $resource('resourceNotice.shtml');
	}]);
	app.factory('CalculateFormula', ['$resource', function($resource) {
		return $resource('calculateFormula.shtml');
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
	
 
	app.factory('GroupRedpacket', ['$resource', function($resource) {
		return $resource('groupRedpacket.shtml');
	}]);
	app.factory('RedpacketReceive', ['$resource', function($resource) {
		return $resource('redpacketReceive.shtml');
	}]);
	app.factory('ThirdPayLog', ['$resource', function($resource) {
		return $resource('thirdPayLog.shtml');
	}]);
	
	
	
	
});