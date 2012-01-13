Ext.define('AM.view.contract.List', {
			extend : 'Ext.grid.Panel',
			alias : 'widget.contractList',

			title : 'All Contract',
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
									disabled : true,
									itemId : 'delete',
									scope : this,
									handler : this.onDeleteClick
								}]
					}],

			initComponent : function() {
				this.columns = [{
							header : 'Contract Type',
							dataIndex : 'contractType',
							flex : 1
						}, {
							header : 'Last Shipping Date',
							dataIndex : 'lastShippingDate',
							xtype : 'datecolumn',
							flex : 1,
							format : 'Y-m-d'
						}, {
							header : 'Contract No',
							dataIndex : 'contractNo',
							flex : 1
						}];

				this.callParent(arguments);
			}
		});