Ext.define('AM.model.MaterialDocItem', {
			extend : 'Ext.data.Model',
			idProperty : 'lineId',
			
			proxy : {
				type : 'rest',
				url : 'materialdocitems/1',
				reader : {
					type : 'json',
					root : 'materialdocitems',
					successProperty : 'success'
				}
			},
			
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
						//associatedName : 'lineId_in',
						getterName : 'getLineId_in',
						setterName : 'setLineId_in',
						primaryKey: 'lineId',
						foreignKey: 'lineId_in_key'
					},{
						type : 'belongsTo',
						model : 'AM.model.MaterialDoc',
						primaryKey: 'docNo',
						foreignKey: 'materialDoc'
					}]

			
		});