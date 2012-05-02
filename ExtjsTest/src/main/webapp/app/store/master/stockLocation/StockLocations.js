

Ext.define('AM.store.master.stockLocation.StockLocations', {
			extend : 'Ext.data.ux.Store',
			model : 'AM.model.master.stockLocation.StockLocation',

			autoLoad : true,
			proxy : {
				type : 'rest',
				url : 'stocklocations',
				reader : {
					type : 'json',
					root : 'stockLocations',
					successProperty : 'success'
				},

				writer : {
					type : 'json',
					writeAllFields : true,
					root : ''
				}
			}
		});
