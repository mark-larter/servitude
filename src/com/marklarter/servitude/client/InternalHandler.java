package com.bi.services;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * A base handler class for static handler classes used in Activity or Service for
 * messaging. Base class stores weak reference to its containing instance, and will
 * only call {@link #handleMessage(Message, T)} on derived class instance if the weak 
 * reference can still be resolved, i.e. the containing instance still exists.
 * <p>
 * Example:
 * <pre>
 * private static class SomeHandler extends InternalHandler<SomeActivity> {
 * 
 *     public SomeHandler(SomeActivity parent) {
 *         super(parent);
 *     }
 *   
 *     @Override
 *     public handleMessage(Message message, SomeActivity parent) {
 *     // Handle message ...
 *     }
 *   
 * }
 * </pre>
 */
public abstract class InternalHandler<T>
	extends
		Handler
{

	public InternalHandler(T parent, Looper looper) {
		super(looper);
		weakParent = new WeakReference<T>(parent);
	}

	public InternalHandler(T parent) {
		super();
		weakParent = new WeakReference<T>(parent);
	}
	
	@Override
	public final void handleMessage(Message msg) {
		final T parent = weakParent.get();
		if (parent != null) {
	    	handleMessage(msg, parent);
	    }
	}
	
	/**
	 * Handles message in derived instance.
	 * 
	 * @param message The message to handle.
	 * @param parent A strong reference to the parent instance.
	 * @see Handler#handleMessage(Message)
	 */
	public abstract void handleMessage(Message message, T parent);

	private final WeakReference<T> weakParent;

}
