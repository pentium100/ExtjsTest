Ext.define('AM.model.ContractItem', {
			extend : 'Ext.data.Model',
			idProperty : 'id',
			fields : [{
						name : 'model',
						type : 'string'
					}, {
						name : 'quantity',
						type : 'float',
						useNull : true
					}, {
						name : 'unitPrice',
						type : 'float',
						useNull : true
					}, {
						name : 'remark',
						type : 'string'
					}, {
						name : 'usedQuantity',
						persist : false
					}, {
						name : 'id',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}]

		});