Ext.define('AM.view.materialDoc.ItemSearch', {
			extend : 'Ext.window.Window',

			height : 518,
			width : 678,
			layout : {
				type : 'border'
			},
			modal : true,
			title : '挑选库存记录',
			alias : 'widget.materialDocItemSearch',


			initComponent : function() {
				var me = this;
				var store = Ext.create('AM.store.MaterialDocItems');
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
													name: 'contractNo'
												}, {
													xtype : 'textfield',
													fieldLabel : '进仓单号'
													,name: 'deliveryNote'
												}, {
													xtype : 'textfield',
													fieldLabel : '车号/卡号'
													,name: 'plateNum'
												}, {
													xtype : 'textfield',
													fieldLabel : '批次号'
													,name: 'batchNo'
												}, {
													xtype : 'textfield',
													fieldLabel : '工作号'
													,name: 'workingNo'
												}, {
													xtype : 'textfield',
													fieldLabel : '仓库'
													,name: 'warehouse'
												}],
										buttons : [{
													text : 'Search',
													action : 'search'
												}, {
													text : 'Cancel',
													action : 'cancel',
													scope : this,
													handler : this.close

												}]
									}],
							items : [{
										xtype : 'gridpanel',
										title : '搜索结果',
										region : 'center',
										store : store,
										by: this.by,
										columns : [{
													xtype : 'gridcolumn',
													
													text : '合同号',
													dataIndex: 'contractNo'
												}, {
													xtype : 'gridcolumn',
													
													text : '进仓单号',
													dataIndex: 'deliveryNote'
												}, {
													xtype : 'gridcolumn',
													
													text : '车号/卡号',
													dataIndex: 'plateNum'
												}, {
													xtype : 'gridcolumn',
													
													text : '批次号',
													dataIndex: 'batchNo'
												}, {
													xtype : 'gridcolumn',
													
													text : '规格(合同)',
													dataIndex: 'model_contract'
												}, {
													xtype : 'gridcolumn',
													
													text : '规格(检验后)',
													dataIndex: 'model_tested'
												}, {
													xtype : 'gridcolumn',
													
													text : '净重',
													dataIndex: 'netWeight'
												}, {
													xtype : 'gridcolumn',
													
													text : '仓库',
													dataIndex: 'warehouse'
												}, {
													xtype : 'gridcolumn',
													format : 'Y-m-d',
													text : '进仓日期',
													dataIndex: 'docDate'
												}],
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