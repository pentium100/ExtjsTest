

Ext.define('AM.view.contract.report.OpenOrder', {
	extend : 'Ext.panel.Panel',
	layout : {
		type : 'border'
	},
	frame : true,
	alias : 'widget.OpenOrder',

	initComponent : function() {
		var me = this;
		var store = Ext.create('AM.store.contract.report.OpenOrder', {
					autoLoad : false,
					autoSync : true
				});

		Ext.applyIf(me, {

					dockedItems : [{
								xtype : 'form',
								tpl : Ext.create('Ext.XTemplate', ''),
								layout : {
									type : 'column'
								},
								bodyPadding : 10,
								preventHeader : true,

								region : 'center',
								dock : 'top',
								items : [{
											xtype : 'datefield',
											fieldLabel : '签约日期从',
											name : 'signDateFrom',
											format : 'Y-m-d'
										}, {
											xtype : 'datefield',
											fieldLabel : '签约日期到',
											name : 'signDateTo',
											format : 'Y-m-d'
										}, {
											xtype : 'textfield',
											fieldLabel : '规格',
											name : 'model'
										}],
								buttons : [{
											text : '提交',
											action : 'search'
										}, {
											text : '取消',
											action : 'cancel',
											scope : this,
											handler : this.close

										}]
							}],
					items : [{
								xtype : 'gridpanel',
								title : '报表清单',
								region : 'center',
								store : store,
								stateful : true,
								stateId : 'openOrder',
								plugins : [Ext.create(
										'Ext.grid.plugin.CellEditing', {

										})],
								columns : [{
											xtype : 'gridcolumn',
											dataIndex : 'model',
											text : '规格',
											flex : 0
										}, {
											xtype : 'numbercolumn',
											dataIndex : 'quantity_purchases',
											text : '采购数量',
											sortable : false,
											format : '0,000.000',
											align : 'right'
										}, {
											xtype : 'numbercolumn',
											dataIndex : 'quantity_sales',
											text : '销售数量',
											sortable : false,
											format : '0,000.000',
											align : 'right'
										}, {
											xtype : 'numbercolumn',
											dataIndex : 'quantity_open',
											text : '敞口数量',
											sortable : false,
											format : '0,000.000',
											align : 'right'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'memo',
											text : '备注',
											sortable : false,
											editor : {
												xtype : 'textfield'
											}

										}, {
											xtype : 'gridcolumn',
											dataIndex : 'update_user',
											text : '更新用户',
											sortable : false

										}, {

											dataIndex : 'update_time',
											text : '更新时间',
											sortable : false,
											xtype : 'datecolumn',
											format : 'Y-m-d H:i:s'

										}

								],

								dockedItems : [{
											xtype : 'pagingtoolbar',
											// width : 360,
											displayInfo : true,
											dock : 'bottom',
											store : store,
											items : [{
														xtype : 'button',
														text : '导出到excel',
														action : 'exportToExcel'
													}]
										}]

							}]
				});

		me.callParent(arguments);
	}
});