

Ext.define('AM.store.MessagesStore', {
			extend : 'Ext.data.Store',
			model : 'AM.model.Message',

			autoLoad : true,
			proxy : {
				type : 'rest',
				url : 'messages',
				reader : {
					type : 'json',
					root : 'messages',
					successProperty : 'success'
				},

				writer : {
					type : 'json',
					writeAllFields : true,
					root : ''
				}
			}
		});
