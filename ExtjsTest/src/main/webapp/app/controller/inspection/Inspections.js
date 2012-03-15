Ext.define('AM.controller.inspection.Inspections', {
			extend : 'Ext.app.Controller',

			views : ['inspection.List', 'inspection.Edit', 'materialDoc.ItemSearch'],
			stores : ['inspection.Inspections'],
			models : ['inspection.Inspection','inspection.InspectionItem','MaterialDoc', 'MaterialDocItem', 'MaterialDocType'],

			init : function(options) {

				Ext.apply(this, options);

				this.control({

							'inspectionList' : {
								itemdblclick : this.editInspection
							},
							'inspectionList button[action=add]' : {
								click : this.addInspection
							},

							'inspectionList button[action=delete]' : {
								click : this.deleteInspection
							},

							'materialDocItemSearch button[action=search]' : {
								click : this.searchMaterialDocItem
							},

							
							'materialDocItemSearch gridpanel[by=inspectionEdit]' : {
								itemdblclick : this.selectMaterialDocItem
							},
							
							'inspectionEdit button[action=save]' : {
								click : this.saveInspection
							},

							'inspectionEdit button[action=cancel]' : {
								click : this.cancelInspection
							},

							'inspectionEdit button[action=add]' : {
								click : this.addInspectionItem
							},

							'inspectionEdit button[action=delete]' : {
								click : this.deleteInspectionItem
							}

						});

			},
			editInspection : function(grid, record) {
				var view = Ext.widget('inspectionEdit');

				view.down('form').loadRecord(record);
				view.down('form').setTitle('凭证号:' + record.get('id'));
				
	            
				view.down('grid').reconfigure(record.items());

			},
			
			
			

			addInspection : function(button) {

				var record = new AM.model.inspection.Inspection();
				var grid = button.up('gridpanel');
				var store = grid.getStore();
				store.insert(0,record);
				//this.getStore('OutgoingDocs').insert(0, record);
				
				var view = Ext.widget('inspectionEdit');
				view.down('form').loadRecord(record);
				view.down('form').setTitle('凭证号:' + record.get('id'));
				view.down('grid').reconfigure(record.items());

			},
			addInspectionItem : function(button) {
				var win = button.up('window');
				var grid = win.down('gridpanel');
				var store = grid.getStore();
				var record = new AM.model.inspection.InspectionItem();
				
				store.insert(0, record);


			},
			deleteInspectionItem : function(button) {
				var win = button.up('window');
				var grid = win.down('gridpanel');
				var store = grid.getStore();

				var selection = grid.getView().getSelectionModel()
						.getSelection()[0];
				if (selection) {
					store.remove(selection);
				}

			},

			deleteInspection : function(button) {
				var viewport = button.up('viewport');
				var grid = viewport.down('inspectionList');
				var selection = grid.getView().getSelectionModel()
						.getSelection()[0];
				if (selection) {
					grid.getStore().remove(selection);
					grid.getStore().sync();
				}

			},

			searchMaterialDocItem : function(button) {
				var win = button.up('window');
				var grid = win.down('gridpanel');
				var store = grid.getStore();
				

				var tmp = [];
				var filter = {};
				var record = button.up('form').getValues();

				filter.type = "int";
				filter.field = "materialDoc.docType";
				filter.comparison = 'eq';
				filter.value = '1';
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

				if (record.warehouse != "") {
					filter.type = "string";
					filter.field = "warehouse";
					filter.value = record.warehouse;
					tmp.push(Ext.apply({}, filter));
				}

				var p = store.getProxy();
				p.extraParams.filter = Ext.JSON.encode(tmp);
				p.url = 'materialdocitems/2',

				store.load();
			},
			
			selectMaterialDocItem : function(grid, record) {
				var win = grid.up('window');
				win.close();
				var view = win.parentWindow;
				var grid = view.down('gridpanel');
				var itemRecord = grid.getView().getSelectionModel().getSelection()[0];
				itemRecord.set('materialDocItem', record.data);
				itemRecord.set('contractNo', record.data.contractNo);
				itemRecord.set('model_contract', record.data.model_contract);
				
				itemRecord.set('deliveryNote', record.data.deliveryNote);
				itemRecord.set('batchNo', record.data.batchNo);
				itemRecord.set('plateNum', record.data.plateNum);
				itemRecord.set('netWeight', record.data.netWeight);

				
				
			},

			saveInspection : function(button) {

				var win = button.up('window');
				form = win.down('form');
				var record = form.getRecord();
				values = form.getValues();
				values.inspectionDate = Ext.Date.parse(values.inspectionDate, 'Y-m-d');
				record.set(values);

				// record.data.items = win.down('grid').getStore();
				record.store.sync();
				win.close();

			},

			cancelMaterialDoc : function(button) {
				var win = button.up('window');
				var form = win.down('form');
				var grid = win.down('gridpanel')
				

				win.parentGrid.getStore().rejectChanges();

				grid.getStore().rejectChanges();

			}

		});