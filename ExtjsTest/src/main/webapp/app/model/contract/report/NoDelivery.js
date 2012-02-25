Ext.define('AM.model.contract.report.NoDelivery', {
			extend : 'Ext.data.Model',
			//idProperty: 'id',
			fields : [ {
						name : 'supplier',
						type : 'string'
						
					},{
						name : 'contractNo',
						type : 'string'
					}, {
						name : 'model_contract',
						type : 'string'
						
					},{
						name : 'quantity_in_contract',
						type : 'float'
					},{
						name : 'quantity_in_receipt',
						type : 'float'
					},{
						name : 'quantity_no_delivery',
						type : 'float'
					},{
						name : 'unitPrice',
						type : 'float'
					},{
						name : 'id',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}]


		});