

Ext.define('AM.view.contract.report.ContractHistory', {
	extend : 'Ext.panel.Panel',
	layout : {
		type : 'border'
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
									xtype : 'textfield',
									fieldLabel : '供应商',
									name : 'supplier'
								}, {
									xtype : 'textfield',
									fieldLabel : '规格',
									name : 'model'
								}, {
									xtype : 'textfield',
									fieldLabel : '付款方式',
									name : 'pay_term'
								}],  //合同类型	合同号	供应商	付款方式	备注	规格	数量	单价	备注
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
						columns : [{  //合同类型	合同号	供应商	付款方式	备注	规格	数量	单价	备注
									xtype : 'gridcolumn',
									dataIndex : 'contract_type',
									text : '合同类型'
								}, { 
									xtype : 'gridcolumn',
									dataIndex : 'contract_no',
									text : '合同号'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'supplier',
									text : '供应商'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'pay_term',
									text : '付款方式'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'remark',
									text : '备注'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'model',
									text : '规格'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'quantity',
									text : '签约数量'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'unit_price',
									text : '单价',
									sortable : false
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'item_remark',
									text : '备注',
									sortable : false
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