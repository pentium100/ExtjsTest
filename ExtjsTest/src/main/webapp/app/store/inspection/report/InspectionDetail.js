

Ext.define('AM.store.inspection.report.InspectionDetail', {
			extend : 'Ext.data.ux.Store',
			model : 'AM.model.inspection.report.InspectionDetail',
			remoteSort: true,

			proxy : {
				type : 'rest',
				url : 'reports/inspectionDetails',
				reader : {
					type : 'json',
					root : 'inspectionDetails',
					successProperty : 'success'
				}
			}
		});
