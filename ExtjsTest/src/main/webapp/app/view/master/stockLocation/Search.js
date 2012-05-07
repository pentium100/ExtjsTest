Ext.define('AM.view.master.stockLocation.Search', {
			extend : 'Ext.window.Window',

			height : 518,
			width : 678,
			layout : {
				type : 'border'
			},
			modal : true,
			title : '仓库搜索',
			alias : 'widget.stockLocationSearch',

			initComponent : function() {
				var me = this;
				var store = Ext.create(
						'AM.store.master.stockLocation.StockLocations', {
							autoLoad : false
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
													fieldLabel : '仓库名称',
													name : 'stockLocation'
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
													text : '仓库编号'
												}, {
													xtype : 'gridcolumn',
													dataIndex : 'stockLocation',
													text : '仓库名称'
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