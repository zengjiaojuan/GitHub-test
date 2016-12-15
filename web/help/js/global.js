/*!
 * Global 
 * @author WangPing
 * @since 2014/04/18 09:50
 */
var Util = Util || {};
Util.config = {
	jsPath : "/pay/js/",
	cssPath : "/pay/css/",
	imgPath : "/pay/img/"
};
Util.isMobile = {  
	Android: function() {  
		return navigator.userAgent.match(/Android/i) ? true : false;  
	},  
	BlackBerry: function() {  
		return navigator.userAgent.match(/BlackBerry/i) ? true : false;  
	},  
	iOS: function() {  
		return navigator.userAgent.match(/iPhone|iPad|iPod/i) ? true : false;  
	},  
	Windows: function() {  
		return navigator.userAgent.match(/IEMobile/i) ? true : false;  
	},  
	any: function() {  
		return (Util.isMobile.Android() || Util.isMobile.BlackBerry() || Util.isMobile.iOS() || Util.isMobile.Windows());  
	}  
};
/*!
 * Init 
 */
$(function() {
	$('[data-url]').on('click',function(){
		var self = $(this);
		if(self.attr('data-url') != ''){
			window.location.href = self.data('url');
		}
	});
});