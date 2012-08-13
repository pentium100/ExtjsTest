Ext.define('AM.model.contract.report.ContractHistory', {
			extend : 'Ext.data.Model',
			//idProperty: 'id', 合同类型	合同号	供应商	付款方式	备注	规格	数量	单价	备注

			fields : [ {
						name : 'contract_type',
						type : 'string'
						
					},{
						name : 'contract_no',
						type : 'string'
					}, {
						name : 'supplier',
						type : 'string'
						
					},{
						name : 'sign_date',
						type : 'date',
						dateFormat : 'Y-m-d H:i:s'
					}, {
						name : 'pay_term',
						type : 'string'
					},{
						name : 'remark',
						type : 'string'
					},{
						name : 'model',
						type : 'string'
					},{
						name : 'quantity',
						type : 'float'
					},{
						name : 'unit_price',
						type : 'float'
					},{
						name : 'item_remark',
						type : 'string'
					},{
						name : 'name',
						type : 'string'
					}]


		});