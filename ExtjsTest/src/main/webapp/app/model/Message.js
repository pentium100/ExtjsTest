Ext.define('AM.model.Message', {
			extend : 'Ext.data.Model',
			idProperty : 'id',
			fields : [{
						name : 'department',
						type : 'string'
					}, {
						name : 'group',
						type : 'string'
						
					}, {
						name : 'type',
						type : 'string'
						
					}, {
						name : 'article',
						type : 'string'

					}, {
						name : 'quantity',
						type : 'float'

					}, {
						name : 'departure',
						type : 'string'

					}, {
						name : 'eta',
						type : 'string'

					}, {
						name : 'supplier',
						type : 'string'

					}, {
						name : 'owner',
						type : 'string'

					}, {
						name : 'costPrice',
						type : 'float'

					}, {
						name : 'suggestedPrice',
						type : 'float'

					}, {
						name : 'remark',
						type : 'string'

					}, {
						name : 'validBefore',
						type : 'date'

					}, {
						name : 'isUrgent',
						type : 'boolean'

					}, {
						name : 'id',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}],
			hasMany : [{
				model : 'AM.model.Specification',
				name : 'specifications'
				
			}]
			
				


			
		});