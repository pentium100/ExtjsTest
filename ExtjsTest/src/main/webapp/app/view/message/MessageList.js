Ext.define('AM.view.message.MessageList', {
			extend : 'Ext.grid.Panel',
			alias : 'widget.messageList',

			// title : 'All Contract',
			

			requires : ['Ext.grid.plugin.CellEditing', 'Ext.form.field.Text',
					'Ext.toolbar.TextItem', 'Ext.ux.grid.FiltersFeature'],

			iconCls : 'icon-grid',
			frame : true,
            store : 'MessagesStore',
			initComponent : function() {
				
				this.columns = [{
							header : '类别',
							dataIndex : 'type',
							flex : 1,
							filter : {
								type : 'list',
								store: Ext.widget('messageTypesStore'),
								phpMode: true

							},
							
						}, {
							header : '品名',
							dataIndex : 'article',
							flex : 1,
							filterable : true
						}, {
							header : '数量',
							dataIndex : 'quantity',
							filterable : true,
							flex : 1
						}, {
							header : '装港laycan',
							dataIndex : 'departure',
							filterable : true,
							flex : 1
						}, {
							header : 'ETA卸港',
							dataIndex : 'eta',
							filterable : true,
							flex : 1
						}, {
							header : '供应商',
							dataIndex : 'supplier',
							filterable : true,
							flex : 1
						}, {
							header : '有效日期',
							dataIndex : 'validBefore',
							filterable : true,
							xtype : 'datecolumn',
							format : 'Y-m-d',
							flex : 1
						}, {
							header : '负责业务员',
							dataIndex : 'owner',
							filterable : true,
							flex : 1
						}, {
							header : '成本价',
							dataIndex : 'costPrice',
							filterable : true,
							flex : 1
						}, {
							header : '建议销售价',
							dataIndex : 'suggestedPrice',
							filterable : true,
							flex : 1
						}, {
							header : '备注',
							dataIndex : 'remark',
							filterable : true,
							flex : 4
						}, {
							header : '紧急',
							dataIndex : 'isUrgent',
							filterable : true,
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