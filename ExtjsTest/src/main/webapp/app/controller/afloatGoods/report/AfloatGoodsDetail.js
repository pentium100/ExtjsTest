Ext.define('AM.controller.afloatGoods.report.AfloatGoodsDetail', {
			extend : 'Ext.app.Controller',

			views  : ['afloatGoods.report.AfloatGoodsDetail'],
			stores : ['afloatGoods.report.AfloatGoodsDetail'],
			models : ['afloatGoods.report.AfloatGoodsDetail'],

			init : function(options) {

				Ext.apply(this, options);

				this.control({

							'AfloatGoodsDetail button[action=submit]' : {
								click : this.submitReport
							},

							'AfloatGoodsDetail button[action=exportToExcel]' : {
								click : this.exportToExcel
							}
						});

			},

			exportToExcel : function(button) {
				var panel = button.up('AfloatGoodsDetail');
				var grid = panel.down('gridpanel');
				var store = grid.getStore();

				
				
				var record = panel.down('form').getValues();
				////合同类型	合同号	供应商	付款方式	备注	规格	数量	单价	备注
				var tmp = [];
				tmp = this.getFilterParam(record);
				var param;
				param = 'filter='+encodeURI(Ext.JSON.encode(tmp))+'&excel=true&start=0&limit=10000000';				
				
				window.open('reports/afloatGoodsDetails?'+param);

				
				

			},

			submitReport : function(button) {

				var panel = button.up('AfloatGoodsDetail');
				var grid = panel.down('gridpanel');
				var store = grid.getStore();

				var tmp = [];
				var record = button.up('form').getValues(false, false,true);
				tmp = this.getFilterParam(record);
				
				
				
				var p = store.getProxy();
				p.extraParams.filter = Ext.JSON.encode(tmp);
				store.load();
			},
			
			
			getFilterParam:function(record){
				var filter = {};
				var tmp = [];
				if (record.contract_no != "") {
					filter.type = "string";
					filter.field = "contract.contract_no";
					filter.value = record.contract_no;
					tmp.push(Ext.apply({}, filter));
				}
				if (record.supplier != "") {
					filter.type = "string";
					filter.field = "contract.supplier";
					filter.value = record.supplier;
					tmp.push(Ext.apply({}, filter));
				}
				if (record.model != "") {
					filter.type = "string";
					filter.field = "afloat_goods_item.model";
					filter.value = record.model_contract;
					tmp.push(Ext.apply({}, filter));
				}
				
				if (record.original != "" && record.original != null) {
					filter.type = "int";
					filter.field = "afloat_goods.original";
					filter.comparison = 'eq';
					filter.value = record.original;
					tmp.push(Ext.apply({}, filter));
				}

				if (record.dispatchDateFrom != "") {
					filter.type = "date";
					filter.field = "afloat_goods.dispatch_date";
					filter.comparison = 'ge';
					filter.value = record.dispatchDateFrom;
					tmp.push(Ext.apply({}, filter));
				}
				
				if (record.dispatchDateTo != "") {
					filter.type = "date";
					filter.field = "afloat_goods.dispatch_date";
					filter.comparison = 'le';
					filter.value = record.dispatchDateTo;
					tmp.push(Ext.apply({}, filter));
				}
				
			
				
				if (record.plate_num != "") {
					filter.type = "string";
					filter.field = "afloat_goods.plate_num";
					filter.value = record.plate_num;
					tmp.push(Ext.apply({}, filter));
				}
				if (record.batch_no != "") {
					filter.type = "string";
					filter.field = "afloat_goods_item.batch_no";
					filter.value = record.batch_no;
					tmp.push(Ext.apply({}, filter));
				}
				if (record.dispatch != "") {
					filter.type = "string";
					filter.field = "afloat_goods.dispatch";
					filter.value = record.dispatch;
					tmp.push(Ext.apply({}, filter));
				}
				

				if (record.destination != "") {
					filter.type = "string";
					filter.field = "afloat_goods.destination";
					filter.value = record.destination;
					tmp.push(Ext.apply({}, filter));
				}

				if (record.transportDateFrom != "") {
					filter.type = "date";
					filter.field = "afloat_goods.transport_date";
					filter.comparison = 'ge';					
					filter.value = record.transportDateFrom;
					tmp.push(Ext.apply({}, filter));
				}
				
				if (record.transportDateTo != "") {
					filter.type = "date";
					filter.field = "afloat_goods.transport_date";
					filter.comparison = 'le';					
					filter.value = record.transportDateTo;
					tmp.push(Ext.apply({}, filter));
				}


				if (record.etaFrom != "") {
					filter.type = "date";
					filter.field = "afloat_goods.eta";
					filter.comparison = 'ge';					
					filter.value = record.etaFrom;
					tmp.push(Ext.apply({}, filter));
				}
				
				if (record.etaTo != "") {
					filter.type = "date";
					filter.field = "afloat_goods.eta";
					filter.comparison = 'le';					
					filter.value = record.etaTo;
					tmp.push(Ext.apply({}, filter));
				}
				
				if (record.received != "" && record.received != null) {
					filter.type = "date";
					filter.field = "afloat_goods.arrive_date";
					
					filter.comparison = 'is';
					
					filter.value = record.received;
					tmp.push(Ext.apply({}, filter));
				}
				
				
				return tmp;
                
				
				
			}
		});