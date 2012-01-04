Ext.define('AM.store.Contracts', {
    extend: 'Ext.data.Store',
    model: 'AM.model.Contract',
    data: [
        {contractType: 'SD',    eta: '2011-12-1', contractNo:'100000-1',
         contractItems:[{model:'xxx1', quantity:120},{model:'xxx2', quantity:240},{model:'xxx3', quantity:360}]
        },
        {contractType: 'SD',    eta: '2011-12-2', contractNo:'100000-2',
         contractItems:[{model:'yyy1', quantity:120}, {model:'yyy2', quantity:240},{model:'yyy3', quantity:360}]
        }
        
    ]
});