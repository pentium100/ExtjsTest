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
				+ Ext.example.shortBogusMarkup + '</div>';
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
			border : false,
			// plain: true,
			items : [{
				id : 'HomePage',
				title : '首页',
				iconCls : 'home',
				layout : 'fit',
				//bodyPadding : '5 5 5 5',
				bodyPadding : '0 0 0 0',
				items : [{
					id : 'app-portal',
					xtype : 'portalpanel',
					region : 'center',
					layout : {
						//type : 'border',
						//padding : '0 5 5 5' // pad the layout from the window
											// edges
					},
					//bodyPadding : 0,

					items : [{
						id : 'col-1',
						items : [{
							id : 'portlet-1',
							title : 'Grid Portlet',
							tools : this.getTools(),
							items : Ext.create('AM.portal.classes.GridPortlet'),
							listeners : {
								'close' : Ext.bind(this.onPortletClose, this)
							}
						}, {
							id : 'portlet-2',
							title : 'Portlet 2',
							tools : this.getTools(),
							html : content,
							listeners : {
								'close' : Ext.bind(this.onPortletClose, this)
							}
						}]
					}, {
						id : 'col-2',
						items : [{
							id : 'portlet-3',
							title : 'Portlet 3',
							tools : this.getTools(),
							html : '<div class="portlet-content">'
									+ Ext.example.bogusMarkup + '</div>',
							listeners : {
								'close' : Ext.bind(this.onPortletClose, this)
							}
						}]
					}, {
						id : 'col-3',
						items : [{
							id : 'portlet-4',
							title : 'Stock Portlet',
							tools : this.getTools(),
							items : Ext
									.create('AM.portal.classes.ChartPortlet'),
							listeners : {
								'close' : Ext.bind(this.onPortletClose, this)
							}
						}]
					}]
				}

				]
			}]
		});
		this.callParent(arguments);
	}
})