Ext.define('AM.view.outgoingDoc.Edit', {
	extend : 'Ext.window.Window',
	alias : 'widget.outgoingDocEdit',
	xtype : 'outgoingDocEdit',
	autoShow : true,
	height : 414,
	width : 806,
	modal : true,
	layout : {
		type : 'border'
	},
	title : '货物出仓凭证',
	views : ['contract.Search', 'materialDoc.ItemSearch'],

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
									editable : false,
									anchor : '100%',

									onTriggerClick : function(e) {
										var view = Ext.widget('contractSearch',
												{
													parentWindow : me,
													contractTypeReadonly : true,
													contractTypeDefaultValue : "1",
													by : me.xtype

												});

										view.show();

									}
								}, {
									xtype : 'textfield',
									name : 'deliveryNote',
									fieldLabel : '出仓单号'
								}, {
									xtype : 'textfield',
									name : 'plateNum',
									fieldLabel : '车号/卡号'
									,hidden: true
								}, {
									xtype : 'textfield',
									name : 'batchNo',
									fieldLabel : '批次号'
									,hidden: true
								}, {
									xtype : 'datefield',
									name : 'docDate',
									fieldLabel : '出仓日期',
									format : 'Y-m-d'
								}, {
									xtype : 'textfield',
									name : 'workingNo',
									fieldLabel : '工作号'
									,hidden: true
								}]
					}],
			items : [{
				xtype : 'gridpanel',
				title : '进出仓明细',
				region : 'center',
				dockedItems : [{
							xtype : 'toolbar',
							items : [{
										iconCls : 'icon-add',
										text : 'Add',
										scope : this,
										itemId : 'add',
										action : 'add'
									}, {
										iconCls : 'icon-delete',
										text : 'Delete',
										disabled : false,
										itemId : 'delete',
										scope : this,
										action : 'delete'

									}]
						}],
				plugins : [Ext.create('Ext.grid.plugin.CellEditing', {

				})],

				columns : [{
							xtype : 'gridcolumn',
							dataIndex : 'model_contract',
							text : '规格',
							width: 80,
							// field : 'trigger',
							editor : {
								xtype : 'trigger',
								triggerCls : 'icon-search',
								editable : false,

								onTriggerClick : function(e) {
									var view = Ext.widget(
											'materialDocItemSearch', {
												parentWindow : me,
												by : me.xtype
											});

									view.show();

								}

							}

						}, {
							xtype : 'gridcolumn',
							dataIndex : 'model_tested',
							width: 80,
							text : '规格(检验后)'

						}, {
							xtype : 'numbercolumn',
							dataIndex : 'netWeight',
							width: 80,
							text : '净重',
							field : 'numberfield'
						}, {
							xtype : 'gridcolumn',
							dataIndex : 'deliveryNote',
							text : '进仓单号',
							width: 100

						}, {
							xtype : 'gridcolumn',
							dataIndex : 'workingNo',
							text : '工作号',
							width: 100

						}, {
							xtype : 'gridcolumn',
							dataIndex : 'plateNum',
							width: 100,
							text : '车号/卡号'

						}, {
							xtype : 'gridcolumn',
							dataIndex : 'batchNo',
							width: 200,
							text : '批号'

						}, {
							xtype : 'gridcolumn',
							dataIndex : 'warehouse',
							width: 100,
							text : '仓库'

						}, {
							xtype : 'gridcolumn',
							dataIndex : 'remark',
							text : '备注',
							field : 'textfield'
						}, {
							xtype : 'gridcolumn',
							dataIndex : 'moveType',
							text : '移动类型'
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
						action : 'cancel',
						handler : this.close
					}]

		});

		me.callParent(arguments);
	}
});