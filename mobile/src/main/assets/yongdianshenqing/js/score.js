
/**
 * JS评分效果
 */
 function Score(options) {
	this.config = {
		selector                  :   '.star',     // 评分容器
		renderCallback            :   null,        // 渲染页面后回调
		callback                  :   null         // 点击评分回调                         
	};

	this.cache = {
		aMsg : [
				"很不满意|差得太离谱，与卖家描述的严重不符，非常不满",
				"不满意|部分有破损，与卖家描述的不符，不满意",
				"一般|质量一般，没有卖家描述的那么好",
				"满意|质量不错，与卖家描述的基本一致，还是挺满意的",
				"非常满意|质量非常好，与卖家描述的完全一致，非常满意"
				],
		iStar  : 0,
		iScore : 0
	};

	this.init(options);
 }

 Score.prototype = {

	constructor: Score,

	init: function(options){
		this.config = $.extend(this.config,options || {});
		var self = this,
			_config = self.config,
			_cache = self.cache;

		self._renderHTML();
	},
	_renderHTML: function(){
		var self = this,
			_config = self.config;
		var html = '<span class="desc"></span>' + 
				   '<p class="star-p hidden"></p>';
		$(_config.selector).each(function(index,item){
			$(item).append(html);
			$(item).wrap($('<div class="parentCls" style="position:relative"></div>'));
			var parentCls = $(item).closest('.parentCls');
			self._bindEnv(parentCls);
			_config.renderCallback && $.isFunction(_config.renderCallback) && _config.renderCallback();
		});

	},
	_bindEnv: function(parentCls){
		var self = this,
			_config = self.config,
			_cache = self.cache;

		$(_config.selector + ' li',parentCls).each(function(index,item){
			
			// 鼠标移上
			$(item).mouseover(function(e){
				var offsetLeft = $('ul',parentCls)[0].offsetLeft;
				ismax(index + 1);
				
				$('p',parentCls).hasClass('hidden') && $('p',parentCls).removeClass('hidden');
				$('p',parentCls).css({'left':index*$(this).width() + 12 + 'px'});
				

				var html = '<em>' + 
							  '<b>'+index+'</b>分 '+_cache.aMsg[index].split('|')[0]+'' + 
						   '</em>';
				$('p',parentCls).html(html);
			});

			// 鼠标移出
			$(item).mouseout(function(){
				ismax();
 				!$('p',parentCls).hasClass('hidden') && $('p',parentCls).addClass('hidden');
			});
			
			// 鼠标点击
			$(item).click(function(e){
				var index = $(_config.selector + ' li',parentCls).index($(this));
				_cache.iStar = index + 1;
								
				!$('p',parentCls).hasClass('hidden') && $('p',parentCls).addClass('hidden');
				var html = '<strong>' +
								(index+1) +
						   '分</strong>';

				$('.desc',parentCls).html(html);
				_config.callback && $.isFunction(_config.callback) && _config.callback({starAmount:_cache.iStar});
			});
			
		});

		function ismax(iArg) {
			_cache.iScore = iArg || _cache.iStar;
			var lis = $(_config.selector + ' li',parentCls);
			
			for(var i = 0; i < lis.length; i++) {
				lis[i].className = i < _cache.iScore ? "on" : "";
			}
		}
	}
 };

