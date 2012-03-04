Ext.define('AM.model.materialDoc.report.StockQuery', {
			extend : 'Ext.data.Model',
			//合同号	供应商  进仓单号	进仓日期	车号/卡号	批次号	规格(合同)	规格(检验后)	净重	仓库	


			fields : [ {
						name : 'contract_no',
						type : 'string'
					}, {
						name : 'supplier',
						type : 'string'
						
					},{
						name : 'delivery_note',
						type : 'string'
					},{
						name : 'doc_date',
						type : 'date'
					},{
						name : 'plate_num',
						type : 'string'
					},{
						name : 'batch_no',
						type : 'string'
					},{
						name : 'model_contract',
						type : 'string'
					},{
						name : 'model_tested',
						type : 'string'
					},{
						name : 'net_weight',
						type : 'float'
					},{
						name : 'unit_price',
						type : 'float'
					},{
						name : 'warehouse',
						type : 'string'
					}]


		});