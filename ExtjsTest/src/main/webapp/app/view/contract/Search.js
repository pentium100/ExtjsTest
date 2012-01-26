Ext.define('AM.view.contract.Search', {
    extend: 'Ext.window.Window',

    height: 518,
    width: 678,
    layout: {
        type: 'border'
    },
    title: '合同搜索',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            dockedItems: [
                {
                    xtype: 'form',
                    tpl: Ext.create('Ext.XTemplate', 
                        ''
                    ),
                    layout: {
                        type: 'column'
                    },
                    bodyPadding: 10,
                    preventHeader: true,
                    region: 'center',
                    dock: 'top',
                    items: [
                        {
                            xtype: 'combobox',
                            fieldLabel: '合同类型'
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: '合同号'
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: '供应商'
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: '规格'
                        }
                    ],
                    buttons:[
                    	{
                    	   text:'Search',
                    	   action:'search'
                    	
                    	}
                    ]
                }
            ],
            items: [
                {
                    xtype: 'gridpanel',
                    title: '搜索结果',
                    region: 'center',
                    columns: [
                        {
                            xtype: 'gridcolumn',
                            dataIndex: 'string',
                            text: '合同号'
                        },
                        {
                            xtype: 'gridcolumn',
                            dataIndex: 'string',
                            text: '供应商'
                        },
                        {
                            xtype: 'gridcolumn',
                            dataIndex: 'string',
                            text: '付款方式'
                        },
                        {
                            xtype: 'gridcolumn',
                            dataIndex: 'number',
                            text: '规格'
                        },
                        {
                            xtype: 'gridcolumn',
                            dataIndex: 'number',
                            text: '数量'
                        },
                        {
                            xtype: 'gridcolumn',
                            dataIndex: 'date',
                            text: '单价'
                        },
                        {
                            xtype: 'gridcolumn',
                            dataIndex: 'bool',
                            text: '备注'
                        }
                    ],
                    viewConfig: {

                    },
                    dockedItems: [
                        {
                            xtype: 'pagingtoolbar',
                            width: 360,
                            displayInfo: true,
                            dock: 'bottom'
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    }
});