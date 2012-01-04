Ext.define('AM.controller.Contracts', {
    extend: 'Ext.app.Controller',
	
	views:['contract.List', 'contract.Edit'],
	stores:['Users', 'Contracts'],
	models:['User', 'Contract'],

    init: function() {
		this.control({
            'viewport > contractList': {
                itemdblclick: this.editContract
            },
            'contractEdit button[action=save]': {
                click: this.updateContract
            }
        });
		
		
		

    
    },
	editContract:function(grid, record){
        console.log('Double clicked on ' + record.get('contractNo'));
		var view = Ext.widget('contractEdit');

        view.down('form').loadRecord(record);
		
	},
	
    updateContract: function(button) {
        var win    = button.up('window'),
        form   = win.down('form'),
        record = form.getRecord(),
        values = form.getValues();

        record.set(values);
        win.close();
	},
	
    editUser: function(grid, record) {
        console.log('Double clicked on ' + record.get('name'));
		var view = Ext.widget('useredit');

        view.down('form').loadRecord(record);
    },		
    
    updateUser: function(button) {
        var win    = button.up('window'),
        form   = win.down('form'),
        record = form.getRecord(),
        values = form.getValues();

        record.set(values);
        win.close();
	},
    onPanelRendered: function() {
        console.log('The panel was rendered');
    }
});