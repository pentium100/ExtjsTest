Ext.define('AM.model.MaterialDocItem', {
			extend : 'Ext.data.Model',
			idProperty : 'lineId',
			fields : [{
						name : 'moveType',
						type : 'string'
					}, {
						name : 'remark',
						type : 'string'
						
					}, {
						name : 'model_contract',
						type : 'string'
					}, {
						name : 'model_tested',
						type : 'string'

					}, {
						name : 'grossWeight',
						type : 'float'
					}, {
						name : 'netWeight',
						type : 'float'
					}, {
						name : 'warehouse',
						type : 'string'
					}, {
						name : 'direction',
						type : 'int'
					}, {
						name : 'lineId',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}
					
					],

			associations : [{
						type : 'belongsTo',
						model : 'AM.model.MaterialDocItem',
						primaryKey: 'lineId',
						foreignKey: 'lineId_in'
					},{
						type : 'belongsTo',
						model : 'AM.model.MaterialDoc',
						primaryKey: 'docNo',
						foreignKey: 'materialDoc'
					}]

			
		});