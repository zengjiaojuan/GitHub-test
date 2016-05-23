// JavaScript Document
$(function(){
	var items = $('.ask-help');
	var adt = items.find('dt');
	var add = items.find('dd');
	var aright = 'arrows-right';
	var adown = 'arrows-down';
	var cur = 'current';
	var t = 300;
	var html = '<i class="arrows-right"></i>';
	adt.append(html);
	var aicon = adt.find('i');
	var url = window.location.href;
	var para = url.split('#')[1];
	if(para!=''){
		var dl = $('#'+para);
		dl.find('dt').addClass(cur);
		dl.find('dt').find('i').removeClass(aright).addClass(adown);
		dl.find('dd').slideDown(t);
	}
	adt.on('click',function(){
		var self = $(this);
		var sicon = self.find('i');
		var sib = self.siblings('dd');
		if(sib.is(':hidden')){
			adt.removeClass(cur);
			self.addClass(cur);
			aicon.removeClass(adown).addClass(aright);
			sicon.removeClass(aright).addClass(adown);
			add.slideUp(t);
			sib.slideDown(t);
		}else{
			self.removeClass(cur);
			sicon.removeClass(adown).addClass(aright);
			sib.slideUp(t);
		}
	});
})
