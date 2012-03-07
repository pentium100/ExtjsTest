Ext.define('AM.model.afloatGoods.AfloatGoodsItem', {
			extend : 'Ext.data.Model',
			idProperty: 'id',
			fields : [{
						name : 'model',
						type : 'string'
					}, {
						name : 'quantity',
						type : 'float'
					}, {
						name : 'batchNo',
						type : 'string'
					}, {
						name : 'id',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}]
					
		});