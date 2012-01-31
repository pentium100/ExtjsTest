Ext.define('AM.view.TabPanel', {
	extend : 'Ext.tab.Panel',

	getTools : function() {
		return [{
					xtype : 'tool',
					type : 'gear',
					handler : function(e, target, panelHeader, tool) {
						var portlet = panelHeader.ownerCt;
						portlet.setLoading('Working...');
						Ext.defer(function() {
									portlet.setLoading(false);
								}, 2000);
					}
				}];
	},

	initComponent : function() {
		var content = '<div class="portlet-content">'
				+  '</div>';
		Ext.apply(this, {
			id : 'content-panel',
			region : 'center',
			alias : 'widget.amtabpanel',
			defaults : {
				autoScroll : true,
				bodyPadding : 10
			},
			layout : 'fit',
			autoDestroy : true,
			activeTab : 0,
			border : false
			//,
			// plain: true,
			//items : [{
			//	id : 'HomePage',
			//	title : '首页',
			//	iconCls : 'home',
			//	layout : 'fit',
			//	bodyPadding : '0 0 0 0'
			//}]
		});
		this.callParent(arguments);
	}
})