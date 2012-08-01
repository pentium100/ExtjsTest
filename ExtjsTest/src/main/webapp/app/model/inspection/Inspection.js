Ext.define('AM.model.inspection.Inspection', {
			extend : 'Ext.data.Model',
			idProperty : 'id',
			fields : [{
						name : 'inspectionDate',
						type : 'date',
						dateFormat : 'Y-m-d H:i:s'
					}, {
						name : 'authority',
						type : 'string'

					}, {
						name : 'docNo',
						type : 'string'

					}, {
						name : 'original',
						type : 'boolean'

					}, {
						name : 'remark',
						type : 'string'
					}, {
						name : 'contracts',
						type : 'string'
					},  {
						name : 'model_tested',
						type : 'string'
					}, {
						name : 'id',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}],

			hasMany : [{
						model : 'AM.model.inspection.InspectionItem',
						name : 'items',
						storeConfig : {
							filterOnLoad : false
						}
					}]

		});