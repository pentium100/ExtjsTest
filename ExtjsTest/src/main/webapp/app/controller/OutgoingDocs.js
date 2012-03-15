Ext.define('AM.controller.OutgoingDocs', {
			extend : 'Ext.app.Controller',

			views : ['outgoingDoc.List', 'outgoingDoc.Edit', 'contract.Search', 'materialDoc.ItemSearch'],
			stores : ['OutgoingDocs', 'ContractType', 'Contracts',
					'MaterialDocTypes'],
			models : ['MaterialDoc', 'MaterialDocItem', 'MaterialDocType'],

			init : function(options) {

				Ext.apply(this, options);

				this.control({

							'outgoingDocList' : {
								itemdblclick : this.editMaterialDoc
							},
							'outgoingDocList button[action=add]' : {
								click : this.addMaterialDoc
							},

							'outgoingDocList button[action=delete]' : {
								click : this.deleteMaterialDoc
							},

							'materialDocItemSearch button[action=search]' : {
								click : this.searchMaterialDocItem
							},

							
							'materialDocItemSearch gridpanel' : {
								itemdblclick : this.selectMaterialDocItem
							},
							
							'contractSearch button[action=search]' : {
								click : this.searchContract
							},

							'contractSearch gridpanel[by=outgoingDocEdit]' : {
								itemdblclick : this.selectContract
							},

							'outgoingDocEdit button[action=save]' : {
								click : this.saveMaterialDoc
							},

							'outgoingDocEdit button[action=cancel]' : {
								click : this.cancelMaterialDoc
							},

							'outgoingDocEdit button[action=add]' : {
								click : this.addMaterialDocItem
							},

							'outgoingDocEdit button[action=delete]' : {
								click : this.deleteMaterialDocItem
							}

						});

			},
			editMaterialDoc : function(grid, record) {
				var view = Ext.widget('outgoingDocEdit');

				view.down('form').loadRecord(record);
				view.down('form').setTitle('凭证号:' + record.get('docNo'));
				
	
  
                //var store = record.items();
                
                //store.each(function(record){
                //	record.set('lineId_in_key', record.raw.lineId_in.lineId);
               	 
                //	record.getLineId_in({
                //	   success:function(model){
                //	   	 var me = this;
                //	   	 me['AM.model.MaterialDocItemBelongsToInstance'] = model;
                	   	 
                //	   },
                //	   scope: record
                	
                //	});
                
                //}, this);
                
                
				view.down('grid').reconfigure(record.items());

			},
			
			
			

			addMaterialDoc : function(button) {

				var record = new AM.model.MaterialDoc();
				record.setDocType({
							id : this.docType
						});
				this.getStore('OutgoingDocs').insert(0, record);
				var view = Ext.widget('outgoingDocEdit');
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

				// var edit = grid.editing;

				// edit.cancelEdit();
				store.insert(0, record);
				// edit.startEditByPosition({

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
				var grid = viewport.down('outgoingDocList');
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

				if (record.warehouse != "") {
					filter.type = "string";
					filter.field = "warehouse";
					filter.value = record.warehouse;
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
				itemRecord.set('lineId_in', record.data);
				itemRecord.set('model_contract', record.data.model_contract);
				itemRecord.set('model_tested', record.data.model_tested);
				itemRecord.set('deliveryNote', record.data.deliveryNote);
				itemRecord.set('batchNo', record.data.batchNo);
				itemRecord.set('plateNum', record.data.plateNum);
				itemRecord.set('warehouse', record.data.warehouse);
				itemRecord.set('netWeight', record.data.netWeight);
				itemRecord.set('direction', -1);
				
				
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

			//selectMaterialDocItem : function(grid, record) {
			//	var win = grid.up('window');
			//	win.close();
			//	var view = win.parentWindow;
			//	var form = view.down('form');
			//	var oldRecord = form.getRecord();

			//	oldRecord.setLineid_in(record.data);
			//	oldRecord.set('model_contract', record.get('model_contract'));
			//	form.loadRecord(oldRecord);

			//},

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

				this.getStore('OutgoingDocs').rejectChanges();

				grid.getStore().rejectChanges();

			}

		});