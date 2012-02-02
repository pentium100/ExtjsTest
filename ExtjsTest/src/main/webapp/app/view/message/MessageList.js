Ext.define('AM.view.message.MessageList', {
			extend : 'Ext.grid.Panel',
			alias : 'widget.messageList',

			// title : 'All Contract',
			store : 'MessagesStore',

			requires : ['Ext.grid.plugin.CellEditing', 'Ext.form.field.Text',
					'Ext.toolbar.TextItem'],

			iconCls : 'icon-grid',
			frame : true,

			initComponent : function() {
				this.columns = [{
							header : '类别',
							dataIndex : 'type',
							flex : 1
						}, {
							header : '品名',
							dataIndex : 'article',
							flex : 1
						}, {
							header : '数量',
							dataIndex : 'quantity',
							flex : 1
						}, {
							header : '装港laycan',
							dataIndex : 'departure',
							flex : 1
						}, {
							header : 'ETA卸港',
							dataIndex : 'eta',
							flex : 1
						}, {
							header : '供应商',
							dataIndex : 'supplier',
							flex : 1
						}, {
							header : '有效日期',
							dataIndex : 'validBefore',
							xtype : 'datecolumn',
							format : 'Y-m-d',
							flex : 1
						}, {
							header : '负责业务员',
							dataIndex : 'owner',
							flex : 1
						}, {
							header : '成本价',
							dataIndex : 'costPrice',
							flex : 1
						}, {
							header : '建议销售价',
							dataIndex : 'suggestedPrice',
							flex : 1
						}, {
							header : '备注',
							dataIndex : 'remark',
							flex : 4
						}, {
							header : '紧急',
							dataIndex : 'isUrgent',
							flex : 1
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
							store : 'MessagesStore',
							dock : 'bottom'
						}

				];

				this.callParent(arguments);
			}
		});