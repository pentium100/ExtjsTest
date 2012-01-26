Ext.define('AM.controller.MaterialDocs', {
			extend : 'Ext.app.Controller',

			views : ['materialDoc.List','materialDoc.Edit'],
			stores : ['MaterialDocs'],
			models : ['MaterialDoc', 'MaterialDocItem'],

			init : function() {
				this.control({
				
					'materialDocList':{
						itemdblclick : this.editMaterialDoc
					},
					'materialDocEdit trigger[name=contractNo]':{
						keydown : this.disableChange, 
						onTriggerClick : this.searchContract
					}
					
				});
				
				

			},
			editMaterialDoc:function(grid, record){
				var view = Ext.widget('materialDocEdit');

				view.down('form').loadRecord(record);
				view.down('form').setTitle('凭证号:'+record.get('docNo'));

				view.down('grid').reconfigure(record.items());

			},

			disableChange:function(field, e, eOpts ){
				e.stopEvent();
			},
			
			searchContract:function(){
				
			}
			
		});