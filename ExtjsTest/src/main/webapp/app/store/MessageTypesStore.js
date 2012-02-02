Ext.define('AM.store.MessageTypesStore', {
			extend : 'Ext.data.Store',
			alias : 'widget.messageTypesStore',
			fields : ['id', 'text'],
			data : [{
						id : "供应",
						text : "供应"
					}, {
						id : "需求",
						text : "需求"
					}, {
						id : "敞口",
						text : "敞口"
					}, {
						id : "锁定",
						text : "锁定"
					}

			]
		});