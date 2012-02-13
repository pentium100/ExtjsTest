Ext.Loader.setConfig({
			enabled : true
		});
Ext.application({
			name : 'AM',
			appFolder : 'app',
			controllers : ['AM.controller.Messages'],
			launch : function() {
				Ext.create('Ext.container.Viewport', {

							layout : 'fit',
							hideBorders : true,
							layout : 'fit',
							items : [Ext.create('AM.view.message.MessageList')]

						});
			}
		});