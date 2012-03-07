Ext.define('AM.controller.userDetail.UserDetail', {
			extend : 'Ext.app.Controller',

			views : ['userDetail.List', 'userDetail.Edit'],
			stores : ['userDetail.UserDetail'],
			models : ['userDetail.UserDetail','userDetail.SecurityRole'],

			init : function() {
				this.control({
							'userDetailList' : {
								itemdblclick : this.editUserDetail
								,activate:this.loadUserDetailData
								
							},
							
							'userDetailEdit button[action=save]' : {
								click : this.updateUserDetail
							},
							'userDetailList button[text=Add]' : {
								click : this.addUserDetail
							},
							'userDetailList button[text=Delete]' : {
								click : this.deleteUserDetail
							},

							'userDetailEdit button[text=Cancel]' : {
								click : this.cancelUpdate
							},
							
							'userDetailEdit button[action=add]' : {
								click : this.addUserDetailRole
							},

							'userDetailEdit button[action=delete]' : {
								click : this.deleteUserDetailRole
							}
							

						});

			},
            loadUserDetailData:function(){
            	this.getStore('userDetail.UserDetail').load();
            },
			deleteUserDetail : function(button) {
				var viewport = button.up('viewport');
				var grid = viewport.down('userDetailList');
				var selection = grid.getView().getSelectionModel()
						.getSelection()[0];
				var store = grid.getStore();
				if (selection) {
					store.remove(selection);
					store.sync();
				}
			},

			editUserDetail : function(grid, record) {
				
				var view = Ext.widget('userDetailEdit');

				view.down('form').loadRecord(record);
				
				view.down('grid').reconfigure(record.roles());
				

				

			},
			
			
			
			addUserDetail : function(button) {
				var record = new AM.model.userDetail.UserDetail();
				
				this.getStore('userDetail.UserDetail').insert(0, record);
				
				var view = Ext.widget('userDetailEdit');
				view.down('form').loadRecord(record);
				
				view.down('grid').reconfigure(record.roles());
				

			},

			
			addUserDetailRole : function(button) {
				var win = button.up('window');
				var grid = win.down('gridpanel');
				var store = grid.getStore();
				var record = new AM.model.userDetail.SecurityRole();
	
				
				store.insert(0, record);

			},
			deleteUserDetailRole : function(button) {
				var win = button.up('window');
				var grid = win.down('gridpanel');
				var store = grid.getStore();
	
				var selection = grid.getView().getSelectionModel().getSelection()[0];
				if (selection) {
					store.remove(selection);
				}

			},
			

			updateUserDetail : function(button) {
				var win = button.up('window');
				form = win.down('form');
				var record = form.getRecord();
				values = form.getValues();

				record.set(values);

				// record.data.items = win.down('grid').getStore();
				this.getStore('userDetail.UserDetail').sync();
				win.close();
			},
			cancelUpdate : function(button) {
				var win = button.up('window');
				this.getStore('userDetail.UserDetail').rejectChanges();

				win.close();
			}
		});