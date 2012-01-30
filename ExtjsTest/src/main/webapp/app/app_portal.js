Ext.Loader.setConfig({
			enabled : true
		});
Ext.application({
			name : 'AM',
			appFolder : 'app',
			controllers : ['Menus'],
			views: ['AM.portal.Portal'],
			launch : function() {
				Ext.create('AM.portal.Portal');
			
			}
		});
		
		
		