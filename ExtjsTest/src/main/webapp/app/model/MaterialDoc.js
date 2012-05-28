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
				name : 'invNo',
				type : 'string'

			}, {
				name : 'batchNo',
				type : 'string'

			}, {
				name : 'workingNo',
				type : 'string'
			}, {
				name : 'cause',
				type : 'string'
			}, {
				name : 'contract'

			}, {
				name : 'docType'

			}, {
				name : 'targetWarehouse',
				type : 'string'

			},

			{
				name : 'docType_txt',
				mapping : 'docType.docType_txt'

			}, {
				name : 'contractNo',
				type : 'string',
				mapping : 'contract.contractNo'
			}, {
				name : 'docNo',
				type : 'int'
			}, {
				name : 'version',
				type : 'int'
			}],
	hasMany : [{
				model : 'AM.model.MaterialDocItem',
				name : 'items',
				primaryKey : 'lineId',
				storeConfig : {
					filterOnLoad : false
				}
			}],

	associations : [{
				type : 'hasOne',

				model : 'AM.model.master.stockLocation.StockLocation',
				primaryKey : 'id',
				foreignKey : 'stockLocation_id',
				associationKey : 'targetStockLocation',
				getterName : 'getStockLocation',
				setterName : 'setStockLocation'
			}],

	belongsTo : [{
				model : 'AM.model.MaterialDocType',
				name : 'docType',
				foreignKey : 'docType',
				getterName : 'getDocType',
				setterName : 'setDocType'

			}, {
				model : 'AM.model.Contract',
				name : 'contract',
				foreignKey : 'contract'

				,
				getterName : 'getContract',
				setterName : 'setContract'
			}

	]

		// associations : [{
		// type : 'hasMany',
		// model : 'AM.model.MaterialDocItem',
		// name : 'items'

		// }, {
		// type : 'belongsTo',
		// model : 'AM.model.Contract',
		// primaryKey : 'id',
		// // autoLoad:true,
		// getterName : 'getContract',
		// foreignKey : 'contract'

		// }, {
		// type : 'belongsTo',
		// model : 'AM.model.MaterialDocType',
		// primaryKey : 'id',
		// getterName : 'getDocType',
		// autoLoad:true,
		// foreignKey : 'docType'

		// }]

	});