

Ext.define('AM.view.afloatGoods.report.AfloatGoodsDetail', {
	extend : 'Ext.panel.Panel',
	layout : {
		type : 'border'
	},
	frame : true,
	alias : 'widget.AfloatGoodsDetail',

	initComponent : function() {
		var me = this;
		var store = Ext.create('AM.store.afloatGoods.report.AfloatGoodsDetail', {
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
											fieldLabel : '车号',
											name : 'plate_num'
										}, {
											xtype : 'datefield',
											fieldLabel : '发货日期从',
											name : 'dispatchDateFrom',
											format : 'Y-m-d'
										}, {
											xtype : 'datefield',
											fieldLabel : '发货日期到',
											name : 'dispatchDateTo',
											format : 'Y-m-d'
										}, {
											xtype : 'textfield',
											fieldLabel : '发货地点',
											name : 'dispatch'
										}, {
											xtype : 'combo',
											fieldLabel : '货物状态',
											name : 'received',
											
											forceSelection : true,
											store : Ext.create(
													'Ext.data.Store', {
														fields : ['id', 'text'],
														data : [{
																	"id" : '',
																	"text" : ""
																},{
																	"id" : 'not null',
																	"text" : "已到"
																}, {
																	"id" : 'null',
																	"text" : "未到"
																}]
													}),

											queryMode : 'local',
											displayField : 'text',
											valueField : 'id'

										}, {
											xtype : 'combo',
											fieldLabel : '正本单据',
											name : 'original',
											
											forceSelection : true,
											store : Ext.create(
													'Ext.data.Store', {
														fields : ['id', 'text'],
														data : [{
																	"id" : '',
																	"text" : ""
																},{
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
											name : 'model'
										}, {
											xtype : 'textfield',
											fieldLabel : '到达',
											name : 'destination'
										}, {
											xtype : 'textfield',
											fieldLabel : '超期天数',
											name : 'beyond_days'
										}, {
											xtype : 'textfield',
											fieldLabel : '批次号',
											name : 'batch_no'
										}, {
											xtype : 'datefield',
											fieldLabel : '转货日期从',
											name : 'transportDateFrom',
											format : 'Y-m-d'
										}, {
											xtype : 'datefield',
											fieldLabel : '转货日期到',
											name : 'transportDateTo',
											format : 'Y-m-d'
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
								stateId : 'afloatGoodsDetail',								
								columns : [{ // 合同号 供应商 规格 车号 批次号 进仓单号 检验日期
									// 机构 编号 正本
									// 数量 SI fe al ca p 备注

									xtype : 'gridcolumn',
									dataIndex : 'contract_no',
									text : '合同号'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'supplier',
									text : '供应商'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'model',
									text : '规格'
								}, {
									xtype : 'numbercolumn',
									dataIndex : 'quantity',
									align:'right',
									text : '数量'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'plate_num',
									text : '车号'
								}, {
									xtype : 'datecolumn',
									dataIndex : 'dispatch_date',
									text : '发货日期',
									format : 'Y-m-d'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'dispatch',
									text : '发货地点'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'destination',
									text : '到达'
								}, {
									xtype : 'datecolumn',
									dataIndex : 'eta',
									text : '预计到货日期',
									format: 'Y-m-d'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'beyond_days',
									text : '超期天数',
									align:'right'
								}, {
									xtype : 'datecolumn',
									dataIndex : 'arrival_date',
									text : '实际到货日期',
									format : 'Y-m-d'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'batch_no',
									text : '批次号'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'original',
									text : '正本'
								}, {
									xtype : 'datecolumn',
									dataIndex : 'transport_date',
									text : '转货时间',
									format: 'Y-m-d'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'remark',
									text : '备注'
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