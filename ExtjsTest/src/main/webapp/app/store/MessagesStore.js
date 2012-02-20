

Ext.define('AM.store.MessagesStore', {
			extend : 'Ext.data.ux.Store',
			model : 'AM.model.Message',

			autoLoad : true,
			proxy : {
				type : 'rest',
				url : 'messages',
				reader : {
					type : 'json',
					root : 'messages',
					successProperty : 'success'
				},

				writer : {
					type : 'json',
					writeAllFields : true,
					root : ''
				}

				
			}
			//,listeners: {
			//	write2: function(store, operation){
			//		var record = operation.getRecords()[0],
			//			name = Ext.String.capitalize(operation.action),
			//			verb;
                    
                    
			//		if (name == 'Destroy') {
			//			record = operation.records[0];
			//			verb = 'Destroyed';
			//		} else {
			//			verb = name + 'd';
			//		}
			//		Ext.example.msg(name, Ext.String.format("{0} Message: {1}", verb, record.getId()));
                
			//	}
			//}	
			
			
		});
