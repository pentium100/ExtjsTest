Ext.define('AM.controller.Menus', {
			extend : 'Ext.app.Controller',
	        
			uses : [
				'AM.portal.classes.Portlet',
				'AM.portal.classes.PortalColumn',
				'AM.portal.classes.PortalPanel',
				
                'AM.portal.classes.PortalDropZone'
			],		

			refs : [{
						ref : 'menu',
						selector : '#menu-panel'
					}, {
						ref : 'tabpanel',
						selector : '#content-panel'
					}],
			init : function() {
				this.control({
							'#menu-panel' : {
								itemclick : this.itemclick
							}
						});

			},

			itemclick : function(view, record, item, index, e, eOpts) {
				var tabs = this.getTabpanel();

				if (!record.get("leaf")) {
					return;
				}

				var tab = tabs
						.down("#" + record.raw.controller + record.raw.id);
				if (tab == undefined) {

					// var initNeeded = true;
					if (!this.application.controllers.get(record.raw.controller)) {
						var c = this.getController(record.raw.controller);
						var controllerParam;
						if (record.raw.controllerParam != "") {
							controllerParam = Ext
									.decode(record.raw.controllerParam)
						}

						c.init(controllerParam);

						// initNeeded = false;
					}

					// var c =
					// Ext.create("AM.controller."+record.raw.controller)

					// if(initNeeded){
					// c.init(controllerParam);
					// }


					tab = tabs.add({
								// we use the tabs.items property to get the
								// length of current
								// items/tabs
								title : record.get("text"),
								//layout : 'fit',
								closable : true,
								autoDestroy : true,
								iconCls : record.get("iconCls"),

								//autoScroll : true,
								bodyPadding : 0,
								xtype : record.raw.views,
								

								id : record.raw.controller + record.raw.id

							});
							
					tab.show();		

				}
				tabs.setActiveTab(tab);
			}

		});