Ext.define('AM.controller.Messages', {
			extend : 'Ext.app.Controller',

			views : ['message.MessageList', 'message.MessageEdit'],
			stores : ['MessagesStore', 'MessageTypesStore'],
			models : ['Message', 'Specification'],

			init : function() {
				this.control({
							'messageList ' : {
								itemdblclick : this.editMessage
								
							},
							
							'messageEdit button[action=save]' : {
								click : this.updateMessage
							},
							'messageList button[text=Add]' : {
								click : this.addMessage
							},
							'messageList button[text=Delete]' : {
								click : this.deleteMessage
							},

							'messageEdit button[text=Cancel]' : {
								click : this.cancelUpdate
							}

						});

			},

			deleteMessage : function(button) {
				var viewport = button.up('viewport');
				var grid = viewport.down('messageList');
				var selection = grid.getView().getSelectionModel()
						.getSelection()[0];
				if (selection) {
					grid.getStore().remove(selection);
					grid.getStore().sync();
				}
			},

			editMessage : function(grid, record) {
				console.log('Double clicked on ' + record.get('id'));
				var view = Ext.widget('messageEdit');

				view.down('form').loadRecord(record);

				view.down('grid').reconfigure(record.specifications());

			},
			addMessage : function(button) {
				var record = new AM.model.Message();
				this.getStore('MessagesStore').insert(0, record);
				var view = Ext.widget('messageEdit');
				view.down('form').loadRecord(record);
				
				
				var items = record.specifications();
				
				var specification = new AM.model.Specification();
				specification.set('specification', 'AFT');
				//specification.setDirty(false);
				items.insert(0, specification);
				
				specification = new AM.model.Specification();
				specification.set('specification', 'HGI');
				//specification.setDirty(false);
				items.insert(0, specification);
				
				specification = new AM.model.Specification();
				specification.set('specification', 'NAR');
				//specification.setDirty(false);
				items.insert(0, specification);
				
				specification = new AM.model.Specification();
				specification.set('specification', 'Sulphur');
				//specification.setDirty(false);
				items.insert(0, specification);
				
				specification = new AM.model.Specification();
				specification.set('specification', 'Volatile Matter');
				//specification.setDirty(false);
				items.insert(0, specification);
				
				specification = new AM.model.Specification();
				specification.set('specification', 'Ash');
				//specification.setDirty(false);
				items.insert(0, specification);
				
				specification = new AM.model.Specification();
				specification.set('specification', 'Inherent Moisture');
				//specification.setDirty(false);
				items.insert(0, specification);
				
				specification = new AM.model.Specification();
				specification.set('specification', 'Total Moisture');
				//specification.setDirty(false);
				items.insert(0, specification);
				
				//items.sync();
				
				
				view.down('grid').reconfigure(record.specifications());

			},


			updateMessage : function(button) {
				var win = button.up('window');
				form = win.down('form');
				var record = form.getRecord();
				values = form.getValues();

				record.set(values);

				// record.data.items = win.down('grid').getStore();
				this.getStore('MessagesStore').sync();
				win.close();
			},
			cancelUpdate : function(button) {
				var win = button.up('window');
				var grid = win.down('gridpanel');
				
				grid.getStore().rejectChanges();
				this.getStore('MessagesStore').rejectChanges();

				//grid.getStore().each(function(record) {
				//			record.reject();
				//		})
				win.close();
			},
			onPanelRendered : function() {
				console.log('The panel was rendered');
			}
		});