Ext.define('AM.view.message.MessageEdit', {
    extend: 'Ext.window.Window',
    alias: 'widget.messageEdit',
	//xtype: 'messageEdit',
    height: 496,
    width: 833,
    title: '订单详情',
	modal: true,
	autoShow: true,
	layout : {
		type : 'border'
	},
	

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [ 
                {
                    xtype: 'form',
                    height: 182,
                    width: 821,
					region: 'north',
                    layout: {
                        type: 'column'
                    },
                    title: '信息内容',
                    items: [
                        {
                            xtype: 'combobox',
                            fieldLabel: '类别',
                            store: 'MessageTypesStore',
							name: 'type'
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: '品名',
							name: 'article'
                        },
                        {
                            xtype: 'numberfield',
                            fieldLabel: '数量(MT)',
							name: 'quantity'
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: '装港Laycan',
							name: 'departure'
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: 'ETA',
							name: 'eta'
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: '供应商',
							name:'supplier'
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: '负责业务员',
							name:'owner'
                        },
                        {
                            xtype: 'numberfield',
                            fieldLabel: '建议零售价',
							name:'suggestedPrice'
                        },
                        {
                            xtype: 'numberfield',
                            fieldLabel: '成本价',
							name:'costPrice'
                        },
                        {
                            xtype: 'textareafield',
                            height: 72,
                            width: 764,
                            fieldLabel: '备注状态',
							name: 'remark'
                        }
                    ]
                },
                {
                    xtype: 'gridpanel',
                    height: 214,
                    width: 831,
                    title: '规格',
					region: 'center',
					plugins : [Ext.create('Ext.grid.plugin.CellEditing', {

					})],

                    columns: [
                        {
                            xtype: 'gridcolumn',
                            dataIndex: 'specification',
                            text: '规格'
                        },
                        {
                            xtype: 'numbercolumn',
                            dataIndex: 'typical',
                            text: '典型',
							field:'numberfield'
                        },
                        {
                            xtype: 'numbercolumn',
                            dataIndex: 'reject',
                            text: '拒收',
							field:'numberfield'
                        }
                    ],
                    viewConfig: {

                    }
                }
            ],
			buttons : [{
						text : 'Save',
						action : 'save'
					}, {
						text : 'Cancel',
						scope : this,
						handler : this.close
					}]
				
        });

        me.callParent(arguments);
    }
});