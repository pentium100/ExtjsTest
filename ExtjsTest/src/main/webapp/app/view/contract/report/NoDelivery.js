

Ext.define('AM.view.contract.report.NoDelivery', {
	extend : 'Ext.panel.Panel',
	layout : {
		type : 'border'
	},
	frame : true,
	alias : 'widget.NoDelivery',

	initComponent : function() {
		var me = this;
		var store = Ext.create('AM.store.contract.report.NoDelivery', {
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
						stateId : 'noDelivery',
						columns : [{
									xtype : 'gridcolumn',
									dataIndex : 'contract_type',
									flex : 1,
									text : '合同类型'
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
									text : '供应商'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'model',
									text : '规格'
								}, {
									xtype : 'numbercolumn',
									dataIndex : 'quantity',
									text : '签约数量',
									align : 'right'
								}, {
									xtype : 'numbercolumn',
									dataIndex : 'quantity_in_receipt',
									text : '执行数量',
									sortable : false,
									align : 'right'
								}, {
									xtype : 'numbercolumn',
									dataIndex : 'quantity_no_delivery',
									text : '未执行数量',
									sortable : false,
									align : 'right'
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