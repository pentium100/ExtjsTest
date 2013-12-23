Ext.define('AM.view.contract.List', {
			extend : 'Ext.grid.Panel',
			alias : 'widget.contractList',

			// title : 'All Contract',
			store : 'Contracts',

			requires : ['Ext.grid.plugin.CellEditing', 'Ext.form.field.Text',
					'Ext.toolbar.TextItem', 'Ext.ux.grid.FiltersFeature'],

			iconCls : 'icon-grid',
			frame : true,
			stateful : true,
			stateId : 'contractList',

			initComponent : function() {
				this.columns = [{
							header : '合同类型',
							dataIndex : 'contractType',
							filterable : true,
							filter : {
								type : 'list',
								store : Ext.widget('contractTypeStore'),
								phpMode : true

							}

						}, {
							header : '合同号',
							dataIndex : 'contractNo',
							filterable : true,
							width : 120
							// flex : 1
					}	, {
							header : '签约日期',
							dataIndex : 'signDate',
							xtype : 'datecolumn',
							format : 'Y-m-d',
							filterable : true,
							width : 80

						}, {
							header : '供应商',
							dataIndex : 'supplier',
							filterable : true,
							width : 240
						}, {
							header : '付款方式',
							dataIndex : 'payTerm',
							filterable : true
						}, {
							header : '业务员',
							dataIndex : 'payTerm',
							renderer : function(value, metaData, rec) {

								var emp = rec.getEmployee();
								return emp.get('name');

							},
							filter:{
							
								filterField:'employee.name'
							},
							filterable : true
							
							
						}, {
							header : '关闭',
							dataIndex : 'closed',
							filterable : true,
							flex : 1

						}, {
							header : '备注',
							dataIndex : 'remark',
							filterable : true,
							flex : 1

						}];

				this.dockedItems = [{
							xtype : 'toolbar',
							items : [{
										iconCls : 'icon-add',
										text : 'Add',
										scope : this,
										handler : this.onAddClick
									}, {
										iconCls : 'icon-delete',
										text : 'Delete',
										disabled : false,
										itemId : 'delete',
										scope : this,
										handler : this.onDeleteClick
									}]
						}, {
							xtype : 'pagingtoolbar',
							displayInfo : true,
							store : 'Contracts',
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