package org.jooq.xtend

class GenerateAll {
    def static void main(String[] args) {
    	Conversions::main(args);
    	Factory::main(args);
    	Records::main(args);
    	Rows::main(args);
    	Update::main(args);
    }
}