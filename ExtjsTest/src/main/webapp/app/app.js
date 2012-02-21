
Ext.application({
			name : 'AM',
			appFolder : 'app',
			controllers : ['Menus'],
			launch : function() {
				Ext.create('Ext.container.Viewport', {

							layout : 'fit',
							hideBorders : true,
							requires : ['AM.view.Header', 'AM.view.Menu',
									'AM.view.TabPanel', 'AM.view.South'],

							layout : 'fit',
							items : [{
								id : 'desk',
								layout : 'border',
								items : [Ext.create('AM.view.Header'),
										Ext.create('AM.view.Menu'),
										Ext.create('AM.view.TabPanel', {

													items : [{
																id : 'HomePage',
																title : '首页',
																xtype : 'panel',
																iconCls : 'home',
																layout : 'fit',
																bodyPadding : '0 5 5 5'
															}]

												}), Ext.create('AM.view.South')]
							}]

						});
			}
		});