Ext.define('AM.controller.Messages', {
			extend : 'Ext.app.Controller',

			views : ['message.MessageList', 'message.MessageEdit'],
			stores : ['MessageTypesStore'],
			models : ['Message', 'Specification'],

			init : function(param) {
				
				if(param){
					
					//var filters = this.getView('message.MessageList').filters;
					//alter(filters.getFilterData());
					Ext.applyIf(this,param);
					
				}
				
				this.control({
							'messageList' : {
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
				var selection = grid.getView().getSelectionModel().getSelection()[0];
				if (selection) {
					grid.getStore().remove(selection);
					grid.getStore().sync();
				}
			},

			editMessage : function(grid, record) {
				//console.log('Double clicked on ' + record.get('id'));
				var view = Ext.widget('messageEdit', {parentGrid:grid});

				view.down('form').loadRecord(record);

				view.down('grid').reconfigure(record.specifications());

			},
			addMessage : function(button) {
				
				var record = new AM.model.Message();
				var grid = button.up('gridpanel');
					if(grid){
						if(grid.id=="messageList-1"){
							
							record.set('type', "供应");							
						}
						if(grid.id=="messageList-2"){
							
							record.set('type', "需求");							
						}
						if(grid.id=="messageList-3"){
							
							record.set('type', "敞口");							
						}
						if(grid.id=="messageList-4"){
							
							record.set('type', "锁定");							
						}


					}
				
				
				
				if(_DEFAULT_USER_NAME){
					record.set('owner', _DEFAULT_USER_NAME);
				}
				
				grid.getStore().insert(0, record);
				var view = Ext.widget('messageEdit', {parentGrid:grid});
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
				var form = win.down('form');
				var record = form.getRecord();
				var values = form.getValues();
				
				values.validBefore = Ext.Date.parse(values.validBefore, 'Y-m-d');

				record.set(values);

				// record.data.items = win.down('grid').getStore();
				var grid = win.parentGrid;
				grid.getStore().sync();
				win.close();
			},
			cancelUpdate : function(button) {
				var win = button.up('window');
				var grid = win.down('gridpanel');
				
				grid.getStore().rejectChanges();
				grid = win.parentGrid;
				grid.getStore().rejectChanges();

				//grid.getStore().each(function(record) {
				//			record.reject();
				//		})
				win.close();
			},
			onPanelRendered : function() {
				//console.log('The panel was rendered');
			}
		});
