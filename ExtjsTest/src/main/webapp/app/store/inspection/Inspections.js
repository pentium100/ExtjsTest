

Ext.define('AM.store.inspection.Inspections', {
			extend : 'Ext.data.ux.Store',
			model : 'AM.model.inspection.Inspection',

			autoLoad : true,
			proxy : {
				type : 'rest',
				url : 'inspections',
				reader : {
					type : 'json',
					root : 'inspections',
					successProperty : 'success'
				},

				writer : {
					type : 'json',
					writeAllFields : true,
					root : ''
				}
			}
		});
