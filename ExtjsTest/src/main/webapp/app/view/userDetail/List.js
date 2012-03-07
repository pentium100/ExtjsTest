Ext.define('AM.view.userDetail.List', {
			extend : 'Ext.grid.Panel',
			alias : 'widget.userDetailList',

			// title : 'All Contract',
			store : 'userDetail.UserDetail',

			requires : ['Ext.grid.plugin.CellEditing', 'Ext.form.field.Text',
					'Ext.toolbar.TextItem'],

			iconCls : 'icon-grid',
			frame : true,

			initComponent : function() {
				this.columns = [{
							header : '用户名',
							dataIndex : 'userName',
							flex : 1
							
						}, {
							header : '全名',
							dataIndex : 'fullName',
							flex : 1
							,filterable : true
						}, {
							header : '激活',
							dataIndex : 'enabled',
							flex : 1
							,filterable : true
						}, {
							header : '用户级别',
							dataIndex : 'userLevel',
							flex : 1
							,filterable : true
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
										
										
									}]
						}, {
							xtype : 'pagingtoolbar',
							displayInfo : true,
							store : 'userDetail.UserDetail',
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