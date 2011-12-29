Ext.define('AM.model.Supplier', {
			extend : 'Ext.data.Model',
			fields : [{
						name : 'name',
						type : 'string'
					}, {
						name : 'id',
						type : 'int'
					}],

			associations : [{
						type : 'belongsTo',
						model : 'Contract'
					}]
		});