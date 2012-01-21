Ext.define('AM.view.contract.List', {
			extend : 'Ext.grid.Panel',
			alias : 'widget.contractList',

			// title : 'All Contract',
			store : 'Contracts',

			requires : ['Ext.grid.plugin.CellEditing', 'Ext.form.field.Text',
					'Ext.toolbar.TextItem'],

			iconCls : 'icon-grid',
			frame : true,

			dockedItems : [{
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
						dock : 'bottom'
					}

			],

			initComponent : function() {
				this.columns = [{
							header : '合同类型',
							dataIndex : 'contractType',
							flex : 1
						}, {
							header : '合同号',
							dataIndex : 'contractNo',
							flex : 1
						}, {
							header : '供应商',
							dataIndex : 'supplier',
							flex : 1
						}, {
							header : '付款方式',
							dataIndex : 'payTerm',
							flex : 1
						}, {
							header : '备注',
							dataIndex : 'remark',
							flex : 4
						}];

				this.callParent(arguments);
			}
		});