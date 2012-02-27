

Ext.define('AM.store.contract.report.OpenOrder', {
			extend : 'Ext.data.ux.Store',
			model : 'AM.model.contract.report.OpenOrder',
			remoteSort: true,

			proxy : {
				type : 'rest',
				url : 'reports/openOrders',
				reader : {
					type : 'json',
					root : 'openOrders',
					successProperty : 'success'
				}
			}
		});
