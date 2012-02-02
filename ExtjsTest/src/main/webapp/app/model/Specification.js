Ext.define('AM.model.Specification', {
			extend : 'Ext.data.Model',
			idProperty : 'id',
			fields : [{
						name : 'specification',
						type : 'string'
					}, {
						name : 'typical',
						type : 'float'
						
					}, {
						name : 'reject',
						type : 'float'

					}, {
						name : 'id',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}]


			
		});