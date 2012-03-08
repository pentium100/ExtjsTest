Ext.define('AM.controller.Messages', {
			extend : 'Ext.app.Controller',

			views  : ['message.MessagePortal', 'message.MessageList', 'message.MessageEdit'],
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
				var grid = button.up('gridpanel');
				
				var selection = grid.getView().getSelectionModel().getSelection()[0];
				if (selection) {
					
					
					if(selection.get("owner")==_DEFAULT_USER_NAME){
					
						grid.getStore().remove(selection);
						grid.getStore().sync();
					}else{
						
						Ext.MessageBox.show({
													title : '错误信息',
													msg : '您只能删除自已创建的信息！',
													buttons : Ext.MessageBox.OK,
													icon : Ext.MessageBox.ERROR,
													closable : false,
													modal : true
												});
					}
						
				}
			},

			editMessage : function(grid, record) {
				//console.log('Double clicked on ' + record.get('id'));
				var view = Ext.widget('messageEdit', {parentGrid:grid});

				view.down('form').loadRecord(record);
				var grid = view.down('grid');
				grid.reconfigure(record.specifications());
				
				if(record.get("owner") != _DEFAULT_USER_NAME){
					
					Ext.each(view.down('form').items.items, function(item, index, items){
						
						
						if(item.name!="remark"){
							item.setReadOnly ( true);
						}
						
					});
					
				
					
				}else{
					
					Ext.each(view.down('form').items.items, function(item, index, items){
						
						item.setReadOnly ( false);
						
					});
					
				}
				
					Ext.each(grid.plugins, function(plugin){
						
						if(plugin.name=="cellEditing"){
							
							plugin.addListener("beforeedit", function(e, eOpts){
								
								    if(eOpts.get("owner") != _DEFAULT_USER_NAME){
										return false;
									}
									return true;
								  
								},grid, record)
							
						}
						
						
					});
					
				var field = view.down('textfield[name=owner]');	
				if(field){
					field.setReadOnly(true);
				}
				
				
				field = view.down('numberfield[name=suggestedPrice]');
				if(field){
					field.setReadOnly (_DEFAULT_USER_LEVEL < 50);
				}

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
				
				var field = view.down('textfield[name=owner]');	
				if(field){
					field.setReadOnly(true);
				}
				
				
				field = view.down('numberfield[name=suggestedPrice]');
				if(field){
					field.setReadOnly (_DEFAULT_USER_LEVEL < 50);
				}

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
