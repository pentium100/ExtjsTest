

Ext.define('AM.view.userDetail.Edit', {
    extend: 'Ext.window.Window',
    alias : 'widget.userDetailEdit',

    title : 'Edit User',
    layout: 'fit',
	height : 414,
	width : 806,

    autoShow: true,
	modal: true,
	layout : {
		type : 'border'
	},
	

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [ 
                {
                    xtype: 'form',
					region: 'north',
                    layout: {
                        type: 'column'
                    },
                    
                    items: [
                        
                        {
                            xtype: 'textfield',
                            fieldLabel: '用户名',
							name: 'userName'
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: '密码',
							name: 'password',
							inputType: 'password'
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: '全名',
							name: 'fullName'
                        },
                        {
                            xtype: 'checkbox',
                            fieldLabel: '激活',
							name: 'enabled',
							inputValue: 'true',
                        },
                        {
                            xtype: 'numberfield',
                            fieldLabel: '级别',
							name: 'userLevel'
							
                        }
                    ]
                },
                {
                    xtype: 'gridpanel',
                    height: 214,
                    width: 831,
                    title: '规格',
					region: 'center',
					
					dockedItems : [{
							xtype : 'toolbar',
							items : [{
										iconCls : 'icon-add',
										text : 'Add',
										scope : this,
										itemId : 'add',
										action : 'add'
									}, {
										iconCls : 'icon-delete',
										text : 'Delete',
										disabled : false,
										itemId : 'delete',
										scope : this,
										action : 'delete'

									}]
					}],
					
					plugins : [Ext.create('Ext.grid.plugin.CellEditing', {
						name: 'cellEditing'
						
					})],

                    columns: [
                        {
                            xtype: 'gridcolumn',
                            dataIndex: 'roleName',
                            text: '角色',
							field : 'textfield'
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

