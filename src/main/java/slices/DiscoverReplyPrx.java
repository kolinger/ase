// **********************************************************************
//
// Copyright (c) 2003-2013 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.5.0
//
// <auto-generated>
//
// Generated from file `Slices.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package slices;

public interface DiscoverReplyPrx extends Ice.ObjectPrx
{
    public void reply(Ice.ObjectPrx obj);

    public void reply(Ice.ObjectPrx obj, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_reply(Ice.ObjectPrx obj);

    public Ice.AsyncResult begin_reply(Ice.ObjectPrx obj, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_reply(Ice.ObjectPrx obj, Ice.Callback __cb);

    public Ice.AsyncResult begin_reply(Ice.ObjectPrx obj, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_reply(Ice.ObjectPrx obj, Callback_DiscoverReply_reply __cb);

    public Ice.AsyncResult begin_reply(Ice.ObjectPrx obj, java.util.Map<String, String> __ctx, Callback_DiscoverReply_reply __cb);

    public void end_reply(Ice.AsyncResult __result);
}
