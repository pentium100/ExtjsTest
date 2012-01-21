Ext.define('AM.view.contract.Edit', {
			extend : 'Ext.window.Window',
			alias : 'widget.contractEdit',

			title : 'Edit Contract',
			layout : 'border',
			autoShow : true,
			height : 400,
			width : 450,

			initComponent : function() {
				this.items = [

				{
							region : 'north',
							xtype : 'form',
							items : [{
										xtype : 'combo',
										name : 'contractType',
										fieldLabel : '合同类型',
										store : Ext.create('AM.store.ContractType'),
										queryMode : 'local',
										displayField : 'text',
										valueField : 'text'
										
									}, {
										xtype : 'textfield',
										name : 'contractNo',
										fieldLabel : '合同号'
										

									}, {
										xtype : 'textfield',
										name : 'supplier',
										fieldLabel : '供应商'
									}, {
										xtype : 'textfield',
										name : 'payTerm',
										fieldLabel : '付款方式'
									}, {
										xtype : 'textfield',
										name : 'remark',
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
									}, {
										xtype : 'numbercolumn',
										itemId : 'unitPrice',
										dataIndex : 'unitPrice',
										text : '单价',
										field : 'numberfield'
									},{
										xtype : 'gridcolumn',
										id : 'remark',
										dataIndex : 'remark',
										text : '备注',

										field : 'textfield'
									}, ]
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