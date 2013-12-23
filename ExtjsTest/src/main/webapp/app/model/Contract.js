Ext.define('AM.model.Contract', {
			extend : 'Ext.data.Model',
			idProperty : 'id',
			fields : [{
						name : 'contractType',
						type : 'string'
					}, {
						name : 'lastShippingDate',
						type : 'date',
						dateFormat : 'Y-m-d H:i:s'
					}, {
						name : 'signDate',
						type : 'date',
						dateFormat : 'Y-m-d H:i:s'
					}, {
						name : 'supplier',
						type : 'string'

					}, {
						name : 'payTerm',
						type : 'string'

					}, {
						name : 'contractNo',
						type : 'string'
					}, {
						name : 'remark',
						type : 'string'

					},  {
						name : 'closed',
						type : 'boolean'

					}, {
						name : 'id',
						type : 'int'
					}, {
						name : 'version',
						type : 'int'
					}],

			hasMany : [{
						model : 'AM.model.ContractItem',
						name : 'items',
						storeConfig : {
							filterOnLoad : false
						}
					}],

			associations : [{
						type : 'hasOne',

						model : 'AM.model.master.employee.Employee',
						primaryKey : 'id',
						foreignKey : 'employee_id',
						associationKey : 'employee',
						getterName : 'getEmployee',
						setterName : 'setEmployee'
					}]

		});