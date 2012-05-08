

Ext.define('AM.store.ContractItems', {
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
				extraParams : {
					byItems : true
					
				},
				writer : {
					type : 'json',
					writeAllFields : true,
					root : ''
				}
			}
		});
