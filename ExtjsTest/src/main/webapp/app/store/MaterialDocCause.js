Ext.define('AM.store.MaterialDocCause', {
			extend : 'Ext.data.Store',
			alias : 'widget.materialDocCauseStore',
			fields : ['id', 'text'],
			data : [{
						id : "1",
						text : "销售"
					}, {
						id : "2",
						text : "退货"
					}, {
						id : "3",
						text : "货损"
					}

			]
		});