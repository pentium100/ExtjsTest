Ext.define('AM.model.afloatGoods.AfloatGoods', {
			extend : 'Ext.data.Model',
			idProperty: 'id',
			fields : [{
						name : 'contract'
						
					}, {
						name : 'contractNo',
						mapping: 'contract.contractNo'
						
					}, {
						name : 'supplier',
						mapping:'contract.supplier'
						
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
						dateFormat: 'Y-m-d'
						
					}, {
						name : 'dispatchDate',
						type : 'date',
						dateFormat : 'Y-m-d'
					}, {
						name : 'eta',
						type : 'date',
						dateFormat: 'Y-m-d'
						
					}, {
						name : 'arrivDate',
						type : 'date',
						dateFormat: 'Y-m-d'
					},{
						name : 'remark',
						type : 'string'
					},{
						name : 'id',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}],

			hasMany : [{
						model : 'AM.model.afloatGoods.AfloatGoodsItem',
						name : 'items'
					}]

		});