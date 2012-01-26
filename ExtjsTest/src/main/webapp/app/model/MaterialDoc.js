Ext.define('AM.model.MaterialDoc', {
			extend : 'Ext.data.Model',
			idProperty : 'docNo',
			fields : [{
						name : 'deliveryNote',
						type : 'string'
					}, {
						name : 'docDate',
						type : 'date',
						dateFormat : 'Y-m-d H:i:s'
					}, {
						name : 'plateNum',
						type : 'string'

					}, {
						name : 'batchNo',
						type : 'string'

					}, {
						name : 'workingNo',
						type : 'string'
					}, {
						name : 'contract'
						
					}, {
						name : 'docType'
						
						
					}, {
						name : 'docType_txt',
						mapping : 'docType.docType_txt'
						
						
					}, {
						name : 'contractNo',
						type : 'string',
						mapping:'contract.contractNo'
						//convert : function(value, record) {
						//	return record.get("contract").contractNo;
						//}
					}, {
						name : 'docNo',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}],
			hasMany : [{
						model : 'AM.model.MaterialDocItem',
						name : 'items'
					}],

			associations : [{
						type : 'belongsTo',
						model : 'AM.model.Contract',
						primaryKey : 'id',
						autoLoad:true,
						getterName: 'getContract',
						foreignKey : 'contract'

					},{
						type : 'belongsTo',
						model : 'AM.model.MaterialDocType',
						primaryKey : 'id',
						autoLoad:true,
						foreignKey : 'docType'

					}]

		});