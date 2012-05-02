Ext.define('AM.model.master.stockLocation.StockLocation', {
			extend : 'Ext.data.Model',
			idProperty: 'id',
			fields : [{
						name : 'stockLocation',
						type : 'string'
					}, {
						name : 'id',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}]


		});