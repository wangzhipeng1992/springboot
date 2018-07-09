/*
 * ComboTree 树形表格组件,对Ztree的封装
 * version: 1.0.0
 * author: stoney
 * date: 2012/08/03
 * usage:
 * $("#combotree").combotree({}); 
 */
(function($) {
	$.fn.combotree = function(options, param) {
        if (typeof options == 'string') {
            switch (options) {
	            case 'setValue':
	                return this.each(function() {
	                    setValue(this, param);
	                });
	            case 'reload':
	                return this.each(function() {
	                    reload(this, param);
	                });
            }
        }

        options = options || {};
        return this.each(function() {
            var state = $.data(this, 'combotree');
            if (state) {
                $.extend(state.options, options);
            } else {
                var r = init(this);
                state = $.data(this, 'combotree', {
                    options: $.extend({},$.fn.combotree.defaults, {
                        width: (parseInt($(this).css('width')) || 'auto'),
                        treeWidth: $(this).attr('treeWidth'),
                        treeHeight: ($(this).attr('treeHeight') || 200),
                        url: $(this).attr('url'),
                        style: $(this).attr('class'),
                        check: $(this).attr('check')=='true'?true:false,
                        collapse: $(this).attr('collapse')=='true'?true:false,
                        asynch: $(this).attr('asynch')=='true'?true:false,
                        idField:$(this).attr('idField')||'id',
                        textField:$(this).attr('textField')||'name',
                        onChange:function(newValue,oldValue){
                        	var onChangeMethod = $(this).attr('onChange');
                        	if (onChangeMethod){
                        		onChangeMethod = eval(onChangeMethod);
                        		onChangeMethod(newValue,oldValue);
                        	}
                        }
                    },options),
                    combotree: r.combotree,
                    content: r.content
                });
            }

            setSize(this);
        });
    };

    $.fn.combotree.defaults = {
    	collapse: false,
    	check: false,
        width: 'auto',
        treeWidth: null,
        treeHeight: 200,
        url: null,
        onSelect: function() {},
        onChange: function(newValue, oldValue) {}
    };
    
    function setSize(target) {
        var opts = $.data(target, 'combotree').options;
        var combo = $.data(target, 'combotree').combotree;
        var content = $.data(target, 'combotree').content;
        if (!isNaN(opts.width)) {
            //var arrowWidth = combo.find('.combotree-arrow').outerWidth();
        	var arrowWidth = 0;
  /*          var width = opts.width - arrowWidth - (combo.outerWidth() - combo.width());
            combo.find('input.combotree-text').width(width);*/
            combo.find('input.combotree-text').addClass(opts.style);
        }
        if (opts.treeWidth) {
            content.width(opts.treeWidth);
        } else {
            content.width($.boxModel == true ? combo.outerWidth() - (content.outerWidth() - content.width()) : combo.outerWidth());
        }
        if (opts.treeHeight) {
            content.height(opts.treeHeight);
        }

        var ul = content.find('>ul');
        $(ul).attr("id",combo.find('input.combotree-value').attr("id"));
        var setting = {
	        	check: {
					enable: opts.check,
					chkboxType :{ "Y": "s", "N": "ps" }
				},
				async:{
					enable:true,
					type:"post",
					url:opts.url
				},
				data: {
					key:{
						name:'name'
					},
					simpleData: {
						enable: true,
						idKey:'id',
						pIdKey:'parentId',
						rootPid:0
					}
				},
				callback: {
					onClick: function (e, treeId, treeNode){
						onNodeClick(target, e, treeId, treeNode);
					},
					onCheck: function (e, treeId, treeNode){
						onNodeCheck(target, e, treeId, treeNode);
					},
					beforeCheck:function(){
						return true;
					},
					onAsyncSuccess:function (event, treeId, treeNode, msg){
						var zTree = $.fn.zTree.getZTreeObj(treeId);
						if(!opts.asynch){
							zTree.expandAll(!opts.collapse);
						}
					}
				}
        	};
        if(opts.asynch){
        	setting.async.autoParam=["id"];
        	setting.async.otherParam={"async":"true"};
        }
        $.fn.zTree.init( ul , setting);
    }

    function onNodeClick(target, e, treeId, treeNode) {
    	var opts = $.data(target, 'combotree').options;
    	if(opts.check){
    		return;
    	}
        var combo = $.data(target, 'combotree').combotree;
        var content = $.data(target, 'combotree').content;
        
		var zTree = $.fn.zTree.getZTreeObj(treeId),
		nodes = zTree.getSelectedNodes(),
		v = "";
		k = "";
		nodes.sort(function compare(a,b){return a[opts.idField]-b[opts.idField];});
		for (var i=0, l=nodes.length; i<l; i++) {
			v += nodes[i][opts.textField] + ",";
			k += nodes[i][opts.idField] + ",";
		}
		if (v.length > 0 ) v = v.substring(0, v.length-1);
		if (k.length > 0 ) k = k.substring(0, k.length-1);
		var oldValue = combo.find('input.combotree-value').val();
		combo.find('input.combotree-value').val(k).trigger('change');;
        combo.find('input.combotree-text').val(v).trigger('change');;
        content.hide();
        $(document).unbind('.combotree');
        opts.onSelect.call(target);
        if (oldValue != k) {
            opts.onChange.call(target, k, oldValue);
        }
	}

    function onNodeCheck(target, e, treeId, treeNode) {
    	var opts = $.data(target, 'combotree').options;
    	var combo = $.data(target, 'combotree').combotree;
        
		var zTree = $.fn.zTree.getZTreeObj(treeId),
		nodes = zTree.getCheckedNodes(),
		v = "";
		k = "";
		nodes.sort(function compare(a,b){return a[opts.idField]-b[opts.idField];});
		for (var i=0, l=nodes.length; i<l; i++) {
			v += nodes[i][opts.textField] + ",";
			k += nodes[i][opts.idField] + ",";
		}
		if (v.length > 0 ) v = v.substring(0, v.length-1);
		if (k.length > 0 ) k = k.substring(0, k.length-1);
		
		var oldValue = combo.find('input.combotree-value').val();
		
		combo.find('input.combotree-value').val(k).trigger('change');;
        combo.find('input.combotree-text').val(v).trigger('change');;
        
        opts.onSelect.call(target);
        if (oldValue != k) {
            opts.onChange.call(target, k, oldValue);
        }
	}
    
    function init(target) {
        $(target).hide();

        var span = $('<div class="input-group "><input type="text" class="combotree-text form-control" readonly="true"  ><div class="input-group-addon"><span class="glyphicon glyphicon-triangle-bottom"></span></div></div>').insertAfter(target);
        $('<input type="hidden" class="combotree-value"></input>').appendTo(span);
        var content = $('<div class="combotree-content"><ul class="ztree"></ul></div>').insertAfter(span);

        var name = $(target).attr('name');
        if (name) {
            span.find('input.combotree-value').attr('name', name);
            $(target).removeAttr('name');
        }
        var placeholder = $(target).attr('placeholder');
        if (placeholder) {
            span.find('input.combotree-text').attr('placeholder', placeholder);
            $(target).removeAttr('placeholder');
        }
        var id = $(target).attr('id');
        if (id) {
        	span.find('input.combotree-value').attr('id', id);
            $(target).removeAttr('id');
        }
        
        var text = $(target).attr('text');
        if (text) {
            span.find('input.combotree-text').val(text);
            $(target).removeAttr('text');
        }
        
        var textId = $(target).attr('textId');
        if (textId) {
            span.find('input.combotree-text').attr('id', textId);
            span.find('input.combotree-text').attr('name', textId);
            $(target).removeAttr('textId');
        }
        
        var val = $(target).val();
        if (val) {
            span.find('input.combotree-value').val(val);
            $(target).removeAttr('value');
        }

        function show() {
        	$(".combotree-content").hide();
        	var top = span.position().top + span.outerHeight();
        	if($(".ocp_ontop_main").length>0){
        		var parentTop = $(".ocp_ontop_main").scrollTop();
            	var parentHeight = $(".ocp_ontop_main").height();
            	if(top + parentTop + content.outerHeight() > parentHeight + parentTop){
            		top = span.position().top - content.outerHeight()+ parentTop;
            	}
        	}
            content.css({
                display: 'block',
                left: span.position().left,
                top:top
            });

            $(document).unbind('.combotree').bind('click.combotree',function() {
	            content.hide();
	            $(document).unbind('.combotree');
	            return false;
            });
            $(content).unbind('.combotree').bind('click.combotree',function() {
	            return false;
            });
        }

        span.click(function() {
            show();
            return false;
        });

        return {
            combotree: span,
            content: content
        };
    }

    function setValue(target, node) {
        var opts = $.data(target, 'combotree').options;
        var combo = $.data(target, 'combotree').combotree;
        var content = $.data(target, 'combotree').content;

        var oldValue = combo.find('input.combotree-value').val();
        combo.find('input.combotree-value').val(node.id);
        combo.find('input.combotree-text').val(node.text);

        if (oldValue != node.id) {
            opts.onChange.call(target, node.id, oldValue);
        }
    }

    function reload(target, url) {
        var opts = $.data(target, 'combotree').options;
        var content = $.data(target, 'combotree').content;
        
        //刷新树
    }

})(jQuery);