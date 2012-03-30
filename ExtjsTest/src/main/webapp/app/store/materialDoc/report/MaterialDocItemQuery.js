

Ext.define('AM.store.materialDoc.report.MaterialDocItemQuery', {
			extend : 'Ext.data.ux.Store',
			model : 'AM.model.materialDoc.report.MaterialDocItemQuery',
			remoteSort: true,

			proxy : {
				type : 'rest',
				url : 'reports/materialDocItemQuerys',
				reader : {
					type : 'json',
					root : 'materialDocItemQuerys',
					successProperty : 'success'
				}
			}
		});
