

Ext.define('AM.store.MaterialDocItems', {
			extend : 'Ext.data.Store',
			model : 'AM.model.MaterialDocItemSearch',

			autoLoad : true,
			proxy : {
				type : 'rest',
				url : 'materialdocitems/1',
				reader : {
					type : 'json',
					root : 'materialdocitems',
					successProperty : 'success'
				},

				writer : {
					type : 'json',
					writeAllFields : true,
					root : ''
				}
			}
		});
