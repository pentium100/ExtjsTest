Ext.define('AM.model.MaterialDocType', {
			extend : 'Ext.data.Model',
			idProperty : 'id',
			fields : [{
						name : 'docType_txt',
						type : 'string'
					}, {
						name : 'id',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}]
		});