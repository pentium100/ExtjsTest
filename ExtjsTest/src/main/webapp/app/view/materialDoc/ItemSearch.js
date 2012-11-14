Ext.define('AM.view.materialDoc.ItemSearch', {
	extend : 'Ext.window.Window',

	height : 518,
	width : 678,
	layout : {
		type : 'border'
	},
	modal : true,
	title : '挑选库存记录',
	alias : 'widget.materialDocItemSearch',
	xtype : 'materialDocItemSearch',

	initComponent : function() {
		var me = this;

		var store = Ext.create('AM.store.MaterialDocItems', {
					proxyUrl : me.proxyUrl
				});

		var selModel = Ext.create('Ext.selection.CheckboxModel', {
					mode : this.selMode,
					checkOnly : true
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
											fieldLabel : '合同号',
											name : 'contractNo',
											value : me.contractNo,
											readOnly : me.contractNo != undefined
										}, {
											xtype : 'textfield',
											fieldLabel : '进仓单号',
											name : 'deliveryNote'
										}, {
											xtype : 'textfield',
											fieldLabel : '车号/卡号',
											name : 'plateNum'
										}, {
											xtype : 'textfield',
											fieldLabel : '批次号',
											name : 'batchNo'
										}, {
											xtype : 'textfield',
											fieldLabel : '工作号',
											name : 'workingNo'
										}, {
											xtype : 'textfield',
											fieldLabel : '仓库',
											name : 'stockLocation'
										}, {
											xtype : 'textfield',
											fieldLabel : '规格',
											name : 'model'
										}, {
											xtype : 'textfield',
											fieldLabel : '供应商',
											name : 'supplier'
										}],
								buttons : [{
											text : 'Search',
											action : 'search',
											handler : this.searchMaterialDocItem
										}, {
											text : 'Add',
											action : 'add'
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
								selModel : selModel,
								columns : [{
											xtype : 'gridcolumn',

											text : '合同号',
											dataIndex : 'contractNo'
										}, {
											xtype : 'gridcolumn',

											text : '进仓单号',
											dataIndex : 'deliveryNote'
										}, {
											xtype : 'gridcolumn',

											text : '净重',
											dataIndex : 'netWeight'
										}, {
											xtype : 'gridcolumn',

											text : '车号/卡号',
											dataIndex : 'plateNum'
										}, {
											xtype : 'gridcolumn',

											text : '批次号',
											dataIndex : 'batchNo'
										}, {
											xtype : 'gridcolumn',

											text : '规格(合同)',
											dataIndex : 'model_contract'
										}, {
											xtype : 'gridcolumn',

											text : '规格(检验后)',
											dataIndex : 'model_tested'
										}, {
											xtype : 'gridcolumn',

											text : '仓库',
											dataIndex : 'warehouse'
										}, {
											xtype : 'gridcolumn',
											format : 'Y-m-d',
											text : '进仓日期',
											dataIndex : 'docDate'
										}, {
											xtype : 'gridcolumn',

											text : '工作号',
											dataIndex : 'workingNo'
										}],
								viewConfig : {

					}			,
								dockedItems : [{
											xtype : 'pagingtoolbar',
											displayInfo : true,
											dock : 'bottom',
											store : store
										}]

							}]
				});

		me.callParent(arguments);
	},

	searchMaterialDocItem : function(button) {
		var win = button.up('window');
		var grid = win.down('gridpanel');
		var store = grid.getStore();

		var tmp = [];
		var filter = {};
		var record = button.up('form').getValues();

		filter.type = "list";
		filter.field = "materialDoc.docType.id";
		filter.comparison = 'eq';
		filter.value = '1,3';
		tmp.push(Ext.apply({}, filter));

		
		if (record.contractNo != "") {
			filter.type = "string";
			filter.field = "contract.contractNo";
			filter.value = record.contractNo;
			tmp.push(Ext.apply({}, filter));
		}

		if (record.deliveryNote != "") {
			filter.type = "string";
			filter.field = "deliveryNote";
			filter.value = record.deliveryNote;
			tmp.push(Ext.apply({}, filter));
		}
		if (record.plateNum != "") {
			filter.type = "list";
			filter.field = "plateNum";
			filter.value = record.plateNum;
			tmp.push(Ext.apply({}, filter));
		}

		if (record.batchNo != "") {
			filter.type = "string";
			filter.field = "materialDoc.batchNo";
			filter.value = record.batchNo;
			tmp.push(Ext.apply({}, filter));
		}

		if (record.workingNo != "") {
			filter.type = "string";
			filter.field = "materialDoc.workingNo";
			filter.value = record.workingNo;
			tmp.push(Ext.apply({}, filter));
		}

		if (record.stockLocation != "") {
			filter.type = "string";
			filter.field = "stockLocation.stockLocation";
			filter.value = record.stockLocation;
			tmp.push(Ext.apply({}, filter));
		}

		if (record.model != "") {
			filter.type = "string";
			filter.field = "model_contract";
			filter.value = record.model;
			tmp.push(Ext.apply({}, filter));
		}

		if (record.supplier != "") {
			filter.type = "string";
			filter.field = "contract.supplier";
			filter.value = record.supplier;
			tmp.push(Ext.apply({}, filter));
		}

		var p = store.getProxy();
		p.extraParams.filter = Ext.JSON.encode(tmp);
		// p.url = 'materialdocitems/2',
		p.url = store.proxyUrl;

		store.load();
	}
});