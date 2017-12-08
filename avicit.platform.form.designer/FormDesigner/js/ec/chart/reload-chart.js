
function generateData(){
    var data = [];
    for(var i = 0; i < 30; ++i){
        data.push({date:i, tps: 0, hps: 0, app1cpu:0});
    }
    var datas = {};
    datas.data = data;
    datas.total = 30;
    return datas;
}

Ext.onReady(function(){

	root = "/testAxis2";
	Ext.chart.Chart.CHART_URL = root + '/charts.swf';

    var store = new Ext.data.JsonStore({
        fields: ['date', 'tps', 'hps', 'app1cpu'],
        //['time', 'hits'],{name: 'date', type: 'date', dateFormat: 'time'}
//        data: generateData(),
        totalProperty: 'total',
        root: 'data'
    });
    
    new Ext.Panel({
        width: 700,
        height: 400,
        renderTo: document.body,
        title: 'Column Chart with Reload - Hits per Month',
        tbar: [{
            text: 'Load new data set',
            handler: function(){
                store.loadData(generateData());
            }
        }],
        items: {
            xtype: 'linechart',
			store: store,
            xField: 'date',
			xAxis: new Ext.chart.CategoryAxis({
                title: 'date'
            }),
			series: [{
				displayName: 'TPS',
				yField: 'tps',
				style: {
					color: 0x88ff88,
					size: 5,
					lineSize: 1
				}
			},{
				displayName: 'APP1 CPU',
				yField: 'app1cpu',
				style: {
					color: 0xff8888,
					size: 5,
					lineSize: 1
				}
			},{
				displayName: 'HPS',
				yField: 'hps',
				style: {
					color: 0x555555,
					size: 5,
					lineSize: 1
				}
			}],
			extraStyle: {
				legend: {
					display: 'right'
				},
				yAxis: {
					majorGridLines: {
						color: 0xdddddd
					}
				}
			}
        }
    });
    
    store.loadData(generateData());
    
    //the servlet should return String("{total:1, data: [{date:" + num + ", tps:" + value + ", hps:" + value1 + ",app1cpu: " + value2 +"}]}").
    Ext.TaskMgr.start({
            interval: 2000, //runs every 1 sec
            run: function() {
            	Ext.Ajax.request({
			       method:"post",
			       url: root + '/data',
			       success: function(result, request){
			        	var obj = Ext.decode(result.responseText);
		            	store.loadData(obj,true);
		            	if(store.getCount() > 30)
		            	{
		            		var flen = obj.length;
		            		store.remove(store.getAt(0,flen-1));
		            	}
			       },
			       failure: function(result, request){
			       }
			       
			     });
            }
        });
        
});