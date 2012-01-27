Ext.define('AM.controller.MaterialDocs', {
			extend : 'Ext.app.Controller',

			views : ['materialDoc.List', 'materialDoc.Edit', 'contract.Search'],
			stores : ['MaterialDocs', 'ContractType', 'Contracts'],
			models : ['MaterialDoc', 'MaterialDocItem'],

			init : function() {
				
				this.control({

							'materialDocList' : {
								itemdblclick : this.editMaterialDoc
							},
							// 'materialDocEdit trigger[name=contractNo]':{
							// onTriggerClick : this.searchContract
							// },
							'contractSearch button[action=search]' : {
								click : this.searchContract
							},
							'contractSearch gridpanel' : {
								itemdblclick : this.selectContract
							},

							'materialDocEdit button[action=save]' : {
								click : this.saveMaterialDoc
							}

						});

			},
			editMaterialDoc : function(grid, record) {
				var view = Ext.widget('materialDocEdit');

				view.down('form').loadRecord(record);
				view.down('form').setTitle('凭证号:' + record.get('docNo'));

				view.down('grid').reconfigure(record.items());

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
				oldRecord.set('contract', record.data);
				oldRecord.set('contractNo', record.get('contractNo'));
				form.loadRecord(oldRecord);
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

			}

		});