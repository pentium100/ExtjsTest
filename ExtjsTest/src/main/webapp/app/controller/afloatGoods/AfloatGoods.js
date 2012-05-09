Ext.define('AM.controller.afloatGoods.AfloatGoods', {

	extend : 'Ext.app.Controller',

	views : ['afloatGoods.List', 'afloatGoods.Edit', 'contract.Search'],
	stores : ['afloatGoods.AfloatGoods', 'ContractType', 'Contracts'],
	models : ['afloatGoods.AfloatGoods', 'afloatGoods.AfloatGoodsItem',
			'Contract', 'ContractItem'],

	init : function() {
		this.control({
					'afloatGoodsList' : {
						itemdblclick : this.editAfloatGoods,
						activate : this.loadAfloatGoodsData

					},

					'contractSearch[by=afloatGoodsEdit] button[action=search]' : {
						click : this.searchContract
					},
					'contractSearch gridpanel[by=afloatGoodsEdit]' : {
						itemdblclick : this.selectContract
					},

					'afloatGoodsEdit button[action=save]' : {
						click : this.updateAfloatGoods
					},
					'afloatGoodsList button[text=Add]' : {
						click : this.addAfloatGoods
					},
					'afloatGoodsList button[text=Delete]' : {
						click : this.deleteAfloatGoods
					},

					'afloatGoodsEdit button[text=Add]' : {
						click : this.addAfloatGoodsItem
					},
					'afloatGoodsEdit button[text=Delete]' : {
						click : this.deleteAfloatGoodsItem
					},

					'afloatGoodsEdit button[text=Cancel]' : {
						click : this.cancelUpdate
					}

				});

	},
	loadAfloatGoodsData : function() {
		this.getStore('afloatGoods.AfloatGoods').load();
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

	selectContract : function(grid, record) {
		var win = grid.up('window');
		win.close();
		var view = win.parentWindow;
		var form = view.down('form');
		var oldRecord = form.getRecord();
		// oldRecord.set('contract_id', record.get('id'));
		//oldRecord.setContract(record.data);
		oldRecord.set('contract', record.data);
		oldRecord.set('contractNo', record.get('contractNo'));
		oldRecord.set('supplier', record.get('supplier'));		
		form.loadRecord(oldRecord);

		var grid = view.down('gridpanel');
		var afloatGoodsItems = grid.getStore();

		if (afloatGoodsItems.getCount() == 0) {
			var contractItems = record.items();
			contractItems.each(function(contractItem) {

						var record = new AM.model.afloatGoods.AfloatGoodsItem();
						record.set('model', contractItem.get("model"));
						afloatGoodsItems.insert(0, record);

					}, this);

		}

	},

	deleteAfloatGoods : function(button) {
		var viewport = button.up('viewport');
		var grid = viewport.down('afloatGoodsList');

		
		var selection = grid.getView().getSelectionModel().getSelection()[0];
		var store = grid.getStore();
		if (selection) {
			store.remove(selection);
			store.sync();
		}
	},

	editAfloatGoods : function(grid, record) {

		var view = Ext.widget('afloatGoodsEdit', {
					parentGrid : grid
				});

		view.down('form').loadRecord(record);

		view.down('grid').reconfigure(record.items());

	},
	addAfloatGoods : function(button) {
		var record = new AM.model.afloatGoods.AfloatGoods();
		var viewport = button.up('viewport');
		var grid = viewport.down('afloatGoodsList');
		var store = grid.getStore();
        
		store.insert(0, record);
		record.store = store;
		var view = Ext.widget('afloatGoodsEdit', {
					parentGrid : grid
			
				});
		view.down('form').loadRecord(record);
		view.down('grid').reconfigure(record.items());

	},

	addAfloatGoodsItem : function(button) {
		var win = button.up('window');
		var grid = win.down('gridpanel');
		var store = grid.getStore();
		var record = new AM.model.afloatGoods.AfloatGoodsItem();
		store.insert(0, record);

	},

	deleteAfloatGoodsItem : function(button) {
		var win = button.up('window');
		var grid = win.down('gridpanel');
		var store = grid.getStore();

		var selection = grid.getView().getSelectionModel().getSelection()[0];
		if (selection) {
			store.remove(selection);
		}
	},

	updateAfloatGoods : function(button) {
		var win = button.up('window');
		form = win.down('form');
		var record = form.getRecord();
		values = form.getValues();

		values.transportDate = Ext.Date.parse(values.transportDate, 'Y-m-d');

		values.dispatchDate = Ext.Date.parse(values.dispatchDate, 'Y-m-d');
		values.eta = Ext.Date.parse(values.eta, 'Y-m-d');
		values.arrivalDate = Ext.Date.parse(values.arrivalDate, 'Y-m-d');

		record.set(values);

		// record.data.items = win.down('grid').getStore();
		// this.getStore('Contracts').sync();
		win.parentGrid.getStore().sync();
		win.close();
	},
	cancelUpdate : function(button) {
		var win = button.up('window');
		var grid = win.down('gridpanel');

		grid.getStore().rejectChanges();
		win.parentGrid.getStore().rejectChanges();
		// this.getStore('AfloatGoods').rejectChanges();

		// grid.getStore().each(function(record) {
		// record.reject();
		// })
		win.close();
	}
});