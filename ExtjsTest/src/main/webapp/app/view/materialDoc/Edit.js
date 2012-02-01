Ext.define('AM.view.materialDoc.Edit', {
	extend : 'Ext.window.Window',
	alias : 'widget.materialDocEdit',
	xtype : 'materialDocEdit',
	autoShow : true,
	height : 414,
	width : 806,
	modal : true,
	layout : {
		type : 'border'
	},
	title : '货物进出仓凭证',
	views : ['contract.Search'],

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
													contractTypeDefaultValue : "0"

												});

										view.show();

									}
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
									fieldLabel : '进出仓日期',
									format : 'Y-m-d'
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
									text : '型号(合同)'
									,field: 'textfield'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'model_tested',
									text : '型号(检验后)'
									,field: 'textfield'
								}, {
									xtype : 'numbercolumn',
									dataIndex : 'grossWeight',
									text : '毛重'
									,field: 'numberfield'
								}, {
									xtype : 'numbercolumn',
									dataIndex : 'netWeight',
									text : '净重'
									,field: 'numberfield'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'moveType',
									text : '移动类型'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'warehouse',
									text : '仓库'
									,field: 'textfield'
								}, {
									xtype : 'gridcolumn',
									dataIndex : 'remark',
									text : '备注'
									,field: 'textfield'
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