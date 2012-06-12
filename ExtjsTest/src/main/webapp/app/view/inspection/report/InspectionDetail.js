

Ext.define('AM.view.inspection.report.InspectionDetail', {
	extend : 'Ext.panel.Panel',
	layout : {
		type : 'border'
	},
	frame : true,
	alias : 'widget.InspectionDetail',

	initComponent : function() {
		var me = this;
		var store = Ext.create('AM.store.inspection.report.InspectionDetail', {
					autoLoad : false
				});
		Ext.applyIf(me, {

					dockedItems : [{
								xtype : 'form',
								layout : {
									type : 'column'
								},
								bodyPadding : 10,
								preventHeader : true,

								region : 'center',
								dock : 'top',
								items : [{
											xtype : 'textfield',
											fieldLabel : '合同号',
											name : 'contract_no'
										}, {
											xtype : 'textfield',
											fieldLabel : '供应商',
											name : 'supplier'
										}, {
											xtype : 'textfield',
											fieldLabel : '检验机构',
											name : 'authority'
										}, {
											xtype : 'datefield',
											fieldLabel : '检验日期从',
											name : 'inspectionDateFrom',
											format : 'Y-m-d'
										}, {
											xtype : 'datefield',
											fieldLabel : '检验日期到',
											name : 'inspectionDateTo',
											format : 'Y-m-d'
										}, {
											xtype : 'textfield',
											fieldLabel : '编号',
											name : 'doc_no'
										}, {
											xtype : 'combo',
											fieldLabel : '证书',
											name : 'original',

											forceSelection : true,
											store : Ext.create(
													'Ext.data.Store', {
														fields : ['id', 'text'],
														data : [{
																	"id" : '',
																	"text" : ""
																}, {
																	"id" : '1',
																	"text" : "已收到"
																}, {
																	"id" : '0',
																	"text" : "未收到"
																}]
													}),

											queryMode : 'local',
											displayField : 'text',
											valueField : 'id'

										}, {
											xtype : 'textfield',
											fieldLabel : '规格',
											name : 'model_contract'
										}, {
											xtype : 'textfield',
											fieldLabel : '车号',
											name : 'plate_num'
										}, {
											xtype : 'textfield',
											fieldLabel : '批次号',
											name : 'batch_no'
										}, {
											xtype : 'textfield',
											fieldLabel : '进仓单号',
											name : 'delivery_note'
										}],
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
								stateful : true,
								stateId : 'inspectionDetail',

								columns : [{ // 合同号 供应商 规格 车号 批次号 进仓单号 检验日期
									// 机构 编号 正本
									// 数量 SI fe al ca p 备注

									xtype : 'gridcolumn',
									dataIndex : 'contract_no',
									text : '合同号',
									flex : 0

								}, {
									xtype : 'gridcolumn',
									dataIndex : 'supplier',
									text : '供应商'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'model_contract',
									text : '规格'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'plate_num',
									text : '车号'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'batch_no',
									text : '批次号'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'delivery_note',
									text : '进仓单号'
								}, {
									xtype : 'datecolumn',
									dataIndex : 'inspection_date',
									text : '检验日期',
									format : 'Y-m-d'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'authority',
									text : '检验机构'

								}, {
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
									dataIndex : 'remark',
									text : '备注'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'doc_no',
									text : '编号'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'original',
									text : '正本'
								}, {
									xtype : 'numbercolumn',
									dataIndex : 'net_weight',
									text : '数量'
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