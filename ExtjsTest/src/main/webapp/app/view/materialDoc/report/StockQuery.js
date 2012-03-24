

Ext.define('AM.view.materialDoc.report.StockQuery', {
	extend : 'Ext.panel.Panel',
	layout : {
		type : 'border'
	},
	frame : true,
	alias : 'widget.StockQuery',

	initComponent : function() {
		var me = this;
		var store = Ext.create('AM.store.materialDoc.report.StockQuery', {
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
											fieldLabel : '截止日期',
											name : 'endDate',
											format : 'Y-m-d',
											allowBlank: false,
											value : new Date()

										}, {
											xtype : 'textfield',
											fieldLabel : '合同号',
											name : 'contract_no'
										}, {
											xtype : 'textfield',
											fieldLabel : '供应商',
											name : 'supplier'
										}, {
											xtype : 'datefield',
											fieldLabel : '进仓日期从',
											name : 'docDateFrom'
										}, {
											xtype : 'datefield',
											fieldLabel : '进仓日期到',
											name : 'docDateTo'
										}, {
											xtype : 'textfield',
											fieldLabel : '仓库',
											name : 'warehouse'
										}, {
											xtype : 'textfield',
											fieldLabel : '进仓单号',
											name : 'delivery_note'
										}, {
											xtype : 'textfield',
											fieldLabel : '车号/卡号',
											name : 'plate_num'
										}, {
											xtype : 'textfield',
											fieldLabel : '批次号',
											name : 'batch_no'
										}, {
											xtype : 'textfield',
											fieldLabel : '规格(合同)',
											name : 'model_contract'
										}], // 合同类型 合同号 供应商 付款方式 备注 规格 数量 单价 备注
								buttons : [{
											text : '提交',
											action : 'submit'
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
								// 合同号 进仓单号 进仓日期 车号/卡号 批次号 规格(合同) 规格(检验后) 净重 仓库
								columns : [{
											xtype : 'gridcolumn',
											dataIndex : 'contract_no',
											text : '合同号'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'supplier',
											text : '供应商'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'model_contract',
											text : '规格(合同)'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'unit_price',
											text : '单价',
											sortable : false
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'gross_weight',
											text : '毛重'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'net_weight',
											text : '净重'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'delivery_note',
											text : '进仓单号'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'plate_num',
											text : '车号/卡号'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'batch_no',
											text : '批次号'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'warehouse',
											text : '仓库'

										}, {
											xtype : 'datecolumn',
											format : 'Y-m-d',
											dataIndex : 'doc_date',
											text : '进仓日期'
										}, {
											xtype : 'datecolumn',
											dataIndex : 'inspection_date',
											format : 'Y-m-d',
											text : '检验日期'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'authority',
											text : '检验机构'
										} , {
											xtype : 'gridcolumn',
											dataIndex : 'si',
											text : 'si'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'fe',
											text : 'fe'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'al',
											text : 'al'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'ca',
											text : 'ca'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'p',
											text : 'p'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'inspection_remark',
											text : '检验备注'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'doc_no',
											text : '证书编号'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'original',
											text : '正本'
										}

								],
								viewConfig : {

					}			,
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