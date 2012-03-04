

Ext.define('AM.store.materialDoc.report.StockQuery', {
			extend : 'Ext.data.ux.Store',
			model : 'AM.model.materialDoc.report.StockQuery',
			remoteSort: true,

			proxy : {
				type : 'rest',
				url : 'reports/stockQuerys',
				reader : {
					type : 'json',
					root : 'stockQuerys',
					successProperty : 'success'
				}
			}
		});
