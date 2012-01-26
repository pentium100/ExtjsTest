

Ext.define('AM.store.MaterialDocs', {
			extend : 'Ext.data.Store',
			model : 'AM.model.MaterialDoc',

			autoLoad : true,
			proxy : {
				type : 'rest',
				url : 'materialdocs',
				reader : {
					type : 'json',
					root : 'materialdocs',
					successProperty : 'success'
				},

				writer : {
					type : 'json',
					writeAllFields : true,
					root : ''
				}
			}
		});
