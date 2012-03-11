Ext.define('AM.model.inspection.report.InspectionDetail', {
			extend : 'Ext.data.Model',
			idProperty: 'id',
			fields : [{
						name : 'inspection_date',
						type : 'date',
						dateFormat : 'Y-m-d H:i:s'
					}, {
						name : 'authority',
						type : 'string'
						
					}, {
						name : 'doc_no',
						type : 'string'
						
					}, {
						name : 'original',
						type : 'boolean'
						
					},{
						name : 'remark',
						type : 'string'
					},{
						name : 'contract_no',
						type : 'string'
					},{
						name : 'supplier',
						type : 'string'
					},{
						name : 'model_contract',
						type : 'string'
					},{
						name : 'plate_num',
						type : 'string'
					},{
						name : 'batch_no',
						type : 'string'
					},{
						name : 'delivery_note',
						type : 'string'
					},{
						name : 'net_weight',
						type : 'float'
					},{
						name : 'si',
						type : 'float'
					},{
						name : 'fe',
						type : 'float'
					},{
						name : 'al',
						type : 'float'
					},{
						name : 'ca',
						type : 'float'
					},{
						name : 'p',
						type : 'float'
					},{
						name : 'remark',
						type : 'string'
					}
					
					
					]


		});