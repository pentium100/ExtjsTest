Ext.define('AM.model.inspection.InspectionItem', {
			extend : 'Ext.data.Model',
			idProperty: 'id',
			fields : [{
						name : 'netWeight',
						type : 'float'
					}, {
						name : 'si',
						type : 'float'
					}, {
						name : 'fe',
						type : 'float'
					}, {
						name : 'al',
						type : 'float'
					}, {
						name : 'ca',
						type : 'float'
					}, {
						name : 'p',
						type : 'float'
					}, {
						name : 'remark',
						type : 'string'
					}, {
						name : 'id',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					},{
						name: 'materialDocItem'
					}
					
					, {
						name : 'contractNo',
						type : 'string'
					}
					
					, {
						name : 'model_contract',
						type : 'string'
					}
					
					, {
						name : 'plateNum',
						type : 'string'
					}
					, {
						name : 'batchNo',
						type : 'string'
					}
					, {
						name : 'deliveryNote',
						type : 'string'
					}
					
					
					
					]
					
					
					
		});