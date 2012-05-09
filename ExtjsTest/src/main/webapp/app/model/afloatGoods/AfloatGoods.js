Ext.define('AM.model.afloatGoods.AfloatGoods', {
			extend : 'Ext.data.Model',
			idProperty : 'id',
			fields : [{
						name : 'contract'

					}, {
						name : 'contractNo',
						mapping : 'contract.contractNo'

					}, {
						name : 'supplier',
						mapping : 'contract.supplier'

					}, {
						name : 'plateNum',
						type : 'string'

					}, {
						name : 'dispatch',
						type : 'string'

					}, {
						name : 'destination',
						type : 'string'

					}, {
						name : 'transportDate',
						type : 'date',
						dateFormat : 'Y-m-d H:i:s'

					}, {
						name : 'dispatchDate',
						type : 'date',
						dateFormat : 'Y-m-d H:i:s'
					}, {
						name : 'eta',
						type : 'date',
						dateFormat : 'Y-m-d H:i:s'

					}, {
						name : 'arrivalDate',
						type : 'date',
						dateFormat : 'Y-m-d H:i:s'
					}, {
						name : 'original',
						type : 'boolean'

					}, {
						name : 'remark',
						type : 'string'
					}, {
						name : 'id',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}],

			hasMany : [{
						model : 'AM.model.afloatGoods.AfloatGoodsItem',
						name : 'items',
						storeConfig : {
							filterOnLoad : false
						}
					}]

		});