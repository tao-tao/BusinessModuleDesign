package com.tansun.data.db.validator;

import com.tansun.data.db.visual.model.RootModel;

public abstract class Validator {
	public abstract void execute(RootModel model,DiagramErrors errors);
	
}
