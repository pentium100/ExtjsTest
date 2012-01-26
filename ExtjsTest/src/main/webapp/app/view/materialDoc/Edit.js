
Ext.define('AM.view.materialDoc.Edit', {
			extend : 'Ext.window.Window',
			alias : 'widget.materialDocEdit',
			autoShow : true,
			height : 414,
			width : 806,
			layout : {
				type : 'border'
			},
			title : '货物进出仓凭证',

			initComponent : function() {
				var me = this;

				Ext.applyIf(me, {
							dockedItems : [{
										xtype : 'form',
										tpl : Ext.create('Ext.XTemplate', ''),
										layout : {
											type : 'column'
										},
										bodyPadding : 10,
										title : '凭证号:',
										dock : 'top',
										items : [{
													xtype : 'trigger',
													fieldLabel : '合同号',
													name : 'contractNo',
													triggerCls : 'icon-search',
													enableKeyEvents : true,
													readOnly: false,
													anchor : '100%'
												}, {
													xtype : 'textfield',
													name : 'deliveryNote',
													fieldLabel : '进仓单号' 
												}, {
													xtype : 'textfield',
													name : 'plateNum',
													fieldLabel : '车号/卡号'
												}, {
													xtype : 'textfield',
													name : 'batchNo',
													fieldLabel : '批次号'
												}, {
													xtype : 'datefield',
													name : 'docDate',
													fieldLabel : '进出仓日期'
												}, {
													xtype : 'textfield',
													name : 'workingNo',
													fieldLabel : '工作号'
												}]
									}],
							items : [{
										xtype : 'gridpanel',
										title : '进出仓明细',
										region : 'center',
										columns : [{
													xtype : 'gridcolumn',
													dataIndex : 'model_contract',
													text : '型号(合同)'
												}, {
													xtype : 'gridcolumn',
													dataIndex : 'model_tested',
													text : '型号(检验后)'
												}, {
													xtype : 'numbercolumn',
													dataIndex : 'grossWeight',
													text : '毛重'
												}, {
													xtype : 'numbercolumn',
													dataIndex : 'netWeight',
													text : '净重'
												}, {
													xtype : 'gridcolumn',
													dataIndex : 'moveType',
													text : '移动类型'
												}, {
													xtype : 'gridcolumn',
													dataIndex : 'warehouse',
													text : '仓库'
												}, {
													xtype : 'gridcolumn',
													dataIndex : 'remark',
													text : '备注'
												}],
										viewConfig : {

										}
									}],

							buttons : [{
										text : 'Save',
										action : 'save'
									}, {
										text : 'Cancel',
										scope : this,
										handler : this.close
									}]

						});

				me.callParent(arguments);
			}
		});