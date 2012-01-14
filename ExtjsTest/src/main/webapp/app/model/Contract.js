Ext.define('AM.model.Contract', {
			extend : 'Ext.data.Model',
			idProperty: 'id',
			fields : [{
						name : 'contractType',
						type : 'string'
					}, {
						name : 'lastShippingDate',
						type : 'date',
						dateFormat : 'Y-m-d H:i:s'
					}, {
						name : 'contractNo',
						type : 'string'
					}, {
						name : 'id',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}],

			hasMany : [{
						model : 'AM.model.ContractItem',
						name : 'items'
					}]

		});