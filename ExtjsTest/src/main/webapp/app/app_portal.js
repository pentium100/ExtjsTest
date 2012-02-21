Ext.Loader.setConfig({
			enabled : true
		});
Ext.application({
			name : 'AM',
			appFolder : 'app',
			views: ['AM.portal.Portal_OA'],
			controllers : ['AM.controller.Messages'],
			launch : function() {
				Ext.create('AM.portal.Portal_OA');
			
			}
		});
		
		
		