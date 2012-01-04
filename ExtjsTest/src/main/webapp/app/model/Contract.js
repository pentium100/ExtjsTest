Ext.define('AM.model.Contract', {
			extend : 'Ext.data.Model',
			fields : [{
						name : 'contractType',
						type : 'string'
					}, {
						name : 'eta',
						type : 'date'
					}, {
						name : 'contractNo',
						type : 'string'
					}],
		associations : [{
						type  : 'hasMany',
						model : 'ContractItem',
						name  : 'contractItems'
					}]					
					
					
		});