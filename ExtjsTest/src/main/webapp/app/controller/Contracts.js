Ext.define('AM.controller.Contracts', {
			extend : 'Ext.app.Controller',

			views : ['contract.List', 'contract.Edit', 'master.employee.Search'],
			stores : ['Contracts', 'ContractType', 'master.employee.Employees'],
			models : ['Contract', 'ContractItem', 'master.employee.Employee'],

			init : function() {
				this.control({
							'contractList' : {
								itemdblclick : this.editContract,
								activate : this.loadContractData,
								destroy : this.freeUpStore
							},

							'contractEdit button[action=save]' : {
								click : this.updateContract
							},
							'contractList button[text=Add]' : {
								click : this.addContract
							},
							'contractList button[text=Delete]' : {
								click : this.deleteContract
							},

							'contractEdit button[text=Add]' : {
								click : this.addContractItem
							},
							'contractEdit button[text=Delete]' : {
								click : this.deleteContractItem
							},

							'contractEdit button[text=Cancel]' : {
								click : this.cancelUpdate
							}

						});

			},
			loadContractData : function() {
				this.getStore('Contracts').load();
			},
			freeUpStore : function() {
				// var store = this.getStore('Contracts');
				// Ext.data.StoreManager.remove(store);
			},
			deleteContract : function(button) {
				var viewport = button.up('viewport');
				var grid = viewport.down('contractList');
				var selection = grid.getView().getSelectionModel()
						.getSelection()[0];
				if (selection) {
					this.getStore('Contracts').remove(selection);
					this.getStore('Contracts').sync();
				}
			},

			editContract : function(grid, record) {
				// console.log('Double clicked on ' + record.get('contractNo'));
				var view = Ext.widget('contractEdit');

				var form = view.down('form');
				form.loadRecord(record);

				// form.getForm().setValues('employee',record.getEmployee().getId());
				var emp = form.down('combobox[name=employee]');

				emp.setValue(record.getEmployee().getId());

				//var store = record.items();
				//store.group('model');

				view.down('grid').reconfigure(record.items());

			},
			addContract : function(button) {
				var record = new AM.model.Contract();
				this.getStore('Contracts').insert(0, record);
				// record.store = this.getStore('Contracts');
				record.join(this.getStore('Contracts'));
				var view = Ext.widget('contractEdit');
				view.down('form').loadRecord(record);
				view.down('grid').reconfigure(record.items());

			},

			addContractItem : function(button) {
				var win = button.up('window');
				var grid = win.down('gridpanel');
				var store = grid.getStore();
				var record = new AM.model.ContractItem();

				// var edit = grid.editing;

				// edit.cancelEdit();
				store.insert(0, record);
				// edit.startEditByPosition({
				// row: 0,
				// column: 1
				// });

			},

			deleteContractItem : function(button) {
				var win = button.up('window');
				var grid = win.down('gridpanel');
				var store = grid.getStore();

				var selection = grid.getView().getSelectionModel()
						.getSelection()[0];
				if (selection) {
					store.remove(selection);
				}
			},

			updateContract : function(button) {
				var win = button.up('window');
				var form = win.down('form');
				var record = form.getRecord();
				var values = form.getValues();
				values.signDate = Ext.Date.parse(values.signDate, 'Y-m-d');
				record.set(values);
				var emp = form.down('combobox[name=employee]');
				var store = emp.getStore();

				record.setEmployee(store.getById(values.employee));

				// record.data.items = win.down('grid').getStore();
				this.getStore('Contracts').sync();
				win.close();
			},
			cancelUpdate : function(button) {
				var win = button.up('window');
				var grid = win.down('gridpanel');

				grid.getStore().rejectChanges();
				this.getStore('Contracts').rejectChanges();

				// grid.getStore().each(function(record) {
				// record.reject();
				// })
				win.close();
			},
			onPanelRendered : function() {
				// console.log('The panel was rendered');
			}
		});