Ext.define('AM.model.Contract', {
			extend : 'Ext.data.Model',
			fields : [{
						name : 'model',
						type : 'string'
					}, {
						name : 'quantity',
						type : 'float'
					}],
					
		associations : [{
						type : 'hasMany',
						model : 'ContractItem'
					}]					
		});