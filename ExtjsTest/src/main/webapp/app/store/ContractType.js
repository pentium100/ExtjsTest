Ext.define('AM.store.ContractType', {
			extend : 'Ext.data.Store',
			alias : 'widget.contractTypeStore',
			fields : ['id', 'text'],
			data : [{
						id : "0",
						text : "采购合同"
					}, {
						id : "1",
						text : "销售合同"
					}

			]
		});