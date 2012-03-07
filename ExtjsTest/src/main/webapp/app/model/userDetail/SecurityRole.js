Ext.define('AM.model.userDetail.SecurityRole', {
			extend : 'Ext.data.Model',
			idProperty: 'id',
			fields : [{
						name : 'roleName',
						type : 'string'
						
					},{
						name : 'id',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}]
					
					

		});