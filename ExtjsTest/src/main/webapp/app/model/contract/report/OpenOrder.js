Ext.define('AM.model.contract.report.OpenOrder', {
			extend : 'Ext.data.Model',
			idProperty: 'model',
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
						
					}, {
						name : 'memo',
						type : 'string'
						
					}, {
						name : 'update_user',
						type : 'string'
						
					}, {
						name : 'update_time',
						type : 'date',
						dateFormat : 'Y-m-d H:i:s'
						
					}]


		});