
Ext.require(['Ext.state.Manager','Ext.state.CookieProvider' ]);

Ext.application({
			name : 'AM',
			appFolder : 'app',
			controllers : ['Menus'],
			
			launch : function() {

				Ext.Ajax.request({
							url : 'menus/getUserInfo',
							params : {

				}			,
							success : function(response) {

								var user = Ext.JSON
										.decode(response.responseText);
								_DEFAULT_USER_NAME = user.userName;
								_DEFAULT_USER_LEVEL = user.userLevel;

								var bottom = Ext.getCmp("bottom");
								if (bottom != undefined) {

									bottom.items.get(0).setText('当前用户: '
											+ user.fullName);
								}

							}
						});
				Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
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