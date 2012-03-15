Ext.define('AM.view.contract.Search', {
	extend : 'Ext.window.Window',

	height : 518,
	width : 678,
	layout : {
		type : 'border'
	},
	modal : true,
	title : '合同搜索',
	alias : 'widget.contractSearch',
	
	renderModel : function(value, metadata, record) {
		var items = record.raw.items;
		var value = "";
		Ext.Array.each(items, function(rec, index) {
					if (value == "") {
						value = rec.model;
					} else {
						value = value + "," + rec.model;
					}
				});

		return value;

	},
	contractTypeReadonly : true,
	contractTypeDefaultValue : 0,

	initComponent : function() {
		var me = this;
        var store = Ext.create('AM.store.Contracts',{autoLoad:false});
		Ext.applyIf(me, {
			        
					dockedItems : [{
								xtype : 'form',
								tpl : Ext.create('Ext.XTemplate', ''),
								layout : {
									type : 'column'
								},
								bodyPadding : 10,
								preventHeader : true,
								
								region : 'center',
								dock : 'top',
								items : [{
											xtype : 'combobox',
											fieldLabel : '合同类型',
											store : 'ContractType',
											value : me.contractTypeDefaultValue,
											readOnly : me.contractTypeReadonly,
											editable: false,
											valueField: 'id',
											displayField: 'text',
											name:'contractType'
										}, {
											xtype : 'textfield',
											fieldLabel : '合同号',
											name:'contractNo'
										}, {
											xtype : 'textfield',
											fieldLabel : '供应商',
											name:'supplier'
										}, {
											xtype : 'textfield',
											fieldLabel : '规格',
											name:'model'
										}],
								buttons : [{
											text : 'Search',
											action : 'search'
										}, {
											text : 'Cancel',
											action : 'cancel',
											scope : this,
											handler : this.close

										}]
							}],
					items : [{
								xtype : 'gridpanel',
								title : '搜索结果',
								region : 'center',
								store : store,
								by : this.by,
								columns : [{
											xtype : 'gridcolumn',
											dataIndex : 'contractType',
											text : '合同类型'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'contractNo',
											text : '合同号'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'supplier',
											text : '供应商'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'payTerm',
											text : '付款方式'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'model',
											scope : this,
											renderer : this.renderModel,
											text : '规格'
										}, {
											xtype : 'gridcolumn',
											dataIndex : 'remark',
											text : '备注'
										}],
								viewConfig : {

					}			,
								dockedItems : [{
											xtype : 'pagingtoolbar',
											
											displayInfo : true,
											dock : 'bottom',
											store : store
										}]

							}]
				});

		me.callParent(arguments);
	}
});