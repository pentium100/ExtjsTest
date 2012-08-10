Ext.define('AM.model.master.employee.Employee', {
			extend : 'Ext.data.Model',
			idProperty: 'id',
			fields : [{
						name : 'name',
						type : 'string'
					}, {
						name : 'id',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}]


		});