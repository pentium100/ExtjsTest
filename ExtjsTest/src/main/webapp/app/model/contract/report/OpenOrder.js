Ext.define('AM.model.contract.report.OpenOrder', {
			extend : 'Ext.data.Model',
			//idProperty: 'id',
			fields : [ {
						name : 'model',
						type : 'string'
						
					},{
						name : 'quantity_purchases',
						type : 'float'
					}, {
						name : 'quantity_sales',
						type : 'float'
						
					}, {
						name : 'quantity_open',
						type : 'float'
						
					}]


		});