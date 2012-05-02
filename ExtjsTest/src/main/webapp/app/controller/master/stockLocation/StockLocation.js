Ext.define('AM.controller.master.stockLocation.StockLocation', {
			extend : 'Ext.app.Controller',

			views : ['master.stockLocation.StockLocationList'],
			stores : ['master.stockLocation.StockLocations'],
			models : ['master.stockLocation.StockLocation'],

			init : function() {
				this.control({
							
							'stockLocationList button[text=Add]' : {
								click : this.addStockLocation
							},
							'stockLocationList button[text=Delete]' : {
								click : this.deleteStockLocation
							},
							'stockLocationList button[action=save]' : {
								click : this.saveStockLocation
							}


						});

			},
			deleteStockLocation : function(button) {
				var viewport = button.up('viewport');
				var grid = viewport.down('stockLocationList');
				var selection = grid.getView().getSelectionModel()
						.getSelection()[0];
				if (selection) {
					this.getStore('master.stockLocation.StockLocations').remove(selection);
					this.getStore('master.stockLocation.StockLocations').sync();
				}
			},

			addStockLocation : function(button) {
				var record = new AM.model.master.stockLocation.StockLocation();
				this.getStore('master.stockLocation.StockLocations').insert(0, record);
			},
			
			saveStockLocation : function(button) {
				
				this.getStore('master.stockLocation.StockLocations').sync();
			}			
		});