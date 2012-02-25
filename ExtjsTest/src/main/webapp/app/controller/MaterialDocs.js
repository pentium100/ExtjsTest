Ext.define('AM.controller.MaterialDocs', {
			extend : 'Ext.app.Controller',

			views : ['materialDoc.List', 'materialDoc.Edit', 'contract.Search'],
			stores : ['MaterialDocs', 'ContractType', 'Contracts',
					'MaterialDocTypes'],
			models : ['MaterialDoc', 'MaterialDocItem', 'MaterialDocType', 'Contract', 'ContractItem'],

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

							'contractSearch button[action=search]' : {
								click : this.searchContract
							},
							'contractSearch gridpanel' : {
								itemdblclick : this.selectContract
							},

							'materialDocEdit button[action=save]' : {
								click : this.saveMaterialDoc
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
			editMaterialDoc : function(grid, record) {
				var view = Ext.widget('materialDocEdit');

				view.down('form').loadRecord(record);
				view.down('form').setTitle('凭证号:' + record.get('docNo'));

				view.down('grid').reconfigure(record.items());

				var items = record.items();

				items.each(function(item) {

							if (item.get('lineId_in') == "") {
								var lineId_in = {
									'lineId' : item.get('lineId'),
									'version' : item.get('version')
								};
								item.set('lineId_in', lineId_in);
							}

						}, this);

			},

			addMaterialDoc : function(button) {

				var record = new AM.model.MaterialDoc();
				record.setDocType({
							'id' : this.docType
						});
				this.getStore('MaterialDocs').insert(0, record);
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

			},
			deleteMaterialDocItem : function(button) {
				var win = button.up('window');
				var grid = win.down('gridpanel');
				var store = grid.getStore();

				var selection = grid.getView().getSelectionModel()
						.getSelection()[0];
				if (selection) {
					store.remove(selection);
				}

			},

			deleteMaterialDoc : function(button) {
				var viewport = button.up('viewport');
				var grid = viewport.down('materialDocList');
				var selection = grid.getView().getSelectionModel()
						.getSelection()[0];
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

				var grid = view.down('gridpanel');
				var materialDocItems = grid.getStore();

				var contractItems = record.items();

				contractItems.each(function(contractItem) {

							var record = new AM.model.MaterialDocItem();
							record.set('model_contract', contractItem.get("model"));
							record.set('moveType', this.moveType1);
							record.set('direction', 1);
							var lineId_in = {
								'lineId' : 0,
								'version' : 0
							};
							record.set('lineId_in', lineId_in);
							materialDocItems.insert(0, record);

							

						}, this);

				// oldRecord.get('contract').setDirty();
			},

			saveMaterialDoc : function(button) {

				var win = button.up('window');
				form = win.down('form');
				var record = form.getRecord();
				values = form.getValues();
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

				this.getStore('MaterialDocs').rejectChanges();

				grid.getStore().rejectChanges();

			}

		});