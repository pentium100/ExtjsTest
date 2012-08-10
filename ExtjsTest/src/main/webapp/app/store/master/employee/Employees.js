

Ext.define('AM.store.master.employee.Employees', {
			extend : 'Ext.data.ux.Store',
			model : 'AM.model.master.employee.Employee',

			autoLoad : true,
			proxy : {
				type : 'rest',
				url : 'employees',
				reader : {
					type : 'json',
					root : 'employees',
					successProperty : 'success'
				},

				writer : {
					type : 'json',
					writeAllFields : true,
					root : ''
				}
			}
		});
