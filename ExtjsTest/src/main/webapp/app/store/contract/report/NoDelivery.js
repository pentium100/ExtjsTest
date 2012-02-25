

Ext.define('AM.store.contract.report.NoDelivery', {
			extend : 'Ext.data.ux.Store',
			model : 'AM.model.contract.report.NoDelivery',

			proxy : {
				type : 'rest',
				url : 'reports/noDelivery',
				reader : {
					type : 'json',
					root : 'noDeliverys',
					successProperty : 'success'
				}
			}
		});
