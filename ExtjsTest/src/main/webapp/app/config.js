Ext.Loader.setConfig({
			enabled : true,
			disableCaching : true
		});
Ext.Loader.setPath('Ext.ux', 'js/extjs4/examples/ux');
Ext.Loader.setPath('Ext', 'js/extjs4/src');
Ext.require(['Ext.form.field.Trigger', 'Ext.ux.grid.FiltersFeature']);
// Ext.require([
// 'Ext.data.*',
// 'Ext.tip.QuickTipManager',
// 'Ext.form.*',
// 'Ext.ux.data.PagingMemoryProxy',
// 'Ext.grid.Panel',
// 'Ext.view.View',
// ]);
Ext.require(['Ext.form.field.Number'], function() {

			Ext.form.field.Number.override({

						hideTrigger : true

					});
		});

Ext.require(['Ext.data.association.HasOne'], function() {

	Ext.data.association.HasOne.override({
		createSetter : function() {
			var me = this, ownerModel = me.ownerModel, associatedModel = me.associatedModel, associatedName = me.associatedName, primaryKey = me.primaryKey, instanceName = me.instanceName, foreignKey = me.foreignKey;

			// 'this' refers to the Model instance inside this function
			return function(value, options, scope) {
				if (value && value.isModel) {
					model = this;
					model[instanceName].set(value.data);

					value = value.getId();
					// associatedModel.loadData(value.data);
				}

				this.set(foreignKey, value);

				if (Ext.isFunction(options)) {
					options = {
						callback : options,
						scope : scope || this
					};
				}

				if (Ext.isObject(options)) {
					return this.save(options);
				}
			};

		},
		createGetter : function() {
			var me = this, ownerModel = me.ownerModel, associatedName = me.associatedName, associatedModel = me.associatedModel, foreignKey = me.foreignKey, primaryKey = me.primaryKey, instanceName = me.instanceName;

			// 'this' refers to the Model instance
			// inside this function
			return function(options, scope) {
				options = options || {};

				var model = this, foreignKeyId = model.get(foreignKey), success, instance, args;

				if (options.reload === true
						|| model[instanceName] === undefined) {
					instance = Ext.ModelManager.create({}, associatedName);
					instance.set(primaryKey, foreignKeyId);

					if (typeof options == 'function') {
						options = {
							callback : options,
							scope : scope || model
						};
					}

					// Overwrite the success handler so
					// we can assign the current
					// instance
					success = options.success;
					options.success = function(rec) {
						model[instanceName] = rec;
						if (success) {
							success.call(this, arguments);
						}
					};

					// associatedModel.load(foreignKeyId,
					// options);
					// assign temporarily while we wait
					// for data to return
					model[instanceName] = instance;
					return instance;
				} else {
					instance = model[instanceName];
					args = [instance];
					scope = scope || model;

					// TODO: We're duplicating the
					// callback invokation code that the
					// instance.load() call above
					// makes here - ought to be able to
					// normalize this - perhaps by
					// caching at the Model.load layer
					// instead of the association layer.
					Ext.callback(options, scope, args);
					Ext.callback(options.success, scope, args);
					Ext.callback(options.failure, scope, args);
					Ext.callback(options.callback, scope, args);

					return instance;
				}
			};

		}

	});

});

Ext.require(['Ext.data.BelongsToAssociation'], function() {

	Ext.data.BelongsToAssociation.override({
		createGetter : function() {
			var me = this, ownerModel = me.ownerModel, associatedName = me.associatedName, associatedModel = me.associatedModel, foreignKey = me.foreignKey, primaryKey = me.primaryKey, instanceName = me.instanceName;

			// 'this' refers to the Model instance
			// inside this function
			return function(options, scope) {
				options = options || {};

				var model = this, foreignKeyId = model.get(foreignKey), instance, args;

				if (model[instanceName] === undefined) {
					instance = Ext.ModelManager.create({}, associatedName);
					instance.set(primaryKey, foreignKeyId);

					if (typeof options == 'function') {
						options = {
							callback : options,
							scope : scope || model
						};
					}

					// var readOp = new
					// Ext.data.Operation({
					// action : 'read',
					// id : foreignKeyId
					// });
					// var proxy =
					// associatedModel.getProxy();
					// proxy.read(readOp);
					// instance =
					// readOp.getRecords()[0];
					associatedModel.load(foreignKeyId, options);

					// model[instanceName] =
					// associatedModel;
					model[instanceName] = instance;
					return associatedModel;
				} else {
					instance = model[instanceName];
					args = [instance];
					scope = scope || model;

					// TODO: We're duplicating the
					// callback invokation code that
					// the instance.load() call above
					// makes here - ought to be able to
					// normalize this - perhaps
					// by caching at the Model.load
					// layer
					// instead of the association layer.
					Ext.callback(options, scope, args);
					Ext.callback(options.success, scope, args);
					Ext.callback(options.failure, scope, args);
					Ext.callback(options.callback, scope, args);

					return instance;
				}
			};
		}

	});
});
Ext.require(['Ext.data.writer.Json', 'Ext.data.Store', 'Ext.data.TreeStore',
				'Ext.window.MessageBox', 'Ext.ux.grid.menu.ListMenu'],
		function() {
			//
			Ext.data.writer.Json.override({
				/*
				 * This function overrides the default implementation of json
				 * writer. Any hasMany relationships will be submitted as nested
				 * objects. When preparing the data, only children which have
				 * been newly created, modified or marked for deletion will be
				 * added. To do this, a depth first bottom -> up recursive
				 * technique was used.
				 */
				getRecordData : function(record) {
					// Setup variables
					var me = this, i, association, childStore, data = {};
					if (record.proxy.writer.writeAllFields) {
						data = record.data;
					} else {
						var changes, name, field, fields = record.fields, nameProperty = this.nameProperty, key;
						changes = record.getChanges();
						for (key in changes) {
							if (changes.hasOwnProperty(key)) {
								field = fields.get(key);
								name = field[nameProperty] || field.name;
								data[name] = changes[key];
							}
						}
						if (!record.phantom) {
							// always include the id for non
							// phantoms
							data[record.idProperty] = record.getId();
						}
					}

					// Iterate over all the hasMany associations
					for (i = 0; i < record.associations.length; i++) {

						association = record.associations.get(i);

						if (association.type == "hasOne") {
							if (record[association.instanceName] != undefined) {
								data[association.associationKey] = record[association.instanceName].data;
							}

						}

						if (association.type == "hasMany") {
							data[association.name] = [];
							childStore = record[association.storeName];

							// Iterate over all the children in
							// the current
							// association
							childStore.each(function(childRecord) {

										// Recursively
										// get the
										// record data
										// for
										// children
										// (depth
										// first)
										childRecord.setDirty(true);
										var childData = this.getRecordData
												.call(this, childRecord);

										/*
										 * If the child was marked dirty or
										 * phantom it must be added. If there
										 * was data returned that was neither
										 * dirty or phantom, this means that the
										 * depth first recursion has detected
										 * that it has a child which is either
										 * dirty or phantom. For this child to
										 * be put into the prepared data, it's
										 * parents must be in place whether they
										 * were modified or not.
										 */
										if (childRecord.dirty
												| childRecord.phantom
												| (childData != null)) {
											data[association.name]
													.push(childData);
											record.setDirty();
										}
									}, me);

							/*
							 * Iterate over all the removed records and add them
							 * to the preparedData. Set a flag on them to show
							 * that they are to be deleted
							 */
							// Ext.each(childStore.removed,
							// function(
							// removedChildRecord) {
							// Set a flag here to identify
							// removed
							// records
							// removedChildRecord.set('forDeletion',
							// true);
							// var removedChildData =
							// this.getRecordData
							// .call(this, removedChildRecord);
							// data[association.name]
							// .push(removedChildData);
							// record.setDirty();
							// }, me);
						}

						// record.getDocType(function(docType,
						// operation) {
						// // do something with the category
						// object
						// alert(docType.get('id')); // alerts
						// 20
						// }, this);
						// if(association.type=='belongsTo'){
						// var childData =
						// data[association.name];
						// data[association.name].push();
						// }
					}

					// Only return data if it was dirty, new or
					// marked for
					// deletion.
					if (record.dirty | record.phantom
							| record.get('forDeletion')) {
						return data;
					}
				}
			});

			Ext.data.Store.override({
				remove : function(records, /* private */isMove) {
					if (!Ext.isArray(records)) {
						records = [records];
					}

					/*
					 * Pass the isMove parameter if we know we're going to be
					 * re-inserting this record
					 */
					isMove = isMove === true;
					var me = this, sync = false, i = 0, length = records.length, isPhantom, index, record;

					for (; i < length; i++) {
						var record = records[i];
						index = me.data.indexOf(record);

						if (me.snapshot) {
							me.snapshot.remove(record);
						}

						if (index > -1) {
							isPhantom = record.phantom === true;
							if (!isMove && !isPhantom) {
								// don't push phantom records
								// onto removed
								record.set('forDeletion', true);
								me.removed.push(record);
							}

							record.unjoin(me);
							me.data.remove(record);
							sync = sync || !isPhantom;

							me.fireEvent('remove', me, record, index);
						}
					}

					me.fireEvent('datachanged', me);
					if (!isMove && me.autoSync && sync) {
						me.sync();
					}
				}
			});

			Ext.data.Store.override({
				filterUpdated : function(item) {
					// only want dirty records, not phantoms
					// that are valid
					var itemUpdated = false, i;
					var masterUpdated = item.dirty === true
							&& item.phantom !== true && item.isValid();
					if (!masterUpdated && !item.phantom) {

						for (i = 0; i < item.associations.length; i++) {
							var association = item.associations.get(i);
							// data[association.name] = [];
							if (association.type == "hasOne") {
								if (!itemUpdated) {
									if (item[association.instanceName] != undefined) {
										itemUpdated = item[association.instanceName].dirty;
									}
								}
								// if(itemUpdated){
								// break;
								// }
							}
							if (association.type == "hasMany") {
								var childStore = item[association.storeName];

								// Iterate over all the children
								// in the current
								// association
								var toCreate = childStore.getNewRecords(), toUpdate = childStore
										.getUpdatedRecords(), toDestroy = childStore
										.getRemovedRecords();
								if (toCreate.length > 0 || toUpdate.length > 0
										|| toDestroy.length > 0) {

									itemUpdated = true;

									// break;
								}
							}

						}
					}

					return masterUpdated || itemUpdated;
				}

			});

			Ext.data.TreeStore.override({
						onNodeRemove : function(parent, node) {
							var removed = this.removed;

							if (!node.isReplace
									&& Ext.Array.indexOf(removed, node) == -1) {
								node.set('forDeletion', true);
								removed.push(node);
							}
						}
					});

			Ext.data.Store.override({
						rejectChanges22 : function() {
							var me = this;

							// re-add removed records
							Ext.each(me.removed, function(rec) {
										rec.join(me);
										me.data.add(rec);
									});
							me.removed = [];

							// revert dirty records and trash newly added
							// records
							// ('phantoms')
							me.each(function(rec) {
										if (rec == undefined) {
											return;
										}
										if (rec.dirty) {
											rec.reject();
										}
										if (rec.phantom) {
											// record.unjoin(me); // probably
											// not
											rec.unjoin(me);
											// really
											// necessary
											me.data.remove(rec);
										}
									});

							me.fireEvent('datachanged', me);
						}
					});

			Ext.ux.grid.menu.ListMenu.override({
						show : function() {
							var lastArgs = null;
							return function() {
								lastArgs = arguments;
								if (this.loadOnShow && !this.loaded) {
									this.store.load();
								}
								this.callParent(arguments);

							}
						}()
					});

			Ext.define('Ext.data.ux.Store', {

				extend : 'Ext.data.Store',

				constructor : function(config) {
					Ext.data.ux.Store.superclass.constructor.call(this, config)
					this.addListener('write', function(store, operation) {

								var record = operation.getRecords()[0];
								var name = Ext.String
										.capitalize(operation.action);
								var verb;

								if (name == 'Destroy') {
									record = operation.records[0];
									verb = 'Destroyed';
								} else {
									verb = name + 'd';
								}
								Ext.example.msg(name, Ext.String.format(
												"{0} successful: {1}", verb,
												record.getId()));

							}, this

					);

					var proxy = this.getProxy();
					proxy.addListener('exception', function(proxy, res) {

						var response;
						if (res.responseText) {
							response = Ext.decode(res.responseText, true);
						}

						var errmsg;
						if (response && response.message) {

							errmsg = response.message;

						}

						if (res.status == 500) {

							errmsg = res.statusText;

						}

						if (res.status == 403) {

							errmsg = '您没有权限进行些操作!';

						}

						if (errmsg == "") {

							errmsg = '系统错误，请与管理员联系。';
						}

						var icon = Ext.MessageBox.ERROR;
						var buttons = Ext.MessageBox.OK;
						if (res.status == 300) {
							icon = Ext.MessageBox.WARNING;
							buttons = Ext.MessageBox.YESNO;

						}

						Ext.MessageBox.show({
							title : '错误信息',
							msg : errmsg,
							buttons : buttons,
							icon : icon,
							closable : false,
							modal : true,
							fn : function(btn) {
								if (btn == "yes") {
									var proxy = this.getProxy();
									Ext.apply(proxy.extraParams, {
												focusUpdate : true
											});
									this.sync();
									Ext.apply(proxy.extraParams, {
												focusUpdate : false
											});
								}

								if (btn = "ok") {
									if (this.removed != undefined) {

										Ext.each(this.removed, function(rec) {
											rec.join(this);
											this.data.add(rec);
											rec.reject();
											if (Ext.isDefined(this.snapshot)) {
												this.snapshot.add(rec);
											}
											for (i = 0; i < rec.associations.length; i++) {
												var association = rec.associations
														.get(i);
												if (association.type == "hasMany") {
													var childStore = rec[association.storeName];
													childStore.rejectChanges();
												}
											}

										}, this);

										this.removed = [];
										this.fireEvent('datachanged', this);

									}

								}
							},
							scope : this
						});

							// this.rejectChanges();
					}, this);

					// Copy configured listeners into
					// *this* object so
					// that the base class's
					// constructor will add them.
					// this.listeners =
					// config.listeners;
					// Call our superclass constructor
					// to complete
					// construction process.
				}

			});
			// Ext.data.Store.override({
			// listeners:{
			// write: function(store, operation){
			// var record = operation.getRecords()[0];
			// var name = Ext.String.capitalize(operation.action);
			// var verb;
			// if (name == 'Destroy') {
			// record = operation.records[0];
			// verb = 'Destroyed';
			// } else {
			// verb = name + 'd';
			// }
			// Ext.example.msg(name, Ext.String.format("{0} Message:
			// {1}", verb,
			// record.getId()));
			// }
			// }
			// });
		});

Ext.JSON.encodeDate = function(d) {
	return Ext.Date.format(d, '"Y-m-d H:i:s"');
};

Ext.require(['Ext.data.Model'], function() {

	Ext.data.Model.override({
		copyFrom : function(sourceRecord) {

			if (sourceRecord) {

				var me = this, fields = me.fields.items, fieldCount = fields.length, field, i = 0, myData = me[me.persistenceProperty], sourceData = sourceRecord[sourceRecord.persistenceProperty], value;

				for (; i < fieldCount; i++) {
					field = fields[i];

					// Do not use setters.
					// Copy returned values in directly from the data object.
					// Converters have already been called because new Records
					// have been created to copy from.
					// This is a direct record-to-record value copy operation.
					value = sourceData[field.name];
					if (value !== undefined) {
						myData[field.name] = value;
					}
				}

				// If this is a phantom record being updated from a concrete
				// record, copy the ID in.
				if (me.phantom && !sourceRecord.phantom) {
					me.setId(sourceRecord.getId());
				}

				// copy hasmany association property from source.
				for (i = 0; i < sourceRecord.associations.length; i++) {

					association = sourceRecord.associations.get(i);

					// if (association.type == "hasOne") {
					// me[association.associationKey] =
					// sourceRecord[association.instanceName].data;

					// }

					if (association.type == "hasMany") {

						var clientStore = me[association.storeName];
						var sourceStore = sourceRecord[association.storeName];

						clientStore.loadData(sourceStore.getRange());

					}
				}

			}

		}
	});

});

Ext.require(['Ext.ux.grid.FiltersFeature'], function() {

			Ext.ux.grid.FiltersFeature.override({
						getFilterData : function() {

							var filters = [], i, len;

							this.filters.each(function(f) {
										if (f.active) {
											var d = [].concat(f.serialize());
											for (i = 0, len = d.length; i < len; i++) {

												var filterField = f.dataIndex;
												if (f.filterField) {
													filterField = f.filterField;

												}
												filters.push({
															field : filterField,
															data : d[i]
														});
											}
										}
									});
							return filters;

						}
					})
		});

Ext.require(['Ext.grid.column.Column'], function() {
			Ext.grid.column.Column.override({

						getSortParam : function() {
							if (this.sortFieldName) {
								return this.sortFieldName;
							} else {
								return this.dataIndex;
							}
						}
					})
		});