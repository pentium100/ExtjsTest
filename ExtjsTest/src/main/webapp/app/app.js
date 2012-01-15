Ext.Loader.setConfig({
			enabled : true
		});
Ext.application({
			name : 'AM',
			appFolder : 'app',
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
										Ext.create('AM.view.TabPanel'),
										Ext.create('AM.view.South')]
							}]

						});
			}
		});