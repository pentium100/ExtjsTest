Ext.application({
			name : 'AM',

			appFolder : 'app',

			controllers : ['Contracts'],

			launch : function() {

				Ext.JSON.encodeDate = function(d) {
					return Ext.Date.format(d, '"Y-m-d H:i:s"');
				};
				Ext.create('Ext.container.Viewport', {
							layout : 'fit',
							items : [{
										xtype : 'contractList',
										title : 'Contracts',
										html : 'List of users will go here'
									}]

						});
			}
		});