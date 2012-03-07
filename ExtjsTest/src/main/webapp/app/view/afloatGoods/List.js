Ext.define('AM.view.afloatGoods.List', {
			extend : 'Ext.grid.Panel',
			alias : 'widget.afloatGoodsList',

			// title : 'All Contract',
			store : 'afloatGoods.AfloatGoods',

			requires : ['Ext.grid.plugin.CellEditing', 'Ext.form.field.Text',
					'Ext.toolbar.TextItem', 'Ext.ux.grid.FiltersFeature'],

			iconCls : 'icon-grid',
			frame : true,

			initComponent : function() {
				this.columns = [{
							header : '合同号',
							dataIndex : 'contractNo',
							filterable : true,
							flex : 1
						}, {
							header : '供应商',
							dataIndex : 'supplier',
							filterable : true,
							flex : 1
						}, {
							header : '车号',
							dataIndex : 'plateNum',
							filterable : true,
							flex : 1
						}, {
							header : '发货地点',
							dataIndex : 'dispatch',
							filterable : true,
							flex : 1
						}, {
							header : '到达',
							dataIndex : 'destination',
							filterable : true,
							flex : 1
						}, {
							header : '转货时间',
							dataIndex : 'transportDate',
							xtype : 'datecolumn',
							format : 'Y-m-d',
							filterable : true,
							flex : 1
						}, {
							header : '发货时间',
							dataIndex : 'dispatchDate',
							xtype : 'datecolumn',
							format : 'Y-m-d',
							filterable : true,
							flex : 1
						}, {
							header : '预计到货日',
							dataIndex : 'eta',
							xtype : 'datecolumn',
							format : 'Y-m-d',
							filterable : true,
							flex : 1
						}, {
							header : '实际到货日期',
							dataIndex : 'arrivalDate',
							xtype : 'datecolumn',
							format : 'Y-m-d',
							filterable : true,
							flex : 1
						}, {
							header : '正本单据',
							dataIndex : 'original',
							xtype : 'gridcolumn',
							inputValue:'true',
							filterable : true,
							flex : 1
						}, {
							header : '备注',
							dataIndex : 'remark',
							filterable : true,
							flex : 4
						}];

				this.dockedItems = [{
							xtype : 'toolbar',
							items : [{
										iconCls : 'icon-add',
										text : 'Add',
										action : 'add'

									}, {
										iconCls : 'icon-delete',
										text : 'Delete',
										action : 'delete'

									}]
						}, {
							xtype : 'pagingtoolbar',
							displayInfo : true,
							store : 'afloatGoods.AfloatGoods',
							dock : 'bottom'
						}

				];

				this.features = [{
							ftype : 'filters',
							encode : true, // json encode the filter query
							local : false
						}];

				this.callParent(arguments);
			}
		});