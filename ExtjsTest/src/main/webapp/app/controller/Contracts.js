Ext.define('AM.controller.Contracts', {
    extend: 'Ext.app.Controller',
	
	views:['contract.List', 'contract.Edit'],
	stores:['Contracts'],
	models:['Contract', 'ContractItem'],

    init: function() {
		this.control({
            'viewport > contractList': {
                itemdblclick: this.editContract
            },
            'contractEdit button[action=save]': {
                click: this.updateContract
            },
            'contractList button[text=Add]':{
            	click: this.addContract
            }
            
        });
		
		
		

    
    },
	editContract:function(grid, record){
        console.log('Double clicked on ' + record.get('contractNo'));
		var view = Ext.widget('contractEdit');

        view.down('form').loadRecord(record);
        
        
        view.down('grid').reconfigure(record.items());
        
		
	},
	addContract:function(button){
		record = new AM.model.Contract();
		this.getStore('Contracts').insert(0,record);
		var view = Ext.widget('contractEdit');
        view.down('form').loadRecord(record);
        view.down('grid').reconfigure(record.items());
		
		
	},
	
	
    updateContract: function(button) {
        var win    = button.up('window');
        form   = win.down('form');
        record = form.getRecord();
        values = form.getValues();
        values.lastShippingDate = Ext.Date.parse(values.lastShippingDate, 'Y-m-d');
        
        

        record.set(values);
        
        
        // record.data.items = win.down('grid').getStore();
        this.getStore('Contracts').save();
        win.close();
	},
	
    onPanelRendered: function() {
        console.log('The panel was rendered');
    }
});