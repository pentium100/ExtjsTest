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
										xtype : 'textfield',
										name : 'contractType',
										fieldLabel : 'Contract Type'
									}, {
										xtype : 'datefield',
										name : 'lastShippingDate',
										fieldLabel : 'Last Shipping Date',
										format : 'Y-m-d'

									}, {
										xtype : 'textfield',
										name : 'contractNo',
										fieldLabel : 'Contract No'
									}]
						}, {
							region : 'center',
							xtype : 'gridpanel',
							//selType : 'cellmodel',
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