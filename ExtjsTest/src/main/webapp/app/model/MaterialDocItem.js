Ext.define('AM.model.MaterialDocItem', {
	extend : 'Ext.data.Model',
	idProperty : 'lineId',

	proxy : {
		type : 'rest',
		url : 'materialdocitems/1',
		reader : {
			type : 'json',
			root : 'materialdocitems',
			successProperty : 'success'
		}
	},

	fields : [ {
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
		type : 'float',
		useNull : true
	}, {
		name : 'netWeight',
		type : 'float',
		useNull : true
	}, {
		name : 'lots',
		type : 'float',
		useNull : true
	}, {
		name : 'warehouse',
		type : 'string'
	}, {
		name : 'direction',
		type : 'int'
	}, {
		name : 'lineId',
		type : 'int'
	}, {
		name : 'version',
		type : 'int'
	}, {
		name : 'lineId_in'

	}

	, {
		name : 'contractNo',
		persist : false
	}, {
		name : 'batchNo',
		persist : false
	}, {
		name : 'plateNum',
		persist : false
	}, {
		name : 'deliveryNote',
		persist : false
	}, {
		name : 'workingNo',
		persist : false
	} , {
		name : 'usedQuantity',
		persist : false
	} ],

	associations : [ {
		type : 'hasOne',

		model : 'AM.model.master.stockLocation.StockLocation',
		primaryKey : 'id',
		foreignKey : 'stockLocation_id',
	    associationKey : 'stockLocation',
	    getterName: 'getStockLocation', 
        setterName: 'setStockLocation' 
	},

	{
		type : 'belongsTo',
		model : 'AM.model.MaterialDoc',
		primaryKey : 'docNo',
		foreignKey : 'materialDoc'
	} ]

});