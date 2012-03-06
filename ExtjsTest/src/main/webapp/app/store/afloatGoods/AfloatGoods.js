

Ext.define('AM.store.afloatGoods.AfloatGoods', {
			extend : 'Ext.data.ux.Store',
			model : 'AM.model.afloatGoods.AfloatGoods',

			autoLoad : true,
			proxy : {
				type : 'rest',
				url : 'afloatgoodses',
				reader : {
					type : 'json',
					root : 'afloatGoods',
					successProperty : 'success'
				},

				writer : {
					type : 'json',
					writeAllFields : true,
					root : ''
				}
			}
		});
