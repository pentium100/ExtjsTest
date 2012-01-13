Ext.define('AM.model.ContractItem', {
			extend : 'Ext.data.Model',
			fields : [{
						name : 'model',
						type : 'string'
					}, {
						name : 'quantity',
						type : 'float'
					}, {
						name : 'id',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}]
					
		});