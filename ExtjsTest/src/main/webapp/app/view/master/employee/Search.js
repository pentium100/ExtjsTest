Ext.define('AM.view.master.employee.Search', {
			extend : 'Ext.window.Window',

			height : 518,
			width : 678,
			layout : {
				type : 'border'
			},
			modal : true,
			title : '仓库搜索',
			alias : 'widget.employeeSearch',

			initComponent : function() {
				var me = this;
				var store = Ext.create('AM.store.master.employee.Employees', {
							autoLoad : true
						});
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
													fieldLabel : '业务员',
													name : 'employee'
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
										by : this.by,
										columns : [{
													xtype : 'gridcolumn',
													dataIndex : 'id',
													text : '编号'
												}, {
													xtype : 'gridcolumn',
													dataIndex : 'name',
													text : '业务员'
												}],
										dockedItems : [{
													xtype : 'pagingtoolbar',
													displayInfo : true,
													dock : 'bottom',
													store : store
												}]

									}]
						});

				me.callParent(arguments);
			}
		});