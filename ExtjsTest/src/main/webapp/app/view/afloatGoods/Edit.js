Ext.define('AM.view.aflaotGoods.Edit', {
			extend : 'Ext.window.Window',
			alias : 'widget.afloatGoodsEdit',

			title : '在途信息',
			layout : 'border',
			autoShow : true,
			height : 400,
			width : 825,
			modal: true,

			initComponent : function() {
				this.items = [

				{
							region : 'north',
							xtype : 'form',
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
										name : 'supplier',
										fieldLabel : '供应商',
										readonly:true
									}, {
										xtype : 'textfield',
										name : 'plateNum',
										fieldLabel : '车号'
									}, {
										xtype : 'textfield',
										name : 'dispatch',
										fieldLabel : '发货地点'
									}, {
										xtype : 'textfield',
										name : 'destination',
										fieldLabel : '到达'
									}, {
										xtype : 'datefield',
										name : 'transportDate',
										fieldLabel : '转货时间',
										format: 'Y-m-d'
										
									}, {
										xtype : 'datefield',
										name : 'dispatchDate',
										fieldLabel : '发货日期',
										format: 'Y-m-d'
										
									}, {
										xtype : 'datefield',
										name : 'eta',
										fieldLabel : '预计到货日期',
										format: 'Y-m-d'
										
									}, {
										xtype : 'datefield',
										name : 'arrivalDate',
										fieldLabel : '实际到货日期',
										format: 'Y-m-d'
										
									}, {
										xtype : 'textfield',
										name : 'remark',
										width: 764,
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
													itemId : 'add'
												}, {
													iconCls : 'icon-delete',
													text : 'Delete',
													disabled : false,
													itemId : 'delete',
													scope : this

												}]
									}],
							plugins : [Ext.create(
									'Ext.grid.plugin.CellEditing', {

									})],
							columns : [{
										xtype : 'gridcolumn',
										id : 'model',
										dataIndex : 'model',
										text : '规格',

										field : 'textfield'
									}, {
										xtype : 'numbercolumn',
										itemId : 'quantity',
										dataIndex : 'quantity',
										text : '数量',
										field : 'numberfield'
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