Ext.define('AM.view.contract.Edit', {
    extend: 'Ext.window.Window',
    alias : 'widget.contractEdit',

    title : 'Edit Contract',
    layout: 'fit',
    autoShow: true,

    initComponent: function() {
        this.items = [
            {
                xtype: 'form',
                items: [
                    {
                        xtype: 'textfield',
                        name : 'contractType',
                        fieldLabel: 'Contract Type'
                    },
                    {
                        xtype: 'datefield',
                        name : 'eta',
                        fieldLabel: 'ETA'
                    },
                    {
                        xtype: 'textfield',
                        name : 'contractNo',
                        fieldLabel: 'Contract No'
                    }
                ]
            }
        ];

        this.buttons = [
            {
                text: 'Save',
                action: 'save'
            },
            {
                text: 'Cancel',
                scope: this,
                handler: this.close
            }
        ];

        this.callParent(arguments);
    }
});