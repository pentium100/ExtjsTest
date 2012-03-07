

Ext.define('AM.store.userDetail.UserDetail', {
			extend : 'Ext.data.ux.Store',
			model : 'AM.model.userDetail.UserDetail',

			autoLoad : true,
			proxy : {
				type : 'rest',
				url : 'userdetails',
				reader : {
					type : 'json',
					root : 'userDetails',
					successProperty : 'success'
				},

				writer : {
					type : 'json',
					writeAllFields : true,
					root : ''
				}
			}
		});
