Ext.define('AM.view.master.employee.EmployeeList', {
			extend : 'Ext.grid.Panel',
			alias : 'widget.employeeList',

			// title : 'All Contract',
			store : 'master.employee.Employees',

			requires : ['Ext.grid.plugin.CellEditing', 'Ext.form.field.Text',
					'Ext.toolbar.TextItem'],

			iconCls : 'icon-grid',
			frame : true,
			stateful: true,
   			stateId: 'employeeList',		

			initComponent : function() {
				this.plugins = [Ext.create('Ext.grid.plugin.CellEditing', {

				})];
				
				this.columns = [{
							header : 'id',
							dataIndex : 'id'
							
						}, {
							header : '业务员',
							dataIndex : 'name'
							,filterable : true
							,field: 'textfield'
						}];

				this.dockedItems = [{
							xtype : 'toolbar',
							items : [{
										iconCls : 'icon-add',
										text : 'Add',
										action: 'add'
										
										
										
									}, {
										iconCls : 'icon-delete',
										text : 'Delete',
										action : 'delete'
										
										
									}, {
										iconCls : 'icon-save',
										text : 'Save',
										action : 'save'
										
										
									}]
						}, {
							xtype : 'pagingtoolbar',
							displayInfo : true,
							store : 'master.employee.Employees',
							dock : 'bottom'
						}

				];
				
				this.features = [{
					ftype : 'filters',
					// encode and local configuration options defined
					// previously for easier reuse
					encode : true, // json encode the filter query
					local : false
						// defaults to false (remote filtering)

					}];
				

				this.callParent(arguments);
			}
		});