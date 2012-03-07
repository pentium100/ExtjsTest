Ext.define('AM.model.userDetail.UserDetail', {
			extend : 'Ext.data.Model',
			idProperty: 'id',
			fields : [{
						name : 'userName',
						type : 'string'
						
					}, {
						name : 'password',
						type : 'string'
						
					}, {
						name : 'fullName',
						type : 'string'
						
					}, {
						name : 'userLevel',
						type : 'int'
						
					}, {
						name : 'enabled',
						type : 'boolean'
						
					}, {
						name : 'id',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}],
					
			hasMany : [{
						model : 'AM.model.userDetail.SecurityRole',
						name : 'roles'
					}]
					

		});