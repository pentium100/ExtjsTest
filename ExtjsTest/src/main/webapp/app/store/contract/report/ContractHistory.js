

Ext.define('AM.store.contract.report.ContractHistory', {
			extend : 'Ext.data.ux.Store',
			model : 'AM.model.contract.report.ContractHistory',
			remoteSort: true,

			proxy : {
				type : 'rest',
				url : 'reports/contractHistorys',
				reader : {
					type : 'json',
					root : 'contractHistorys',
					successProperty : 'success'
				}
			}
		});
