Ext.define('AM.controller.MaterialDocs', {
	extend : 'Ext.app.Controller',

	views : ['materialDoc.List', 'materialDoc.Edit', 'contract.Search',
			'master.stockLocation.Search'],
	stores : ['MaterialDocs', 'ContractType', 'Contracts', 'MaterialDocTypes'],
	models : ['MaterialDoc', 'MaterialDocItem', 'MaterialDocType', 'Contract',
			'ContractItem', 'master.stockLocation.StockLocation'],

	init : function(options) {

		Ext.apply(this, options);

		this.control({

					'materialDocList' : {
						itemdblclick : this.editMaterialDoc
					},
					'materialDocList button[action=add]' : {
						click : this.addMaterialDoc
					},

					'materialDocList button[action=delete]' : {
						click : this.deleteMaterialDoc
					},

					'materialDocList button[action=copy]' : {
						click : this.copyMaterialDoc
					},

					'contractSearch[by=materialDocEdit] button[action=search]' : {
						click : this.searchContract
					},
					'contractSearch gridpanel[by=materialDocEdit]' : {
						itemdblclick : this.selectContract
					},

					'materialDocEdit button[action=save]' : {
						click : this.saveMaterialDoc
					},

					'stockLocationSearch[by=materialDocEdit] button[action=search]' : {
						click : this.searchStockLocation
					},

					'stockLocationSearch[by=materialDocEdit] gridpanel' : {
						itemdblclick : this.selectStockLocation
					},

					'materialDocEdit button[action=cancel]' : {
						click : this.cancelMaterialDoc
					},

					'materialDocEdit button[action=add]' : {
						click : this.addMaterialDocItem
					},

					'materialDocEdit button[action=delete]' : {
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
	editMaterialDoc : function(grid, record) {

		var items = record.items();

		items.each(function(item) {

					if (item.get('lineId_in') == "") {
						var lineId_in = {
							'lineId' : item.get('lineId'),
							'version' : item.get('version')
						};
						item.set('lineId_in', lineId_in);
						// item.lineId_in = lineId_in;
						// item.dirty = false;
						item.commit(true);
					}

				}, this);

		var view = Ext.widget('materialDocEdit');

		view.down('form').loadRecord(record);
		view.down('form').setTitle('凭证号:' + record.get('docNo'));

		view.down('grid').reconfigure(record.items());

	},

	copyMaterialDoc : function(button) {
		var viewport = button.up('viewport');
		var grid = viewport.down('materialDocList');
		var selection = grid.getView().getSelectionModel().getSelection()[0];
		if (selection) {

			var record = selection.copy();
			Ext.data.Model.id(record);
			this.getStore('MaterialDocs').insert(0, record);

			record.setDocType({
						'id' : this.docType
					});

			record.set('cause', '采购');
			record.set('docNo', 0);
			//var items = selection.items();

			//items.each(function(item) {

			//			var item2 = item.copy();
			//			Ext.data.Model.id(item2);

			//			var lineId_in = {
			//				'lineId' : 0,
			//				'version' : 0
			//			};
			//			item2.set('lineId_in', lineId_in);
						// item.lineId_in = lineId_in;
						// item.dirty = false;
			//			item2.set('lineId', 0);
						// item2.set('lineId_test', 0);
						// item2.set('lineId_up', 0)
			//			item2.phantom = true;
						// item.commit(true);
			//			var itemStore = this.items();
			//			itemStore.insert(0, item2);
			//			item2.join(this.items());
			//		}, record);
			record.phantom = true;
			record.join(this.getStore('MaterialDocs'));
			var view = Ext.widget('materialDocEdit');
			view.down('form').loadRecord(record);
			view.down('form').setTitle('凭证号:' + record.get('docNo'));
			view.down('grid').reconfigure(record.items());

			// grid.getStore().remove(selection);
			// grid.getStore().sync();
		}

	},

	addMaterialDoc : function(button) {

		var record = new AM.model.MaterialDoc();
		record.setDocType({
					'id' : this.docType
				});
		record.set('cause', '采购');
		this.getStore('MaterialDocs').insert(0, record);
		record.store = this.getStore('MaterialDocs');
		var view = Ext.widget('materialDocEdit');

		view.down('form').loadRecord(record);
		view.down('form').setTitle('凭证号:' + record.get('docNo'));
		view.down('grid').reconfigure(record.items());

	},
	addMaterialDocItem : function(button) {
		var win = button.up('window');
		var grid = win.down('gridpanel');
		var store = grid.getStore();
		var record = new AM.model.MaterialDocItem();

		record.set('moveType', this.moveType1);
		record.set('direction', 1);
		var lineId_in = {
			'lineId' : 0,
			'version' : 0
		};
		record.set('lineId_in', lineId_in);
		store.insert(0, record);
		// record.store = store;

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
		var grid = viewport.down('materialDocList');
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

	selectStockLocation : function(grid, record) {
		var win = grid.up('window');
		win.close();

		var view = win.parentWindow;
		var grid = view.down('gridpanel');
		var itemRecord = grid.getView().getSelectionModel().getSelection()[0];
		itemRecord.setStockLocation(record);

	},
	selectContract : function(grid, record) {
		var win = grid.up('window');
		win.close();
		var view = win.parentWindow;
		var form = view.down('form');
		var oldRecord = form.getRecord();
		// oldRecord.set('contract_id', record.get('id'));
		oldRecord.set('contract', record.data);
		oldRecord.set('contractNo', record.get('contractNo'));
		form.loadRecord(oldRecord);

		var grid = view.down('gridpanel');
		var materialDocItems = grid.getStore();

		if (materialDocItems.getCount() == 0) {
			var contractItems = record.items();

			contractItems.each(function(contractItem) {

						var record = new AM.model.MaterialDocItem();
						record.set('model_contract', contractItem.get("model"));
						record.set('moveType', this.moveType1);
						record.set('direction', 1);
						var remainQuantity = contractItem.get("quantity")
								- contractItem.get('usedQuantity');
						record.set('netWeight', remainQuantity);
						var lineId_in = {
							'lineId' : 0,
							'version' : 0
						};
						record.set('usedQuantity', contractItem
										.get('usedQuantity'));
						record.set('remark', contractItem.get('remark'));
						record.set('lineId_in', lineId_in);
						materialDocItems.insert(0, record);

					}, this);

		}

		// oldRecord.get('contract').setDirty();
	},

	saveMaterialDoc : function(button) {

		var win = button.up('window');
		form = win.down('form');
		var record = form.getRecord();
		values = form.getValues();
		values.docDate = Ext.Date.parse(values.docDate, 'Y-m-d');
		record.set(values);

		// var itemsStore = record.getItemsStore();

		// record.data.items = win.down('grid').getStore();
		var items = record.items();
		var canUpdate = true;

		items.each(function(item) {

					var stockLocation = item.getStockLocation();
					if (stockLocation.get('id') == 0) {
						Ext.MessageBox.show({
									title : '错误信息',
									msg : '行项目上的仓库必输!',
									buttons : Ext.Msg.OK,
									icon : Ext.MessageBox.ERROR,
									closable : false,
									modal : true
								});
						canUpdate = false;

					}

				}, this);

		if (canUpdate) {
			
			if(record.store==undefined){
				var store = this.getMaterialDocsStore();
				record.join(store)
			}
			record.store.sync();
			win.close();
		}

		delete canUpdate;

	},

	cancelMaterialDoc : function(button) {
		var win = button.up('window');
		var form = win.down('form');
		var grid = win.down('gridpanel')

		grid.getStore().rejectChanges();
		this.getStore('MaterialDocs').rejectChanges();

	}

});