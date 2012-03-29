Ext.define('AM.model.inspection.InspectionItem', {
			extend : 'Ext.data.Model',
			idProperty: 'id',
			fields : [{
						name : 'netWeight',
						type : 'float',
						useNull:true
					}, {
						name : 'si',
						type : 'float',
						useNull:true
					}, {
						name : 'fe',
						type : 'float',
						useNull:true
					}, {
						name : 'al',
						type : 'float',
						useNull:true
					}, {
						name : 'ca',
						type : 'float',
						useNull:true
					}, {
						name : 'p',
						type : 'float',
						useNull:true
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