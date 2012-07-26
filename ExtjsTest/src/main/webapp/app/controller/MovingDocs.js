Ext.define('AM.controller.MovingDocs', {
	extend : 'Ext.app.Controller',

	views : ['movingDoc.List', 'movingDoc.Edit', 'materialDoc.ItemSearch',
			'contract.Search', 'master.stockLocation.Search'],

	stores : ['MovingDocs', 'MaterialDocTypes', 'ContractType', 'Contracts'],
	models : ['MaterialDoc', 'MaterialDocItem', 'MaterialDocType',
			'master.stockLocation.StockLocation'],

	init : function(options) {

		Ext.apply(this, options);

		this.control({

					'movingDocList' : {
						itemdblclick : this.editMaterialDoc
					},
					'movingDocList button[action=add]' : {
						click : this.addMaterialDoc
					},

					'movingDocList button[action=delete]' : {
						click : this.deleteMaterialDoc
					},

					'stockLocationSearch[by=movingDocEdit] button[action=search]' : {
						click : this.searchStockLocation
					},

					'stockLocationSearch[by=movingDocEdit] gridpanel' : {
						itemdblclick : this.selectStockLocation
					},

					// 'contractSearch gridpanel[by=movingDocEdit] >
					// button[action=search]' : {
					'contractSearch[by=movingDocEdit] button[text="Search"]' : {
						click : this.searchContract
					},
					'contractSearch gridpanel[by=movingDocEdit]' : {
						itemdblclick : this.selectContract
					},

					'materialDocItemSearch[by=movingDocEdit] button[action=search]' : {
						click : this.searchMaterialDocItem
					},

					'materialDocItemSearch[by=movingDocEdit] button[action=add]' : {
						click : this.addMaterialDocItems
					},

					'materialDocItemSearch gridpanel[by=movingDocEdit]' : {
						itemdblclick : this.selectMaterialDocItem
					},

					'movingDocEdit button[action=save]' : {
						click : this.saveMaterialDoc
					},

					'movingDocEdit button[action=cancel]' : {
						click : this.cancelMaterialDoc
					},

					'movingDocEdit button[action=add]' : {
						click : this.addMaterialDocItem
					},

					'movingDocEdit button[action=delete]' : {
						click : this.deleteMaterialDocItem
					}

				});

	},

	searchStockLocation : function(button) {
		var win = button.up('window');
		var grid = win.down('gridpanel');
		var store = grid.getStore();

		var tmp = [];
		var filter = {};
		var record = button.up('form').getValues();
		if (record.stockLocation != "") {
			filter.type = "string";
			filter.field = "stockLocation";
			filter.value = record.stockLocation;
			tmp.push(Ext.apply({}, filter));
		}

		var p = store.getProxy();
		p.extraParams.filter = Ext.JSON.encode(tmp);

		store.load();

	},

	selectStockLocation : function(grid, record) {
		var win = grid.up('window');
		win.close();

		var view = win.parentWindow;

		var form = view.down('form');
		var oldRecord = form.getRecord();

		oldRecord.setStockLocation(record);

		var values = form.getValues();
		values.docDate = Ext.Date.parse(values.docDate, 'Y-m-d');
		values.targetWarehouse = record.get('stockLocation');
		// oldRecord.set('targetWarehouse', record.data.stockLocation);
		oldRecord.set(values);

		form.loadRecord(oldRecord);
		// var grid = view.down('gridpanel');
		// var itemRecord =
		// grid.getView().getSelectionModel().getSelection()[0];
		// itemRecord.setStockLocation(record);

	},
	editMaterialDoc : function(grid, record) {
		var view = Ext.widget('movingDocEdit', {
					parentGrid : grid
				});

		view.down('form').loadRecord(record);
		view.down('form').setTitle('凭证号:' + record.get('docNo'));

		view.down('grid').reconfigure(record.items());
		var store = record.items();
		store.group('model_contract');
	},

	addMaterialDoc : function(button) {

		var record = new AM.model.MaterialDoc();
		record.setDocType({
					id : this.docType

				});
		record.set('cause', '移仓');

		// record.set("contract",{});
		var grid = button.up("gridpanel");

		grid.getStore().insert(0, record);

		record.join(grid.getStore());
		record.getStockLocation();

		var view = Ext.widget('movingDocEdit', {
					parentGrid : grid
				});

		view.down('form').loadRecord(record);
		view.down('form').setTitle('凭证号:' + record.get('docNo'));
		view.down('grid').reconfigure(record.items());
		var store = record.items();
		store.group('model_contract');
	},

	addMaterialDocItems : function(button) {
		var win = button.up('window');

		var searchGrid = win.down('gridpanel');

		var selModel = searchGrid.getView().getSelectionModel();
		var items = selModel.getSelection();
		if (selModel.mode == "SINGLE" && items.length == 1) {

			return this.selectMaterialDocItem(searchGrid, items[0]);
		}

		win.close();
		var view = win.parentWindow;
		var grid = view.down('gridpanel');
		var store = grid.getStore();
		var i;
		for (i = 0; i < items.length; i++) {
			var itemRecord = new AM.model.MaterialDocItem();
			itemRecord.getStockLocation();
			itemRecord.set('moveType', this.moveType1);

			var record = items[i];
			itemRecord.set('lineId_in', record.data);
			itemRecord.set('contractNo', record.data.contractNo);
			itemRecord.set('model_contract', record.data.model_contract);
			itemRecord.set('model_tested', record.data.model_tested);
			itemRecord.set('deliveryNote', record.data.deliveryNote);
			itemRecord.set('batchNo', record.data.batchNo);
			itemRecord.set('plateNum', record.data.plateNum);
			itemRecord.set('warehouse', record.data.warehouse);
			itemRecord.setStockLocation(record.getStockLocation());
			itemRecord.set('netWeight', record.data.netWeight);
			itemRecord.set('direction', -1);
			store.insert(0, itemRecord);
			itemRecord.join(store);

		}

		view = grid.getView();
		view.refresh();

	},

	addMaterialDocItem : function(button) {

		var win = button.up('window');
		var view = Ext.widget('materialDocItemSearch', {
					parentWindow : win,
					by : win.xtype,
					selMode : 'MULTI'
				});

		view.show();

	},
	deleteMaterialDocItem : function(button) {
		var win = button.up('window');
		var grid = win.down('gridpanel');
		var store = grid.getStore();

		var selection = grid.getView().getSelectionModel().getSelection()[0];
		if (selection) {
			store.remove(selection);
		}

	},

	deleteMaterialDoc : function(button) {
		var viewport = button.up('viewport');
		var grid = viewport.down('movingDocList');
		var selection = grid.getView().getSelectionModel().getSelection()[0];
		if (selection) {
			grid.getStore().remove(selection);
			grid.getStore().sync();
		}

	},
	searchContract : function(button) {
		var win = button.up('window');
		var grid = win.down('gridpanel');
		var store = grid.getStore();

		var tmp = [];
		var filter = {};
		var record = button.up('form').getValues();
		if (record.contractNo != "") {
			filter.type = "string";
			filter.field = "contractNo";
			filter.value = record.contractNo;
			tmp.push(Ext.apply({}, filter));
		}

		if (record.supplier != "") {
			filter.type = "string";
			filter.field = "supplier";
			filter.value = record.supplier;
			tmp.push(Ext.apply({}, filter));
		}
		if (record.contractType != "") {
			filter.type = "list";
			filter.field = "contractType";
			filter.value = record.contractType;
			tmp.push(Ext.apply({}, filter));
		}

		if (record.model != "") {
			filter.type = "string";
			filter.field = "items.model";
			filter.value = record.model;
			tmp.push(Ext.apply({}, filter));
		}

		var p = store.getProxy();
		p.extraParams.filter = Ext.JSON.encode(tmp);

		store.load();
	},

	searchMaterialDocItem : function(button) {
		var win = button.up('window');
		var grid = win.down('gridpanel');
		var store = grid.getStore();

		var tmp = [];
		var filter = {};
		var record = button.up('form').getValues();
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
			filter.field = "batchNo";
			filter.value = record.batchNo;
			tmp.push(Ext.apply({}, filter));
		}

		if (record.workingNo != "") {
			filter.type = "string";
			filter.field = "workingNo";
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
		var p = store.getProxy();
		p.extraParams.filter = Ext.JSON.encode(tmp);

		store.load();
	},

	selectMaterialDocItem : function(grid, record) {
		var win = grid.up('window');
		win.close();
		var view = win.parentWindow;
		var grid = view.down('gridpanel');
		var itemRecord = grid.getView().getSelectionModel().getSelection()[0];
		if (itemRecord != undefined) {
			itemRecord.set('lineId_in', record.data);
			itemRecord.set('contractNo', record.data.contractNo);
			itemRecord.set('model_contract', record.data.model_contract);
			itemRecord.set('model_tested', record.data.model_tested);
			itemRecord.set('deliveryNote', record.data.deliveryNote);
			itemRecord.set('batchNo', record.data.batchNo);
			itemRecord.set('plateNum', record.data.plateNum);
			itemRecord.set('warehouse', record.data.warehouse);
			itemRecord.setStockLocation(record.getStockLocation());
			itemRecord.set('netWeight', record.data.netWeight);
			itemRecord.set('direction', -1);
		}
	},
	selectContract : function(grid, record) {
		var win = grid.up('window');
		win.close();
		var view = win.parentWindow;
		var form = view.down('form');
		var oldRecord = form.getRecord();
		// oldRecord.set('contract_id', record.get('id'));
		oldRecord.setContract(record.data);
		oldRecord.set('contractNo', record.get('contractNo'));
		form.loadRecord(oldRecord);
		// oldRecord.get('contract').setDirty();
	},

	// selectMaterialDocItem : function(grid, record) {
	// var win = grid.up('window');
	// win.close();
	// var view = win.parentWindow;
	// var form = view.down('form');
	// var oldRecord = form.getRecord();

	// oldRecord.setLineid_in(record.data);
	// oldRecord.set('model_contract', record.get('model_contract'));
	// form.loadRecord(oldRecord);

	// },

	saveMaterialDoc : function(button) {

		var win = button.up('window');
		form = win.down('form');
		var record = form.getRecord();

		values = form.getValues();
		if (values.targetWarehouse == "") {
			return;
		}

		values.docDate = Ext.Date.parse(values.docDate, 'Y-m-d');
		record.set(values);

		// record.data.items = win.down('grid').getStore();
		record.store.sync();
		win.close();

	},

	cancelMaterialDoc : function(button) {
		var win = button.up('window');
		var form = win.down('form');
		var grid = win.down('gridpanel')

		// this.getStore('OutgoingDocs').rejectChanges();
		win.parentGrid.getStore().rejectChanges();

		grid.getStore().rejectChanges();

	}

});