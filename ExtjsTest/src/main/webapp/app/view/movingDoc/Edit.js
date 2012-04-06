Ext.define('AM.view.movingDoc.Edit', {
	extend : 'Ext.window.Window',
	alias : 'widget.movingDocEdit',
	xtype : 'movingDocEdit',
	autoShow : true,
	height : 414,
	width : 806,
	modal : true,
	layout : {
		type : 'border'
	},
	title : '货物移仓凭证',
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
									xtype : 'textfield',
									name : 'workingNo',
									fieldLabel : '工作号'
								}, {
									xtype : 'datefield',
									name : 'docDate',
									fieldLabel : '进出仓日期',
									format : 'Y-m-d'
								}, {
									xtype : 'textfield',
									name : 'targetWarehouse',
									fieldLabel : '目的仓库',
									allowBlank: false
								}, {
									xtype : 'textfield',
									name : 'plateNum',
									fieldLabel : '车号/卡号'
								}, {
									xtype : 'textfield',
									name : 'batchNo',
									fieldLabel : '批号'
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
							// field : 'trigger',
							editor : {
								xtype : 'trigger',
								triggerCls : 'icon-search',
								editable : false,

								onTriggerClick : function(e) {
									//if(me.down("form").getValues().contractNo==""){return;}
									//var contractNo = me.down("form").getValues().contractNo;
									var view = Ext.create(
											'AM.view.materialDoc.ItemSearch', {
												parentWindow : me,
												by : me.xtype
												
												

											});

									view.show();

								}

							}

						}, {
							xtype : 'gridcolumn',
							dataIndex : 'model_tested',
							text : '规格(检验后)'

						}, {
							xtype : 'numbercolumn',
							dataIndex : 'netWeight',
							text : '净重',
							field : 'numberfield'
						}, {
							xtype : 'gridcolumn',
							dataIndex : 'deliveryNote',
							text : '进仓单号'

						}, {
							xtype : 'gridcolumn',
							dataIndex : 'plateNum',
							text : '车号/卡号'

						}, {
							xtype : 'gridcolumn',
							dataIndex : 'batchNo',
							text : '批号'

						}, {
							xtype : 'gridcolumn',
							dataIndex : 'warehouse',
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