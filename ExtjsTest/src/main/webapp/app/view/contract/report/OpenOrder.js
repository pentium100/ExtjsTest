

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
					autoLoad : false
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
								columns : [{
											xtype : 'gridcolumn',
											dataIndex : 'model',
											text : '规格'
										}, {
											xtype : 'numbercolumn',
											dataIndex : 'quantity_purchases',
											text : '采购数量',
											sortable : false,
											align:'right'
										}, {
											xtype : 'numbercolumn',
											dataIndex : 'quantity_sales',
											text : '销售数量',
											sortable : false,
											align:'right'
										}, {
											xtype : 'numbercolumn',
											dataIndex : 'quantity_open',
											text : '敞口数量',
											sortable : false,
											align:'right'
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