Ext.define('AM.view.inspection.Edit', {
			extend : 'Ext.window.Window',
			alias : 'widget.inspectionEdit',
			xtype : 'inspectionEdit',

			title : '检验信息',
			layout : 'border',
			autoShow : true,
			height : 400,
			width : 825,
			modal : true,
			views:['materialDoc.ItemSearch'],

			initComponent : function() {
				var me = this;
				this.items = [

				{
							region : 'north',
							xtype : 'form',
							layout : {
								type : 'column'
							},
							items : [{
										xtype : 'datefield',
										name : 'inspectionDate',
										fieldLabel : '检验日期',
										format : 'Y-m-d'

									}, {
										xtype : 'textfield',
										name : 'authority',
										fieldLabel : '检验机构'
									}, {
										xtype : 'textfield',
										name : 'docNo',
										fieldLabel : '编号'
									}, {
										xtype : 'checkbox',
										name : 'original',
										fieldLabel : '收到证书',
										inputValue : 'true'
									}, {
										xtype : 'textfield',
										name : 'remark',
										width : 764,
										fieldLabel : '备注'
									}]
						}, {
							region : 'center',
							xtype : 'gridpanel',
							// selType : 'cellmodel',
							dockedItems : [{
										xtype : 'toolbar',
										items : [{
													iconCls : 'icon-add',
													text : 'Add',
													scope : this,
													action : 'add'
												}, {
													iconCls : 'icon-delete',
													text : 'Delete',
													scope : this,
													action : 'delete'

												}]
									}],
							plugins : [Ext.create(
									'Ext.grid.plugin.CellEditing', {

									})],
							columns : [{
								xtype : 'gridcolumn',
								dataIndex : 'contractNo',
								text : '合同号',
								editor : {
									xtype : 'trigger',
									triggerCls : 'icon-search',
									editable : false,

									onTriggerClick : function(e) {
										var view = Ext.widget(
												'materialDocItemSearch', {
													parentWindow : me,
													by: me.xtype
													

												});

										view.show();

									}

								}

							}, {
								xtype : 'gridcolumn',
								dataIndex : 'model_contract',
								text : '规格'
								
							},  {
								xtype : 'gridcolumn',
								dataIndex : 'plateNum',
								text : '车号'
								
							}, {
								xtype : 'gridcolumn',
								dataIndex : 'batchNo',
								text : '批次号',
								field : 'textfield'
							}, {
								xtype : 'gridcolumn',
								dataIndex : 'deliveryNote',
								text : '进仓单号'
								
							}, {
								xtype : 'numbercolumn',
								dataIndex : 'netWeight',
								text : '数量',
								align: 'right',
								field : 'numberfield'
							}, {
								xtype : 'numbercolumn',
								dataIndex : 'si',
								width: 51,
								align: 'right',
								text : 'si',
								field : 'numberfield'
							}, {
								xtype : 'numbercolumn',
								dataIndex : 'fe',
								width: 51,
								align: 'right',
								text : 'fe',
								field : 'numberfield'
							} , {
								xtype : 'numbercolumn',
								dataIndex : 'al',
								width: 51,
								align: 'right',
								text : 'al',
								field : 'numberfield'
							}, {
								xtype : 'numbercolumn',
								dataIndex : 'ca',
								width: 51,
								align: 'right',
								text : 'ca',
								field : 'numberfield'
							}, {
								xtype : 'numbercolumn',
								dataIndex : 'p',
								width: 51,
								align: 'right',
								text : 'p',
								field : 'numberfield'
							}, {
								xtype : 'gridcolumn',
								dataIndex : '备注',
								text : '备注',
								field : 'textfield'
							}]
						}];

				this.buttons = [{
							text : 'Save',
							action : 'save'
						}, {
							text : 'Cancel',
							scope : this,
							handler : this.close
						}];

				this.callParent(arguments);
			}
		});