/**
 * layout方法扩展
 * @param {Object} jq
 * @param {Object} region
 */
$.extend($.fn.layout.methods, { 
    /**
     * 面板是否存在和可见
     * @param {Object} jq
     * @param {Object} params
     */
    isVisible:function(jq,params){
        return jq.each(function(){
            var panels = $.data(this, 'layout').panels;
            var pp =  panels[params];
            if (!pp) {
                return false;
            }
            if (pp.length) {
                return pp.panel('panel').is(':visible');
            }
            else {
                return false;
            }
        });
    },
    /**
     * 隐藏除某个region，center除外。
     * @param {Object} jq
     * @param {Object} params
     */
    hidden:function(jq,params){
        return jq.each(function(){
            var opts = $.data(this, 'layout').options;
            var panels = $.data(this, 'layout').panels;
            var cc = $(this);
            var region = params;
//            alert(region);
            if (region == 'east') {
                if (panels.expandEast) {
                    if ($(this).layout('isVisible','expandEast')) {
                        opts.regionState.east = 1;
                        panels.expandEast.panel('close');
                    }
                    else
                        if ($(this).layout('isVisible','east')) {
                            opts.regionState.east = 0;
                            panels.east.panel('close');
                        }
                }
                else {
                    panels.east.panel('close');
                }
                $(this).layout('resize');
            } else if (region == 'west') {
                if (panels.expandWest) {
                    if ($(this).layout('isVisible','expandWest')) {
                        opts.regionState.west = 1;
                        panels.expandWest.panel('close');
                    }
                    else
                        if ($(this).layout('isVisible','west')) {
                            opts.regionState.west = 0;
                            panels.west.panel('close');
                        }
                }
                else {
                    panels.west.panel('close');
                }
                $(this).layout('resize');
            }  else if (region == 'north') {
                if (panels.expandNorth) {
                    if ($(this).layout('isVisible','expandNorth')) {
                        opts.regionState.north = 1;
                        panels.expandNorth.panel('close');
                    }
                    else
                        if ($(this).layout('isVisible','north')) {
                            opts.regionState.north = 0;
                            panels.north.panel('close');
                        }
                }
                else {
                    panels.north.panel('close');
                }
                $(this).layout('resize');
            } else  if (region == 'south') {
                if (panels.expandSouth) {
                    if ($(this).layout('isVisible','expandSouth')) {
                        opts.regionState.south = 1;
                        panels.expandSouth.panel('close');
                    }
                    else
                        if ($(this).layout('isVisible','south')) {
                            opts.regionState.south = 0;
                            panels.south.panel('close');
                        }
                }
                else {
                    panels.south.panel('close');
                }
                $(this).layout('resize');
            } else  if (region == 'center') {
                if (panels.expandSouth) {
                    if ($(this).layout('isVisible','expandCenter')) {
                        opts.regionState.south = 1;
                        panels.expandCenter.panel('close');
                    }
                    else
                        if ($(this).layout('isVisible','center')) {
                            opts.regionState.center = 0;
                            panels.center.panel('close');
                        }
                }
                else {
                    panels.center.panel('close');
                }
                $(this).layout('resize');
            }
        });
    },
    /**
     * 显示某个region，center除外。
     * @param {Object} jq
     * @param {Object} params
     */
    show:function(jq,params){
        return jq.each(function(){
            var opts = $.data(this, 'layout').options;
            var panels = $.data(this, 'layout').panels;
            var cc = $(this);
            var region = params;
            if (region == 'east') {
                if (panels.expandEast) {
                    if (!$(this).layout('isVisible','expandEast')) {
                        if (!$(this).layout('isVisible','east')) {
                            if (opts.regionState.east == 1) {
                                panels.expandEast.panel('open');
                            }
                            else {
                                panels.east.panel('open');
                            }
                        }
                    }
                }
                else {
                    panels.east.panel('open');
                }
                $(this).layout('resize');
            }
            else {
                if (region == 'west') {
                    if (panels.expandWest) {
                        if (!$(this).layout('isVisible','expandWest')) {
                            if (!$(this).layout('isVisible','west')) {
                                if (opts.regionState.west == 1) {
                                    panels.expandWest.panel('open');
                                }
                                else {
                                    panels.west.panel('open');
                                }
                            }
                        }
                    }
                    else {
                        panels.west.panel('open');
                    }
                    $(this).layout('resize');
                }
                else {
                    if (region == 'north') {
                        if (panels.expandNorth) {
                            if (!$(this).layout('isVisible','expandNorth')) {
                                if (!$(this).layout('isVisible','north')) {
                                    if (opts.regionState.north == 1) {
                                        panels.expandNorth.panel('open');
                                    }
                                    else {
                                        panels.north.panel('open');
                                    }
                                }
                            }
                        }
                        else {
                            panels.north.panel('open');
                        }
                        $(this).layout('resize');
                    }
                    else {
                        if (region == 'south') {
                            if (panels.expandSouth) {
                                if (!$(this).layout('isVisible','expandSouth')) {
                                    if (!$(this).layout('isVisible','south')) {
                                        if (opts.regionState.south == 1) {
                                            panels.expandSouth.panel('open');
                                        }
                                        else {
                                            panels.south.panel('open');
                                        }
                                    }
                                }
                            }
                            else {
                                panels.south.panel('open');
                            }
                            $(this).layout('resize');
                        }
                    }
                }
            }
        });
    }
});