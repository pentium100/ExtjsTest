Ext.define('AM.model.materialDoc.report.StockQuery', {
			extend : 'Ext.data.Model',
			//合同号	供应商  进仓单号	进仓日期	车号/卡号	批次号	规格(合同)	规格(检验后)	净重	仓库
			
			idProperty: 'report_key',


			fields : [{
						name : 'report_key',
						type : 'string'
					}, 	{
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
						name : 'gross_weight',
						type : 'float'
					},{
						name : 'unit_price',
						type : 'float'
					},{
						name : 'warehouse',
						type : 'string'
					},{
						name : 'inspection_date',
						type : 'date'
					},{
						name : 'authority',
						type : 'string'
					},{
						name : 'si',
						type : 'float'
					},{
						name : 'fe',
						type : 'float'
					},{
						name : 'al',
						type : 'float'
					},{
						name : 'ca',
						type : 'float'
					},{
						name : 'p',
						type : 'float'
					},{
						name : 'inspection_remark',
						type : 'string'
					},{
						name : 'doc_no',
						type : 'string'
					},{
						name : 'original',
						type : 'boolean'
					}]


		});