Ext.define('AM.view.contract.List' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.contractList',

    title : 'All Contract',
    store : 'Contracts',

    initComponent: function() {
        this.columns = [
            {header: 'Contract Type',  dataIndex: 'contractType',  flex: 1},
            {header: 'Last Shipping Date', dataIndex: 'lastShippingDate', xtype:'datecolumn', flex: 1, format:'Y-m-d'},
            {header: 'Contract No', dataIndex: 'contractNo', flex: 1}
        ];

        this.callParent(arguments);
    }
});