

Ext.define('AM.view.contract.report.NoDelivery', {
	extend : 'Ext.panel.Panel',
	layout : {
		type : 'border'
	},
	frame : true, 
	alias : 'widget.NoDelivery',
	

	initComponent : function() {
		var me = this;
        var store = Ext.create('AM.store.contract.report.NoDelivery',{autoLoad:false});
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
											xtype : 'textfield',
											fieldLabel : '合同号',
											name:'contractNo'
										}, {
											xtype : 'textfield',
											fieldLabel : '供应商',
											name:'supplier'
										}, {
											xtype : 'textfield',
											fieldLabel : '规格',
											name:'model'
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
											dataIndex : 'contractNo',
											text : '合同号'
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
											dataIndex : 'quantity_in_contract',
											text : '签约数量'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'quantity_in_receipt',
											text : '到货数量'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'quantity_no_delivery',
											text : '未到货数量'
										} 
												
													
										],
								viewConfig : {

					}			,
								dockedItems : [{
											xtype : 'pagingtoolbar',
											width : 360,
											displayInfo : true,
											dock : 'bottom',
											store : store
										}]

							}]
				});

		me.callParent(arguments);
	}
});