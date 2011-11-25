

Ext.define('AM.store.MaterialDocTypes', {
			extend : 'Ext.data.ux.Store',
			model : 'AM.model.MaterialDocType',

			autoLoad : true,
			proxy : {
				type : 'rest',
				url : 'materialdoctypes',
				reader : {
					type : 'json',
					root : 'materialdoctypes',
					successProperty : 'success'
				},

				writer : {
					type : 'json',
					writeAllFields : true,
					root : ''
				}
			}
		});
