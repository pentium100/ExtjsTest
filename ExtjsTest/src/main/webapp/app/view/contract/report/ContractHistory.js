

Ext.define('AM.view.contract.report.ContractHistory', {
	extend : 'Ext.panel.Panel',
	layout : {
		type : 'fit'
	},
	frame : true,
	alias : 'widget.ContractHistory',

	initComponent : function() {
		var me = this;
		var store = Ext.create('AM.store.contract.report.ContractHistory', {
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
									xtype : 'combo',
									name : 'contract_type',
									fieldLabel : '合同类型',
									store : Ext.create('AM.store.ContractType'),
									queryMode : 'local',
									displayField : 'text',
									valueField : 'id'

								}, {
									xtype : 'textfield',
									fieldLabel : '合同号',
									name : 'contract_no'
								}, {
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
									fieldLabel : '供应商/客户',
									name : 'supplier'
								}, {
									xtype : 'textfield',
									fieldLabel : '规格',
									name : 'model'
								}, {
									xtype : 'textfield',
									fieldLabel : '付款方式',
									name : 'pay_term'
								}, {
									xtype : 'combo',
									name : 'employee',
									fieldLabel : '业务员',
									// store : Ext
									// .create('AM.store.master.employee.Employees'),
									store : 'master.employee.Employees',
									queryMode : 'local',
									displayField : 'name',
									valueField : 'id',
									triggerAction : 'all'

								},{
									xtype : 'textfield',
									fieldLabel : '合同备注',
									name : 'remark'
								}, {
									xtype : 'textfield',
									fieldLabel : '项目备注',
									name : 'item_remark'
								}, ], // 合同类型 合同号 供应商 付款方式 备注 规格 数量 单价 备注
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
						stateId : 'contractHistory',
						columns : [{ // 合同类型 合同号 供应商 付款方式 备注 规格 数量 单价 备注
							xtype : 'gridcolumn',
							dataIndex : 'contract_type',
							text : '合同类型',
							flex : 0
						}, {
							xtype : 'gridcolumn',
							dataIndex : 'contract_no',
							text : '合同号'
						}, {
							xtype : 'datecolumn',
							dataIndex : 'sign_date',
							text : '签约日期',
							format : 'Y-m-d'
						}, {
							xtype : 'gridcolumn',
							dataIndex : 'supplier',
							text : '供应商/客户'
						}, {
							xtype : 'gridcolumn',
							dataIndex : 'pay_term',
							text : '付款方式'
						}, {
							xtype : 'gridcolumn',
							dataIndex : 'remark',
							text : '备注',
							sortFieldName : 'contract.remark'
						}, {
							xtype : 'gridcolumn',
							dataIndex : 'model',
							text : '规格'
						}, {
							xtype : 'numbercolumn',
							dataIndex : 'quantity',
							text : '签约数量',
							align : 'right',
							summaryType : 'sum'
						}, {
							xtype : 'numbercolumn',
							dataIndex : 'unit_price',
							text : '单价',
							align : 'right',
							sortable : false
						}, {
							xtype : 'gridcolumn',
							dataIndex : 'name',
							text : '业务员',
							sortable : false
						}, {
							xtype : 'gridcolumn',
							dataIndex : 'item_remark',
							text : '备注',
							sortFieldName : 'contract_item.remark',
							sortable : false
						}

						],
						features : [{
									ftype : 'remotesummary',
									remoteRoot : 'remoteSummary'
								}],
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