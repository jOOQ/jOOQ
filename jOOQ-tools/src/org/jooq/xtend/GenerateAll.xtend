package org.jooq.xtend

class GenerateAll {
    def static void main(String[] args) {
    	BetweenAndSteps::main(args);
    	Conversions::main(args);
    	ContextDSL::main(args);
    	Factory::main(args);
    	InsertDSL::main(args);
    	MergeDSL::main(args);
    	Records::main(args);
    	Rows::main(args);
    	UpdateDSL::main(args);
    }
}