Ext.define('AM.model.afloatGoods.report.AfloatGoodsDetail', {
			extend : 'Ext.data.Model',
			idProperty: 'id',
			fields : [{
						name : 'contract_no',
						type : 'string'
						
					}, {
						name : 'supplier',
						type : 'string'
						
					}, {
						name : 'model',
						type : 'string'
						
					}, {
						name : 'destination',
						type : 'string'
						
					}, {
						name : 'dispatch',
						type : 'string'
						
					}, {
						name : 'transport_date',
						type : 'string',
						dateFormat : 'Y-m-d H:i:s'
						
					},{
						name : 'original',
						type : 'boolean'
						
					},{
						name : 'remark',
						type : 'string'
					},{
						name : 'batch_no',
						type : 'string'
					},{
						name : 'quantity',
						type : 'float'
					},{
						name : 'dispatch_date',
						type : 'date',
						dateFormat: 'Y-m-d H:i:s'
					},{
						name : 'plate_num',
						type : 'string'
					},{
						name : 'eta',
						type : 'date',
						dateFormat: 'Y-m-d H:i:s'
					},{
						name : 'beyond_days',
						type : 'number'
					},{
						name : 'arrival_date',
						type : 'date',
						dateFormat:'Y-m-d H:i:s'
					}
					
					
					]


		});