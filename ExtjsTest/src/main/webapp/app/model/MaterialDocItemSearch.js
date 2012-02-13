Ext.define('AM.model.MaterialDocItemSearch', {
			extend : 'Ext.data.Model',
			idProperty : 'lindId',
			fields : [{
						name : 'moveType',
						type : 'string'
					}, {
						name : 'remark',
						type : 'string'

					}, {
						name : 'model_contract',
						type : 'string'
					}, {
						name : 'model_tested',
						type : 'string'

					}, {
						name : 'grossWeight',
						type : 'float'
					}, {
						name : 'netWeight',
						type : 'float'
					}, {
						name : 'warehouse',
						type : 'string'
					}, {
						name : 'lineId',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}, {
						name : 'contractNo',
						mapping : 'materialDoc.contract.contractNo'
					}, {
						name : 'plateNum',
						mapping : 'materialDoc.plateNum'
					}, {
						name : 'deliveryNote',
						mapping : 'materialDoc.deliveryNote'
					}, {
						name : 'batchNo',
						mapping : 'materialDoc.batchNo'
					}, {
						name : 'docDate',
						mapping : 'materialDoc.docDate'
					}

			],

			associations : [{
						type : 'belongsTo',
						model : 'AM.model.MaterialDocItem',
						primaryKey : 'lineId',
						foreignKey : 'lineId_in'
					}, {
						type : 'belongsTo',
						model : 'AM.model.MaterialDoc',
						primaryKey : 'docNo',
						foreignKey : 'materialDoc'
					}]

		});
