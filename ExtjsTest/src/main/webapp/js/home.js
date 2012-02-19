xxx(options, scope)
{
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
		associatedModel.load(foreignKeyId, options);
		model[instanceName] = associatedModel;
		return associatedModel;
	} else {
		instance = model[instanceName];
		args = [instance];
		scope = scope || model; // TODO:
	}
}
// We're
// duplicating
// the
// callback
// invokation
// code
// that
// the
// instance.load()
// call
// above
// //makes
// here
// -
// ought
// to
// be
// able
// to
// normalize
// this
// -
// perhaps
// by
// caching
// at
// the
// Model.load
// layer
// //instead
// of
// the
// association
// layer.
// Ext.callback(options,
// scope,
// args);
// Ext.callback(options.success,
// scope,
// args);
// Ext.callback(options.failure,
// scope,
// args);
// Ext.callback(options.callback,
// scope,
// args);
// return
// instance;
// } }
