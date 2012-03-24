

Ext.define('AM.store.Contracts', {
			extend : 'Ext.data.ux.Store',
			model : 'AM.model.Contract',

			autoLoad : false,
			proxy : {
				type : 'rest',
				url : 'contracts',
				reader : {
					type : 'json',
					root : 'contracts',
					successProperty : 'success'
				},

				writer : {
					type : 'json',
					writeAllFields : true,
					root : ''
				}
			}
		});
