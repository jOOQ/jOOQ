/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.jooq.debug.impl;

import org.jooq.ExecuteContext;
import org.jooq.impl.DefaultExecuteListener;

/**
 * @author Christopher Deckers
 */
public class DebugListener extends DefaultExecuteListener {

    @Override
    public void start(ExecuteContext ctx) {
        DebugProcessor debugProcessor = new DebugProcessor();
        ctx.setData("org.jooq.debug.DebugProcessor", debugProcessor);
//        debugProcessor.start(ctx);
    }

    @Override
    public void end(ExecuteContext ctx) {
//        DebugProcessor debugProcessor = getDebugProcessor(ctx);
        ctx.setData("org.jooq.debug.DebugProcessor", null);
//        debugProcessor.end(ctx);
    }

    private DebugProcessor getDebugProcessor(ExecuteContext ctx) {
        return (DebugProcessor)ctx.getData("org.jooq.debug.DebugProcessor");
    }

    @Override
    public void renderStart(ExecuteContext ctx) {
        getDebugProcessor(ctx).renderStart(ctx);
    }

    @Override
    public void prepareStart(ExecuteContext ctx) {
        getDebugProcessor(ctx).prepareStart(ctx);
    }

    @Override
    public void prepareEnd(ExecuteContext ctx) {
        getDebugProcessor(ctx).prepareEnd(ctx);
    }

    @Override
    public void bindStart(ExecuteContext ctx) {
        getDebugProcessor(ctx).bindStart(ctx);
    }

    @Override
    public void bindEnd(ExecuteContext ctx) {
        getDebugProcessor(ctx).bindEnd(ctx);
    }

    @Override
    public void executeStart(ExecuteContext ctx) {
        getDebugProcessor(ctx).executeStart(ctx);
    }

    @Override
    public void executeEnd(ExecuteContext ctx) {
        getDebugProcessor(ctx).executeEnd(ctx);
    }

}
