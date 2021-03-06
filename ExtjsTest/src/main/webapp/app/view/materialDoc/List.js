Ext.define('AM.view.materialDoc.List', {
			extend : 'Ext.grid.Panel',
			alias : 'widget.materialDocList',

			// title : 'All Contract',
			store : 'MaterialDocs',

			requires : ['Ext.grid.plugin.CellEditing', 'Ext.form.field.Text',
					'Ext.toolbar.TextItem'],

			iconCls : 'icon-grid',
			frame : true,
			stateful : true,
			stateId : 'materialDocList',

			initComponent : function() {
				this.columns = [{
							header : '单据类型',
							dataIndex : 'docType_txt'

						}, {
							//xtype:'numbercolumn',
							header : '单据号',
							dataIndex : 'docNo',
							filterable : true
						}, {
							header : '合同号',
							dataIndex : 'contractNo',
							width : 120,
							filterable : true
						}, {
							header : '进仓单号',
							dataIndex : 'deliveryNote',
							width : 100,
							filterable : true
						}, {
							header : '仓库',
							dataIndex : 'stockLocation',
							width : 100,
							renderer : function(value, metaData, rec) {
								var text = '';

								var items = rec.items();
								for (var i = 0; i < items.getCount(); i++) {
									var item = items.getAt(i);
									var stock = item.getStockLocation()
											.get('stockLocation');
									if (text.indexOf(stock + ',') == -1) {
										text = text + stock + ','
									}
								}

								return text;

							}

						}, {
							header : '车号/卡号',
							dataIndex : 'plateNum',
							width : 100,
							filterable : true
						}, {
							header : '批号',
							dataIndex : 'batchNo',
							width : 200,
							filterable : true
						}, {
							header : '日期',
							dataIndex : 'docDate',
							xtype : 'datecolumn',
							format : 'Y-m-d',
							filterable : true
						}, {
							header : '工作号',
							dataIndex : 'workingNo',
							width : 100,
							filterable : true
						}, {
							header : '备注',
							dataIndex : 'remark',
							flex : 4,
							renderer : function(value, metaData, rec) {

								var text = '';

								var items = rec.items();
								for (var i = 0; i < items.getCount(); i++) {
									var item = items.getAt(i);
									if (item.get('remark') != "") {
										text = text + item.get('remark') + ','
									}
								}

								return text;

							}

						}];

				this.dockedItems = [{
							xtype : 'toolbar',
							items : [{
										iconCls : 'icon-add',
										text : 'Add',
										action : 'add'

									}, {
										iconCls : 'icon-copy',
										text : 'Copy',
										action : 'copy'

									}, {
										iconCls : 'icon-delete',
										text : 'Delete',
										action : 'delete'

									}]
						}, {
							xtype : 'pagingtoolbar',
							displayInfo : true,
							store : 'MaterialDocs',
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